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
 * Class to hold an identifier.
 * 
 * @author Nathan Sarr
 *
 */
public class ItemIdentifier extends BasePersistent{
	
	/**  Generated serial id. */
	private static final long serialVersionUID = -208669694151854900L;

	/**  The type of identifier   */
	private IdentifierType identifierType;
	
	/**  Identifier value  */
	private String value;
	
	/**  The item this identifier belongs to. */
	private GenericItem item;
	
	/**
	 *  Constructor
	 *  
	 */
	ItemIdentifier() {}
	
	/**
	 *  Constructor
	 *  
	 */
	public ItemIdentifier(IdentifierType identifierType, GenericItem item) {
		this.identifierType = identifierType;
		this.item = item;

	}
	
	/**
	 *  Constructor
	 *  
	 * @param item
	 * @param identifierType
	 */
	ItemIdentifier(IdentifierType identifierType) {
		this.identifierType = identifierType;
	}

	/**
	 * Get the type of identifier.
	 * 
	 * @return the identifier value
	 */
	public IdentifierType getIdentifierType() {
		return identifierType;
	}

	/**
	 * Set the identifier type.
	 * 
	 * @param identifierType
	 */
	public void setIdentifierType(IdentifierType identifierType) {
		this.identifierType = identifierType;
	}

	/**
	 * Get the identifier value.
	 * 
	 * @return the identifier value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the identifier value.
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
		value += identifierType == null ? 0 : identifierType.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		
		StringBuffer sb = new StringBuffer("[ItemIdentifier id = ");
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
		if (!(o instanceof ItemIdentifier)) return false;

		final ItemIdentifier other = (ItemIdentifier) o;

		if( ( identifierType != null && !identifierType.equals(other.getIdentifierType()) ) ||
			( identifierType == null && other.getIdentifierType() != null ) ) return false;
		
		if( ( value != null && !value.equals(other.getValue()) ) ||
			( value == null && other.getValue() != null ) ) return false;
		
		return true;
	}

	/**
	 * The item this identifier belongs to.
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set the item this identifier belongs to.
	 * 
	 * @param item
	 */
	void setItem(GenericItem item) {
		this.item = item;
	}

}
