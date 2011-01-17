/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.ir.web.action.group;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.SearchResults;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupSearchService;
import edu.ur.ir.web.table.Pager;

/**
 * User group search.
 * 
 * @author Nathan Sarr
 *
 */
public class UserGroupSearch extends Pager{
	
	/* eclipse generated id */
	private static final long serialVersionUID = -8043365727284355067L;

	/* Search service for user information */
	private UserGroupSearchService userGroupSearchService;

	/* Query for institutional collection search */
	private String query;

	/*  Get the logger for this class */
	private static final Logger log = Logger.getLogger(UserGroupSearch.class);
	
	/*  User as result of search  */
	private List<IrUserGroup> userGroups;
	
	/* total number of hits */
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
	
	/**
	 * Default constructor
	 */
	public UserGroupSearch()
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
		
		    File userGroupIndexFolder = new File(repo.getUserGroupIndexFolder());

		    rowEnd = rowStart + numberOfResultsToShow;
	    
		    log.debug("user group search service = " + userGroupSearchService);
		    SearchResults<IrUserGroup> result = userGroupSearchService.search(userGroupIndexFolder, 
				query, 
				rowStart, 
	    		numberOfResultsToShow);
		
		    userGroups = result.getObjects();

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
	 * Get the row end.
	 * 
	 * @return
	 */
	public int getRowEnd() {
		return rowEnd;
	}


	/**
	 * Set the repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	/**
	 * Determine if this is the initial search.
	 * 
	 * @return true if this is the search initialization
	 */
	public boolean getSearchInit() {
		return searchInit;
	}

	/**
	 * Get the view type 
	 * 
	 * @return
	 */
	public String getViewType() {
		return viewType;
	}
	
	/**
	 * Get the user groups
	 * 
	 * @return
	 */
	public List<IrUserGroup> getUserGroups() {
		return userGroups;
	}
	
	/**
	 * Set the search service.
	 * 
	 * @param userGroupSearchService
	 */
	public void setUserGroupSearchService(
			UserGroupSearchService userGroupSearchService) {
		this.userGroupSearchService = userGroupSearchService;
	}



}
