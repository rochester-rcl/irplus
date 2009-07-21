package edu.ur.ir.user.service;

import java.util.List;

import edu.ur.ir.FileSystem;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecord;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;

/**
 * Implementation of the user workspace index processing record service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserWorkspaceIndexProcessingRecordService implements UserWorkspaceIndexProcessingRecordService{

	public void allUserItems(IrUser user, IndexProcessingType processingType) {
		// TODO Auto-generated method stub
		
	}

	public void delete(
			UserWorkspaceIndexProcessingRecord userWorkspaceIndexProcessingRecord) {
		// TODO Auto-generated method stub
		
	}

	public UserWorkspaceIndexProcessingRecord get(Long id, boolean lock) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserWorkspaceIndexProcessingRecord get(Long userId,
			FileSystem fileSystem, IndexProcessingType processingType) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UserWorkspaceIndexProcessingRecord> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UserWorkspaceIndexProcessingRecord> getAllOrderByIdDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getCount() {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(
			UserWorkspaceIndexProcessingRecord userWorkspaceIndexProcessingRecord) {
		// TODO Auto-generated method stub
		
	}

	public UserWorkspaceIndexProcessingRecord save(Long userId,
			FileSystem fileSystem, IndexProcessingType processingType) {
		// TODO Auto-generated method stub
		return null;
	}

}
