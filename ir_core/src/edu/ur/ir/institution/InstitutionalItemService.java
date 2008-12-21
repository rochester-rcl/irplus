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
import java.util.Set;

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.user.IrUser;


/**
 * Service helping with getting institutional items.
 * 
 * @author NathanS
 *
 */
public interface InstitutionalItemService {

	/**
	 * Get a count of all institutional items in the system - this is across all
	 * repositories if there is more than one.
	 * 
	 * @return count of all items in the system
	 */
	public Long getCount();
	
	/**
	 * Get a count of all institutional items - this is for the
	 * specified repository - usually the system will have only one repository
	 * 
	 * @return count of all items in a specified repository
	 */
	public Long getCount(Long repositoryId);
	
	/**
	 * Get a count of all institutional items in the specified repository with
	 * specified first characters 
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstChar - first character in the name of the item
	 * @return count of items found
	 */
	public Long getCount(Long repositoryId, char nameFirstChar);
	
	/**
	 * Get a count of all institutional items in the specified repository with
	 * specified first characters 
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstCharRange - first character in range
	 * @param nameLastCharRange - last character in the range
	 * 
	 * @return count of titles found that have a first character in the specified range
	 */
	public Long getCount(Long repositoryId, char nameFirstCharRange, char namelastCharRange);
	
	
	/**
	 * Get a count of all institutional items in the specified collection with
	 * specified first character in the title.  THIS INCLUDES items in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @param nameFirstChar - first character in the name of the item
	 * @return count of items found
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstChar);
	
	/**
	 * Get a count of all institutional items in the specified collection with
	 * specified first character -  THIS INCLUDES items in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @param nameFirstCharRange - first character in range
	 * @param nameLastCharRange - last character in the range
	 * 
	 * @return count of titles found that have a first character in the specified range
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstCharRange, char nameLastCharRange);
	
	
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsBetweenChar(final int rowStart,
			int maxResults, 
			Long repositoryId,
			char firstChar,
			char lastChar,
			String orderType);
	


	
	/**
	 * Get a list of items for a specified repository.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param rowEnd -  End row to get data
	 * @param collectionId - id of the collection to get items 
	 * @param propertyName - The property to sort on
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsOrderByName(int rowStart, int rowEnd, Long repositoryId, 
			String orderType) ;	

    /**
     * Get the Institutional item version
     * 
     * @param id - id of the institutional item version
     * @param lock - upgrade the lock
     * 
     * @return the institutional item version or null if not found.
     */
    public InstitutionalItemVersion getInstitutionalItemVersion(Long id, boolean lock);
    
    /**
     * Get the Institutional item
     * 
     * @param id - id of the institutional item
     * @param lock - upgrade the lock
     * 
     * @return the institutional item or null if not found.
     */
    public InstitutionalItem getInstitutionalItem(Long id, boolean lock);
    
	/**
	 * Delete an institutional item with the given unique item id.
	 * 
	 * @param item Item to be deleted
	 * @param deletingUser User deleting the item
	 */
	public void deleteInstitutionalItem(InstitutionalItem item, IrUser deletingUser);
	
    
	/**
	 * Get institutional items for the given Ids
	 * 
	 * @param iItemIds List of institutional item ids
	 * 
	 * @return List of institutional items with the specified ids
	 */
	public List<InstitutionalItem> getInstitutionalItems(List<Long> itemIds);
	
	/**
	 * Save Institutional Item
	 * 
	 * @param InstitutionalItem
	 */
	public void saveInstitutionalItem(InstitutionalItem institutionalItem); 
	
	
	/**
	 * Get a count of institutional items using the generic item using it's id
	 * 
	 * @param genericItemId - id of the generic item to check for
	 * @return number of institutional items using the generic item.
	 */
	public Long getCountByGenericItem(Long genericItemId);
	
	/**
	 * Get a list of items for a specified repository by first character of the name
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsByChar(int rowStart,
			int maxResults, 
			Long repositoryId,
			char firstChar,
			String orderType);
		

	
	/**
	 * Get the list of items for the specified collection.  This includes items in sub collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param rowEnd -  End row to get data
	 * @param collectionId - id of the collection to get items 
	 * @param propertyName - The property to sort on
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsOrderByName(int rowStart, 
			int rowEnd, 
			InstitutionalCollection collection, 
			String orderType) ;
	
	/**
	 * Get a list of items for a specified collection by first character of the name.  
	 * This INCLUDES items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutionalcollection - parent collection
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsByChar(final int rowStart,
			int maxResults, 
			InstitutionalCollection institutionalCollection,
			char firstChar,
			String orderType);
	
	/**
	 * Get a list of items for a specified collection by titles
	 * that start between the specified first character in the name.  This 
	 * INCLUDES items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutional Collection - parent collection 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenChar(int rowStart,
			int maxResults, 
			InstitutionalCollection institutionalCollection,
			char firstChar,
			char lastChar,
			String orderType);
	

	/**
	 * Get a count of institutional items in a collection and its children.
	 * 
	 * @return Items within the specified collection and its sub collection
	 */
	public Long getCountForCollectionAndChildren(InstitutionalCollection collection);

	/**
	 * Get a count of distinct institutional items in the repository.
	 * 
	 * @return
	 */
	public Long getDistinctInstitutionalItemCount();
	
	/**
	 * Save Institutional Item Version
	 * 
	 * @param institutionalItemVersion
	 */
	public void saveInstitutionalItemVersion(InstitutionalItemVersion institutionalItemVersion);
	
	/**
	 * Get a institutional collections  the generic item exists in
	 * 
	 * @param itemId Id of generic Item 
	 * @return the list of collections found
	 */
	public List<InstitutionalCollection> getInstitutionalCollectionsSubmittedForGenericItem(Long itemId);

	/**
	 * Delete institutional item history
	 * 
	 * @param deletedInstitutionalItem
	 */
	public void deleteInstitutionalItemHistory(DeletedInstitutionalItem entity) ;

	/**
	 * Delete institutional item history
	 * 
	 * @param deletedInstitutionalItem
	 */
	public void deleteAllInstitutionalItemHistory();

	/**
	 * Get Delete info for institutional item 
	 * 
	 * @param institutionalItemId Id of institutional item
	 * @return Information about deleted institutional item
	 */
	public DeletedInstitutionalItem getDeleteInfoForInstitutionalItem(Long institutionalItemId);

	/**
	 * Get the publications published under given names
	 * 
	 * @param personNames
	 * @return List of publications
	 */
	public List<InstitutionalItemVersion> getPublicationVersionsByPersonName(Set<PersonName> personName);
	
	/**
	 * Set item as private and assign submitted collections user group permissions
	 * 
	 * @param item Item to be assigned private permission
	 * @param collections Collections the item is submitted to
	 */
	public void setItemPrivatePermissions(GenericItem item, List<InstitutionalCollection> collections);

	/**
	 * Get number of deleted institutional items for user
	 * 
	 * @param userId Id of user who deleted institutional item
	 * @return Number of institutional items deleted
	 */
	public Long getDeletedInstitutionalItemCountForUser(Long userId);

	/**
	 * Get the number of publications contributed by given person names
	 *  
	 * @param personNames Person name of user who contributed
	 * @return Count of publications
	 */
	public Long getPublicationCountByPersonName(Set<PersonName> personNames) ;

	/**
	 * Get Institutional item by given version id
	 * 
	 * @param institutionalVersionId Version id
	 * @return Institutional item found
	 */
	public InstitutionalItem getInstitutionalItemByVersionId(Long institutionalVersionId) ;

	/**
	 * Get institutional items having given generic item id as the latest version
	 * 
	 * @param genericItemId Id of generic item
	 * @return Institutional items found
	 */
	public List<InstitutionalItem> getInstitutionalItemsByGenericItemId(Long genericItemId) ;
}
