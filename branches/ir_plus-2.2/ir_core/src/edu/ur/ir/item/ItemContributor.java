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

import edu.ur.ir.person.Contributor;
import edu.ur.order.Orderable;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a link between a contributor and
 * an item
 * 
 * @author Nathan Sarr
 *
 */
public class ItemContributor extends BasePersistent implements Orderable{

	/** eclipse generated id */
	private static final long serialVersionUID = -8115388428068259979L;
	
	/**  the generic item this contributor made a contribution to*/
	private GenericItem item;
	
	/** order to display this in */
	private int order;
	
	/** contributor to the item */
	private Contributor contributor;
	
	/** Package protected constructor */
	ItemContributor(){}
	
	/**
	 * Create a new item contributor 
	 * 
	 * @param item - item this contributor has made a contribution to
	 * @param order - order for the item
	 * @param contributor - contributor that has made the contribution.
	 */
	public ItemContributor(GenericItem item, int order, Contributor contributor)
	{
		setItem(item);
		setOrder(order);
		setContributor(contributor);
	}

	public GenericItem getItem() {
		return item;
	}

	void setItem(GenericItem item) {
		this.item = item;
	}

	public int getOrder() {
		return order;
	}

	void setOrder(int order) {
		this.order = order;
	}

	public Contributor getContributor() {
		return contributor;
	}

	void setContributor(Contributor contributor) {
		this.contributor = contributor;
	}
	
    /**
     * Override Equals.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
    	if (this == o) return true;
		if (!(o instanceof ItemContributor)) return false;

		final ItemContributor other = (ItemContributor) o;

		if( ( item != null && !item.equals(other.getItem()) ) ||
			( item == null && other.getItem() != null ) ) return false;
		
		if( ( contributor != null && !contributor.equals(other.getContributor()) ) ||
			( contributor == null && other.getContributor() != null ) ) return false;
		return true;
    }
    
    /**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += item == null ? 0 : item.hashCode();
    	hash += contributor == null ? 0 : contributor.hashCode();
    	return hash;
    }
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" order = ");
		sb.append(order);
		sb.append("]");
		return sb.toString();
	}
}
