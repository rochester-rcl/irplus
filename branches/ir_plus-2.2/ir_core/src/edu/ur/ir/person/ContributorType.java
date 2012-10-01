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

package edu.ur.ir.person;

import edu.ur.persistent.CommonPersistent;
import edu.ur.simple.type.UniqueSystemCodeAware;


/**
 * Class that represents some type of contributor in the system.  
 *  
 * @author Nathan Sarr
 *
 */
public class ContributorType extends CommonPersistent implements UniqueSystemCodeAware {

	/**  Generated id. */
	private static final long serialVersionUID = -3715792887181737445L;
	
	/** System code for this contributor type */
	private String uniqueSystemCode;
	
	/** determine if this is an authoring type of contributor */
	private boolean authorType = false;

	/**
	 * Package protected constructor 
	 */
	public ContributorType(){}
	
	/**
	 * Create a contributor type with the specified name.
	 * 
	 * @param name
	 */
	public ContributorType(String name)
	{
		setName(name);
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
		StringBuffer sb = new StringBuffer("[");
		sb.append(" id = ");
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" description = ");
		sb.append(description);
		sb.append(" unique system code = ");
		sb.append(uniqueSystemCode);
		sb.append("]");
		return sb.toString();
		
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ContributorType)) return false;

		final ContributorType other = (ContributorType) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}


	
	public String getUniqueSystemCode() {
		return uniqueSystemCode;
	}


	public void setUniqueSystemCode(String systemCode) {
		this.uniqueSystemCode = systemCode;
	}
	
	
	public boolean getAuthorType() {
		return authorType;
	}

	public void setAuthorType(boolean authorType) {
		this.authorType = authorType;
	}

}
