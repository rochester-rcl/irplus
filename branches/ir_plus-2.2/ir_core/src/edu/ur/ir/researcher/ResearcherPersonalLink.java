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

package edu.ur.ir.researcher;

import edu.ur.order.Orderable;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a personal link for the researcher.
 * 
 * @author Nathan Sarr
 *
 */
public class ResearcherPersonalLink extends BasePersistent implements Orderable {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 560020469821019205L;

	/** researcher this link belongs to */
	private Researcher researcher;
	
	/** url this link belongs to for the researcher */
	private String url;
	
	/** determines the order of the link */
	private int order;
	
	/** name given to the link */
	private String name;
	
	/** description given to the link */
	private String description;
	
	
	/**
	 * Package protected constructor
	 */
	ResearcherPersonalLink(){}
	
	/**
	 * Default constructor 
	 * 
	 * @param researcher - researcher who owns the link
	 * @param name - name of the link
	 * @param url - link to the resource
	 */
	public ResearcherPersonalLink(Researcher researcher, String name, String url, int order)
	{
		setOrder(order);
		setUrl(url);
		setResearcher(researcher);
		setName(name);
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ name = ");
		sb.append(name);
		sb.append(" id = ");
		sb.append(id);
		sb.append(" url = ");
		sb.append(url);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Hash code for a personal link
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += getName() == null ? 0 : getName().hashCode();
		value += researcher == null ? 0 : researcher.hashCode();
		return value;
		
	}
	
	/**
	 * Equals method for a personal researcher link.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ResearcherPersonalLink)) return false;

		final ResearcherPersonalLink other = (ResearcherPersonalLink) o;

		if( (other.getName() != null && !other.getName().equals(getName())) ||
			(other.getName() == null && getName() != null )	) return false;
		
		if( (other.getResearcher() != null && !other.getResearcher().equals(getResearcher())) ||
			(other.getResearcher() == null && getResearcher() != null )	) return false;

		return true;
	}

	public Researcher getResearcher() {
		return researcher;
	}

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getOrder() {
		return order;
	}

	void setOrder(int order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
