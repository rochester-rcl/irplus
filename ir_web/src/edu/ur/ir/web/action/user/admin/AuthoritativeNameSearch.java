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


package edu.ur.ir.web.action.user.admin;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.SearchResults;
import edu.ur.ir.person.NameAuthoritySearchService;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;


/**
 * Execute a search for authoritative name for user.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class AuthoritativeNameSearch extends ActionSupport  {

	/** Eclipse generated id. */
	private static final long serialVersionUID = 2953451341923955277L;

	/** Search service for name information */
	private NameAuthoritySearchService nameAuthoritySearchService;
	
	/** Query for user search */
	private String query;

	/**  Get the logger for this class */
	private static final Logger log = LogManager.getLogger(AuthoritativeNameSearch.class);
	
	/**  Person name authorities as result of search  */
	private List<PersonNameAuthority> nameAuthorities;
	
	/** total number of Person name authorities */
	private Integer totalHits;

	/** Repository service */
	private RepositoryService repositoryService;
	
	/** Service for loading personal data.  */
	private UserService userService;
	
	/** Row to start retrieving the results  */
	private int rowStart = 0;
	
	/** total number of rows to show  */
	private int numberOfResultsToShow;
	
	/** Id of the user performing search  */
	private Long userId;
	
	/** User being edited */
	private IrUser irUser;
	

	/**
	 * Searches for the person name authorities
	 */
	public String execute()
	{
		irUser = userService.getUser(userId, false);
		
		Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		File nameIndexFolder = new File(repo.getNameIndexFolder());

		if ((query == null) || (query.equals(""))) {
			query = "a";
		}
		
		if( numberOfResultsToShow <= 0 )
		{
			numberOfResultsToShow = 1;
		}
		
		int rowEnd = rowStart + (numberOfResultsToShow - 1);
		log.debug("*** rowStart = " + rowStart );
		log.debug("*** numberOfResultsToShow = " + numberOfResultsToShow );
		log.debug("*** rowEnd = " + rowEnd );
		
		SearchResults<PersonNameAuthority> nameSearchResults = nameAuthoritySearchService.search(nameIndexFolder, query, rowStart, rowEnd);
		nameAuthorities = nameSearchResults.getObjects();
		totalHits = nameSearchResults.getTotalHits();
		
		return SUCCESS;
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
	 * Get Service to search for person name 
	 * 
	 * @return
	 */
	public NameAuthoritySearchService getNameAuthoritySearchService() {
		return nameAuthoritySearchService;
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
	 * Get the total number of results
	 * 
	 * @return
	 */
	public Integer getTotalHits() {
		return totalHits;
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

	/**
	 * Set the name authority results of  search
	 * 
	 * @param nameAuthorities
	 */
	public void setNameAuthorities(List<PersonNameAuthority> nameAuthorities) {
		this.nameAuthorities = nameAuthorities;
	}

	public int getNumberOfResultsToShow() {
		return numberOfResultsToShow;
	}

	public void setNumberOfResultsToShow(int numberOfResultsToShow) {
		this.numberOfResultsToShow = numberOfResultsToShow;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public IrUser getIrUser() {
		return irUser;
	}


}
