package edu.ur.ir.item;

import edu.ur.persistent.CommonPersistent;

/**
 * Represents a copyright statement.  The name
 * of the copyright statement is used to uniquely identify
 * which copyright statement is being accessed.
 * 
 * @author Nathan Sarr
 *
 */
public class CopyrightStatement extends CommonPersistent{

	/** eclipse generated id */
	private static final long serialVersionUID = 610942926758515642L;
	
	/** text for they copyright statement */
	private String text;
	
	/**
	 * Package protected constructor
	 */
	CopyrightStatement(){}
	
	/**
	 * Create a copyright statement with the specified name.
	 * 
	 * @param name - name of the copyright statement
	 */
	public CopyrightStatement(String name)
	{
		setName(name);
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
		StringBuffer sb = new StringBuffer("[CopyrightStatement id = ");
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
		if (!(o instanceof CopyrightStatement)) return false;

		final CopyrightStatement other = (CopyrightStatement) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
