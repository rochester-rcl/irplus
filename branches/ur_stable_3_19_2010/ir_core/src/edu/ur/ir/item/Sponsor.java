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
 * Sponsors
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class Sponsor extends CommonPersistent {
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = -6950892882624713152L;
	
	/** first character of the sponsor */
	private char sponsorFirstChar;
	


	/**
	 * Default constructor
	 */
	public Sponsor(){}
	
	/**
	 * Constructor.
	 * 
	 * @param name
	 */
	public Sponsor(String name)
	{
		setName(name);
	}
	
	/**
	 * Set the name of the sponsor.
	 * 
	 * @see edu.ur.persistent.CommonPersistent#setName(java.lang.String)
	 */
	public void setName(String name)
	{
		this.name = name;
		if(name.length() > 0)
		{
		    this.sponsorFirstChar = Character.toLowerCase(name.charAt(0));
		}
		
	}
	
	/**
	 * Sponsor name first char.
	 * 
	 * @return
	 */
	public char getSponsorFirstChar() {
		return sponsorFirstChar;
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
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" name = " );
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
		if (!(o instanceof Sponsor)) return false;

		final Sponsor other = (Sponsor) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}


}
