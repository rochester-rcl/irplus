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


package edu.ur.ir.user.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalCollectionDAO;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.PersonalItemDAO;
import edu.ur.ir.user.PersonalItemDeleteRecord;
import edu.ur.ir.user.PersonalItemDeleteRecordDAO;
import edu.ur.ir.user.UserPublishingFileSystemService;


/**
 * Implementation of the user publishing file system service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserPublishingFileSystemService implements UserPublishingFileSystemService {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 526098690480011095L;

	/** collection data access object */
	private PersonalCollectionDAO personalCollectionDAO;
	
	/**   Data access for personal items */
	private PersonalItemDAO personalItemDAO;
	
	/** Service for dealing with items. */
	private ItemService itemService;
	
	/**  User data access  */
	private IrUserDAO irUserDAO;
	
	/** captures information about deleting information */
	private PersonalItemDeleteRecordDAO personalItemDeleteRecordDAO;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserPublishingFileSystemService.class);

	
	/**
	 * Get personal collections by the specified id.
	 * 
	 * @param id - id of the collection.
	 * @return
	 */
	public PersonalCollection getPersonalCollectionById(Long id) {
        return personalCollectionDAO.getById(id, false);		
	}
	
	/**
	 * Get the personal collection path 
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getPersonalCollectionPath(java.lang.Long)
	 */
	public List<PersonalCollection> getPersonalCollectionPath(Long personalCollectionId) {
		PersonalCollection c = this.getPersonalCollectionById(personalCollectionId);
		return personalCollectionDAO.getPath(c);
	}

	
	/**
	 * Get the personal collections with the specified criteria
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getPersonalCollections(java.util.List, java.lang.Long, java.lang.Long, int, int)
	 */
	public List<PersonalCollection> getPersonalCollections( Long userId,
			Long parentCollectionId) {
		if( parentCollectionId == null || parentCollectionId == ROOT_COLLECTION_ID)
		{
			return personalCollectionDAO.getRootPersonalCollections(userId);
		}
		else
		{
		    return personalCollectionDAO.getSubPersonalCollections(  userId, parentCollectionId );
		}
	}
	
	/**
	 * Get the personal collections for the specified user.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getAllPersonalCollectionsForUser(java.lang.Long)
	 */
	public List<PersonalCollection> getAllPersonalCollectionsForUser(Long userId){
		return personalCollectionDAO.getAllPersonalCollectionsForUser(userId);
	}

	/**
	 * Save personal item
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#makePersonalItemPersistent(edu.ur.ir.user.PersonalItem)
	 */
	public void makePersonalItemPersistent(PersonalItem personalItem){
		personalItemDAO.makePersistent(personalItem);
	}

	/**
	 * Get the collections for the specified user.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getPersonalCollections(java.lang.Long, java.util.List)
	 */
	public List<PersonalCollection> getPersonalCollections(Long userId, List<Long> collectionIds) {
		return personalCollectionDAO.getPersonalCollections(userId, collectionIds);
	}

	/**
	 * Get the items for the specified user.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getPersonalItems(java.lang.Long, java.util.List)
	 */
	public List<PersonalItem> getPersonalItems(Long userId, List<Long> itemIds) {
		return  personalItemDAO.getPersonalItems(userId, itemIds);
	}


	/**
	 * Get the personal items.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getPersonalItems(java.util.List, java.lang.Long, java.lang.Long, int, int)
	 */
	public List<PersonalItem> getPersonalItems( Long userId,
			Long parentCollectionId) {
		if( parentCollectionId == null || parentCollectionId == ROOT_COLLECTION_ID)
		{
			return  personalItemDAO.getRootPersonalItems( userId);
		}
		else
		{
		   return personalItemDAO.getPersonalItemsInCollectionForUser(userId, parentCollectionId);
		}
	}



	/**
	 * Get the personal collection data access object.
	 * 
	 * @return
	 */
	public PersonalCollectionDAO getPersonalCollectionDAO() {
		return personalCollectionDAO;
	}

	/**
	 * Set the personal collection data access object.
	 * 
	 * @param personalCollectionDAO
	 */
	public void setPersonalCollectionDAO(PersonalCollectionDAO personalCollectionDAO) {
		this.personalCollectionDAO = personalCollectionDAO;
	}
	
	/**
	 * Get sub collections within parent collection for a user 
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getPersonalCollectionsForUser(java.lang.Long, java.lang.Long)
	 */
	public List<PersonalCollection> getPersonalCollectionsForUser(Long userId, Long parentCollectionId) {
	    
		if( parentCollectionId == null || parentCollectionId == ROOT_COLLECTION_ID)
		{
			return  personalCollectionDAO.getRootPersonalCollections(userId);
		}
		else
		{
		   return personalCollectionDAO.getPersonalSubCollectionsForCollection(userId, parentCollectionId);
		}
	}
	
	/**
	 * Create a personal item 
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#createPersonalItem(edu.ur.ir.user.PersonalCollection, edu.ur.ir.user.IrUser, java.lang.String)
	 */
	public PersonalItem createPersonalItem(PersonalCollection parentCollection, IrUser owner, String nameArticles, String name) {
		GenericItem genericItem = new GenericItem(nameArticles, name);
		genericItem.setOwner(owner);
		VersionedItem versionedItem = new VersionedItem(owner, genericItem);
		PersonalItem personalItem = parentCollection.addVersionedItem(versionedItem);
		personalItemDAO.makePersistent(personalItem);
		return personalItem;
	}

	/**
	 * Create a root personal collection 
	 * @throws DuplicateNameException 
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#createRootPersonalCollection(edu.ur.ir.user.IrUser, java.lang.String, java.lang.String)
	 */
	public PersonalCollection createRootPersonalCollection(IrUser owner, String name, String description) 
			throws DuplicateNameException {

		PersonalCollection personalCollection = owner.createRootPersonalCollection(name);
        personalCollection.setDescription(description);
		irUserDAO.makePersistent(owner);
		 
		return personalCollection;
         
	}

	/**
	 * Create the root personal item.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#createRootPersonalItem(edu.ur.ir.user.IrUser, java.lang.String)
	 */
	public PersonalItem createRootPersonalItem(IrUser owner, String nameArticles, String name) {
		GenericItem genericItem = new GenericItem(nameArticles, name);
		genericItem.setOwner(owner);
		VersionedItem versionedItem = new VersionedItem(owner, genericItem);
		PersonalItem personalItem = owner.createRootPersonalItem(versionedItem);
		personalItemDAO.makePersistent(personalItem);
		return personalItem;
	}


	/**
	 * Set the personal item data access object.
	 * 
	 * @return
	 */
	public PersonalItemDAO getPersonalItemDAO() {
		return personalItemDAO;
	}

	/**
	 * Set the personal item data access object.
	 * 
	 * @param personalItemDAO
	 */
	public void setPersonalItemDAO(PersonalItemDAO personalItemDAO) {
		this.personalItemDAO = personalItemDAO;
	}

	
	/**
	 * Get the personal collection with the specified id.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getPersonalCollection(java.lang.Long, boolean)
	 */
	public PersonalCollection getPersonalCollection(Long id, boolean lock) {
		return personalCollectionDAO.getById(id, lock);
	}
	
	/**
	 * Save the personal collection.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#makePersonalCollectionPersistent(edu.ur.ir.user.PersonalCollection)
	 */
	public void makePersonalCollectionPersistent(PersonalCollection entity) {
		personalCollectionDAO.makePersistent(entity);
	}

	
	/**
	 * Delete the personal collection.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#deletePersonalCollection(edu.ur.ir.user.PersonalCollection)
	 */
	public void deletePersonalCollection( PersonalCollection personalCollection, IrUser deletingUser, String deleteReason) {
		
		List<PersonalItem> personalItems = personalCollectionDAO.getAllItemsForCollection(personalCollection);
		
		//delete records for all personal items
		for(PersonalItem personalItem : personalItems)
		{
			deletePersonalItem(personalItem, deletingUser, deleteReason);
		}
		
		if( personalCollection.isRoot() )
		{
			IrUser parentUser = personalCollection.getOwner();
			parentUser.removeRootPersonalCollection(personalCollection);
		}
		else
		{
			PersonalCollection parentCollection = personalCollection.getParent();
			parentCollection.removeChild(personalCollection);
		}
		
		personalCollectionDAO.makeTransient(personalCollection);
		
	}
	
	/**
	 * Get all items for the collection including items from children.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getAllItemsForCollection(edu.ur.ir.user.PersonalCollection)
	 */
	public List<PersonalItem> getAllItemsForCollection(PersonalCollection personalCollection)
	{
		return personalCollectionDAO.getAllItemsForCollection(personalCollection);
	}

	/**
	 * Delete the personal item.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#deletePersonalItem(edu.ur.ir.user.PersonalItem, edu.ur.ir.user.IrUser, java.lang.String)
	 */
	public void deletePersonalItem(PersonalItem personalItem, IrUser deletingUser, String deleteReason) {
		
		// create a delete record
		PersonalItemDeleteRecord personalItemDeleteRecord = new PersonalItemDeleteRecord(deletingUser.getId(),
				personalItem.getId(),
				personalItem.getFullPath(), 
				personalItem.getDescription());
		personalItemDeleteRecord.setDeleteReason(deleteReason);
		personalItemDeleteRecordDAO.makePersistent(personalItemDeleteRecord);
		
		// remove the file from the parent folder
		if( personalItem.getPersonalCollection() != null )
		{
		   personalItem.getPersonalCollection().removePersonalItem(personalItem);
		}
		
		// delete the item
		personalItemDAO.makeTransient(personalItem);
		itemService.deleteVersionedItem(personalItem.getVersionedItem());
	}


	/**
	 * Get the personal item.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getPersonalItem(java.lang.Long, boolean)
	 */
	public PersonalItem getPersonalItem(Long id, boolean lock) {
		return personalItemDAO.getById(id, lock);
	}

	
	/**
	 * Get the personal collection with the specified name and parent collection.
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getPersonalCollection(java.lang.String, java.lang.Long)
	 */
	public PersonalCollection getPersonalCollection(String collectionName,
			Long parentCollectionId) {
		return personalCollectionDAO.getPersonalCollection(collectionName, parentCollectionId);
	}

	/**
	 * Get the root personal collections with the specified name for the speicified user.
	 *  
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getRootPersonalCollection(java.lang.String, java.lang.Long)
	 */
	public PersonalCollection getRootPersonalCollection(String name, Long userId) {
        return personalCollectionDAO.getRootPersonalCollection(name, userId);
	}

	/**
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#getAllCollectionsNotInChildCollections(java.util.List, java.lang.Long, java.lang.Long)
	 */
	public List<PersonalCollection> getAllCollectionsNotInChildCollections(
			List<Long> collections, Long userId, Long parentCollectionId) {
		
		
		HashMap<Long, LinkedList<PersonalCollection>> collectionsGroupedByRoot = 
			new HashMap<Long, LinkedList<PersonalCollection>>();
		LinkedList<Long> rootCollectionIds = new LinkedList<Long>();
		// get the collection id's
		for(Long collectionId : collections)
		{
			// find the collection
			PersonalCollection p = getPersonalCollection(collectionId, false);
			
			// see if we already have a group for it's root collection if not add it
			LinkedList<PersonalCollection> collectionGroup = collectionsGroupedByRoot.get(p.getTreeRoot().getId());
			if( collectionGroup == null)
			{
				rootCollectionIds.add(p.getTreeRoot().getId());
				collectionGroup = new LinkedList<PersonalCollection>();
				collectionGroup.add(p);
				collectionsGroupedByRoot.put(p.getTreeRoot().getId(), collectionGroup);
			}
			else
			{
				collectionGroup.add(p);
			}
		}
		
		// for each set of excluded collections for a root collection owned by the user execute the query.
		Iterator<Long> rootCollectionIterator = collectionsGroupedByRoot.keySet().iterator();
		LinkedList<PersonalCollection> availableCollections = new LinkedList<PersonalCollection>();
		
		while(rootCollectionIterator.hasNext())
		{
			    Long rootCollectionId = rootCollectionIterator.next();
			    List<PersonalCollection> collectionGroup = collectionsGroupedByRoot.get(rootCollectionId); 
				availableCollections.addAll(personalCollectionDAO.getAllCollectionsNotInChildCollections(collectionGroup, 
						userId, rootCollectionId));
				
				if(log.isDebugEnabled())
				{
					log.debug("Current available collections");
					for(PersonalCollection f: availableCollections)
					{
						log.debug("Collection id = " + f.getId());
					}
				}
		}
		
		// all other root collections can be added
		if(log.isDebugEnabled())
		{
			for( Long id : rootCollectionIds)
			{
				log.debug("Adding the following id " + id);
			}
		}
		availableCollections.addAll(personalCollectionDAO.getAllOtherRootCollections(rootCollectionIds, 
				userId));
		return availableCollections;
		
	}

	/**
	 * Allow a user to move items and Collections into a given Collection
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#moveCollectionSystemInformation(edu.ur.ir.user.PersonalCollection, java.util.List, java.util.List)
	 */
	public List<FileSystem> moveCollectionSystemInformation(PersonalCollection destination,
			List<PersonalCollection> CollectionsToMove, List<PersonalItem> itemsToMove) 
	{
		
		LinkedList<FileSystem> notMoved = new LinkedList<FileSystem>();

		// move Collections first
		if( CollectionsToMove != null )
		{
		    for( PersonalCollection collection : CollectionsToMove)
		    {
		    	log.debug("Adding Collection " + collection + " to destination " + destination);
			    
			    try {
			    	destination.addChild(collection);
				} catch (DuplicateNameException e) {
					notMoved.add(collection);
				}

		    }
		}
		
		if( itemsToMove != null && notMoved.size() == 0)
		{
		    for( PersonalItem item : itemsToMove)
		    {
		    	log.debug("Adding item " + item + " to destination " + destination);
		        destination.addItem(item);
		    }
		}
		
		if( notMoved.size() == 0)
		{
			personalCollectionDAO.makePersistent(destination);
		}
		
		return notMoved;
		
	}

	/**
	 * Move the collections.
	 * @throws DuplicateNameException 
	 * 
	 * @see edu.ur.ir.user.UserPublishingFileSystemService#moveCollectionSystemInformation(edu.ur.ir.user.IrUser, java.util.List, java.util.List)
	 */
	public List<FileSystem> moveCollectionSystemInformation(IrUser user,
			List<PersonalCollection> collectionsToMove, List<PersonalItem> itemsToMove) 
	{

		LinkedList<FileSystem> notMoved = new LinkedList<FileSystem>();
		
		// move collections first
		if( collectionsToMove != null )
		{
		    for( PersonalCollection collection : collectionsToMove)
		    {
		    	log.debug("Adding collection " + collection + " to root of user " + user);
			    try {
			    	user.addRootCollection(collection);
				} catch (DuplicateNameException e) {
					notMoved.add(collection);
				}		    	
		    	
		    }
		}
		
		// then move the items
		if( itemsToMove != null && notMoved.size() == 0)
		{
		    for( PersonalItem item : itemsToMove)
		    {
		    	log.debug("Adding item " + item + " to root of user " + user);
		        user.addRootItem(item);
		    }
		}
		
		if( notMoved.size() == 0)
		{
			log.debug("saving user " + user);
			irUserDAO.makePersistent(user);
		}
		
		return notMoved;		
		
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public IrUserDAO getIrUserDAO() {
		return irUserDAO;
	}

	public void setIrUserDAO(IrUserDAO irUserDAO) {
		this.irUserDAO = irUserDAO;
	}

	/**
	 * Get personal item which has specified generic item 
	 * 
	 * @param item
	 * @return
	 */
	public PersonalItem getPersonalItem(GenericItem item) {
		
		return personalItemDAO.getPersonalItem(item.getId());
	}

	public PersonalItemDeleteRecordDAO getPersonalItemDeleteRecordDAO() {
		return personalItemDeleteRecordDAO;
	}

	public void setPersonalItemDeleteRecordDAO(
			PersonalItemDeleteRecordDAO personalItemDeleteRecordDAO) {
		this.personalItemDeleteRecordDAO = personalItemDeleteRecordDAO;
	}
	
	/**
	 * Get all personal items which have the specified generic item ids 
	 * 
	 * @param itemIds - list of generic item ids
	 * @return - all personal items that contain the generic item id.
	 */
	public List<PersonalItem> getAllPersonalItemsByGenericItemIds(List<Long> itemIds)
	{
		return personalItemDAO.getAllPersonalItemsByGenericItemIds(itemIds);
	}
}
