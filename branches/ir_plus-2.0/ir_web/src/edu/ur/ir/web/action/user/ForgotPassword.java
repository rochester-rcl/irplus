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

/**
 * Action to handle forgot password 
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ForgotPassword extends ActionSupport {

	/** Eclipse generated Id */
	private static final long serialVersionUID = 7491973408867996093L;

	/**  Logger for add user action */
	private static final Logger log = Logger.getLogger(ForgotPassword.class);

	/** Email id to send the password details */
	private String email;

	/** User service */
	private UserService userService;
	
	/** Indicates whether the password token is added or not. */
	private boolean added = false;

	/** Error message to the user  */
	private String message;

	/**
	 * Execute method
	 * 
	 */
	public String execute(){
		log.debug("Execute called");
		added = false;
		
		IrUser user = userService.getUserByEmail(email); 
		
		if (user == null)
		{
			message = getText("emailDoesnotExist");
			addFieldError("emailDoesnotExist", 
					"The Email id doesnot exist in the system" + email);
		} else {
			String token = userService.savePasswordToken(user);
			userService.sendEmailForForgotPassword(token, email);
			added = true;
		}
		
		return SUCCESS;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public boolean isAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



}
