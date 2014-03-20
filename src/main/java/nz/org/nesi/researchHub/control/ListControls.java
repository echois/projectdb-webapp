/**
 * 
 */
package nz.org.nesi.researchHub.control;

import java.util.LinkedList;
import java.util.List;

import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pm.pojo.Affiliation;

/**
 * @author echoi
 * 
 */
public class ListControls extends AbstractControl {

	/**
	 * 
	 */
	private static final String SEPERATOR = " -- ";

	public static void main(final String[] args) throws Exception {

		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"rest-servlet.xml", "pm-servlet.xml", "signup-servlet.xml",
				"root-context.xml");

		final ListControls lc = (ListControls) context.getBean("listControls");

		for (final Affiliation af : lc.getAllAffiliations()) {
			// print out string and object both
		}

	}
	// Read
	/**
	 * Returns list of all affiliations.
	 * 
	 * @return all affiliations in the project database
	 */
	public List<Affiliation> getAllAffiliations() {

		List<Affiliation> afl = null;
		try {
			afl = projectDao.getAffiliations();
		} catch (final Exception e) {
			throw new DatabaseException("Can't get Affiliations.", e);
		}
		return afl;

	}
	
	public void createAffiliation(final Affiliation affiliation) throws InvalidEntityException {
		try {
			projectDao.createInstitution(affiliation);
			if(!affiliation.getDivision().isEmpty() ||!affiliation.getDivisionCode().isEmpty())
				projectDao.createDivision(affiliation);
			if(!affiliation.getDepartment().isEmpty()||!affiliation.getDepartmentCode().isEmpty())			
				projectDao.createDepartment(affiliation);
		} catch (final Exception e) {
			throw new DatabaseException("Can't create affiliation '"+ affiliation.getInstitution() + "'", e);
		}
	}
}
