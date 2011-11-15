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

import java.io.File;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.transformer.ThumbnailTransformerService;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Add new file version to group file workspace.
 * 
 * @author Nathan Sarr
 *
 */
public class AddNewGroupFileVersion extends ActionSupport implements UserIdAware{
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = -4200554064572751758L;

	/*  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(AddNewGroupFileVersion.class);
	
	/* id of the personal file  */
	private Long groupWorkspaceFileId;

	/* personal file for the user  */
	private GroupWorkspaceFile groupWorkspaceFile;
	
	/* User trying to upload the file */
	private Long userId;
	
	/* User service access  */
	private UserService userService;
	
	/* File system service for users. */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;
	
	/* process for setting up personal workspace information to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/* service for accessing index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/* description of the file  */
	private String groupFileDescription;
	
	/* actual file uploaded */
	private File file;
	

	/*  File name uploaded from the file system */
	private String fileFileName;
	
	/* Repository service for placing information in the repository */
	private RepositoryService repositoryService;
	
	/* set to true if the version was added  */
	private boolean versionAdded = false; 
	
	/* Keep the file locked  even after the new version has been uploaded*/
	private boolean keepLocked = false;
	
	/* service to create thumbnails  */
	private ThumbnailTransformerService thumbnailTransformerService;

	/**
	 * Used to view the page.
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		groupWorkspaceFile = groupWorkspaceFileSystemService.getFile(groupWorkspaceFileId, false);
		return SUCCESS;
	}
	
	/**
	 * Add a new version of the versioned file to the system.
	 * 
	 * @return
	 * @throws IllegalFileSystemNameException 
	 */
	public String addNewFileVersion() throws IllegalFileSystemNameException
	{
		log.debug("Adding new version " );
		String returnStatus = SUCCESS;
		groupWorkspaceFile = groupWorkspaceFileSystemService.getFile(groupWorkspaceFileId, false);
		VersionedFile versionedFile = groupWorkspaceFile.getVersionedFile();
		
		log.debug("User Id = " + userId );
		IrUser user = userService.getUser(userId, false);
		
		boolean canLock = repositoryService.canLockVersionedFile(versionedFile, user);
		
		// lock the file for the user if they have not already
		if(canLock)
		{
			Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
					false);
			if(versionedFile.getLockedBy() == null)
			{
				repositoryService.lockVersionedFile(versionedFile, user);
			}
			
			if(versionedFile.getLockedBy().equals(user))
			{
				repositoryService.addNewFileToVersionedFile(repository, 
						versionedFile, file, fileFileName, groupFileDescription, user);
			
				versionAdded = true;
			    IrFile irFile = groupWorkspaceFile.getVersionedFile().getCurrentVersion().getIrFile();
			    thumbnailTransformerService.transformFile(repository, irFile);			    
			    userWorkspaceIndexProcessingRecordService.saveAll(groupWorkspaceFile, 
			    			indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
			}
			else
			{
				addFieldError("lockedByUser", 
				"The file is currently locked by user: " + versionedFile.getLockedBy().getUsername());
		        returnStatus = INPUT;
			}
			// unlock the file if the user did not select keep locked
			if( !keepLocked)
			{
				repositoryService.unlockVersionedFile(versionedFile, user);
			}
			
		}
		else
		{
			addFieldError("cannotLock", 
			"You do not have permission to add versions to this file ");
	        returnStatus = INPUT;
		}

		return returnStatus;
		
	}
	

	/**
	 * Get user id
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * inject the user id.
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the group workspace file id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceFileId() {
		return groupWorkspaceFileId;
	}

	/**
	 * Set the group workspace file id.
	 * 
	 * @param groupWorkspaceFileId
	 */
	public void setGroupWorkspaceFileId(Long groupWorkspaceFileId) {
		this.groupWorkspaceFileId = groupWorkspaceFileId;
	}

	/**
	 * Get the group workspace file
	 * @return
	 */
	public GroupWorkspaceFile getGroupWorkspaceFile() {
		return groupWorkspaceFile;
	}

    /**
     * Get the group file description.
     * 
     * @return
     */
	public String getGroupFileDescription() {
		return groupFileDescription;
	}

	/**
	 * Set the group file description.
	 * 
	 * @param groupFileDescription
	 */
	public void setGroupFileDescription(String groupFileDescription) {
		this.groupFileDescription = groupFileDescription;
	}

	/**
	 * Get the group file name.
	 * 
	 * @return
	 */
	public String getFileName() {
		return fileFileName;
	}

	/**
	 * Set the group file name.
	 * 
	 * @param groupFileName
	 */
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	/**
	 * Get the version added.
	 * 
	 * @return
	 */
	public boolean getVersionAdded() {
		return versionAdded;
	}

	/**
	 * Set the version added.
	 * 
	 * @param versionAdded
	 */
	public void setVersionAdded(boolean versionAdded) {
		this.versionAdded = versionAdded;
	}

	/**
	 * Keep the file locked.
	 * 
	 * @return
	 */
	public boolean getKeepLocked() {
		return keepLocked;
	}

	/**
	 * Keep the file locked.
	 * 
	 * @param keepLocked
	 */
	public void setKeepLocked(boolean keepLocked) {
		this.keepLocked = keepLocked;
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
	 * Set the group workspace file system service.
	 * 
	 * @param groupWorkspaceFileSystemService
	 */
	public void setGroupWorkspaceFileSystemService(
			GroupWorkspaceFileSystemService groupWorkspaceFileSystemService) {
		this.groupWorkspaceFileSystemService = groupWorkspaceFileSystemService;
	}

	/**
	 * Set the user workspace index processing record service.
	 * 
	 * @param userWorkspaceIndexProcessingRecordService
	 */
	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}

	/**
	 * Set the index processing type service.
	 * 
	 * @param indexProcessingTypeService
	 */
	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

	/**
	 * Set the repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * Set the thumbnail transformer service.
	 * 
	 * @param thumbnailTransformerService
	 */
	public void setThumbnailTransformerService(
			ThumbnailTransformerService thumbnailTransformerService) {
		this.thumbnailTransformerService = thumbnailTransformerService;
	}

	/**
	 * File to add.
	 * 
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	


}
