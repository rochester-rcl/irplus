package edu.ur.ir.person;

import edu.ur.persistent.BasePersistent;

public class PersonNameAuthorityIdentifier extends BasePersistent{	

	/**
	 * Generated identifier
	 */
	private static final long serialVersionUID = -8372477880142210490L;

	/**  The type of identifier   */
	private PersonNameAuthorityIdentifierType personNameAuthorityIdentifierType;

	/**  Identifier value  */
	private String value;
	
	/**  The item this identifier belongs to. */
	private PersonNameAuthority personNameAuthority;
	
	/**
	 *  Constructor
	 *  
	 */
	PersonNameAuthorityIdentifier() {}
	
	/**
	 *  Constructor
	 *  
	 */
	public PersonNameAuthorityIdentifier (PersonNameAuthorityIdentifierType personNameAuthorityIdentifierType, PersonNameAuthority personNameAuthority) {
		this.personNameAuthorityIdentifierType = personNameAuthorityIdentifierType;
		this.personNameAuthority = personNameAuthority;

	}
	
	/**
	 *  Constructor
	 *  
	 * @param item
	 * @param identifierType
	 */
	PersonNameAuthorityIdentifier (PersonNameAuthorityIdentifierType personNameAuthorityIdentifierType) {
		this.personNameAuthorityIdentifierType = personNameAuthorityIdentifierType;
	}

	/**
	 * Get the person name authority identifier type
	 * 
	 * @return
	 */
	public PersonNameAuthorityIdentifierType getPersonNameAuthorityIdentifierType() {
		return personNameAuthorityIdentifierType;
	}

	/**
	 * Set the person name authority identifier type
	 * @param personNameAuthorityIdentifierType
	 */
	public void setPersonNameAuthorityIdentifierType(PersonNameAuthorityIdentifierType personNameAuthorityIdentifierType) {
		this.personNameAuthorityIdentifierType = personNameAuthorityIdentifierType;
	}

	/**
	 * Get the identifier value.
	 * 
	 * @return the identifier value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the identifier value.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += this.value == null ? 0 : this.value.hashCode();
		value += personNameAuthorityIdentifierType == null ? 0 : personNameAuthorityIdentifierType.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		
		StringBuffer sb = new StringBuffer("[PersonNameAuthorityIdentifier id = ");
		sb.append(id);
		sb.append(" person name authority = " );
		sb.append(personNameAuthorityIdentifierType);
		sb.append(" value= " );
		sb.append(value);
		sb.append("]");
		
		return  sb.toString();
		
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof PersonNameAuthorityIdentifier)) return false;

		final PersonNameAuthorityIdentifier other = (PersonNameAuthorityIdentifier) o;

		if( ( personNameAuthorityIdentifierType != null && !personNameAuthorityIdentifierType.equals(other.getPersonNameAuthorityIdentifierType()) ) ||
			( personNameAuthorityIdentifierType == null && other.getPersonNameAuthorityIdentifierType() != null ) ) return false;
		
		if( ( value != null && !value.equals(other.getValue()) ) ||
			( value == null && other.getValue() != null ) ) return false;
		
		return true;
	}

	/**
	 * The item this identifier belongs to.
	 * 
	 * @return
	 */
	public PersonNameAuthority getPersonNameAuthority() {
		return personNameAuthority;
	}

	/**
	 * Set the item this identifier belongs to.
	 * 
	 * @param item
	 */
	void setPersonNameAuthority(PersonNameAuthority personNameAuthority) {
		this.personNameAuthority = personNameAuthority;
	}

}
