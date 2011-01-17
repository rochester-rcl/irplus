package edu.ur.ir;

import java.io.Serializable;

/**
 * Service for dealing with users who need help
 * 
 * @author Nathan Sarr
 *
 */
public interface PublicHelpService extends Serializable {
	

	/**
	 * Sends an email with the specified subject and message 
	 * 
	 * @param subject - subject of the email
	 * @param from - email address of user who is sending the email
	 * @param message - message 
	 */
	public void sendHelpEmail(String subject, String from,  String message);

}
