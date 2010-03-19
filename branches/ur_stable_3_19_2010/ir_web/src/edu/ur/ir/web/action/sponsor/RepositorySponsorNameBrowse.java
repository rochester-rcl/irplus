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

import org.apache.log4j.Logger;

import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.web.action.person.RepositoryPersonNameBrowse;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Allow a user to browse all sponsors for the repository.
 * 
 * @author Nathan Sarr
 *
 */
public class RepositorySponsorNameBrowse extends Pager{

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(RepositoryPersonNameBrowse.class);
	
	/** eclipse generated serial version id */
	private static final long serialVersionUID = 7404992323965723601L;
	
	/** List of characters/options that can be selected */
	private String[] alphaList = new String[]{
			"All", "0-9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	};
	
	/** currently selected option */
	private String selectedAlpha ="All";
	
	/** list of sponsors found */
	private List<Sponsor> sponsors = new LinkedList<Sponsor>();
	
	/** Service for dealing with sponsor information */
	private SponsorService sponsorService;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** Total number of sponsors */
	private int totalHits;
	
	/** repository object */
	private Repository repository;
	
	/** Service for dealing with repositories */
	private RepositoryService repositoryService;
	
	/** Row End */
	private int rowEnd;
	
	/** parent id of the collection */
	private long parentCollectionId = 0l;
	
	/** Indicates this is a browse by sponsor name*/
	private String viewType = "browseSponsorName";
		
	/** Sort on sponsor name element  */
	private String sortElement = "sponsorName";
	
	


	/** Default constructor */
	public RepositorySponsorNameBrowse()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 20;
	}
	
	/**
	 * Browse repository by sponsor
	 * 
	 * @return
	 */
	public String execute() 
	{
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		log.debug("selected Alpha = " + selectedAlpha);
		rowEnd = rowStart + numberOfResultsToShow;
		if( selectedAlpha == null || selectedAlpha.equals("All") || selectedAlpha.trim().equals(""))
		{
		    sponsors = sponsorService.getOrderByName(rowStart, numberOfResultsToShow, OrderType.getOrderType(sortType));
		    totalHits = sponsorService.getCount().intValue();
		}
		else if (selectedAlpha.equals("0-9"))
		{
			sponsors = sponsorService.getSponsorsByNameBetweenChar(rowStart, numberOfResultsToShow, '0', '9', OrderType.getOrderType(sortType));
			totalHits = sponsorService.getCount('0', '9').intValue();
		}
		else
		{
			sponsors = sponsorService.getByNameFirstChar(rowStart, numberOfResultsToShow, selectedAlpha.charAt(0), OrderType.getOrderType(sortType));
			totalHits = sponsorService.getCount(selectedAlpha.charAt(0)).intValue();
		}
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
		
		return SUCCESS;
	}
	
	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	public long getParentCollectionId() {
		return parentCollectionId;
	}

	public void setParentCollectionId(long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	public String getViewType() {
		return viewType;
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

	public int getTotalHits() {
		return totalHits;
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
	public String getSortElement() {
		return sortElement;
	}

	
	public void setSortElement(String sortElement) {
		this.sortElement = sortElement;
	}

}
