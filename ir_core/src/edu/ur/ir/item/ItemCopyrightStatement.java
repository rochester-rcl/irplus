package edu.ur.ir.item;

import edu.ur.persistent.BasePersistent;

/**
 * Represents a copyright statement for an item
 * 
 * @author Nathan Sarr
 *
 */
public class ItemCopyrightStatement extends BasePersistent{
	
	/** eclipse generated id. */
	private static final long serialVersionUID = -641813141389387922L;

	/**  The copy right statement   */
	private CopyrightStatement copyrightStatement;
	
	/**  The item this copyright statement belongs to. */
	private GenericItem item;
	
	/**
	 * Package protected constructor
	 */
	ItemCopyrightStatement(){}
	
	/**
	 * Create an item copyright statement
	 * 
	 * @param copyrightStatement - copyright statement to attach to the item
	 * @param genericItem - generic item to attach the copyright to.
	 */
	public ItemCopyrightStatement(CopyrightStatement copyrightStatement, GenericItem genericItem)
	{
		this.setCopyrightStatement(copyrightStatement);
		this.setItem(genericItem);
	}

	/**
	 * Get the generic item 
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set the generic item
	 * 
	 * @param item
	 */
	void setItem(GenericItem item) {
		this.item = item;
	}

	/**
	 * Get the copyright statement.
	 * 
	 * @return
	 */
	public CopyrightStatement getCopyrightStatement() {
		return copyrightStatement;
	}

	/**
	 * Set the copyright statment for the item.
	 * 
	 * @param copyrightStatement
	 */
	void setCopyrightStatement(CopyrightStatement copyrightStatement) {
		this.copyrightStatement = copyrightStatement;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += this.copyrightStatement == null ? 0 : this.copyrightStatement.hashCode();
		value += item == null ? 0 : item.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		
		StringBuffer sb = new StringBuffer("[Item Copyright Statement id = ");
		sb.append(id);
		sb.append(" item = " );
		sb.append(item);
		sb.append(" copyright = " );
		sb.append(copyrightStatement);
		sb.append("]");
		
		return  sb.toString();
		
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ItemCopyrightStatement)) return false;

		final ItemCopyrightStatement other = (ItemCopyrightStatement) o;

		if( ( copyrightStatement != null && !copyrightStatement.equals(other.getCopyrightStatement()) ) ||
			( copyrightStatement == null && other.getCopyrightStatement() != null ) ) return false;
		
	
		if( ( item != null && !item.equals(other.getItem()) ) ||
			( item == null && other.getItem() != null ) ) return false;
		
		return true;
	}
	
}
