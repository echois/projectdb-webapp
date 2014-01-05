/**
 * 
 */
package nz.org.nesi.researchHub.control;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;
import nz.org.nesi.researchHub.exceptions.OutOfDateException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pm.pojo.Project;
import pm.pojo.Researcher;
/**
 * @author echoi
 *
 */
@Controller
@RequestMapping(value="\researchers")
public class ResearcherControls extends AbstractControl{
	
	
	public static void main(String[] args) throws Exception{
		
		ApplicationContext context= new ClassPathXmlApplicationContext("rest-servlet.xml", "pm-servlet.xml","signup-servlet.xml","root-servlet.xml");
		
		ResearcherControls rc = (ResearcherControls) context.getBean("researcherControls");
		
        for ( Researcher r : rc.getAllResearchers() ) {

            
        }
	}

	/**
	 * 
	 */
	
	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public ResearcherControls() {
		// TODO Auto-generated constructor stub
	}
	
	private void validateResearcher(Researcher r) throws InvalidEntityException {
		if(r.getFullName().trim().equals("")){
			throw new InvalidEntityException("Researcher name cannot be empty", Researcher.class, "name" );
		}
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Researcher getResearcher(@PathVariable Integer id) throws NoSuchEntityException{
		
		if(id == null){
			throw new IllegalArgumentException("No researcher id provided");
		}
		Researcher r =null;
		try{
			r=projectDao.getResearcherById(id);			
		} catch (NullPointerException npe){
			throw new NoSuchEntityException("Can't find researcher with id"+id, Researcher.class, id);			
		} catch (Exception e){
			throw new DatabaseException("Can't find researcher with id"+ id, e);
		}
		return r;		
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	@ResponseBody
	public List<Researcher> getAllResearchers(){
		
		List<Researcher> rl = null;
		try{
			rl = projectDao.getResearchers();
		} catch (Exception e){
			throw new DatabaseException("Can't get researchers", e);			
		}
		
		return rl;
	}
	
	@RequestMapping(value="/{id}/projects", method=RequestMethod.GET)
	@ResponseBody
	public List<Project> getProjectsForResearcher(@PathVariable int researcherId) {
		List<Project> ps = null;
		try {
			ps = projectDao.getProjectsForResearcherId(researcherId);			
		} catch (Exception e){
			throw new DatabaseException("Can't get projects for resaercher with id"+ researcherId, e);
		}
		
		return ps;	
	
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable Integer id){
		try{
			this.projectDao.deleteResearcher(id);
		} catch(Exception e) {
			throw new DatabaseException("Can't delete researcher with id"+id, e);
		}
	}

	@RequestMapping(value="/{id}", method=RequestMethod.POST)
	@ResponseBody
	public void editResearcher(@PathVariable Integer id, Researcher researcher) throws NoSuchEntityException, InvalidEntityException, OutOfDateException{
		validateResearcher(researcher);
		if(id!=null){
			// check whether a researcher with this id exists
			Researcher res = getResearcher(researcher.getId());
			
			researcher.setId(id);
			
			// Compare timestamps to prevent accidental overwrite
			if(!researcher.getLastModified().equals(res.getLastModified())){
				throw new OutOfDateException("Incorrect timestamp");
			}
			researcher.setLastModified((int) (System.currentTimeMillis()/1000));
			try{
				projectDao.updateResearcher(researcher);
			}
			catch(Exception e){
				throw new DatabaseException("Can't update researcher with id"+researcher.getId(), e);
			}
		} else {
			throw new InvalidEntityException("Can't edit researcher. No id provided", Researcher.class, "id");
		}
	}
	
	@RequestMapping(value="/", method=RequestMethod.PUT)
	@ResponseBody
	public void createResearcher(Researcher researcher) throws InvalidEntityException {
		validateResearcher(researcher);
		if(researcher.getId() != null){
			throw new InvalidEntityException("Researcher can't have id, this property will be auto-generated", Researcher.class, "id");
		}
		try{
			if(StringUtils.isEmpty(researcher.getStartDate())){
				researcher.setStartDate(df.format(new Date()));
			}
			researcher.setLastModified((int) (System.currentTimeMillis()/1000));
			this.projectDao.createResearcher(researcher);			
		} catch (Exception e){
			throw new DatabaseException("Can't create Researcher '" + researcher.getFullName()+"'",e);
		}
	}
	
}
