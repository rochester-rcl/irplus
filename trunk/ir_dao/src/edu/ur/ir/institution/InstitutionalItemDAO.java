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

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;


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
	 * Get a count of all items by a collection id.
	 * 
	 * @param collectionId - id of the collection
	 * @return
	 */
	public Long getCount(InstitutionalCollection collection);
	
	
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
			final String orderType);
	
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
			final String orderType);
	
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
			final String orderType);
	
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
			String orderType);
	
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
			String orderType);
	
	/**
	 * Get a list of items for a specified collection by name.
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
			String orderType);

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
}
