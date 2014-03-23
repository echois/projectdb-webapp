/**
 * 
 */
package nz.org.nesi.researchHub.control;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	private static final String SEPARATOR = " -- ";

	public static void main(final String[] args) throws Exception {

		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"rest-servlet.xml", "pm-servlet.xml", "signup-servlet.xml",
				"root-context.xml");

		final ListControls lc = (ListControls) context.getBean("listControls");

		for (final Affiliation af : lc.getAllAffiliations()) {
			// print out string and object both
		}

	}

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

	/**
	 * Returns list of affiliations on specific institution.
	 * 
	 * @return affiliations on specific institution in the project database
	 */
	public List<Affiliation> getAffiliationsByInstitutionCode(
			String institutionCode) {

		List<Affiliation> afl = null;
		try {
			afl = projectDao.getAffiliationsByInstitutionCode(institutionCode);
		} catch (final Exception e) {
			throw new DatabaseException("Can't get Affiliations.", e);
		}
		return afl;
	}

	/**
	 * Returns list of affiliations in string.
	 * 
	 * @return affiliations in string in the project database
	 */
	public List<String> getAffiliationStrings() throws Exception {
		final List<Affiliation> affiliations = projectDao.getAffiliations();
		final List<String> affiliationStrings = new LinkedList<String>();
		if (affiliations != null) {
			for (final Affiliation a : affiliations) {
				String tmp = a.getInstitution().trim();
				if (!a.getDivision().trim().isEmpty()) {
					tmp += SEPARATOR + a.getDivision().trim();
					if (!a.getDepartment().trim().isEmpty()) {
						tmp += SEPARATOR + a.getDepartment().trim();
					}
				}
				affiliationStrings.add(tmp);
			}
		}
		return affiliationStrings;
	}

	/**
	 * Returns list of division and division codes on specific institution.
	 * 
	 * @return division and division codes on specific institution in the
	 *         project database
	 */

	public List<Map<String, String>> getDivisionsByInstitutionCode(
			String institutionCode) {

		final List<Affiliation> affiliations = getAffiliationsByInstitutionCode(institutionCode);
		final List<Map<String, String>> divisionStrings = new LinkedList<Map<String, String>>();
		if (affiliations != null) {
			for (final Affiliation a : affiliations) {
				String di = "";
				String diCode = "";
				Map<String, String> divisionInfo = new HashMap<String, String>();
				if (!a.getDivision().isEmpty()
						&& !a.getDivisionCode().isEmpty()) {
					di += a.getDivision();
					diCode += a.getDivisionCode();
					divisionInfo.put(di, diCode);
				}
				divisionStrings.add(divisionInfo);
			}
		}
		return divisionStrings;
	}

	/**
	 * Returns list of affiliations on specific division.
	 * 
	 * @return affiliations on specific division in the project database
	 */
	public List<Affiliation> getAffiliationsByDivisionCode(String divisionCode) {

		List<Affiliation> afl = null;
		try {
			afl = projectDao.getAffiliationsByDivisionCode(divisionCode);
		} catch (final Exception e) {
			throw new DatabaseException("Can't get Affiliations.", e);
		}
		return afl;

	}

	/**
	 * Returns list of department and department codes on specific division.
	 * 
	 * @return department and department codes on specific division in the
	 *         project database
	 */

	public List<Map<String, String>> getDepartmentsByDivisionCode(
			String divisionCode) {

		final List<Affiliation> affiliations = getAffiliationsByDivisionCode(divisionCode);
		final List<Map<String, String>> departmentStrings = new LinkedList<Map<String, String>>();
		if (affiliations != null) {
			for (final Affiliation a : affiliations) {
				String de = "";
				String deCode = "";
				Map<String, String> departmentInfo = new HashMap<String, String>();
				if (!a.getDepartment().isEmpty()
						&& !a.getDepartmentCode().isEmpty()) {
					de += a.getDepartment();
					deCode += a.getDepartmentCode();
					departmentInfo.put(de, deCode);
				}
				departmentStrings.add(departmentInfo);
			}
		}
		return departmentStrings;
	}

	/**
	 * Creates affiliation new affiliation.
	 * 
	 * The Affiliation object can't have an id specified, since that gets
	 * auto-generated at affiliation lower level.
	 * 
	 * @param affiliation
	 *            the new Affiliation
	 * @throws InvalidEntityException
	 *             if the new Affiliation object has already an id specified
	 */
	public void createAffiliation(final Affiliation affiliation)
			throws InvalidEntityException {
		try {
			projectDao.createInstitution(affiliation);
			if (!affiliation.getDivision().isEmpty()
					|| !affiliation.getDivisionCode().isEmpty()) projectDao
					.createDivision(affiliation);
			if (!affiliation.getDepartment().isEmpty()
					|| !affiliation.getDepartmentCode().isEmpty()) projectDao
					.createDepartment(affiliation);
		} catch (final Exception e) {
			throw new DatabaseException("Can't create affiliation '"
					+ affiliation.getInstitution() + "'", e);
		}
	}
}
