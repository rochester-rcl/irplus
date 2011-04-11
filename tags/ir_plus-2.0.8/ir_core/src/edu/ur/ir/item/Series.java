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

/**
 * Series for the item
 * 
 * @author Sharmila Ranganathan
 *
 */
public class Series extends CommonPersistent {

	/**
	 * Eclipse generated id
	 */
	private static final long serialVersionUID = 3333578189856899291L;
	
	/** Series number */
	private String number;
	
	/**
	 * Default constructor 
	 */
	Series(){}
	
	/**
	 * Constructor for series
	 */
	public Series(String name) {
		setName(name);
	}
	
	/**
	 * Constructor for series
	 */
	public Series(String name, String number) {
		this(name);
		setNumber(number);
	}

	/**
	 * Get the series number
	 * 
	 * @return
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * Set the series number 
	 * 
	 * @param number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		value += number == null ? 0 : number.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" name = " );
		sb.append(name);
		sb.append(" number = ");
		sb.append(number);
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
		if (!(o instanceof Series)) return false;

		final Series other = (Series) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		if( ( number != null && !number.equals(other.getNumber()) ) ||
			( number == null && other.getNumber() != null ) ) return false;
		
		return true;
	}
}
