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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.ur.persistent.BasePersistent;

/**
 * Name information for a person in the system. A person can have more than one name.  
 * A person does not necessarily need to be a user of the system.
 * 
 * @author Nathan Sarr.
 *
 */
public class PersonName extends BasePersistent{

	/**  Eclipse generated id. */
	private static final long serialVersionUID = -3769156831711705885L;
	
	/**  The authoritative name this name is related to. */
	private PersonNameAuthority personNameAuthority;

	/**  Persons first name. */
	private String forename;
	
	/**  Middle name of the person. */
	private String middleName;
	
	/**  Persons last name */
	private String surname;
	
	/** the last name first character  */
	private char surnameFirstChar;
	
	/**  Family name : Dunlop Family */
	private String familyName;
	
	/**  Initials of the name. */
	private String initials;
	
	/**
	 * A roman numeral alone or a roman 
	 * numeral and a subsquent part of fore name
	 * i.e. II
	 */
	private String numeration;
	
	/**
	 * Titles and other words used for the persons name.
	 * 
	 * Dr., Chef
	 */
	private Set<PersonNameTitle> personNameTitles = new HashSet<PersonNameTitle> ();
	
	
	/**
	 * Package protected constructor
	 */
	public PersonName(){}


	/**
	 * Get the family Name.
	 * 
	 * @return
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * Set the family name.
	 * 
	 * @param familyName
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	/**
	 * Get the forename.
	 * 
	 * @return
	 */
	public String getForename() {
		return forename;
	}

	/**
	 * Set the forename.
	 * 
	 * @param forename
	 */
	public void setForename(String forename) {
		this.forename = forename;
	}

	/**
	 * Get the numeration.
	 * 
	 * @return
	 */
	public String getNumeration() {
		return numeration;
	}

	/**
	 * Set the numeration.
	 * @param numberation
	 */
	public void setNumeration(String numeration) {
		this.numeration = numeration;
	}

	/**
	 * Get the surname.
	 * 
	 * @return
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Set the surname.
	 * 
	 * @param surname
	 */
	public void setSurname(String surname) {
		this.surname = surname;
		
		if(surname.length() > 0)
		{
		    this.surnameFirstChar = Character.toLowerCase(surname.charAt(0));
		}
	}

	/**
	 * Initials of the name.
	 * 
	 * @return
	 */
	public String getInitials() {
		return initials;
	}

	/**
	 * Initials of the name.
	 * 
	 * @param initials
	 */
	public void setInitials(String initials) {
		this.initials = initials;
	}

	/**
	 * Middle name.
	 * 
	 * @return
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * Set the middle name.
	 * 
	 * @param middleName
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	/**
	 * The person this name belongs to.
	 * 
	 * @return the person this name belongs to
	 */
	public PersonNameAuthority getPersonNameAuthority() {
		return personNameAuthority;
	}

	/**
     * Set the person this name belongs to.
     * 
	 * @param personNameAuthority
	 */
	void setPersonNameAuthority(PersonNameAuthority personNameAuthority) {
		this.personNameAuthority = personNameAuthority;
	}
	
	/**
	 * Get the person titles and other words.
	 * 
	 * @return the person titles as an unmodifiable collections
	 */
	public Set<PersonNameTitle> getPersonNameTitles() {
		return Collections.unmodifiableSet(personNameTitles);
	}

	/**
	 * @param personNameTitles
	 */
	void setPersonNameTitles(Set<PersonNameTitle> personNameTitles) {
		this.personNameTitles = personNameTitles;
	}
	
	/**
	 * Add the person name title
	 * 
	 * @param personNameTitle
	 */
	public PersonNameTitle addPersonNameTitle(String title)
	{
		PersonNameTitle newTitle = new PersonNameTitle(title, this);
		personNameTitles.add(newTitle);
		return newTitle;
	}
	
	/**
	 * Remove the person name title from the set.
	 * 
	 * @param personNameTitle
	 * @return true if the personName title is removed.
	 */
	public boolean removePersonNameTitle(PersonNameTitle personNameTitle)
	{
		return personNameTitles.remove(personNameTitle);
	}
	
	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += familyName == null ? 0 : familyName.hashCode();
		value += forename == null? 0 : forename.hashCode();
		value += initials == null? 0 : initials.hashCode();
		value += middleName == null ? 0 : middleName.hashCode();
		value += numeration == null ? 0 : numeration.hashCode();
		value += surname == null ? 0 : surname.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof PersonName)) return false;

		final PersonName other = (PersonName) o;

		if( ( familyName != null && !familyName.equals(other.getFamilyName()) ) ||
			( familyName == null && other.getFamilyName() != null ) ) return false;
		
		if( ( forename != null && !forename.equals(other.getForename()) ) ||
		    ( forename == null && other.getForename() != null ) ) return false;
		
		if( ( initials != null && !initials.equals(other.getInitials()) ) ||
			( initials == null && other.getInitials() != null ) ) return false;

		if( ( middleName != null && !middleName.equals(other.getMiddleName()) ) ||
			( middleName == null && other.getMiddleName() != null ) ) return false;
		
		if( ( numeration != null && !numeration.equals(other.getNumeration()) ) ||
			( numeration == null && other.getNumeration() != null ) ) return false;

		if( ( surname != null && !surname.equals(other.getSurname()) ) ||
			( surname == null && other.getSurname() != null ) ) return false;

		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append( "[Person name id = " );
		sb.append(this.getId());
		sb.append(" familyName = ");
		sb.append(familyName); 
		sb.append(" forename = ");
		sb.append(forename);
		sb.append(" initials = ");
		sb.append(initials);
		sb.append(" middleName = ");
		sb.append(middleName);
		sb.append(" numeration = ");
		sb.append(numeration);
		sb.append(" surename = ");
		sb.append(surname);
		sb.append("]");
		
		return sb.toString();
	}


	public char getSurnameFirstChar() {
		return surnameFirstChar;
	}
	
}
