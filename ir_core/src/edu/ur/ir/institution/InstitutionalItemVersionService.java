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

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.CopyrightStatement;
import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;

/**
 * Deals with institutional item version information.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalItemVersionService extends Serializable {

	/**
	 * Get an institutional item version by handle id.
	 * 
	 * @param handleId - id of the handle to get the institutional item by.
	 * @return the found institutional item or null if item version is not found.
	 */
	public InstitutionalItemVersion getInstitutionalItemByHandleId(Long handleId);
	
	/**
     * Get the Institutional item version
     * 
     * @param id - id of the institutional item version
     * @param lock - upgrade the lock
     * 
     * @return the institutional item version or null if not found.
     */
    public InstitutionalItemVersion getInstitutionalItemVersion(Long id, boolean lock);
    
    /**
     * Get all institutional item versions that use the specified generic item.
     * 
     * @param genericItemId - unique id for the generic item.
     * @return list of institutional item versions that use the generic item.
     */
    public List<InstitutionalItemVersion> getInstitutionalItemVersionsByGenericItemId(Long genericItemId);
    
	/**
	 * Get the count of institutional item version for a given sponsor.
	 * 
	 * @param sponsorId - id of the sponsor
	 * @return - count of items for a sponsor.
	 */
	public Long getItemsBySponsorCount(long sponsorId);
	
	/**
	 * Get a list of institutional item version for a specified sponsor ordered by deposit date.
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
	 * Get a list of institutional item version for a specified sponsor ordered by publication name.
	 * 
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param sponsorId - id of the sponsor
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional item version
	 */
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemDownloadOrder(int rowStart,
			int maxResults, 
			long sponsorId, 
			OrderType orderType);
	
	
	/**
	 * Get a list of institutional item version for a specified sponsor ordered by publication name.
	 * 
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param sponsorId - id of the sponsor
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional item version download count
	 */
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemNameOrder(int rowStart,
			int maxResults, 
			long sponsorId, 
			OrderType orderType);
	
	/**
	 * Get the number of downloads for a given set of person names.
	 * 
	 * @param personNames - set of person names.
	 * @return count for the person names.
	 */
	public Long getNumberOfDownlodsForPersonNames(Set<PersonName> personNames);
	
	
	/**
	 * Get the list of publication versions for names ordered by download.
	 * 
	 * @param rowStart - row start
	 * @param maxResults - maximum number of results
	 * @param personNames - set of name ids to use
	 * @param orderType - order type
	 * 
	 * @return - return the list of institutional item version download counts
	 */
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByDownload(final int rowStart,
			final int maxResults, 
			final Set<PersonName> personNames, 
			final OrderType orderType);
	
	/**
	 * Get the list of publication versions for names ordered by title
	 * 
	 * @param rowStart - start position in the list
	 * @param maxResults - maximum number of results
	 * @param personNames - set of person names.
	 * @param orderType - order type ascending / descending
	 * 
	 * @return - return the list of institutional item version download counts
	 */
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByTitle(final int rowStart,
			final int maxResults, 
			final Set<PersonName> personNames, 
			final OrderType orderType);
	
	/**
	 * Get the list of publication versions for names ordered by title
	 * 
	 * @param rowStart - start position in the list
	 * @param maxResults - maximum number of results
	 * @param personNames - set of person names.
	 * @param orderType - order type ascending / descending
	 * 
	 * @return - return the list of institutional item version download counts
	 */
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesBySubmissionDate(final int rowStart,
			final int maxResults, 
			final Set<PersonName> personNames, 
			final OrderType orderType);
	
	/**
	 * Save Institutional Item Version
	 * 
	 * @param institutionalItemVersion
	 */
	public void saveInstitutionalItemVersion(InstitutionalItemVersion institutionalItemVersion);
	
	/**
	 * Reset the institutional item url.
	 * 
	 * @param institutionalItem
	 * @param institutionalItemVersion
	 */
	public void resetHandle(InstitutionalItemVersion institutionalItemVersion);
	
	/**
	 * Updates all institutional item versions as updated for the specified generic item
	 * id.  
	 * 
	 * @param user - user making the update
	 * @param genericItemId - the generic item id
	 * @param reason - reason the versions were updated.
	 */
	public void setAllVersionsAsUpdated(IrUser user, Long genericItemId, String reason);
	
	
	/**
	 * Get a list of institutional items ordered by institutional item version id ascending.
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
	 * Get a count of the total number of instituional item versions.
	 * 
	 * @return
	 */
	public Long getCount();
	
	/**
	 * Get a count of institutional item versions within a collection
	 * 
	 * @param collection - collection that versions should reside within
	 * @return the count
	 */
	public Long getCount(InstitutionalCollection collection);
	
	/**
	 * Get a count of the institutional item versions that have been modified
	 * between the specified dates - this includes new additions to the repository
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
	 * Get a count of items that have a modification date
	 * greater than or equal to the specified date. 
	 * 
	 * @param fromModifiedDate - date the modification or creation must be greater than or equal to
	 * 
	 * @return the count of the number of items found greater than the specified date 
	 */
	public Long getItemsFromModifiedDateCount(Date fromModifiedDate);
	
	
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
			long lastInstitutionalItemVersionId, Date fromModifiedDate, Date untilModifiedDate, 
			InstitutionalCollection institutionalCollection, int maxResults); 
	
	
	/**
	 * Sets all versions as updated for a content type by a specified user.  
	 * This should also cause any re-indexing to occur.
	 * 
	 * @param contentType - the content type being changed
	 * @param user - user making the change
	 * @param message - message to set for the modification
	 * 
	 * @return number of records set as updated
	 */
	public Long setAllVersionsAsUpdatedForContentType(ContentType contentType, IrUser user, String message);

	
	/**
	 * Set all versions as updated for a contributor type changed by a specific user.  This 
	 * should also cause any re-indexing to occur.
	 * 
	 * @param contributorType - the contributor type that has been changed
	 * @param user - user making the change
	 * @param message - message to set for the modification
	 * 
	 * @return number of records set as updated.
	 */
	public Long setAllVersionsAsUpdatedForContributorType(ContributorType contributorType, IrUser user, String message);

	/**
	 * Set all versions as updated for a copyright change made by a specific user.  This 
	 * should also cause any re-indexing to occur.
	 * 
	 * @param copyright - the copyright that has been changed
	 * @param user - user making the change
	 * @param message - message to set for the modification
	 * 
	 * @return number of records set as updated.
	 */
	public Long setAllVersionsAsUpdatedForCopyrightStatement(CopyrightStatement copyright, IrUser user, String message);
	
	/**
	 * Set all versions as updated for an extent type change made by a specific user.  This 
	 * should also cause any re-indexing to occur.
	 * 
	 * @param extent type - the extent type that has been changed
	 * @param user - user making the change
	 * @param message - message to set for the modification
	 * 
	 * @return number of records set as updated.
	 */
	public Long setAllVersionsAsUpdatedForExtentType(ExtentType extentType, IrUser user, String message);
	
	/**
	 * Set all versions as updated for an identifier type change made by a specific user.  This 
	 * should also cause any re-indexing to occur.
	 * 
	 * @param identifier type - the identifier type that has been changed
	 * @param user - user making the change
	 * @param message - message to set for the modification
	 * 
	 * @return number of records set as updated.
	 */
	public Long setAllVersionsAsUpdatedForIdentifierType(IdentifierType identifierType, IrUser user, String message);

	/**
	 * Set all versions as updated for a language type change made by a specific user.  This 
	 * should also cause any re-indexing to occur.
	 * 
	 * @param language type - the language type that has been changed
	 * @param user - user making the change
	 * @param message - message to set for the modification
	 * 
	 * @return number of records set as updated.
	 */
	public Long setAllVersionsAsUpdatedForLanguageType(LanguageType languageType, IrUser user, String message);
	
	/**
	 * Set all versions as updated for a place of publication change made by a specific user.  This 
	 * should also cause any re-indexing to occur.
	 * 
	 * @param place of publication - the language type that has been changed
	 * @param user - user making the change
	 * @param message - message to set for the modification
	 * 
	 * @return number of records set as updated.
	 */
	public Long setAllVersionsAsUpdatedForPlaceOfPublication(PlaceOfPublication placeOfPublication, IrUser user, String message);
	
	/**
	 * Set all versions as updated for a person name change made by a specific user.  This 
	 * should also cause any re-indexing to occur.
	 * 
	 * @param personName - the language type that has been changed
	 * @param user - user making the change
	 * @param message - message to set for the modification
	 * 
	 * @return number of records set as updated.
	 */
	public Long setAllVersionsAsUpdatedForPersonName(PersonName personName, IrUser user, String message);
	
	/**
	 * Set all versions as updated for a publisher change made by a specific user.  This 
	 * should also cause any re-indexing to occur.
	 * 
	 * @param publisher - the publisher that has been changed
	 * @param user - user making the change
	 * @param message - message to set for the modification
	 * 
	 * @return number of records set as updated.
	 */
	public Long setAllVersionsAsUpdatedForPublisher(Publisher publisher, IrUser user, String message);
	
	/**
	 * Set all versions as updated for a series change made by a specific user.  This 
	 * should also cause any re-indexing to occur.
	 * 
	 * @param  series - the series that has been changed
	 * @param user - user making the change
	 * @param message - message to set for the modification
	 * 
	 * @return number of records set as updated.
	 */
	public Long setAllVersionsAsUpdatedForSeries(Series series, IrUser user, String message);
	
	/**
	 * Set all versions as updated for a sponsor change made by a specific user.  This 
	 * should also cause any re-indexing to occur.
	 * 
	 * @param sponsor - the sponsor that has been changed
	 * @param user - user making the change
	 * @param message - message to set for the modification
	 * 
	 * @return number of records set as updated.
	 */
	public Long setAllVersionsAsUpdatedForSponsor(Sponsor sponsor, IrUser user, String message);


}
