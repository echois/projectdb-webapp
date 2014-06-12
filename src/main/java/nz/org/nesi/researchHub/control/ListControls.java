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
	 * Returns list of affiliations on specific department.
	 * 
	 * @return affiliations on specific department in the project database
	 */
	public List<Affiliation> getAffiliationsByDepartmentCode(
			String departmentCode) {

		List<Affiliation> afl = null;
		try {
			afl = projectDao.getAffiliationsByDepartmentCode(departmentCode);
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
	 * Validates the affiliation object.
	 * 
	 * @param a
	 *            the affiliation object
	 * @throws InvalidEntityException
	 *             if there is something wrong with the affiliation object
	 */
	private void validateAffiliation(final Affiliation af)
			throws InvalidEntityException {

		Boolean emptyInstName = (af.getInstitution() == null || af
				.getInstitution().trim().equals(""));
		Boolean emptyInstCode = (af.getInstitutionCode() == null || af
				.getInstitutionCode().trim().equals(""));
		Boolean emptyDiviName = (af.getDivision().isEmpty() || af.getDivision()
				.trim().equals(""));
		Boolean emptyDiviCode = (af.getDivisionCode().isEmpty() || af
				.getDivisionCode().trim().equals(""));
		Boolean emptyDeptName = (af.getDepartment().isEmpty() || af
				.getDepartment().trim().equals(""));
		Boolean emptyDeptCode = (af.getDepartmentCode().isEmpty() || af
				.getDepartmentCode().trim().equals(""));

		// Affiliation should have at least institution information
		if (emptyInstName) {
			throw new InvalidEntityException(
					"Institution name cannot be empty", Affiliation.class,
					"institution");
		}
		if (emptyInstCode) {
			throw new InvalidEntityException(
					"Institution code cannot be empty", Affiliation.class,
					"institutionCode");
		}

		// If there is department information, division should be entered
		if (!emptyDeptName && !emptyDeptCode) {
			if (emptyDiviName) throw new InvalidEntityException(
					"Division name cannot be empty", Affiliation.class,
					"division");
			else if (emptyDiviCode) throw new InvalidEntityException(
					"Division code cannot be empty", Affiliation.class,
					"divisionCode");
		}
		// If there is division name, division code is mandatory
		if (!emptyDiviName && emptyDiviCode) {
			throw new InvalidEntityException("Division code cannot be empty",
					Affiliation.class, "divisionCode");
		}

		// If there is department name, department code is mandatory
		if (!emptyDeptName && emptyDeptCode) {
			throw new InvalidEntityException("Department code cannot be empty",
					Affiliation.class, "departmentCode");
		}

		// Institution, division and department codes to be unique
		for (final Affiliation other : getAllAffiliations()) {
			// To create a institution
			// Institution code should be unique
			if (af.getInstitutionCode().equals(other.getInstitutionCode())
					&& emptyDiviName) {
				throw new InvalidEntityException(af.getInstitution()
						+ " already exists in the database", Affiliation.class,
						"institutionCode");
			}

			// To create a division
			// Division code should be unique
			if (!emptyDiviName) {
				if (af.getDivisionCode().equals(other.getDivisionCode())
						&& emptyDeptName) throw new InvalidEntityException(
						af.getDivision() + " already exists in the database",
						Affiliation.class, "divisionCode");
			}

			// To create a department
			// Department code should be unique
			if (!emptyDeptName) {
				if (af.getDepartmentCode().equals(other.getDepartmentCode())) throw new InvalidEntityException(
						af.getDepartment() + " already exists in the database",
						Affiliation.class, "DepartmentCode");
			}
		}
	}

	/**
	 * Creates new affiliation.
	 * 
	 * The Affiliation object can't have an id specified, since that gets
	 * auto-generated at affiliation lower level.
	 * 
	 * @param affiliation
	 *            the new Affiliation
	 * @throws Exception
	 * 
	 */
	public void createAffiliation(final Affiliation affiliation)
			throws Exception {
		validateAffiliation(affiliation);
		try {
			createInstitution(affiliation);

		} catch (final Exception e) {
			throw new DatabaseException("Can't create affiliation '"
					+ affiliation.getInstitution() + "'", e);
		}
	}

	/**
	 * Creates new Institution.
	 * 
	 * The Institution object can't have an id specified, since that gets
	 * auto-generated at lower level.
	 * 
	 * @param affiliation
	 *            the new Affiliation
	 * @throws Exception
	 */
	public void createInstitution(final Affiliation affiliation)
			throws InvalidEntityException {
		try {

			// If there is no existing institution with the institution code
			if (getAffiliationsByInstitutionCode(
					affiliation.getInstitutionCode()).isEmpty()) projectDao
					.createInstitution(affiliation);

			// If there are division name and code provided
			if (!affiliation.getDivision().isEmpty()
					|| !affiliation.getDivisionCode().isEmpty()) createDivision(affiliation);

		} catch (final Exception e) {
			throw new DatabaseException("Can't create institution '"
					+ affiliation.getInstitution() + ", "
					+ affiliation.getInstitutionCode() + "'", e);
		}
	}

	/**
	 * Creates new Division.
	 * 
	 * The Division object can't have an id specified, since that gets
	 * auto-generated at lower level.
	 * 
	 * @param affiliation
	 *            the new Affiliation
	 * @throws Exception
	 */
	public void createDivision(final Affiliation affiliation)
			throws InvalidEntityException {
		try {
			// If there is no existing division with the institution code
			if (getAffiliationsByDivisionCode(affiliation.getDivisionCode())
					.isEmpty()) projectDao.createDivision(affiliation);

			// If there are department name and code provided
			if (!affiliation.getDepartment().isEmpty()
					|| !affiliation.getDepartmentCode().isEmpty()) createDepartment(affiliation);

		} catch (final Exception e) {
			throw new DatabaseException("Can't create division '"
					+ affiliation.getDivision() + ", "
					+ affiliation.getDivisionCode() + "'", e);
		}
	}

	/**
	 * Creates new Department.
	 * 
	 * The Department object can't have an id specified, since that gets
	 * auto-generated at lower level.
	 * 
	 * @param affiliation
	 *            the new Affiliation
	 * @throws Exception
	 */
	public void createDepartment(final Affiliation affiliation)
			throws InvalidEntityException {
		try {
			// If there is no existing department with the division code
			if (getAffiliationsByDepartmentCode(affiliation.getDepartmentCode())
					.isEmpty()) projectDao.createDepartment(affiliation);
		} catch (final Exception e) {
			throw new DatabaseException("Can't create department '"
					+ affiliation.getDepartment() + ", "
					+ affiliation.getDepartmentCode() + "'", e);
		}

	}

}
