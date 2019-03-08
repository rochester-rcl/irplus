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

package edu.ur.ir.web.action.item;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Allows a user to add an item to a personal collection.
 * 
 * @author Nathan Sarr
 *
 */
public class AddItemToCollection extends ActionSupport implements UserIdAware{
	
	/** The root collection id. */
	public static final long ROOT_COLLECTION_ID = 0;
	
	/** eclipse generated id. */
	private static final long serialVersionUID = -7805448742772845859L;
	
	/**  Logger for add personal folder action */
	private static final Logger log = LogManager.getLogger(AddItemToCollection.class);
	
	/** User making the changes */
	private Long userId;
	
	/** list of file Ids selected to add to the item */
	private Long[] fileIds;
	
	/** list of folder Ids selected to add to the item */
	private Long[] folderIds;
	
	/** Name to give the item */
	private String itemName;
	
	/** the name articles - A, An the ... */
	private String itemArticles;
	
	/** Id of the item */
	private GenericItem item;
	
	/** User service for creating new items */
	private UserService userService;
	
	/** User file system service */
	private UserFileSystemService userFileSystemService;
	
	/**  Parent collection to add this item to */
	private Long parentCollectionId;
	
	/**  Personal folder id */
	private Long parentFolderId;

	/** Service for dealing with user file system. */
	private UserPublishingFileSystemService userPublishingFileSystemService;
	
	/** Service for delaing with repository */
	private RepositoryService repositoryService;

	/** Message that user has no ownership over few files */
	private String message;
	
	/** Indicates whether user has chosen files for which user is not the owner */
	private boolean hasOwnFiles = true;
	
	/** process for setting up personal workspace information to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/** index processing type service */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/**
	 * Create the item for the specified collection.
	 * 
	 * @return success.
	 */
	public String createItem()
	{
		log.debug("Create Item articles = " + itemArticles + " item name =  " + itemName + " under collection Id:" + parentCollectionId);
		
		IrUser user = userService.getUser(userId, false);
		
		// user must be an author
		if( !user.hasRole(IrRole.AUTHOR_ROLE) )
		{
			return "accessDenied";
		}
		
		// make sure someone did not try to slip in a file id
		checkFileOwnership();
		if( !hasOwnFiles )
		{
			log.debug("user does not own the files");
			return "accessDenied";
		}
		
		
		PersonalItem personalItem = null;
		if( parentCollectionId == null || parentCollectionId == ROOT_COLLECTION_ID)
		{
			personalItem = userPublishingFileSystemService.createRootPersonalItem(user, itemArticles, itemName);
			
		}
		else
		{
			PersonalCollection personalCollection = 
				userPublishingFileSystemService.getPersonalCollection(parentCollectionId, false);

			personalItem = userPublishingFileSystemService.createPersonalItem(personalCollection, user, itemArticles, itemName);
		}
		
		item = personalItem.getVersionedItem().getCurrentVersion().getItem();
		
		// Add files to item		
		if( fileIds != null )
		{
			for(Long fileId: fileIds)
			{
		    	PersonalFile personalFile = userFileSystemService.getPersonalFile(fileId, false);
		    	if (personalFile.getVersionedFile().getOwner().equals(user)) {
			    	ItemFile itemFile = item.addFile(personalFile.getVersionedFile().getCurrentVersion().getIrFile());
					itemFile.setDescription(personalFile.getVersionedFile().getDescription());
			    	itemFile.setVersionNumber(personalFile.getVersionedFile().getLargestVersion());
		    	}
		    }
		}
		
		// Add Files inside the folder to the item
		if (folderIds != null) {
			for(Long folderId: folderIds)
			{
				PersonalFolder personalFolder = userFileSystemService.getPersonalFolder(folderId, false);
				List<PersonalFile> files = userFileSystemService.getAllFilesForFolder(personalFolder);
				
				for (PersonalFile pf:files) {
						
					if (pf.getVersionedFile().getOwner().equals(user)) {
						ItemFile itemFile = item.addFile(pf.getVersionedFile().getCurrentVersion().getIrFile());
						
						if (itemFile != null) {
							itemFile.setOrder(item.getItemFiles().size());
							itemFile.setDescription(pf.getVersionedFile().getDescription());
							itemFile.setVersionNumber(pf.getVersionedFile().getLargestVersion());
						}
					}
				}
			}
		}		
		
		// save personal item
		userPublishingFileSystemService.makePersonalItemPersistent(personalItem);
		userWorkspaceIndexProcessingRecordService.save(personalItem.getOwner().getId(), personalItem, 
    			indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
		
		return SUCCESS;
	}
	
	/**
     * Checks the ownership for the files
     */
	public String checkFileOwnership() {

		IrUser user = userService.getUser(userId, false);
		
		StringBuffer names = new StringBuffer();
		
		if( fileIds != null )
		{
			for(Long fileId: fileIds)
			{
		    	PersonalFile personalFile = userFileSystemService.getPersonalFile(fileId, false);
		    	if (!personalFile.getVersionedFile().getOwner().equals(user)) {
		    		names.append(personalFile.getName());
		    		names.append(", ");
		    		hasOwnFiles = false;
		    	} 

		    }
		}
		
		if (folderIds != null) {
			for(Long folderId: folderIds)
			{
				PersonalFolder personalFolder = userFileSystemService.getPersonalFolder(folderId, false);
				List<PersonalFile> files = userFileSystemService.getAllFilesForFolder(personalFolder);
				
				for (PersonalFile pf:files) {
						
			    	if (!pf.getVersionedFile().getOwner().equals(user)) {
			    		names.append(pf.getName());
			    		names.append(", ");
			    		hasOwnFiles = false;
			    	} 
				}
			}
		}
		
		if (!hasOwnFiles) {
			names.deleteCharAt(names.length() -2);

			message = getText("userNotOwnerError", new String[]{names.toString()});
		}
		
		return SUCCESS;
	}
	

	
	/**
	 * Set User Id
	 * 
	 * @param userId user logged in
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Get user service
	 * 
	 * @return
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Set user service
	 * 
	 * @param userService user service
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Get the collection id to add the item to
	 * 
	 * @return collection id
	 */
	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	/**
	 * Set the collection id to add the item to
	 * 
	 * @param parentCollectionId Id of the collection
	 */
	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	/**
	 * Get file ids to be added to the item
	 * 
	 * @return
	 */
	public Long[] getFileIds() {
		return fileIds;
	}

	/**
	 * Set file ids to be added to the item
	 * 
	 * @param fileIds Ids of the file
	 */
	public void setFileIds(Long[] fileIds) {
		this.fileIds = fileIds;
	}

	/**
	 * Get te parent folder id
	 * 
	 * @return
	 */
	public Long getParentFolderId() {
		return parentFolderId;
	}

	/**
	 * Set parent folder id
	 * 
	 * @param parentFolderId
	 */
	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	/**
	 * Get item name
	 * 
	 * @return
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * Set item name
	 * 
	 * @param itemName
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * Get user file system service
	 * 
	 * @return
	 */
	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	/**
	 * Set user file system service
	 * 
	 * @param userFileSystemService
	 */
	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

	public Long[] getFolderIds() {
		return folderIds;
	}

	public void setFolderIds(Long[] folderIds) {
		this.folderIds = folderIds;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public GenericItem getItem() {
		return item;
	}

	public void setItem(GenericItem item) {
		this.item = item;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isHasOwnFiles() {
		return hasOwnFiles;
	}

	public void setHasOwnFiles(boolean hasOwnFiles) {
		this.hasOwnFiles = hasOwnFiles;
	}


	public String getItemArticles() {
		return itemArticles;
	}

	public void setItemArticles(String itemArticles) {
		this.itemArticles = itemArticles;
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


}
