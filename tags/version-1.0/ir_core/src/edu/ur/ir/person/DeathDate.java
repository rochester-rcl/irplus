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

import edu.ur.persistent.BasePersistent;


/**
 * Represents the death year.
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class DeathDate extends BasePersistent{

	/** Eclipse generated id */
	private static final long serialVersionUID = 841348115525533292L;
	
	/** Person */
	private PersonNameAuthority personNameAuthority;
	
	/** year the person died  */
	private int year;
	
	/** Package protected death date  */
	DeathDate(){}

	/**
	 * Constructor to create death Date
	 * 
	 * @param year year
	 */
	public DeathDate(int year){
		setYear(year);
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += year;
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" death year  = ");
		sb.append(year);
		sb.append("]");
		
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DeathDate)) return false;

		final DeathDate other = (DeathDate) o;
		
		if( other == null )
		{
			return false;
		}
		else
		{
		    return year == other.getYear();
		}
	}


	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}


	/**
	 * Get person
	 * 
	 * @return
	 */
	public PersonNameAuthority getPersonNameAuthority() {
		return personNameAuthority;
	}

	/**
	 * Set person
	 * 
	 * @param personNameAuthority
	 */
	public void setPersonNameAuthority(PersonNameAuthority personNameAuthority) {
		this.personNameAuthority = personNameAuthority;
	}
}
