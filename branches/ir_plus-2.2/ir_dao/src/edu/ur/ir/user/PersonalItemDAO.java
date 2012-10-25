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

import edu.ur.dao.CrudDAO;
import edu.ur.ir.item.GenericItem;

/**
 * Interface for accessing and storing personal item information.
 * 
 * @author Nathan Sarr
 *
 */
public interface PersonalItemDAO  extends CrudDAO<PersonalItem>
{

	
    /**
	 * Find the specified items for the given user.
	 * 
	 * @param userId User of the item
	 * @param itemIds Ids of  the item
	 * 
	 * @return List of Items found
	 */
	public List<PersonalItem> getPersonalItems(final Long userId, final List<Long> itemIds);
	
	/**
	 * Get the items for user id and collection id .
	 * 
	 * @param userId
	 * @param collectionId
	 * 
	 * @return the found items or empty list if no items are found
	 */
	public List<PersonalItem> getPersonalItemsInCollectionForUser(Long userId, Long collectionId);
	
	
	/**
	 * Get the root items for the specified user
	 * 
	 * @param userId
	 * 
	 * @return the found files
	 */
	public List<PersonalItem> getRootPersonalItems(Long userId);

	/**
	 * Get personal item which has specified generic item 
	 * 
	 * @param genericItemId
	 * @return
	 */
	public PersonalItem getPersonalItem(Long genericItemId);
	
	/**
	 * Get all personal items which have the specified generic item ids 
	 * 
	 * @param itemIds - list of generic item ids
	 * @return - all personal items that contain the generic item id.
	 */
	public List<PersonalItem> getAllPersonalItemsByGenericItemIds(List<Long> itemIds);
}
