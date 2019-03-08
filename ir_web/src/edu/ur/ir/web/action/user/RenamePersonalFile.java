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

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to rename personal file
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class RenamePersonalFile extends ActionSupport implements UserIdAware{

	/** Eclipse generated Id	 */
	private static final long serialVersionUID = -7262077243101493697L;
	
	/** User file system service. */
	private UserFileSystemService userFileSystemService;
	
	/**  Indicates the file has been renamed */
	private boolean fileRenamed = false;
	
	/** Message that can be displayed to the user. */
	private String renameMessage;
	
	/** New file name */
	private String newFileName;
	
	/** description of the file */
	private String fileDescription;
	
	/**  Logger for action */
	private static final Logger log = LogManager.getLogger(RenamePersonalFile.class);
	
	/** Id of personal file */
	private Long personalFileId;
	
	/** id of the user trying to make the change */
	private Long userId;

	/**
	 * Renames a file
	 * 
	 * @return
	 */
	public String rename() {
		
		log.debug("Rename Personal file::" + personalFileId + " file name = " + newFileName);
		
		PersonalFile personalFile = userFileSystemService.getPersonalFile(personalFileId, false);
		
		// check access
		if( !personalFile.getOwner().getId().equals(userId))
		{
			return "accessDenied";
		}
	
		// Check the owner's file system
		PersonalFile ownerPf  = userFileSystemService.getPersonalFile(personalFile.getVersionedFile().getOwner(), personalFile.getVersionedFile());
		ownerPf.getVersionedFile().setDescription(fileDescription);
		if( !personalFile.getVersionedFile().getNameWithExtension().equals(newFileName))
		{
			Set<FileCollaborator> collaborators = personalFile.getVersionedFile().getCollaborators();
			StringBuffer buffer = new StringBuffer();
			String conflictingUserNames = null;
			
		    // Can be null when owner deletes the file from the system
		    if (ownerPf != null) {
			    if (checkFileNameExist(ownerPf.getVersionedFile().getOwner(), ownerPf.getPersonalFolder(), newFileName)) {
				    buffer.append(" ");
				    buffer.append(ownerPf.getOwner().getFirstName());
				    buffer.append(" ");
				    buffer.append(ownerPf.getOwner().getLastName());
				    buffer.append(",");
			    }
		    }
		    // Check collaborator's file system
		    for (FileCollaborator collaborator: collaborators) {
			    PersonalFile pf  = userFileSystemService.getPersonalFile(collaborator.getCollaborator(), collaborator.getVersionedFile());
				
			    // Can be null when the file is in shared file inbox
			    if (pf != null) {
				    if (checkFileNameExist(collaborator.getCollaborator(), pf.getPersonalFolder(), newFileName)) {
					    buffer.append(" ");
					    buffer.append(collaborator.getCollaborator().getFirstName());
					    buffer.append(" ");
					    buffer.append(collaborator.getCollaborator().getLastName());
					    buffer.append(",");
				    }
	    	    }
		    }
			
		    if (buffer.length() == 0) {
			    try {
				    personalFile.getVersionedFile().reName(newFileName);
			    } catch (IllegalFileSystemNameException e) {
				    renameMessage = getText("illegalNameError", new String[]{e.getName(), String.valueOf(e.getIllegalCharacters())});
				    addFieldError("illegalNameError", renameMessage);
			    }
			    userFileSystemService.makePersonalFilePersistent(personalFile);
			    fileRenamed = true;
				
		    } else {
		    	conflictingUserNames = buffer.toString();
			    conflictingUserNames = conflictingUserNames.substring(0, conflictingUserNames.length() - 1);
			    renameMessage = getText("renameFileMessage", new String[]{conflictingUserNames});
			    addFieldError("renameFileMessage", renameMessage);
			    
		    }
		}
		else
		{
			fileRenamed = true;
			// assume description change
			userFileSystemService.makePersonalFilePersistent(personalFile);
		}
		
		return SUCCESS;
	}
	
	/*
	 * Checks if user has new file name in the specified folder
	 */
	private boolean checkFileNameExist(IrUser user, PersonalFolder personalFolder, String nameWithExtension) {
		if (personalFolder == null) 
		{
			return user.getRootFile(nameWithExtension) != null;
		} 
		else 
		{
			return personalFolder.getFile(nameWithExtension) != null;
		}
	}

	/**
	 * Get personal file name
	 * 
	 * @return
	 */
	public String get() {
		PersonalFile personalFile = userFileSystemService.getPersonalFile(personalFileId, false);
		newFileName = personalFile.getVersionedFile().getNameWithExtension();
		fileDescription = personalFile.getVersionedFile().getDescription();
		return "get";
	}
	/**
	 * Get User file system service
	 *  
	 * @return
	 */
	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	/**
	 * Set User file system service
	 * 
	 * @param userFileSystemService
	 */
	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	/**
	 * Get rename message
	 * 
	 * @return
	 */
	public String getRenameMessage() {
		return renameMessage;
	}

	/**
	 * Set rename message
	 * 
	 * @param renameMessage
	 */
	public void setRenameMessage(String renameMessage) {
		this.renameMessage = renameMessage;
	}

	/**
	 * Get personal file id
	 * 
	 * @return
	 */
	public Long getPersonalFileId() {
		return personalFileId;
	}

	/**
	 * Set personal file id
	 * 
	 * @param personalFileId
	 */
	public void setPersonalFileId(Long personalFileId) {
		this.personalFileId = personalFileId;
	}

	/**
	 * Get whether file is renamed or not
	 * 
	 * @return
	 */
	public boolean isFileRenamed() {
		return fileRenamed;
	}

	/**
	 * Set whether file is renamed or not
	 * 
	 * @param fileRenamed
	 */
	public void setFileRenamed(boolean fileRenamed) {
		this.fileRenamed = fileRenamed;
	}

	/**
	 * Get new file name
	 * 
	 * @return
	 */
	public String getNewFileName() {
		return newFileName;
	}

	/**
	 * Set new file name
	 * 
	 * @param newFileName
	 */
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFileDescription() {
		return fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

}
