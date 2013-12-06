package common.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/EmailTest-context.xml"})
public class EmailTest {
	
	@Autowired
	private Email email;
    private GreenMail testSmtp;
	private String from = "from@test.org";
	private String to = "to@test.org";
	private String cc = "cc@test.org";
	private String replyto = "replyto@test.org";
	private String subject = "test subject";
	private String body = "test body";

    @Before
    public void testSmtpInit(){
        testSmtp = new GreenMail(ServerSetupTest.SMTP);
        testSmtp.start();
    }
    
	@Test
	public void testSend() throws Exception {
		this.email.send(from, to, cc, replyto, subject, body);
        Message m = testSmtp.getReceivedMessages()[0];
        assertEquals(from, ((InternetAddress)m.getFrom()[0]).getAddress());
        assertEquals(replyto, ((InternetAddress)m.getReplyTo()[0]).toString());
        assertEquals(to, ((InternetAddress)m.getRecipients(RecipientType.TO)[0]).toString());
        assertEquals(cc, ((InternetAddress)m.getRecipients(RecipientType.CC)[0]).toString());
        assertEquals(subject, m.getSubject());
        assertEquals(body, GreenMailUtil.getBody(m));
	}

	@Test
	public void testValidateEmailAddress() throws Exception {
		email.validateEmailAddress("test@test.org");
		List<String> invalid = Arrays.asList("test", "test@", "@test.org", "", null);
		for (String address: invalid) {
			try {
				email.validateEmailAddress(address);
				fail("'" + address + "' is not a valid e-mail address. Test should have failed.");
			} catch (Exception e) {}			
		}
	}

	@Test(expected = Exception.class)
	public void testSendErrorFromNull() throws Exception {
		this.email.send(null, to, cc, replyto, subject, body);
	}

	@Test(expected = Exception.class)
	public void testSendToNull() throws Exception {
		this.email.send(from, null, cc, replyto, subject, body);
	}

	@Test(expected = Exception.class)
	public void testSendCcNull() throws Exception {
		this.email.send(from, to, null, replyto, subject, body);
	}

	@Test(expected = Exception.class)
	public void testSendReplyToNull() throws Exception {
		this.email.send(from, to, cc, null, subject, body);
	}

	@Test(expected = Exception.class)
	public void testSendSubjectNull() throws Exception {
		this.email.send(from, to, cc, replyto, null, body);
	}

	@Test(expected = Exception.class)
	public void testSendBodyNull() throws Exception {
		this.email.send(from, to, cc, replyto, subject, null);
	}

    @After
    public void cleanup(){
        testSmtp.stop();
    }
}
