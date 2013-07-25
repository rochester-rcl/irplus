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


package edu.ur.ir.web.action.user;


import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.user.FolderAutoShareInfo;
import edu.ur.ir.user.FolderInviteInfo;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
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
	
	private boolean useParentAutoShareProperties = false;
	


	/** process for setting up personal workspace information to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/** service for accessing index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;

	/* invite user service. */
	private InviteUserService inviteUserService;
	
    private PersonalFolder parentFolder;



	/**
	 * Create the new folder
	 */
	public String add() throws Exception
	{
		log.debug("creating a personal folder parent folderId = " + parentFolderId);
		IrUser thisUser = userService.getUser(userId, true);

		
		folderAdded = false;
		// assume that if the current folder id is null or equal to 0
		// then we are adding a root folder to the user.
		if(parentFolderId == null || parentFolderId == 0)
		{
			 // add root folder
			 if( thisUser.getRootFolder(folderName) == null )
		     {
				 PersonalFolder personalFolder = null;
				 try {
					personalFolder = thisUser.createRootFolder(folderName);
					personalFolder.setDescription(folderDescription);
					userFileSystemService.makePersonalFolderPersistent(personalFolder);
					
					userWorkspaceIndexProcessingRecordService.save(personalFolder.getOwner().getId(), personalFolder, 
			    			indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
					
			        folderAdded = true;
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
		}
		else
		{
		    // add sub folder	
			parentFolder = userFileSystemService.getPersonalFolder(parentFolderId, true);
			
			// user must be owner of folder
			if( !parentFolder.getOwner().getId().equals(thisUser.getId()))
			{
				return "accessDenied";
			}
			
			try
			{
			    PersonalFolder personalFolder = parentFolder.createChild(folderName);
			    personalFolder.setDescription(folderDescription);
			    userFileSystemService.makePersonalFolderPersistent(parentFolder);
			    
			    log.debug("use parent auto share properties = " + useParentAutoShareProperties);
			    if(useParentAutoShareProperties)
			    {
			    	Set<FolderAutoShareInfo> shareInfos = parentFolder.getAutoShareInfos();
			    	for(FolderAutoShareInfo info : shareInfos)
			    	{
			    		LinkedList<String> emails = new LinkedList<String>();
			    		emails.add(info.getCollaborator().getDefaultEmail().getEmail());
			    	    inviteUserService.autoShareFolder(emails, personalFolder, info.getPermissions(),  false);
			    	}
			    	
			    	Set<FolderInviteInfo> inviteInfos = parentFolder.getFolderInviteInfos();
			    	for(FolderInviteInfo inviteInfo : inviteInfos)
			    	{
			    		LinkedList<String> emails = new LinkedList<String>();
			    		emails.add(inviteInfo.getEmail());
			    	    inviteUserService.autoShareFolder(emails, personalFolder, inviteInfo.getPermissions(),  false);
			    	}
			    }
			    
			    userWorkspaceIndexProcessingRecordService.save(personalFolder.getOwner().getId(), personalFolder, 
		    			indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
			    
			    folderAdded = true;
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
			
			// do not allow user if they do not own the folder
            if( !existingFolder.getOwner().getId().equals(userId))
            {
            	return "accessDenied";
            }
			
			try {
				existingFolder.reName(folderName);
				userFileSystemService.makePersonalFolderPersistent(existingFolder);
				
				userWorkspaceIndexProcessingRecordService.save(existingFolder.getOwner().getId(), existingFolder, 
		    			indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
				
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
			userWorkspaceIndexProcessingRecordService.save(other.getOwner().getId(), other, 
	    			indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
			
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
		
		if( parentFolderId != null && parentFolderId > 0)
		{
			 parentFolder = userFileSystemService.getPersonalFolder(parentFolderId, true);
			 if( ! parentFolder.getOwner().getId().equals(userId))
			 {
			     return "accessDenied";
			 }
		}
		
		if( updateFolderId != null )
		{
		    PersonalFolder folder = userFileSystemService.getPersonalFolder(updateFolderId, true);
		    
		    if( !folder.getOwner().getId().equals(userId))
		    {
			    return "accessDenied";
		    }
		    
            if( folder.getParent() != null ){
		        parentFolder = folder.getParent();	
		        parentFolderId = parentFolder.getId();
		    }
		    folderName = folder.getName();
		    folderDescription = folder.getDescription();
		   
		}
		
	    return "get";
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

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}
	
	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

	public PersonalFolder getParentFolder() {
		return parentFolder;
	}
	
	public void setUseParentAutoShareProperties(boolean useParentAutoShareProperties) {
		this.useParentAutoShareProperties = useParentAutoShareProperties;
	}

}
