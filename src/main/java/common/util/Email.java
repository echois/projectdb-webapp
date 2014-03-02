package common.util;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class Email {

    private final EmailValidator emailValidator = EmailValidator.getInstance();
    @Autowired
    private MailSender mailSender;

    public void send(final String from, final String to, final String cc,
            final String replyto, final String subject, final String body)
            throws Exception {
        validateEmailAddress(from);
        validateEmailAddress(replyto);
        validateEmailAddress(to);
        validateEmailAddress(cc);
        if (subject == null) {
            throw new Exception("Subject must not be null");
        }
        if (body == null) {
            throw new Exception("Body must not be null");
        }
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setCc(cc);
        mailMessage.setReplyTo(replyto);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailSender.send(mailMessage);
    }

    public void setMailSender(final MailSender mailSender) {
        this.mailSender = mailSender;
    }

    protected void validateEmailAddress(final String address) throws Exception {
        if (!emailValidator.isValid(address)) {
            throw new Exception("Invalid e-mail address: " + address);
        }
    }

}
