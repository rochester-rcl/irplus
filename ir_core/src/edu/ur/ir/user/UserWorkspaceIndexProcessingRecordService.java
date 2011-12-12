package edu.ur.ir.user;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.ur.ir.FileSystem;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.index.IndexProcessingType;


/**
 * 
 * Service for dealing with user workspace index processing records.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserWorkspaceIndexProcessingRecordService extends Serializable{
	
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
     * creates a new one if one does not exist otherwise does an update.  This can insert multiple records.
     * If the file system type is shared across multiple users, it will perform actions that mantain all
     * users.
     * 
	 * @param userId - id of the user who owns the item
	 * @param fileSystem - file system object of the workspace 
	 * @param processingType - processing type 
     */
    public UserWorkspaceIndexProcessingRecord save(Long userId, 
			FileSystem fileSystem, 
			IndexProcessingType processingType);
    
	/**
	 * Update all indexes for a personal file - since a personal file can be shared across multiple users, we may
	 * want to update all users.  This method provides this option.
	 * 
	 * @param personalFile - personal file to update
	 * @param processingType - type of processing
	 * 
	 * @return list of records updated.
	 */
	public List<UserWorkspaceIndexProcessingRecord> saveAll(PersonalFile personalFile, 
			IndexProcessingType processingType);
	
	/**
	 * Update all indexes for a group workspace file - since a workspace file can be shared across multiple users, we may
	 * want to update all users.  This method provides this option.
	 * 
	 * @param workspaceFile - workspace file to update
	 * @param processingType - type of processing
	 * 
	 * @return list of records updated.
	 */
	public List<UserWorkspaceIndexProcessingRecord> saveAll(GroupWorkspaceFile File, 
			IndexProcessingType processingType);
	
    /**
     * Add all file system objects available to the given user to be processed
     * 
     * @param user - user with all items to be processed
     * @param processing type - type of processing to be performed.
     * @throws IOException 
     */
    public void reIndexAllUserItems( IrUser user,
			IndexProcessingType processingType) throws IOException;
    
    /**
     * Will delete and set all user workspaces to be re-indexed
     * 
     * @param processing type - type of processing to be performed.
     * @throws IOException 
     */
    public void reIndexAllWorkspaceUsers(IndexProcessingType processingType) throws IOException;

}
