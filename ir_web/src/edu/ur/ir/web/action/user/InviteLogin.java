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
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.groupspace.GroupWorkspaceEmailInvite;
import edu.ur.ir.groupspace.GroupWorkspaceInviteService;
import edu.ur.ir.invite.InviteToken;
import edu.ur.ir.invite.InviteTokenService;
import edu.ur.ir.user.FileInviteInfo;
import edu.ur.ir.user.InviteUserService;

/**
 * Action for invited user login
 * 
 * @author Sharmila Ranganathan
 *
 */
public class InviteLogin extends ActionSupport implements Preparable {

	/* Eclipse generated Id */
	private static final long serialVersionUID = 3356945932175988498L;	

	/*  Logger for add user action */
	private static final Logger log = Logger.getLogger(InviteLogin.class);

	/* Token to identify the invited user */
	private String token;

	/* check to see if the invite */
	private InviteToken inviteToken;

	/* invite token service */
	private InviteTokenService inviteTokenService;

	/* invite info information */
	private FileInviteInfo inviteInfo;
	
	/* invite user service */
	private InviteUserService inviteUserService;
	
	/* group workspace invite service */
	private GroupWorkspaceInviteService groupWorkspaceInviteService;
	
	/* email invite */
	private GroupWorkspaceEmailInvite groupWorkspaceEmailInvite;
	

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
		    inviteToken = inviteTokenService.getInviteToken(token);
		    if( inviteToken != null )
		    {
		    	groupWorkspaceEmailInvite = groupWorkspaceInviteService.getByToken(token);
		    	inviteInfo = inviteUserService.findInviteInfoByToken(token);
		    	
		    	// delete the token if it is not attached to an invite 
		    	if(groupWorkspaceEmailInvite == null && inviteInfo == null)
		    	{
		    		inviteTokenService.delete(inviteToken);
		    		inviteToken = null;
		    		token = null;
		    	}
		    }
		}
		
		if (inviteToken == null)
		{
			token = null;
			addFieldError("tokenDoesnotExist", 
					"Invalid invitation. Either the file is already shared with the user or the invite is invalid.");
		}
	}

	/**
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}


	public String getMessage() {
		return message;
	}
	
	/**
	 * Set the invite token 
	 * 
	 * @param inviteTokenService
	 */
	public void setInviteTokenService(InviteTokenService inviteTokenService) {
		this.inviteTokenService = inviteTokenService;
	}
	
	/**
	 * Get the invite info.
	 * 
	 * @return
	 */
	public FileInviteInfo getInviteInfo() {
		return inviteInfo;
	}

	/**
	 * Set the user invite service.
	 * 
	 * @param inviteUserService
	 */
	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

	/**
	 * Get the group workspace email invite.
	 * 
	 * @return
	 */
	public GroupWorkspaceEmailInvite getGroupWorkspaceEmailInvite() {
		return groupWorkspaceEmailInvite;
	}


	/**
	 * Set the group workspace invite service.
	 * 
	 * @param groupWorkspaceInviteService
	 */
	public void setGroupWorkspaceInviteService(
			GroupWorkspaceInviteService groupWorkspaceInviteService) {
		this.groupWorkspaceInviteService = groupWorkspaceInviteService;
	}

	/**
	 * Get the token.
	 * 
	 * @return
	 */
	public String getToken() {
		return token;
	}

}
