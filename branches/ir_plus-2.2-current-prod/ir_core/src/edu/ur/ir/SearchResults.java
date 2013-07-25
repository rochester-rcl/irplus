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

package edu.ur.ir;

import java.util.LinkedList;
import java.util.List;

import edu.ur.ir.search.FacetResult;

/**
 * This holds search results from a search
 * 
 * @author Nathan Sarr
 *
 * @param <T>
 */
public class SearchResults<T> {
	/**  Original query  */
	private String originalQuery;
	
	/** Set of file system objects to display*/
	private List<T> objects= new LinkedList<T>();
	
	/** total number of hits  */
	private int totalHits = 0;
	
	/** List of facets found */
	private List<FacetResult> facets = new LinkedList<FacetResult>();
	
	
	/**
	 * Get the set of file system objects found.
	 * 
	 * @return list of file system objects found
	 */
	public List<T> getObjects()
	{
		return objects;
	}
	
	/**
	 * Get the set of facets created.
	 * 
	 * @return
	 */
	public List<FacetResult> getFacets()
	{
		return facets;
	}

	/**
	 * Get the original query written by the user.
	 * 
	 * @return
	 */
	public String getOriginalQuery() {
		return originalQuery;
	}

	/**
	 * Set the orginal query executed by the user.
	 * 
	 * @param originalQuery
	 */
	public void setOriginalQuery(String originalQuery) {
		this.originalQuery = originalQuery;
	}

	/**
	 * Set the file system objects found 
	 * 
	 * @param objects
	 */
	public void setObjects(List<T> objects) {
		this.objects = objects;
	}

	/**
	 * Set the facets created.
	 * 
	 * @param facets
	 */
	public void setFacets(List<FacetResult> facets) {
		this.facets = facets;
	}
	
	/**
	 * Get the number of file system objects in the list
	 * 
	 * @return
	 */
	public int getNumberObjects()
	{
		return objects.size();
	}

	/**
	 * Get the total hits .
	 * 
	 * @return
	 */
	public int getTotalHits() {
		return totalHits;
	}

	/**
	 * Set the total hits.
	 * 
	 * @param totalHits
	 */
	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}


}
