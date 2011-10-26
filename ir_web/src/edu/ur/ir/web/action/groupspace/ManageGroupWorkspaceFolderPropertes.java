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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.group.ManageUserGroups;

/**
 * Allows a user to view and edit group workspace folder properties.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageGroupWorkspaceFolderPropertes extends ActionSupport 
implements  UserIdAware{

	/*eclipse generated id*/
	private static final long serialVersionUID = -5916633281950319826L;
	
	/* id of the user accessing the information */
	private Long userId;
	
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

	/*  Logger for managing content types*/
	private static final Logger log = Logger.getLogger(ManageGroupWorkspaceFolderPropertes.class);

	public String execute()
	{
		IrUser user = userService.getUser(userId, false);
		
		if( groupWorkspaceFolderId != null )
		{
			groupWorkspaceFolder = groupWorkspaceFileSystemService.getFolder(groupWorkspaceFolderId, false);
		}
		
		folderAcl = securityService.getAcl(groupWorkspaceFolder);
		
		String readPermission = GroupWorkspaceFolder.FOLDER_READ_PERMISSION;
		securityService.getPermissionForClass(groupWorkspaceFolder, readPermission);
		
		log.debug(" security service has permission = " + securityService.hasPermission(groupWorkspaceFolder, user, readPermission));
		
		if( user == null || 
				securityService.hasPermission(groupWorkspaceFolder, user, readPermission) <= 0)
		{
			return "accessDenied";
		}
		
		
		if( groupWorkspaceFolder != null )
		{
		    return SUCCESS;
	    }
		else
		{
			return "notFound";
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
	 * Set the group workspace folder.
	 * 
	 * @param groupWorkspaceFolder
	 */
	public void setGroupWorkspaceFolder(GroupWorkspaceFolder groupWorkspaceFolder) {
		this.groupWorkspaceFolder = groupWorkspaceFolder;
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

}
