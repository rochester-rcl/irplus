/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.web.action.groupspace;

import java.util.Arrays;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceInviteService;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Allows users to invite users to the group workspace.
 * 
 * @author Nathan Sarr
 *
 */
public class InviteUsersToGroupWorkspace extends ActionSupport implements UserIdAware{

	/* eclipse generated id */
	private static final long serialVersionUID = -4039144831000805557L;

	/* id of the user */
	private Long userId;
	
	/* group workspace id to add users to */
	private Long groupWorkspaceId;
	
	/* service for dealing with group space information */
	private GroupWorkspaceService groupWorkspaceService;
	
	/* service to deal with group workspace invite information */
	private GroupWorkspaceInviteService groupWorkspaceInviteService;

	/* group workspace */
	private GroupWorkspace groupWorkspace;

	/* service to deal with user information.*/
    private UserService userService;
    
    /* emails of users to invite */
    private String emails;
    
    /* determines if the users should be set as an owner. */
    private boolean setAsOwner = false;
	
	/* Message that is sent to the user in the email. */
	private String inviteMessage;

    private List<String> badEmails;

	/**
	 * Allows a user to view a page to invite users to a group workspace.
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		IrUser user = userService.getUser(userId, false);
		
		if( groupWorkspaceId == null )
		{
			return "notFound";
		}
		
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		
		if( groupWorkspace == null )
		{
			return "notFound";
		}
		
		GroupWorkspaceUser groupWorkspaceUser = groupWorkspace.getUser(user);
	    
		//User must be owner to invite others
        if( groupWorkspaceUser == null || !groupWorkspaceUser.isOwner())
        {
        	return "accessDenied";
        }
		return SUCCESS;
	}
	
	/**
	 * Addes users to the group workspace.
	 * 
	 * @return
	 */
	public String inviteUsers()
	{
        IrUser user = userService.getUser(userId, false);
		
		if( groupWorkspaceId == null )
		{
			return "notFound";
		}
		
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		
		if( groupWorkspace == null )
		{
			return "notFound";
		}
		
		GroupWorkspaceUser groupWorkspaceUser = groupWorkspace.getUser(user);
	    
		//User must be owner to invite others
        if( groupWorkspaceUser == null || !groupWorkspaceUser.isOwner())
        {
        	return "accessDenied";
        }
        
        if(emails != null )
        {
	 	    String[] emailArray = emails.trim().split(";");
	 	    List<String> emailList = Arrays.asList(emailArray);
	 	    try {
				badEmails = groupWorkspaceInviteService.inviteUsers(user, emailList, setAsOwner, null, groupWorkspace, inviteMessage);
			} catch (PermissionNotGrantedException e) {
				return "accessDenied";
			}
	 	    
        }
	    return SUCCESS;	
	}
	
	/**
	 * Inject the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the group workspace id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceId() {
		return groupWorkspaceId;
	}

	/**
	 * Set the group workspace id.
	 * 
	 * @param groupWorkspaceId
	 */
	public void setGroupWorkspaceId(Long groupWorkspaceId) {
		this.groupWorkspaceId = groupWorkspaceId;
	}
	
	/**
	 * Set the group workspace service.
	 * 
	 * @param groupWorkspaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}


	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Get the group workspace.
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}
	
	/**
	 * List of emails to invite to group workspace.
	 * 
	 * @return
	 */
	public String getEmails() {
		return emails;
	}

	/**
	 * Emails to invite to group workspace.
	 * 
	 * @param emails
	 */
	public void setEmails(String emails) {
		this.emails = emails;
	}
	
	/**
	 * Service to deal with group workspace invitations.
	 * 
	 * @param groupWorkspaceInviteService
	 */
	public void setGroupWorkspaceInviteService(
			GroupWorkspaceInviteService groupWorkspaceInviteService) {
		this.groupWorkspaceInviteService = groupWorkspaceInviteService;
	}

	/**
	 * Set the users as an owner of the project.
	 * 
	 * @return
	 */
	public boolean getSetAsOwner() {
		return setAsOwner;
	}

	/**
	 * Set the users as an owner of the group.
	 * 
	 * @param setAsOwner
	 */
	public void setSetAsOwner(boolean setAsOwner) {
		this.setAsOwner = setAsOwner;
	}
	
	/**
	 * Get the invite message.
	 * 
	 * @return
	 */
	public String getInviteMessage() {
		return inviteMessage;
	}

	/**
	 * Set the invite message.
	 * 
	 * @param inviteMessage
	 */
	public void setInviteMessage(String inviteMessage) {
		this.inviteMessage = inviteMessage;
	}
	
	/**
	 * Get the list of bad emails.
	 * 
	 * @return
	 */
	public List<String> getBadEmails() {
		return badEmails;
	}

}
