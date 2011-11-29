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

import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.groupspace.UserHasParentFolderPermissionsException;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Allows a user to view and edit group workspace folder properties.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageGroupWorkspaceFolderProperties extends ActionSupport 
implements  UserIdAware{

	/*eclipse generated id*/
	private static final long serialVersionUID = -5916633281950319826L;
	
	/* id of the user accessing the information */
	private Long userId;
	
	/* id of the user to edit permissions for */
	private Long editUserPermissionsId;
	
	/* user to edit permissions on */
	private IrUser editUser;	

	/* id of the group workspace folder */
	private Long groupWorkspaceFolderId;
	
	/* folder for the group workspace */
	private GroupWorkspaceFolder groupWorkspaceFolder;
	
	/* group workspace file system service */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;

	/* security information */
	private SecurityService securityService;

	/* access control list for the folder */
	private IrAcl folderAcl;
	
	/* service to deal with user information */
	private UserService userService;
	
	/* set of folders that are the path for the current folder */
    private Collection <GroupWorkspaceFolder> folderPath;

    /* access control entry for the user */
    private IrUserAccessControlEntry editUserAcl;
    
    /* list of folder permissions */
    private String[] folderPermissions;
    
    /* apply the permission to children */
    private boolean applyToChildren = false;

	/*  Logger for managing content types*/
	private static final Logger log = Logger.getLogger(ManageGroupWorkspaceFolderProperties.class);

	/**
	 * Allow a user to view folder properties.
	 * 
	 */
	public String execute()
	{
		IrUser user = userService.getUser(userId, false);
		
		if( groupWorkspaceFolderId != null )
		{
			groupWorkspaceFolder = groupWorkspaceFileSystemService.getFolder(groupWorkspaceFolderId, false);
		}
		
		if( groupWorkspaceFolder != null )
		{
			
			log.debug(" security service has permission = " + securityService.hasPermission(groupWorkspaceFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION));
			if( user == null || 
				securityService.hasPermission(groupWorkspaceFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) <= 0)
			{
				return "accessDenied";
			}
			folderAcl = securityService.getAcl(groupWorkspaceFolder);
			folderPath = groupWorkspaceFileSystemService.getFolderPath(groupWorkspaceFolder);
		    return SUCCESS;
	    }
		else
		{
			return "notFound";
		}
	}
	
	/**
	 * Allow a user to view and edit the permissions for a given user.
	 * 
	 * @return
	 */
	public String editUserPermissions()
	{
        IrUser user = userService.getUser(userId, false);
		
        if( user == null )
        {
        	return "accessDenied";
        }
		if( groupWorkspaceFolderId != null )
		{
			groupWorkspaceFolder = groupWorkspaceFileSystemService.getFolder(groupWorkspaceFolderId, false);
		}
		if( groupWorkspaceFolder != null )
		{
			// only owners can make permission changes to folders
			GroupWorkspace groupWorkspace = groupWorkspaceFolder.getGroupWorkspace();
			GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
			if( !workspaceUser.isOwner() )
			{
				return "accessDenied";
			}
			editUser = userService.getUser(editUserPermissionsId, false);
			
			
			if( editUser == null )
			{
				return "userNotFound";
			}
			IrAcl userAcl = securityService.getAcl(groupWorkspaceFolder, editUser);
			editUserAcl = userAcl.getUserAccessControlEntryByUserId(editUser.getId());
			log.debug("editUserAcl = " + editUserAcl);
			folderPath = groupWorkspaceFileSystemService.getFolderPath(groupWorkspaceFolder);
			
		}
		else
		{
			return "notFound";
		}
		return SUCCESS;
	}
	
	/**
	 * Save the selected permissions for the user.
	 * 
	 * @return
	 */
	public String saveUserPermissions()
	{
		boolean error = false;
        IrUser user = userService.getUser(userId, false);
		
        if( user == null )
        {
        	return "accessDenied";
        }
		if( groupWorkspaceFolderId != null )
		{
			groupWorkspaceFolder = groupWorkspaceFileSystemService.getFolder(groupWorkspaceFolderId, false);
		}
		if( groupWorkspaceFolder != null )
		{
			// only owners can make permission changes to folders
			GroupWorkspace groupWorkspace = groupWorkspaceFolder.getGroupWorkspace();
			GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
			if( !workspaceUser.isOwner() )
			{
				return "accessDenied";
			}
			editUser = userService.getUser(editUserPermissionsId, false);
			
			
			if( editUser == null )
			{
				return "userNotFound";
			}
			
			log.debug("printing folder permissions ");
			HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
			
			if( folderPermissions != null )
			{
			    for(String permission : folderPermissions)
			    {
				    log.debug("permission = " + permission);
				    if(permission.equals(GroupWorkspaceFolder.FOLDER_READ_PERMISSION) )
				    {
				    	IrClassTypePermission read = securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_READ_PERMISSION);
				    	if( read == null )
				    	{
				    		throw new IllegalStateException("read permission is null");
				    	}
					    permissions.add(read);
				    }
				
				    if( permission.equals(GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION))
				    {
				    	IrClassTypePermission addFile = securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION);
				    	if( addFile == null )
				    	{
				    		throw new IllegalStateException("add file permission is null");
				    	}
				    	permissions.add(addFile);
				    }
				
				    if( permission.equals(GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION))
				    {
				    	IrClassTypePermission editFolder = securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION);
				    	if( editFolder == null )
				    	{
				    		throw new IllegalStateException("edit folder permission is null");
				    	}
				    	permissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION));
				    }
			    }
			}
			
			try {
				groupWorkspaceFileSystemService.changeUserPermissionsForFolder(editUser, groupWorkspaceFolder, permissions, applyToChildren);
			} catch (UserHasParentFolderPermissionsException e) {
				addFieldError("parentFolderPermissionsError", 
						"A parent folder has permissions which would override the permissions choosen please check parent folders for edit permissions");
				error = true;
			}
			IrAcl userAcl = securityService.getAcl(groupWorkspaceFolder, editUser);
			editUserAcl = userAcl.getUserAccessControlEntryByUserId(editUser.getId());
			folderPath = groupWorkspaceFileSystemService.getFolderPath(groupWorkspaceFolder);
		}
		else
		{
			return "notFound";
		}
		
		
		if( !error )
		{
		    return SUCCESS;
		}
		else
		{
			return ERROR;
		}
	}
	
	/**
	 * Get the folder access control entry.
	 * 
	 * @return
	 */
	public IrAcl getFolderAcl() {
		return folderAcl;
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
	 * Get the group workspace folder id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceFolderId() {
		return groupWorkspaceFolderId;
	}

	/**
	 * Set the group workspace folder id.
	 * 
	 * @param groupWorkspaceFolderId
	 */
	public void setGroupWorkspaceFolderId(Long groupWorkspaceFolderId) {
		this.groupWorkspaceFolderId = groupWorkspaceFolderId;
	}

	/**
	 * Set the user id
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the group worskpace folder.
	 * 
	 * @return
	 */
	public GroupWorkspaceFolder getGroupWorkspaceFolder() {
		return groupWorkspaceFolder;
	}

	/**
	 * Set the group workspace file system service.
	 * 
	 * @param groupWorkspaceFileSystemService
	 */
	public void setGroupWorkspaceFileSystemService(
			GroupWorkspaceFileSystemService groupWorkspaceFileSystemService) {
		this.groupWorkspaceFileSystemService = groupWorkspaceFileSystemService;
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
	 * Get the folder path.
	 * 
	 * @return the folder path
	 */
	public Collection<GroupWorkspaceFolder> getFolderPath() {
		return folderPath;
	}
	
	/**
	 * Id of the user to edit permissions on.
	 * @return
	 */
	public Long getEditUserPermissionsId() {
		return editUserPermissionsId;
	}

	/**
	 * Id of the user to edit permissions on.
	 * @param editUserPermissionsId
	 */
	public void setEditUserPermissionsId(Long editUserPermissionsId) {
		this.editUserPermissionsId = editUserPermissionsId;
	}

	/**
	 * Get the user you are making permission changes to.
	 * 
	 * @return
	 */
	public IrUser getEditUser() {
		return editUser;
	}

	/**
	 * Get the edit user access control list.
	 * 
	 * @return
	 */
	public IrUserAccessControlEntry getEditUserAcl() {
		return editUserAcl;
	}

	/**
	 * Set the users folder permissions.
	 * 
	 * @param folderPermissions
	 */
	public void setFolderPermissions(String[] folderPermissions) {
		this.folderPermissions = folderPermissions;
	}
	
	/**
	 * Determine if permissions should be applied to child files and folders.
	 * 
	 * @param applyToChildren
	 */
	public void setApplyToChildren(boolean applyToChildren) {
		this.applyToChildren = applyToChildren;
	}
}
