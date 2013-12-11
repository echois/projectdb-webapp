package common.util;

import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

public class TemplateEmail {
	
	@Autowired
	private Email email;
	
	public void send(String from, String to, String cc,
		String replyto, String subject, String body, Map<String, String> templateParams) throws Exception {
		if (templateParams != null && body != null) {
			for (String key: templateParams.keySet()) {
				body = body.replace(key, templateParams.get(key));
			}
		}
		this.email.send(from, to, cc, replyto, subject, body);
	}

	public void sendFromResource(String from, String to, String cc,
		String replyto, String subject, Resource body, Map<String, String> templateParams) throws Exception {
		String bodyString = null;
		if (body != null && body.exists()) {
			bodyString = IOUtils.toString(body.getInputStream());
		}
        this.send(from, to, cc, replyto, subject, bodyString, templateParams);
	}

	public void setEmail(Email email) {
		this.email = email;
	}
}
