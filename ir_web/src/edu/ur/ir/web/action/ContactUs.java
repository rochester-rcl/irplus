package edu.ur.ir.web.action;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.PublicHelpService;

/**
 * Action to help send email when users are asking for help.
 * 
 * 
 * @author Nathan Sarr
 *
 */
public class ContactUs extends ActionSupport{

	/** generated version id */
	private static final long serialVersionUID = 2637166611473974881L;
	
	/** Message the user wishes to send */
	private String message;
	
	/** from address  */
	private String from;
	
	/** subject of the message */
	private String subject;
	
	private String reason;
	

	/** Service to help deal with help */
	private PublicHelpService publicHelpService;
	
	/**  Logger for class */
	private static final Logger log = Logger.getLogger(ContactUs.class);
	
	/**
	 * Send the email
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		log.debug("other testing");
		log.debug("Sending email message subject = " + subject + " from = " + from + " message = " + message);
		if( reason == null || reason.trim().equals("")){
			try {
				publicHelpService.sendHelpEmail(subject, from, message);
			} catch (Exception e) {
				// don't send error
			}
			
		}
		else {
			log.debug("NOT sending email");
		}
		
		return SUCCESS;
	}

	/**
	 * Validate the data.
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	public void validate()
	{
		if( message == null || message.trim().equals("") )
		{
			this.addFieldError("message", "Must enter a message");
		}
		
		if( from == null || from.trim().equals("") )
		{
			this.addFieldError("from", "Must enter an email");
		}
		
		if( subject == null || subject.trim().equals("") )
		{
			this.addFieldError("subject", "Must enter a subject");
		}
		

	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public PublicHelpService getPublicHelpService() {
		return publicHelpService;
	}

	public void setPublicHelpService(PublicHelpService publicHelpService) {
		this.publicHelpService = publicHelpService;
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}


}
