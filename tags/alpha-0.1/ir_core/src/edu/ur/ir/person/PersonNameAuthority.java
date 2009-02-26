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
 * Base Information for a person name authority.
 * 
 * @author Nathan Sarr
 *
 */
public class PersonNameAuthority extends BasePersistent{

	/**  Eclipse gererated id. */
	private static final long serialVersionUID = -6036204812394520787L;
	
	/**  Birth date of the person */
	private BirthDate birthDate;
	
	/**  Death Date of the person. */
	private DeathDate deathDate;
	
	/**  Authoritative name of the person */
	private PersonName authoritativeName;
	
	/**  Names used by the person. */
	private Set<PersonName> names = new HashSet<PersonName>();


	/**
	 * Default package protected constructor
	 */
	PersonNameAuthority(){}

	/**
	 * Create a person with a default authoritative name.
	 * 
	 * @param authoritativeName
	 */
	public PersonNameAuthority(PersonName authoritativeName)
	{
		addName(authoritativeName, true);
	}
	
	/**
	 * Get the authoritative name for the user.
	 * 
	 * @return
	 */
	public PersonName getAuthoritativeName() {
		return authoritativeName;
	}
	
	/**
	 * Set the persons authoritative name.
	 * 
	 * @param name
	 */
	void setAuthoritativeName(PersonName name)
	{
		authoritativeName = name;
	}

	/**
	 * Get the birth date for the user.
	 * 
	 * @return
	 */
	public BirthDate getBirthDate() {
		return birthDate;
	}

	/**
	 * Set the birth date for the user.
	 * 
	 * @param birthDate
	 */
	public void setBirthDate(BirthDate birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * Add the birth date for the person
	 * 
	 * @param month
	 * @param day
	 * @param year
	 * @return birth date
	 */
	public BirthDate addBirthDate(int month, int day, int year) {
		BirthDate birthDate = new BirthDate(month, day, year);
		birthDate.setPersonNameAuthority(this);
		this.birthDate = birthDate;
		
		return birthDate;
	}
	
	/**
	 * Add the death date for the person
	 * 
	 * @param month
	 * @param day
	 * @param year
	 * @return death date
	 */
	public DeathDate addDeathDate(int month, int day, int year) {
		DeathDate deathDate = new DeathDate(month, day, year);
		deathDate.setPersonNameAuthority(this);
		this.deathDate = deathDate;
		
		return deathDate;
	}
	/**
	 * Get the death date for the user.
	 * 
	 * @return
	 */
	public DeathDate getDeathDate() {
		return deathDate;
	}

	/**
	 * Set the death date for the user.
	 * 
	 * @param deathDate
	 */
	public void setDeathDate(DeathDate deathDate) {
		this.deathDate = deathDate;
	}
	
	/**
	 * Add this name as a name the person uses.  If the name is already
	 * in the set of names, no changes are made and the method returns.
	 * 
	 * @param name to add
	 * @param authoritative if true then set this name as the authoritative name.
	 */
	public boolean addName(PersonName name, boolean authoritative)
	{
		if( names.contains(name))
		{
			return false;
		}
		
		if( (name.getPersonNameAuthority() != null) && (!name.getPersonNameAuthority().equals(this)) )
		{
			name.getPersonNameAuthority().removeName(name);
		}
		
		name.setPersonNameAuthority(this);
		names.add(name);
		
		if( authoritative)
		{
		    setAuthoritativeName(name);
		}
		return true;
	}
	
	
	/**
	 * Set the name with the specified id as the authoritative name. If
	 * a name with the specified id is not found, no change is made.
	 *  
	 * @param id
	 * @return true if the authoritative name has been changed
	 */
	public boolean changeAuthoritativeName(Long id)
	{
		for(PersonName name: names)
		{
			if(name.getId().equals(id))
			{
				setAuthoritativeName(name);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Change the authoritative name to the specified name.
	 * 
	 * @param name to set the person name to.
	 * @return
	 */
	public void changeAuthoritativeName(PersonName name)
	{
		if(!names.contains(name))
		{
			addName(name, true);
		}
		else
		{
		    setAuthoritativeName(name);
		}
	}
	
	/**
	 * Remove the name from the person.
	 * 
	 * @param name
	 *
	 * @return true if the name is removed.
	 */
	public boolean removeName(PersonName name)
	{
		if(name.equals(this.authoritativeName))
		{
			authoritativeName = null;
		}
		
		if( names.contains(name))
		{
			name.setPersonNameAuthority(null);
		}
		return names.remove(name);
	}
	
	/**
	 * Get the name based on id.
	 * 
	 * @param id
	 * @return
	 */
	public PersonName getName(Long id)
	{
		PersonName myName = null;
		for( PersonName name : names)
		{
			if( name.getId().equals(id))
			{
				myName = name;
			}
		}
		
		return myName;
	}

	/**
	 * Unmodifiable Set of names of the person.
	 * 
	 * @return
	 */
	public Set<PersonName> getNames() {
		return Collections.unmodifiableSet(names);
	}

	/**
	 * Set the names used by the user.
	 * 
	 * @param names
	 */
	public void setNames(Set<PersonName> names) {
		this.names = names;
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int value = 0;
		value += authoritativeName == null ? 0 : authoritativeName.hashCode();
		value += deathDate == null ? 0 : deathDate.hashCode();
		value += birthDate == null ? 0 : birthDate.hashCode();
		value += id == null ? 0 : id.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PersonNameAuthority)) return false;

		final PersonNameAuthority other = (PersonNameAuthority) o;

		if( ( authoritativeName != null && !authoritativeName.equals(other.getAuthoritativeName()) ) ||
			( authoritativeName == null && other.getAuthoritativeName() != null ) ) return false;
		
		if( ( deathDate != null && !deathDate.equals(other.getDeathDate()) ) ||
		    ( deathDate == null && other.getDeathDate() != null ) ) return false;
		
		if( ( birthDate != null && !birthDate.equals(other.getBirthDate()) ) ||
			( birthDate == null && other.getBirthDate() != null ) ) return false;
		
		if( ( id != null && !id.equals(other.getId()) ) ||
			( id == null && other.getId() != null ) ) return false;

		return true;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean softEquals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PersonNameAuthority)) return false;

		final PersonNameAuthority other = (PersonNameAuthority) o;

		if( ( authoritativeName != null && !authoritativeName.equals(other.getAuthoritativeName()) ) ||
			( authoritativeName == null && other.getAuthoritativeName() != null ) ) return false;
		
		if( ( deathDate != null && !deathDate.equals(other.getDeathDate()) ) ||
		    ( deathDate == null && other.getDeathDate() != null ) ) return false;
		
		if( ( birthDate != null && !birthDate.equals(other.getBirthDate()) ) ||
			( birthDate == null && other.getBirthDate() != null ) ) return false;
		return true;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("[Person id = ");
		sb.append(id);
		sb.append(" birthDate= ");
		sb.append(birthDate);
		sb.append(" deathDate = ");
		sb.append(deathDate);
		sb.append("]");
		return sb.toString();
	}
	
}
