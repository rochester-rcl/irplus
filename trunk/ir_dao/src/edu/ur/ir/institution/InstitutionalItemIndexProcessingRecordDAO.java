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
import edu.ur.ir.index.IndexProcessingType;

/**
 * Data access for institutional item index processing record.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalItemIndexProcessingRecordDAO 
    extends CrudDAO<InstitutionalItemIndexProcessingRecord>, CountableDAO{
	
	/**
	 * Get all institutional item indexing processing record ordered by item id and updated date.
	 * 
	 * @return all institutional item index processing records or an empty list if none found
	 */
	public List<InstitutionalItemIndexProcessingRecord> getAllOrderByItemIdUpdatedDate();
	
	/**
	 * Get the processing record by item id and processing type.
	 * 
	 * @param itemId - id of the item
	 * @param processingType - processing type.
	 * 
	 * @return - the index processing record if found otherwise null
	 */
	public InstitutionalItemIndexProcessingRecord get(Long itemId, IndexProcessingType processingType);

	
	/**
	 * Insert all items within the specified collection to be processed.
	 * 
	 * @param - institutional collection
	 * @param processingType - processing type.
	 * 
	 * @return - number of records created.
	 */
	public Long insertAllItemsForCollection(InstitutionalCollection institutionalCollection, IndexProcessingType processingType);
	
	/**
	 * Insert all items for the repository
	 * 
	 * @param processingType - processing type.
	 * 
	 * @return - number of records created for processing
	 */
	public Long insertAllItemsForRepository(IndexProcessingType processingType);
	
	/**
	 * Set all institutional items to be re-indexed based on the content type id.  This
	 * should include items that use the content type as a secondary content type.
	 * 
	 * @param contentTypeId - id for the content type
	 * @param processingType - type of processing to complete
	 * @return number of records created for processing
	 */
	public Long insertAllItemsForContentType(Long contentTypeId, IndexProcessingType processingType);
}
