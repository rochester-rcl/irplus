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
 * Represents a facet result.
 * 
 * @author Nathan Sarr
 *
 */
public class FacetResult {
	
	/** Number of hits for that facet */
	private Integer hits;
	
	/** type of facet for example Author, Language, Subject etc.*/
	private String field;
	
	/** Name of the facet */
	private String facetName;
	
	/**
	 * Default constructor
	 * 
	 * @param hits - number of hits for this facet
	 * @param facetName - name of the facet.
	 * @param originalQuery - original query executed.
	 */
	public FacetResult(int hits, String field, String facetName)
	{
		this.hits = hits;
		this.facetName = facetName;
		this.field = field;
	}

	/**
	 * Get the number of hits.
	 * 
	 * @return
	 */
	public int getHits() {
		return hits;
	}

	/**
	 * Get the name of the facet.
	 * 
	 * @return
	 */
	public String getFacetName() {
		return facetName;
	}
	
	/**
	 * Get the type of facet.
	 * 
	 * @return
	 */
	public String getField() {
		return field;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ hits = ");
		sb.append(hits);
		sb.append(" facet Type = ");
		sb.append(field);
		sb.append(" facet name = ");
		sb.append(facetName);
		sb.append( "]");
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int hashCode = 0;
		hashCode += hits == null ? 0 : hits.hashCode();
		hashCode += facetName == null ? 0 : facetName.hashCode();
		hashCode += field == null ? 0 : field.hashCode();
		return hashCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof FacetResult)) return false;

		final FacetResult other = (FacetResult) o;

		if( ( facetName != null && !facetName.equals(other.getFacetName()) ) ||
			( facetName == null && other.getFacetName() != null ) ) return false;

		if( ( field != null && !field.equals(other.getField()) ) ||
			( field == null && other.getField() != null ) ) return false;

		return true;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}



}
