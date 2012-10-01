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

package edu.ur.ir.security.service;

import edu.ur.ir.security.ExternalAuthenticationDetails;
import edu.ur.ir.user.ExternalAccountType;

/**
 * Default Implementation of external authentication details
 * 
 * @author Nathan Sarr 
 *
 */
public class DefaultExternalAuthenticationDetails implements ExternalAuthenticationDetails{

	/**  The type of authentication that was successful */
	private ExternalAccountType type;
	
	/**
	 * Default constructor.
	 * 
	 * @param type
	 */
	public DefaultExternalAuthenticationDetails(ExternalAccountType type)
	{
		setType(type);
	}
	
	/**
	 * The type of authentication that was successful.
	 * 
	 * @see edu.ur.ir.security.ExternalAuthenticationDetails#getType()
	 */
	public ExternalAccountType getType() {
		return type;
	}
	
	/**
	 * Set the external account type.
	 * 
	 * @param type
	 */
	void setType(ExternalAccountType type)
	{
		this.type = type;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[DefaultExternalAuthenticationDetails type = ");
		sb.append(type);
		sb.append("]");
		return sb.toString();

	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += type == null ? 0 : type.hashCode();
		return value;
	}
	
	/**
	 * This assumes if the names are the same, the IrFiles are the
	 * same.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DefaultExternalAuthenticationDetails)) return false;

		final DefaultExternalAuthenticationDetails other = (DefaultExternalAuthenticationDetails) o;

		if( ( type != null && !type.equals(other.getType()) ) ||
			( type == null && other.getType() != null ) ) return false;
	
		return true;
	}

	
}
