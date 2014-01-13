package nz.org.nesi.researchHub.control;

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

import pm.pojo.Adviser;
import pm.pojo.AdviserRole;
import pm.pojo.Affiliation;
import pm.pojo.Project;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 5/12/13
 * Time: 11:33 AM
 */
public class AdviserControls extends AbstractControl {

    public static void main(String[] args) throws Exception {

        ApplicationContext context =
                new ClassPathXmlApplicationContext( "rest-servlet.xml", "pm-servlet.xml", "signup-servlet.xml", "root-context.xml");

//        for ( String bean : context.getBeanDefinitionNames() ) {
//            if ( bean.contains("ntrols") ) {
//                System.out.println(bean);
//            }
//        }

        AdviserControls ac = (AdviserControls) context.getBean("adviserControls");

//        Adviser adviser = ac.getAdviser(14444);
//
//        System.out.println(adviser);


        for ( Adviser a : ac.getAllAdvisers() ) {

            System.out.println(a.getId() + " : " + ac.getProjectsForAdviser(a.getId()).size());
        }


    }

    public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");


//    public AdviserControls(ProjectDao o) {
//        this.projectDao = o;
//    }
    
    /**
     * Validates the adviser object.
     *
     * @param a the project wrapper
     * @throws InvalidEntityException if there is something wrong with the adviser object
     */
    private void validateAdviser(Adviser a) throws InvalidEntityException {
		if (a.getFullName().trim().equals("")) {
			throw new InvalidEntityException("Adviser name cannot be empty", Adviser.class, "name");
		}
		for (Adviser other:getAllAdvisers()) {
			if (a.getFullName().equals(other.getFullName()) && (a.getId()==null || !a.getId().equals(other.getId()))) {
				throw new InvalidEntityException(a.getFullName() + " already exists in the database", Adviser.class, "name");
			}
		}
	}

    /**
     * Returns the adviser with the specified id.
     *
     * @param id the advisers' id
     * @return the advisor object
     * @throws NoSuchEntityException if the adviser or his projects can't be found
     * @throws DatabaseException if there is adviser problem with the database
     */
	public Adviser getAdviser(Integer id) throws NoSuchEntityException {

    	if (id==null) {
            throw new IllegalArgumentException("No adviser id provided");
        }

        Adviser a = null;
        try {
            a = projectDao.getAdviserById(id);
        } catch (NullPointerException npe) {
            throw new NoSuchEntityException("Can't find advisor with id "+id, Adviser.class, id);
        } catch (Exception e) {
            throw new DatabaseException("Can't find adviser with id "+id, e);
        }

        return a;
	}

    /**
     * Returns adviser list of all projects for this adviser.
     *
     * Note: this method returns an empty List if the specified advisor does not exist
     *
     * @param advisorId the advisers' id
     * @return the list of projects
     * @throws DatabaseException if there is adviser problem retrieving the projects
     */
    public List<Project> getProjectsForAdviser(int advisorId) {
        List<Project> ps = null;
        try {
            ps = projectDao.getProjectsForAdviserId(advisorId);
        } catch (Exception e) {
            throw new DatabaseException("Can't get projects for adviser with id "+advisorId, e);
        }
        return ps;
    }

    /**
     * Returns adviser list of all advisers.
     *
     * @return all advisors in the project database
     */
	public List<Adviser> getAllAdvisers() {

        List<Adviser> al = null;
        try {
            al = projectDao.getAdvisers();
        } catch (Exception e) {
            throw new DatabaseException("Can't get advisers.", e);
        }

        return al;
	}


    /**
     * Delete the Adviser with the specified id.
     *
     * @param id the advisers' id
     */
	public void delete(Integer id) {
        try {
            this.projectDao.deleteAdviser(id);
        } catch (Exception e) {
            throw new DatabaseException("Can't delete advisor with id "+id, e);
        }
    }

    /**
     * Replaces an existing adviser object with new properties.
     *
     * The provided Adviser object needs to have an id, otherwise it can't be matched with an existing on in the database.
     *
     * @param adviser the updated adviser object
     * @throws NoSuchEntityException if no Adviser with the specified
     * @throws InvalidEntityException if updated Adviser object doesn't have an id specified
     * @throws OutOfDateException 
     */
	public void editAdviser(Integer id, Adviser adviser) throws NoSuchEntityException, InvalidEntityException, OutOfDateException {
        validateAdviser(adviser);
		if (id != null) {
            // check whether an adviser with this id exists
            Adviser temp = getAdviser(adviser.getId());
            // great, no exception, means an adviser with this id does already exist, now let's merge those two
            adviser.setId(id);
            // Compare timestamps to prevent accidental overwrite
            if (!adviser.getLastModified().equals(temp.getLastModified())) {
            	throw new OutOfDateException("Incorrect timestamp");
            }
            adviser.setLastModified((int) (System.currentTimeMillis() / 1000));
            try {
                projectDao.updateAdviser(adviser);
            } catch (Exception e) {
                throw new DatabaseException("Can't update adviser with id "+adviser.getId(), e);
            }
		} else {
            throw new InvalidEntityException("Can't edit advisor. No id provided.", Adviser.class, "id");
        }
	}

    /**
     * Creates adviser new adviser.
     *
     * The Adviser object can't have an id specified, since that gets auto-generated at adviser lower level.
     * @param adviser the new Adviser
     * @throws InvalidEntityException if the new Adviser object has already an id specified
     */
    public void createAdviser(Adviser adviser) throws InvalidEntityException {
    	validateAdviser(adviser);
        if ( adviser.getId() != null ) {
            throw new InvalidEntityException("Adviser can't have id, this property will be auto-generated.", Adviser.class, "id");
        }
        try {
            if (StringUtils.isEmpty(adviser.getStartDate()) ) {
                adviser.setStartDate(df.format(new Date()));
            }
            adviser.setLastModified((int) (System.currentTimeMillis() / 1000));
            this.projectDao.createAdviser(adviser);
        } catch (Exception e) {
            throw new DatabaseException("Can't create Adviser '"+ adviser.getFullName()+"'", e);
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
     * Returns a list of AdviserRoles.
     *
     * @return a list of AdviserRoles
     * @throws Exception 
     */
    public List<AdviserRole> getAdviserRoles() throws Exception {
    	return this.projectDao.getAdviserRoles();
    }

}
