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

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.web.action.person.CollectionPersonNameBrowse;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Browse for collection sponsors.
 * 
 * @author Nathan Sarr
 *
 */
public class CollectionSponsorNameBrowse extends Pager{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 9202520102243983587L;

	/**  Get the logger for this class */
	private static final Logger log = LogManager.getLogger(CollectionPersonNameBrowse.class);
	
	/** List of characters/options that can be selected */
	private String[] alphaList = new String[]{
			"All", "0-9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	};
	
	/** currently selected option */
	private String selectedAlpha ="All";
	
	/** Service for dealing with sponsor information */
	private SponsorService sponsorService;

	/** Service for dealing with institutional collections */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** List of person names found*/
	private List<Sponsor> sponsors;

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
	private String viewType = "browseSponsorName";
	
	private int rowEnd;
	
	/** Path for a the set of collections */
	private List<InstitutionalCollection> collectionPath;
	
	/** repository object */
	private Repository repository;
	
	/** Service for dealing with repositories */
	private RepositoryService repositoryService;
	
	/** Default constructor */
	public CollectionSponsorNameBrowse()
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
	 * Browse people within the collection
	 * 
	 * @return
	 */
	public String execute() {
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		institutionalCollection = institutionalCollectionService.getCollection(collectionId, false);
		if( institutionalCollection == null )
		{
			return "collectionNotFound";
		}
		collectionPath = institutionalCollectionService.getPath(institutionalCollection);
		log.debug("selected Alpha = " + selectedAlpha);
		rowEnd = rowStart + numberOfResultsToShow;
		log.debug("looking at collection " + institutionalCollection);
		
		if( selectedAlpha == null || selectedAlpha.equals("All") || selectedAlpha.trim().equals(""))
		{
		    sponsors = sponsorService.getCollectionSponsorsOrderByName(rowStart, numberOfResultsToShow, institutionalCollection, OrderType.getOrderType(sortType));
		    totalHits = sponsorService.getCount(institutionalCollection).intValue();
		}
		else if (selectedAlpha.equals("0-9"))
		{
			sponsors = sponsorService.getCollectionSponsorsBetweenChar(rowStart, numberOfResultsToShow, 
					institutionalCollection, '0', '9', OrderType.getOrderType(sortType));
			
			totalHits = sponsorService.getCount(institutionalCollection, '0', '9').intValue();
		}
		else
		{
			sponsors = sponsorService.getCollectionSponsorsByChar(rowStart, numberOfResultsToShow, institutionalCollection, selectedAlpha.charAt(0), OrderType.getOrderType(sortType));
			totalHits = sponsorService.getCount(institutionalCollection, selectedAlpha.charAt(0)).intValue();
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

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	public List<Sponsor> getSponsors() {
		return sponsors;
	}

	public void setSponsors(List<Sponsor> sponsors) {
		this.sponsors = sponsors;
	}
	
	public SponsorService getSponsorService() {
		return sponsorService;
	}

	public void setSponsorService(SponsorService sponsorService) {
		this.sponsorService = sponsorService;
	}

}
