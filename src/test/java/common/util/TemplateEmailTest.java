package common.util;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class TemplateEmailTest {
	
	private TemplateEmail templateEmail = new TemplateEmail();
	private Resource template;
	
	private String from = "from@test.org";
	private String to = "to@test.org";
	private String cc = "cc@test.org";
	private String replyto = "replyto@test.org";
	private String subject = "subject";
	private String templateBody = "test ${param} test";
	private String expectedBody = "test hello, world! test";
	private String paramKey = "${param}";
	private String paramValue = "hello, world!";
	private Map<String,String> templateParams = new HashMap<String,String>();

	@Before
	public void SetUp() {
		templateParams.put(paramKey, paramValue);
		template = new ClassPathResource("template_email.tpl");
	}
	
	@Test
	public void testSendFromTemplate() throws Exception {
		Email emailMock = mock(Email.class);
		this.templateEmail.setEmail(emailMock);
		this.templateEmail.send(from, to, cc, replyto, subject, templateBody, templateParams);
		verify(emailMock).send(from, to, cc, replyto, subject, expectedBody);
	}

	@Test
	public void testSendFromTemplateResource() throws Exception {
		Email emailMock = mock(Email.class);
		this.templateEmail.setEmail(emailMock);
		this.templateEmail.sendFromResource(from, to, cc, replyto, subject, this.template, templateParams);
		verify(emailMock).send(from, to, cc, replyto, subject, expectedBody);
	}

}
