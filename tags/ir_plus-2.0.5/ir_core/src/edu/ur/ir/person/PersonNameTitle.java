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
 * A word or title for the person name.
 *  
 * @author Nathan Sarr
 *
 */
public class PersonNameTitle extends BasePersistent {

	/**  Eclipse generated Id. */
	private static final long serialVersionUID = -4156119594986688072L;
	
	/**  The title or other word value */
	private String title;
	
	/**  The Person name  */
	private PersonName personName;
	
	/**
	 * Package protected constructor
	 */
	PersonNameTitle(){};
	
	/**
	 * Constructor.
	 * 
	 * @param value
	 */
	public PersonNameTitle(String value, PersonName personName)
	{
		this.setTitle(value);
		this.setPersonName(personName);
	}

	/**
	 * Get the value
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 */
	public void setTitle(String value) {
		this.title = value;
	}
	
    /**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += title == null ? 0 : title.hashCode();
    	return hash;
    }
    
    /**
     * Person Name Equals.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
    	if (this == o) return true;
		if (!(o instanceof PersonNameTitle)) return false;

		final PersonNameTitle other = (PersonNameTitle) o;

		if( ( title != null && !title.equals(other.getTitle()) ) ||
			( title == null && other.getTitle() != null ) ) return false;
		
		return true;
    }
	
	/**
	 * To string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[ Id = " );
		sb.append(id);
		sb.append(" value = ");
		sb.append(title);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Returns Person name
	 * @return
	 */
	public PersonName getPersonName() {
		return personName;
	}

	/**
	 * Sets the Person name
	 * @param personName
	 */
	void setPersonName(PersonName personName) {
		this.personName = personName;
	}

}
