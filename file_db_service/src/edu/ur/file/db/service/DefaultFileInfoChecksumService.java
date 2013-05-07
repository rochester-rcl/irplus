package edu.ur.file.db.service;

import java.util.Date;
import java.util.List;

import edu.ur.file.db.FileInfoChecksum;
import edu.ur.file.db.FileInfoChecksumDAO;
import edu.ur.file.db.FileInfoChecksumService;
import edu.ur.order.OrderType;

/**
 * Default file info checksum service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultFileInfoChecksumService implements FileInfoChecksumService {

	/* service to deal with file info checksum data */
	private FileInfoChecksumDAO fileInfoChecksumDAO;


	/**
	 * Get the file info checksum by id.
	 * 
	 * @param id - id of the checksum
	 * @param lock - upgrade the lock
	 * 
	 * @return the file info checksum or null if not found
	 */
	public FileInfoChecksum getById(Long id, boolean lock)
	{
		return fileInfoChecksumDAO.getById(id, lock);
	}

	/**
	 * Save the file info checksum.
	 * 
	 * @param entity
	 */
	public void save(FileInfoChecksum entity)
	{
		fileInfoChecksumDAO.makePersistent(entity);
	}

	/**
	 * Delete the file info checksum.
	 * 
	 * @param entity
	 */
	public void delete(FileInfoChecksum entity){
		fileInfoChecksumDAO.makeTransient(entity);
	}

	/**
	 * Get the oldest checked checksums where check = true.
	 * 
	 * @param start - start position in the number of 
	 * @param maxResults - maximum number of results to retrieve.
	 * @param beforeDate - checksum must have been recalcuated before the given date.
	 * 
	 * @return - all checksums that should be checked that are before the given date
	 */
	public List<FileInfoChecksum> getOldestChecksumsForChecker(int start, int maxResults, Date beforeDate)
	{
		return fileInfoChecksumDAO.getOldestChecksumsForChecker(start, maxResults, beforeDate);
	}
	
	public FileInfoChecksumDAO getFileInfoChecksumDAO() {
		return fileInfoChecksumDAO;
	}

	public void setFileInfoChecksumDAO(FileInfoChecksumDAO fileInfoChecksumDAO) {
		this.fileInfoChecksumDAO = fileInfoChecksumDAO;
	}
	
	/**
	 * Total number of checksum infos in the repository.
	 * 
	 * @return total number of checksum infos.
	 */
	public Long getCount()
	{
		return fileInfoChecksumDAO.getCount();
	}

	/**
	 * Get the checksum file info's in date order 
	 * 
	 * @param start - start position.
	 * @param maxResults - maximum number of results to retrieve.
	 * @param onlyFails - get only the one's that have failed their checksum
	 * @param orderType - order date ascending or descending
	 * 
	 * @return - all checksum infos found
	 */
	public List<FileInfoChecksum> getChecksumInfosDateOrder(int start,
			int maxResults, boolean onlyFails, OrderType orderType) {
		return fileInfoChecksumDAO.getChecksumInfosDateOrder(start, maxResults, onlyFails, orderType);
	}

	
	public Long getChecksumInfoFailsCount() {
		return fileInfoChecksumDAO.getChecksumInfoFailsCount();
	}

}
