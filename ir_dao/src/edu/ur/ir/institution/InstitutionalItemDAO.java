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

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.item.ContentTypeCount;
import edu.ur.order.OrderType;


/**
 * Data access for institutional items.
 * 
 * @author Nathan Sarr.
 *
 */
public interface InstitutionalItemDAO extends CrudDAO<InstitutionalItem>, CountableDAO 
{
	
    /**
	 * Find if the item version is already published to this collection.
	 * 
	 * @param  institutionalCollectionId Id of the institutional collection
	 * @param itemVersionId Id of the item version
	 * 
	 * @return True if item is published to the collection else false
	 */
	public boolean isItemPublishedToCollection(Long institutionalCollectionId, Long itemVersionId);  
	
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
	 * Get a list of items that have the specified ids.
	 * 
	 * @param itemIds - ids of the items
	 * 
	 * @return List of items found
	 */
	public List<InstitutionalItem> getInstitutionalItems(final List<Long> itemIds);
	
	/**
	 * Get a count of all items by a repository id.
	 * 
	 * @param repositoryId - id of the repository 
	 * @return
	 */
	public Long getCount(Long repositoryId);
	
	/**
	 * Get a  count of institutional items with the given content type id.
	 * 
	 * @param repositoryId - id of the repository
	 * @param contentTypeId - content type
	 * 
	 * @return the count of institutional items with the content type in the specified
	 * repository.
	 */
	public Long getCount(Long repositoryId, Long contentTypeId);
	
	/**
	 * Get a count of all items by a collection id.
	 * 
	 * @param collectionId - id of the collection
	 * @return
	 */
	public Long getCount(InstitutionalCollection collection);
	
	/**
	 * Get a count of institutional items within a collection 
	 * @param collection
	 * @param contentTypeId
	 * @return
	 */
	public Long getCount(InstitutionalCollection collection, Long contentTypeId);
	
	/**
	 * Get a count of institutional items with the specified repository id
	 * has a name starting with the first character and the specified content type id.
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstChar - name of the first character
	 * @param contentTypeId - specified content type id
	 * 
	 * @return the count of items with the specified criteria
	 */
	public Long getCount(Long repositoryId, char nameFirstChar,
			Long contentTypeId);
	
	/**
	 * Get a count of all items within the specified repository with a name
	 * first character starting between the given character range and the content type id.
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstCharRange - starting character range
	 * @param namelastCharRange - ending character range
	 * @param contentTypeId - id of the content type
	 * 
	 * @return the count of items 
	 */
	public Long getCount(Long repositoryId, char nameFirstCharRange,
			char namelastCharRange, Long contentTypeId);
	
	/**
	 * Get a count of all items within the specified collection, has the specified first character 
	 * and a given content type id.  This includes a count of items within sub collections.
	 * 
	 * @param collection - collection items must be within sub collections
	 * @param nameFirstChar - name starts with the specified first character
	 * @param contentTypeId - id of the content type
	 * 
	 * @return count of items 
	 */
	public Long getCount(InstitutionalCollection collection,
			char nameFirstChar, Long contentTypeId);
	
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
	public List<InstitutionalItem> getRepositoryItemsByChar(final int rowStart,
			final int maxResults, 
			final Long repositoryId,
			final char firstChar,
			final OrderType orderType);
	
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
	public List<InstitutionalItem> getRepositoryItemsByCharPublicationDateOrder(final int rowStart,
			final int maxResults, 
			final Long repositoryId,
			final char firstChar,
			final OrderType orderType);
	
	
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
	public List<InstitutionalItem> getRepositoryItemsByCharFirstAvailableOrder(final int rowStart,
			final int maxResults, 
			final Long repositoryId,
			final char firstChar,
			final OrderType orderType);
	
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
			int maxResults, Long repositoryId, Long contentTypeId,
			char firstChar, OrderType orderType);
	
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
			int maxResults, Long repositoryId, Long contentTypeId,
			char firstChar, OrderType orderType);
	
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
			final int maxResults, 
			final Long repositoryId,
			final char firstChar,
			final char lastChar,
			final OrderType orderType);
	
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
	public List<InstitutionalItem> getRepositoryItemsBetweenCharPublicationDateOrder(final int rowStart,
			final int maxResults, 
			final Long repositoryId,
			final char firstChar,
			final char lastChar,
			final OrderType orderType);
	
	
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
	public List<InstitutionalItem> getRepositoryItemsBetweenCharFirstAvailableOrder(final int rowStart,
			final int maxResults, 
			final Long repositoryId,
			final char firstChar,
			final char lastChar,
			final OrderType orderType);
	
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
	public List<InstitutionalItem> getRepositoryItemsBetweenChar(int rowStart,
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
	public List<InstitutionalItem> getRepositoryItemsBetweenCharPublicationDateOrder(int rowStart,
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
	public List<InstitutionalItem> getRepositoryItemsBetweenCharFirstAvailableOrder(int rowStart,
			int maxResults, 
			Long repositoryId, 
			char firstChar, 
			char lastChar,
			Long contentTypeId, 
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository by name.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsByName(final int rowStart,
			final int maxResults, 
			final Long repositoryId, 
			final OrderType orderType);
	
	
	/**
	 * Get a list of items for a specified repository with the given content type id.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param propertyName - The property to sort on
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsOrderByName(int rowStart,
			int maxResults, 
			Long repositoryId, 
			Long contentTypeId,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository with the given content type id.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param propertyName - The property to sort on
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsPublicationDateOrder(int rowStart,
			int maxResults, 
			Long repositoryId, 
			Long contentTypeId,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository with the given content type id.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param propertyName - The property to sort on
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsFirstAvailableOrder(int rowStart,
			int maxResults, 
			Long repositoryId, 
			Long contentTypeId,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param propertyName - The property to sort on
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsPublicationDateOrder(int rowStart,
			int maxResults, 
			Long repositoryId, 
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param propertyName - The property to sort on
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsFirstAvailableOrder(int rowStart,
			int maxResults, 
			Long repositoryId, 
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
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
	 * Get a list of items for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
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
	 * Get a list of items for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
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
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
	 * @param contentTypeId - id of the content type 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsByChar(int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId, 
			char firstChar, 
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
	 * @param contentTypeId - id of the content type 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsByCharPublicationDateOrder(int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId, 
			char firstChar, 
			OrderType orderType);
	
	
	/**
	 * Get a list of items for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
	 * @param contentTypeId - id of the content type 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsByCharFirstAvailableOrder(int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId, 
			char firstChar, 
			OrderType orderType);
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenChar(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection, 
			char firstChar,
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenCharPublicationDateOrder(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection, 
			char firstChar,
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenCharFirstAvailableOrder(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection, 
			char firstChar,
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param contentTypeId - content type id the items must have
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return list of items matching the specified criteria
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenChar(int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId, 
			char firstChar, 
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param contentTypeId - content type id the items must have
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return list of items matching the specified criteria
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenCharPublicationDateOrder(int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId, 
			char firstChar, 
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param contentTypeId - content type id the items must have
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return list of items matching the specified criteria
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenCharFirstAvailableOrder(int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId, 
			char firstChar, 
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified collection ordered by name.
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collectionId - id of the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsByName(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection, 
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified collection ordered by name.
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collectionId - id of the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsPublicationDateOrder(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection, 
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified collection ordered by name.
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collectionId - id of the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getCollectionItemsFirstAvailableOrder(final int rowStart,
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
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCollectionItemsByName(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, edu.ur.order.OrderType)
	 */
	public List<InstitutionalItem> getCollectionItemsByName(int rowStart,
			int maxResults, 
			InstitutionalCollection collection,
			Long contentTypeId, 
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
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCollectionItemsByName(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, edu.ur.order.OrderType)
	 */
	public List<InstitutionalItem> getCollectionItemsPublicationDateOrder(int rowStart,
			int maxResults, InstitutionalCollection collection,
			Long contentTypeId, OrderType orderType);
	
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
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCollectionItemsByName(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, edu.ur.order.OrderType)
	 */
	public List<InstitutionalItem> getCollectionItemsFirstAvailableOrder(int rowStart,
			int maxResults, InstitutionalCollection collection,
			Long contentTypeId, OrderType orderType);
	
	/**
	 * Get a list of item ids for a specified collection ordered by id.
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collectionId - id of the collection to get item ids
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<Long> getCollectionItemsIds(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection, 
			OrderType orderType);

	/**
	 * Get a count of institutional items in a collection and its children.
	 * 
	 * @param collectionId - id of the collection
	 * @return Items within the specified collection and its sub collection
	 */
	public Long getCountForCollectionAndChildren(InstitutionalCollection collection);
	

	/**
	 * Get a count of institutional items using the generic item using it's id
	 * 
	 * @param genericItemId - id of the generic item to check for
	 * @return number of institutional items using the generic item.
	 */
	public Long getCountByGenericItem(Long genericItemId);
	
	/**
	 * Get a count of institutional items in the repository with a name
	 * that starts with the specified first character.
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstChar - first character of the name
	 * 
	 * @return the count found
	 */
	public Long getCount(Long repositoryId, char nameFirstChar);
	
	/**
	 * Get a count of institutional items in the repository with a name
	 * that starts with the specified first character in the given range.
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstCharRange - first character of the name start of range
	 * @param nameLastCharRange- first character of the name end of range
	 * 
	 * @return the count found
	 */
	public Long getCount(Long repositoryId, char nameFirstCharRange, char namelastCharRange);
	
	/**
	 * Get a count of institutional items in the collection with a name
	 * that starts with the specified first character.
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param collection -  institutional collection to look in
	 * @param nameFirstChar - first character of the name
	 * 
	 * @return the count found
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstChar);
	
	/**
	 * Get a count of institutional items in the collection with a name
	 * that starts with the specified first character in the given range.  
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param collection - institutional collection to look in
	 * @param nameFirstCharRange - first character of the name start of range
	 * @param nameLastCharRange- first character of the name end of range
	 * 
	 * @return the count found
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstCharRange, char namelastCharRange);

	/**
	 * Get a count of distinct institutional items in the repository.
	 * 
	 * @return
	 */
	public Long getDistinctInstitutionalItemCount();
	
	/**
	 * Get a institutional collections  the generic item exists in
	 * 
	 * @return the list of collections found
	 */
	public List<InstitutionalCollection> getInstitutionalCollectionsForGenericItem(Long itemId);
	
	/**
	 * Get the publication count  for given name id.
	 * 
	 * @param personNameIds Id of person names who contributed to this item
	 */
	public Long getPublicationCountByPersonName(List<Long> personNameIds) ;

	/**
	 * Get Institutional item by given version id
	 * 
	 * @param institutionalVerisonId Id of version
	 * @return Institutional item found
	 */
	public InstitutionalItem getInstitutionalItemByVersionId(Long institutionalVerisonId);

	/**
	 * Get a institutional items where the generic item is the latest version
	 *
	 * @param itemId Id of generic item
	 * 
	 * @return List of institutional items found
	 */
	public List<InstitutionalItem> getInstitutionalItemsForGenericItemId(Long itemId);
	
	/**
	 * Get subset of all item published ordered by date.
	 *
	 * @param rowStart - row to start
	 * @param maxResults - max number of results to fetch
	 * @param collection - collection to get the items for
	 * @param orderType - way to order ascending or descending

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
	 * Get a list of of repository content types with counts for the number
	 * of items within the repository.  This will return only those
	 * items that have a count greater than 0.
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
}
