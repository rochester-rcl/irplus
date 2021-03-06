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

import edu.ur.ir.user.IrUser;

/**
 * Represents an access control entry for a specific user.
 * 
 * @author Nathan Sarr
 *
 */
public class IrUserAccessControlEntry extends IrAbstractAccessControlEntry {

	/**  Eclipse generated id. */
	private static final long serialVersionUID = -850918808258723219L;
	
	/**  User name that has permissions to this object  */
	private IrUser irUser;

	/**
	 * Package protected constructor.
	 */
	IrUserAccessControlEntry(){}
	
	/**
	 * Default constructor.
	 * 
	 * @param irUser - user who is in the control entry
	 * @param irAcl - parent access control list
	 */
	public IrUserAccessControlEntry(IrUser irUser, IrAcl irAcl)
	{
		this.irAcl = irAcl;
		this.irUser = irUser;
	}
	
	/**
	 * Get the sid (Secure identity)
	 * 
	 * @return
	 */
	public IrUser getSid() {
		return irUser;
	}
	
	/**
	 * Get the ir User
	 * 
	 * @return
	 */
	public IrUser getIrUser() {
		return irUser;
	}

	/**
	 * Set the ir User
	 * @param irUser
	 */
	void setIrUser(IrUser irUser) {
		this.irUser = irUser;
	}
	
	/**
	 * Return the has code 
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value+= irUser == null ? 0 : irUser.hashCode();
		value+= irAcl == null ? 0 : irAcl.hashCode();
		
		return value;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[ id = ");
		sb.append(this.getId());
		if( getAcl() != null )
		{
			sb.append( " acl  = ");
			sb.append(getAcl().toString());
		}
		if( getIrUser() != null )
		{
			sb.append( " user  = ");
			sb.append(getIrUser().toString());
		}
		sb.append("]");

		return sb.toString();
	}
	
	/**
	 * Test user access control entry equality
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IrUserAccessControlEntry)) return false;

		final IrUserAccessControlEntry other = (IrUserAccessControlEntry) o;

		if( ( irUser != null && !irUser.equals(other.getIrUser()) ) ||
			( irUser == null && other.getIrUser() != null ) ) return false;

		if( ( irAcl != null && !irAcl.equals(other.getIrAcl()) ) ||
			( irAcl == null && other.getIrAcl() != null ) ) return false;
		return true;
	}
}
