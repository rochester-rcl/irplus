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

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Class for showing the available shared files
 * 
 * @author Nathan Sarr
 *
 */
public class ManageSharedInboxFiles extends ActionSupport implements UserIdAware{
	
	/** generated version id. */
	private static final long serialVersionUID = -7954124847449231029L;
	
	/** content type service */
	private UserService userService;
	
	/** Service for dealing with user file systems */
	private UserFileSystemService userFileSystemService;

	/** Service for dealing with inviting user */
	private InviteUserService inviteUserService;

	/**  Logger for managing content types*/
	private static final Logger log = Logger.getLogger(ManageSharedInboxFiles.class);
	
	/** Set of content types for viewing the content types */
	private Collection<SharedInboxFile> sharedInboxFiles;	
	
	/** Number of shared inbox files */
	private Long sharedInboxFilesCount;

	/** User who owns the shared files  */
	private Long userId;
	
	/** User */
	private IrUser user;
	
	/** set of file ids to move */
	private Long[] sharedInboxFileIds = {};
	
	/** current destination */
	private PersonalFolder destination;
	
	/** boolean to indicate that the action was successful */
	private boolean actionSuccess;
	
	/** files to move */
	private List<SharedInboxFile> filesToMove = new LinkedList<SharedInboxFile> ();

	/** path to the destination */
	private List<PersonalFolder> destinationPath;
	
	/** current contents of the destination folder */
	private List<FileSystem> currentDestinationContents = new LinkedList<FileSystem>();
	
	/** location to move the folders and files  */
	private Long destinationId = UserFileSystemService.ROOT_FOLDER_ID;
	
	/** Set of files not moved  */
	private Set<SharedInboxFile> filesNotMoved = new HashSet<SharedInboxFile>();
	
	/** User index service for indexing files */
	private UserWorkspaceIndexService userWorkspaceIndexService;
	
	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;

	
	private void moveToUser(Repository repository)
	{
 		for(SharedInboxFile inboxFile : filesToMove)
		{
			PersonalFile pf = null;
		    try 
		    {
			    pf = userFileSystemService.addSharedInboxFileToFolders(user, 
							    inboxFile);
				try {
					userWorkspaceIndexService.addToIndex(repository, pf);
				} catch (LocationAlreadyExistsException e) {
					log.error(e);
				} catch (IOException e) {
					log.error(e);
				}
				userWorkspaceIndexService.deleteFromIndex(inboxFile);
			}
		    catch (DuplicateNameException e) 
		    {
		    	filesNotMoved.add(inboxFile);
			}	
		}
	}

	/**
	 * Removes the selected files to workspace
	 * 
	 * @return
	 */
	private void moveToFolder(Repository repository)
	{
		for(SharedInboxFile inboxFile : filesToMove)
		{
			PersonalFile pf = null;
	        
			if( inboxFile != null )
			{
		        try 
		        {
				    pf = userFileSystemService.addSharedInboxFileToFolders(destination,  inboxFile);
					try {
						userWorkspaceIndexService.addToIndex(repository , pf);
					} catch (LocationAlreadyExistsException e) {
						log.error(e);
					} catch (IOException e) {
						log.error(e);
					}
					userWorkspaceIndexService.deleteFromIndex(inboxFile);
		        } catch (DuplicateNameException e) {
		            filesNotMoved.add(inboxFile);
				}
			}
		}
	}
	
	/**
	 * Takes the user to view the locations that the folder can be moved to.
	 * 
	 * @return
	 */
	public String viewLocations()
	{
		log.debug("view move locations");
		user = userService.getUser(userId, false);

		List<Long> listFileIds = new LinkedList<Long>();
		for( Long id : sharedInboxFileIds)
		{
			    listFileIds.add(id);
		}
		filesToMove = userFileSystemService.getSharedInboxFiles(userId, listFileIds);
		
		if (!destinationId.equals(UserFileSystemService.ROOT_FOLDER_ID))
		{
		    destination = 
		    	userFileSystemService.getPersonalFolder(destinationId, false);
		    
		    // user passed in destination that does not belong to them
		    if( !destination.getOwner().getId().equals(userId))
			{
				return "accessDenied";
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
	 * Moves the files to workspace
	 * 
	 * @return
	 */
	public String move()
	{
		log.debug("move called");
		user = userService.getUser(userId, false);

		actionSuccess = true;

		List<Long> listFileIds = new LinkedList<Long>();
		for( Long id : sharedInboxFileIds)
		{
		    listFileIds.add(id);
		}
		
		filesToMove = userFileSystemService.getSharedInboxFiles(userId, listFileIds);
		
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
				false);
		
		
		if( !destinationId.equals(UserFileSystemService.ROOT_FOLDER_ID))
		{
			log.debug("checking destination");
			destination = userFileSystemService.getPersonalFolder(destinationId, false);
			if( !destination.getOwner().getId().equals(userId))
			{
				return "accessDenied";
			}
		    moveToFolder(repository);
		}
		else
		{
			moveToUser(repository);
		}
		
		if( filesNotMoved.size() > 0 )
		{
			String message = getText("fileNamesAlreadyExist");
			actionSuccess = false;
			for(FileSystem fileSystem : filesNotMoved)
			{
			    message = message + " " + fileSystem.getName();
			}
			addFieldError("moveError", message);
		}
		
		//load the data
        viewLocations();		
		
		return SUCCESS;
	}	
 

	
	/**
	 * Get shared inbox files
	 * 
	 * @return
	 */
	public String viewSharedInboxFiles()
	{
		IrUser user = userService.getUser(userId, false);
		log.debug("getting shared inbox files");
		sharedInboxFiles = user.getSharedInboxFiles();
		log.debug("found " + sharedInboxFiles.size() + " inbox files");
		sharedInboxFilesCount = new Long(user.getSharedInboxFiles().size());
		return SUCCESS;

	}

	/**
	 * Get the number of shared inbox files
	 * 
	 * @return
	 */
	public String getNumberOfSharedInboxFiles()
	{
		IrUser user = userService.getUser(userId, false);
		sharedInboxFilesCount = new Long(user.getSharedInboxFiles().size());
		return SUCCESS;
	}
	
	/**
	 * Delete inbox files
	 * 
	 * @return
	 */
	public String deleteSharedInboxFiles() {
		
		for (Long id : sharedInboxFileIds) {
			SharedInboxFile inboxFile = userFileSystemService.getSharedInboxFile(id, false);
			
			// user trying to delete files that do not belong to them
			// exit immediately
			if(!inboxFile.getSharedWithUser().getId().equals(userId))
			{
				return ("accessDenied");
			}
			inviteUserService.deleteSharedInboxFile(inboxFile);
		}
		
		return SUCCESS;
		
	}

	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Determines if all selected files were moved into
	 * the selected folder. 
	 * 
	 * @return true if all files selected were moved
	 */
	public boolean getSelectedFilesMoved()
	{
		return filesNotMoved.size() == 0;
	}

	/**
	 * Service for user data access.
	 * 
	 * @return service for user data access
	 */
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	public Collection<SharedInboxFile> getSharedInboxFiles() {
		return sharedInboxFiles;
	}

	public void setSharedInboxFiles(Collection<SharedInboxFile> sharedInboxFiles) {
		this.sharedInboxFiles = sharedInboxFiles;
	}

	public Set<SharedInboxFile> getFilesNotMoved() {
		return filesNotMoved;
	}

	public void setFilesNotMoved(Set<SharedInboxFile> filesNotMoved) {
		this.filesNotMoved = filesNotMoved;
	}

	public Long getUserId() {
		return userId;
	}

	public void setSharedInboxFileIds(Long[] sharedInboxFileIds) {
		this.sharedInboxFileIds = sharedInboxFileIds;
	}

	public IrUser getUser() {
		return user;
	}

	public List<SharedInboxFile> getFilesToMove() {
		return filesToMove;
	}

	public void setFilesToMove(List<SharedInboxFile> filesToMove) {
		this.filesToMove = filesToMove;
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

	public void setActionSuccess(boolean actionSuccess) {
		this.actionSuccess = actionSuccess;
	}

	public Long getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public UserWorkspaceIndexService getUserWorkspaceIndexService() {
		return userWorkspaceIndexService;
	}

	public void setUserWorkspaceIndexService(
			UserWorkspaceIndexService userWorkspaceIndexService) {
		this.userWorkspaceIndexService = userWorkspaceIndexService;
	}

	public Long getSharedInboxFilesCount() {
		return sharedInboxFilesCount;
	}

	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

}
