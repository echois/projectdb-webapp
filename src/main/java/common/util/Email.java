package common.util;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class Email {

	@Autowired
	private MailSender mailSender;
	private EmailValidator emailValidator = EmailValidator.getInstance();

    public void send (String from, String to, String cc, String replyto,
    	String subject, String body) throws Exception {
    	this.validateEmailAddress(from);
    	this.validateEmailAddress(replyto);
    	this.validateEmailAddress(to);
    	this.validateEmailAddress(cc);
    	if (subject == null) {
    		throw new Exception("Subject must not be null");
    	}
    	if (body == null) {
    		throw new Exception("Body must not be null");
    	}
    	SimpleMailMessage mailMessage = new SimpleMailMessage();
    	mailMessage.setFrom(from);
    	mailMessage.setTo(to);
    	mailMessage.setCc(cc);
    	mailMessage.setReplyTo(replyto);
    	mailMessage.setSubject(subject);
    	mailMessage.setText(body);
    	this.mailSender.send(mailMessage);
    }

    protected void validateEmailAddress(String address) throws Exception {
        if (!this.emailValidator.isValid(address)) {
        	throw new Exception("Invalid e-mail address: " + address);
        }
    }

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
    
}
