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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceInviteService;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.security.SecurityService;
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

	/* Permission types for the file */
	private List<IrClassTypePermission> classTypePermissions;

	/* ACL service */
	private  SecurityService securityService;
    
    /* list of bad emails */
	private List<String> badEmails;
	
	/* Permissions to be assigned while sharing the file */
	private List<Long> selectedPermissions = new ArrayList<Long>();	
	
	/* remove the user id */
	private Long removeUserId;




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
        
        classTypePermissions = this.orderPermissionsList(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
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
		
		// Create the list of permissions
		Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		for(Long id : selectedPermissions)
		{
			permissions.add(securityService.getIrClassTypePermissionById(id, false));
		}			
		
        if(emails != null )
        {
	 	    String[] emailArray = emails.trim().split(";");
	 	    List<String> emailList = Arrays.asList(emailArray);
	 	    try {
				badEmails = groupWorkspaceInviteService.inviteUsers(user, emailList, setAsOwner, permissions, groupWorkspace, inviteMessage);
			} catch (PermissionNotGrantedException e) {
				return "accessDenied";
			}
	 	    
        }
	    return SUCCESS;	
	}
	
	/** 
	 * Remove the user from the group workspace.
	 * 
	 * @return
	 */
	public String removeUser()
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
        
        IrUser removeUser = userService.getUser(removeUserId, false);
        
        if( removeUser != null )
        {
            GroupWorkspaceUser removeGroupWorkspaceUser = groupWorkspace.getUser(removeUser);
            if(removeGroupWorkspaceUser != null )
            {
            	groupWorkspace.remove(removeGroupWorkspaceUser);
            	groupWorkspaceService.save(groupWorkspace);
            }
        }
		return SUCCESS;	
	}
	
	/**
	 * Order the permissions in a perdictable manner.
	 * 
	 * @param permissionsToOrder
	 * @return
	 */
	private List<IrClassTypePermission> orderPermissionsList(List<IrClassTypePermission> permissionsToOrder)
	{
		List<IrClassTypePermission> orderedPermissions = new LinkedList<IrClassTypePermission>();
		IrClassTypePermission addFile = null;
		IrClassTypePermission edit = null;
		IrClassTypePermission read = null;
		
		for(IrClassTypePermission permission : permissionsToOrder)
		{
			if( permission.getName().equals("GROUP_WORKSPACE_EDIT"))
			{
				edit = permission;
			}
			else if( permission.getName().equals("GROUP_WORKSPACE_ADD_FILE"))
			{
				addFile = permission;
			}
			else if( permission.getName().equals("GROUP_WORKSPACE_READ"))
			{
				 read = permission;
			}
		}
		
		if( read != null )
		{
			orderedPermissions.add(read);
		}
		if( addFile != null )
		{
			orderedPermissions.add(addFile);
		}
		if( edit != null )
		{
			orderedPermissions.add(edit);
		}
		
		return orderedPermissions;
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
	
	/**
	 * Set the security service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	/**
	 * Get the class type permissions.
	 * 
	 * @return class type permissions
	 */
	public List<IrClassTypePermission> getClassTypePermissions() {
		return classTypePermissions;
	}
	
	/**
	 * Get the selected permissions.
	 * 
	 * @return
	 */
	public List<Long> getSelectedPermissions() {
		return selectedPermissions;
	}

	/**
	 * Set the selected permissions.
	 * 
	 * @param selectedPermissions
	 */
	public void setSelectedPermissions(List<Long> selectedPermissions) {
		this.selectedPermissions = selectedPermissions;
	}
	
	/**
	 * Get the remove user id.
	 * 
	 * @return
	 */
	public Long getRemoveUserId() {
		return removeUserId;
	}

	/**
	 * Set the remove user id.
	 * 
	 * @param removeUserId
	 */
	public void setRemoveUserId(Long removeUserId) {
		this.removeUserId = removeUserId;
	}


}
