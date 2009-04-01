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

package edu.ur.ir.web.action.person;

import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameService;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

public class RepositoryPersonNameBrowse extends Pager {

	/** eclipse generated id */
	private static final long serialVersionUID = -7121226780091062392L;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(RepositoryPersonNameBrowse.class);
	
	/** List of characters/options that can be selected */
	private String[] alphaList = new String[]{
			"All", "0-9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	};
	
	/** currently selected option */
	private String selectedAlpha ="All";

	/** service for dealing with person name information */
	private PersonNameService personNameService;
	

	/** List of person names found */
	private List<PersonName> personNames;
	


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
	public RepositoryPersonNameBrowse()
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
		    
		    personNames = personNameService.getPersonNamesOrderByLastName(rowStart, 
		    		numberOfResultsToShow,  OrderType.getOrderType(sortType));
		    totalHits = personNameService.getCount().intValue();
		}
		else if (selectedAlpha.equals("0-9"))
		{
			personNames = personNameService.getPersonNamesBetweenChar(rowStart, numberOfResultsToShow, '0', '9', OrderType.getOrderType(sortType));
			totalHits = personNameService.getCount('0', '9').intValue();
		}
		else
		{
			personNames = personNameService.getPersonNamesByLastNameChar(rowStart, numberOfResultsToShow, selectedAlpha.charAt(0), OrderType.getOrderType(sortType));
			totalHits = personNameService.getCount(selectedAlpha.charAt(0)).intValue();
			return SUCCESS;
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


	public List<PersonName> getPersonNames() {
		return personNames;
	}

	public void setPersonNames(List<PersonName> personNames) {
		this.personNames = personNames;
	}

	public PersonNameService getPersonNameService() {
		return personNameService;
	}

	public void setPersonNameService(PersonNameService personNameService) {
		this.personNameService = personNameService;
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
