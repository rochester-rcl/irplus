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

	/** From Ip address part 1 */
	private int fromAddress1;

	/** From Ip address part 2 */
	private int fromAddress2;

	/** From Ip address part 3 */
	private int fromAddress3;

	/** From Ip address part 4 */
	private int fromAddress4;
	
	/** TO Ip address part 4 */
	private int toAddress4;
	
	/** determine if the ignore ip address should be stored persistently
	 *  if this is set to false the ip address should not be stored*/
	private boolean storeCounts = false;


	/**
	 * Default Constructor
	 */
	public IgnoreIpAddress() {}
	
	/**
	 * Constructor with ip address
	 */
	public IgnoreIpAddress(int fromAddress1, int fromAddress2, int fromAddress3, int fromAddress4, int toAddress4) {
		setFromAddress1(fromAddress1);
		setFromAddress2(fromAddress2);
		setFromAddress3(fromAddress3);
		setFromAddress4(fromAddress4);
		setToAddress4(toAddress4);
	}

	public int getFromAddress1() {
		return fromAddress1;
	}

	public void setFromAddress1(int fromAddress1) {
		this.fromAddress1 = fromAddress1;
	}

	public int getFromAddress2() {
		return fromAddress2;
	}

	public void setFromAddress2(int fromAddress2) {
		this.fromAddress2 = fromAddress2;
	}

	public int getFromAddress3() {
		return fromAddress3;
	}

	public void setFromAddress3(int fromAddress3) {
		this.fromAddress3 = fromAddress3;
	}

	public int getFromAddress4() {
		return fromAddress4;
	}

	public void setFromAddress4(int fromAddress4) {
		this.fromAddress4 = fromAddress4;
	}

	public int getToAddress4() {
		return toAddress4;
	}

	public void setToAddress4(int toAddress4) {
		this.toAddress4 = toAddress4;
	}
	
	public boolean isStoreCounts() {
		return storeCounts;
	}

	public void setStoreCounts(boolean store) {
		this.storeCounts = store;
	}
	
	public boolean getStoreCounts(){
		return storeCounts;
	}


	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IgnoreIpAddress)) return false;

		final IgnoreIpAddress other = (IgnoreIpAddress) o;

		if( ( fromAddress1 != other.getFromAddress1()) )  return false;
		if( ( fromAddress2 != other.getFromAddress2()) )  return false;
		if( ( fromAddress3 != other.getFromAddress3()) )  return false;
		if( ( fromAddress4 != other.getFromAddress4()) )  return false;
		if( ( toAddress4 != other.getToAddress4()) )  return false;
		
		return true;
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" fromAddress1 = ");
		sb.append(fromAddress1);
		sb.append(" fromAddress2 = ");
		sb.append(fromAddress2);
		sb.append(" fromAddress3 = ");
		sb.append(fromAddress3);
		sb.append(" fromAddress4 = ");
		sb.append(fromAddress4);
		sb.append(" toAddress4 = ");
		sb.append(toAddress4);
		sb.append(" store = ");
		sb.append(storeCounts);
		sb.append("]");
		return sb.toString();
	}
	
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += fromAddress1;
		value += fromAddress2;
		value += fromAddress3;
		value += fromAddress4;
		value += toAddress4;
		
		return value;
	}	
}
