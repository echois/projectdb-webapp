package signup.slcs;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/SLCSTest-context.xml"})
public class SLCSTest {

	@Autowired
	private SLCS slcs;
	
	@Test
	public void testGetIdpDnMap() throws Exception {
		Map<String, String> m = this.slcs.getIdpDnMap();
		assertNotNull(m);
		assertTrue(m.size() > 0);
	}
	
	@Test
	public void testGetDn() throws Exception {
		Map<String, String> m = this.slcs.getIdpDnMap();		
	    assertTrue(m.containsKey("http://iam.auckland.ac.nz/idp"));
	    assertEquals("/DC=nz/DC=org/DC=bestgrid/DC=slcs/O=The University of Auckland", m.get("http://iam.auckland.ac.nz/idp"));
	    assertNull(m.get("invalid_url"));
	}

	@Test(expected = UnknownHostException.class)
	@DirtiesContext
	public void testBadHost() throws Exception {
		this.slcs.setSlcsMapUrl("http://some.bogus.host/idp");
		this.slcs.getIdpDnMap();
	}

	@Test(expected = Exception.class)
	@DirtiesContext
	public void testBadUrl() throws Exception {
		this.slcs.setSlcsMapUrl("http://iam.auckland.ac.nz/does/not/exist");
		this.slcs.getIdpDnMap();
	}

}
