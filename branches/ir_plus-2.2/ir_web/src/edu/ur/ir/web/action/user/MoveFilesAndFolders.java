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

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.FileSystem;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Deals with moving files and folders to a specified location.
 * 
 * @author Nathan Sarr
 * 
 */
public class MoveFilesAndFolders extends ActionSupport implements UserIdAware {
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -4003445321902043652L;

	/**  The user id  */
	private Long userId;
	
	/** set of folder ids to move */
	private Long[] folderIds = {};
	
	/** set of file ids to move */
	private Long[] fileIds = {};
	
	/** folders to move */
	private List<PersonalFolder> foldersToMove = new LinkedList<PersonalFolder>();
	
	/** files to move */
	private List<PersonalFile> filesToMove = new LinkedList<PersonalFile> ();
	
	/** location to move the folders and files  */
	private Long destinationId = UserFileSystemService.ROOT_FOLDER_ID;
	
	/** Files and folder service */
	private UserFileSystemService userFileSystemService;
	
	/** User service */
	private UserService userService;
	
	private IrUser user;
	
	/** path to the destination */
	private List<PersonalFolder> destinationPath;
	
	/** current contents of the destination folder */
	private List<FileSystem> currentDestinationContents = new LinkedList<FileSystem>();
	
	/** boolean to indicate that the action was successful */
	private boolean actionSuccess;
	
	/** current destination */
	private PersonalFolder destination;
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(MoveFilesAndFolders.class);

    /** current root location where all files are being moved from*/
    private Long parentFolderId;
	
    /* invite service to deal with auto shared files and folders */
    private InviteUserService inviteUserService;
    
	/* service to send emails when an error occurs */
	private ErrorEmailService errorEmailService;
	
	/* if set to true auto-sharing will be applied to the folders being moved */
	private boolean applyAutoShare;
	
 



	/**
	 * Takes the user to view the locations that the folder can be moved to.
	 * 
	 * @return
	 */
	public String viewLocations()
	{
		log.debug("view move locations");
		user = userService.getUser(userId, false);

		List<Long> listFolderIds = new LinkedList<Long>();
		for( Long id : folderIds)
		{
		    listFolderIds.add(id);
		}
		
		foldersToMove = userFileSystemService.getFolders(userId, listFolderIds);
		List<Long> listFileIds = new LinkedList<Long>();
		for( Long id : fileIds)
		{
			    listFileIds.add(id);
		}
		filesToMove = userFileSystemService.getFiles(userId, listFileIds);
		
		if( !destinationId.equals(UserFileSystemService.ROOT_FOLDER_ID))
		{
		    destination = 
		    	userFileSystemService.getPersonalFolder(destinationId, false);
		    
		    if( !destination.getOwner().getId().equals(userId))
		    {
		    	// user cannot move file into a destination that they do not own
		    	return "accessDenied";
		    }
		    
		    // make sure the user has not navigated into a child or itself- this is illegal
		    for(PersonalFolder folder: foldersToMove)
		    {
		    	if(destination.equals(folder))
		    	{
		    		throw new IllegalStateException("cannot move a folder into itself destination = " + destination
		    				+ " folder = " + folder);
		    	}
		    	else if( folder.getTreeRoot().equals(destination.getTreeRoot()) &&
		    			 destination.getLeftValue() > folder.getLeftValue() &&
		    			 destination.getRightValue() < folder.getRightValue() )
		    	{
		    		throw new IllegalStateException("cannot move a folder into a child destination = " + destination
		    				+ " folder = " + folder);
		    	}
		    }
		    
		    destinationPath = userFileSystemService.getPersonalFolderPath(destination.getId());
		    currentDestinationContents.addAll(destination.getChildren());
		    currentDestinationContents.addAll(destination.getFiles());
		}
		else
		{
			currentDestinationContents.addAll(user.getRootFolders());
			currentDestinationContents.addAll(user.getRootFiles());
		}
		
		return SUCCESS;
	}

	/**
	 * Takes the user to view the locations that the folder can be moved to.
	 * 
	 * @return
	 */
	public String move()
	{
		log.debug("move files and folders called");
		user = userService.getUser(userId, false);

		List<FileSystem> notMoved = new LinkedList<FileSystem>();
		actionSuccess = true;

		List<Long> listFolderIds = new LinkedList<Long>();
		for( Long id : folderIds)
		{
			log.debug("adding folder id " + id);
		    listFolderIds.add(id);
		}
		
		// folders are accessed by user id so they cannot move folders that do
		// not belong to them.
		foldersToMove = userFileSystemService.getFolders(userId, listFolderIds);

		List<Long> listFileIds = new LinkedList<Long>();
		for( Long id : fileIds)
		{
			log.debug("adding file id " + id);
		    listFileIds.add(id);
		}
		
		// files are accessed by user id so this prevents users from accessing files
		// that do not belong to them
		filesToMove = userFileSystemService.getFiles(userId, listFileIds);
		

		log.debug( "destination id = " + destinationId);
		if( !destinationId.equals(UserFileSystemService.ROOT_FOLDER_ID))
		{
		    destination = 
		    	userFileSystemService.getPersonalFolder(destinationId, false);
		    if( !destination.getOwner().getId().equals(userId))
		    {
		    	// user cannot move file into a destination that they do not own
		    	return "accessDenied";
		    }
		    
		    notMoved = 
				userFileSystemService.moveFolderSystemInformation(destination, 
						foldersToMove, filesToMove);
					    
		}
		else
		{
			notMoved = userFileSystemService.moveFolderSystemInformation(user, 
						foldersToMove, filesToMove);
		}
		
		if( notMoved.size() > 0 )
		{
			String message = getText("folderNamesAlreadyExist");
			actionSuccess = false;
			for(FileSystem fileSystem : notMoved)
			{
			    message = message + " " + fileSystem.getName();
			}
			addFieldError("moveError", message);
		}
		else
		{
			try {
				
				
				LinkedList<PersonalFolder> autoShareFolders = new LinkedList<PersonalFolder>();
				LinkedList<PersonalFile> autoShareFiles = new LinkedList<PersonalFile>();
				if( applyAutoShare )
				{
					autoShareFolders.addAll(foldersToMove);
					autoShareFiles.addAll(filesToMove);
				    inviteUserService.addNewFilesFoldersToFolderWithAutoShare(destination, autoShareFolders, autoShareFiles);
				}
			} catch (FileSharingException e) {
				// this should never happen so log and send email
				log.error(e);
				errorEmailService.sendError(e);
			} catch (PermissionNotGrantedException e) {
				// this should never happen so log and send email
				log.error(e);
				errorEmailService.sendError(e);
			}
		}
		
		
		//load the data
        viewLocations();		
		
		return SUCCESS;
	}

	public void setFolderIds(Long[] folderIds) {
		this.folderIds = folderIds;
	}


	public void setFileIds(Long[] fileIds) {
		this.fileIds = fileIds;
	}


	public void setFoldersToMove(List<PersonalFolder> foldersToMove) {
		this.foldersToMove = foldersToMove;
	}


	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}

	public List<PersonalFolder> getDestinationPath() {
		return destinationPath;
	}


	public List<FileSystem> getCurrentDestinationContents() {
		return currentDestinationContents;
	}

	public boolean getActionSuccess() {
		return actionSuccess;
	}

	public Long getDestinationId() {
		return destinationId;
	}

	public PersonalFolder getDestination() {
		return destination;
	}

	public void setDestination(PersonalFolder destination) {
		this.destination = destination;
	}

	public Long getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	/**
	 * Allow a user id to be passed in.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<PersonalFile> getFilesToMove() {
		return filesToMove;
	}

	public void setFilesToMove(List<PersonalFile> filesToMove) {
		this.filesToMove = filesToMove;
	}

	public List<PersonalFolder> getFoldersToMove() {
		return foldersToMove;
	}

	public IrUser getUser() {
		return user;
	}
	
	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}
	
	public void setApplyAutoShare(boolean applyAutoShare) {
		this.applyAutoShare = applyAutoShare;
	}


	
}