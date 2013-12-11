package signup.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import common.util.AffiliationUtil;

import pm.db.ProjectDao;
import pm.pojo.InstitutionalRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/RequestAccountControllerValidationTest-context.xml", "/root-context.xml"})
@WebAppConfiguration
public class RequestAccountControllerValidationTest {

	  @Autowired
	  private WebApplicationContext wac;
	  @Autowired
	  private ProjectDao projectDaoMock;
	  @Autowired
	  private AffiliationUtil affiliationUtilMock;
	  
	  private MockMvc mockMvc;

	  @Before
	  public void setup() throws Exception {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	  }

	  @Test
	  public void postAccountRequestSuccess() throws Exception {
		  when(projectDaoMock.getInstitutionalRoles()).thenReturn(new LinkedList<InstitutionalRole>());
		  when(affiliationUtilMock.getAffiliationStrings()).thenReturn(new LinkedList<String>());
	      this.mockMvc.perform(post("/requestaccount").param("institution", "Test Institution")
	        .param("institutionalRoleId", "42").param("email", "test@test.org"))
	        .andExpect(status().isOk())
	        //.andDo(print())
	        .andExpect(model().attributeHasNoErrors("requestaccount"));
	  }

	  @Test
	  public void postAccountRequestMissingEmail() throws Exception {
		  when(projectDaoMock.getInstitutionalRoles()).thenReturn(new LinkedList<InstitutionalRole>());
		  when(affiliationUtilMock.getAffiliationStrings()).thenReturn(new LinkedList<String>());
	      this.mockMvc.perform(post("/requestaccount").param("institution", "Test Institution")
	        .param("institutionalRoleId", "42"))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeHasFieldErrors("requestaccount", "email"));
	  }

	  @Test
	  public void postAccountRequestInvalidEmail() throws Exception {
		  when(projectDaoMock.getInstitutionalRoles()).thenReturn(new LinkedList<InstitutionalRole>());
		  when(affiliationUtilMock.getAffiliationStrings()).thenReturn(new LinkedList<String>());
	      this.mockMvc.perform(post("/requestaccount").param("institution", "Test Institution")
	        .param("institutionalRoleId", "42").param("email", "test"))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeHasFieldErrors("requestaccount", "email"));
	  }

	  @Test
	  public void postAccountRequest() throws Exception {
		  when(projectDaoMock.getInstitutionalRoles()).thenReturn(new LinkedList<InstitutionalRole>());
		  when(affiliationUtilMock.getAffiliationStrings()).thenReturn(new LinkedList<String>());
	      this.mockMvc.perform(post("/requestaccount"))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeHasFieldErrors("requestaccount",
	        	"institution", "institutionalRoleId", "email"));
	  }
}
