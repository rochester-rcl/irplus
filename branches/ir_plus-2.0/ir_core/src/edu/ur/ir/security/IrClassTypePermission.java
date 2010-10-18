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

import edu.ur.persistent.CommonPersistent;

/**
 * Class type permissions.  This allows the descriptions and
 * names of the permissions to be class specific even
 * though all permissions really come from <code>Permission</code> 
 * and hopefully almost all can be grouped under one of these <code>Permission</code>(s)
 * 
 * This is used to give users the descriptive capabilities of the permission.  This 
 * allows the developers to rename the permission so that it makes more sense.
 * 
 * @author Nathan Sarr
 *
 */
public class IrClassTypePermission extends CommonPersistent {

	/**  Eclipse generated id. */
	private static final long serialVersionUID = 7420969222677474986L;
	
	/**  The class type for this permission */
	private IrClassType irClassType;
	
	/**
	 * Package protected constructor. 
	 */
	IrClassTypePermission(){}
	
	/**
	 * Create a class type permission.
	 * 
	 * @param irClassType
	 */
	public IrClassTypePermission(IrClassType irClassType)
	{
		this.irClassType = irClassType;
	}

	/**
	 * Class type this represents the permission for
	 * 
	 * @return
	 */
	public IrClassType getIrClassType() {
		return irClassType;
	}

	/**
	 * Class type this represents the permission for
	 * 
	 * @param irClassType
	 */
	void setIrClassType(IrClassType irClassType) {
		this.irClassType = irClassType;
	}

	public int hashCode()
	{
		int value = 0;
		value += irClassType == null ? 0 : irClassType.hashCode();
		value += name == null ? 0 : name.hashCode();
		
		return value;
	}
	
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IrClassTypePermission)) return false;

		final IrClassTypePermission other = (IrClassTypePermission) o;

		if( ( irClassType != null && !irClassType.equals(other.getIrClassType()) ) ||
			( irClassType == null && other.getIrClassType() != null ) ) return false;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		
		if( irClassType != null)
		{
			sb.append(irClassType.toString());
		}
		sb.append("Id= "+id);
		sb.append("name=" + name);
		sb.append(" description = " + description);
		sb.append("]");
		
		return sb.toString();
	}
}
