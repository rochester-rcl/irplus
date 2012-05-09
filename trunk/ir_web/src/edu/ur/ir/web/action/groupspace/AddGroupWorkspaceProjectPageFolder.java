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

package edu.ur.ir.web.action.groupspace;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * @author Nathan Sarr
 *
 */
public class AddGroupWorkspaceProjectPageFolder extends ActionSupport implements UserIdAware{
	
	// eclipse generated id
	private static final long serialVersionUID = -3708204560203600520L;

	// Project page  service. 
	private GroupWorkspaceProjectPageService groupWorkspaceProjectPageService;
	


	// Service for dealing with researcher file system information 
	private GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService;
	
	// service for user information
	private UserService userService;
	
	// the name of the folder to add 
	private String folderName;
	
	// Description of the folder 
	private String folderDescription;
	
	// Current folder the user is looking at  
	private Long parentFolderId;
	
	// Id of the folder to update for updating   
	private Long updateFolderId;
	
	//  Logger for add researcher folder action  
	private static final Logger log = Logger.getLogger(AddGroupWorkspaceProjectPageFolder.class);
	
	// Group workspace project page id 
	private Long projectPageId;

	// Indicates the folder has been added 
	private boolean added = false;
	
	// Message that can be displayed to the user.  
	private String message;
	
	// id of the user making changes  
	private Long userId;
	
	/**
	 * Create the new folder
	 */
	public String save()throws Exception
	{
		log.debug("creating a project page folder parent folderId = " + parentFolderId);
		GroupWorkspaceProjectPage projectPage = groupWorkspaceProjectPageService.getById(projectPageId, false);
		
		IrUser user = userService.getUser(userId, false);
		GroupWorkspaceUser workspaceUser = null;
		if( user != null )
		{
		    workspaceUser = projectPage.getGroupWorkspace().getUser(user);
		}
		
		if( workspaceUser == null || !workspaceUser.isOwner())
		{
			return "accessDenied";
		}
		
		added = false;
		// assume that if the current folder id is null or equal to 0
		// then we are adding a root folder to the user.
		if(parentFolderId == null || parentFolderId == 0)
		{
			 if( projectPage.getRootFolder(folderName) == null )
		     {
				 GroupWorkspaceProjectPageFolder projectPageFolder = null;
				 try 
				 {
					projectPageFolder = projectPage.createRootFolder(folderName);
					projectPageFolder.setDescription(folderDescription);
					groupWorkspaceProjectPageFileSystemService.save(projectPageFolder);
					added = true;
				 } 
				 catch (DuplicateNameException e) 
				 {
					throw new RuntimeException("Fix this save error");
				 }
	         }
		}
		else
		{
			GroupWorkspaceProjectPageFolder folder = groupWorkspaceProjectPageFileSystemService.getFolder(parentFolderId, true);
			if( !folder.getGroupWorkspaceProjectPage().equals(projectPage) )
			{
				return "accessDenied";
			}
		
			try
			{
			    GroupWorkspaceProjectPageFolder projectFolder = folder.createChild(folderName);
			    projectFolder.setDescription(folderDescription);
			    groupWorkspaceProjectPageFileSystemService.save(folder);
			    added = true;
			}
			catch(DuplicateNameException e)
			{
				added = false;
			}
			
		}
		
		if( !added)
		{
			message = getText("researcherFolderAlreadyExists", new String[]{folderName});
		}
        return SUCCESS;
	}
	
	/**
	 * Update a folder with the given information.
	 * 
	 * @return success if the folder is updated.
	 * @throws Exception
	 */
	public String updateFolder()throws Exception
	{
		
		log.debug("updating a researcher folder parent folderId = " + parentFolderId);
		added = false;

		GroupWorkspaceProjectPageFolder other = null;
		
		// check the name.  This makes sure that 
		// if the name has been changed, it does not conflict
		// with a folder already in the folder system.
		if( parentFolderId == null || parentFolderId == 0)
		{
			other = groupWorkspaceProjectPageFileSystemService.getRootFolder(folderName, projectPageId);
		}
		else
		{
			other = groupWorkspaceProjectPageFileSystemService.getFolder(folderName, parentFolderId);
		}
		
		IrUser user = userService.getUser(userId, false);
		GroupWorkspaceProjectPageFolder existingFolder = null;
		GroupWorkspaceProjectPage projectPage = null;
		
		if( other == null )
		{
		    existingFolder = groupWorkspaceProjectPageFileSystemService.getFolder(updateFolderId, true);
		    if( existingFolder != null )
		    {
		        projectPage = existingFolder.getGroupWorkspaceProjectPage();
		    }
		    else
		    {
		    	return "notFound";
		    }
		}
		else
		{
			projectPage = other.getGroupWorkspaceProjectPage();
		}
	
		GroupWorkspaceUser workspaceUser = null;
		
		if( user != null)
		{
		    workspaceUser = projectPage.getGroupWorkspace().getUser(user);
		}
		
		// make sure user is owner of group workspace
		if( workspaceUser == null || !workspaceUser.isOwner())
		{
		    return "accessDenied";
		}
		
		// name has been changed and does not conflict
		if( other == null)
		{
			existingFolder.setName(folderName);
			existingFolder.setDescription(folderDescription);
			groupWorkspaceProjectPageFileSystemService.save(existingFolder);
			added = true;
		}
		// name has not been changed
		else if(other.getId().equals(updateFolderId))
		{
			other.setDescription(folderDescription);
			groupWorkspaceProjectPageFileSystemService.save(other);
			added = true;
		}

		if( !added)
		{
			message = getText("groupWorkspaceProjectPageFolderAlreadyExists", new String[]{folderName});
		}
		
			
	    return SUCCESS;
		
	}

	/**
	 * Get the name of the folder to add.
	 * 
	 * @return
	 */
	public String getFolderName() {
		return folderName;
	}

	/**
	 * Set the name of the folder to add.
	 * 
	 * @param folderName
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	/**
	 * Current folder the user is looking at.
	 * 
	 * @return
	 */
	public Long getParentFolderId() {
		return parentFolderId;
	}

	/**
	 * The current folder the user is looking at.
	 * 
	 * @param currentFolderId
	 */
	public void setParentFolderId(Long currentFolderId) {
		this.parentFolderId = currentFolderId;
	}
	
	/**
	 * Indicates if the folder has been added 
	 * 
	 * @return
	 */
	public boolean isAdded() {
		return added;
	}

	/**
	 * Get the folder added message.
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Description of the folder.
	 * 
	 * @return
	 */
	public String getFolderDescription() {
		return folderDescription;
	}

	/**
	 * Description of the folder.
	 * 
	 * @param folderDescription
	 */
	public void setFolderDescription(String folderDescription) {
		this.folderDescription = folderDescription;
	}

	/**
	 * Get the update folder id.
	 * 
	 * @return
	 */
	public Long getUpdateFolderId() {
		return updateFolderId;
	}

	/**
	 * Set the update folder id.
	 * 
	 * @param updateFolderId
	 */
	public void setUpdateFolderId(Long updateFolderId) {
		this.updateFolderId = updateFolderId;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the project page id.
	 * 
	 * @return
	 */
	public Long getProjectPageId() {
		return projectPageId;
	}

	/**
	 * Set the project page id.
	 * 
	 * @param projectPageId
	 */
	public void setProjectPageId(Long projectPageId) {
		this.projectPageId = projectPageId;
	}

	/**
	 * Set the group workspace project page service.
	 * 
	 * @param groupWorkspaceProjectPageService
	 */
	public void setGroupWorkspaceProjectPageService(
			GroupWorkspaceProjectPageService groupWorkspaceProjectPageService) {
		this.groupWorkspaceProjectPageService = groupWorkspaceProjectPageService;
	}

	/**
	 * Set the group workspace project page file system service.
	 * 
	 * @param groupWorkspaceProjectPageFileSystemService
	 */
	public void setGroupWorkspaceProjectPageFileSystemService(
			GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService) {
		this.groupWorkspaceProjectPageFileSystemService = groupWorkspaceProjectPageFileSystemService;
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
