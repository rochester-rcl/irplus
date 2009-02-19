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

package edu.ur.ir.web.action.item;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.SearchResults;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.person.NameAuthoritySearchService;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;


/**
 * Execute a search for a name.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class NameSearch extends Pager implements  UserIdAware, Preparable  {

	/** Eclipse generated id. */
	private static final long serialVersionUID = 2953451341923955277L;

	/** Search service for name information */
	private NameAuthoritySearchService nameAuthoritySearchService;
	
	/** Query for user search */
	private String query;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(NameSearch.class);
	
	/**  Person name authorities as result of search  */
	private List<PersonNameAuthority> nameAuthorities;
	
	/** Personal item being edited */
	private GenericItem item;
	
	/** Repository service */
	private RepositoryService repositoryService;
	
	/** Id of the item to add the files */
	private Long genericItemId;
	
	/** Service for loading personal data.  */
	private UserService userService;
	
	/** Id of the user performing search  */
	private Long userId;
	
	/** User performing search */
	private IrUser user;
	
	/** Service for dealing with item */
	private ItemService itemService;
	
	/** Id of parent collection */
	private Long parentCollectionId;

	/** Id of institutional item being edited */
	private Long institutionalItemId;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of affiliations  */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public  NameSearch()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Prepare method initializes the personal item
	 */
	public void prepare() {
		log.debug("Peronal ItemId = " + genericItemId);

		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false);

		}
		
	}
	
	/**
	 * Searches for the person name authorities
	 */
	public String execute()
	{
		Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		File nameIndexFolder = new File(repo.getNameIndexFolder().getFullPath());

		if ((query == null) || (query.equals(""))) {
			query = "a";
		}
		
		rowEnd = rowStart + numberOfResultsToShow;
	    
		SearchResults<PersonNameAuthority> nameSearchResults = nameAuthoritySearchService.search(nameIndexFolder, query, rowStart, numberOfResultsToShow);
		nameAuthorities = nameSearchResults.getObjects();
		totalHits = nameSearchResults.getTotalHits();
		
		// Helps to display [My Name] next to the searching users name in the search results
		if (userId != null) {
			user = userService.getUser(userId, false);
		}

		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public IrUser getUser() {
		return user;
	}

	public void setUser(IrUser user) {
		this.user = user;
	}

	public Long getGenericItemId() {
		return genericItemId;
	}

	public void setGenericItemId(Long genericItemId) {
		this.genericItemId = genericItemId;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public GenericItem getItem() {
		return item;
	}

	public void setItem(GenericItem item) {
		this.item = item;
	}

	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

}
