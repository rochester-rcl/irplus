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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.transformer.BasicThumbnailTransformer;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserWorkspaceIndexService;
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
	
	/** description of the file  */
	private String userFileDescription;
	
	/** actual set of files uploaded */
	private File file;
	
	/**  File name uploaded from the file system */
	private String fileFileName;
	
	/** content types of the files.  */
	private String fileContentType;

	/** Class to create jpeg thumbnails. */
	private BasicThumbnailTransformer defaultThumbnailTransformer;
	
	/** Temporary file creator to allow a temporary file to be created for processing */
	private TemporaryFileCreator temporaryFileCreator;
	
	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;
	
	/** User index service for indexing files */
	private UserWorkspaceIndexService userWorkspaceIndexService;
	
	/** set to true if the version was added  */
	private boolean versionAdded = false; 
	
	/** Keep the file locked  even after the new version has been uploaded*/
	private boolean keepLocked = false;
	
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
	 */
	public String addNewFileVersion()
	{
		String returnStatus = SUCCESS;
		personalFile = userFileSystemService.getPersonalFile(personalFileId, false);
		VersionedFile versionedFile = personalFile.getVersionedFile();
		
		log.debug("User Id = " + userId);
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
						versionedFile, file, fileFileName, user);
				versionAdded = true;
			    IrFile irFile = personalFile.getVersionedFile().getCurrentVersion().getIrFile();
			    FileInfo fileInfo = irFile.getFileInfo();
			    String extension = fileInfo.getExtension();
			
			    log.debug("Extension = " + extension  + " can thumbnail = " +
			    defaultThumbnailTransformer.canTransform(extension));
			
			    if(defaultThumbnailTransformer.canTransform(extension))
			    {
				    try
				    {
				        File tempFile = temporaryFileCreator.createTemporaryFile(extension);
				        defaultThumbnailTransformer.transformFile(file, extension, tempFile);
				        
				        if( tempFile != null && tempFile.exists() && tempFile.length() != 0l)
				        {
				            TransformedFileType transformedFileType = repositoryService.getTransformedFileTypeBySystemCode("PRIMARY_THUMBNAIL");
				    
				            repositoryService.addTransformedFile(repository, 
				    		    irFile, 
				    		    tempFile, 
				    		    "JPEG file", 
				    		    defaultThumbnailTransformer.getFileExtension(), 
				    		    transformedFileType);
				        }
				        else
				        {
				        	log.error("could not create thumbnail for file " + fileInfo);
				        }
				    
				    }
				    catch(Exception e)
				    {
					    log.error("Could not create thumbnail", e);
				    }
			    }
			    userWorkspaceIndexService.updateAllIndexes(repository, personalFile);

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

	public BasicThumbnailTransformer getDefaultThumbnailTransformer() {
		return defaultThumbnailTransformer;
	}

	public void setDefaultThumbnailTransformer(
			BasicThumbnailTransformer defaultThumbnailTransformer) {
		this.defaultThumbnailTransformer = defaultThumbnailTransformer;
	}

	public TemporaryFileCreator getTemporaryFileCreator() {
		return temporaryFileCreator;
	}

	public void setTemporaryFileCreator(TemporaryFileCreator temporaryFileCreator) {
		this.temporaryFileCreator = temporaryFileCreator;
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

	public UserWorkspaceIndexService getUserWorkspaceIndexService() {
		return userWorkspaceIndexService;
	}

	public void setUserWorkspaceIndexService(UserWorkspaceIndexService userIndexService) {
		this.userWorkspaceIndexService = userIndexService;
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


}
