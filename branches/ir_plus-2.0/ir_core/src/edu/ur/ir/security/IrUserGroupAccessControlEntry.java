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

import edu.ur.ir.user.IrUserGroup;

/**
 * The access control entry for the group.
 * 
 * @author Nathan Sarr
 *
 */
public class IrUserGroupAccessControlEntry extends IrAbstractAccessControlEntry{

	/**  Eclipse generated id. */
	private static final long serialVersionUID = -3000020912273126297L;
	
	/**  The group for this access control entry.  */
	private IrUserGroup userGroup;
	
	/**
	 * Package protected constructor
	 */
	IrUserGroupAccessControlEntry(){};
	
	/**
	 * Group access control entry.
	 * 
	 * @param group with permission
	 * @param irAcl acl group.
	 */
	public IrUserGroupAccessControlEntry(IrUserGroup group, IrAcl irAcl)
	{
		setIrAcl(irAcl);
		setUserGroup(group);
	}
	
	/**
	 * 
	 * @see edu.ur.ir.security.AccessControlEntry#getSid()
	 */
	public IrUserGroup getSid() {
		return userGroup;
	}

	/**
	 * Get the group.
	 * 
	 * @return
	 */
	public IrUserGroup getUserGroup() {
		return userGroup;
	}

	/**
	 * Set the group for this access control entry.
	 * 
	 * @param userGroup
	 */
	void setUserGroup(IrUserGroup irGroup) {
		this.userGroup = irGroup;
	}
	
	/**
	 * Return the has code 
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value+= userGroup == null ? 0 : userGroup.hashCode();
		value+= irAcl == null ? 0 : irAcl.hashCode();
		
		return value;
	}
	
	/**
	 * Test IrGroup access control entry equality
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IrUserGroupAccessControlEntry)) return false;

		final IrUserGroupAccessControlEntry other = (IrUserGroupAccessControlEntry) o;

		if( ( userGroup != null && !userGroup.equals(other.getUserGroup()) ) ||
			( userGroup == null && other.getUserGroup() != null ) ) return false;
		
		if( ( irAcl != null && !irAcl.equals(other.getIrAcl()) ) ||
			( irAcl == null && other.getIrAcl() != null ) ) return false;

		return true;
	}
	
	/**
	 * Method that returns this object as a string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[id = ");
		sb.append(id);
		
		if( userGroup != null )
		{
		    sb.append(" group name = " );
		    sb.append(userGroup);
		}
		
		sb.append("]");
		return sb.toString();
 	}

}
