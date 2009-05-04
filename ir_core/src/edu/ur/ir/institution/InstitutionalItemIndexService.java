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

import java.io.File;
import java.util.List;

import edu.ur.ir.NoIndexFoundException;

/**
 * Service class for indexing institutional items.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalItemIndexService {
	
	
	/**
	 * Batch size for processing collections
	 */
	public int getCollectionBatchSize();
	
	/**
	 * Add a set of items to the index - this is generally used for batch processing of multiple institutional items.
	 * This can also be used to re-index a set the existing set of items.
	 * 
	 * @param items - set of items to add
	 * @param institutionalItemIndex - index to add it to
	 * @param overwriteExistingIndex - indicates this should overwrite the current index
	 */
	public void addItems(List<InstitutionalItem> items, File institutionalItemIndex, boolean overwriteExistingIndex);
	
	/**
	 * Index the specified institutional item.
	 * 
	 * @param institutionalItem
	 */
	public void addItem(InstitutionalItem institutionalItem, File insitutionalItemIndex) throws NoIndexFoundException;
	
	/**
	 * Update the specified institutional item in the index.
	 * 
	 * @param institutionalItem
	 */
	public void updateItem(InstitutionalItem institutionalItem, File insitutionalItemIndex) throws NoIndexFoundException;
	
	/**
	 * Delete the institutional Item from the index.
	 * 
	 * @param institutionalItem
	 */
	public void deleteItem(InstitutionalItem institutionalItem, File insitutionalItemIndex);
	
	/**
	 * Delete all items for the specified institutional collection.
	 * 
	 * @param institutionalCollection
	 * @param institutionalItemIndex
	 */
	public void deleteItemsForCollection(InstitutionalCollection institutionalCollection, File institutionalItemIndex);


	/**
	 * Re-Index all items in a given collection, this re-indexes all items in sub collections.
	 * 
	 * @param institutionalCollection
	 * @param institutionalItemIndex
	 */
	public void reIndexItemsInCollection(InstitutionalCollection institutionalCollection, File institutionalItemIndex, int batchSize) throws NoIndexFoundException;
}
