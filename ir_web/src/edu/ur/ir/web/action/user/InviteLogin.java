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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.user.FileInviteInfo;
import edu.ur.ir.user.InviteUserService;

/**
 * Action for invited user login
 * 
 * @author Sharmila Ranganathan
 *
 */
public class InviteLogin extends ActionSupport implements Preparable {

	/** Eclipse generated Id */
	private static final long serialVersionUID = 3356945932175988498L;	

	/**  Logger for add user action */
	private static final Logger log = LogManager.getLogger(InviteLogin.class);

	/** Token to identify the invited user */
	private String token;

	/** Invite information */
	private FileInviteInfo inviteInfo;

	/** Invite service */
	private InviteUserService inviteUserService;
	
	/** Message that can be displayed to the user. */
	private String message;

	/**
	 * Prepare the ur published object
	 * 
	 * @see com.opensymphony.xwork.Preparable#prepare()
	 */
	public void prepare(){
		log.debug("prepare called");
		log.debug("token = " + token);
		
		if( token != null )
		{	
		    inviteInfo = inviteUserService.findInviteInfoByToken(token); 
		}
		
		
		if (inviteInfo == null)
		{
			token = null;
			addFieldError("tokenDoesnotExist", 
					"Invalid invitation. Either the file is already shared with the user or the invite is invalid.");
		}
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public FileInviteInfo getInviteInfo() {
		return inviteInfo;
	}

	public void setInviteInfo(FileInviteInfo inviteInfo) {
		this.inviteInfo = inviteInfo;
	}

	public InviteUserService getInviteUserService() {
		return inviteUserService;
	}

	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
