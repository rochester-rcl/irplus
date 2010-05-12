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
import java.util.List;
import java.util.Set;

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

}
