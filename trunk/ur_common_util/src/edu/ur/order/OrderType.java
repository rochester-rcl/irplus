package edu.ur.order;

/**
 * This represents the type of ordering.  Generally used
 * for database ordering.
 * 
 * @author Nathan Sarr
 *
 */
public class OrderType {
	
	public static final OrderType ASCENDING_ORDER = new OrderType("asc");
	public static final OrderType DESCENDING_ORDER = new OrderType("desc");
	
	
	/** Order type name */
	private String type;

	/**
	 * Constructor 
	 * 
	 * @param type
	 */
	private OrderType(String type)
	{
	    this.type = type;	
	}
	
	public String getType() {
		return type;
	}
	
	public String toString()
	{
		return type;
	}
	
	public int hashCode()
	{
		int hashCode = 0;
		hashCode += type != null ? type.hashCode() : 0;
		return hashCode;
	}
	
	public boolean equals(Object o)
	{
    	if (this == o) return true;
		if (!(o instanceof OrderType)) return false;

		final OrderType other = (OrderType) o;

		if( ( type != null && !type.equals(other.getType()) ) ||
			( type == null && other.getType() != null ) ) return false;
		
		return true;
	}

}
