package edu.ur.ir.util.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import edu.ur.ir.PublicHelpService;

/**
 * Implementation of help service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultPublicHelpService implements PublicHelpService{

    /** eclipse generated id */
	private static final long serialVersionUID = -6937481628862884612L;

	/** Java mail sender to send emails */
    private transient JavaMailSender mailSender;
    
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultPublicHelpService.class);
	
	/** email address to send the help email to*/
	private String toAddress;
	
	/**
	 * Send the help email.
	 * 
	 * @see edu.ur.ir.PublicHelpService#sendHelpEmail(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void sendHelpEmail(String subject, String from, String message) {
		log.debug("send subscribers emails");
		 MimeMessage mimeMessage = mailSender.createMimeMessage();
		 MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		 try 
		 {
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setFrom(from);
				mimeMessageHelper.setTo(toAddress);
				mimeMessageHelper.setText(message);
			    mailSender.send(mimeMessage);
		 } 
		 catch (MessagingException e) 
		 {
			log.error("Messaging exception occured ", e);
		 }
		
		
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

}
