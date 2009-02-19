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

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.web.table.Pager;

/**
 * Class for browsing items within an institutional colleciton.
 * 
 * @author Nathan Sarr
 *
 */
public class CollectionInstitutionalItemBrowse extends Pager {

	/** Eclipse generated id */
	private static final long serialVersionUID = -6924511274479254476L;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(CollectionInstitutionalItemBrowse.class);
	
	/** List of characters/options that can be selected */
	private String[] alphaList = new String[]{
			"All", "0-9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	};
	
	/** currently selected option */
	private String selectedAlpha ="All";
	
	/** Service for accessing institutional collections*/
	private InstitutionalItemService institutionalItemService;
	
	/** Service for dealing with institutional collections */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** List of researchers */
	private List<InstitutionalItem> institutionalItems;
	
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
	public CollectionInstitutionalItemBrowse()
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
		    
		    institutionalItems = institutionalItemService.getCollectionItemsOrderByName(rowStart, 
		    		numberOfResultsToShow, institutionalCollection, sortType);
		    totalHits = institutionalItemService.getCountForCollectionAndChildren(institutionalCollection).intValue();
		}
		else if (selectedAlpha.equals("0-9"))
		{
			institutionalItems = institutionalItemService.getCollectionItemsBetweenChar(rowStart, numberOfResultsToShow, 
					institutionalCollection, '0', '9', sortType);
			totalHits = institutionalItemService.getCount(Repository.DEFAULT_REPOSITORY_ID, '0', '9').intValue();
		}
		else
		{
			institutionalItems = institutionalItemService.getCollectionItemsByChar(rowStart, numberOfResultsToShow, institutionalCollection, selectedAlpha.charAt(0), sortType);
			log.debug("test hits = " + institutionalItemService.getCount(institutionalCollection, selectedAlpha.charAt(0)).intValue());
			totalHits = institutionalItemService.getCount(institutionalCollection, selectedAlpha.charAt(0)).intValue();
		}
		
		log.debug("institutionalItems size = " + institutionalItems.size());
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
