/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.user;

import edu.ur.persistent.CommonPersistent;

/**
 * Represents an external authorization account type - this supports
 * systems who will need to authorize users against multiple accounts.
 * 
 * @author Nathan Sarr
 *
 */
public class ExternalAccountType extends CommonPersistent {

	/** eclipse generated id */
	private static final long serialVersionUID = 3988403684531611527L;
	
	/**  Constructor */
	public ExternalAccountType(){};
	
	/**
	 * Default constructor.
	 * 
	 * @param name - name of the authorization account
	 */
	public ExternalAccountType(String name)
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
		StringBuffer sb = new StringBuffer("[ExternalAuthorizationAccount = ");
		sb.append(id);
		sb.append(" version = " );
		sb.append(version);
		sb.append(" name = " );
		sb.append(name);
		sb.append(" description = " );
		sb.append(description);
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
		if (!(o instanceof ExternalAccountType)) return false;

		final ExternalAccountType other = (ExternalAccountType) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;

		return true;
	}
}
