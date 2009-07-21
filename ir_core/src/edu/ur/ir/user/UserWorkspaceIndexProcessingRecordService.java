package edu.ur.ir.user;

import java.util.List;

import edu.ur.ir.FileSystem;
import edu.ur.ir.index.IndexProcessingType;


/**
 * 
 * Service for dealing with user workspace index processing records.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserWorkspaceIndexProcessingRecordService {
	
	/**
	 * Get all user workspace index processing records ordered by user id then processing date.
	 * 
	 * @return
	 */
	public List<UserWorkspaceIndexProcessingRecord> getAllOrderByIdDate();
	
	/**
     * Get a count of workspace index processing records 
     *  
     * @return - the number of workspace index processing records  found
     */
    public Long getCount();
    
    /**
     * Delete the workspace index processing records 
     * 
     * @param  indexProcessingType
     */
    public void delete(UserWorkspaceIndexProcessingRecord userWorkspaceIndexProcessingRecord);    
    
    /**
     * Get a workspace index processing records by id
     * 
     * @param id - unique id of the workspace index processing record.
     * @param lock - upgrade the lock on the data
     * @return - the found workspace index processing record or null if the institutional item index processing record is not found.
     */
    public UserWorkspaceIndexProcessingRecord get(Long id, boolean lock);
    
    /**
     * Save the workspace index processing record
     * 
     * @param userWorkspaceIndexProcessingRecord
     */
    public void save(UserWorkspaceIndexProcessingRecord userWorkspaceIndexProcessingRecord);
 
	/**
	 * Get all workspace index processing records 
	 * 
	 * @return List of all index processing types
	 */
	public List<UserWorkspaceIndexProcessingRecord> getAll();
	
	/**
	 * Get the processing record by the workspace item id, user id and file system type.
	 * 
	 * @param userId - id of the user who owns the item
	 * @param fileSystem - file system object of the workspace 
	 * @param processingType - processing type 
	 * 
	 * @return the found user workspace index processing record or null.
	 */
	public UserWorkspaceIndexProcessingRecord get(Long userId, 
			FileSystem fileSystem, 
			IndexProcessingType processingType);
	

    /**
     * Determines if there is already an existing processing record for the record, processing type and
     * creates a new one if one does not exist otherwise does an update.
     * 
	 * @param userId - id of the user who owns the item
	 * @param fileSystem - file system object of the workspace 
	 * @param processingType - processing type 
     */
    public UserWorkspaceIndexProcessingRecord save(Long userId, 
			FileSystem fileSystem, 
			IndexProcessingType processingType);
    
    /**
     * Add all items within the given user to be processed
     * 
     * @param user - user with all items to be processed
     * @param processing type - type of processing to be performed.
     */
    public void allUserItems( IrUser user,
			IndexProcessingType processingType);
    
  

}
