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

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.SearchResults;
import edu.ur.ir.person.NameAuthoritySearchService;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.web.table.Pager;


/**
 * Execute a search for a name.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class PersonNameSearch extends Pager  {

	/** Eclipse generated id. */
	private static final long serialVersionUID = -6787854950835737774L;

	/** Search service for name information */
	private NameAuthoritySearchService nameAuthoritySearchService;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(PersonNameSearch.class);
	
	/**  Person name authorities as result of search  */
	private List<PersonNameAuthority> nameAuthorities;
	
	/** Query for user search */
	private String query;

	/** total number of users */
	private int totalHits;

	/** Repository service */
	private RepositoryService repositoryService;

	/** Row to start retrieving the results  */
	private int rowStart = 0;
	
	/** Row End */
	private int rowEnd;
	
	/**
	 * Default constructor
	 * 
	 * Set the number of results to show on each page
	 * and the number of pages to show
	 */
	public PersonNameSearch() {
		numberOfResultsToShow = 50;
		numberOfPagesToShow = 10;		
	}
	
	/**
	 * Searches for the person name authorities
	 */
	public String execute()
	{
		log.debug("Exceucting search for persons");
		getSearchResults();
		
		return SUCCESS;
	}

	/**
	 * Get the users found with the specified query
	 * @return
	 */
	public List<PersonNameAuthority> getSearchResults() 
	{

		log.debug("Person search RowStart = " + rowStart
	    		+ "   numberOfResultsToShow=" + numberOfResultsToShow  );
		
		Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		File nameIndexFolder = new File(repo.getNameIndexFolder().getFullPath());
		
		rowEnd = rowStart + numberOfResultsToShow;
	    
		SearchResults<PersonNameAuthority> nameSearchResults = nameAuthoritySearchService.search(nameIndexFolder, query, rowStart, 
	    		numberOfResultsToShow);
		
		nameAuthorities = nameSearchResults.getObjects();

		totalHits = nameSearchResults.getTotalHits();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return nameAuthorities;
		
	}

	/**
	 * Get query for searching the person names
	 * 
	 * @return
	 */
	public String getQuery() {
		return query;	
	}

	/**
	 * Set query for searching the person names
	 * 
	 * @param query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Set Service to search for person name
	 * 
	 * @param nameAuthoritySearchService
	 */
	public void setNameAuthoritySearchService(
			NameAuthoritySearchService nameAuthoritySearchService) {
		this.nameAuthoritySearchService = nameAuthoritySearchService;
	}

	/**
	 * Get the name authority results
	 *  
	 * @return
	 */
	public List<PersonNameAuthority> getNameAuthorities() {
		return nameAuthorities;
	}

	/**
	 * Get start row to retrieve results
	 * 
	 * @return
	 */
	public int getRowStart() {
		return rowStart;
	}

	/**
	 * Set start row to retrieve results
	 * 
	 * @param rowStart
	 */
	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}

	/**
	 * Get the total number of results
	 * 
	 * @return
	 */
	public int getTotalHits() {
		return totalHits;
	}

	/**
	 * Set the total number of results
	 * 
	 * @param totalHits
	 */
	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	/**
	 * Set repository service
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public int getNumberOfResultsToShow() {
		return numberOfResultsToShow;
	}

	public void setNumberOfResultsToShow(int numberOfResultsToShow) {
		this.numberOfResultsToShow = numberOfResultsToShow;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

}
