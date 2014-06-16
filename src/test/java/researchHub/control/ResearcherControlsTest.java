/**
 * 
 */
package researchHub.control;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import nz.org.nesi.researchHub.control.ResearcherControls;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
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

import pm.authz.AuthzAspect;
import pm.db.ProjectDao;
import pm.pojo.InstitutionalRole;
import pm.pojo.Project;
import pm.pojo.Researcher;
import pm.pojo.ResearcherRole;

import com.google.common.collect.Lists;

/**
 * @author echoi
 * 
 */
@Repository(value = "ProjectDao")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/Controls-context.xml" })
public class ResearcherControlsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@InjectMocks
	private ResearcherControls researcherControls;
	@InjectMocks
	private final ProjectDao projectDaoMock = Mockito.mock(ProjectDao.class);
	private final AuthzAspect authzAspectMock = Mockito.mock(AuthzAspect.class);

	private Researcher researcher;
	private Project project;
	private List<Project> projects;

	@Before
	public void setUp() throws Exception {

		// provide minimum information to create a researcher.
		researcher = new Researcher() {
			{
				setFullName("TestName");
				setPhone("09000000");
				setEmail("test@auckland.ac.nz");
				setInstitutionalRoleId(1);
				setStatusId(1);
				setPictureUrl("http://img1.wikia.nocookie.net/__cb20140207172458/simpsons/images/6/65/Bart_Simpson.png");

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

		researcherControls = new ResearcherControls() {
			{
				projectDao = projectDaoMock;
				authzAspect = authzAspectMock;
			}
		};
	}

	@Test
	public void testGetResearcherById() throws Exception {

		String expectedFullname = "TestName";
		when(researcherControls.getResearcher(1)).thenReturn(researcher);
		assertEquals(expectedFullname, researcherControls.getResearcher(1)
				.getFullName());
	}

	@Test
	public void testGetProjectsForResearcher() throws Exception {

		when(researcherControls.getResearcher(1)).thenReturn(researcher);
		when(researcherControls.getProjectsForResearcher(1)).thenReturn(
				projects);
	}

	@Test
	public void testGetAllResearchers() throws Exception {

		List<Researcher> all = new LinkedList<Researcher>();
		all.add(new Researcher() {
			{
				setFullName("TestName1");
			}
		});
		all.add(new Researcher() {
			{
				setFullName("TestName2");
			}
		});

		researcherControls.getAllResearchers();
		verify(projectDaoMock).getResearchers();

	}

	@Test
	public void testGetResearcherRoles() throws Exception {
		ResearcherRole researcherRole = new ResearcherRole() {
			{
				setName("Primary Researcher");
			}
		};
		List<ResearcherRole> researcherRoles = new LinkedList<ResearcherRole>();
		researcherRoles.add(researcherRole);

		researcherControls.getResearcherRoles();
		verify(projectDaoMock).getResearcherRoles();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetInvalidResearcher() throws Exception {

		researcherControls.getResearcher(null);
	}

	@Test
	public void testCreateResearcherSuccessfully() throws Exception {

		InstitutionalRole ir = new InstitutionalRole();
		ir.setId(1);
		ir.setName("StupidRole");
		when(researcherControls.getInstitutionalRoles()).thenReturn(
				Lists.newArrayList(ir));

		when(projectDaoMock.createResearcher(researcher)).thenReturn(1);

		researcherControls.createResearcher(researcher);
		verify(projectDaoMock).createResearcher(researcher);
	}

	@Test(expected = InvalidEntityException.class)
	public void testResearcherMissingFullName() throws Exception {

		Researcher newresearcher = new Researcher() {
			{
				setFullName("");
			}
		};
		when(projectDaoMock.createResearcher(newresearcher)).thenReturn(1);

		researcherControls.createResearcher(newresearcher);
	}

	@Test(expected = InvalidEntityException.class)
	public void testDuplicateResearcherFullName() throws Exception {

		List<Researcher> all = new LinkedList<Researcher>();
		all.add(new Researcher() {
			{
				setFullName("TestName");
			}
		});
		all.add(new Researcher() {
			{
				setFullName("TestName2");
			}
		});

		researcherControls.getAllResearchers();
		when(researcherControls.getAllResearchers()).thenReturn(all);

		researcherControls.createResearcher(researcher);
	}

	@Test(expected = InvalidEntityException.class)
	public void testCreateResearcherWithId() throws Exception {

		Researcher newresearcher = new Researcher() {
			{
				setFullName("TestNewName");
				setId(1);
				setStatusId(1);
				setPhone("09000000");
				setEmail("test@auckland.ac.nz");
			}
		};
		when(projectDaoMock.getResearcherById(1)).thenReturn(newresearcher);

		researcherControls.createResearcher(newresearcher);
	}

	@Test(expected = InvalidEntityException.class)
	public void testIncompleteResearcher() throws Exception {

		Researcher newresearcher = new Researcher() {
			{
				setFullName("TestNewName");
				setId(1);
				setStatusId(6);
				setPhone("09000000");
				setEmail("test@auckland.ac.nz");
			}
		};
		when(projectDaoMock.getResearcherById(1)).thenReturn(newresearcher);

		researcherControls.createResearcher(newresearcher);
	}

	@Test
	public void testAllowDuplicateFullNameAsNewResearcher() throws Exception {

		List<Researcher> all = new LinkedList<Researcher>();
		all.add(new Researcher() {
			{
				setFullName("TestName");
				setPhone("09000000");
				setEmail("test@auckland.ac.nz");
			}
		});
		all.add(new Researcher() {
			{
				setFullName("New Researcher");
				setPhone("09000000");
				setEmail("test@auckland.ac.nz");
			}
		});

		Researcher newresearcher = new Researcher() {
			{
				setFullName("New Researcher");
				setPhone("09000000");
				setEmail("test@auckland.ac.nz");
			}
		};

		// Mock alert: return mocked result set on find
		try {
			when(projectDaoMock.getResearchers()).thenReturn(all);
		} catch (Exception e) {
			e.printStackTrace();
		}

		when(researcherControls.getAllResearchers()).thenReturn(all);

		researcherControls.createResearcher(newresearcher);
	}

	@Test(expected = InvalidEntityException.class)
	public void testEditResearcherWithNoId() throws Exception {

		when(researcherControls.getResearcher(1)).thenReturn(researcher);
		researcherControls.editResearcher(researcher);
		verify(projectDaoMock).updateResearcher(researcher);

	}

	@Test
	public void testEditResearcher() throws Exception {
		researcher.setId(1);
		researcher.setLastModified("01/01/2014");

		InstitutionalRole ir = new InstitutionalRole();
		ir.setId(1);
		ir.setName("StupidRole");
		when(researcherControls.getInstitutionalRoles()).thenReturn(
				Lists.newArrayList(ir));
		when(researcherControls.getResearcher(1)).thenReturn(researcher);

		researcherControls.editResearcher(researcher);

		verify(projectDaoMock).updateResearcher(researcher);

	}

	@Test(expected = NullPointerException.class)
	public void testEditAdviserWithIncorrectId() throws Exception {

		researcher.setId(1);
		researcher.setLastModified("1/1/2014");

		when(researcherControls.getResearcher(1)).thenReturn(researcher);

		researcherControls
				.editResearcher(2, "FullName", "1/1/2014", "TestName");
		verify(projectDaoMock).updateResearcher(researcher);
	}

	@Test(expected = OutOfDateException.class)
	public void testEditAdviserWithIncorrectTimestamp() throws Exception {

		researcher.setId(1);
		researcher.setLastModified("1/1/2014");

		when(researcherControls.getResearcher(1)).thenReturn(researcher);

		// Try to update the field with incorrect timestamp
		researcherControls
				.editResearcher(1, "FullName", "1/2/2014", "TestName");
		verify(projectDaoMock).updateResearcher(researcher);
	}

	@Test(expected = NullPointerException.class)
	public void testDeleteResearcher() throws Exception {

		InstitutionalRole ir = new InstitutionalRole();
		ir.setId(1);
		ir.setName("StupidRole");
		when(researcherControls.getInstitutionalRoles()).thenReturn(
				Lists.newArrayList(ir));

		researcher.setId(1);

		when(researcherControls.getResearcher(1)).thenReturn(researcher);

		researcherControls.delete(1);
		verify(projectDaoMock).deleteResearcher(1);

		// Try edit researcher to see if researcher is completely removed
		researcherControls.editResearcher(researcher);
	}

	@Test(expected = ArgumentsAreDifferent.class)
	public void testDeleteIncorrectAdviser() throws Exception {

		InstitutionalRole ir = new InstitutionalRole();
		ir.setId(1);
		ir.setName("StupidRole");
		when(projectDaoMock.getInstitutionalRoles()).thenReturn(
				Lists.newArrayList(ir));

		researcherControls.createResearcher(researcher);
		researcher.setId(1);

		when(researcherControls.getResearcher(1)).thenReturn(researcher);

		researcherControls.delete(2);
		verify(projectDaoMock).deleteResearcher(1);
		// Try edit researcher to see if researcher is completely removed
		researcherControls.editResearcher(researcher);
	}
}
