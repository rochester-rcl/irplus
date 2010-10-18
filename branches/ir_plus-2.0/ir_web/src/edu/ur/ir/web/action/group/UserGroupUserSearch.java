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
package edu.ur.ir.web.action.group;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.web.action.user.UserSearch;

/**
 * Searching for users on the user group pages
 * 
 * @author Nathan Sarr
 *
 */
public class UserGroupUserSearch extends UserSearch implements Preparable{
	
	/** id of the user group */
	private Long id;
	
	/** Loaded user group */
	private IrUserGroup userGroup;
	
	/** user group service */
	private UserGroupService userGroupService;
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -9146778996348362304L;

	/**
	 * Default constructor
	 * 
	 * Set the number of results to show on each page
	 * and the number of pages to show
	 */
	public UserGroupUserSearch() {
		numberOfResultsToShow = 50;
		numberOfPagesToShow = 10;
	}
	
	public String execute()
	{
		getSearchResults();
	    return SUCCESS;	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void prepare() throws Exception {
		if( id != null )
		{
			userGroup = userGroupService.get(id, false);
		}
		
	}

	public IrUserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(IrUserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public UserGroupService getUserGroupService() {
		return userGroupService;
	}

	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

}
