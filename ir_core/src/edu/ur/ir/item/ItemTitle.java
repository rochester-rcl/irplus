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
 * Represents a sub titles for item
 *   
 * @author Sharmila Ranganathan
 *
 */
public class ItemTitle extends BasePersistent  {

	/**  Eclipse generated id */
	private static final long serialVersionUID = 7213668037628436533L;

	/** Item title  */
	private String title;
	
	/** The item this title belongs to. */
	private GenericItem item;

	
	/**
	 * Package protected constructor.
	 */
	ItemTitle(){};
	
	/**
	 * Create a item title 
	 * 
	 * @param linkVersion
	 */
	ItemTitle(GenericItem item, String title)
	{
		setItem(item);
		setTitle(title);
	}
	
	/**
	 * The item this title belongs to.
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set the item this title belongs to.
	 * 
	 * @param item
	 */
	public void setItem(GenericItem item) {
		this.item = item;
	}
	
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += title == null ? 0 : title.hashCode();
		value += item == null ? 0 : item.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Item Title id = ");
		sb.append(id);
		sb.append(" version = " );
		sb.append(version);
		sb.append(" title = " );
		sb.append(title);
		sb.append("]");
		
		return  sb.toString();
	}
	
	/**
	 * Check equality of the object
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ItemTitle)) return false;

		final ItemTitle other = (ItemTitle) o;

		if( ( title != null && !title.equals(other.getTitle()) ) ||
			( title == null && other.getTitle() != null ) ) return false;
		
		if( ( item != null && !item.equals(other.getTitle())) ||
			( item == null && other.getTitle() != null ) ) return false;

		return true;
	}

	/**
	 * Get item title 
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set item title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
