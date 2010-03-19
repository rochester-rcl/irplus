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

package edu.ur.ir.institution;

import edu.ur.persistent.CommonPersistent;
import edu.ur.order.Orderable;

/**
 * This class represents a link for an institutional collection.
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalCollectionLink extends CommonPersistent implements Orderable{

	/** Eclipse generated id */
	private static final long serialVersionUID = -1972658002262680177L;
	
	/** URL value specified by the user  */
	private String url;
	
	/** Order for the links */
	private int order;	
	
	/** Institutional collection this link belongs to */
	private InstitutionalCollection institutionalCollection;
	
    /**
     * Package protected constructor
     */
    InstitutionalCollectionLink() {}
	
	/**
	 * Public link for url for a collection
	 * 
	 * @param name - name of the url
	 * @param url - url value
	 * @param order - numeric order
	 * @param collection - collection this link belongs to
	 */
	public InstitutionalCollectionLink(String name, String url, int order, InstitutionalCollection collection)
	{
		this.name = name;
		this.url = url;
		this.order = order;
		this.institutionalCollection = collection;
	}
	
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof InstitutionalCollectionLink)) return false;

		final InstitutionalCollectionLink other = (InstitutionalCollectionLink) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		if( ( institutionalCollection != null && !institutionalCollection.equals(other.getInstitutionalCollection()) ) ||
			( institutionalCollection == null && other.getInstitutionalCollection() != null ) ) return false;

		return true;
	}
	
	public int hashCode()
	{
		int value = 0;
		value =  name == null ? 0 : name.hashCode();
		value += institutionalCollection == null ? 0 : institutionalCollection.hashCode();
		return value;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[id = ");
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append( " url = ");
		sb.append(url);
		sb.append(" order = ");
		sb.append(order);
		sb.append("]");
		return sb.toString();
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

	public void setOrder(int order) {
		this.order = order;
	}

	public InstitutionalCollection getInstitutionalCollection() {
		return institutionalCollection;
	}

	void setInstitutionalCollection(
			InstitutionalCollection institutionalCollection) {
		this.institutionalCollection = institutionalCollection;
	}

}
