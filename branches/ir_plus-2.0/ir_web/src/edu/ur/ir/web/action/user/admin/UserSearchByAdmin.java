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

import edu.ur.ir.web.action.user.UserSearch;

/**
 * Search user
 * 
 * @author Sharmila Ranganathan
 *
 */
public class UserSearchByAdmin extends UserSearch {

	/** Eclipse generated id. */
	private static final long serialVersionUID = -5116142022251008399L;

	/**
	 * Default constructor
	 * 
	 * Set the number of results to show on each page
	 * and the number of pages to show
	 */
	public UserSearchByAdmin() {
		numberOfResultsToShow = 50;
		numberOfPagesToShow = 10;
	}
	
	/**
	 * Get the users found with the specified query
	 * @return
	 */
	public String execute() 
	{
		getSearchResults();
		
		return SUCCESS;
	}



}
