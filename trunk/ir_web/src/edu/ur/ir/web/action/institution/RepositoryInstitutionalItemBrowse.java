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

package edu.ur.ir.web.action.institution;

import java.util.List;

import org.apache.log4j.Logger;


import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.repository.Repository;

import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Browse for institutional items.
 * 
 * @author Nathan Sarr
 *
 */
public class RepositoryInstitutionalItemBrowse extends Pager {

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(RepositoryInstitutionalItemBrowse.class);
	
	/** List of characters/options that can be selected */
	private String[] alphaList = new String[]{
			"All", "0-9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	};
	
	/** currently selected option */
	private String selectedAlpha ="All";
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -6584343467238699904L;
	
	/** Service for accessing institutional collections*/
	private InstitutionalItemService institutionalItemService;
	
	/** List of institutional items found from searching */
	private List<InstitutionalItem> institutionalItems;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "name";

	/** Total number of institutional items*/
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** parent id of the collection */
	private long parentCollectionId = 0l;
	
	/** Indicates this is a browse */
	private String viewType = "browse";
	
	/** Default constructor */
	public RepositoryInstitutionalItemBrowse()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 20;
	}
	
	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	/**
	 * Browse researcher
	 * 
	 * @return
	 */
	public String browseRepositoryItems() {
		log.debug("selected Alpha = " + selectedAlpha);
		rowEnd = rowStart + numberOfResultsToShow;
		if( selectedAlpha == null || selectedAlpha.equals("All") || selectedAlpha.trim().equals(""))
		{
		    
		    institutionalItems = institutionalItemService.getRepositoryItemsOrderByName(rowStart, 
		    		numberOfResultsToShow, Repository.DEFAULT_REPOSITORY_ID, OrderType.getOrderType(sortType));
		    totalHits = institutionalItemService.getCount(Repository.DEFAULT_REPOSITORY_ID).intValue();
		}
		else if (selectedAlpha.equals("0-9"))
		{
			institutionalItems = institutionalItemService.getRepositoryItemsBetweenChar(rowStart, numberOfResultsToShow, 
					Repository.DEFAULT_REPOSITORY_ID, '0', '9', OrderType.getOrderType(sortType));
			totalHits = institutionalItemService.getCount(Repository.DEFAULT_REPOSITORY_ID, '0', '9').intValue();
		}
		else
		{
			institutionalItems = institutionalItemService.getRepositoryItemsByChar(rowStart, numberOfResultsToShow, Repository.DEFAULT_REPOSITORY_ID, selectedAlpha.charAt(0), OrderType.getOrderType(sortType));
			totalHits = institutionalItemService.getCount(Repository.DEFAULT_REPOSITORY_ID, selectedAlpha.charAt(0)).intValue();
		}
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
		
		return SUCCESS;
	}
	
	/**
	 * Get total number of researchers
	 * 
	 * @see edu.ur.ir.web.table.Pager#getTotalHits()
	 */
	public int getTotalHits() {		
		return totalHits;
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


	public List<InstitutionalItem> getInstitutionalItems() {
		return institutionalItems;
	}

	public void setInstitutionalItems(List<InstitutionalItem> institutionalItems) {
		this.institutionalItems = institutionalItems;
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public long getParentCollectionId() {
		return parentCollectionId;
	}

	public void setParentCollectionId(long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	public String[] getAlphaList() {
		return alphaList;
	}

	public void setAlphaList(String[] alphaList) {
		this.alphaList = alphaList;
	}

	public String getSelectedAlpha() {
		return selectedAlpha;
	}

	public void setSelectedAlpha(String selectedAlpha) {
		this.selectedAlpha = selectedAlpha;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}



}
