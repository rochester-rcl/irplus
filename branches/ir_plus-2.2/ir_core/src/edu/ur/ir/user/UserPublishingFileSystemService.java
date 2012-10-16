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

package edu.ur.ir.user;

import java.io.Serializable;
import java.util.List;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.item.GenericItem;

/**
 * Methods for helping deal with the publishing file system for a user.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserPublishingFileSystemService  extends Serializable{
	
	/** indicates that the user should be used as the root of the file  system */
	public static final long ROOT_COLLECTION_ID = 0L;

	/**
	 * Gets the path to the collection starting from the top parent all the way
	 * down to the specified child.  Only includes parents of the specified 
	 * collection.  The list is ordered highest level parent to last child.  This
	 * is useful for displaying the path to a given collection.
	 * 
	 * @param collection 
	 * @return list of parent collections.
	 * 
	 */
	public List<PersonalCollection> getPersonalCollectionPath(Long personalCollectionId);
	
	/**
	 * Get personal collections sorting according to the sort and filter information for the 
	 * specified user.  If the parent collection id is null then the root set of collections are 
	 * returned
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
	 * @return List of root collections containing the specified information.
	 */
	public List<PersonalCollection> getPersonalCollections(final Long userId, final Long parentCollectionId);

  
    /**
     * Returns a list of personal items in the system 
     * If the parent collection id is null, the root items
     * are returned.
     * 
     * @param critiera - criteria to apply for the selection
     * @param userId - id of the user
     * @param rowStart - start row for paging
     * @param rowEnd - end row for paging
     * 
     * @return - the set of personal items meeting the specified criteria.
     */
    public List<PersonalItem> getPersonalItems( final Long userId, final Long parentCollectionId);


	/**
	 * Get the personal collection by unique id.
	 * 
	 * @param id
	 * @param lock
	 * 
	 * @return the found personal collection or null if no collection is found.
	 */
	public PersonalCollection getPersonalCollection(Long id, boolean lock);
	
	/**
	 * Make the personal collection persistent.
	 * 
	 * @param entity
	 */
	public void makePersonalCollectionPersistent(PersonalCollection entity);
	
	/**
	 * Remove the personal collection from persistent storage.
	 * 
	 * @param personalCollection - personal collection to delete
	 * @param deletingUser - user performing the delete
	 * @param deleteReason - reason for the delete
	 */
	public void deletePersonalCollection(PersonalCollection personalCollection, IrUser deletingUser, String deleteReason);
	
	
	/**
	 * This gets all items for the given personal collection.  This includes items from all child
	 * collections.
	 * 
	 * @param personalCollection - personal collection to get items for
	 * @return - all items including items from children.
	 */
	public List<PersonalItem> getAllItemsForCollection(PersonalCollection personalCollection);
	
	
	/**
	 * Get a personal collection with the given name and specified 
	 * parent collection id.
	 * 
	 * @return
	 */
	public PersonalCollection getPersonalCollection(String collectionName, 
			Long parentCollectionId);
	
	
	/**
	 * Get a root personal collection with the specified name for the specified user.
	 * 
	 * @param name - name of the collection to find
	 * @param userId - user id of user who owns the personal collection.
	 * 
	 * @return - the personal collection if found otherwise null. 
	 */
	public PersonalCollection getRootPersonalCollection(String name, Long userId);

	
	/**
	 * Remove the personal item from persistent storage.
	 * 
	 * @param personalItem - item to be deleted
	 * @param deletingUser - user deleting the item
	 * @param deleteReason - reason the item is being deleted
	 */
	public void deletePersonalItem(PersonalItem personalItem, IrUser deletingUser, String deleteReason);
	
	/**
	 * Get the personal item by unique id.
	 * 
	 * @param id
	 * @param lock
	 * 
	 * @return the found personal item or null if no collection is found.
	 */
	public PersonalItem getPersonalItem(Long id, boolean lock);

	/**
	 * Create a personal item with the specified name.
	 * 
	 * @param parentCollectionId - id of the parent collection id.
	 * @param name - name to give the personal item
	 * @param owner - user to create the item for.
	 * 
	 * @return - create the personal item.
	 */
	public PersonalItem createPersonalItem(PersonalCollection personalCollection, IrUser owner, String nameArticles, String name);

	/**
	 * Create the root personal item.
	 * 
	 * @param owner - owner of the item.
	 * @param name - name of the personal item
	 * 
	 * @return the created personal item.
	 */
	public PersonalItem createRootPersonalItem(IrUser owner, String nameArticles, String name);

	/**
	 * Get the personal collections for the specified user.
	 * 
	 * @param userId Id of the user to get the collections
	 * @return
	 */
	public List<PersonalCollection> getAllPersonalCollectionsForUser(Long userId);

	/**
	 * Save personal item
	 * 
	 * @param personalItem Personal item to be saved
	 */
	public void makePersonalItemPersistent(PersonalItem personalItem);

	/**
	 * Get all personal collections not within the specified set of collections.
	 * 
	 * @param personalCollection
	 * @param user who owns the collections
	 * @param id of the parent collection
	 * 
	 * @return all collections not within the specified trees.
	 */
    public List<PersonalCollection> getAllCollectionsNotInChildCollections(List<Long> collectionIds, 
			Long userId,
			Long parentCollectionId);
    
    /**
	 * Create a root personal collection 
	 * 
	 * @param owner User creating the collection
	 * @param name Name of  the collection
	 * @param description Description for the collection
	 * 
	 * @return The Collection created
	 */
    public PersonalCollection createRootPersonalCollection(IrUser owner, String name, String description) 
	throws DuplicateNameException;
 
	/**
	 * Allow a user to move collections and items to a given location.
	 * 
	 * @param destination
	 * @param CollectionsToMove
	 * @param itemsToMove
	 * 
	 * @return List of collections that cannot be moved
	 */
	public List<FileSystem> moveCollectionSystemInformation(PersonalCollection destination,
			List<PersonalCollection> CollectionsToMove, List<PersonalItem> itemsToMove);


	/**
	 * Allow user information to be moved to the root level.
	 * 
	 * @param user - user to add the information to.
	 * 
	 * @param collectionsToMove - collection to move
	 * @param itemsToMove - items to move
	 * 
	 * @return List of collections that cannot be moved
	 */
	public List<FileSystem> moveCollectionSystemInformation(IrUser user,
			List<PersonalCollection> collectionsToMove, List<PersonalItem> itemsToMove);

	/**
	 * Get collections for the specified user with the given collection ids.
	 * 
	 * @param userId - id of the user to get the collections for
	 * @param collectionIds - id's of the collection to get
	 * 
	 * @return the list of found collections
	 */
	public List<PersonalCollection> getPersonalCollections(Long userId, List<Long> collectionIds);
	
	/**
	 * Get items for the specified user with the given item ids.
	 * 
	 * @param userId - id of the user to get the items for
	 * @param itemIds - id's of the item to get
	 * 
	 * @return the list of found items
	 */
	public List<PersonalItem> getPersonalItems(Long userId, List<Long> itemIds) ;

	/**
	 * Get the personal collections for the user.  If the parent collection id is
	 * null or equal to ROOT_COLLECTION_ID the root collections for the user are returned
	 * 
	 * @param userId - id of the user
	 * @param parentCollectionId - id of the collection to use.
	 * 
	 * @return the found collections
	 */
	public List<PersonalCollection> getPersonalCollectionsForUser(Long userId, Long parentCollectionId);
	

	/**
	 * Get personal item which has specified generic item 
	 * 
	 * @param item
	 * @return
	 */
	public PersonalItem getPersonalItem(GenericItem item);
	
	/**
	 * Get all personal items which have the specified generic item 
	 * 
	 * @param itemIds - list of generic item ids
	 * @return - all personal items that contain the generic item id.
	 */
	public List<PersonalItem> getAllPersonalItems(List<Long> itemIds);
}
