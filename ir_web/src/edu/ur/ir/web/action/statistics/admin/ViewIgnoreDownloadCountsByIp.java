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

package edu.ur.ir.web.action.statistics.admin;

import java.util.LinkedList;
import java.util.List;

import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.statistics.IpDownloadCount;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * @author NathanS
 *
 */
public class ViewIgnoreDownloadCountsByIp  extends Pager{

	/** eclipse generated id */
	private static final long serialVersionUID = -2182591934182790818L;

	/** Service to help with download information */
	private DownloadStatisticsService downloadStatisticsService;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "desc";

	/** Total number of ip addresses to ignore  */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** list of download counts found */
	List<IpDownloadCount> downloadCounts = new LinkedList<IpDownloadCount>();
	


	/**
	 * Default constructor
	 */
	public ViewIgnoreDownloadCountsByIp()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}
	
	/**
	 * Load the list of download counts for viewing.
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		rowEnd = rowStart + numberOfResultsToShow;
		downloadCounts = downloadStatisticsService.getIgnoreIpOrderByDownloadCount(rowStart, numberOfResultsToShow, OrderType.getOrderType(sortType));
		totalHits = downloadStatisticsService.getGroupByIgnoreIpAddressCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
		
		return SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.ir.web.table.Pager#getTotalHits()
	 */
	public int getTotalHits() {
		return totalHits;
	}
	
	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}
	
	public List<IpDownloadCount> getDownloadCounts() {
		return downloadCounts;
	}

}
