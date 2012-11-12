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


package edu.ur.ir.web.action.user;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.transformer.ThumbnailTransformerService;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action dealing with adding a new version of a file for a given person.
 * 
 * @author Nathan Sarr
 *
 */
public class AddNewFileVersion extends ActionSupport implements UserIdAware{
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = -2621769968886370338L;
	
	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(AddNewFileVersion.class);
	
	/** id of the personal file  */
	private Long personalFileId;
	
	/** personal file for the user  */
	private PersonalFile personalFile;
	
	/** User trying to upload the file */
	private Long userId;
	
	/** User service access  */
	private UserService userService;
	
	/** File system service for users. */
	private UserFileSystemService userFileSystemService;
	
	/** process for setting up personal workspace information to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/** service for accessing index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/** description of the file  */
	private String userFileDescription;
	
	/** actual set of files uploaded */
	private File file;
	
	/**  File name uploaded from the file system */
	private String fileFileName;
	
	/** content types of the files.  */
	private String fileContentType;
	
	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;
	
	/** set to true if the version was added  */
	private boolean versionAdded = false; 
	
	/** Keep the file locked  even after the new version has been uploaded*/
	private boolean keepLocked = false;
	
	/* notify collaborators */
	private Long[] collaboratorIds;
	
	/* notify owner of change */
	private boolean notifyOwner = false;
	





	/* service to deal with inviting users */
	private InviteUserService inviteUserService;

	/* service to create thumbnails  */
	private ThumbnailTransformerService thumbnailTransformerService;

	/**
	 * Used to view the page.
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		personalFile = userFileSystemService.getPersonalFile(personalFileId, false);
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
		personalFile = userFileSystemService.getPersonalFile(personalFileId, false);
		VersionedFile versionedFile = personalFile.getVersionedFile();
		
		log.debug("User Id = " + userId );
		IrUser user = userService.getUser(userId, false);
		
		boolean canLock = repositoryService.canLockVersionedFile(versionedFile, user);
		
		// lock the file for the user if they have not already
		if(canLock)
		{
			Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
					false);
			
			// lock the file for the user to make change
			if(versionedFile.getLockedBy() == null)
			{
				repositoryService.lockVersionedFile(versionedFile, user);
			}
			
			if(versionedFile.getLockedBy().equals(user))
			{
				repositoryService.addNewFileToVersionedFile(repository, 
						versionedFile, file, fileFileName, userFileDescription, user);
			
				versionAdded = true;
			    IrFile irFile = personalFile.getVersionedFile().getCurrentVersion().getIrFile();
			    thumbnailTransformerService.transformFile(repository, irFile);			    
			    userWorkspaceIndexProcessingRecordService.saveAll(personalFile, 
			    			indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
			    
			    // unlock the file if the user did not select keep locked
				if( !keepLocked)
				{
					repositoryService.unlockVersionedFile(versionedFile, user);
				}
				
				if( collaboratorIds != null && collaboratorIds.length > 0 )
				{
					List<Long> collaborators = Arrays.asList(collaboratorIds);
				    inviteUserService.notifyCollaboratorsOfNewVersion(personalFile, collaborators, notifyOwner);
				}
			}
			else
			{
				addFieldError("lockedByUser", 
				"The file is currently locked by user: " + versionedFile.getLockedBy().getUsername());
		        returnStatus = INPUT;
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
	
	public Long getPersonalFileId() {
		return personalFileId;
	}

	public void setPersonalFileId(Long personalFileId) {
		this.personalFileId = personalFileId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public PersonalFile getPersonalFile() {
		return personalFile;
	}

	public void setPersonalFile(PersonalFile personalFile) {
		this.personalFile = personalFile;
	}

	public String getUserFileDescription() {
		return userFileDescription;
	}

	public void setUserFileDescription(String userFileDescription) {
		this.userFileDescription = userFileDescription;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}


	public boolean isVersionAdded() {
		return versionAdded;
	}

	public void setVersionAdded(boolean versionAdded) {
		this.versionAdded = versionAdded;
	}

	public void setKeepLocked(boolean keepLocked) {
		this.keepLocked = keepLocked;
	}

	public UserWorkspaceIndexProcessingRecordService getUserWorkspaceIndexProcessingRecordService() {
		return userWorkspaceIndexProcessingRecordService;
	}

	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}

	public IndexProcessingTypeService getIndexProcessingTypeService() {
		return indexProcessingTypeService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

	public ThumbnailTransformerService getThumbnailTransformerService() {
		return thumbnailTransformerService;
	}

	public void setThumbnailTransformerService(
			ThumbnailTransformerService thumbnailTransformerService) {
		this.thumbnailTransformerService = thumbnailTransformerService;
	}
	

	/**
	 * Invite user service.
	 * 
	 * @param inviteUserService
	 */
	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

	public Long[] getCollaboratorIds() {
		return collaboratorIds;
	}

	public void setCollaboratorIds(Long[] collaboratorIds) {
		this.collaboratorIds = collaboratorIds;
	}
	
	public boolean getNotifyOwner() {
		return notifyOwner;
	}

	public void setNotifyOwner(boolean notifyOwner) {
		this.notifyOwner = notifyOwner;
	}
}
