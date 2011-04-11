package edu.ur.ir.user;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.FileSystem;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecord;

/**
 * Data access for user workspace index processing record.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserWorkspaceIndexProcessingRecordDAO extends CrudDAO<UserWorkspaceIndexProcessingRecord>, CountableDAO{

	/**
	 * Get all institutional item indexing processing record ordered by item id and updated date.
	 * 
	 * @return all institutional item index processing records or an empty list if none found
	 */
	public List<UserWorkspaceIndexProcessingRecord> getAllOrderByUserIdDate();
	
	/**
	 * Get the processing record by file system, user id and processing type
	 * 
	 * @param fileSystem - file system object
	 * @param userId - id of the user who owns the object
	 * @param processingType - processing type.
	 * 
	 * @return - the processing record if found otherwise null
	 */
	public UserWorkspaceIndexProcessingRecord get(FileSystem fileSystem, Long userId, IndexProcessingType processingType);

	

	
	
}
