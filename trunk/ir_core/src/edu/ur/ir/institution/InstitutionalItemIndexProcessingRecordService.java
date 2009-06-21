package edu.ur.ir.institution;

import java.util.List;

/**
 * 
 * Service for dealing with institutional item index processing records.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalItemIndexProcessingRecordService {
	
	
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

}
