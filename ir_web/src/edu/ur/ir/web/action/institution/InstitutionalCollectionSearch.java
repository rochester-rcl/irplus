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

package edu.ur.ir.web.action.institution;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.SearchResults;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSearchService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.web.table.Pager;

/**
 * Action to allow for searching institutional collection information.
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalCollectionSearch extends Pager{
	
	/* eclipse generated id */
	private static final long serialVersionUID = 9100655709322426569L;

	/* Search service for user information */
	private InstitutionalCollectionSearchService institutionalCollectionSearchService;
	
	/* Query for institutional collection search */
	private String query;

	/*  Get the logger for this class */
	private static final Logger log = Logger.getLogger(InstitutionalCollectionSearch.class);
	
	/*  User as result of search  */
	private List<InstitutionalCollection> collections;
	
	/* total number of users */
	private int totalHits;

	/* Repository service */
	private RepositoryService repositoryService;

	/* Row to start retrieving the results  */
	private int rowStart = 0;
	
	/* Row End */
	private int rowEnd;
	
	/* view type  */
	private String viewType = "search";
	
	/* indicates this the first time the user viewing the search */
	private boolean searchInit = false;
	
	/* current parent collection id */
	private Long parentCollectionId;



	/**
	 * Default constructor
	 */
	public InstitutionalCollectionSearch()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;	
	}
	
	/**
	 * Get the users found with the specified query
	 * @return
	 */
	public String execute()
	{
		log.debug("query = " + query);
		log.debug("Institutional collection search RowStart = " + rowStart
	    		+ "   numberOfResultsToShow=" + numberOfResultsToShow  );
		
		if( query != null && !query.trim().equals(""))
		{
		    Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		    File collectionFolder = new File(repo.getInstitutionalCollectionIndexFolder());

		    rowEnd = rowStart + numberOfResultsToShow;
	    
		    SearchResults<InstitutionalCollection> result = institutionalCollectionSearchService.search(collectionFolder, 
				query, 
				rowStart, 
	    		numberOfResultsToShow);
		
		    collections = result.getObjects();

		    totalHits = result.getTotalHits();
		
		    if(rowEnd > totalHits)
		    {
			    rowEnd = totalHits;
		    }
		}

		return SUCCESS;
	}
	

	/**
	 * Get the total hits.
	 * 
	 * @see edu.ur.ir.web.table.Pager#getTotalHits()
	 */
	public int getTotalHits() {
		return totalHits;
	}
	
	/**
	 * Get the query.
	 * 
	 * @return
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * Set the query.
	 * 
	 * @param query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Get the row start.
	 * 
	 * @see edu.ur.ir.web.table.Pager#getRowStart()
	 */
	public int getRowStart() {
		return rowStart;
	}

	/**
	 * Set the row start.
	 * 
	 * @see edu.ur.ir.web.table.Pager#setRowStart(int)
	 */
	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}

	/**
	 * Get the collections.
	 * 
	 * @return
	 */
	public List<InstitutionalCollection> getCollections() {
		return collections;
	}

	/**
	 * Get the row end.
	 * 
	 * @return
	 */
	public int getRowEnd() {
		return rowEnd;
	}

	/**
	 * Set the institutional collection search service.
	 * 
	 * @param institutionalCollectionSearchService
	 */
	public void setInstitutionalCollectionSearchService(
			InstitutionalCollectionSearchService institutionalCollectionSearchService) {
		this.institutionalCollectionSearchService = institutionalCollectionSearchService;
	}

	/**
	 * Set the repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	public boolean getSearchInit() {
		return searchInit;
	}

	public String getViewType() {
		return viewType;
	}

	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

}
