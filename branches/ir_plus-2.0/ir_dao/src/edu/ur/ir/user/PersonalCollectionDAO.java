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

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;


/**
 * Interface for dealing with personal collections.
 * 
 * @author Nathan Sarr
 *
 */
public interface PersonalCollectionDAO extends CountableDAO, CrudDAO<PersonalCollection>
{
	/**
	 * Find the root personal collection for the specified folder name and 
	 * user id.
	 * 
	 * @param name of the personal collection
	 * @param userId id of the user
	 * @return the found root personal collection or null if the personal collection
	 *  is not found.
	 */
	public PersonalCollection getRootPersonalCollection(String name, Long userId);
	
	/**
	 * Find the personal collection for the specified folder name and 
	 * parent folder id.
	 * 
	 * @param name of the personal collection
	 * @param parentId id of the parent personal collection
	 * @return the personal collection or null if the personal collection is not found.
	 */
	public PersonalCollection getPersonalCollection(String name, Long parentId);
	
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
	public List<PersonalCollection> getNodesLeftRightGreaterEqual(Long value, Long rootId);

	/**
	 * Get all nodes not within the specified tree.
	 * 
	 * @param personalCollection
	 * @return all nodes not within the specified tree.
	 */
	public List<PersonalCollection> getAllNodesNotInChildTree(PersonalCollection personalCollection);
	
	
	/**
	 * Get all personal collections for the specified user.
	 * 
	 * @param userId id - id of the user
	 * @return all personal collections for the specified user.
	 */
	public List<PersonalCollection> getAllPersonalCollectionsForUser(Long userId);
	
	/**
	 * This returns all personal items for the specified parent folder.  
	 * 
	 * @param personalCollection
	 * @return
	 */
	public List<PersonalItem> getAllItemsForCollection(PersonalCollection personalCollection);
	
	
	/**
	 * Gets the path to the personal collection starting from the top parent all the way
	 * down to the specified child.  Only includes parents of the specified 
	 * personal collection.  The list is ordered highest level parent and includes the specified 
	 * personal collection.  This is useful for displaying the path to a given child personal collection.
	 * 
	 * @param personal collection
	 * @return list of parent personal collections.
	 */
	public List<PersonalCollection> getPath(PersonalCollection personalCollection);
	
	/**
	 * Get personal collections sorting according to the sort and filter information for the 
	 * specified user - this only returns root personal collection.
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
     * @param userId - the id of the user who owns the personal collections
	 * @param rowStart - start position in paged set
	 * @param numtoFetch - maximum number of items to fetch
	 * @return List of root collections containing the specified information.
	 */
	public List<PersonalCollection> getRootPersonalCollections( Long userId, int rowStart, int maxNumberToFetch);


   
	/**
	 * Get the list of sub personal collections for the specified parent personal collection.
	 * 
	 * 
	 * @param userId - user who should have the personal collection
	 * @param parentPersonalCollectionId - parent personal collection id for the user
	 * @param rowStart - start position
	 * @param rowEnd - number of rows to grab.
	 * 
	 * @return list of personal folders found.
	 */
	public List<PersonalCollection> getSubPersonalCollections( final Long userId, final Long parentPersonalCollectionId);
	
	

	/**
	 * Get all nodes not within the specified trees.  This helps with selecting
	 * collections users can move other collections to.  This only works on a single root
	 * collection at a time and assumes the list of personal collections all belon to the same 
	 * root collection.
	 * 
	 * @param personalCollections - list of collections that collections cannot be in
	 * @param userId - owner of the collections
	 * @param rootCollectionId - the id of the root collection 
	 * 
	 * 
	 * @return all nodes not within the specified collection trees - including themselves.
	 */
	public List<PersonalCollection> getAllCollectionsNotInChildCollections(List<PersonalCollection> collections, 
			Long userId, Long rootCollectionId);

	/**
	 * Get all other root collections for the user except the ones with the specified id's
	 * 
	 * @param rootCollectionIds - root collections
	 * @param userId - id of the user
	 * 
	 * @return list of root collections.
	 */
	public List<PersonalCollection> getAllOtherRootCollections(final List<Long> rootCollectionIds, final Long userId);

	/**
	 * Find the specified collections.
	 * 
	 * @param userId owner of the collection
	 * @param collectionIds List of collections ids
	 * 
	 * @return List of personal collections found
	 * 
	 */
	public List<PersonalCollection> getPersonalCollections(final Long userId, final List<Long> collectionIds);
	
	/**
	 * Get sub collections for the parent collection
	 * 
	 * @param userId Id of the user having the collection
	 * @param parentFolderId Id of the parent collection
	 * 
	 * @return List of sub collections for parent collection of user
	 */
	public List<PersonalCollection> getPersonalSubCollectionsForCollection(Long userId, Long parentCollectionId);
	
	/**
	 * Get root collections for user
	 * 
	 * @param userId User holding the collections
	 * 
	 * @return List of root collections
	 */
	public List<PersonalCollection> getRootPersonalCollections(Long userId);
}
