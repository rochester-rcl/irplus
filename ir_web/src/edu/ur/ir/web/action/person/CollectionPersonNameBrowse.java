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

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameService;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

public class CollectionPersonNameBrowse extends Pager {

	/** Eclipse generated id */
	private static final long serialVersionUID = -6924511274479254476L;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(CollectionPersonNameBrowse.class);
	
	/** List of characters/options that can be selected */
	private String[] alphaList = new String[]{
			"All", "0-9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	};
	
	/** currently selected option */
	private String selectedAlpha ="All";
	
	/** Service for accessing institutional collections*/
	private PersonNameService personNameService;

	/** Service for dealing with institutional collections */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** List of person names found*/
	private List<PersonName> personNames;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "name";

	/** Total number of institutional items*/
	private int totalHits;
	
	/** id of the collection */
	private long collectionId = 0l;
	
	/** collection being looked at  */
	private InstitutionalCollection institutionalCollection;
	
	/** Indicates this is a browse */
	private String viewType = "browse";
	
	private int rowEnd;
	
	/** Path for a the set of collections */
	private List<InstitutionalCollection> collectionPath;
	
	/** Default constructor */
	public CollectionPersonNameBrowse()
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
	public String browseCollectionItems() {
		
		institutionalCollection = institutionalCollectionService.getCollection(collectionId, false);
		collectionPath = institutionalCollectionService.getPath(institutionalCollection);
		log.debug("selected Alpha = " + selectedAlpha);
		rowEnd = rowStart + numberOfResultsToShow;
		log.debug("looking at collection " + institutionalCollection);
		
		if( selectedAlpha == null || selectedAlpha.equals("All") || selectedAlpha.trim().equals(""))
		{
		    
		    personNames = personNameService.getCollectionPersonNamesOrderByLastName(rowStart, numberOfResultsToShow, institutionalCollection, OrderType.getOrderType(sortType));
		    totalHits = personNameService.getCount(institutionalCollection).intValue();
		}
		else if (selectedAlpha.equals("0-9"))
		{
			personNames = personNameService.getCollectionPersonNamesBetweenChar(rowStart, numberOfResultsToShow, 
					institutionalCollection, '0', '9', OrderType.getOrderType(sortType));
			totalHits = personNameService.getCount(institutionalCollection, '0', '9').intValue();
		}
		else
		{
			personNames = personNameService.getPersonNamesByChar(rowStart, numberOfResultsToShow, institutionalCollection, selectedAlpha.charAt(0), OrderType.getOrderType(sortType));
			totalHits = personNameService.getCount(institutionalCollection, selectedAlpha.charAt(0)).intValue();
		}
		
		log.debug("total hits = " + totalHits);

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


	public PersonNameService getPersonNameService() {
		return personNameService;
	}

	public void setPersonNameService(PersonNameService personNameService) {
		this.personNameService = personNameService;
	}

	public List<PersonName> getPersonNames() {
		return personNames;
	}

	public void setPersonNames(List<PersonName> personNames) {
		this.personNames = personNames;
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

	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	public InstitutionalCollection getInstitutionalCollection() {
		return institutionalCollection;
	}

	public void setInstitutionalCollection(InstitutionalCollection collection) {
		this.institutionalCollection = collection;
	}

	public long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(long collectionId) {
		this.collectionId = collectionId;
	}

	public List<InstitutionalCollection> getCollectionPath() {
		return collectionPath;
	}

	public void setCollectionPath(List<InstitutionalCollection> collectionPath) {
		this.collectionPath = collectionPath;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

}
