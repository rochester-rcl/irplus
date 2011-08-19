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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.ur.ir.search.FacetFilter;
import edu.ur.ir.search.FacetResult;

/**
 * Helper which contains search related data.
 * 
 * @author Nathan Sarr
 *
 */
public class FacetSearchHelper implements Serializable{

	/** eclipse generated id.*/
	private static final long serialVersionUID = -3758633706897516873L;

	/** Hits found */
	private Set<Long> hitIds;
	
	/** total number of hits */
	private int hitSize;
	
	/** facets found  */
	private HashMap<String, Collection<FacetResult>> facets;
	
	/** Original query submitted */
	private String userQuery;
	
	/** Query executed, if it was fixed up */
	private String executedQuery;
	
	/** Set of facets the user has traversed */
	private List<FacetFilter> facetTrail;
	
	
	/**
	 * Default construtor
	 * 
	 * @param hitIds - ids for the searche entity 
	 * @param hitSize - total number of hits for the query
	 * @param facets - facets generated
	 * @param userQuery - query executed by the query
	 */
	public FacetSearchHelper(Set<Long> hitIds, 
			int hitSize,
			HashMap<String, Collection<FacetResult>> facets, 
			String userQuery)
	{
		this.hitSize = hitSize;
		this.hitIds = hitIds;
		this.facets = facets;
		this.userQuery = userQuery;
		this.facetTrail = new LinkedList<FacetFilter>();
	}

	/**
	 * Default constructor
	 * 
	 * @param hitIds - ids for the searche entity 
	 * @param hitSize - total number of hits for the query
	 * @param facets - facets generated
	 * @param userQuery - query executed by the query
	 * @param facetTrail - set of facets that have been selected by user
	 */
	public FacetSearchHelper(Set<Long> hitIds, 
			int hitSize,
			HashMap<String, Collection<FacetResult>> facets, 
			String userQuery, 
			List<FacetFilter> facetTrail)
	{
		this.hitSize = hitSize;
		this.hitIds = hitIds;
		this.facets = facets;
		this.userQuery = userQuery;
		this.facetTrail = facetTrail;
	}

	
	public HashMap<String, Collection<FacetResult>> getFacets() {
		return facets;
	}

	public String getUserQuery() {
		return userQuery;
	}
	
	public String toString()
	{
		StringBuffer sb  = new StringBuffer("[ FacetSearchHelper : \n");
		sb.append("Hits size = " + hitSize + " \n");
		sb.append("userQuery = " + userQuery + "\n");
		Set<String> keys = facets.keySet();
		Iterator<String> itr = keys.iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			Collection<FacetResult> results = facets.get(key);
			
			sb.append("*********************************** \n");
			sb.append("Printing facets for " + key + "\n");
			for(FacetResult facet : results)
			{
				sb.append(facet);
				sb.append("\n");
			}
			sb.append("*********************************** \n");
		}

		
		return sb.toString();
	}
	

	public int getHitSize() {
		return hitSize;
	}

	public Set<Long> getHitIds() {
		return hitIds;
	}


	public List<FacetFilter> getFacetTrail() {
		return facetTrail;
	}

	public void setFacetTrail(List<FacetFilter> facetTrail) {
		this.facetTrail = facetTrail;
	}

	public String getExecutedQuery() {
		return executedQuery;
	}

	public void setExecutedQuery(String executedQuery) {
		this.executedQuery = executedQuery;
	}

	

}
