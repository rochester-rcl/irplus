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

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
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
 * Allow a user to manage group workspace file properties.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageGroupWorkspaceFileProperties  extends ActionSupport 
implements  UserIdAware{

	/* unique id created by eclipse */
	private static final long serialVersionUID = 4656970684278365613L;

	/* id of the user accessing the information */
	private Long userId;
	
	/* id of the group workspace file */
	private Long groupWorkspaceFileId;
	
	/* file for the group workspace */
	private GroupWorkspaceFile groupWorkspaceFile;
	
	/* group workspace file system service */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;

	/* security information */
	private SecurityService securityService;

	/* access control list for the file */
	private IrAcl fileAcl;
	
	/* service to deal with user information */
	private UserService userService;
	
	/* set of folders that are the path for the current folder */
    private Collection <GroupWorkspaceFolder> folderPath;
    
	/* id of the user to edit permissions for */
	private Long editUserPermissionsId;
	
    /* access control entry for the user */
    private IrUserAccessControlEntry editUserAcl;

    /* list of file permissions */
    private String[] filePermissions;



	/* user to edit permissions on */
	private IrUser editUser;	

	/*  Logger */
	private static final Logger log = Logger.getLogger(ManageGroupWorkspaceFileProperties.class);

	public String execute()
	{
		IrUser user = userService.getUser(userId, false);
		
		if( groupWorkspaceFileId != null )
		{
			groupWorkspaceFile = groupWorkspaceFileSystemService.getFile(groupWorkspaceFileId, false);
			
		}
		
		if( groupWorkspaceFile != null )
		{
			fileAcl = securityService.getAcl(groupWorkspaceFile.getVersionedFile());
			
			String readPermission = VersionedFile.VIEW_PERMISSION;
			securityService.getPermissionForClass(groupWorkspaceFile.getVersionedFile(), readPermission);
			
			log.debug(" security service has permission = " + securityService.hasPermission(groupWorkspaceFile.getVersionedFile(), user, readPermission));
			
			if( user == null || 
					securityService.hasPermission(groupWorkspaceFile.getVersionedFile(), user, readPermission) <= 0)
			{
				return "accessDenied";
			}
			if( groupWorkspaceFile.getGroupWorkspaceFolder() != null )
			{
			    folderPath = groupWorkspaceFileSystemService.getFolderPath(groupWorkspaceFile.getGroupWorkspaceFolder());
			}
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
		if( groupWorkspaceFileId != null )
		{
			groupWorkspaceFile = groupWorkspaceFileSystemService.getFile(groupWorkspaceFileId, false);
		}
		if( groupWorkspaceFile != null )
		{
			// only owners can make permission changes to folders
			GroupWorkspace groupWorkspace = groupWorkspaceFile.getGroupWorkspace();
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
			IrAcl userAcl = securityService.getAcl(groupWorkspaceFile.getVersionedFile(), editUser);
			editUserAcl = userAcl.getUserAccessControlEntryByUserId(editUser.getId());
			log.debug("editUserAcl = " + editUserAcl);
			
			if( groupWorkspaceFile.getGroupWorkspaceFolder() != null )
			{
			    folderPath = groupWorkspaceFileSystemService.getFolderPath(groupWorkspaceFile.getGroupWorkspaceFolder());
			}
			
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
        IrUser user = userService.getUser(userId, false);
		boolean error = false;
        if( user == null )
        {
        	return "accessDenied";
        }
		if( groupWorkspaceFileId != null )
		{
			groupWorkspaceFile = groupWorkspaceFileSystemService.getFile(groupWorkspaceFileId, false);
		}
		if( groupWorkspaceFile != null )
		{
			// only owners can make permission changes to folders
			GroupWorkspace groupWorkspace = groupWorkspaceFile.getGroupWorkspace();
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
			
			if( filePermissions != null )
			{
			    for(String permission : filePermissions)
			    {
				    log.debug("permission = " + permission);
				    if(permission.equals(VersionedFile.VIEW_PERMISSION) )
				    {
					    permissions.add(securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.VIEW_PERMISSION));
				    }
				
				    if( permission.equals(VersionedFile.EDIT_PERMISSION))
				    {
				    	permissions.add(securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.EDIT_PERMISSION));
				    }
			    }
			}
			
			try {
				groupWorkspaceFileSystemService.changeUserPermissionsForFile(editUser, groupWorkspaceFile, permissions);
			} catch (UserHasParentFolderPermissionsException e) {
				error = true;
				addFieldError("parentFolderPermissionsError", 
						"A parent folder has permissions which would override the permissions choosen please check parent folders for edit permissions");
			}
			IrAcl userAcl = securityService.getAcl(groupWorkspaceFile.getVersionedFile(), editUser);
			editUserAcl = userAcl.getUserAccessControlEntryByUserId(editUser.getId());
			if( groupWorkspaceFile.getGroupWorkspaceFolder() != null )
			{
			    folderPath = groupWorkspaceFileSystemService.getFolderPath(groupWorkspaceFile.getGroupWorkspaceFolder());
			}
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
	public IrAcl getFileAcl() {
		return fileAcl;
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
	public Long getGroupWorkspaceFileId() {
		return groupWorkspaceFileId;
	}

	/**
	 * Set the group workspace folder id.
	 * 
	 * @param groupWorkspaceFolderId
	 */
	public void setGroupWorkspaceFileId(Long groupWorkspaceFileId) {
		this.groupWorkspaceFileId = groupWorkspaceFileId;
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
	public GroupWorkspaceFile getGroupWorkspaceFile() {
		return groupWorkspaceFile;
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
	 * Get the folder path to the file.
	 * 
	 * @return
	 */
	public Collection<GroupWorkspaceFolder> getFolderPath() {
		return folderPath;
	}
	
	/**
	 * Id of the user to get permissions for.
	 * 
	 * @return
	 */
	public Long getEditUserPermissionsId() {
		return editUserPermissionsId;
	}

	/**
	 * Set the edit user permissions id.
	 * 
	 * @param editUserPermissionsId
	 */
	public void setEditUserPermissionsId(Long editUserPermissionsId) {
		this.editUserPermissionsId = editUserPermissionsId;
	}

	/**
	 * The user who's permissions are being edited.
	 * 
	 * @return
	 */
	public IrUser getEditUser() {
		return editUser;
	}
	
	/**
	 * Get the edit user acl.
	 * 
	 * @return
	 */
	public IrUserAccessControlEntry getEditUserAcl() {
		return editUserAcl;
	}
	
	/**
	 * Set the file permissions.
	 * 
	 * @param filePermissions
	 */
	public void setFilePermissions(String[] filePermissions) {
		this.filePermissions = filePermissions;
	}


}
