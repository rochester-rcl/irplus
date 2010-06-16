/**  
   Copyright 2008 University of Rochester

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

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;


/**
 * Implementation for Institutional item version.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalItemVersionDAO extends CrudDAO<InstitutionalItemVersion>, CountableDAO{

	
	/**
	 * Get a count of all institutional item versions 
	 * 
	 * @param collection - collection the instituional item versions must be within - this includes
	 * sub collections.
	 * 
	 * @return - count of all items within the collections
	 */
	public Long getCount(InstitutionalCollection collection);
	
	/**
	 * Get an item version by handle id.
	 * 
	 * @param handleId - id of the handle
	 * @return - the item version that uses the handle or null if not found
	 */
	public InstitutionalItemVersion getItemVersionByHandleId(Long handleId);
	
	/**
	 * Get a list of items for a specified sponsor ordered by publication name.
	 * 
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param sponsorId - id of the sponsor
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional item version
	 */
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemNameOrder(final int rowStart,
			int maxResults, 
			long sponsorId, 
			OrderType orderType);
	
	/**
	 * Get a list of items for a specified sponsor ordered by deposit date.
	 * 
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param sponsorId - id of the sponsor
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional item version
	 */
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemDepositDateOrder(int rowStart,
			int maxResults, 
			long sponsorId, 
			OrderType orderType);
	
	/**
	 * Get the count of institutional item version for a given sponsor.
	 * 
	 * @param sponsorId - id of the sponsor
	 * @return - count of items for a sponsor.
	 */
	public Long getItemsBySponsorCount(long sponsorId);
	
	/**
	 * Get the items by sponsor item download order.
	 * 
	 * @param rowStart
	 * @param maxResults
	 * @param sponsorId
	 * @param orderType
	 * @return
	 */
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemDownloadOrder(
			final int rowStart, final int maxResults, final long sponsorId, final OrderType orderType);
		
	/**
	 * Get the total downloads for a sponsor.
	 * 
	 * @param sponsorId
	 * @return the download count
	 */
	public Long getDownloadCountForSponsor(Long sponsorId);
	
	/**
	 * Get the sum of downloads for a given perons name id.
	 * 
	 * @param personNameIds - set of person name ids
	 * @return sum of downloads
	 */
	public Long getDownloadCountByPersonName(List<Long> personNameIds);
	
	/**
	 * Get the  publications for a given set of names ordered by title.
	 * 
	 * @param rowStart - start position
	 * @param maxResults - maximum number of results to return
	 * @param personNameIds - set of name ids to get 
	 * @param orderType - way to order the set
	 * 
	 * @return
	 */
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByTitle(int rowStart,
			int maxResults, 
			List<Long> personNameIds, 
			OrderType orderType);
	
	/**
	 * Get the  publications for a given set of names by title.
	 * 
	 * @param rowStart - start position
	 * @param maxResults - maximum number of results to return
	 * @param personNameIds - set of name ids to get 
	 * @param orderType - way to order the set
	 * 
	 * @return - 
	 */
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByDownload(final int rowStart,
			final int maxResults, 
			final List<Long> personNameIds, 
			final OrderType orderType);
	
	/**
	 * Get the  publications for a given set of names by submission date
	 * 
	 * @param rowStart - start position
	 * @param maxResults - maximum number of results to return
	 * @param personNameIds - set of name ids to get 
	 * @param orderType - way to order the set
	 * 
	 * @return - 
	 */
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesBySubmissionDate(final int rowStart,
			final int maxResults, 
			final List<Long> personNameIds, 
			final OrderType orderType);
	
	
	/**
	 * Get the earliest submission date found in the institutional item versions. 
	 * 
	 * @return the earliest date of deposit found in the repository.
	 */
	public Date getEarliestDateOfDeposit();
	
	/**
	 * Get all institutional item versions by using the specified generic item id.
	 * 
	 * @param genericItemId - the generic item id
	 * @return list of institutional item versions using the specified generic item id.
	 */
	public List<InstitutionalItemVersion> getInstitutionalItemVersionsByGenericItemId(Long genericItemId);

	
	/**
	 * Get a list of institutional items ordered by institutional item version id order ascending.
	 * 
	 * @param lastInstitutionalItemVersionId - the last institutional item version id
	 * to be processed.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param maxResulsts - maximum number of results to fetch
	 * @return List of institutional item version
	 */
	public List<InstitutionalItemVersion> getItemsIdOrder( long lastInstitutionalItemVersionId,
			int maxResults);
	
	/**
	 * Get a list of institutional items ordered by institutional item version id order ascending.
	 * 
	 * @param lastInstitutionalItemVersionId - the last institutional item version id
	 * to be processed.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param institutional collection - the set to look within
	 * @param maxResults - maximum number of results
	 * 
	 * @return - items greater than the given id and belong to the specified set
	 */
	public List<InstitutionalItemVersion> getItemsIdOrder( long lastInstitutionalItemVersionId,
			InstitutionalCollection institutionalCollection, int maxResults);
	
	/**
	 * Get the items ordered by id  that are greater than or equal to the given modified date.  This will include 
	 * those items that were modified on the date.Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param lastInstitutionalItemVersionId - last institutional item id.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * @param fromModifiedDate - starting from modification date
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return list of institutional item versions greater than the given id and greater than or equal to
	 * the given from deposit date.
	 */
	public List<InstitutionalItemVersion> getItemsIdOrderFromModifiedDate(
			long lastInstitutionalItemVersionId, Date fromModifiedDate, int maxResults); 
	
	/**
	 * Get the items ordered by id  that are greater than or equal to the given modification date.  This will include 
	 * those items that were modified on the date.  Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param lastInstitutionalItemVersionId - last institutional item id.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * @param fromModifiedDate - starting from modified date
	 * @param institutional collection - set the items must belong to 
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return list of institutional item versions greater than the given id and greater than or equal to
	 * the given from modified date.
	 */
	public List<InstitutionalItemVersion> getItemsIdOrderFromModifiedDate(
			long lastInstitutionalItemVersionId, Date fromModifiedDate, InstitutionalCollection institutionalCollection, int maxResults); 
	
	/**
	 * Get the items ordered by id  that are less than or equal to the given modified date.  
	 * This will include those items that were modified on the date.  Will grab max results
	 * of where ids are greater than the given id.

	 * @param lastInstitutionalItemVersionId - last institutional item id that items must be greater than
	 * @param untilModifiedDate - modified date the items must be less than or equal to
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return - list of items id ordered that are less than or equal to the given date.
	 */
	public List<InstitutionalItemVersion> getItemsIdOrderUntilModifiedDate(
			long lastInstitutionalItemVersionId, Date untilModifiedDate, int maxResults); 
	
	/**
	 * Get the items ordered by id  that are less than or equal to the given deposit date.  
	 * This will include those items that were modified on the date.  Will grab max results
	 * of where ids are greater than the given id.

	 * @param lastInstitutionalItemVersionId
	 * @param untilModifiedDate - modified date the items must be less than or equal to
	 * @param institutional collection - set the items must belong to.
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return list of items id ordered that are less than or equal to the given date and belong to 
	 * the specified set id
	 */
	public List<InstitutionalItemVersion> getItemsIdOrderUntilModifiedDate(
			long lastInstitutionalItemVersionId, Date untilModifiedDate, 
			InstitutionalCollection institutionalCollection, int maxResults); 
	
	/**
	 * Get the items ordered by id  that are between the given from and until deposit dates.  Will return 
	 * only items greater than the given last institutional item ids.
	 *   
	 * This will include those items that were modified on the given dates.  Will grab max results
	 * where ids are greater than the given id.

	 * @param lastInstitutionalItemVersionId - id that institutional item versions must be greater than
     * @param fromModifiedDate - items modified date must be greater than or equal to
	 * @param untilModifiedDate - items modified date must be less than or equal to
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return - list of items that meet the specified criteria.
	 */
	public List<InstitutionalItemVersion> getItemsIdOrderBetweenModifiedDates(
			long lastInstitutionalItemVersionId, Date fromModifiedDate, Date untilModifiedDate, int maxResults); 
	
	/**
	 * Get the items ordered by id  that are between the given from and until modified dates.  Will return 
	 * only items greater than the given last institutional item ids.
	 *   
	 * This will include those items that were modified on the given dates.  Will grab max results
	 * where ids are greater than the given id.

	 * @param lastInstitutionalItemVersionId - id that institutional item versions must be greater than
     * @param fromModifiedDate - items modified date must be greater than or equal to
	 * @param untilModifiedDate - items modified date must be less than or equal to
	 * @param institutional collection - the set the items must belong to
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return - list of items that meet the specified criteria.
	 */

	public List<InstitutionalItemVersion> getItemsIdOrderBetweenModifiedDates(
			long lastInstitutionalItemVersionId, Date fromModifiedDate, Date untilModifiedDate, InstitutionalCollection institutionalCollection, int maxResults); 
	
	/**
	 * Get a count of the institutional item versions 
	 * 
	 * @param fromModifiedDate - from date modification
	 * @param untilModifiedDate - until date modification
	 * @param institutionalCollection - collections the items should be within
	 * 
	 * @return - count of items found
	 */
	public Long getItemsBetweenModifiedDatesCount( Date fromModifiedDate,
			Date untilModifiedDate, InstitutionalCollection institutionalCollection);
	
	/**
	 * Get a count of institutional item versions between the modified dates.
	 * 
	 * @param fromModifiedDate - from date
	 * @param untilModifiedDate - until date
	 * 
	 * @return all items modified between the from date and until date.
	 */
	public Long getItemsBetweenModifiedDatesCount( Date fromModifiedDate,
			Date untilModifiedDate); 
	
	/**
	 * Get a count of items within a given collection that have a modification date
	 * greater than or equal to the specified date.  This includes sub collections.
	 * 
	 * @param fromModifiedDate - date the modification or creation must be greater than or equal to
	 * @param institutionalCollection - the institutional collection they must reside in.  
	 * 
	 * @return the count of the number of items found greater than the specified date and within the specified collection
	 * or sub collections
	 */
	public Long getItemsFromModifiedDateCount(
			Date fromModifiedDate,
			InstitutionalCollection institutionalCollection);
	
	/**
	 * Get a count of items that have a modification date
	 * greater than or equal to the specified date. 
	 * 
	 * @param fromModifiedDate - date the modification or creation must be greater than or equal to
	 * 
	 * @return the count of the number of items found greater than the specified date 
	 */
	public Long getItemsFromModifiedDateCount(Date fromModifiedDate);
	
	
	/**
	 * Get a count of all items added or modified equal to or before the specified date.
	 * 
	 * @param until ModifiedDate - date the modification or addition should be less than or equal to
	 * @return
	 */
	public Long getItemsUntilModifiedDateCount(Date untilModifiedDate);
	
	/**
	 * Get a count of items within a given collection that have a modification date
	 * less than or equal to the specified date.  This includes sub collections.
	 * 
	 * @param fromModifiedDate - date the modification or creation must be greater than or equal to
	 * @param institutionalCollection - the institutional collection they must reside in.  
	 * 
	 * @return the count of the number of items found less than or equal to the specified date and within the specified collection
	 * or sub collections
	 */
	public Long getItemsUntilModifiedDateCount(
			Date untilModifiedDate,
			InstitutionalCollection institutionalCollection);
	
	/**
	 * Updates all versions with the specified content type as modified with current 
	 * time and the specified message.  This includes both primary content types and
	 * secondary content types.
	 * 
	 * @param contentType - content type id that has changed
	 * @param user - user making the change
	 * @param message - message for the change
	 * 
	 * @return number of item versions set as modified
	 */
	public Long setAsModifiedByContentTypeChange(ContentType contentType, IrUser user, String message);

	/**
	 * Updates all versions with the speicified contributor type as modified with current time and
	 * specified message.  
	 * 
	 * @param contributorType  -  of the contributor type modified
	 * @param user - user making the change
	 * @param message - message to set for the changes.
	 * 
	 * @return the number of item versions set as modified
	 */
	public Long setAsModifiedByContributorTypeChange(ContributorType contributorType, IrUser user, String message);

}
