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

import edu.ur.ir.FileSystem;
import edu.ur.ir.SearchResults;

/**
 * Search user information.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserWorkspaceSearchService {

	/**
	 * Returns search results for the user.
	 * 
	 * @param user - user to execute the query for.
	 * @param query - Query to execute
	 * @param offset - offset from original position to perform the search
	 * @param numResults - maximum number of results to return.
	 * 
	 * @return the search results.
	 */
	public SearchResults<FileSystem> search(File personalIndexFolder, String query, int offset, 
			int numResults);

}
