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


package edu.ur.ir.institution;

import java.io.File;
import java.io.Serializable;

import edu.ur.ir.SearchResults;

/**
 * Interface to perform searching over the institutional collection index. 
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalCollectionSearchService extends Serializable {
	
	/**
	 * Returns search results for institutional collections.
	 * 
	 * @param institutionalCollectionIndexFolder - folder for the institutional collections
	 * @param query - query to execute
	 * @param offset - offset to start at
	 * @param numResults - number of results.
	 * 
	 * @return - set of institutional collections found for the query.
	 */
	public SearchResults<InstitutionalCollection> search(File institutionalCollectionIndexFolder, String query, int offset, int numResults);

}
