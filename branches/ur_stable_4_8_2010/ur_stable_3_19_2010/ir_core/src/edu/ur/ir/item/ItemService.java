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

package edu.ur.ir.item;

import java.util.List;
import java.util.Set;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.user.IrUser;

public interface ItemService {
	
	/**
	 * Delete the versioned item from persisten storage.
	 * 
	 * @param versionedItem - versioned item to remove
	 */
	public void deleteVersionedItem(VersionedItem versionedItem);

	
	/**
	 * Get a versioned item by id.
	 * 
	 * @param id - id to get the versioned item for.
	 * @param lock - upgrade the lock on the versioned item.
	 * 
	 * @return - the versioned item if found otherwise null.
	 */
	public VersionedItem getVersionedItem(Long id, boolean lock);
	
	/**
	 * Save the item file
	 * 
	 * @param itemFile - item file to be saved
	 */
	public void saveItemFile(ItemFile itemFile);
	
	/**
	 * Delete item file
	 * 
	 * @param itemFile - item file to be deleted
	 */
	public void deleteItemFile(ItemFile itemFile);
	
	/**
	 * Get the count of items using this file
	 * 
	 * @param irFile irFile used by item
	 */
	public Long getItemFileCount(IrFile irFile);
	
	/**
	 * Deletes the IrFiles that are not used by any item and PersonalFiles
	 * 
	 * @param irFileIds Set of ir file ids to be deleted
	 * 
	 */
	public void deleteUnUsedIrFiles(Set<Long> irFileIds);
	
	/**
	 * Delete the item.
	 * 
	 * @param item  Item to be deleted
	 * 
	 */
	public void deleteItem(GenericItem item);
	
	/**
	 * Save item
	 * 
	 * @param item
	 */
	public void makePersistent(GenericItem item);
	
	/**
	 * Get the item with the specified id.
	 * 
	 * @param id - id to get the generic item for.
	 * @param lock - upgrade the lock on the generic item.
	 * 
	 * @return - the generic item if found otherwise null.
	 */
	public GenericItem getGenericItem(Long id, boolean lock);

	/**
	 * Get the item file with the specified id.
	 * 
	 * @param id - id to get the item file for.
	 * @param lock - upgrade the lock on the item file.
	 * 
	 * @return - the item file if found otherwise null.
	 */
	public ItemFile getItemFile(Long id, boolean lock);

	/**
	 * Get the list of items owned by an user
	 * 	
	 * @param user - User owning the items 
	 * @return list of Items
	 */
	public List<GenericItem> getAllItemsForUser(IrUser user);
	
	/**
	 * Get all versioned items for a user.
	 * 
	 * @param user - user who owns the versioned items
	 * @return - the versioned items owned by this user.
	 */
	public List<VersionedItem> getAllVersionedItemsForUser(IrUser user);
	
	/**
	 * Number of items using the given person name as contributor
	 * 
	 * @param contributor used by items
	 * @return Number of items
	 */
	public Long getItemCountByContributor(Contributor contributor); 
	
	/**
	 * Number of items having the person as contributor for the specified name
	 * 
	 * @see edu.ur.ir.item.ItemService#getItemCountByContributor(PersonName)
	 */
	public Long getItemCountByPersonName(PersonName personName);


	/**
	 * Delete external publiched item dataa
	 * 
	 * @param externalPublishedItem data to be deleted
	 */
	public void deleteExternalPublishedItem(ExternalPublishedItem externalPublishedItem);
	
	/**
	 * Get the count of item version using this generic item
	 * 
	 * @param item GenericItem to be searched for in item version
	 * 
	 * @return Count of Item version containing the given generic Item
	 */
	public Long getItemVersionCount(GenericItem item);

	/**
	 * Get the item version with the specified id.
	 * 
	 * @param id - id to get the item version for.
	 * @param lock - upgrade the lock on the item version.
	 * 
	 * @return - the item version if found otherwise null.
	 */
	public ItemVersion getItemVersion(Long id, boolean lock);
}
