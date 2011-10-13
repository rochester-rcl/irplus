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

import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.invite.InviteToken;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a user invited by email this user either does not yet
 * exist in the system or was invited by an unknown email.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceEmailInvite extends BasePersistent{
	
	/* eclipse generated id */
	private static final long serialVersionUID = 1027832070910545487L;

	/* Group workspace the user was invited to join */
	private GroupWorkspace groupWorkspace;
	
	 /* token for the invite  */
    private InviteToken inviteToken; 

	/* indicates the user is an owner of the group workspace */
	private boolean setAsOwner = false;
	
	/* Permissions given to the user */
	private Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();


	/**
	 * Package protected constructor
	 */
	GroupWorkspaceEmailInvite(){}
	
	/**
	 * Create the workspace invite for a user who does not yet
	 * exist in the system.
	 * 
	 * @param groupWorkspace - workspace the user is being invited to
	 * @param permissions - permissions to be given to the user on the group
	 * @param invite token - token for the invite
	 */
	public GroupWorkspaceEmailInvite( GroupWorkspace groupWorkspace, 
			Set<IrClassTypePermission> permissions, InviteToken inviteToken)
	{
		this.groupWorkspace = groupWorkspace;
		this.inviteToken = inviteToken;
		this.permissions = permissions;
	}
	
	/**
	 * Create the workspace invite for a user who does not yet
	 * exist in the system.
	 * 
	 * @param groupWorkspace - workspace the user is being invited to
	 * @param permissions - permissions to be given to the user on the group
	 * @param invite token - token for the invite
	 * @param setAsOwner - true indicates this user will be an owner of the group workspace
	 */
	public GroupWorkspaceEmailInvite( GroupWorkspace groupWorkspace, Set<IrClassTypePermission> permissions, 
			InviteToken inviteToken, boolean setAsOwner)
	{
		this(groupWorkspace, permissions, inviteToken);
		setSetAsOwner(setAsOwner);
	}
	

	
	/**
	 * Get the group this user was invited to.
	 * 
	 * @return group user was invited to
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}

	/**
	 * Hash code is based on group and email
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += inviteToken == null ? 0 : inviteToken.hashCode();
		return value;
	}
	
	/**
	 * Equals is tested based on email ignoring case and group
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceEmailInvite)) return false;

		final GroupWorkspaceEmailInvite other = (GroupWorkspaceEmailInvite) o;

		if( ( inviteToken != null && !inviteToken.equals(other.getInviteToken()) ) ||
			( inviteToken == null && other.getInviteToken() != null ) ) return false;
		
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ Group Workspace Email invite  User id = ");
		sb.append(id);
		sb.append(" token = ");
		sb.append(inviteToken);
		sb.append("]");
		return sb.toString();
	}
	


	/**
	 * Get the invite token information.
	 * 
	 * @return - invite token information.
	 */
	public InviteToken getInviteToken() {
		return inviteToken;
	}
	
	/**
	 * True indicates the user is to be set as an owner.
	 * 
	 * @return
	 */
	public boolean isSetAsOwner() {
		return setAsOwner;
	}

	/**
	 * Set the user as the owner 
	 * 
	 * @param setAsOwner
	 */
	public void setSetAsOwner(boolean setAsOwner) {
		this.setAsOwner = setAsOwner;
	}
	
	/**
	 * Permissions to give to the user.
	 * 
	 * @return
	 */
	public Set<IrClassTypePermission> getPermissions() {
		return permissions;
	}

	/**
	 * Permissions to give to the user.
	 * 
	 * @param permissions
	 */
	public void setPermissions(Set<IrClassTypePermission> permissions) {
		this.permissions = permissions;
	}



}
