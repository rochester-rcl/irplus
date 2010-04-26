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

package edu.ur.ir.item;

import edu.ur.persistent.BasePersistent;

/**
 * Base Information for item sponsor.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ItemSponsor extends BasePersistent{

	/**  Eclipse gererated id. */
	private static final long serialVersionUID = 1255257070126819891L;

	/**  Item */
	private GenericItem item;
	
	/**  Primary item type */
	private Sponsor sponsor;
	
	/**  Secondary item types */
	private String description;


	/**
	 * Default package protected constructor
	 */
	ItemSponsor(){}

	/**
	 * Create a item primary content type
	 * 
	 * @param primaryType
	 */
	public ItemSponsor(Sponsor sponsor, GenericItem item)
	{
		this.sponsor = sponsor;
		this.item = item;
	}
	
	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int value = 0;
		value += item == null ? 0 : item .hashCode();
		value += sponsor == null ? 0 : sponsor.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ItemSponsor)) return false;

		final ItemSponsor other = (ItemSponsor) o;

		if( ( item != null && !item.equals(other.getItem()) ) ||
			( item == null && other.getItem() != null ) ) return false;
		
		if( ( sponsor != null && !sponsor.equals(other.getSponsor()) ) ||
			( sponsor == null && other.getSponsor() != null ) ) return false;

		return true;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("[Item sponsor id = ");
		sb.append(id);
		sb.append(" sponsor= ");
		sb.append(sponsor);
		sb.append(" item  = ");
		sb.append(item);
		sb.append("]");
		return sb.toString();
	}

	public GenericItem getItem() {
		return item;
	}

	public void setItem(GenericItem item) {
		this.item = item;
	}

	public Sponsor getSponsor() {
		return sponsor;
	}

	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
