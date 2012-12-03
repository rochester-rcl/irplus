package edu.ur.ir.repository;

/**
 * Represents a cached object with repository information.
 * 
 * @author Nathan Sarr
 *
 */
public interface RepositoryStatsCacheService {

	
	/**
	 * Get the download count for the repository
	 * @param forceSelect - if true will perform a select against the database and update results
	 * 
	 * @return the total download count
	 */
	public Long getDownloadCount(boolean forceSelect);
	
	/**
	 * Get the total item count in the repository
	 * 
	 * @param forceSelect - if true will perform a select against the database and update results
	 * @return the total item count
	 */
	public Long getItemCount(boolean forceSelect);
	
	
	/**
	 * Get the total collection count
	 * 
	 * @param forceSelect - if true will perform select against the database and update result
	 * @return collection count - total collection count
	 */
	public Long getCollectionCount(boolean forceSelect);
	
	/**
	 * Get the total number of users.
	 * @param forceSelect - if true number of users is selected from the database.
	 * @return - total number of users
	 */
	public Long getUserCount(boolean forceSelect);
	
	/**
	 * Force all cache values to be updated from database
	 */
	public void forceCacheUpdate();
}
