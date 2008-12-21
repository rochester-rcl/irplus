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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserWorkspaceIndexService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to add a folder to the users set of folders.
 * 
 * @author Nathan Sarr
 *
 */
public class AddPersonalFolder extends ActionSupport implements UserIdAware{
	
	/**  Collection data access  */
	private UserService userService;
	
	/** User file system service. */
	private UserFileSystemService userFileSystemService;

	/** the name of the folder to add */
	private String folderName;
	
	/** Description of the folder */
	private String folderDescription;
	
	/** Current folder the user is looking at  */
	private Long parentFolderId;
	
	/** Id of the folder to update for updating  */
	private Long updateFolderId;
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = -927739179789125748L;
	
	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(AddPersonalFolder.class);
	
	/**  User object */
	private Long userId;

	/**  Indicates the folder has been added*/
	private boolean folderAdded = false;
	
	/** Message that can be displayed to the user. */
	private String folderMessage;
	
	/** User index service for indexing files */
	private UserWorkspaceIndexService userWorkspaceIndexService;
	
	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;

	
	/**
	 * Create the new folder
	 */
	public String add() throws Exception
	{
		log.debug("creating a personal folder parent folderId = " + parentFolderId);
		folderAdded = false;
		// assume that if the current folder id is null or equal to 0
		// then we are adding a root folder to the user.
		if(parentFolderId == null || parentFolderId == 0)
		{
			folderAdded = addRootFolder();
		}
		else
		{
			folderAdded = addSubFolder();
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
		
		Repository repository = 
			repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		log.debug("updating a personal folder parent folderId = " + parentFolderId);
		folderAdded = false;

		PersonalFolder other = null;
		
		// check the name.  This makes sure that 
		// if the name has been changed, it does not conflict
		// with a folder already in the folder system.
		if( parentFolderId == null || parentFolderId == 0)
		{
			other = userFileSystemService.getRootPersonalFolder(folderName, userId);
		}
		else
		{
			other = userFileSystemService.getPersonalFolder(folderName, parentFolderId);
		}
		
		// name has been changed and does not conflict
		if( other == null)
		{
			PersonalFolder existingFolder = userFileSystemService.getPersonalFolder(updateFolderId, true);
			try {
				existingFolder.reName(folderName);
				userFileSystemService.makePersonalFolderPersistent(existingFolder);
				userWorkspaceIndexService.updateIndex(repository, existingFolder);
				folderAdded = true;
			} catch (DuplicateNameException e) {
				folderAdded = false;
				folderMessage = getText("personalFolderAlreadyExists", new String[]{folderName});
				addFieldError("personalFolderAlreadyExists", folderMessage);
			} catch (IllegalFileSystemNameException ifsne) {
				folderAdded = false;
				folderMessage = getText("illegalPersonalFolderName", new String[]{folderName, String.valueOf(ifsne.getIllegalCharacters())});
				addFieldError("illegalPersonalFolderName", folderMessage);
			}
			
		}
		// name has not been changed
		else if(other.getId().equals(updateFolderId))
		{
			other.setDescription(folderDescription);
			userFileSystemService.makePersonalFolderPersistent(other);
			userWorkspaceIndexService.updateIndex(repository, other);
			folderAdded = true;
		} else {
			folderMessage = getText("personalFolderAlreadyExists", new String[]{folderName});
			addFieldError("personalFolderAlreadyExists", folderMessage);
		}
			
	    return "added";
		
	}

	/**
	 * Loads the folder
	 * 
	 * @return
	 */
	public String get()
	{
		log.debug("get called");
		
		PersonalFolder folder = userFileSystemService.getPersonalFolder(updateFolderId, true);
		folderName = folder.getName();
		folderDescription = folder.getDescription();
		
	    return "get";
	}
	
	/**
	 * The user service for dealing with actions.
	 * 
	 * @return
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * The user service for dealing with actions.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Get the user for this folder
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Set the user for this user.
	 * 
	 * @see edu.ur.ir.web.action.UserAware#setOwner(edu.ur.ir.user.IrUser)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
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
	 * Creates a new root folder 
	 * @throws DuplicateNameException 
	 * @throws IOException 
	 */
	private boolean addRootFolder()  throws IllegalFileSystemNameException
	{
		Repository repository = 
			repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		 boolean added = false;
		 IrUser thisUser = userService.getUser(userId, true);
		 if( thisUser.getRootFolder(folderName) == null )
	     {
			 PersonalFolder personalFolder = null;
			 try {
				personalFolder = thisUser.createRootFolder(folderName);
				personalFolder.setDescription(folderDescription);
				userFileSystemService.makePersonalFolderPersistent(personalFolder);
				 
				IrUser user = personalFolder.getOwner();
				if( user.getPersonalIndexFolder() == null )
				{
						userFileSystemService.createIndexFolder(user, repository, 
								user.getUsername() + " Index Folder");
				}
				userWorkspaceIndexService.addToIndex(repository, personalFolder);
				
		        added = true;
			 } catch (DuplicateNameException e) {
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
		 return added;
	}
	
	/**
	 * adds a sub folder to an existing folder
	 * @throws IOException 
	 */
	private boolean addSubFolder() 
	{
		boolean added = false;
		PersonalFolder folder = userFileSystemService.getPersonalFolder(parentFolderId, true);
		Repository repository = 
			repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		try
		{
		    PersonalFolder personalFolder = folder.createChild(folderName);
		    personalFolder.setDescription(folderDescription);
		    userFileSystemService.makePersonalFolderPersistent(folder);
		  
		    userWorkspaceIndexService.addToIndex(repository, personalFolder);
			
		    added = true;
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
		return added;
	}
	
	/**
	 * Indicates if the folder has been added 
	 * 
	 * @return
	 */
	public boolean isFolderAdded() {
		return folderAdded;
	}

	/**
	 * Get the folder added message.
	 * 
	 * @return
	 */
	public String getFolderMessage() {
		return folderMessage;
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

	public Long getUpdateFolderId() {
		return updateFolderId;
	}

	public void setUpdateFolderId(Long updateFolderId) {
		this.updateFolderId = updateFolderId;
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

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
}
