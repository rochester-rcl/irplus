/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.groupspace;


import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a users who belongs to a group workspace.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceUser extends BasePersistent {
	
	/* eclipse generated id */
	private static final long serialVersionUID = 186970723642439395L;
	
	/* user who belongs to the workspace */
	private IrUser user;
	
	/* group workspace this user belongs to */
	private GroupWorkspace groupWorkspace;
	
	/* indicates the user is an owner of the workspace */
	private boolean owner = false;
	

	/**
	 * Package protected constructor
	 */
	GroupWorkspaceUser(){}
	
	/**
	 * Create a group workspace user. Defaults owner to false
	 * 
	 * @param groupWorkspace - group workspace the user belongs to
	 * @param user - user who belongs to the group workspace
	 */
	public GroupWorkspaceUser(GroupWorkspace groupWorkspace, IrUser user)
	{
		setGroupWorkspace(groupWorkspace);
		setUser(user);
	}

	
	/**
	 * @param groupWorkspace - group workspace the user belongs to
	 * @param user - user who belongs to the group workspace
	 * @param setAsOwner - explicitly set user ownership role
	 */
	public GroupWorkspaceUser(GroupWorkspace groupWorkspace, IrUser user, boolean setAsOwner)
	{
		this(groupWorkspace, user);
		setOwner(setAsOwner);
	}

	
	/**
	 * Get the group space.
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}

	/**
	 * Set the group space.
	 * 
	 * @param groupSpace
	 */
	void setGroupWorkspace(GroupWorkspace groupSpace) {
		this.groupWorkspace = groupSpace;
	}

	
	/**
	 * Hash code is based on the name of
	 * the group space
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += user == null ? 0 : user.hashCode();
		value += groupWorkspace == null ? 0 : groupWorkspace.hashCode();
		return value;
	}
	
	/**
	 * Equals is tested based on name ignoring case 
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceUser)) return false;
		final GroupWorkspaceUser other = (GroupWorkspaceUser) o;

		if( ( user != null && !user.equals(other.getUser()) ) ||
			( user == null && other.getUser() != null ) ) return false;
		if( ( groupWorkspace != null && !groupWorkspace.equals(other.getGroupWorkspace()) ) ||
			( groupWorkspace == null && other.getGroupWorkspace() != null ) ) return false;
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ Group workspace user  id = ");
		sb.append(id);
		sb.append(" user = ");
		sb.append(user);
		sb.append(" group workspace = ");
		sb.append(groupWorkspace);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Get the user who is part of the group workspace.
	 * 
	 * @return
	 */
	public IrUser getUser() {
		return user;
	}
	
	/**
	 * Get the user who is part of the group workspace.
	 * 
	 * @return
	 */
	void setUser(IrUser user) {
		this.user = user;
	}

	/**
	 * Determine if the user is the owner of the group workspace.
	 * 
	 * @return
	 */
	public boolean isOwner() {
		return owner;
	}
	
	/**
	 * Determine if the user is the owner of the group workspace.
	 * 
	 * @return
	 */
	public boolean getOwner() {
		return owner;
	}

	/**
	 * Set the user as an owner of this group workspace
	 * 
	 * @param owner
	 */
	public void setOwner(boolean owner) {
		this.owner = owner;
	}
	
}
