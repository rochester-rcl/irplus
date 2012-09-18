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

import edu.ur.ir.user.IrRole;

/**
 * The access control for a given role.
 * 
 * @author Nathan Sarr
 *
 */
public class IrRoleAccessControlEntry extends IrAbstractAccessControlEntry{

	/**
	 * Eclipse generated id.
	 */
	private static final long serialVersionUID = -17701130332559877L;
	
	/**
	 * The security identity object
	 */
	private IrRole irRole;
	
	/**
	 * Package protected default constructor.
	 */
	IrRoleAccessControlEntry(){};

	/**
	 * Get the security id for this object.
	 * 
	 * @see edu.ur.ir.security.AccessControlEntry#getSid()
	 */
	public IrRole getSid() {
		return irRole;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param irRole the role for this access control entry
	 * @param irAcl the access control list.
	 */
	public IrRoleAccessControlEntry(IrRole irRole, IrAcl irAcl)
	{
		this.irAcl = irAcl;
		this.irRole = irRole;
	}

	/**
	 * The role which contains
	 * 
	 * @return
	 */
	public IrRole getIrRole() {
		return irRole;
	}

	/**
	 * Set the role for this access control entry.
	 * 
	 * @param irRole
	 */
	public void setIrRole(IrRole irRole) {
		this.irRole = irRole;
	}
	
	/**
	 * To string for IrRoleAccessControlEntry
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[ id = ");
		sb.append(this.getId());
		if( getAcl() != null )
		{
			sb.append(" acl = " );
			sb.append(getAcl().toString());
		}
		if( getIrRole() != null )
		{
			sb.append(" role = ");
			sb.append(getIrRole().toString());
		}
		sb.append("]");

		return sb.toString();
	}
	
	/**
	 * Return the has code 
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value+= irRole == null ? 0 : irRole.hashCode();
		value+= irAcl == null ? 0 : irAcl.hashCode();
		
		return value;
	}
	
	/**
	 * Test Role access control entry equality
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IrRoleAccessControlEntry)) return false;

		final IrRoleAccessControlEntry other = (IrRoleAccessControlEntry) o;

		if( ( irRole != null && !irRole.equals(other.getIrRole()) ) ||
			( irRole == null && other.getIrRole() != null ) ) return false;
		
		if( ( irAcl != null && !irAcl.equals(other.getIrAcl()) ) ||
			( irAcl == null && other.getIrAcl() != null ) ) return false;

		return true;
	}

}
