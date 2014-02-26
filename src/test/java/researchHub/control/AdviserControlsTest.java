/**
 * 
 */
package researchHub.control;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import nz.org.nesi.researchHub.control.AdviserControls;
import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;
import nz.org.nesi.researchHub.exceptions.OutOfDateException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pm.db.ProjectDao;
import pm.pojo.Adviser;
import pm.pojo.AdviserRole;
import pm.pojo.Affiliation;
import pm.pojo.Project;

/**
 * @author echoi
 * 
 */
@Repository(value = "ProjectDao")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/Controls-context.xml" })
public class AdviserControlsTest {

	/**
	 * @throws java.lang.Exception
	 */

	@InjectMocks
	private AdviserControls adviserControls;
	@InjectMocks
	private final ProjectDao projectDaoMock = Mockito.mock(ProjectDao.class);

	private Adviser adviser;
	private Project project;
	private List<Project> projects;

	@Before
	public void setUp() throws Exception {

		// Provide minimum information to create an adviser.
		adviser = new Adviser() {
			{
				setFullName("TestName");
			}
		};

		project = new Project() {
			{
				setName("TestName");
				setProjectCode("");
				setHostInstitution("");

			}
		};

		projects = new LinkedList<Project>();
		projects.add(project);

		adviserControls = new AdviserControls() {
			{
				projectDao = projectDaoMock;
			}
		};

	}

	@Test
	public void testGetAdviserById() throws Exception {

		String expectedFullname = "TestName";
		when(adviserControls.getAdviser(1)).thenReturn(adviser);
		assertEquals(expectedFullname, adviserControls.getAdviser(1)
				.getFullName());
	}

	@Test
	public void testGetProjectsForAdviser() throws Exception {

		when(adviserControls.getAdviser(1)).thenReturn(adviser);
		when(adviserControls.getProjectsForAdviser(1)).thenReturn(projects);
	}

	@Test
	public void testGetAllAdvisers() throws Exception {

		List<Adviser> all = new LinkedList<Adviser>();
		all.add(new Adviser() {
			{
				setFullName("TestName1");
			}
		});
		all.add(new Adviser() {
			{
				setFullName("TestName2");
			}
		});

		adviserControls.getAllAdvisers();
		verify(projectDaoMock).getAdvisers();

	}

	@Test
	public void testGetAdviserRoles() throws Exception {
		AdviserRole adviserRole = new AdviserRole() {
			{
				setName("Primary Adviser");
			}
		};
		List<AdviserRole> adviserRoles = new LinkedList<AdviserRole>();
		adviserRoles.add(adviserRole);

		adviserControls.getAdviserRoles();
		verify(projectDaoMock).getAdviserRoles();
	}

	@Test
	public void testGetAffiliations() throws Exception {
		Affiliation affiliation = new Affiliation() {
			{
				setInstitution("The University of Auckland");
			}
		};
		List<Affiliation> affiliations = new LinkedList<Affiliation>();
		affiliations.add(affiliation);

		adviserControls.getAffiliations();
		verify(projectDaoMock).getAffiliations();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetInvalidAdviser() throws Exception {

		adviserControls.getAdviser(null);
	}

	@Test
	public void testCreateAdviserSuccessfully() throws Exception {

		when(projectDaoMock.createAdviser(adviser)).thenReturn(1);

		adviserControls.createAdviser(adviser);
		verify(projectDaoMock).createAdviser(adviser);
	}

	@Test(expected = InvalidEntityException.class)
	public void testMissingFullName() throws Exception {

		Adviser newadviser = new Adviser() {
			{
				setFullName("");
			}
		};
		when(projectDaoMock.createAdviser(newadviser)).thenReturn(1);

		adviserControls.createAdviser(newadviser);
	}

	@Test(expected = InvalidEntityException.class)
	public void testDuplicateAdviserFullName() throws Exception {

		List<Adviser> all = new LinkedList<Adviser>();
		all.add(new Adviser() {
			{
				setFullName("TestName");
			}
		});
		all.add(new Adviser() {
			{
				setFullName("TestName2");
			}
		});

		adviserControls.getAllAdvisers();
		when(adviserControls.getAllAdvisers()).thenReturn(all);

		adviserControls.createAdviser(adviser);
	}

	@Test(expected = InvalidEntityException.class)
	public void testCreateAdviserWithId() throws Exception {

		Adviser newadviser = new Adviser() {
			{
				setFullName("TestNewName");
				setId(1);
			}
		};
		when(projectDaoMock.getAdviserById(1)).thenReturn(newadviser);

		adviserControls.createAdviser(newadviser);
	}

	@Test
	public void testAllowDuplicateFullNameAsNewAdviser() throws Exception {

		List<Adviser> all = new LinkedList<Adviser>();
		all.add(new Adviser() {
			{
				setFullName("TestName");
			}
		});
		all.add(new Adviser() {
			{
				setFullName("New Adviser");
			}
		});

		Adviser newadviser = new Adviser() {
			{
				setFullName("New Adviser");
			}
		};

		// Mock alert: return mocked result set on find
		try {
			when(projectDaoMock.getAdvisers()).thenReturn(all);
		} catch (Exception e) {
			e.printStackTrace();
		}

		when(adviserControls.getAllAdvisers()).thenReturn(all);

		adviserControls.createAdviser(newadviser);
	}

	@Test
	public void testGetAdviserByDrupalId() throws Exception {

		String expectedFullname = "TestName";
		when(projectDaoMock.getAdviserByDrupalId("1")).thenReturn(adviser);
		when(adviserControls.getAdviserByDrupalId("1")).thenReturn(adviser);

		assertEquals(expectedFullname, adviserControls
				.getAdviserByDrupalId("1").getFullName());
	}

	@Test(expected = NoSuchEntityException.class)
	public void testGetAdviserByDrupalIdWithInvalidData() throws Exception {

		when(projectDaoMock.getAdviserByDrupalId("abc")).thenReturn(adviser);
		when(adviserControls.getAdviserByDrupalId("abc")).thenReturn(adviser);
		adviserControls.getAdviserByDrupalId("123");
	}

	@Test(expected = InvalidEntityException.class)
	public void testEditAdviserWithNoId() throws Exception {

		when(adviserControls.getAdviser(1)).thenReturn(adviser);
		adviserControls.editAdviser(adviser);
		verify(projectDaoMock).updateAdviser(adviser);

	}

	@Test
	public void testEditAdviser() throws Exception {
		adviser.setId(1);
		adviser.setLastModified("01/01/2014");

		when(adviserControls.getAdviser(1)).thenReturn(adviser);

		adviserControls.editAdviser(adviser);

		verify(projectDaoMock).updateAdviser(adviser);

	}

	@Test(expected = NullPointerException.class)
	public void testEditAdviserWithIncorrectId() throws Exception {

		adviser.setId(1);
		adviser.setLastModified("1/1/2014");

		when(adviserControls.getAdviser(1)).thenReturn(adviser);

		adviserControls.editAdviser(2, "FullName", "1/1/2014", "TestName");
		verify(projectDaoMock).updateAdviser(adviser);
	}

	@Test(expected = DatabaseException.class)
	public void testEditAdviserWithData() throws Exception {

		adviser.setId(1);
		adviser.setLastModified("1/1/2014");

		when(adviserControls.getAdviser(1)).thenReturn(adviser);

		// Try to update the field which has no data provided for the adviser.
		adviserControls.editAdviser(1, "LastName", "1/1/2014", "TestName");
		verify(projectDaoMock).updateAdviser(adviser);
	}

	@Test(expected = OutOfDateException.class)
	public void testEditAdviserWithIncorrectTimestamp() throws Exception {

		adviser.setId(1);
		adviser.setLastModified("1/1/2014");

		when(adviserControls.getAdviser(1)).thenReturn(adviser);

		// Try to update the field with incorrect timestamp
		adviserControls.editAdviser(1, "FullName", "1/2/2014", "TestName");
		verify(projectDaoMock).updateAdviser(adviser);
	}

	@Test(expected = NullPointerException.class)
	public void testDeleteAdviser() throws Exception {
		adviser.setId(1);

		when(adviserControls.getAdviser(1)).thenReturn(adviser);

		adviserControls.delete(1);
		verify(projectDaoMock).deleteAdviser(1);

		// Try edit adviser to see if adviser is completely removed
		adviserControls.editAdviser(adviser);
	}

	@Test(expected = ArgumentsAreDifferent.class)
	public void testDeleteIncorrectAdviser() throws Exception {

		adviserControls.createAdviser(adviser);
		adviser.setId(1);
		when(adviserControls.getAdviser(1)).thenReturn(adviser);

		adviserControls.delete(2);
		verify(projectDaoMock).deleteAdviser(1);
		// Try edit adviser to see if adviser is completely removed
		adviserControls.editAdviser(adviser);
	}
}
