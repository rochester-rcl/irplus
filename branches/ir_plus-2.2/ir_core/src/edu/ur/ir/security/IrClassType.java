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

package edu.ur.ir.security;

import edu.ur.cgLib.CgLibHelper;
import edu.ur.persistent.BasePersistent;

/**
 * Represents the class type that is being secured.  The
 * name is the fully qualified class name.
 * 
 * @author Nathan Sarr
 *
 */
public class IrClassType extends BasePersistent{

	
	/** Name of the class type */
	private String name;
	
	/** Description of the class type */
	private String description;
	
	/**  Eclipse Generated id. */
	private static final long serialVersionUID = 4720833232516110273L;
	
	/**
	 * Create a class type with the specified class type.
	 * 
	 * @param c
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public IrClassType(Class c) throws ClassNotFoundException
	{
		this(c.getName());
	}
	
	
	/** 
	 * Constructor that takes the class name.  This validates
	 * the class exists
	 * 
	 * @param className
	 * @throws ClassNotFoundException 
	 */
	public IrClassType(String className) throws ClassNotFoundException 
	{
		name = CgLibHelper.cleanClassName(className);
		Class.forName(name);
	}
	
	/**
	 * Package protected constructor. 
	 */
	IrClassType(){}
		
	/**
	  * Get the java class type.
	  * 
	  * @see edu.ur.ir.security.ObjectIdentity#getJavaType()
	  */
	@SuppressWarnings("unchecked")
	public Class getJavaType() {
		try
		{
		    return Class.forName(getName());
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IrClassType)) return false;

		final IrClassType other = (IrClassType) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;

		return true;
	}
	
	/**
	 * The to string operation.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ name = ");
		sb.append(name);
		sb.append(" id = ");
		sb.append(id);
		sb.append(" description = ");
		sb.append(description);
		sb.append("]");
		return sb.toString();
	}


	/**
	 * Get the description of the class.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * Description of the class.
	 * 
	 * @param description
	 */
	void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Name of the class.
	 * 
	 * @return the fully qualified name of the calss
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the class.
	 * 
	 * @param name
	 */
	void setName(String name) {
		this.name = name;
	}

}
