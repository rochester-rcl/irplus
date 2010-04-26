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

import java.util.List;

import edu.ur.dao.CrudDAO;
import edu.ur.order.OrderType;


/**
 * Implementation for Institutional item version.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalItemVersionDAO extends CrudDAO<InstitutionalItemVersion>{

	
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

}
