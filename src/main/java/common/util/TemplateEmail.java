package common.util;

import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

public class TemplateEmail {

    @Autowired
    private Email email;

    public void send(final String from, final String to, final String cc,
            final String replyto, final String subject, String body,
            final Map<String, String> templateParams) throws Exception {
        if (templateParams != null && body != null) {
            for (final String key : templateParams.keySet()) {
                body = body.replace(key, templateParams.get(key));
            }
        }
        email.send(from, to, cc, replyto, subject, body);
    }

    public void sendFromResource(final String from, final String to,
            final String cc, final String replyto, final String subject,
            final Resource body, final Map<String, String> templateParams)
            throws Exception {
        String bodyString = null;
        if (body != null && body.exists()) {
            bodyString = IOUtils.toString(body.getInputStream());
        }
        send(from, to, cc, replyto, subject, bodyString, templateParams);
    }

    public void setEmail(final Email email) {
        this.email = email;
    }
}
