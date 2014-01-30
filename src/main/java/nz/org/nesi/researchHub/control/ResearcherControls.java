package nz.org.nesi.researchHub.control;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;
import nz.org.nesi.researchHub.exceptions.OutOfDateException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pm.pojo.Affiliation;
import pm.pojo.InstitutionalRole;
import pm.pojo.Project;
import pm.pojo.ProjectWrapper;
import pm.pojo.Researcher;
import pm.pojo.ResearcherRole;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 5/12/13
 * Time: 11:33 AM
 */
public class ResearcherControls extends AbstractControl {

    public static void main(String[] args) throws Exception {

        ApplicationContext context =
                new ClassPathXmlApplicationContext( "rest-servlet.xml", "pm-servlet.xml", "signup-servlet.xml", "root-context.xml");

//        for ( String bean : context.getBeanDefinitionNames() ) {
//            if ( bean.contains("ntrols") ) {
//                System.out.println(bean);
//            }
//        }

        ResearcherControls rc = (ResearcherControls) context.getBean("researcherControls");

        for ( Researcher r : rc.getAllResearchers() ) {

            System.out.println(r.getId() + " : " + rc.getProjectsForResearcher(r.getId()).size());
        }


    }

    public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Validates the researcher object.
     *
     * @param a the project wrapper
     * @throws InvalidEntityException if there is something wrong with the researcher object
     */
    private void validateResearcher(Researcher r) throws InvalidEntityException {
		if (r.getFullName().trim().equals("")) {
			throw new InvalidEntityException("Researcher name cannot be empty", Researcher.class, "name");
		}
		for (Researcher other:getAllResearchers()) {
			if (r.getFullName().equals(other.getFullName()) && (r.getId()==null || !r.getId().equals(other.getId()))) {
				throw new InvalidEntityException(r.getFullName() + " already exists in the database", Researcher.class, "name");
			}
		}
	}

    /**
     * Returns the researcher with the specified id.
     *
     * @param id the researchers' id
     * @return the researcher object
     * @throws NoSuchEntityException if the researcher or his projects can't be found
     * @throws DatabaseException if there is researcher problem with the database
     */
	public Researcher getResearcher(Integer id) throws NoSuchEntityException {

    	if (id==null) {
            throw new IllegalArgumentException("No researcher id provided");
        }

        Researcher r = null;
        try {
            r = projectDao.getResearcherById(id);
        } catch (NullPointerException npe) {
            throw new NoSuchEntityException("Can't find researcher with id "+id, Researcher.class, id);
        } catch (Exception e) {
            throw new DatabaseException("Can't find researcher with id "+id, e);
        }

        return r;
	}

    /**
     * Returns researcher list of all projects for this researcher.
     *
     * Note: this method returns an empty List if the specified researcher does not exist
     *
     * @param researcherId the researchers' id
     * @return the list of projects
     * @throws DatabaseException if there is researcher problem retrieving the projects
     */
    public List<Project> getProjectsForResearcher(int researcherId) {
        List<Project> ps = null;
        try {
            ps = projectDao.getProjectsForResearcherId(researcherId);
        } catch (Exception e) {
            throw new DatabaseException("Can't get projects for researcher with id "+researcherId, e);
        }
        return ps;
    }

    /**
     * Returns researcher list of all researchers.
     *
     * @return all researchers in the project database
     */
	public List<Researcher> getAllResearchers() {

        List<Researcher> rl = null;
        try {
            rl = projectDao.getResearchers();
        } catch (Exception e) {
            throw new DatabaseException("Can't get researchers.", e);
        }

        return rl;
	}


    /**
     * Delete the Researcher with the specified id.
     *
     * @param id the researchers' id
     */
	public void delete(Integer id) {
        try {
            this.projectDao.deleteResearcher(id);
        } catch (Exception e) {
            throw new DatabaseException("Can't delete researcher with id "+id, e);
        }
    }

    /**
     * Replaces an existing researcher object with new properties.
     *
     * The provided Researcher object needs to have an id, otherwise it can't be matched with an existing on in the database.
     *
     * @param researcher the updated researcher object
     * @throws NoSuchEntityException if no Researcher with the specified
     * @throws InvalidEntityException if updated Researcher object doesn't have an id specified
     * @throws OutOfDateException 
     */
	public void editResearcher(Integer id, Researcher researcher) throws NoSuchEntityException, InvalidEntityException, OutOfDateException {
        validateResearcher(researcher);
		if (id != null) {
            // check whether an researcher with this id exists
            Researcher temp = getResearcher(researcher.getId());
            // great, no exception, means an researcher with this id does already exist, now let's merge those two
            researcher.setId(id);
            // Compare timestamps to prevent accidental overwrite
            if (!researcher.getLastModified().equals(temp.getLastModified())) {
            	throw new OutOfDateException("Incorrect timestamp");
            }
            researcher.setLastModified((int) (System.currentTimeMillis() / 1000));
            try {
                projectDao.updateResearcher(researcher);
            } catch (Exception e) {
                throw new DatabaseException("Can't update researcher with id "+researcher.getId(), e);
            }
		} else {
            throw new InvalidEntityException("Can't edit researcher. No id provided.", Researcher.class, "id");
        }
	}
	
	/**
     * Edit one field of a researcher.
     *
     * The provided Researcher object needs to have an id, otherwise it can't be matched with an existing on in the database.
     *
     * @param researcher the updated researcher object
     * @throws NoSuchEntityException if no Researcher with the specified
     * @throws InvalidEntityException if updated Researcher object doesn't have an id specified
     * @throws OutOfDateException 
     */
	public void editResearcher(Integer id, String field, String timestamp, String data) throws NoSuchEntityException, InvalidEntityException, OutOfDateException {
		if (id != null) {
            // check whether an researcher with this id exists
            Researcher temp = getResearcher(id);
            // great, no exception, means an researcher with this id does already exist, now let's merge those two
            // Compare timestamps to prevent accidental overwrite
            boolean force = timestamp.equals("force");
            if (!force && !timestamp.equals(temp.getLastModified())) {
            	throw new OutOfDateException("Incorrect timestamp. Researcher has been modified since you last loaded it.");
            }
            try {
            	String method = "set" + field;
            	Class<Researcher> c = Researcher.class;
            	try {
            		// Try use the parameter as an integer
            		Integer intData = Integer.valueOf(data);
            		Method set = c.getDeclaredMethod (method, Integer.class);
		            set.invoke (temp, intData);
            	} catch (Exception e) {
            		// String fallback
            		Method set = c.getDeclaredMethod (method, String.class);
		            set.invoke (temp, data);
            	}
            	validateResearcher(temp);
                projectDao.updateResearcher(temp);
            } catch (Exception e) {
                throw new DatabaseException("Can't update researcher with id "+id, e);
            }
		} else {
            throw new InvalidEntityException("Can't edit researcher. No id provided.", Researcher.class, "id");
        }
	}

    /**
     * Creates researcher new researcher.
     *
     * The Researcher object can't have an id specified, since that gets auto-generated at researcher lower level.
     * @param researcher the new Researcher
     * @throws InvalidEntityException if the new Researcher object has already an id specified
     */
    public void createResearcher(Researcher researcher) throws InvalidEntityException {
    	validateResearcher(researcher);
        if ( researcher.getId() != null ) {
            throw new InvalidEntityException("Researcher can't have id, this property will be auto-generated.", Researcher.class, "id");
        }
        try {
            if (StringUtils.isEmpty(researcher.getStartDate()) ) {
                researcher.setStartDate(df.format(new Date()));
            }
            researcher.setLastModified((int) (System.currentTimeMillis() / 1000));
            this.projectDao.createResearcher(researcher);
        } catch (Exception e) {
            throw new DatabaseException("Can't create Researcher '"+ researcher.getFullName()+"'", e);
        }
    }
    

    
    /**
     * Returns a list of Affiliations.
     *
     * @return a list of Affiliations
     * @throws Exception 
     */
    public List<Affiliation> getAffiliations() throws Exception {
    	return this.projectDao.getAffiliations();
    }
    
    /**
     * Returns a list of InstitutionalRoles.
     *
     * @return a list of InstitutionalRoles
     * @throws Exception 
     */
    public List<InstitutionalRole> getInstitutionalRoles() throws Exception {
    	return this.projectDao.getInstitutionalRoles();
    }
    
    /**
     * Returns a list of ResearcherRoles.
     *
     * @return a list of ResearcherRoles
     * @throws Exception 
     */
    public List<ResearcherRole> getResearcherRoles() throws Exception {
    	return this.projectDao.getResearcherRoles();
    }
    
    /**
     * Returns the user's linux username.
     *
     * @return a string
     * @throws Exception 
     */
    public String getLinuxUsernameForResearcher(Integer id) throws Exception {
    	return this.projectDao.getLinuxUsername(id);
    }

}
