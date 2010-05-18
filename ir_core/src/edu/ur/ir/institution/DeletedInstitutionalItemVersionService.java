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

/**
 * Service to help deal with deleted institutional item version information.
 * 
 * @author Nathan Sarr
 *
 */
public interface DeletedInstitutionalItemVersionService {
	
	/**
	 * Gets a deleted institutional item version by the original institutional item version.
	 * 
	 * @param institutionalItemVersionId - the original institutional item version id.
	 * 
	 * @return the deleted institutional item version record.
	 */
	public DeletedInstitutionalItemVersion getDeletedVersionByItemVersionId(Long institutionalItemVersionId);

	
	/**
	 * Get the deleted institutional item version by the original institutional item id and version number.
	 * 
	 * @param institutionalItemId - original institutional item id
	 * @param versionNumber - version number
	 * 
	 * @return - the deleted institutional item version record.
	 */
	public DeletedInstitutionalItemVersion getDeletedVersionByItemVersion(Long institutionalItemId, int versionNumber);
	
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
	 * @param institutional collection - the set to look within - this includes sub collections
	 * @param maxResults - maximum number of results
	 * 
	 * @return - deleted items greater than the given id and belong to the specified set
	 */
	public List<DeletedInstitutionalItemVersion> getItemsIdOrder( long lastDeletedItemVersionId,
			InstitutionalCollection institutionalCollection, int maxResults);
	
	/**
	 * Get a count of the total number of deleted institutional item versions.
	 * 
	 * @return
	 */
	public Long getCount();
	
	/**
	 * Get a count of deleted institutional item versions within a collection
	 * 
	 * @param collection - collection that versions should reside within - this will count items
	 * within sub collections.
	 * 
	 * @return the count
	 */
	public Long getCount(InstitutionalCollection collection);
	
	/**
	 * Get a count of the deleted institutional item versions that have been deleted
	 * between the specified dates - this includes new additions to the repository
	 * 
	 * @param fromDeletedDate - from date deleted
	 * @param untilDeletedDate - until date deleted
	 * @param institutionalCollection - collections the items should be within
	 * 
	 * @return - count of items found
	 */
	public Long getItemsBetweenDeletedDatesCount( Date fromDeletedDate,
			Date untilDeletedDate, InstitutionalCollection institutionalCollection);
	
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
	 * @param institutionalCollection - the institutional collection they must reside in.  
	 * 
	 * @return the count of the number of items found greater than the specified date and within the specified collection
	 * or sub collections
	 */
	public Long getItemsFromDeletedDateCount( Date fromDeletedDate,
			InstitutionalCollection institutionalCollection);
	
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
	 * @param institutionalCollection - the institutional collection they must reside in.  
	 * 
	 * @return the count of the number of items found less than or equal to the specified date and within the specified collection
	 * or sub collections
	 */
	public Long getItemsUntilDeletedDateCount(
			Date untilDeletedDate,
			InstitutionalCollection institutionalCollection);
	
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
	 * @param institutional collection - set the items must belong to 
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return list of institutional item versions greater than the given id and greater than or equal to
	 * the given from deleted date.
	 */
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderFromDeletedDate(
			long lastDeletedInstitutionalItemVersionId, Date fromDeletedDate, 
			InstitutionalCollection institutionalCollection, int maxResults); 
	
	/**
	 * Get the items ordered by id  that are less than or equal to the given deleted date.  
	 * This will include those items that were deleted on the date.  Will grab max results
	 * of where ids are greater than the given id.

	 * @param lastDeletedInstitutionalItemVersionId
	 * @param untilDeletedDate - deleted date the items must be less than or equal to
	 * @param institutional collection - set the items must belong to.
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return list of items id ordered that are less than or equal to the given date and belong to 
	 * the specified set id
	 */
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderUntilDeletedDate(
			long lastDeletedInstitutionalItemVersionId, Date untilDeletedDate, 
			InstitutionalCollection institutionalCollection, int maxResults); 
	
	/**
	 * Get the items ordered by id  that are between the given from and until deleted dates.  Will return 
	 * only items greater than the given last institutional item ids.
	 *   
	 * This will include those items that were deleted on the given dates.  Will grab max results
	 * where ids are greater than the given id.

	 * @param lastDeletedInstitutionalItemVersionId - id that institutional item versions must be greater than
     * @param fromDeletedDate - items deleted date must be greater than or equal to
	 * @param untilDeletedDate - items deleted date must be less than or equal to
	 * @param institutional collection - the set the items must belong to
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return - list of items that meet the specified criteria.
	 */

	public List<DeletedInstitutionalItemVersion> getItemsIdOrderBetweenDeletedDates(
			long lastDeletedInstitutionalItemVersionId, Date fromDeletedDate, Date untilDeletedDate, 
			InstitutionalCollection institutionalCollection, int maxResults); 

}
