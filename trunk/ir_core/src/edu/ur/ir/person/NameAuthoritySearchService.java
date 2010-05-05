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

package edu.ur.ir.person;

import java.io.File;
import java.io.Serializable;

import edu.ur.ir.SearchResults;

/**
 * Search person name information.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface NameAuthoritySearchService extends Serializable{

	/**
	 * Returns search results for the names.  This searches all names for all users but returns the
	 * authoritative name which is linked to all other names for the user
	 * 
	 * @param index where the authority index folder is located
	 * @param query - Query to execute
	 * @param offset - offset from original position to perform the search
	 * @param numResults - maximum number of results to return.
	 * 
	 * @return the search results.
	 */
	public SearchResults<PersonNameAuthority> search(File authorityIndexFolder, String query, int offset, int numResults);
	

}
