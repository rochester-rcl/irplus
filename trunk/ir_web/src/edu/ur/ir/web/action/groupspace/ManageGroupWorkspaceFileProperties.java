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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.security.IrAcl;
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

}
