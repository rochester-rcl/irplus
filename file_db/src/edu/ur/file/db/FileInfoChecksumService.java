package edu.ur.file.db;

import java.util.Date;
import java.util.List;

import edu.ur.order.OrderType;


/**
 * Service to deal with file info checksum information.
 * 
 * @author Nathan Sarr
 *
 */
public interface FileInfoChecksumService {
	
	/**
	 * Get the file info checksum by id.
	 * 
	 * @param id - id of the checksum
	 * @param lock - upgrade the lock
	 * 
	 * @return the file info checksum or null if not found
	 */
	public FileInfoChecksum getById(Long id, boolean lock);

	/**
	 * Save the file info checksum.
	 * 
	 * @param entity
	 */
	public void save(FileInfoChecksum entity);

	/**
	 * Delete the file info checksum.
	 * 
	 * @param entity
	 */
	public void delete(FileInfoChecksum entity);

	/**
	 * Get the oldest checked checksums where check = true.
	 * 
	 * @param start - start position in the number of 
	 * @param maxResults - maximum number of results to retrieve.
	 * @param beforeDate - checksum must have been recalcuated before the given date.
	 * 
	 * @return - all checksums that should be checked that are before the given date
	 */
	public List<FileInfoChecksum> getOldestChecksumsForChecker(int start, int maxResults, Date beforeDate);
	
	
	/**
	 * Total number of checksum infos in the repository.
	 * 
	 * @return total number of checksum infos.
	 */
	public Long getCount(); 

	
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
			int maxResults, 
			boolean onlyFails, 
			OrderType orderType);
	
	/**
	 * Get a count of all the checksum infos that have failed checking.
	 * 
	 * @return count of failed checksums
	 */
	public Long getChecksumInfoFailsCount();
}
