package edu.ur.ir.user.service;

import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.FileSystem;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecord;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordDAO;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;

/**
 * Implementation of the user workspace index processing record service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserWorkspaceIndexProcessingRecordService implements 
UserWorkspaceIndexProcessingRecordService
{
	/** worspace data access object */
	private UserWorkspaceIndexProcessingRecordDAO userWorkspaceIndexProcessingRecordDAO;
	
	/** File system service for users. */
	private UserFileSystemService userFileSystemService;
	
    /** user publishing file system service */
    private UserPublishingFileSystemService userPublishingFileSystemService;
    
	/** Service for dealing with processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserWorkspaceIndexProcessingRecordService.class);



	public void allUserItems(IrUser user, IndexProcessingType processingType) {
		// TODO Auto-generated method stub
	}

	/**
	 * Delete the workspace entity.
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService#delete(edu.ur.ir.user.UserWorkspaceIndexProcessingRecord)
	 */
	public void delete( UserWorkspaceIndexProcessingRecord entity) {
		userWorkspaceIndexProcessingRecordDAO.makeTransient(entity);
	}

	/**
	 * Get the processing record by id.
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService#get(java.lang.Long, boolean)
	 */
	public UserWorkspaceIndexProcessingRecord get(Long id, boolean lock) {
		return userWorkspaceIndexProcessingRecordDAO.getById(id, lock);
	}

	public UserWorkspaceIndexProcessingRecord get(Long userId,
			FileSystem fileSystem, IndexProcessingType processingType) {
		return userWorkspaceIndexProcessingRecordDAO.get(fileSystem, userId, processingType);
	}

	@SuppressWarnings("unchecked")
	public List<UserWorkspaceIndexProcessingRecord> getAll() {
		return userWorkspaceIndexProcessingRecordDAO.getAll();
	}

	public List<UserWorkspaceIndexProcessingRecord> getAllOrderByIdDate() {
		return userWorkspaceIndexProcessingRecordDAO.getAllOrderByUserIdDate();
	}

	public Long getCount() {
		return userWorkspaceIndexProcessingRecordDAO.getCount();
	}

	public void save(UserWorkspaceIndexProcessingRecord entity) {
		userWorkspaceIndexProcessingRecordDAO.makePersistent(entity);
	}

	public UserWorkspaceIndexProcessingRecord save(Long userId,
			FileSystem fileSystem, IndexProcessingType processingType) {
		
		IndexProcessingType updateProcessingType = indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE);
		IndexProcessingType deleteProcessingType = indexProcessingTypeService.get(IndexProcessingTypeService.DELETE);
		IndexProcessingType insertProcessingType = indexProcessingTypeService.get(IndexProcessingTypeService.INSERT);

		
		UserWorkspaceIndexProcessingRecord record = userWorkspaceIndexProcessingRecordDAO.get(fileSystem, userId, processingType);
		if( record != null)
		{
		    // do nothing record is already set to be processed
		}
		else
		{
			if(processingType.equals(updateProcessingType))
			{
				// only update if there is no insert or delete processing type records
				if( get(userId, fileSystem, insertProcessingType) == null &&  get(userId, fileSystem, deleteProcessingType) == null)
				{
					record = new UserWorkspaceIndexProcessingRecord (fileSystem, processingType, userId);
				    save(record);
				}
				else
				{
					// do nothing already set to be inserted
				}

			}
			else if(processingType.equals(deleteProcessingType))
			{
				// deleting item - so all other processing types do not need to occur
				UserWorkspaceIndexProcessingRecord updateProcessing = get(userId, fileSystem, updateProcessingType);
				if( updateProcessing != null )
				{
					delete(updateProcessing);
				}
				
				UserWorkspaceIndexProcessingRecord insertProcessing = get(userId, fileSystem, insertProcessingType);
				if( insertProcessing != null )
				{
					delete(insertProcessing);
				}
				record = new UserWorkspaceIndexProcessingRecord(fileSystem, processingType, userId);
				save(record);

			}
			else if(processingType.equals(insertProcessingType))
			{
				// there should never be an update, delete record existing for an insert
				if( get(userId, fileSystem, updateProcessingType) == null &&  get(userId, fileSystem, deleteProcessingType) == null )
				{
					record = new UserWorkspaceIndexProcessingRecord(fileSystem, processingType, userId);
				    save(record);
				}
				else
				{
					throw new IllegalStateException(" Insert entered with existing processing type record already existing user id = " 
							+  userId + " fileSystem = " + fileSystem + " processingType = " + processingType );
				}
			}
			else
			{
				log.error("Could not identify processing type " + processingType);
			}

		}

		
		
		return record;
	}
	
	public UserWorkspaceIndexProcessingRecordDAO getUserWorkspaceIndexProcessingRecordDAO() {
		return userWorkspaceIndexProcessingRecordDAO;
	}

	public void setUserWorkspaceIndexProcessingRecordDAO(
			UserWorkspaceIndexProcessingRecordDAO userWorkspaceIndexProcessingRecordDAO) {
		this.userWorkspaceIndexProcessingRecordDAO = userWorkspaceIndexProcessingRecordDAO;
	}
	
	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}
	
	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}
	
	public IndexProcessingTypeService getIndexProcessingTypeService() {
		return indexProcessingTypeService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

}
