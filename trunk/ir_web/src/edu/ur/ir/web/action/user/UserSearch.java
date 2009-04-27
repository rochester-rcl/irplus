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


package edu.ur.ir.web.action.user;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.SearchResults;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserSearchService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.table.Pager;

/**
 * Search used for finding users.
 * 
 * @author Nathan Sarr
 *
 */
public class UserSearch extends Pager {

	/** Eclipse generated id. */
	private static final long serialVersionUID = 6166553828705898698L;

	/** Search service for user information */
	private UserSearchService userSearchService;
	
	/** Query for user search */
	private String query;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(UserSearch.class);
	
	/**  User as result of search  */
	private List<IrUser> users;
	
	/** total number of users */
	private int totalHits;

	/** Repository service */
	private RepositoryService repositoryService;

	/** Service for loading personal data.  */
	private UserService userService;
	
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
	public UserSearch() {
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;		
	}
	
	/**
	 * Searches for the person name authorities
	 */
	public String execute()
	{
		log.debug("Exceucting search for users");
		getSearchResults();
		
		return SUCCESS;
	}

	/**
	 * Get the users found with the specified query
	 * @return
	 */
	public List<IrUser> getSearchResults() 
	{

		log.debug("User search RowStart = " + rowStart
	    		+ "   numberOfResultsToShow=" + numberOfResultsToShow  );
		
		Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		File userFolder = new File(repo.getUserIndexFolder());

		rowEnd = rowStart + numberOfResultsToShow;
	    
		SearchResults<IrUser> result = userSearchService.search(userFolder, query, rowStart, 
	    		numberOfResultsToShow);
		
		users = result.getObjects();

		totalHits = result.getTotalHits();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return users;
		
	}
	
	/**
	 * Get query for searching the person names
	 * 
	 * @return
	 */
	public String getQuery() {
		return query;	}

	/**
	 * Set query for searching the person names
	 * 
	 * @param query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Get user service
	 * 
	 * @return
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Set user service
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
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
	 * Set the total number of results
	 * 
	 * @param totalHits
	 */
	public void setTotalHits(Integer totalHits) {
		this.totalHits = totalHits;
	}

	/**
	 * Get repository service
	 * 
	 * @return
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
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


	public void setUserSearchService(UserSearchService userSearchService) {
		this.userSearchService = userSearchService;
	}


	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public List<IrUser> getUsers() {
		return users;
	}



}
