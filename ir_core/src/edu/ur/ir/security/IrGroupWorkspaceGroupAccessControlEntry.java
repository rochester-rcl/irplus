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

import edu.ur.ir.groupspace.GroupWorkspaceGroup;

/**
 * The access control entry for a group workspace group.
 * 
 * @author Nathan Sarr
 *
 */
public class IrGroupWorkspaceGroupAccessControlEntry extends IrAbstractAccessControlEntry{
	
	/* eclipse generated id.  */
	private static final long serialVersionUID = -7191915583686456626L;
	
	/*  The group for this access control entry.  */
	private GroupWorkspaceGroup workspaceGroup;

	/**
	 * Package protected constructor
	 */
	IrGroupWorkspaceGroupAccessControlEntry(){};
	
	/**
	 * Group access control entry.
	 * 
	 * @param group with permission
	 * @param irAcl acl group.
	 */
	public IrGroupWorkspaceGroupAccessControlEntry(GroupWorkspaceGroup group, IrAcl irAcl)
	{
		setIrAcl(irAcl);
		setWorkspaceGroup(group);
	}
	
	/**
	 * 
	 * @see edu.ur.ir.security.AccessControlEntry#getSid()
	 */
	public GroupWorkspaceGroup getSid() {
		return workspaceGroup;
	}

	/**
	 * Get the group workspace group for this Access control entry
	 * 
	 * @return
	 */
	public GroupWorkspaceGroup getWorkspaceGroup() {
		return workspaceGroup;
	}

	/**
	 * Set the group for this access control entry.
	 * 
	 * @param group
	 */
	public void setWorkspaceGroup(GroupWorkspaceGroup group) {
		this.workspaceGroup = group;
	}
	
	/**
	 * Return the has code 
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value+= workspaceGroup == null ? 0 : workspaceGroup.hashCode();
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
		if (!(o instanceof IrGroupWorkspaceGroupAccessControlEntry)) return false;

		final IrGroupWorkspaceGroupAccessControlEntry other = (IrGroupWorkspaceGroupAccessControlEntry) o;

		if( ( workspaceGroup != null && !workspaceGroup.equals(other.getWorkspaceGroup()) ) ||
			( workspaceGroup == null && other.getWorkspaceGroup() != null ) ) return false;
		
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
		
		if( workspaceGroup != null )
		{
		    sb.append(" group  = " );
		    sb.append(workspaceGroup);
		}
		
		sb.append("]");
		return sb.toString();
 	}
}
