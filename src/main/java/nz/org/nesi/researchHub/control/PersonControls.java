package nz.org.nesi.researchHub.control;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import pm.pojo.Person;

public class PersonControls extends AbstractControl {

	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(final String[] args) throws Exception {

		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"rest-servlet.xml", "pm-servlet.xml", "signup-servlet.xml",
				"root-context.xml");

		final PersonControls pc = (PersonControls) context
				.getBean("personControls");

		for (final Person p : pc.getAllPersons()) {
			System.out.println(p.getId() + " : ");
		}

	}

	/**
	 * Creates new person.
	 * 
	 * The Person object can't have an id specified, since that gets
	 * auto-generated at lower level.
	 * 
	 * @param person
	 *            the new Person
	 * @throws Exception
	 * 
	 */
	public Integer createPerson(final Person person)
			throws InvalidEntityException {
		if (person.getId() != null) {
			throw new InvalidEntityException(
					"Person can't have id, this property will be auto-generated.",
					Person.class, "id");
		}
		try {
			if (StringUtils.isEmpty(person.getStartDate())) {
				person.setStartDate(df.format(new Date()));
			}
			return projectDao.createPerson(person);
		} catch (final Exception e) {
			throw new DatabaseException("Can't create Person '"
					+ person.getFullName() + "'", e);
		}
	}

	/**
	 * Returns person list of all persons.
	 * 
	 * @return all persons in the project database
	 */
	public List<Person> getAllPersons() {

		List<Person> al = null;
		try {
			al = projectDao.getPersons();
		} catch (final Exception e) {
			throw new DatabaseException("Can't get persons.", e);
		}

		return al;
	}

	/**
	 * Returns the person with the specified id.
	 * 
	 * @param id
	 *            the persons' id
	 * @return the advisor object
	 * @throws NoSuchEntityException
	 *             if the person or his projects can't be found
	 * @throws DatabaseException
	 *             if there is person problem with the database
	 */
	public Person getPerson(final Integer id) throws NoSuchEntityException {

		if (id == null) {
			throw new IllegalArgumentException("No person id provided");
		}

		Person p = null;
		try {
			p = projectDao.getPersonById(id);
		} catch (final NullPointerException npe) {
			throw new NoSuchEntityException("Can't find person with id " + id,
					Person.class, id, npe);
		} catch (final Exception e) {
			throw new DatabaseException("Can't find person with id " + id, e);
		}

		return p;
	}

	// TODO merge validation from researcher and adviser

	/*
	 * /** Validates the adviser object.
	 * 
	 * @param a the adviser object
	 * 
	 * @throws InvalidEntityException if there is something wrong with the
	 * adviser object
	 */
	/*
	 * private void validatePerson(final Person p) throws InvalidEntityException
	 * { if (p.getFullName() == null || p.getFullName().trim().equals("")) {
	 * throw new InvalidEntityException("Person name cannot be empty",
	 * Person.class, "name"); } if (p.getFullName().equals("New Adviser")) {
	 * return; } if (p.getEmail() == null || p.getEmail().trim().equals("") ||
	 * !a.getEmail().matches(".+@.+[.].+")) { throw new
	 * InvalidEntityException("A valid email is required", Adviser.class,
	 * "email"); } if (p.getPhone() == null || p.getPhone().trim().equals("") ||
	 * !p.getPhone().matches(".+[0-9].+")) { throw new InvalidEntityException(
	 * "Phone must contain at least one digit", Person.class, "phone"); } for
	 * (final Person other : getAllPersons()) { if
	 * (p.getFullName().equals(other.getFullName()) && (p.getId() == null ||
	 * !p.getId().equals(other.getId()))) { throw new
	 * InvalidEntityException(p.getFullName() +
	 * " already exists in the database", Person.class, "name"); } } }
	 */

}
