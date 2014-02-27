/**
 * 
 */
package researchHub.control;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import nz.org.nesi.researchHub.control.ProjectControls;
import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pm.db.ProjectDao;
import pm.pojo.APLink;
import pm.pojo.Adviser;
import pm.pojo.Project;
import pm.pojo.ProjectFacility;
import pm.pojo.ProjectWrapper;
import pm.pojo.RPLink;
import pm.pojo.Researcher;

/**
 * @author echoi
 * 
 */
@Repository(value = "ProjectDao")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/Controls-context.xml" })
public class ProjectControlsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@InjectMocks
	private ProjectControls projectControls = new ProjectControls();
	@InjectMocks
	private final ProjectDao projectDaoMock = Mockito.mock(ProjectDao.class);

	private Project project;
	private ProjectWrapper projectWrapper;
	private ProjectFacility projectFacility;
	private APLink aplink;
	private Adviser adviser;
	private Researcher researcher;
	private RPLink rplink;

	@Before
	public void setUp() throws Exception {

		// provide minimum information to create a project.
		project = new Project() {
			{
				setName("TestName");
				setProjectCode("");
				setHostInstitution("");

			}
		};

		projectWrapper = new ProjectWrapper() {
			{
				setProject(project);
			}
		};

		projectControls = new ProjectControls() {
			{
				projectDao = projectDaoMock;
			}
		};

		projectFacility = new ProjectFacility() {
			{
				setFacilityName("Pan");
				setProjectId(null);
				setFacilityId(1);
			}
		};

		adviser = new Adviser() {
			{
				setFullName("TestAdviser");
			}
		};

		aplink = new APLink() {
			{
				setAdviser(adviser);
			}
		};

		researcher = new Researcher() {
			{
				setFullName("TestResearcher");
			}
		};

		rplink = new RPLink() {
			{
				setResearcher(researcher);
			}
		};
	}

	@Test
	public void testGetProjectById() throws Exception {

		String expectedFullname = "TestName";

		project.setId(1);

		projectWrapper.getProject().setId(1);
		when(projectControls.getProjectWrapper(1)).thenReturn(projectWrapper);
		assertEquals(expectedFullname, projectControls.getProjectWrapper(1)
				.getProject().getName());
	}

	@Test
	public void testCreateProjectSuccessfully() throws Exception {

		when(projectDaoMock.createProjectWrapper(projectWrapper)).thenReturn(1);

		projectControls.createProjectWrapper(projectWrapper);
		verify(projectDaoMock).createProjectWrapper(projectWrapper);
	}

	@Test(expected = InvalidEntityException.class)
	public void testMissingName() throws Exception {

		final Project newproject = new Project() {
			{
				setName("");
				setProjectCode("");
				setHostInstitution("");
			}
		};

		ProjectWrapper newprojectWrapper = new ProjectWrapper() {
			{
				setProject(newproject);
			}
		};
		when(projectDaoMock.createProjectWrapper(newprojectWrapper))
				.thenReturn(1);

		projectControls.createProjectWrapper(newprojectWrapper);

	}

	@Test
	public void testDuplicateProjectName() throws Exception {

		final List<Project> all = new LinkedList<Project>();
		all.add(new Project() {
			{
				setName("TestName");
				setProjectCode("");
				setHostInstitution("");
			}
		});
		all.add(new Project() {
			{
				setName("TestName2");
				setProjectCode("");
				setHostInstitution("");
			}
		});

		ProjectWrapper newprojectWrapper = new ProjectWrapper() {
			{
				setProject(all.get(0));
			}
		};
		projectControls.getProjects();
		when(projectControls.getProjects()).thenReturn(all);

		projectControls.createProjectWrapper(newprojectWrapper);
	}

	@Test(expected = InvalidEntityException.class)
	public void testCreateProjectWithId() throws Exception {

		final Project newproject = new Project() {
			{
				setName("TestNewName");
				setId(1);
				setProjectCode("");
				setHostInstitution("");
			}
		};
		ProjectWrapper newprojectWrapper = new ProjectWrapper() {
			{
				setProject(newproject);
			}
		};

		when(projectDaoMock.getProjectWrapperById(1)).thenReturn(
				newprojectWrapper);

		projectControls.createProjectWrapper(newprojectWrapper);
	}

	@Test
	public void testAllowDuplicateFullNameAsNewProject() throws Exception {

		final List<Project> all = new LinkedList<Project>();
		all.add(new Project() {
			{
				setName("TestName");
				setProjectCode("");
				setHostInstitution("");
			}
		});
		all.add(new Project() {
			{
				setName("New Project");
				setProjectCode("");
				setHostInstitution("");
			}
		});

		final Project newproject = new Project() {
			{
				setName("New Project");
				setProjectCode("");
				setHostInstitution("");
			}
		};
		ProjectWrapper newprojectWrapper = new ProjectWrapper() {
			{
				setProject(newproject);
			}
		};

		// Mock alert: return mocked result set on find
		try {
			when(projectDaoMock.getProjects()).thenReturn(all);
		} catch (Exception e) {
			e.printStackTrace();
		}

		when(projectControls.getProjects()).thenReturn(all);

		projectControls.createProjectWrapper(newprojectWrapper);
	}

	@Test
	public void testGetProjectsByProjectCode() throws Exception {

		String expectedFullname = "TestName";
		project.setProjectCode("pc0001");
		project.setId(1);

		when(projectDaoMock.getProjectWrapperById(1))
				.thenReturn(projectWrapper);
		when(projectControls.getProjectWrapper("pc0001")).thenReturn(
				projectWrapper);

		assertEquals(expectedFullname,
				projectControls.getProjectWrapper("pc0001").getProject()
						.getName());
	}

	@Test
	public void testGetNesiProjectByProjectCode() throws Exception {

		project.setProjectCode("nesi00001");
		projectFacility.setProjectId(1);
		final List<ProjectFacility> projectFacilities = new LinkedList<ProjectFacility>();
		projectFacilities.add(projectFacility);

		ProjectWrapper newpw = new ProjectWrapper() {
			{
				setProject(project);
				setProjectFacilities(projectFacilities);
			}
		};

		when(projectControls.getProjectWrapper("nesi00001")).thenReturn(newpw);
		when(projectControls.createProjectWrapper(newpw)).thenReturn(1);
	}

	@Test(expected = InvalidEntityException.class)
	public void testCreateProjectWithNoHCPFacilities() throws Exception {
		project.setProjectCode("pc0001");
		project.setId(1);
		when(projectControls.getProjectWrapper(1)).thenReturn(projectWrapper);
		projectControls.createProjectWrapper(projectWrapper);
	}

	@Test(expected = InvalidEntityException.class)
	public void testAddInvalidProjectAdviser() throws Exception {
		projectControls.addAdviser(aplink);
	}

	@Test(expected = DatabaseException.class)
	public void testAddProjectAdviserWithIncorrectId() throws Exception {
		aplink.setAdviserId(1);
		projectControls.addAdviser(aplink);
	}

	@Test
	public void testAddProjectAdviserSuccessfully() throws Exception {
		project.setProjectCode("pc0001");
		when(projectDaoMock.getProjectWrapperById(1))
				.thenReturn(projectWrapper);
		when(projectControls.getProjectWrapper(1)).thenReturn(projectWrapper);

		projectControls.createProjectWrapper(projectWrapper);

		adviser.setId(1);
		aplink.setAdviser(adviser);
		aplink.setProjectId(1);
		projectWrapper.getApLinks().add(aplink);
		aplink.setAdviserId(adviser.getId());
		aplink.setAdviserRoleId(2);
		aplink.setAdviserRoleName("Primary Adviser");
		projectControls.addAdviser(aplink);
	}

	@Test(expected = InvalidEntityException.class)
	public void testAddInvalidProjectResarcher() throws Exception {
		projectControls.addResearcher(rplink);
	}

	@Test(expected = DatabaseException.class)
	public void testAddProjectResearcherWithIncorrectId() throws Exception {
		rplink.setResearcherId(1);
		projectControls.addResearcher(rplink);
	}

	@Test
	public void testAddProjectResearcherSuccessfully() throws Exception {
		project.setProjectCode("pc0001");
		when(projectDaoMock.getProjectWrapperById(1))
				.thenReturn(projectWrapper);
		when(projectControls.getProjectWrapper(1)).thenReturn(projectWrapper);

		projectControls.createProjectWrapper(projectWrapper);

		researcher.setId(1);
		rplink.setResearcher(researcher);
		rplink.setProjectId(1);
		projectWrapper.getRpLinks().add(rplink);
		rplink.setResearcherId(researcher.getId());
		rplink.setResearcherRoleId(2);
		rplink.setResearcherRoleName("Project Owner");
		projectControls.addResearcher(rplink);
	}
}
