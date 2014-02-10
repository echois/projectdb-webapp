/**
 * 
 */
package pm.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

/**
 * @author echoi
 *
 */
@Repository(value="ProjectDao")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/AdvisercontrolsTest-context.xml", "/root-context.xml"})
//@ContextConfiguration
public class AdviserControlsTest {

	/**
	 * @throws java.lang.Exception
	 */

	@InjectMocks
	private AdviserControls adviserControls = new AdviserControls();
	@InjectMocks
	private ProjectDao projectDaoMock = Mockito.mock(ProjectDao.class);
	
	private Adviser adviser;
	
	@Before
	public void setUp() throws Exception {
		
		adviser = new Adviser(){
			{
			//setId(101);
			setFullName("TestName");
			}
		};
		
		adviserControls = new AdviserControls(){{projectDao = projectDaoMock;}};
		
	}
	
	@Test
	public void testGetaAdviser() throws Exception{
		
		adviser.setId(1);
		projectDaoMock.createAdviser(adviser);
		when(projectDaoMock.getAdviserById(1)).thenReturn(adviser);
		
	
		when(adviserControls.getAdviser(1)).thenReturn(adviser);
		//System.out.println(projectDaoMock.getAdviserById(1).getFullName());
		
		//verify(projectDaoMock, times(2)).getAdviserById(1);
		assertEquals("TestName", adviserControls.getAdviser(1).getFullName());
	}
	
	@Test
	public void testGetAllAdvisers(){
		List<Adviser> all = new LinkedList<Adviser>();
		all.add(new Adviser(){ {setFullName("TestName1");}});
		all.add(new Adviser(){ {setFullName("TestName2");}});

		//Mock alert: return mocked result set on find
		try {
			when(projectDaoMock.getAdvisers()).thenReturn(all);
			//System.out.println("projectDao" + projectDaoMock.getAdvisers());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//call the main method you want to test
		List result = adviserControls.getAllAdvisers();
		//System.out.println("adviserControls" + adviserControls.getAllAdvisers());
		
		//Mock alert: verify the method was called
		try {
			verify(projectDaoMock, times(1)).getAdvisers();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("projectDaoMock is not called");
			e.printStackTrace();
		}
	}
	
	@Test(expected = InvalidEntityException.class)
	public void testMissingFullName() throws Exception{

		Adviser newadviser = new Adviser(){{setFullName(""); setId(1);}};
		when(projectDaoMock.getAdviserById(1)).thenReturn(newadviser);
		
	
		when(adviserControls.getAdviser(1)).thenReturn(newadviser);
		//System.out.println("special"+projectDaoMock.getAdviserById(1).getFullName());
			
		adviserControls.createAdviser(newadviser);
	}

	@Test(expected = InvalidEntityException.class)
	public void testDuplicateAdviserFullName() throws Exception {
		
		List<Adviser> all = new LinkedList<Adviser>();
		all.add(new Adviser(){ {setFullName("TestName");}});
		all.add(new Adviser(){ {setFullName("TestName2");}});
		
		//Mock alert: return mocked result set on find
		try {
			when(projectDaoMock.getAdvisers()).thenReturn(all);
			//System.out.println("projectDao" + projectDaoMock.getAdvisers());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//call the main method you want to test
		List result = adviserControls.getAllAdvisers();
		when(adviserControls.getAllAdvisers()).thenReturn(all);
		
		adviserControls.createAdviser(adviser);		
	}
		
	@Test(expected = InvalidEntityException.class)
	public void testCreateAdviserWithId() throws Exception {
		
		Adviser newadviser = new Adviser(){{setFullName("TestNewName"); setId(3);}};
		projectDaoMock.createAdviser(adviser);
		when(projectDaoMock.getAdviserById(1)).thenReturn(adviser);
		
	
		when(adviserControls.getAdviser(1)).thenReturn(adviser);
		System.out.println(projectDaoMock.getAdviserById(1).getFullName());
		
		verify(projectDaoMock, times(1)).getAdviserById(1);
		assertEquals("TestName", adviserControls.getAdviser(1).getFullName());
		
		adviserControls.createAdviser(newadviser);		
	}
	
	@Test
	public void testAllowDuplicateNewAdviserFullName() throws Exception {
		
		List<Adviser> all = new LinkedList<Adviser>();
		all.add(new Adviser(){ {setFullName("TestName");}});
		all.add(new Adviser(){ {setFullName("New adviser");}});
		
		Adviser newadviser = new Adviser(){{setFullName("New adviser");}};
		
		//Mock alert: return mocked result set on find
		try {
			when(projectDaoMock.getAdvisers()).thenReturn(all);
			//System.out.println("projectDao" + projectDaoMock.getAdvisers());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//call the main method you want to test
		List result = adviserControls.getAllAdvisers();
		when(adviserControls.getAllAdvisers()).thenReturn(all);
		
		adviserControls.createAdviser(newadviser);		
	}

	
}


