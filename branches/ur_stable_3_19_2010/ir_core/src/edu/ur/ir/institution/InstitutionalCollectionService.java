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

package edu.ur.ir.institution;

import java.util.List;

import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;

/**
 * Service class for dealing with institutional collections
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalCollectionService {

	/**  indicates the root collection should be used */
	public static final long ROOT_COLLECTION_ID = 0l;
	
	/**
	 * Delete an institutional collection with the given id.
	 * 
	 * @param id - unique id of the institution
	 */
	public void deleteCollection(InstitutionalCollection collection, IrUser deletingUser);

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
	public List<InstitutionalCollection> getPath(InstitutionalCollection collection);

	/**
	 * Get institutional collections sorting according to the sort information .  
	 * If the parent collection id is null then the root set of collections are 
	 * returned
	 * 
	 * @param repositoryId - id of the repository
	 * @param parentCollectionId - id of the parent collection collection
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param orderType - order(Asc/Desc)
	 * @return List of root collections containing the specified information.
	 */
	public List<InstitutionalCollection> getCollections(
			final Long repositoryId,
			final Long parentCollectionId,
			final int rowStart, final int numberOfResultsToShow, final String orderType);

    /**
     * Get a count of collections for given parent collection.
     * This only returns root collections for the user.  If the parent collections id
     * is null, then selection from the root is assumed.
     *  
     * @param repositoryId - id of the repository
	 * @param parentCollectionId - id of the parent collection collection
     * 
     * @return - the number of collections found
     */
    public Long getCollectionsCount( 
    		final Long repositoryId,
    		final Long parentCollectionId);
    
    /**
     * Get an institutional collection.
     * 
     * @param id - id of the institutional collection
     * @param lock - upgrade the lock
     * 
     * @return the institutional collection if found otherwise null.
     */
    public InstitutionalCollection getCollection(Long id, boolean lock);

	/**
	 * Save the institionalCollection to persistent storage.
	 * 
	 * @param institutionalCollection
	 */
	public void saveCollection(InstitutionalCollection institutionalCollection);


 	/**
	 * Get institutional collection for the given Ids
	 * 
	 * @param collectionIds List of collection ids
	 * 
	 * @return List of institutional collections
	 */
	public List<InstitutionalCollection> getCollections(List<Long> collectionIds);
	
	/**
	 * Returns all children for the given collection.
	 * 
	 * @param parent
	 * @return the list of children for the collection.
	 */
	public List<InstitutionalCollection> getAllChildrenForCollection(InstitutionalCollection parent);

	/**
	 * Find if the item is already published to this collection.
	 * 
	 * @param  institutionalCollectionId Id of the institutional collection
	 * @param generic item Id Id  
	 * 
	 * @return True if item is published to the collection else false
	 */
	public boolean isItemPublishedToCollection(Long institutionalCollectionId, Long genericItemId);
	
	/**
	 * Allows items and collections to be moved to a different parent collection.  If
	 * a duplicate name exists between the moved collections and the destination, 
	 * the move does not occur. Items are allowed to have duplicate names.
	 * 
	 * @param destination - destination to move selected items and collections to
	 * @param collectionToMove - collections to move
	 * @param itemsToMove - items to move
	 * 
	 * @return list of collections that could not be moved if there were duplicate names 
	 * in the destination 
	 */
	public List<InstitutionalCollection> moveCollectionInformation(InstitutionalCollection destination,
			List<InstitutionalCollection> collectionToMove, List<InstitutionalItem> itemsToMove);
		
	
	/**
	 * Move the collection(s) to the root of the repository.  If a duplicate name exists between 
	 * the moved collections and the destination, the move does not occur.  The root of the
	 * repository does not contain items.
	 *  
	 * @param repository - repository to move the collections to.
	 * @param collectionsToMove - set of collections to move
	 * 
	 * @return list of collections that could not be moved if there were duplicate names 
	 * in the destination 
	 */
	public List<InstitutionalCollection> moveCollectionInformation(Repository repository,
			List<InstitutionalCollection> collectionsToMove);
	
	/**
	 * Get the count of number of items in a collection and its sub-collections
	 * 
	 * @param collection
	 *  
	 * @return Number of items
	 */
	public Long getInstitutionalItemCountForCollectionAndChildren(InstitutionalCollection collection);
	
	/**
	 * Get the count of number of items in a collection
	 * 
	 * @param collection
	 *  
	 * @return Number of items
	 */
	public Long getInstitutionalItemCountForCollection(InstitutionalCollection collection);

	/**
	 * Get the count of institutional collections
	 * 
	 * @return Number of collections in repository
	 */
	public Long getCount();
	
	/**
	 * Returns the count for number of children and sub collections for a given collection
	 * 
	 * @param institutionalCollection 
	 * @return
	 */
	public Long getTotalSubcollectionCount(InstitutionalCollection institutionalCollection);
	
	/**
	 * Get list of ir file ids in items os specified collection 
	 * 
	 * @param collectionId Id of collection
	 * @return list of ir file ids
	 */
	public List<Long> getItemIrFileIdsForCollection(Long collectionId);
	
	/**
	 * Get list of ir file ids in items of specified collection and its children 
	 * 
	 * @param institutionalCollection collection
	 * @return list of ir file ids
	 */
	public List<Long> getItemIrFileIdsForCollectionAndItsChildren(InstitutionalCollection institutionalCollection);
	
	/**
	 * Get list of ir file ids in all collection  
	 * 
	 * @return list of ir file ids
	 * @return
	 */
	public List<Long> getItemIrFileIdsForAllCollections() ;
	
	/**
	 * Send email to reviewer of the item
	 * 
	 * @param institutionalCollection
	 * @param itemName
	 */
	public void sendEmailToReviewer(InstitutionalCollection institutionalCollection, String itemName);
	
	/**
	 * Make all items within this specified collection as public
	 * 
	 * @param institutionalCollection whose items has to be set as public
	 */
	public void setAllItemsWithinCollectionPublic(InstitutionalCollection institutionalCollection);
	
	/**
	 * Sets all items within a collection as private
	 * 
	 * @param institutionalCollection Collection whose items has to be set as private
	 */
	public void setAllItemsWithinCollectionPrivate(InstitutionalCollection institutionalCollection);
}
