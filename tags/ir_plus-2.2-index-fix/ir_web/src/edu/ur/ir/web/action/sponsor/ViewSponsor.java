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

package edu.ur.ir.web.action.sponsor;

import java.util.LinkedList;
import java.util.List;

import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionDownloadCount;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorService;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Shows sponsor information 
 * 
 * @author Nathan Sarr
 *
 */
public class ViewSponsor extends Pager  
{

	/** eclipse generated id */
	private static final long serialVersionUID = 7255006748606537145L;
	
	/** Id of the sponsor */
	private Long sponsorId;
	
	/** the sponsor */
	private Sponsor sponsor;
	
	/** Service to deal with sponsor information */
	private SponsorService sponsorService;
	
	/** Service class for institutional Item */
	private InstitutionalItemVersionService institutionalItemVersionService;
	



	/** Statistics service*/
	private DownloadStatisticsService downloadStatisticsService;

	/** Total downloads */
	private Long totalDownloads = 0l;
	
	/** Latest publication */
	private InstitutionalItemVersion latestItemVersion;
	
	/** Maximum number of times an item is downloaded */
	private long mostDownloadedCount = 0l;
	
	/** Maximum downloaded item version */
	private InstitutionalItemVersion mostDownloadedItemVersion;
	
	/** Total number of sponsors */
	private int totalHits;
	
	/** sponsor publications */
	private List<InstitutionalItemVersionDownloadCount> sponsorPublications = new LinkedList<InstitutionalItemVersionDownloadCount>();

	/** Row End */
	private int rowEnd;
	
	/** most downloaded count  */
	private Long mostDownloadCount = 0l;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	

	/** name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "title";


	public ViewSponsor()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 20;
	}
	
	

	public String execute()
	{
		if( sponsorId == null )
		{
			return SUCCESS;
		}
		
		rowEnd = rowStart + numberOfResultsToShow;
		
		sponsor = sponsorService.get(sponsorId, false);
		
		// Construct the object with item and download info for display
		if( sortElement.equals("title"))
		{
		    // Construct the object with item and download info for display
		    sponsorPublications = institutionalItemVersionService.getItemsBySponsorItemNameOrder(rowStart, 
				numberOfResultsToShow, 
				sponsorId, 
				OrderType.getOrderType(sortType));
		}
		if( sortElement.equals("download"))
		{
			// Construct the object with item and download info for display
		    sponsorPublications = institutionalItemVersionService.getItemsBySponsorItemDownloadOrder(rowStart, 
				numberOfResultsToShow, 
				sponsorId, 
				OrderType.getOrderType(sortType));
		}
		if( sortElement.equals("submissionDate"))
		{
			// Construct the object with item and download info for display
		    sponsorPublications = institutionalItemVersionService.getItemsBySponsorItemDepositDateOrder(rowStart, 
				numberOfResultsToShow, 
				sponsorId, 
				OrderType.getOrderType(sortType));
		}
		
		
		totalHits = institutionalItemVersionService.getItemsBySponsorCount(sponsorId).intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
		
		totalDownloads = downloadStatisticsService.getNumberOfDownloadsBySponsor(sponsorId);
		
		// latest added to the repository
		List<InstitutionalItemVersionDownloadCount> dateOrderItems = institutionalItemVersionService.getItemsBySponsorItemDepositDateOrder(0, 1, sponsorId, OrderType.DESCENDING_ORDER);
		if(dateOrderItems.size() > 0 )
		{
			latestItemVersion =  dateOrderItems.get(0).getInstitutionalItemVersion();
		}
		
		//most downloaded
		List<InstitutionalItemVersionDownloadCount> downloadOrderedItems = institutionalItemVersionService.getItemsBySponsorItemDownloadOrder(0, 1, sponsorId, OrderType.DESCENDING_ORDER);
		if( downloadOrderedItems.size() > 0 )
		{
			mostDownloadedItemVersion = downloadOrderedItems.get(0).getInstitutionalItemVersion();
			mostDownloadedCount = downloadOrderedItems.get(0).getDownloadCount();
		}
		
		return SUCCESS;
	}
	
	
	public int getTotalHits() {
		return totalHits;
	}
	
	public int getRowEnd() {
		return rowEnd;
	}


	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}
	
	/**
	 * Get the sponsor id.
	 * @return
	 */
	public long getSponsorId() {
		return sponsorId;
	}

	/**
	 * Set the sponsor id.
	 * 
	 * @param sponsorId
	 */
	public void setSponsorId(long sponsorId) {
		this.sponsorId = sponsorId;
	}
	
	public Sponsor getSponsor() {
		return sponsor;
	}

	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	public SponsorService getSponsorService() {
		return sponsorService;
	}

	public void setSponsorService(SponsorService sponsorService) {
		this.sponsorService = sponsorService;
	}

	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}


	public long getTotalDownloads() {
		return totalDownloads;
	}

	public void setTotalDownloads(long totalDownloads) {
		this.totalDownloads = totalDownloads;
	}

	public InstitutionalItemVersion getLatestItemVersion() {
		return latestItemVersion;
	}

	public void setLatestItemVersion(InstitutionalItemVersion latestItemVersion) {
		this.latestItemVersion = latestItemVersion;
	}

	public long getMostDownloadedCount() {
		return mostDownloadedCount;
	}

	public void setMostDownloadedCount(long mostDownloadedCount) {
		this.mostDownloadedCount = mostDownloadedCount;
	}

	public InstitutionalItemVersion getMostDownloadedItemVersion() {
		return mostDownloadedItemVersion;
	}

	public void setMostDownloadedItemVersion(
			InstitutionalItemVersion mostDownloadedItemVersion) {
		this.mostDownloadedItemVersion = mostDownloadedItemVersion;
	}
	
	
	public List<InstitutionalItemVersionDownloadCount> getSponsorPublications() {
		return sponsorPublications;
	}
	
	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}
	
	public Long getMostDownloadCount() {
		return mostDownloadCount;
	}
	
	public String getSortElement() {
		return sortElement;
	}

	public void setSortElement(String sortElement) {
		this.sortElement = sortElement;
	}
	
	public String getSortType() {
		return sortType;
	}


	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
}
