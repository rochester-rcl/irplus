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

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceEmailInvite;
import edu.ur.ir.groupspace.GroupWorkspaceInviteService;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Allows a user to manage user group workspaces.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageUserGroupWorkspaces extends Pager implements UserIdAware{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 9102093257134215694L;

	/* Name for the content type */
	private String name;

	/* description for the content type */
	private String description;
	
	/* Logger */
	private static final Logger log = Logger.getLogger(ManageGroupWorkspaces.class);
	
	/* service for dealing with group space information */
	private GroupWorkspaceService groupWorkspaceService;

	/* id of the user trying to make changes */
	private Long userId;

	/* Service for dealing with group space information */
	private UserService userService;
	
	/* Group space object */
	private GroupWorkspace groupWorkspace;

	/* indicates successful action */
	private boolean success = true;
	
	/* list of groupspaces */
	private List<GroupWorkspace> groupWorkSpaces;

	/* id of the groupspace to edit */
	private Long groupWorkspaceId;

	/* Message that can be displayed to the user. */
	private String message;

	/* ACL service */
	private  SecurityService securityService;
	
	/* Permissions to be assigned for the group */
	private List<Long> selectedPermissions = new ArrayList<Long>();	
	
    /* emails of users to invite */
    private String emails;
    
    /* determines if the users should be set as an owner. */
    private boolean setAsOwner = false;

	/* Message that is sent to the user in the email. */
	private String inviteMessage;

    /* list of bad emails */
	private List<String> badEmails;

	/* service to deal with group workspace invite information */
	private GroupWorkspaceInviteService groupWorkspaceInviteService;

	/* id of an invite for a user */
	private Long inviteId;

	/* remove the user id */
	private Long removeUserId;
	
	private String tabName = "WORKSPACE";





	/**
	 * Default constructor
	 */
	public ManageUserGroupWorkspaces()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Initial load of all group spaces
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		log.debug("view group workspaces");
		groupWorkSpaces = groupWorkspaceService.getGroupWorkspacesNameOrder(rowStart, numberOfResultsToShow, OrderType.ASCENDING_ORDER);
		return SUCCESS;
	}
	
	/**
	 * Get a specific group space.
	 * 
	 * @return
	 */
	public String get()
	{
		log.debug("get a group space with id = " +  groupWorkspaceId);
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		
		IrUser user = userService.getUser(userId, false);
		// only owners and admins can delete  group workspaces
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		
		log.debug("workspace user = " + workspaceUser);
		if( !user.hasRole(IrRole.ADMIN_ROLE) )
		{
		    if( workspaceUser == null || !workspaceUser.isOwner() )	
		    {
		    	return "accessDenied";
		    }
		}
	    return "get";
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
			log.debug("adding permission " + securityService.getIrClassTypePermissionById(id, false));
			permissions.add(securityService.getIrClassTypePermissionById(id, false));
		}			
		
        if(emails != null )
        {
        	log.debug("set as owner = " + setAsOwner);
	 	    String[] emailArray = emails.trim().split(";");
	 	    List<String> emailList = Arrays.asList(emailArray);
	 	    try {
				badEmails = groupWorkspaceInviteService.inviteUsers(user, emailList, setAsOwner, permissions, groupWorkspace, inviteMessage);
			    if( badEmails.size() <= 0 )
			    {
			    	tabName = "USERS";
			    }
	 	    } catch (PermissionNotGrantedException e) {
				return "accessDenied";
			}
	 	    
        }
       
	    return SUCCESS;	
	}
	
	/**
	 * Delete the specified group space.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("deleting a group space with id = " +  groupWorkspaceId);
		IrUser user = userService.getUser(userId, false);
		// only owners and admins can delete  group workspaces
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId,false);
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		
		log.debug("workspace user = " + workspaceUser);
		if( !user.hasRole(IrRole.ADMIN_ROLE) )
		{
		    if( workspaceUser == null || !workspaceUser.isOwner() )	
		    {
		    	return "accessDenied";
		    }
		}
	    
	    groupWorkspaceService.delete(groupWorkspace, user);
	    groupWorkSpaces = groupWorkspaceService.getGroupWorkspacesNameOrder(rowStart, numberOfResultsToShow, OrderType.ASCENDING_ORDER);

		return "deleted";
	}
	
	/**
	 * Create a new user group workspace.
	 * 
	 * @return
	 * @throws DuplicateNameException 
	 */
	public String create() throws DuplicateNameException
	{
		log.debug("creating a group space with name = " + name);
		IrUser user = userService.getUser(userId, false);
		
		if( user == null || !user.hasRole(IrRole.ADMIN_ROLE) )
		{
			return "accessDenied";
		}
		GroupWorkspace other = groupWorkspaceService.get(name);
		if( other == null )
		{
			groupWorkspace = new GroupWorkspace(name, description);
			groupWorkspace.add(user, true);
		    groupWorkspaceService.save(groupWorkspace);
		    securityService.assignOwnerPermissions(groupWorkspace, user);
		} 
		else
		{
			success = false;
			message = getText("groupWorkspaceNameError", 
					new String[]{name});
			addFieldError("groupWorkspaceAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Update the group workspace.
	 * 
	 * @return success if the update is successful
	 * @throws DuplicateNameException - if a duplicate name error occurs
	 */
	public String update() 
	{
		log.debug("updating group space  = " + name + " id = " + groupWorkspaceId);
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		
		IrUser user = userService.getUser(userId, false);
		// only owners and admins can delete  group workspaces
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		
		
		
		log.debug("workspace user = " + workspaceUser);
		if( !user.hasRole(IrRole.ADMIN_ROLE) )
		{
		    if( workspaceUser == null || !workspaceUser.isOwner() )	
		    {
		    	return "accessDenied";
		    }
		}
		
		GroupWorkspace other = groupWorkspaceService.get(name);
		if( other == null )
		{
			groupWorkspace.setName(name);
			groupWorkspace.setDescription(description);
			groupWorkspaceService.save(groupWorkspace);
		}
		else
		{
			message = getText("groupWorkspaceNameError", 
					new String[]{groupWorkspace.getName()});
			    addFieldError("groupWorkspaceAlreadyExists", message);
			success = false;
		}
		
		return "get";

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
        
        if( removeUser != null && !removeUser.equals(user))
        {
            groupWorkspaceService.removeUserFromGroup(removeUser, groupWorkspace);
        }

        tabName = "USERS";
		return SUCCESS;	
	}
	
	/**
	 * Remove the invite from the group workspace.
	 * 
	 * @return
	 */
	public String removeInvite()
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
		    
		//User must be owner to remove others
	    if( groupWorkspaceUser == null || !groupWorkspaceUser.isOwner())
	    {
	        return "accessDenied";
	    }
		
		GroupWorkspaceEmailInvite invite = groupWorkspaceInviteService.getEmailInviteById(inviteId, false);
		groupWorkspace.deleteEmailInvite(invite.getInviteToken().getEmail());
		groupWorkspaceService.save(groupWorkspace);
		tabName = "USERS";
		return SUCCESS;
	}
	/**
	 * Get the total hits.
	 * 
	 * @see edu.ur.ir.web.table.Pager#getTotalHits()
	 */
	public int getTotalHits() {
		return groupWorkspaceService.getCount().intValue();
	}

	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the description.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * Set the group space service
	 * 
	 * @param groupSpaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}

	/**
	 * Indicates a successful action
	 * 
	 * @return
	 */
	public boolean getSuccess() {
		return success;
	}

	/**
	 * Get a list of the groupspaces.
	 * 
	 * @return
	 */
	public List<GroupWorkspace> getGroupWorkspaces() 
	{
		return groupWorkSpaces;
	}
	
	/**
	 * Get the group space
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}

	/**
	 * Set the id of the group space to load.
	 * 
	 * @param id
	 */
	public void setGroupWorkspaceId(Long id) {
		this.groupWorkspaceId = id;
	}
	
	/**
	 * Get the class type permissions.
	 * 
	 * @return class type permissions
	 */
	public List<IrClassTypePermission> getClassTypePermissions() {
		List<IrClassTypePermission> classTypePermissions;
		classTypePermissions = this.orderPermissionsList(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
		return classTypePermissions;
	}
	
	/**
	 * Order the permissions in a predictable manner.
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
	 * Set the security service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	/**
	 * Permissions selected when inviting users.
	 * 
	 * @return
	 */
	public List<Long> getSelectedPermissions() {
		return selectedPermissions;
	}

	/**
	 * Permissions selected when users are invited
	 * @param selectedPermissions
	 */
	public void setSelectedPermissions(List<Long> selectedPermissions) {
		this.selectedPermissions = selectedPermissions;
	}
	
	/**
	 * List of emails of invited users.
	 * 
	 * @return
	 */
	public String getEmails() {
		return emails;
	}

	/**
	 * Set of emails for invited users.
	 * 
	 * @param emails
	 */
	public void setEmails(String emails) {
		this.emails = emails;
	}
	
	/**
	 * Set the user or users as an owner.
	 * @return
	 */
	public boolean isSetAsOwner() {
		return setAsOwner;
	}

	/**
	 * Set the user or users as the owner.
	 * 
	 * @param setAsOwner
	 */
	public void setSetAsOwner(boolean setAsOwner) {
		this.setAsOwner = setAsOwner;
	}
	
	/**
	 * Get the invite message of the user doing the invitiing.
	 * 
	 * @return
	 */
	public String getInviteMessage() {
		return inviteMessage;
	}

	/**
	 * Set the invite message from the user doing the inviting.
	 * @param inviteMessage
	 */
	public void setInviteMessage(String inviteMessage) {
		this.inviteMessage = inviteMessage;
	}
	
	/**
	 * List of bad emails for users.
	 * 
	 * @return
	 */
	public List<String> getBadEmails() {
		return badEmails;
	}

	/**
	 * Set the bad emails found on invite
	 * @param badEmails
	 */
	public void setBadEmails(List<String> badEmails) {
		this.badEmails = badEmails;
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
	 * Get the invite id passed in.
	 * 
	 * @return
	 */
	public Long getInviteId() {
		return inviteId;
	}

	/**
	 * Set the invite id passed in.
	 * 
	 * @param inviteId
	 */
	public void setInviteId(Long inviteId) {
		this.inviteId = inviteId;
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

	/**
	 * Get the tab name.
	 * 
	 * @return
	 */
	public String getTabName() {
		return tabName;
	}

	/**
	 * Set the tab name.
	 * 
	 * @param tabName
	 */
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
}
