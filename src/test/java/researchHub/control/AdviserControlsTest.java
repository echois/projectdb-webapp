/**
 * 
 */
package researchHub.control;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.activation.DataSource;
import javax.servlet.ServletContext;

import nz.org.nesi.researchHub.control.AbstractControl;
import nz.org.nesi.researchHub.control.AdviserControls;
import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import pm.db.ProjectDao;
import pm.pojo.Adviser;
import pm.pojo.InstitutionalRole;
import pm.pojo.ProjectWrapper;
import static org.hamcrest.core.Is.*;

/**
 * @author echoi
 *
 */
@Repository(value="ProjectDao")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/Controls-context.xml"})
public class AdviserControlsTest {

	/**
	 * @throws java.lang.Exception
	 */

	@InjectMocks
	private AdviserControls adviserControls;
	@InjectMocks
	private ProjectDao projectDaoMock = Mockito.mock(ProjectDao.class);
	
	private Adviser adviser;
	
	@Before
	public void setUp() throws Exception {
		
		// Provide minimum information to create an adviser.
		adviser = new Adviser(){
			{
				setFullName("TestName");
			}
		};
		
		adviserControls = new AdviserControls(){{projectDao = projectDaoMock;}};
		
	}
	
	@Test
	public void testGetAdviserById() throws Exception{
		
		String expectedFullname = "TestName";
		when(adviserControls.getAdviser(1)).thenReturn(adviser);
		assertEquals(expectedFullname, adviserControls.getAdviser(1).getFullName());
	}
	
	//@Test(expected=NoSuchEntityException.class)
	/*@Test
	public void testGetAdviserByIdWithInvalidData() throws Exception {
		//Check why it is failing
		adviser.setId(1);
		when(projectDaoMock.getAdviserById(1)).thenReturn(adviser);
		when(adviserControls.getAdviser(1)).thenReturn(adviser);
		adviserControls.getAdviser(1014);
	}*/
	
	@Test
	public void testGetAllAdvisers() throws Exception{
		
		List<Adviser> all = new LinkedList<Adviser>();
		all.add(new Adviser(){ {setFullName("TestName1");}});
		all.add(new Adviser(){ {setFullName("TestName2");}});

		adviserControls.getAllAdvisers();
		verify(projectDaoMock).getAdvisers();

	}
	
	@Test
	public void testCreateAdviserSuccessfully() throws Exception{
		
		when(projectDaoMock.createAdviser(adviser)).thenReturn(1);
		
		adviserControls.createAdviser(adviser);
		verify(projectDaoMock).createAdviser(adviser);
	}
	
	@Test(expected = InvalidEntityException.class)
	public void testMissingFullName() throws Exception{

		Adviser newadviser = new Adviser(){{setFullName(""); }};
		when(projectDaoMock.createAdviser(newadviser)).thenReturn(1);
			
		adviserControls.createAdviser(newadviser);
	}

	@Test(expected = InvalidEntityException.class)
	public void testDuplicateAdviserFullName() throws Exception {
		
		List<Adviser> all = new LinkedList<Adviser>();
		all.add(new Adviser(){ {setFullName("TestName");}});
		all.add(new Adviser(){ {setFullName("TestName2");}});
		
		adviserControls.getAllAdvisers();
		when(adviserControls.getAllAdvisers()).thenReturn(all);
		
		adviserControls.createAdviser(adviser);		
	}
		
	@Test(expected = InvalidEntityException.class)
	public void testCreateAdviserWithId() throws Exception {
		
		Adviser newadviser = new Adviser(){{setFullName("TestNewName"); setId(1);}};
		when(projectDaoMock.getAdviserById(1)).thenReturn(newadviser);
		
		adviserControls.createAdviser(newadviser);		
	}
	
	@Test
	public void testAllowDuplicateFullNameAsNewAdviser() throws Exception {
		
		List<Adviser> all = new LinkedList<Adviser>();
		all.add(new Adviser(){ {setFullName("TestName");}});
		all.add(new Adviser(){ {setFullName("New Adviser");}});
		
		Adviser newadviser = new Adviser(){{setFullName("New Adviser");}};
		
		//Mock alert: return mocked result set on find
		try {
			when(projectDaoMock.getAdvisers()).thenReturn(all);
			//System.out.println("projectDao" + projectDaoMock.getAdvisers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//call the main method you want to test
		List<Adviser> result = adviserControls.getAllAdvisers();
		when(adviserControls.getAllAdvisers()).thenReturn(all);
		
		adviserControls.createAdviser(newadviser);		
	}
	
	@Test
	public void testGetAdviserByDrupalId() throws Exception {
		
		String expectedFullname = "TestName";
		when(projectDaoMock.getAdviserByDrupalId("1")).thenReturn(adviser);
		when(adviserControls.getAdviserByDrupalId("1")).thenReturn(adviser);
				
		assertEquals(expectedFullname, adviserControls.getAdviserByDrupalId("1").getFullName());
	}
	
	@Test(expected=NoSuchEntityException.class)
	public void testGetAdviserByDrupalIdWithInvalidData() throws Exception {
		
		when(projectDaoMock.getAdviserByDrupalId("abc")).thenReturn(adviser);
		when(adviserControls.getAdviserByDrupalId("abc")).thenReturn(adviser);
		adviserControls.getAdviserByDrupalId("123");				
	}
	
	@Test(expected=InvalidEntityException.class)
	public void testEditAdviserWithNoId() throws Exception{
		
		when(adviserControls.getAdviser(1)).thenReturn(adviser);
		adviserControls.editAdviser(adviser);
		verify(projectDaoMock).updateAdviser(adviser);
			
	}
	
	@Test
	public void testEditAdviser() throws Exception{
		adviser.setId(1);
		adviser.setLastModified("01/01/2014");
		
		when(adviserControls.getAdviser(1)).thenReturn(adviser);
				
		adviserControls.editAdviser(adviser);
		
		verify(projectDaoMock).updateAdviser(adviser);
		
	}
	
	
	@Test(expected=NullPointerException.class)
	public void testDeleteAdviser() throws Exception{
		adviser.setId(1);
		
		when(adviserControls.getAdviser(1)).thenReturn(adviser);
		
		adviserControls.delete(1);
		verify(projectDaoMock).deleteAdviser(1);
		
		// Try edit adviser to see if adviser is completely removed
		adviserControls.editAdviser(adviser);
	}
}


