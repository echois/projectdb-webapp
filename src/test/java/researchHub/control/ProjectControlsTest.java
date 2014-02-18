/**
 * 
 */
package researchHub.control;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import nz.org.nesi.researchHub.control.ProjectControls;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pm.db.ProjectDao;
import pm.pojo.Project;
import pm.pojo.Project;
import pm.pojo.ProjectFacility;
import pm.pojo.ProjectWrapper;

/**
 * @author echoi
 *
 */
@Repository(value="ProjectDao")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/Controls-context.xml"})
public class ProjectControlsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@InjectMocks
	private ProjectControls projectControls = new ProjectControls();
	@InjectMocks
	private ProjectDao projectDaoMock = Mockito.mock(ProjectDao.class);
	
	private Project project;
	private ProjectWrapper projectWrapper;
	private ProjectFacility projectFacility;
	
	@Before
	public void setUp() throws Exception {
		
		//provide minimum information to create a project.
		project = new Project(){
			{
				setName("TestName");
				setProjectCode("");
				setHostInstitution("");
				
			}
		};
		
		projectWrapper = new ProjectWrapper(){
			{
				setProject(project);				
			}
		};
		
		projectControls = new ProjectControls(){
			{
				projectDao = projectDaoMock;
			}
		};
		
		projectFacility = new ProjectFacility(){
			{
				setFacilityName("Pan");
				setProjectId(null);
				setFacilityId(1);
			}
		};
	}
	
	@Test
	public void testGetProjectById() throws Exception{
		
		String expectedFullname = "TestName";
		
		project.setId(1);
		
		projectWrapper.getProject().setId(1);
		when(projectControls.getProjectWrapper(1)).thenReturn(projectWrapper);
		assertEquals(expectedFullname, projectControls.getProjectWrapper(1).getProject().getName());
	}
	
	/*
	@Test
	public void testGetAllProjects() throws Exception{
		
		List<Project> all = new LinkedList<Project>();
		all.add(new Project(){ {setFullName("TestName1");}});
		all.add(new Project(){ {setFullName("TestName2");}});

		projectControls.getAllProjects();
		verify(projectDaoMock).getProjects();

	}
	*/
	
	@Test
	public void testCreateProjectSuccessfully() throws Exception{
		
		when(projectDaoMock.createProjectWrapper(projectWrapper)).thenReturn(1);
		
		projectControls.createProjectWrapper(projectWrapper);
		verify(projectDaoMock).createProjectWrapper(projectWrapper);
	}
	
	
	@Test(expected=InvalidEntityException.class)
	public void testMissingName() throws Exception{
		
		final Project newproject = new Project(){{setName(""); setProjectCode(""); setHostInstitution("");}};

		ProjectWrapper newprojectWrapper = new ProjectWrapper(){{setProject(newproject);}};
		when(projectDaoMock.createProjectWrapper(newprojectWrapper)).thenReturn(1);
			
		projectControls.createProjectWrapper(newprojectWrapper);
		
	}
	
	

	@Test
	//(expected = InvalidEntityException.class)
	public void testDuplicateProjectName() throws Exception {
		
		final List<Project> all = new LinkedList<Project>();
		all.add(new Project(){ {setName("TestName"); setProjectCode(""); setHostInstitution("");}});
		all.add(new Project(){ {setName("TestName2");setProjectCode(""); setHostInstitution("");}});
		
		ProjectWrapper newprojectWrapper = new ProjectWrapper(){{setProject(all.get(0));}};
		projectControls.getProjects();
		when(projectControls.getProjects()).thenReturn(all);
		
		projectControls.createProjectWrapper(newprojectWrapper);		
	}
		
	
	@Test(expected = InvalidEntityException.class)
	public void testCreateProjectWithId() throws Exception {
		
		final Project newproject = new Project(){{setName("TestNewName"); setId(1); setProjectCode(""); setHostInstitution("");}};
		ProjectWrapper newprojectWrapper = new ProjectWrapper(){{setProject(newproject);}};
		
		when(projectDaoMock.getProjectWrapperById(1)).thenReturn(newprojectWrapper);
		
		projectControls.createProjectWrapper(newprojectWrapper);	
	}
	
	
	@Test
	public void testAllowDuplicateFullNameAsNewProject() throws Exception {
		
		final List<Project> all = new LinkedList<Project>();
		all.add(new Project(){ {setName("TestName"); setProjectCode(""); setHostInstitution("");}});
		all.add(new Project(){ {setName("New Project");setProjectCode(""); setHostInstitution("");}});
		
		final Project newproject = new Project(){{setName("New Project"); setProjectCode(""); setHostInstitution(""); }};
		ProjectWrapper newprojectWrapper = new ProjectWrapper(){{setProject(newproject);}};
		
		//Mock alert: return mocked result set on find
		try {
			when(projectDaoMock.getProjects()).thenReturn(all);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//call the main method you want to test
		List<Project> result = projectControls.getProjects();
		when(projectControls.getProjects()).thenReturn(all);
		
		projectControls.createProjectWrapper(newprojectWrapper);		
	}
	
	@Test
	public void testGetProjectsByProjectCode() throws Exception {
		
		String expectedFullname = "TestName";
		project.setProjectCode("pc0001");
		project.setId(1);
		
		//projectWrapper.getProject().setId(1);
		//projectWrapper.getProject().setProjectCode("pc0001");
		when(projectDaoMock.getProjectWrapperById(1)).thenReturn(projectWrapper);
		when(projectControls.getProjectWrapper("pc0001")).thenReturn(projectWrapper);
				
		assertEquals(expectedFullname, projectControls.getProjectWrapper("pc0001").getProject().getName());
	}
	
	@Test
	public void testGetNesiProjectByProjectCode() throws Exception {
		
		//String expectedFullname = "TestName";
		project.setProjectCode("nesi");
		//project.setId(1);
		
		projectFacility.setProjectId(1);
		final List<ProjectFacility> projectFacilities = new LinkedList<ProjectFacility>();
		projectFacilities.add(projectFacility);
		
		ProjectWrapper newpw = new ProjectWrapper(){{ setProject(project); setProjectFacilities(projectFacilities); }};
				
		when(projectControls.getProjectWrapper("nesi")).thenReturn(newpw);
		when(projectControls.createProjectWrapper(newpw)).thenReturn(1);
	}

}
