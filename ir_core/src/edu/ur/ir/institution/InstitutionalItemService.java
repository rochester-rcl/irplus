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

import java.util.Date;
import java.util.List;
import java.util.Set;

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;


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
			OrderType orderType);
	


	
	/**
	 * Get a list of items for a specified repository.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param rowEnd -  End row to get data
	 * @param repositoryId - id of the repository to get items 
	 * @param propertyName - The property to sort on
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsOrderByName(int rowStart, int rowEnd, Long repositoryId, 
			OrderType orderType) ;	

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
			OrderType orderType);
		

	
	/**
	 * Get the list of items for the specified collection.  This includes items in sub collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param rowEnd -  maximum number of results to return
	 * @param collectionId - id of the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsOrderByName(int rowStart, 
			int maxResults, 
			InstitutionalCollection collection, 
			OrderType orderType) ;
	
	/**
	 * Get the list of item ids for the specified collection.  This includes items in sub collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param rowEnd -  maximum number of results to return
	 * @param collectionId - id of the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<Long> getCollectionItemsIds(int rowStart, 
			int maxResults, 
			InstitutionalCollection collection, 
			OrderType orderType) ;
	
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
			OrderType orderType);
	
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
			OrderType orderType);
	

	/**
	 * Get a count of institutional items in a collection and its children.
	 * 
	 * @return Items within the specified collection and its sub collection
	 */
	public Long getCountForCollectionAndChildren(InstitutionalCollection collection);
	
	/**
	 * Get subset of all item published between the given start and end dates for the given collection.
	 *
	 * @param rowStart - row to start
	 * @param maxResults - max number of results to fetch
	 * @param collection - collection to get the items for
     * @param orderType - order (ascending /descending)
	 * 
	 * @return - the items between the given start and end date 
	 */
	public List<InstitutionalItem> getItemsOrderByDate(int rowStart,
			int maxResults, InstitutionalCollection collection, OrderType orderType);
	
	/**
	 * Get all item published between the given start and end dates for the given collection.
	 *
	 * @param collection - collection to get the items for
	 * @param startDate - start date
	 * @param endDate - end date
	 * 
	 * @return - the items between the given start and end date 
	 */
	public List<InstitutionalItem> getItems(InstitutionalCollection collection, Date startDate, Date endDate);

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
	 * Get a list of institutional item version for a specified sponsor ordered by publication name.
	 * 
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param sponsorId - id of the sponsor
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional item version download count
	 */
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemNameOrder(int rowStart,
			int maxResults, 
			long sponsorId, 
			OrderType orderType);
	
	/**
	 * Get a list of institutional item version for a specified sponsor ordered by deposit date.
	 * 
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param sponsorId - id of the sponsor
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional item version
	 */
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemDepositDateOrder(int rowStart,
			int maxResults, 
			long sponsorId, 
			OrderType orderType);
	
	/**
	 * Get a list of institutional item version for a specified sponsor ordered by publication name.
	 * 
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param sponsorId - id of the sponsor
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional item version
	 */
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemDownloadOrder(int rowStart,
			int maxResults, 
			long sponsorId, 
			OrderType orderType);
	

	
	/**
	 * Get the count of institutional item version for a given sponsor.
	 * 
	 * @param sponsorId - id of the sponsor
	 * @return - count of items for a sponsor.
	 */
	public Long getItemsBySponsorCount(long sponsorId);
	
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
	
	/**
	 * Get an institutional item version by handle id.
	 * 
	 * @param handleId - id of the handle to get the institutional item by.
	 * @return the found institutional item or null if item version is not found.
	 */
	public InstitutionalItemVersion getInstitutionalItemByHandleId(Long handleId);
	
	/**
	 * Reset the institutional item url.
	 * 
	 * @param institutionalItem
	 * @param institutionalItemVersion
	 */
	public void resetHandle(InstitutionalItem institutionalItem, InstitutionalItemVersion institutionalItemVersion);
	
	/**
	 * Reset all handles in the system.
	 * 
	 * @param batchSize
	 * @param repositoryId
	 */
	public void resetAllHandles(int batchSize, Long repositoryId);
	
	
	/**
	 * Get the list of publication versions for names ordered by download.
	 * 
	 * @param rowStart - row start
	 * @param maxResults - maximum number of results
	 * @param personNames - set of name ids to use
	 * @param orderType - order type
	 * 
	 * @return - return the list of institutional item version download counts
	 */
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByDownload(final int rowStart,
			final int maxResults, 
			final Set<PersonName> personNames, 
			final OrderType orderType);
	
	/**
	 * Get the list of publication versions for names ordered by title
	 * 
	 * @param rowStart - start position in the list
	 * @param maxResults - maximum number of results
	 * @param personNames - set of person names.
	 * @param orderType - order type ascending / descending
	 * 
	 * @return - return the list of institutional item version download counts
	 */
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByTitle(final int rowStart,
			final int maxResults, 
			final Set<PersonName> personNames, 
			final OrderType orderType);
	
	/**
	 * Get the number of downloads for a given set of person names.
	 * 
	 * @param personNames - set of person names.
	 * @return count for the person names.
	 */
	public Long getNumberOfDownlodsForPersonNames(Set<PersonName> personNames);
	
	/**
	 * Get the list of publication versions for names ordered by title
	 * 
	 * @param rowStart - start position in the list
	 * @param maxResults - maximum number of results
	 * @param personNames - set of person names.
	 * @param orderType - order type ascending / descending
	 * 
	 * @return - return the list of institutional item version download counts
	 */
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesBySubmissionDate(final int rowStart,
			final int maxResults, 
			final Set<PersonName> personNames, 
			final OrderType orderType);
	

	/**
	 * Gets a deleted institutional item version by the original institutional item version.
	 * 
	 * @param institutionalItemVersionId - the original institutional item version id.
	 * 
	 * @return the deleted institutional item version record.
	 */
	public DeletedInstitutionalItemVersion getDeletedVersionByItemVersionId(Long institutionalItemVersionId);

	
	/**
	 * Get the deleted institutional item version by the original institutional item id and version number.
	 * 
	 * @param institutionalItemId - original institutional item id
	 * @param versionNumber - version number
	 * 
	 * @return - the deleted institutional item version record.
	 */
	public DeletedInstitutionalItemVersion getDeletedVersionByItemVersion(Long institutionalItemId, int versionNumber);
}
