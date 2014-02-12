/**
 * 
 */
package pm.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import nz.org.nesi.researchHub.control.ResearcherControls;
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
import pm.pojo.Researcher;

/**
 * @author echoi
 *
 */
@Repository(value="ProjectDao")
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations={"/root-context.xml"})
public class ResearcherControlsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@InjectMocks
	private ResearcherControls researcherControls;
	@InjectMocks
	private ProjectDao projectDaoMock = Mockito.mock(ProjectDao.class);
	
	private Researcher researcher;
	
	@Before
	public void setUp() throws Exception {
		
		// provide minimum information to create a researcher.
		researcher = new Researcher(){
			{
				setFullName("TestName");
			}
		};
		
		researcherControls = new ResearcherControls(){{projectDao = projectDaoMock;}};		
	}
	
	
	@Test
	public void testGetResearcherById() throws Exception{
		
		String expectedFullname = "TestName";
		when(researcherControls.getResearcher(1)).thenReturn(researcher);
		assertEquals(expectedFullname, researcherControls.getResearcher(1).getFullName());
	}
	
	//@Test(expected=NoSuchEntityException.class)
	/*@Test
	public void testGetresearcherByIdWithInvalidData() throws Exception {
		//Check why it is failing
		researcher.setId(1);
		when(projectDaoMock.getresearcherById(1)).thenReturn(researcher);
		when(researcherControls.getresearcher(1)).thenReturn(researcher);
		researcherControls.getresearcher(1014);
	}*/
	
	@Test
	public void testGetAllResearchers() throws Exception{
		
		List<Researcher> all = new LinkedList<Researcher>();
		all.add(new Researcher(){ {setFullName("TestName1");}});
		all.add(new Researcher(){ {setFullName("TestName2");}});

		researcherControls.getAllResearchers();
		verify(projectDaoMock).getResearchers();

	}
	
	@Test
	public void testCreateResearcherSuccessfully() throws Exception{
		
		when(projectDaoMock.createResearcher(researcher)).thenReturn(1);
		
		researcherControls.createResearcher(researcher);
		verify(projectDaoMock).createResearcher(researcher);
	}
	
	@Test(expected = InvalidEntityException.class)
	public void testResearcherMissingFullName() throws Exception{

		Researcher newresearcher = new Researcher(){{setFullName(""); }};
		when(projectDaoMock.createResearcher(newresearcher)).thenReturn(1);
			
		researcherControls.createResearcher(newresearcher);
	}

	@Test(expected = InvalidEntityException.class)
	public void testDuplicateResearcherFullName() throws Exception {
		
		List<Researcher> all = new LinkedList<Researcher>();
		all.add(new Researcher(){ {setFullName("TestName");}});
		all.add(new Researcher(){ {setFullName("TestName2");}});
		
		researcherControls.getAllResearchers();
		when(researcherControls.getAllResearchers()).thenReturn(all);
		
		researcherControls.createResearcher(researcher);		
	}
		
	@Test(expected = InvalidEntityException.class)
	public void testCreateResearcherWithId() throws Exception {
		
		Researcher newresearcher = new Researcher(){{setFullName("TestNewName"); setId(1);}};
		when(projectDaoMock.getResearcherById(1)).thenReturn(newresearcher);
		
		researcherControls.createResearcher(newresearcher);		
	}
	
	@Test
	public void testAllowDuplicateFullNameAsNewResearcher() throws Exception {
		
		List<Researcher> all = new LinkedList<Researcher>();
		all.add(new Researcher(){ {setFullName("TestName");}});
		all.add(new Researcher(){ {setFullName("New Researcher");}});
		
		Researcher newresearcher = new Researcher(){{setFullName("New Researcher");}};
		
		//Mock alert: return mocked result set on find
		try {
			when(projectDaoMock.getResearchers()).thenReturn(all);
			//System.out.println("projectDao" + projectDaoMock.getresearchers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//call the main method you want to test
		List<Researcher> result = researcherControls.getAllResearchers();
		when(researcherControls.getAllResearchers()).thenReturn(all);
		
		researcherControls.createResearcher(newresearcher);		
	}
	
	@Test(expected=InvalidEntityException.class)
	public void testEditResearcherWithNoId() throws Exception{
		
		when(researcherControls.getResearcher(1)).thenReturn(researcher);
		researcherControls.editResearcher(researcher);
		verify(projectDaoMock).updateResearcher(researcher);
			
	}
	
	@Test
	public void testEditResearcher() throws Exception{
		researcher.setId(1);
		researcher.setLastModified("01/01/2014");
		
		when(researcherControls.getResearcher(1)).thenReturn(researcher);
				
		researcherControls.editResearcher(researcher);
		
		verify(projectDaoMock).updateResearcher(researcher);
		
	}
	
	
	@Test(expected=NullPointerException.class)
	public void testDeleteResearcher() throws Exception{
		researcher.setId(1);
		
		when(researcherControls.getResearcher(1)).thenReturn(researcher);
		
		researcherControls.delete(1);
		verify(projectDaoMock).deleteResearcher(1);
		
		// Try edit researcher to see if researcher is completely removed
		researcherControls.editResearcher(researcher);
	}

}
