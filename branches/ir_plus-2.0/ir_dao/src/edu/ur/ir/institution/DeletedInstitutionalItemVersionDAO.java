/**  
   Copyright 2008-2010 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  

package edu.ur.ir.institution;

import java.util.Date;
import java.util.List;

import edu.ur.dao.CrudDAO;

/**
 * Database access interface for institutional item versions
 * 
 * @author Nathan Sarr
 *
 */
public interface DeletedInstitutionalItemVersionDAO extends CrudDAO<DeletedInstitutionalItemVersion>{
	
	/**
	 * Get Delete info for institutional item version by it's original institutional item version id
	 * 
	 * @param institutionalItemVersionId Id of institutional item version
	 * @return Information about deleted institutional item version
	 */
	public DeletedInstitutionalItemVersion get(Long institutionalItemVersionId);
	
	/**
	 * Get Delete info for institutional item version by it's version number and the
	 * institutional item id.
	 * 
	 * @param institutionalItemId - id of the institutional item
	 * @param versionNumber - version number 
	 * 
	 * @return - the deleted institutional item version
	 */
	public DeletedInstitutionalItemVersion get(Long institutionalItemId, int versionNumber);
	
	/**
	 * Get a list of deleted institutional items ordered by institutional item version id ascending.
	 * 
	 * 
	 * @param lastInstitutionalItemVersionId - the last institutional item version id
	 * to be processed.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param maxResulsts - maximum number of results to fetch

	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional item version
	 */
	public List<DeletedInstitutionalItemVersion> getItemsIdOrder( long lastDeletedInstitutionalItemVersionId,
			int maxResults);
	
	/**
	 * Get a list of deleted institutional items ordered by institutional item version id order ascending.
	 * 
	 * @param lastDeletedInstitutionalItemVersionId - the last deleted institutional item version id
	 * to be processed.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param institutional collection ids - the collections to look within
	 * @param maxResults - maximum number of results
	 * 
	 * @return - deleted items greater than the given id and belong to the specified set
	 */
	public List<DeletedInstitutionalItemVersion> getItemsIdOrder( long lastDeletedItemVersionId,
			List<Long> institutionalCollectionIds, int maxResults);
	
	/**
	 * Get a count of the total number of deleted institutional item versions.
	 * 
	 * @return
	 */
	public Long getCount();
	
	/**
	 * Get a count of deleted institutional item versions within a collection
	 * 
	 * @param institutional collection ids - the collections to look within
	 * 
	 * @return the count
	 */
	public Long getCount(List<Long> institutionalCollectionIds);
	
	/**
	 * Get a count of the deleted institutional item versions that have been deleted
	 * between the specified dates - this includes new additions to the repository
	 * 
	 * @param fromDeletedDate - from date deleted
	 * @param untilDeletedDate - until date deleted
	 * @param institutional collection ids - the collections to look within
	 * 
	 * @return - count of items found
	 */
	public Long getItemsBetweenDeletedDatesCount( Date fromDeletedDate,
			Date untilDeletedDate, List<Long> institutionalCollectionIds);
	
	/**
	 * Get a count of deleted institutional item versions between the deleted dates.
	 * 
	 * @param fromDeletedDate - from date
	 * @param untilDeletedDate - until date
	 * 
	 * @return all items deleted between the from date and until date.
	 */
	public Long getItemsBetweenDeletedDatesCount( Date fromDeletedDate,
			Date untilDeletedDate);
	
	/**
	 * Get a count of deleted items within a given collection that have a deleted date
	 * greater than or equal to the specified date.  This includes sub collections.
	 * 
	 * @param fromDeletedDate - date the deletion must be greater than or equal to
	 * @param institutional collection ids - the collections to look within
	 * 
	 * @return the count of the number of items found greater than the specified date and within the specified collection
	 * or sub collections
	 */
	public Long getItemsFromDeletedDateCount( Date fromDeletedDate,
			List<Long> institutionalCollectionIds);
	
	/**
	 * Get a count of all deleted items  equal to or before the specified date.
	 * 
	 * @param until Deleted Date - date the deletion should be less than or equal to
	 * @return
	 */
	public Long getItemsUntilDeletedDateCount(Date untilDeletedDate);
	
	/**
	 * Get a count of deleted items within a given collection that have a deletion date
	 * less than or equal to the specified date.  This includes sub collections.
	 * 
	 * @param fromDeletedDate - date the deletion must be greater than or equal to
	 * @param institutional collection ids - the collections to look within
	 * 
	 * @return the count of the number of items found less than or equal to the specified date and within the specified collection
	 * or sub collections
	 */
	public Long getItemsUntilDeletedDateCount(
			Date untilDeletedDate,
			List<Long> institutionalCollectionIds);
	
	/**
	 * Get the deleted items ordered by id  that are greater than or equal to the given deletion date.  This will include 
	 * those items that were deleted on the date.Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param lastDeletedInstitutionalItemVersionId - last deleted institutional item id.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * @param fromDeletedDate - starting from deletion date
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return list of institutional item versions greater than the given id and greater than or equal to
	 * the given from delete date.
	 */
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderFromDeletedDate(
			long lastDeletedInstitutionalItemVersionId, Date fromDeletedDate, int maxResults); 
	
	/**
	 * Get a count of deleted items that have a deletion date
	 * greater than or equal to the specified date. 
	 * 
	 * @param fromDeletedDate - date the deletion must be greater than or equal to
	 * 
	 * @return the count of the number of items found greater than the specified date 
	 */
	public Long getItemsFromDeletedDateCount(Date fromDeletedDate);
	
	
	/**
	 * Get the items ordered by id  that are less than or equal to the given deletion date.  
	 * This will include those items that were deleted on the date.  Will grab max results
	 * of where ids are greater than the given id.

	 * @param lastDeletedInstitutionalItemVersionId - last institutional item id that items must be greater than
	 * @param untilDeletedDate - deleted date the items must be less than or equal to
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return - list of items id ordered that are less than or equal to the given date.
	 */
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderUntilDeletedDate(
			long lastDeletedInstitutionalItemVersionId, Date untilDeletedDate, int maxResults); 
	
	/**
	 * Get the items ordered by id  that are between the given from and until deposit dates.  Will return 
	 * only items greater than the given last institutional item ids.
	 *   
	 * This will include those items that were deleted on the given dates.  Will grab max results
	 * where ids are greater than the given id.

	 * @param lastDeletedInstitutionalItemVersionId - id that institutional item versions must be greater than
     * @param fromDeletedDate - items deleted date must be greater than or equal to
	 * @param untilDeletedDate - items deleted date must be less than or equal to
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return - list of items that meet the specified criteria.
	 */
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderBetweenDeletedDates(
			long lastDeletedInstitutionalItemVersionId, Date fromDeletedDate, Date untilDeletedDate, int maxResults); 
	
	/**
	 * Get the items ordered by id  that are greater than or equal to the given deleted date.  This will include 
	 * those items that were deleted on the date.  Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param lastDeletedInstitutionalItemVersionId - last institutional item id.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * @param fromDeletedDate - starting from deleted date
	 * @param institutional collection ids - the collections to look within
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return list of institutional item versions greater than the given id and greater than or equal to
	 * the given from deleted date.
	 */
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderFromDeletedDate(
			long lastDeletedInstitutionalItemVersionId, Date fromDeletedDate, 
			List<Long> institutionalCollectionIds, int maxResults); 
	
	/**
	 * Get the items ordered by id  that are less than or equal to the given deleted date.  
	 * This will include those items that were deleted on the date.  Will grab max results
	 * of where ids are greater than the given id.

	 * @param lastDeletedInstitutionalItemVersionId
	 * @param untilDeletedDate - deleted date the items must be less than or equal to
	 * @param institutional collection ids - the collections to look within
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return list of items id ordered that are less than or equal to the given date and belong to 
	 * the specified set id
	 */
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderUntilDeletedDate(
			long lastDeletedInstitutionalItemVersionId, Date untilDeletedDate, 
			List<Long> institutionalCollectionIds, int maxResults); 
	
	/**
	 * Get the items ordered by id  that are between the given from and until deleted dates.  Will return 
	 * only items greater than the given last institutional item ids.
	 *   
	 * This will include those items that were deleted on the given dates.  Will grab max results
	 * where ids are greater than the given id.

	 * @param lastDeletedInstitutionalItemVersionId - id that institutional item versions must be greater than
     * @param fromDeletedDate - items deleted date must be greater than or equal to
	 * @param untilDeletedDate - items deleted date must be less than or equal to
	 * @param institutional collection ids - the collections to look within
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return - list of items that meet the specified criteria.
	 */

	public List<DeletedInstitutionalItemVersion> getItemsIdOrderBetweenDeletedDates(
			long lastDeletedInstitutionalItemVersionId, Date fromDeletedDate, Date untilDeletedDate, 
			List<Long> institutionalCollectionIds, int maxResults); 

}
