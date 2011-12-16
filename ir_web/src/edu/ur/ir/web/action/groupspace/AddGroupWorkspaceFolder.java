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

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.user.IrUser;

import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Manages add/updating a group folder.
 * 
 * @author Nathan Sarr
 *
 */
public class AddGroupWorkspaceFolder extends ActionSupport implements UserIdAware{
	
	// eclipse generated id
	private static final long serialVersionUID = -3802168590556334259L;

	//  user service 
	private UserService userService;
	
	//group workspace service
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;
	
	//service to deal with group workspace information
	private GroupWorkspaceService groupWorkspaceService;

	// the name of the folder to add 
	private String folderName;
	
	// Description of the folder 
	private String folderDescription;
	
	// Current folder the user is looking at 
	private Long parentFolderId;
	
	// Id of the folder to update for updating 
	private Long updateFolderId;
	
	//  Logger for add personal folder action 
	private static final Logger log = Logger.getLogger(AddGroupWorkspaceFolder.class);
	
	//  User id
	private Long userId;
	
	// id of the workspace to add the folder
	private Long groupWorkspaceId;

	//  Indicates the folder has been added
	private boolean folderAdded = false;
	
	// Message that can be displayed to the user.
	private String folderMessage;
	
	// process for setting up personal workspace information to be indexed 
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	// service for accessing index processing types 
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/**
	 * Create the new folder
	 */
	public String add() 
	{
		log.debug("creating a group folder parent folderId = " + parentFolderId);
		IrUser thisUser = userService.getUser(userId, true);
		GroupWorkspace groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		
		folderAdded = false;
		// assume that if the current folder id is null or equal to 0
		// then we are adding a root folder to the user.
		if(parentFolderId == null || parentFolderId == 0)
		{
			 // add root folder
			 if( groupWorkspace.getRootFolder(folderName) == null )
		     {
				 GroupWorkspaceFolder folder = null;
				 try {
					folder = groupWorkspaceFileSystemService.addFolder(groupWorkspace, folderName, folderDescription, thisUser);
					userWorkspaceIndexProcessingRecordService.saveAll( folder, 
			    			indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
					
			        folderAdded = true;
				 }
				 catch(PermissionNotGrantedException pnge )
				 {
					 addFieldError("permissionDenied", "You do not have permission to add a folder to this group");
				 }
				 catch (DuplicateNameException e) {
					folderMessage = getText("personalFolderAlreadyExists", new String[]{folderName});
					addFieldError("personalFolderAlreadyExists", folderMessage);
				 }
				 catch(IllegalFileSystemNameException ifsne)
				 {
					folderMessage = getText("illegalPersonalFolderName", new String[]{folderName, String.valueOf(ifsne.getIllegalCharacters())});
					addFieldError("illegalPersonalFolderName", folderMessage);
				 }
	         } else {
	        	 folderMessage = getText("personalFolderAlreadyExists", new String[]{folderName});
	        	 addFieldError("personalFolderAlreadyExists", folderMessage);
	         }
		}
		else
		{
		    // add sub folder	
			GroupWorkspaceFolder parentFolder = groupWorkspaceFileSystemService.getFolder(parentFolderId, true);
			try
			{
			    GroupWorkspaceFolder folder = groupWorkspaceFileSystemService.addFolder(parentFolder, folderName, folderDescription, thisUser);
			    
			    userWorkspaceIndexProcessingRecordService.saveAll(  folder, 
		    			indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
			    
			    folderAdded = true;
			}
			catch(PermissionNotGrantedException pnge )
			{
				 addFieldError("permissionDenied", "You do not have permission to add a folder to this group");
			}
			catch(DuplicateNameException e)
			{
				folderMessage = getText("personalFolderAlreadyExists", new String[]{folderName});
				addFieldError("personalFolderAlreadyExists", folderMessage);
			}
			catch(IllegalFileSystemNameException ifsne)
			{
				folderMessage = getText("illegalPersonalFolderName", new String[]{folderName, String.valueOf(ifsne.getIllegalCharacters())});
				addFieldError("illegalPersonalFolderName", folderMessage);
			}
		}

        return "added";
	}
	
	/**
	 * Update a folder with the given information.
	 * 
	 * @return success if the folder is updated.
	 * @throws NoIndexFoundException 
	 * @throws Exception
	 */
	public String update() throws NoIndexFoundException
	{
	    return "added";
	}

	/**
	 * Loads the folder
	 * 
	 * @return
	 */
	public String get()
	{
	    return "get";
	}
	

	/**
	 * The user service for dealing with actions.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * Get the user for this folder
	 * 
	 * @return
	 */
	public Long getUserId() 
	{
		return userId;
	}

	/**
	 * Set the user for this user.
	 * 
	 * @see edu.ur.ir.web.action.UserAware#setOwner(edu.ur.ir.user.IrUser)
	 */
	public void injectUserId(Long userId) 
	{
		this.userId = userId;
	}

	/**
	 * Get the name of the folder to add.
	 * 
	 * @return
	 */
	public String getFolderName() 
	{
		return folderName;
	}

	/**
	 * Set the name of the folder to add.
	 * 
	 * @param folderName
	 */
	public void setFolderName(String folderName) 
	{
		this.folderName = folderName;
	}

	/**
	 * Current folder the user is looking at.
	 * 
	 * @return
	 */
	public Long getParentFolderId() 
	{
		return parentFolderId;
	}

	/**
	 * The current folder the user is looking at.
	 * 
	 * @param currentFolderId
	 */
	public void setParentFolderId(Long currentFolderId) 
	{
		this.parentFolderId = currentFolderId;
	}

	/**
	 * Indicates if the folder has been added 
	 * 
	 * @return
	 */
	public boolean isFolderAdded() 
	{
		return folderAdded;
	}

	/**
	 * Get the folder added message.
	 * 
	 * @return
	 */
	public String getFolderMessage() 
	{
		return folderMessage;
	}

	/**
	 * Description of the folder.
	 * 
	 * @return
	 */
	public String getFolderDescription() 
	{
		return folderDescription;
	}

	/**
	 * Description of the folder.
	 * 
	 * @param folderDescription
	 */
	public void setFolderDescription(String folderDescription) 
	{
		this.folderDescription = folderDescription;
	}

	/**
	 * Get the update folder id.
	 * 
	 * @return update folder id
	 */
	public Long getUpdateFolderId() {
		return updateFolderId;
	}

	/**
	 * Set the update folder id
	 * 
	 * @param updateFolderId
	 */
	public void setUpdateFolderId(Long updateFolderId) {
		this.updateFolderId = updateFolderId;
	}

	/**
	 * Set the user index processing record service.
	 * 
	 * @param userWorkspaceIndexProcessingRecordService
	 */
	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}

	/**
	 * Set the index processing record type service.
	 * 
	 * @param indexProcessingTypeService
	 */
	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}
	
	/**
	 * Get the workspace id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceId() {
		return groupWorkspaceId;
	}

	/**
	 * Set the workspace id.
	 * 
	 * @param workspaceId
	 */
	public void setGroupWorkspaceId(Long workspaceId) {
		this.groupWorkspaceId = workspaceId;
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
	 * Set the group workspace service.
	 * 
	 * @param groupWorkspaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}

}
