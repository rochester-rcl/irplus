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

package edu.ur.ir.search;

/**
 * Filter used to execute a sub query on the main query.
 * 
 * @author Nathan Sarr
 *
 */
public class FacetFilter {
	
	/** filed in the index to search over  */
	private String field;
	
	/** sub query value to execute on the field */
	private String query;
	
	/** Display name for the facet  */
	private String displayName;
	
	/**
	 * Default constructor.
	 * 
	 * @param field
	 * @param query
	 * @param displayName
	 */
	public FacetFilter(String field, String query, String displayName)
	{
		this.field = field;
		this.query = query;
		this.displayName = displayName;
	}

	public String getField() {
		return field;
	}

	public String getQuery() {
		return query;
	}

	public String getDisplayName() {
		return displayName;
	}

}
