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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserEmail;
import edu.ur.persistent.CommonPersistent;

/**
 * Represents a group of users within a group work space.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceGroup extends CommonPersistent{
	
	/* eclipse generated id */
	private static final long serialVersionUID = 186970723642439395L;

	/* list of users in the group */
	private Set<IrUser> users = new HashSet<IrUser>();
	
	/* users invited to join the group workspace group */
	private Set<GroupWorkspaceGroupInvite> invitedUsers = new HashSet<GroupWorkspaceGroupInvite>();
	
	/* Owning group Space  */
	private GroupWorkspace groupWorkspace;
	
	/* lower case name value */
	private String lowerCaseName;

	/**
	 * Package protected constructor
	 */
	GroupWorkspaceGroup(){}
	
	/**
	 * Create a group space with the given name.
	 * 
	 * @param name
	 */
	public GroupWorkspaceGroup(GroupWorkspace groupWorkspace, String name)
	{
		this.groupWorkspace = groupWorkspace;
		setName(name);
	}
	
	/**
	 * Create a group space with the given name and description.
	 * 
	 * @param groupSpace
	 * @param name
	 * @param description
	 */
	public GroupWorkspaceGroup(GroupWorkspace groupSpace, String name, String description)
	{
		this(groupSpace, name);
		setDescription(description);
	}

	/**
	 * Get the list of users in this group space.
	 * 
	 * @return
	 */
	public Set<IrUser> getUsers() {
		return Collections.unmodifiableSet(users);
	}

	/**
	 * Set the users in this group space group.
	 * 
	 * @param users
	 */
	void setUsers(Set<IrUser> users) {
		this.users = users;
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
	 * Get the lower case name value.
	 * 
	 * @return
	 */
	public String getLowerCaseName() {
		return lowerCaseName;
	}
	
	/**
	 * Set the name - also sets the lower case name value.
	 * 
	 * @see edu.ur.persistent.CommonPersistent#setName(java.lang.String)
	 */
	public void setName(String name)
	{
		super.setName(name);
		this.lowerCaseName = name.toLowerCase();
	}
	
	/**
	 * Add a user to the group.
	 * 
	 * @param user - user to add to the group
	 */
	public void addUser(IrUser user)
	{
		users.add(user);
	}
	
	/**
	 * Remove a user from the group.
	 * 
	 * @param user - user to remove from the group
	 * @return true if the user is removed from the gorup
	 */
	public boolean removeUser(IrUser user)
	{
		return users.remove(user);
	}
	
	/**
	 * Get the invited user by email - ignores case.  Retun the invite if found
	 * otherwise return null.
	 * 
	 * @param email
	 * @return invite fi found otherwise null.
	 */
	public GroupWorkspaceGroupInvite getInvite(String email)
	{
		for(GroupWorkspaceGroupInvite invite : invitedUsers)
		{
			if( invite.getEmail().equalsIgnoreCase(email))
			{
				return invite;
			}
		}
		return null;
	}
	
	/**
	 * Get the invite for a user who has created an account.
	 * 
	 * @param user - user who exists in the system
	 * @return the workspace group invite if one exists otherwise null
	 */
	public GroupWorkspaceGroupInvite getInvite(IrUser user)
	{
		if( user != null )
		{
		    for(GroupWorkspaceGroupInvite invite : invitedUsers)
		    {
			    for( UserEmail email : user.getUserEmails())
			    {
				    if(email.getEmail().equalsIgnoreCase(invite.getEmail()))
				    {
					    return invite;
				    }
			    } 
		    }
		}
		return null;
	 }
	
	/**
	 * Returns the user if they are a member of the users othwerwise returns null.
	 * 
	 * @param email - email of the user to look for
	 * @return user if found otherwise null
	 */
	public IrUser getUser(String email)
	{
		for(IrUser user : users)
		{
			if( user.getUserEmail(email) != null)
			{
				return user;
			}
		}
		return null;
	}
	
	/**
	 * Create an invite for a user who does not yet exist in the system
	 * 
	 * @param email - email 
	 * @param inviteingUser - user doing the invite
	 * @param token - token for the invite
	 * 
	 * @return - the created invite
	 * @throws GroupWorkspaceInviteException 
	 */
	public GroupWorkspaceGroupInvite inviteUser(String email,
			IrUser inviteingUser, 
			String token ) throws GroupWorkspaceInviteException
	{
		IrUser user = getUser(email);
		if( user != null )
		{
			throw new GroupWorkspaceInviteException("user already exists");
		}
		GroupWorkspaceGroupInvite invite = getInvite(email);
		
		if(  invite == null )
		{
			invite = new GroupWorkspaceGroupInvite(email, 
			    this, 
			    inviteingUser, 
			    token);
		    invitedUsers.add(invite);
		}
		return invite;
	}
	
	/**
	 * Create an invite for a user who exists in the system.
	 * 
	 * @param invitedUser
	 * @param inviteingUser
	 * 
	 * @return
	 * @throws GroupWorkspaceInviteException 
	 */
	public GroupWorkspaceGroupInvite inviteUser(IrUser invitedUser,
			IrUser inviteingUser, String token) throws GroupWorkspaceInviteException
	{
		if( users.contains(invitedUser) )
		{
			throw new GroupWorkspaceInviteException("user already exists");
		}
		
		GroupWorkspaceGroupInvite invite = getInvite(invitedUser);
		if(  invite == null )
		{
			invite = new GroupWorkspaceGroupInvite(invitedUser, 
			    this, 
			    inviteingUser, 
			    token);
		    invitedUsers.add(invite);
		}
		return invite;
	}
	
	/**
	 * Delete the invite if found.  If the invite is not found
	 * this returns true as it is not part of the set.
	 * 
	 * @param email - remove the invite based on email - ignores case
	 * @return true if the invite is deleted.
	 */
	public boolean deleteInvite(String email)
	{
		GroupWorkspaceGroupInvite invite = getInvite(email);
		if( invite != null )
		{
			return invitedUsers.remove(invite);
		}
		return true;
	}
	
	
	/**
	 * Set of users invited to join the group.
	 * 
	 * @return unmodifiable set of invited users
	 */
	public Set<GroupWorkspaceGroupInvite> getInvitedUsers() {
		return Collections.unmodifiableSet(invitedUsers);
	}

	/**
	 * Set of users invited to join the group.
	 * 
	 * @param invitedUsers
	 */
	void setInvitedUsers(Set<GroupWorkspaceGroupInvite> invitedUsers) {
		this.invitedUsers = invitedUsers;
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
		value += name == null ? 0 : name.hashCode();
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
		if (!(o instanceof GroupWorkspaceGroup)) return false;

		final GroupWorkspaceGroup other = (GroupWorkspaceGroup) o;

		if( ( name != null && !name.equalsIgnoreCase(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;

		if( ( groupWorkspace != null && !groupWorkspace.equals(other.getGroupWorkspace()) ) ||
			( groupWorkspace == null && other.getGroupWorkspace() != null ) ) return false;

		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ Group space id = ");
		sb.append(id);
		sb.append( " name = ");
		sb.append(name);
		sb.append(" description = ");
		sb.append(description);
		sb.append("]");
		return sb.toString();
	}


}