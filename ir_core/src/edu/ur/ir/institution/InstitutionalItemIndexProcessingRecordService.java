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
import java.util.List;

import edu.ur.ir.index.IndexProcessingType;

/**
 * 
 * Service for dealing with institutional item index processing records.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalItemIndexProcessingRecordService extends Serializable{
	
	/**
	 * Get all institutional item index processing records ordered by item id then processing date.
	 * 
	 * @return
	 */
	public List<InstitutionalItemIndexProcessingRecord> getAllOrderByItemIdUpdatedDate();
	
	/**
     * Get a count of institutional item index processing records
     *  
     * @return - the number of institutional item index processing records found
     */
    public Long getCount();
    
    /**
     * Delete the institutional item index processing records
     * 
     * @param  indexProcessingType
     */
    public void delete(InstitutionalItemIndexProcessingRecord institutionalItemIndexProcessingRecord);    
    
    /**
     * Get an institutional item index processing record by id
     * 
     * @param id - unique id of the institutional item index processing record.
     * @param lock - upgrade the lock on the data
     * @return - the found institutional item index processing record or null if the institutional item index processing record is not found.
     */
    public InstitutionalItemIndexProcessingRecord get(Long id, boolean lock);
    
    /**
     * Save the institutional item index processing record
     * 
     * @param institutionalItemIndexProcessingRecord
     */
    public void save(InstitutionalItemIndexProcessingRecord institutionalItemIndexProcessingRecord);
 
	/**
	 * Get all institutional item index processing records
	 * 
	 * @return List of all index processing types
	 */
	public List<InstitutionalItemIndexProcessingRecord> getAll();
	
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
     * Determines if there is already an existing processing record for the item id, processing type and
     * creates a new one if one does not exist otherwise does an update.
     * 
     * @param itemId - id of the item
     * @param processingType - type of processing
     * @return the record that was updated or created.
     */
    public InstitutionalItemIndexProcessingRecord save(Long itemId, IndexProcessingType processingType);
    
    /**
     * Add all items within the given collection including child collections to the index.
     * 
     * @param institutionalCollection
     * @param processingType
     */
    public void processItemsInCollection(
			InstitutionalCollection institutionalCollection,
			IndexProcessingType processingType);
    
    /**
     * Add all items within the repository to be processed.
     * 
     * @param processingType
     */
    public void processItemsInRepository(IndexProcessingType processingType);
}
