package edu.ur.ir.person;

import edu.ur.persistent.CommonPersistent;
import edu.ur.simple.type.UniqueSystemCodeAware;

/**
 * Represents a unique global identifier type for a given person 
 * 
 * @author  
 *
 */
public class PersonNameAuthorityIdentifierType extends CommonPersistent implements UniqueSystemCodeAware {

	
	
	/**
	 * Generated serial version uid 
	 */
	private static final long serialVersionUID = -2426007560145294441L;
	
	
	/** allows a unique system code to be set on this object */
	private String uniqueSystemCode;
	
	/**
	 * Default constructor
	 */
	public PersonNameAuthorityIdentifierType(){}
	
	/**
	 * Default constructor.
	 * 
	 * @param name - name of the identifier type
	 */
	public PersonNameAuthorityIdentifierType(String name)
	{
		setName(name);
	}
	
	/**
	 * Create an identifer type with the name and description.
	 * 
	 * @param name
	 * @param description
	 */
	public PersonNameAuthorityIdentifierType(String name, String description)
	{
		setName(name);
		setDescription(description);
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
		StringBuffer sb = new StringBuffer("[Person Name Authority Identifier type id = ");
		sb.append(id);
		sb.append(" name = ");
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
		if (!(o instanceof PersonNameAuthorityIdentifierType)) return false;

		final PersonNameAuthorityIdentifierType other = (PersonNameAuthorityIdentifierType) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}


	public String getUniqueSystemCode() {
		return uniqueSystemCode;
	}

	public void setUniqueSystemCode(String uniqueSystemCode) {
		this.uniqueSystemCode = uniqueSystemCode;
	}


}
