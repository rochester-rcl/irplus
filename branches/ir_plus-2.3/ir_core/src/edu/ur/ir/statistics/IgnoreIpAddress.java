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

package edu.ur.ir.statistics;

import edu.ur.persistent.CommonPersistent;


/**
 * IP addresses to be ignored
 * 
 * @author Sharmila Ranganathan
 *
 */
public class IgnoreIpAddress extends CommonPersistent {

	/** Eclipse generated id */
	private static final long serialVersionUID = 680400303890833403L;

	// ip address
	private String address;

	
	/** determine if the ignore ip address should be stored persistently
	 *  if this is set to false the ip address should not be stored*/
	private boolean storeCounts = false;
	
	/**
	 * Default Constructor
	 */
	IgnoreIpAddress() {}
	
	/**
	 * Create an ip ignore address.
	 * 
	 * @param address - the string representation of the address
	 */
	public IgnoreIpAddress(String address)
	{
		setAddress(address);
	}
	
	/**
	 * Get the address.
	 * 
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Set the address.
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Returns true if the counts should be stored even if they are ignored.
	 * 
	 * @return - true if the counts should be ignored.
	 */
	public boolean getStoreCounts() {
		return storeCounts;
	}

	/**
	 * If set to true the counts will be stored otherwise they will be deleted.
	 * 
	 * @param storeCounts
	 */
	public void setStoreCounts(boolean storeCounts) {
		this.storeCounts = storeCounts;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int hashCode = 0;
		hashCode += address != null ? address.hashCode() : 0;
		return hashCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
    	if (this == o) return true;
		if (!(o instanceof IgnoreIpAddress)) return false;

		final IgnoreIpAddress other = (IgnoreIpAddress) o;

		if( ( address != null && !address.equals(other.getAddress()) ) ||
			( address == null && other.getAddress() != null ) ) return false;
		
		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" address = ");
		sb.append(address);
		sb.append(" store = ");
		sb.append(storeCounts);
		sb.append("]");
		return sb.toString();
	}
}
