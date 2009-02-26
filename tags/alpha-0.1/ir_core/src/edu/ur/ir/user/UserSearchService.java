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

package edu.ur.ir.user;

import java.io.File;

import edu.ur.ir.SearchResults;

public interface UserSearchService {
	
	/**
	 * Returns search results for the user.
	 * 
	 * @param userIndexFolder - location where the index folder is location 
	 * @param query - query to execute
	 * @param offset - offset to start at
	 * @param numResults - number of results.
	 * 
	 * @return - set of users found for the query.
	 */
	public SearchResults<IrUser> search(File userIndexFolder, String query, int offset, int numResults);

}
