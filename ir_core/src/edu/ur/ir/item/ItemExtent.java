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
 * Class to hold an extent.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ItemExtent extends BasePersistent{
	
	/**  Generated serial id. */
	private static final long serialVersionUID = 7880235931259192897L;

	/**  The extent type   */
	private ExtentType extentType;
	
	/**   value  of the extent*/
	private String value;
	
	/**  The item this extent type belongs to. */
	private GenericItem item;
	
	/**
	 *  Constructor
	 *  
	 */
	ItemExtent() {}
	
	/**
	 *  Constructor
	 *  
	 */
	public ItemExtent(ExtentType extentType, GenericItem item, String value) {
		setExtentType(extentType);
		setItem(item);
		setValue(value);
	}
	
	/**
	 *  Constructor
	 *  
	 * @param item
	 * @param extentType
	 */
	ItemExtent(ExtentType extentType) {
		this.extentType = extentType;
	}

	/**
	 * Get the type of extent.
	 * 
	 * @return the extent value
	 */
	public ExtentType getExtentType() {
		return extentType;
	}

	/**
	 * Set the extent type.
	 * 
	 * @param extentType
	 */
	public void setExtentType(ExtentType extentType) {
		this.extentType = extentType;
	}

	/**
	 * Get the extent value.
	 * 
	 * @return the extent value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the extent value.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += this.value == null ? 0 : this.value.hashCode();
		value += extentType == null ? 0 : extentType.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		
		StringBuffer sb = new StringBuffer("[Item extent id = ");
		sb.append(id);
		sb.append(" item = " );
		sb.append(item);
		sb.append(" value= " );
		sb.append(value);
		sb.append("]");
		
		return  sb.toString();
		
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ItemExtent)) return false;

		final ItemExtent other = (ItemExtent) o;

		if( ( extentType != null && !extentType.equals(other.getExtentType()) ) ||
			( extentType == null && other.getExtentType() != null ) ) return false;
		
		if( ( value != null && !value.equals(other.getValue()) ) ||
			( value == null && other.getValue() != null ) ) return false;
		
		if( ( item != null && !item.equals(other.getItem()) ) ||
			( item == null && other.getItem() != null ) ) return false;
		
		return true;
	}

	/**
	 * The item this extent belongs to.
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set the item this extent belongs to.
	 * 
	 * @param item
	 */
	void setItem(GenericItem item) {
		this.item = item;
	}

}
