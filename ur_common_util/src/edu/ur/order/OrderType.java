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

package edu.ur.order;

/**
 * This represents the type of ordering.  Generally used
 * for database ordering.
 * 
 * @author Nathan Sarr
 *
 */
public class OrderType {
	
	/** Ascending order type  */
	public static final OrderType ASCENDING_ORDER = new OrderType("asc");
	
	
	/** Descending order type  */
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
	
	/**
	 * Will compare the string values and return the order type
	 * valid values are "asc", "ascending" for ascending order type 
	 * and "desc", "descending" for descending order type.
	 * 
	 * @param type
	 * @return the correct order type or a newly created order type or null if
	 * the order type is not found.
	 */
	public static OrderType getOrderType(String type)
	{
		if( type.equalsIgnoreCase("asc") || type.equalsIgnoreCase("ascending"))
		{
			return ASCENDING_ORDER;
		}
		else if( type.equalsIgnoreCase("desc") || type.equalsIgnoreCase("descending"))
		{
			return DESCENDING_ORDER;
		} else {
			return ASCENDING_ORDER;
		}
		
	}
	
	/**
	 * Get the type as a string.
	 * 
	 * @return
	 */
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
