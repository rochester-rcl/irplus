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

import edu.ur.persistent.CommonPersistent;
import edu.ur.simple.type.UniqueSystemCodeAware;

/**
 * Class for creating and adding identifier types.
 * 
 * @author Nathan Sarr
 *
 */
public class IdentifierType extends CommonPersistent implements UniqueSystemCodeAware {

	/**  Eclipse Generated id. */
	private static final long serialVersionUID = 2063443132805741171L;
	
	/** allows a unique system code to be set on this object */
	private String uniqueSystemCode;
	
	/**
	 * Default constructor
	 */
	public IdentifierType(){}
	
	/**
	 * Default constructor.
	 * 
	 * @param name - name of the identifier type
	 */
	public IdentifierType(String name)
	{
		setName(name);
	}
	
	/**
	 * Create an identifer type with the name and description.
	 * 
	 * @param name
	 * @param description
	 */
	public IdentifierType(String name, String description)
	{
		setName(name);
		setDescription(description);
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Identifier type id = ");
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" description = ");
		sb.append(description);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IdentifierType)) return false;

		final IdentifierType other = (IdentifierType) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}


	public String getUniqueSystemCode() {
		return uniqueSystemCode;
	}

	public void setUniqueSystemCode(String uniqueSystemCode) {
		this.uniqueSystemCode = uniqueSystemCode;
	}

}
