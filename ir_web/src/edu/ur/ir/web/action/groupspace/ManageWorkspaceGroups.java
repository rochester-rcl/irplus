/**  
   Copyright 2008 - 2011 University of Rochester

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
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceGroup;
import edu.ur.ir.groupspace.GroupWorkspaceGroupService;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

/**
 * Allows users to manage workspace groups.
 * 
 * @author Nathan Sarr
 * 
 */

public class ManageWorkspaceGroups extends Pager implements UserIdAware {

	private static final long serialVersionUID = -7065002160251173177L;

	/* Name for the content type */
	private String name;

	/* description for the content type */
	private String description;

	/* Logger */
	private static final Logger log = Logger.getLogger(ManageWorkspaceGroups.class);

	/* service for dealing with group space information */
	private GroupWorkspaceGroupService groupWorkspaceGroupService;

	/* id of the user trying to make changes */
	private Long userId;

	/* Service for dealing with group space information */
	private UserService userService;

	/* Group space object */
	private GroupWorkspaceGroup workspaceGroup;
	
	/* Permission types for the file */
	private List<IrClassTypePermission> classTypePermissions;

	/* Permissions to be assigned while sharing the file */
	private List<Long> selectedPermissions = new ArrayList<Long>();	
	
	/* ACL service */
	private  SecurityService securityService;



	/* indicates successful action */
	private boolean success = true;

	/* id of the group to edit */
	private Long id;
	
	/* id of the group workspace */
	private Long groupWorkspaceId;
	
	/* Group workspace that has the groups */
	private GroupWorkspace groupWorkspace;
	
	/* Service to deal with group workspace information */
	private GroupWorkspaceService groupWorkspaceService;

	/* Message that can be displayed to the user. */
	private String message;

	/**
	 * Default constructor
	 */
	public ManageWorkspaceGroups() {
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	
	/**
	 * View all the groups for a given workspace
	 * @return
	 */
	public String viewGroups()
	{
		log.debug("get group workspace group id = " + id);
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		IrUser user = userService.getUser(userId, false);
		if( groupWorkspace.getIsOwner(user))
		{
			return SUCCESS;
		}
		else
		{
			workspaceGroup = null;
			return "accessDenied";
		}
	}

	/**
	 * Get a specific group space.
	 * 
	 * @return
	 */
	public String get() {
		log.debug("get group workspace group id = " + id);
		workspaceGroup = groupWorkspaceGroupService.get(id, false);
		groupWorkspace = workspaceGroup.getGroupWorkspace();
		IrUser user = userService.getUser(userId, false);
		classTypePermissions = this.orderPermissionsList(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
		if( workspaceGroup.getGroupWorkspace().getIsOwner(user))
		{
			return "get";
		}
		else
		{
			workspaceGroup = null;
			return "accessDenied";
		}
	}

	/**
	 * Delete the specified group space.
	 * 
	 * @return
	 */
	public String delete() {
		log.debug("delete group workspace group id = " + id );
		workspaceGroup = groupWorkspaceGroupService.get(id, false);
		groupWorkspace = workspaceGroup.getGroupWorkspace();

		IrUser user = userService.getUser(userId, false);
		if( groupWorkspace.getIsOwner(user))
		{
			groupWorkspace.remove(workspaceGroup);
			groupWorkspaceService.save(groupWorkspace);
			return "deleted";
		}
		else
		{
			return "accessDenied";
		}
	}

	/**
	 * Create a new user group.
	 * 
	 * @return
	 */
	public String create() 
	{
		log.debug("creating a group workspace group with name = " + name + " for group workspace id " + groupWorkspaceId);
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		IrUser user = userService.getUser(userId, false);
		if( groupWorkspace.getIsOwner(user))
		{
			try {
				workspaceGroup = groupWorkspace.createGroup(name);
				workspaceGroup.setDescription(description);
				groupWorkspaceGroupService.save(workspaceGroup);
			} catch (DuplicateNameException e) {
				success = false;
				message = getText("workspaceGroupNameError", 
						new String[]{name});
				addFieldError("workspaceGroupAlreadyExists", message);
			}
			
		}
		else
		{
			return "accessDenied";
		}
		return "added";
	}

	/**
	 * Update the group workspace.
	 * 
	 * @return success if the update is successful
	 * @throws DuplicateNameException
	 *             - if a duplicate name error occurs
	 */
	public String update() throws DuplicateNameException {
		
		log.debug("updating group space group = " + name + " id = " + id);
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		GroupWorkspaceGroup other = groupWorkspace.getGroup(name);
		if( other == null )
		{
			workspaceGroup.setName(name);
			workspaceGroup.setDescription(description);
			groupWorkspaceGroupService.save(workspaceGroup);
		}
		else
		{
			message = getText("groupWorkspaceGroupNameError", 
					new String[]{groupWorkspace.getName()});
			    addFieldError("groupWorkspaceAlreadyExists", message);
			success = false;
		}
		
		return "get";

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
	public void setGroupWorkspaceService(
			GroupWorkspaceService groupWorkspaceService) {
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
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Get the workspace group being worked on.
	 * 
	 * @return
	 */
	public GroupWorkspaceGroup getWorkspaceGroup() {
		return workspaceGroup;
	}


	/**
	 * Set the workspace group being worked on.
	 * 
	 * @param workspaceGroup
	 */
	public void setWorkspaceGroup(GroupWorkspaceGroup workspaceGroup) {
		this.workspaceGroup = workspaceGroup;
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
	 * Set the security service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}


	/**
	 * Get the selected permissions.
	 * 
	 * @return selected permissions by the user.
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
	 * Get the class type permissions.
	 * 
	 * @return class type permissions
	 */
	public List<IrClassTypePermission> getClassTypePermissions() {
		return classTypePermissions;
	}


	/**
	 * Get the message for the user
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	

	/**
	 * Set the group workspace group service.
	 * 
	 * @param groupWorkspaceGroupService
	 */
	public void setGroupWorkspaceGroupService(
			GroupWorkspaceGroupService groupWorkspaceGroupService) {
		this.groupWorkspaceGroupService = groupWorkspaceGroupService;
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
		IrClassTypePermission view = null;
		IrClassTypePermission edit = null;
		IrClassTypePermission addOnly = null;
		
		for(IrClassTypePermission permission : permissionsToOrder)
		{
			if( permission.getName().equals("FOLDER_EDIT"))
			{
				edit = permission;
			}
			else if( permission.getName().equals("FOLDER_ADD_FILE"))
			{
				addOnly = permission;
			}
			else if( permission.getName().equals("FOLDER_READ"))
			{
				 view = permission;
			}
		}
		
		if( view != null )
		{
			orderedPermissions.add(view);
		}
		if( addOnly != null )
		{
			orderedPermissions.add(addOnly);
		}
		if( edit != null )
		{
			orderedPermissions.add(edit);
		}
		
		return orderedPermissions;
	}

}
