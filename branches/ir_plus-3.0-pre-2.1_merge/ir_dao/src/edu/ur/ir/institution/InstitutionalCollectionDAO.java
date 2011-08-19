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
import edu.ur.dao.ListAllDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.dao.NonUniqueNameDAO;
import edu.ur.ir.item.GenericItem;

/**
 * Interface for database interaction with institutional Collections.
 * 
 * @author Nathan Sarr
 */
public interface InstitutionalCollectionDAO extends CountableDAO, 
CrudDAO<InstitutionalCollection>, NameListDAO, NonUniqueNameDAO<InstitutionalCollection>, ListAllDAO
{
	/**
	 * Find the root collection for the specified collection name and 
	 * repository id.
	 * 
	 * @param name of the collection
	 * @param repositoryId id of the repository
	 * @return the found collection or null if the collection is not found.
	 */
	public InstitutionalCollection getRootCollection(String name, Long repositoryId);
	
	/**
	 * Find the collection for the specified collection name and 
	 * parent id.
	 * 
	 * @param name of the collection
	 * @param parentId id of the parent collection
	 * @return the found collection or null if the collection is not found.
	 */
	public InstitutionalCollection getCollection(String name, Long parentId);
	
	/**
	 * Get all nodes that have a right and left value grater than the
	 * specified value.  This is for pre-loading all nodes that are to
	 * the right of the specified node.  This can help when re-numbering 
	 * nodes when a new node is added.  This returns nodes only in the
	 * specified root tree. 
	 * 
	 * @param value the left or right value of the node must be grater than this
	 * @param rootId tree to look in
	 * @return the list of nodes found.
	 */
	public List<InstitutionalCollection> getNodesLeftRightGreaterEqual(Long value, Long rootId);

	/**
	 * Get all nodes not within the specified tree.
	 * 
	 * @param irCollection
	 * @return all nodes not within the specified tree.
	 */
	public List<InstitutionalCollection> getAllNodesNotInChildTree(InstitutionalCollection irCollection);
	
	
	/**
	 * Get all collections within the specified repository.
	 * 
	 * @param repositoryId id of the repository
	 * @return all collections within the specified repository.
	 */
	public List<InstitutionalCollection> getAllIrCollectionsInRepository(Long repositoryId);
	
	/**
	 * Get all collections in the repository except for the specified collection and
	 * all its children.
	 * 
	 * @param collection not to include
	 * @return
	 */
	public List<InstitutionalCollection> getAllNodesNotInChildTreeRepo(InstitutionalCollection collection);
	
	
	/**
	 * Gets the path to the collection starting from the top parent all the way
	 * down to the specified child.  Only includes parents of the specified 
	 * collection.  The list is ordered highest level parent and includes the specified 
	 * collection.  This is useful for displaying the path to a given collection.
	 * 
	 * @param collection 
	 * @return list of parent collections.
	 */
	public List<InstitutionalCollection> getPath(InstitutionalCollection collection);
	
	/**
	 * Get personal collections sorting according to the criteria for the 
	 * specified collections.If the parent collection id is null or 0 the set of 
	 * collections at the root of the repository are selected.
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
     * @param repositoryId - the id of the repository
     * @param parentCollectionId - id of the parent collection
	 * @param rowStart - start position in paged set
	 * @param maxNumToFetch - number of records to fetch
	 * @param orderType - order (Asc/desc)
	 * @return List of root collections containing the specified information.
	 */
	public List<InstitutionalCollection> getSubInstituionalCollections(
			final Long repositoryId,
			final Long parentCollectionId,
			final int rowStart, final int maxNumToFetch, final String orderType);

	
	/**
	 * Get root personal collections sorting according to the criteria for the 
	 * specified collections.If the parent collection id is null or 0 the set of 
	 * collections at the root of the repository are selected.
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
     * @param repositoryId - the id of the repository
     * @param parentCollectionId - id of the parent collection
	 * @param rowStart - start position in paged set
	 * @param maxNumToFetch - number of records to fetch
	 * @param orderType - order (Asc/desc)
	 * @return List of root collections containing the specified information.
	 */
	public List<InstitutionalCollection> getRootInstituionalCollections(
			final Long repositoryId,
			final int rowStart, final int maxNumToFetch, final String orderType);


    /**
     * Get a count of personal collections with given criteria for the specified repository.
     * If the parent collection id is null or 0 the set of collections at the root of the 
     * repository are selected.
     *  
     * @param repositoryId - the id of the repository
     * @param parentCollectionId - id of the parent collection
     * @return - the number of personal collections found
     */
    public Long getSubInstitutionalCollectionsCount(Long repositoryId, Long parentCollectionId);

    /**
     * Get a count of personal collections with given criteria for the specified repository.
     * If the parent collection id is null or 0 the set of collections at the root of the 
     * repository are selected.
     *  
     * @param repositoryId - the id of the repository
     * @return - the number of personal collections found
     */
    public Long getRootInstitutionalCollectionsCount(Long repositoryId);

    
	/**
	 * Get the institutional collections for given collection Ids
	 * 
	 * @param collectionIds - List of collection ids
	 * 
	 * @return List of institutional collection
	 */
	public List<InstitutionalCollection> getInstituionalCollections(final List<Long> collectionIds);
	
	
	/**
	 * Returns all children for the parent folders.  This includes all children in the tree.
	 * 
	 * @param parentCollection - parent collection to get children for
	 * @return all children for the specified collection.
	 */
	public List<InstitutionalCollection> getAllChildrenForCollection(InstitutionalCollection parentCollection);

	/**
	 * Returns the count for all children for the parent folders.  This includes all children in the tree.
	 * 
	 * @param parentCollection - parent collection to get children for
	 * @return count of children for the specified collection.
	 */
	public Long getAllChildrenCountForCollection(InstitutionalCollection parentCollection);
	
	/**
	 * Returns list of Ir file ids in the items that belongs to the specified collection
	 * 
	 * @param collectionId - Id of collection to get the ir file ids
	 * @return List of ir file ids
	 */
	public List<Long> getIrFileIdsForCollection(Long collectionId);
	
	/**
	 * Get all generic items used within a collection and all it's children.
	 * 
	 * @param parentCollection - collection to get all generic items from
	 * @return the list of generic items found
	 */
	public List<GenericItem> getAllGenericItemsIncludingChildren(InstitutionalCollection parentCollection);

	/**
	 * Returns list of Ir file ids in the items that belongs to the specified collection and its sub collections
	 * 
	 * @param parentCollection Collection to get the ir file ids 
	 * @return List of ir file ids
	 */
	public List<Long> getIrFileIdsForCollectionAndItsChildren(InstitutionalCollection parentCollection);

	/**
	 * Returns list of Ir file ids in all collections
	 * 
	 * @return List of ir file ids
	 */
	public List<Long> getIrFileIdsForAllCollections(); 
	
	/**
	 * Get the number of file downloads for the collection.  This does
	 * not include the downloads in child collections.
	 * 
	 * @param institutionalCollection - instiitutional collection to get the count for
	 * @return number of downloads not including child collections.
	 */
	public Long getFileDownloads(InstitutionalCollection institutionalCollection);
	
	/**
	 * Get the number of file downloads for the collection.  Include the downloads
	 * for child collections.
	 * 
	 * @param institutionalCollection - instiitutional collection to get the count for
	 * @return the count includeing counts from child collections
	 */
	public Long getFileDownloadsWithChildren(InstitutionalCollection institutionalCollection);
}
