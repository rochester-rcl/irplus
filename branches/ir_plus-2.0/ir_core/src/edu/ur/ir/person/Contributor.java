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
 * Represents a contributor.
 * 
 * @author Nathan Sarr
 *
 */
public class Contributor extends BasePersistent{
	
	/**  Generated id. */
	private static final long serialVersionUID = 3259570269307152910L;

	/**  Name used by person who made the contribution.  */
	private PersonName personName;
	
	/**  The type of contribution they made.  */
	private ContributorType contributorType;

	/**
	 * Default constructor
	 */
	public Contributor() {}

	/**
	 * Contributor record.
	 * 
	 * @param personName - person name 
	 * @param contributorType - type of contribution
	 */
	public Contributor(PersonName personName, ContributorType contributorType) {
		this.personName = personName;
		this.contributorType = contributorType;
	}
	
	/**
	 * The type of contribution made.
	 * 
	 * @return type of contribution made
	 */
	public ContributorType getContributorType() {
		return contributorType;
	}

	/**
	 * Set the contribution type.
	 * 
	 * @param contributorType
	 */
	public void setContributorType(ContributorType contributorType) {
		this.contributorType = contributorType;
	}

	/**
	 * Name of person who made the contribution.
	 * 
	 * @return name of person
	 */
	public PersonName getPersonName() {
		return personName;
	}

	/**
	 * Name of person who made the contribution.
	 * 
	 * @param personName
	 */
	public void setPersonName(PersonName personName) {
		this.personName = personName;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += personName == null ? 0 : personName.hashCode();
		value += contributorType == null ? 0 : contributorType.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" person name = ");
		if(personName != null )
		{
			sb.append(personName);
		}
		else
		{
			sb.append("null");
		}
		
		sb.append( " contributor type = ");
		if( contributorType != null )
		{
			sb.append(contributorType);
		}
		else
		{
			sb.append("null");
		}
		sb.append("]");
		
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Contributor)) return false;

		final Contributor other = (Contributor) o;

		if( ( contributorType != null && !contributorType.equals(other.getContributorType()) ) ||
			( contributorType == null && other.getContributorType() != null ) ) return false;
	
		if( ( personName != null && !personName.equals(other.getPersonName()) ) ||
			( personName == null && other.getPersonName() != null ) ) return false;
		
		return true;
	}
}
