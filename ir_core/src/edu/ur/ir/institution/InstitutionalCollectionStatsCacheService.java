package edu.ur.ir.institution;

public interface InstitutionalCollectionStatsCacheService {
	
	/**
	 * Get the download count for the collection
	 * @param forceSelect - if true will perform a select against the database and update results
	 * 
	 * @return the total download count
	 */
	public Long getDownloadCount(InstitutionalCollection collection, boolean forceSelect);
	
	/**
	 * Get the download count for the given collection including all child collections.
	 * 
	 * @param collectionId - id of the collection 
	 * @param forceSelect - if set to true will select from the database
	 * 
	 * @return download count for all collections including children
	 */
	public Long getDownloadCountWithChildren(InstitutionalCollection collection, boolean forceSelect);
	
	/**
	 * Get the total item count in the collection
	 * 
	 * @param forceSelect - if true will perform a select against the database and update results
	 * @return the total item count
	 */
	public Long getItemCount(InstitutionalCollection collection, boolean forceSelect);
	
	/**
	 * Get the total item count in the collection including child collections
	 * 
	 * @param forceSelect - if true will perform a select against the database and update results
	 * @return the total item count with children
	 */
	public Long getItemCountWithChildren(InstitutionalCollection collection, boolean forceSelect);
	
	
	/**
	 * Get the total child collection count
	 * 
	 * @param forceSelect - if true will perform select against the database and update result
	 * @return collection count - total child collection count
	 */
	public Long getCollectionCount(InstitutionalCollection collection, boolean forceSelect);
	
	
	/**
	 * Update all of the information for a given collection.
	 * 
	 * @param collectionId - id of the collection
	 */
	public void updateChildToParentCollectionStats(InstitutionalCollection collection);
	
	/**
	 * Update all of the information for all collections
	 */
	public void updateAllCollectionStats();

}
