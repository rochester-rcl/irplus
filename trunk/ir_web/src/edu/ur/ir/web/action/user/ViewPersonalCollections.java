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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Validateable;

import edu.ur.ir.FileSystem;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Class to view personal collections.
 * 
 * @author Nathan Sarr
 *
 */
public class ViewPersonalCollections extends ActionSupport implements  
 UserIdAware, Validateable{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -186085179160200023L;

	/**  Id of the user who has the collections  */
	private Long userId;
	
	/** The user who owns the collections  */
	private IrUser user;
	
	/**  User information data access  */
	private UserService userService;
	
	/** process for setting up personal files to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/** service for accessing index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
		
    /** A collection of personal collections and items for a user in a given location of
        their personal directory.*/
    private List<FileSystem> fileSystem = new LinkedList<FileSystem>();
    
    /** set of personal collections that are the path for the current personal collection */
    private Collection <PersonalCollection> collectionPath;
	
	/** The collection that owns the listed items and personal collections */
	private Long parentCollectionId = 0l;
	
	/** list of collection ids to perform actions on*/
	private Long[] collectionIds;
	
	/** list of item ids to perform actions on*/
	private Long[] itemIds;
	
	/** True indicates the collections were deleted */
	private boolean collectionsDeleted = true;
	
	/** Message used to indicate there is a problem with deleting the collections. */
	private String collectionsDeletedMessage;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "name";
	
	/** use the type sort this is information for the page */
	private String collectionTypeSort = "none";
	
	/** use the name sort this is information for the page */
	private String collectionNameSort = "none";
	
	/**  Logger for view personal collections action */
	private static final Logger log = Logger.getLogger(ViewPersonalCollections.class);
	
	/** Service for dealing with user file system. */
	private UserPublishingFileSystemService userPublishingFileSystemService;

	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the table of personal collections and items.
	 * 
	 * @return
	 */
	public String getTable()
	{
		if(parentCollectionId != null && parentCollectionId > 0)
		{
			PersonalCollection parent = userPublishingFileSystemService.getPersonalCollection(parentCollectionId, false);
			if(!parent.getOwner().getId().equals(userId))
			{
				return "accessDenied";
			}
			
		}
		log.debug("getTableCalled");
		createFileSystem();
		return SUCCESS;
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return
	 */
	public String deleteCollectionSystemObjects()
	{
		log.debug("Delete folders called");
		if( collectionIds != null )
		{
		    for(int index = 0; index < collectionIds.length; index++)
		    {
			    log.debug("Deleting collection with id " + collectionIds[index]);
			    PersonalCollection pc = userPublishingFileSystemService.getPersonalCollection(collectionIds[index], false);
			    if(!pc.getOwner().getId().equals(userId))
			    {
			    	return "accessDenied";
			    }
			    
			    List<PersonalItem> allItems = userPublishingFileSystemService.getAllItemsForCollection(pc);
			    for(PersonalItem personalItem : allItems)
			    {
			    	userWorkspaceIndexProcessingRecordService.save(personalItem.getOwner().getId(), personalItem, 
			    			indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
			    }
			    
			    // set delete records for the personal items
			    userPublishingFileSystemService.deletePersonalCollection(pc, pc.getOwner(), "OWNER DELETING PERSONAL COLLECTION - " + pc.getFullPath());
		    }
		}
		
		if(itemIds != null)
		{
			for(int index = 0; index < itemIds.length; index++)
			{
				log.debug("Deleting item with id " + itemIds[index]);
				PersonalItem pi = userPublishingFileSystemService.getPersonalItem(itemIds[index], false);
				if( !pi.getOwner().getId().equals(userId))
				{
					return "accessDenied";
				}
				userWorkspaceIndexProcessingRecordService.save(pi.getOwner().getId(), pi, 
		    			indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
				userPublishingFileSystemService.deletePersonalItem(pi, pi.getOwner(), "OWNER DELETING ITEM");
			}
		}
		createFileSystem();
		return SUCCESS;
	}
	
	
	/**
	 * Create the file system to view.
	 */
	private void createFileSystem()
	{
		user = userService.getUser(userId, false);
		if(parentCollectionId != null && parentCollectionId > 0)
		{
			log.debug("getting path for parent collection id = " + parentCollectionId);
		    collectionPath = userPublishingFileSystemService.getPersonalCollectionPath(parentCollectionId);
		}
		else
		{
			parentCollectionId = 0l;
		}
		
		Collection<PersonalCollection> myPersonalCollections = userPublishingFileSystemService.getPersonalCollectionsForUser(userId, parentCollectionId);
		Collection<PersonalItem> myPersonalItems = userPublishingFileSystemService.getPersonalItems(userId, parentCollectionId);
		
	    fileSystem = new LinkedList<FileSystem>();
	    
	    fileSystem.addAll(myPersonalCollections);
	    fileSystem.addAll(myPersonalItems);
	    
	    FileSystemSortHelper sortHelper = new FileSystemSortHelper();
	    if( sortElement.equals("type"))
	    {
	    	if(sortType.equals("asc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.TYPE_ASC);
	    		collectionTypeSort = "asc";
	    	}
	    	else if( sortType.equals("desc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.TYPE_DESC);
	    		collectionTypeSort = "desc";
	    	}
	    	else
	    	{
	    		collectionTypeSort = "none";
	    	}
	    }
	    
	    if( sortElement.equals("name"))
	    {
	    	if(sortType.equals("asc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.NAME_ASC);
	    		collectionNameSort = "asc";
	    	}
	    	else if( sortType.equals("desc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.NAME_DESC);
	    		collectionNameSort = "desc";
	    	}
	    	else
	    	{
	    		collectionNameSort = "none";
	    	}
	    }
	}
	


	/**
	 * User service for accessing user information.
	 * 
	 * @return
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Set the user service for accessing user information.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	/**
	 * Get the parent collection id.
	 * 
	 * @return
	 */
	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	/**
	 * Set the parent collection id.
	 * 
	 * @param parentCollectionId
	 */
	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	/**
	 * Get the collection ids 
	 * 
	 * @return
	 */
	public Long[] getCollectionIds() {
		return collectionIds;
	}

	/**
	 * Set the collection ids.
	 * 
	 * @param collectionIds
	 */
	public void setCollectionIds(Long[] collectionIds) {
		this.collectionIds = collectionIds;
	}

	/**
	 * Returns true if the collection is deleted.
	 * 
	 * @return
	 */
	public boolean isCollectionsDeleted() {
		return collectionsDeleted;
	}

	/**
	 * @param collectionsDeleted
	 */
	public void setCollectionsDeleted(boolean collectionsDeleted) {
		this.collectionsDeleted = collectionsDeleted;
	}

	public String getcollectionsDeletedMessage() {
		return collectionsDeletedMessage;
	}

	public void setcollectionsDeletedMessage(String collectionsDeletedMessage) {
		this.collectionsDeletedMessage = collectionsDeletedMessage;
	}
	
	
	@SuppressWarnings("unchecked")
	public Collection getFileSystem() {
		return fileSystem;
	}
	

	/**
	 * Set of item ids to work with.
	 * 
	 * @return
	 */
	public Long[] getItemIds() {
		return itemIds;
	}

	/**
	 * Set of item ids to work with.  
	 * 
	 * @param itemIds
	 */
	public void setItemIds(Long[] itemIds) {
		this.itemIds = itemIds;
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	public void validate()
	{
		log.debug("Validate called");
	}

	/**
	 * Get the set of collections that make up the path to this personal collection.
	 * 
	 * @return
	 */
	public Collection<PersonalCollection> getCollectionPath() {
		return collectionPath;
	}

	/**
	 * Get the user who owns the personal collections
	 * 
	 * @return
	 */
	public IrUser getUser() {
		return user;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getSortElement() {
		return sortElement;
	}

	public void setSortElement(String sortElement) {
		this.sortElement = sortElement;
	}

	public String getCollectionTypeSort() {
		return collectionTypeSort;
	}

	public void setCollectionTypeSort(String collectionTypeSort) {
		this.collectionTypeSort = collectionTypeSort;
	}

	public String getCollectionNameSort() {
		return collectionNameSort;
	}

	public void setCollectionNameSort(String collectionNameSort) {
		this.collectionNameSort = collectionNameSort;
	}

	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

}
