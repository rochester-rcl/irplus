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

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.item.ContentTypeCount;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemVersion;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.repository.RepositoryLicenseNotAcceptedException;
import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;


/**
 * Service helping with getting institutional items.
 * 
 * @author NathanS
 *
 */
public interface InstitutionalItemService extends Serializable{

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
	 * @param repositoryId - id of the repository
	 * 
	 * @return count of all items in a specified repository
	 */
	public Long getCount(Long repositoryId);
	
	/**
	 * Get a count of all institutional items for the repository
	 * with the specified content type id
	 * 
	 * @param repositoryId - id of the repository
	 * @param contentTypeId - id of the content type
	 * 
	 * @return count of all items in a specified repository
	 */
	public Long getCount(Long repositoryId, Long contentTypeId);
	
	/**
	 * Get a list of of repository content types with counts for the number
	 * of items within the repository. 
	 * 
	 * @param repositoryId - id of the repository
	 * @return - list of content type counts
	 */
	public List<ContentTypeCount> getRepositoryContentTypeCount(Long repositoryId);

	
	/**
	 * Get a list of of repository content types with counts for the number
	 * of items within the repository. 
	 * 
	 * @param collection - institutional collection
	 * @return - list of content type counts
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getRepositoryContentTypeCount()
	 */
	public List<ContentTypeCount> getCollectionContentTypeCount(InstitutionalCollection collection);
	
	/**
	 * Get a count of all institutional items in the specified repository with
	 * specified first characters 
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstChar - first character in the name of the item
	 * 
	 * @return count of items found
	 */
	public Long getCount(Long repositoryId, char nameFirstChar);
	
	/**
	 * Get a count of all institutional items in the specified repository with
	 * specified first character and content type id 
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstChar - first character in the name of the item
	 * @param contentTypeId - id of the content type
	 * 
	 * @return count of items found
	 */
	public Long getCount(Long repositoryId, char nameFirstChar, Long contentTypeId);

	
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
	 * Get a count of all institutional items in the specified repository with
	 * specified first characters and content type id
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstCharRange - first character in range
	 * @param nameLastCharRange - last character in the range
	 * @param contentTypeId - id of the content type
	 * 
	 * @return count of titles found that have a first character in the specified range
	 */
	public Long getCount(Long repositoryId, char nameFirstCharRange, char namelastCharRange, Long contentTypeId);

	
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
	 * specified first character in the title and has the specified content type.  
	 * THIS INCLUDES items in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @param nameFirstChar - first character in the name of the item
	 * @param contentTypeId - id of the content type
	 * 
	 * @return count of items found
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstChar, Long contentTypeId);

	
	/**
	 * Get a count of all institutional items in the specified collection with
	 * specified first character in the given character range -  THIS INCLUDES items in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @param nameFirstCharRange - first character in range
	 * @param nameLastCharRange - last character in the range
	 * 
	 * @return count of titles found that have a first character in the specified range
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstCharRange, char nameLastCharRange);
	

	/**
	 * Get a count of all institutional items in the specified collection with
	 * specified first character in the given character range with the given content type id-  THIS INCLUDES items in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @param nameFirstCharRange - first character in range
	 * @param nameLastCharRange - last character in the range
	 * 
	 * @return count of titles found that have a first character in the specified range
	 */
	public Long getCount(InstitutionalCollection collection, 
			char nameFirstCharRange, char nameLastCharRange, Long contentTypeId);

	
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
	public List<InstitutionalItem> getRepositoryItemsBetweenCharByPublicationDateOrder(final int rowStart,
			int maxResults, 
			Long repositoryId,
			char firstChar,
			char lastChar,
			OrderType orderType);
	
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
	public List<InstitutionalItem> getRepositoryItemsBetweenCharByFirstAvailableOrder(final int rowStart,
			int maxResults, 
			Long repositoryId,
			char firstChar,
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters with the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsBetweenChar(final int rowStart,
			int maxResults, 
			Long repositoryId,
			char firstChar,
			char lastChar,
			Long contentTypeId,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters with the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsBetweenCharByPublicationDateOrder(final int rowStart,
			int maxResults, 
			Long repositoryId,
			char firstChar,
			char lastChar,
			Long contentTypeId,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters with the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsBetweenCharByFirstAvailableOrder(final int rowStart,
			int maxResults, 
			Long repositoryId,
			char firstChar,
			char lastChar,
			Long contentTypeId,
			OrderType orderType);
	
	
	/**
	 * Get a list of items for a specified repository.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param rowEnd -  End row to get data
	 * @param repositoryId - id of the repository to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsOrderByName(int rowStart, int rowEnd, Long repositoryId, 
			OrderType orderType) ;	


	
	/**
	 * Get a list of items for a specified repository with the given content type id.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsOrderByName(int rowStart, 
			int maxResults, 
			Long repositoryId, 
			Long contentTypeId, 
			OrderType orderType) ;	
    
	
	/**
	 * Get a list of items for a specified repository with the given content type id.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsByPublicationDateOrder(int rowStart, 
			int maxResults, 
			Long repositoryId, 
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository with the given content type id.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsByFirstAvailableOrder(int rowStart, 
			int maxResults, 
			Long repositoryId, 
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository with the given content type id.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsByPublicationDateOrder(int rowStart, 
			int maxResults, 
			Long repositoryId, 
			Long contentTypeId, 
			OrderType orderType) ;
	
	/**
	 * Get a list of items for a specified repository with the given content type id.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsByFirstAvailableOrder(int rowStart, 
			int maxResults, 
			Long repositoryId, 
			Long contentTypeId, 
			OrderType orderType) ;
	
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
	public List<InstitutionalItem> getRepositoryItemsByCharPublicationDateOrder(int rowStart,
			int maxResults, 
			Long repositoryId,
			char firstChar,
			OrderType orderType);
	
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
	public List<InstitutionalItem> getRepositoryItemsByCharFirstAvailableOrder(int rowStart,
			int maxResults, 
			Long repositoryId,
			char firstChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository by first character of the name and
	 * the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsByChar(int rowStart,
			int maxResults, 
			Long repositoryId,
			Long contentTypeId,
			char firstChar,
			OrderType orderType);
		
	/**
	 * Get a list of items for a specified repository by first character of the name and
	 * the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsByCharPublicationDateOrder(int rowStart,
			int maxResults, 
			Long repositoryId,
			Long contentTypeId,
			char firstChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository by first character of the name and
	 * the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsByCharFirstAvailableOrder(int rowStart,
			int maxResults, 
			Long repositoryId,
			Long contentTypeId,
			char firstChar,
			OrderType orderType);
	
	/**
	 * Get the list of items for the specified collection.  This includes items in sub collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
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
	 * Get the list of items for the specified collection.  This includes items in sub collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
	 * @param collectionId - id of the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsPublicationDateOrder(int rowStart, 
			int maxResults, 
			InstitutionalCollection collection, 
			OrderType orderType);


	/**
	 * Get the list of items for the specified collection.  This includes items in sub collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
	 * @param collectionId - id of the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsFirstAvailableOrder(int rowStart, 
			int maxResults, 
			InstitutionalCollection collection, 
			OrderType orderType);
	
	/**
	 * Get the list of items for the specified collection with the given 
	 * content type id.  This includes items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
	 * @param collection - the collection to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsOrderByName(int rowStart, 
			int maxResults, 
			InstitutionalCollection collection, 
			Long contentTypeId,
			OrderType orderType) ;

	/**
	 * Get the list of items for the specified collection with the given 
	 * content type id.  This includes items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
	 * @param collection - the collection to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsPublicationDateOrder(int rowStart, 
			int maxResults, 
			InstitutionalCollection collection, 
			Long contentTypeId,
			OrderType orderType) ;
	
	/**
	 * Get the list of items for the specified collection with the given 
	 * content type id.  This includes items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
	 * @param collection - the collection to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsFirstAvailableOrder(int rowStart, 
			int maxResults, 
			InstitutionalCollection collection, 
			Long contentTypeId,
			OrderType orderType) ;
	/**
	 * Get the list of item ids for the specified collection.  This includes items in child collections
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
	public List<InstitutionalItem> getCollectionItemsByCharPublicationDateOrder(final int rowStart,
			int maxResults, 
			InstitutionalCollection institutionalCollection,
			char firstChar,
			OrderType orderType);
	
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
	public List<InstitutionalItem> getCollectionItemsByCharFirstAvailableOrder(final int rowStart,
			int maxResults, 
			InstitutionalCollection institutionalCollection,
			char firstChar,
			OrderType orderType);
	
	
	/**
	 * Get a list of items for a specified collection by first character of the name 
	 * and the specified content type.  
	 * This INCLUDES items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutionalcollection - parent collection
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsByChar(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId,
			char firstChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified collection by first character of the name 
	 * and the specified content type.  
	 * This INCLUDES items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutionalcollection - parent collection
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsByCharPublicationDateOrder(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId,
			char firstChar,
			OrderType orderType);
	
	
	/**
	 * Get a list of items for a specified collection by first character of the name 
	 * and the specified content type.  
	 * This INCLUDES items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutionalcollection - parent collection
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsByCharFirstAvailableOrder(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId,
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
	public List<InstitutionalItem> getCollectionItemsBetweenCharPublicationDateOrder(int rowStart,
			int maxResults, 
			InstitutionalCollection institutionalCollection,
			char firstChar,
			char lastChar,
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
	public List<InstitutionalItem> getCollectionItemsBetweenCharFirstAvailableOrder(int rowStart,
			int maxResults, 
			InstitutionalCollection institutionalCollection,
			char firstChar,
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified collection by titles
	 * that start between the specified first character in the name and the given content
	 * type.  This INCLUDES items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutional Collection - parent collection 
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenChar(int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId,
			char firstChar,
			char lastChar,
			OrderType orderType);
	

	/**
	 * Get a list of items for a specified collection by titles
	 * that start between the specified first character in the name and the given content
	 * type.  This INCLUDES items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutional Collection - parent collection 
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenCharPublicationDateOrder(int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId,
			char firstChar,
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified collection by titles
	 * that start between the specified first character in the name and the given content
	 * type.  This INCLUDES items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutional Collection - parent collection 
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenCharFirstAvailableOrder(int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId,
			char firstChar,
			char lastChar,
			OrderType orderType);
	
	
	/**
	 * Get a count of institutional items in a collection and its children.
	 * 
	 * @param collection - collection to start counting from
	 * 
	 * @return Items within the specified collection and its sub collection
	 */
	public Long getCountForCollectionAndChildren(InstitutionalCollection collection);

	/**
	 * Get a count of institutional items in a collection and its children with
	 * the specified content type.
	 * 
	 * @param collection - collection to start counting from
	 * @param contentTypeId - id of the content type 
	 * 
	 * @return Items within the specified collection and its sub collection
	 */
	public Long getCount(InstitutionalCollection collection, Long contentTypeId);

	
	/**
	 * Get items submitted into the system ordered by submission date
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
	 * @return count of distinct institutional items for the entire system
	 */
	public Long getDistinctInstitutionalItemCount();
	
	/**
	 * Get a institutional collections  the generic item exists in
	 * 
	 * @param itemId Id of generic Item 
	 * @return the list of collections found
	 */
	public List<InstitutionalCollection> getInstitutionalCollectionsSubmittedForGenericItem(Long itemId);

	
	/**
	 * Set item as private and assign submitted collections user group permissions
	 * 
	 * @param item Item to be assigned private permission
	 * @param collections Collections the item is submitted to
	 */
	public void setItemPrivatePermissions(GenericItem item, List<InstitutionalCollection> collections);

	/**
	 * Get the number of publications contributed by given person names
	 *  
	 * @param personNames Person name of user who contributed
	 * @return Count of publications
	 */
	public Long getPublicationCountByPersonName(Set<PersonName> personNames) ;

	/**
	 * Get institutional items having given generic item id as the LATEST VERSION.
	 * 
	 * @param genericItemId Id of generic item
	 * @return Institutional items found
	 */
	public List<InstitutionalItem> getInstitutionalItemsByGenericItemId(Long genericItemId) ;
		
	/**
	 * Reset all handles in the system.
	 * 
	 * @param batchSize
	 * @param repositoryId
	 */
	public void resetAllHandles(int batchSize, Long repositoryId);	
	
	/**
	 * Mark all institutional items for update that have the most current version pointing to the
	 * generic item id.
	 * 
	 * @param genericItemId - the generic item id
	 * @param index processing type - way the records should be processed
	 */
	public void markAllInstitutionalItemsForIndexing(Long genericItemId, IndexProcessingType processingType);
	
	/**
	 * Get an institutional item by collection id and generic item id.
	 * 
	 * @param collectionId - id of the collection 
	 * @param genericItemId - the generic item id.
	 * 
	 * @return the institutional item
	 */
	public InstitutionalItem getInstitutionalItem(Long collectionId, Long genericItemId);
	
	/**
	 * Add a handle to the specified version.  Will not add a handle if one already
	 * exists.
	 * 
	 * @param itemVersion - item version to add the new handle to
	 */
	public void addHandle(HandleNameAuthority handleNameAuthority, InstitutionalItemVersion itemVersion);
	
	/**
	 * Add a new version to an existing institutional item.
	 * 
	 * @param institutionalItem - item to add a new version to 
	 * @param version - item version to add to the item
	 * @throws RepositoryLicenseNotAcceptedException - thrown if the user has not accepted the repository license
	 */
	public void addNewVersionToItem(IrUser user, InstitutionalItem institutionalItem, ItemVersion version) throws RepositoryLicenseNotAcceptedException;
	
	
	
}
