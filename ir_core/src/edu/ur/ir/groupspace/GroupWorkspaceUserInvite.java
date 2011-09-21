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


import java.sql.Timestamp;
import java.util.Date;

import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 * Indicates a user who has been invited to be part of a
 * group workspace.  This is for inviting a user who already
 * exists within the system.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceUserInvite extends BasePersistent{
	
	/* eclipse generated id */
	private static final long serialVersionUID = -2662145849417211639L;

	/* Group workspace the user was invited to join */
	private GroupWorkspace groupWorkspace;
	
    /* user invited  */
	private IrUser invitedUser;
	
	/* email used to invite the existing user */
	private String email;

	/* user doing the inviting */
	private IrUser invitingUser;
	
	/* indicates the user is an owner of the group workspace */
	private boolean setAsOwner = false;
	
	/* date the workspace invite was created */
	private Timestamp createdDate;
	
	/* message to include when inviting user */
	private String inviteMessage;

	/**
	 * Package protected constructor
	 */
	GroupWorkspaceUserInvite(){}
	
	/**
	 * Create the workspace invite for a user who 
	 * exist in the system. Defaults setAsOwner to false.
	 * 	
	 * @param groupWorkspace - group workspace the user is being invited to
	 * @param invitingUser - user who is doing the inviting
	 * @param invitedUser - user who is invited.
	 * @param email - email used to invite the user.
	 */
	public GroupWorkspaceUserInvite( GroupWorkspace groupWorkspace, 
			IrUser invitingUser, IrUser invitedUser, String email)
	{
		createdDate = new Timestamp(new Date().getTime());
		this.groupWorkspace = groupWorkspace;
		setInvitingUser(invitingUser);
		setInvitedUser(invitedUser);
		setEmail(email);
	}
	
	/**
	 * Create the group workspace invite for a user who exists in the system.
	 * 
	 * @param groupWorkspace - group workspace the user is invited to
	 * @param invitingUser - user inviting other users
	 * @param invitedUser - user invited to join the workspace
	 * @param email - email used to invite the user.
	 * @param setAsOwner - set the user as an owner of the project
	 * 
	 */
	public GroupWorkspaceUserInvite( GroupWorkspace groupWorkspace, 
			IrUser invitingUser, IrUser invitedUser, String email, boolean setAsOwner)
	{
		this(groupWorkspace, invitingUser, invitedUser, email);
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
		value += groupWorkspace == null ? 0 : groupWorkspace.hashCode();
		value += invitedUser == null ? 0 : invitedUser.hashCode();
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
		if (!(o instanceof GroupWorkspaceUserInvite)) return false;

		final GroupWorkspaceUserInvite other = (GroupWorkspaceUserInvite) o;

		if( ( groupWorkspace != null && !groupWorkspace.equals(other.getGroupWorkspace()) ) ||
			( groupWorkspace == null && other.getGroupWorkspace() != null ) ) return false;
		
		if( ( invitedUser != null && !invitedUser.equals(other.getInvitedUser()) ) ||
			( invitedUser == null && other.getInvitedUser() != null ) ) return false;
		
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Group workspace existing user invite   id = ");
		sb.append(id);
		sb.append(" set as owner = ");
		sb.append(setAsOwner);
		sb.append(" email = ");
		sb.append(email);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Set the invited user.
	 * 
	 * @param invitedUser
	 */
	public void setInvitedUser(IrUser invitedUser) {
		this.invitedUser = invitedUser;
	}

	/**
	 * Get the invited user if they exist
	 * 
	 * @return
	 */
	public IrUser getInvitedUser() {
		return invitedUser;
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
	 * Date the record was created
	 * 
	 * @return
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * Set the date created.
	 * 
	 * @param dateCreated
	 */
	void setCreatedDate(Timestamp dateCreated) {
		this.createdDate = dateCreated;
	}
	
	/**
	 * Get the user doing the inviting.
	 * 
	 * @return
	 */
	public IrUser getInvitingUser() {
		return invitingUser;
	}

	/**
	 * Set the user doing the inviting.
	 * 
	 * @param invitor
	 */
	void setInvitingUser(IrUser invitor) {
		this.invitingUser = invitor;
	}
	
	/**
	 * Email used to invite the user.
	 * 
	 * @return - email used to invite the user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set the email used to invite the user.
	 * 
	 * @param email
	 */
	void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Get the invite message to send to the user.
	 * 
	 * @return
	 */
	public String getInviteMessage() {
		return inviteMessage;
	}

	/**
	 * Set the invite message to send to the user.
	 * 
	 * @param inviteMessage
	 */
	public void setInviteMessage(String inviteMessage) {
		this.inviteMessage = inviteMessage;
	}

}
