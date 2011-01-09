package edu.ur.ir.util.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import edu.ur.ir.ErrorEmailService;

/**
 * Default implementation of the Error Email Service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultErrorEmailService implements ErrorEmailService{

    /** eclipse generated id */
	private static final long serialVersionUID = -3929770443839230101L;

	/** Java mail sender to send emails */
    private JavaMailSender mailSender;
    
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultErrorEmailService.class);
	
	/** Subject line for the email */
	private String subject;
	
	/** email address to send the errors to*/
	private String toAddress;
	
	/** indicates if emails should be sent */
	private boolean sendEmails = true;
	

	/**
	 * Send the error to the specified address.
	 * 
	 * @see edu.ur.ir.ErrorEmailService#sendError(java.lang.String, java.lang.String)
	 */
	public void sendError( String error) {
		log.info("send emails = " + sendEmails);
		if( sendEmails )
		{
		    log.debug("send subscribers emails");
		    MimeMessage message = mailSender.createMimeMessage();
		    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
		    try 
		    {
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setTo(toAddress);
				mimeMessageHelper.setText(error);
			    mailSender.send(message);
		    } 
		    catch (MessagingException e) 
		    {
			    log.error("Messaging exception occured ", e);
		    }
		}
		else
		{
			log.info( "send emails disabled ");
			log.error(error);
		}
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	
	public void setSendEmails(boolean sendEmails) {
		this.sendEmails = sendEmails;
	}
    
	public boolean getSendEmails()
	{
		return sendEmails;
	}

	
	/**
	 * Email the specified error.
	 * 
	 * @see edu.ur.ir.ErrorEmailService#sendError(java.lang.Error)
	 */
	public void sendError(Exception e) {
		StringWriter sw = null;
		PrintWriter pw = null;
		
		sw = new StringWriter();
		pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		sw.flush();
		pw.close();
		
		sendError(sw.toString());
	}


}
