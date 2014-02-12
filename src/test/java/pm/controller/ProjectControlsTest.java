/**
 * 
 */
package pm.controller;

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

/**
 * @author echoi
 *
 */
@Repository(value="ProjectDao")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"root-context.xml"})
public class ProjectControlsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@InjectMocks
	private ProjectControls projectControls = new ProjectControls();
	@InjectMocks
	private ProjectDao projectDaoMock = Mockito.mock(ProjectDao.class);
	
	private Project project;
	
	@Before
	public void setUp() throws Exception {
		
		//provide minimum information to create a project.
		project = new Project(){
			{
				setName("TestName");
			}
		};
		
		projectControls = new ProjectControls(){
			{
				projectDao = projectDaoMock;
			}
		};
	}
	
	@Test
	public void testGetProjectById() throws Exception{
		
		String expectedFullname = "TestName";
		when(projectControls.getProject(1)).thenReturn(project);
		assertEquals(expectedFullname, projectControls.getProject(1).getFullName());
	}
	
	
	@Test
	public void testGetAllProjects() throws Exception{
		
		List<Project> all = new LinkedList<Project>();
		all.add(new Project(){ {setFullName("TestName1");}});
		all.add(new Project(){ {setFullName("TestName2");}});

		projectControls.getAllProjects();
		verify(projectDaoMock).getProjects();

	}
	
	@Test
	public void testCreateProjectSuccessfully() throws Exception{
		
		when(projectDaoMock.createProject(project)).thenReturn(1);
		
		projectControls.createProject(project);
		verify(projectDaoMock).createProject(project);
	}
	
	@Test(expected = InvalidEntityException.class)
	public void testMissingFullName() throws Exception{

		Project newproject = new Project(){{setFullName(""); }};
		when(projectDaoMock.createProject(newproject)).thenReturn(1);
			
		projectControls.createProject(newproject);
	}

	@Test(expected = InvalidEntityException.class)
	public void testDuplicateProjectFullName() throws Exception {
		
		List<Project> all = new LinkedList<Project>();
		all.add(new Project(){ {setFullName("TestName");}});
		all.add(new Project(){ {setFullName("TestName2");}});
		
		projectControls.getAllProjects();
		when(projectControls.getAllProjects()).thenReturn(all);
		
		projectControls.createProject(project);		
	}
		
	@Test(expected = InvalidEntityException.class)
	public void testCreateProjectWithId() throws Exception {
		
		Project newproject = new Project(){{setFullName("TestNewName"); setId(1);}};
		when(projectDaoMock.getProjectById(1)).thenReturn(newproject);
		
		projectControls.createProject(newproject);		
	}
	
	@Test
	public void testAllowDuplicateFullNameAsNewProject() throws Exception {
		
		List<Project> all = new LinkedList<Project>();
		all.add(new Project(){ {setFullName("TestName");}});
		all.add(new Project(){ {setFullName("New Project");}});
		
		Project newproject = new Project(){{setFullName("New Project");}};
		
		//Mock alert: return mocked result set on find
		try {
			when(projectDaoMock.getProjects()).thenReturn(all);
			//System.out.println("projectDao" + projectDaoMock.getProjects());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//call the main method you want to test
		List<Project> result = projectControls.getAllProjects();
		when(projectControls.getAllProjects()).thenReturn(all);
		
		projectControls.createProject(newproject);		
	}
	
	@Test
	public void testGetProjectByDrupalId() throws Exception {
		
		String expectedFullname = "TestName";
		when(projectDaoMock.getProjectByDrupalId("1")).thenReturn(project);
		when(projectControls.getProjectByDrupalId("1")).thenReturn(project);
				
		assertEquals(expectedFullname, projectControls.getProjectByDrupalId("1").getFullName());
	}
	
	@Test(expected=NoSuchEntityException.class)
	public void testGetProjectByDrupalIdWithInvalidData() throws Exception {
		
		when(projectDaoMock.getProjectByDrupalId("abc")).thenReturn(project);
		when(projectControls.getProjectByDrupalId("abc")).thenReturn(project);
		projectControls.getProjectByDrupalId("123");				
	}
	
	@Test(expected=InvalidEntityException.class)
	public void testEditProjectWithNoId() throws Exception{
		
		when(projectControls.getProject(1)).thenReturn(project);
		projectControls.editProject(project);
		verify(projectDaoMock).updateProject(project);
			
	}
	
	@Test
	public void testEditProject() throws Exception{
		project.setId(1);
		project.setLastModified("01/01/2014");
		
		when(projectControls.getProject(1)).thenReturn(project);
				
		projectControls.editProject(project);
		
		verify(projectDaoMock).updateProject(project);
		
	}
	
	
	@Test(expected=NullPointerException.class)
	public void testDeleteProject() throws Exception{
		project.setId(1);
		
		when(projectControls.getProject(1)).thenReturn(project);
		
		projectControls.delete(1);
		verify(projectDaoMock).deleteProject(1);
		
		// Try edit project to see if project is completely removed
		projectControls.editProject(project);
	}

}
