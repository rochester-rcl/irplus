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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.FileSystem;
import edu.ur.ir.SearchResults;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserWorkspaceSearchService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Execute a search for a user.
 * 
 * @author Nathan Sarr
 *
 */
public class UserWorkspaceSearch extends ActionSupport implements UserIdAware {

	/** Eclipse generated id. */
	private static final long serialVersionUID = 5890310867893139594L;
	
	/** Search service for user information */
	private UserWorkspaceSearchService userWorkspaceSearchService;
	
	/** User service for accessing user information  */
	private UserService userService;
	
	/** Query for user search */
	private String query;
	
	/** Results gathered from the search */
	private SearchResults<FileSystem> userWorkspaceSearchResults = new SearchResults<FileSystem>();
	
	/** offset to start from when asking for the results */
	private int offset = 0;
	
	/** number of results to fetch */
	private int numResults = 20;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(UserWorkspaceSearch.class);
	
	/** Id of the user executing the search */
	private Long userId;
	
	
	
	public String execute()
	{
		log.debug("executing search");
		IrUser user = userService.getUser(userId, false);
		
		if( log.isDebugEnabled())
		{
		     log.debug("Executing query " + query + " offset = " + offset + " num results = " + 
			     numResults);
		}
		
		if( user.getPersonalIndexFolder() != null && !user.getPersonalIndexFolder().equals(""))
		{
		    File personalIndexFolder = new File(user.getPersonalIndexFolder());
		    userWorkspaceSearchResults = userWorkspaceSearchService.search(personalIndexFolder, query, offset, numResults);
		}
		return SUCCESS;
	}

	public UserWorkspaceSearchService getUserWorkspaceSearchService() {
		return userWorkspaceSearchService;
	}

	public void setUserWorkspaceSearchService(UserWorkspaceSearchService userSearchService) {
		this.userWorkspaceSearchService = userSearchService;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	
	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * User service for accessing user information.
	 * 
	 * @return user service.
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Set the user service to access user information.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * User search results.  Results gathered from executing the search.
	 * 
	 * @return
	 */
	public SearchResults<FileSystem> getUserWorkspaceSearchResults() {
		return userWorkspaceSearchResults;
	}

	/**
	 * Set the user search results.
	 * 
	 * @param userWorkspaceSearchResults
	 */
	public void setUserWorkspaceSearchResults(SearchResults<FileSystem> userWorkspaceSearchResults) {
		this.userWorkspaceSearchResults = userWorkspaceSearchResults;
	}

	/**
	 * Get the offset to start from in the list of results.
	 * 
	 * @return
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Set the offset to start from in the list of results.
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Get the number of results to fetch.
	 * 
	 * @return
	 */
	public int getNumResults() {
		return numResults;
	}

	/**
	 * Set the number of results to fetch.
	 * 
	 * @param numResults
	 */
	public void setNumResults(int numResults) {
		this.numResults = numResults;
	}

}
