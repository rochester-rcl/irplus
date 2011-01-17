/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.groupspace;

import java.io.File;
import java.io.Serializable;

import edu.ur.ir.SearchResults;

/**
 * Search the group workspace index
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceSearchService extends Serializable {
	
	/**
	 * Returns search results for group workspaces.
	 * 
	 * @param indexFolder - folder for the group workspaces index
	 * @param query - query to execute
	 * @param offset - offset to start at
	 * @param numResults - number of results.
	 * 
	 * @return - set of group workspaces found for the query.
	 */
	public SearchResults<GroupWorkspace> search(File indexFolder, String query, int offset, int numResults);


}
