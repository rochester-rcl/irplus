package edu.ur.ir;

/**
 * This service is designed to send emails when an error occurs.
 * 
 * @author Nathan Sarr
 *
 */
public interface ErrorEmailService {
	

	/**
	 * Send the error to the specified toAddress.
	 * 
	 * @param toAddress - email address to send the error to
	 * @param error - error string to send.
	 */
	public void sendError(String toAddress, String error);

}
