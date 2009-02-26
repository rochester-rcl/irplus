/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  


package edu.ur.ir.web.action.user;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to enter new password
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ChangePassword extends ActionSupport implements UserIdAware {

	/** Eclipse generated Id */
	private static final long serialVersionUID = 8841773111377070719L;

	/**  Logger for add user action */
	private static final Logger log = Logger.getLogger(ChangePassword.class);

	/** Password token */
	private String token;

	/** New password */
	private String password;

	/**  User object */
	private Long userId;

	/** User changing the password*/
	private IrUser userToChangePassword;

	/** User service */
	private UserService userService;
	
	/** Indicates whether the token is valid or no t. */
	private boolean validToken = true;

	/**
	 * Execute method
	 */
	public String execute(){
		log.debug("Execute called");

		validToken = true;
		userToChangePassword = userService.getUserByToken(token); 
		
		if (userToChangePassword == null)
		{
			validToken = false;
			addFieldError("tokenDoesnotExist", 
					"Invalid link to change password.");
		} 
		
		return SUCCESS;
	}

	/**
	 * Change password. This when the user forgets the password, 
	 * clicks on forgot password link and the system sends email 
	 * with a token to change password.  
	 * 
	 */
	public String savePassword() {

		userToChangePassword = userService.getUserByToken(token); 
		
		userService.updatePassword(password, userToChangePassword); 
		
		// Reset the password token to null		
		userToChangePassword.setPasswordToken(null);
		userService.makeUserPersistent(userToChangePassword);
		
		// Automatically logins the user after changing the password
    	userService.authenticateUser(userToChangePassword, password, userToChangePassword.getRoles());
    	
		return SUCCESS;
	}

	/**
	 * Change password. This is when the admin creates the user and emails
	 * the account details, asking to change password on the first login.
	 * 
	 */
	public String changePassword() {
		
		log.debug("change Password");
		
		// loaded from session 
		userToChangePassword = userService.getUser(userId, false);
		
		// Set the password change to false 
		userToChangePassword.setPasswordChangeRequired(false);
		
		userService.updatePassword(password, userToChangePassword); 

		// Automatically logins the user after changing the password
    	userService.authenticateUser(userToChangePassword, password, userToChangePassword.getRoles());
    	
		return SUCCESS;
	}

	/**
	 * Get password token
     *
	 * @return token given to user 
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Set password token
     *
	 * @param token password token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Get User changing the password
     *
	 * @return User
	 */
	public IrUser getUserToChangePassword() {
		return userToChangePassword;
	}

	/**
	 * Set User changing the password
     *
	 * @param user
	 */
	public void setUserToChangePassword(IrUser user) {
		this.userToChangePassword = user;
	}

	/**
	 * Get User Service
     *
	 * @return User Service
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Set User service
     *
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns true if the token is valid
     *
	 * @return 
	 */
	public boolean isValidToken() {
		return validToken;
	}

	/**
	 * Set whether the token is valid
     *
	 * @param validToken
	 */
	public void setValidToken(boolean validToken) {
		this.validToken = validToken;
	}

	/**
	 * Get password entered by the user
     *
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set new password
     *
	 * @param password new password entered by user
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
