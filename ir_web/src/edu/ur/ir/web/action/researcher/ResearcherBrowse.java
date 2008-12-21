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


package edu.ur.ir.web.action.researcher;

import java.util.List;

import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.web.table.Pager;

/**
 * Action to browse researcher
 *  
 * @author Sharmila Ranganathan
 *
 */
public class ResearcherBrowse extends Pager {

	/**Eclipse gernerated id */
	private static final long serialVersionUID = 8561277951764749268L;

	/** Researcher service */
	private ResearcherService researcherService;
	
	/** List of researchers */
	private List<Researcher> researchers;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "lastName";

	/** Total number of researchers */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Indicates this is a browse */
	private String viewType = "browse";
	
	/** Default constructor */
	public ResearcherBrowse()
	{
		numberOfResultsToShow = 100;
		numberOfPagesToShow = 20;
	}

	
	/**
	 * Browse researcher
	 * 
	 * @return
	 */
	public String browseResearcher() {

		rowEnd = rowStart + numberOfResultsToShow;

		researchers = researcherService.getResearchers(rowStart, numberOfResultsToShow, sortElement, sortType);
		totalHits = researcherService.getAllResearchers().size();
		
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

	public List<Researcher> getResearchers() {
		return researchers;
	}

	public ResearcherService getResearcherService() {
		return researcherService;
	}

	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
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


	public String getViewType() {
		return viewType;
	}


	public void setViewType(String viewType) {
		this.viewType = viewType;
	}


	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}


	public int getRowEnd() {
		return rowEnd;
	}


	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}
	
}
