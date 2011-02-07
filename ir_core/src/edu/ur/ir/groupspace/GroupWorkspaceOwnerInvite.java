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
 * Represents an invitation to join a group workspace.  This can
 * be either someone who does not yet exist in the system or
 * already exists as part of the IR+ system.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceOwnerInvite extends BasePersistent{

	/* eclipse generated id  */
	private static final long serialVersionUID = 7228329226519598249L;
	
	/* Email id - to send the invitation to if needed */
	private String email;
	
	/* Token sent to the user if needed*/
	private String token;
	
	/* user invited  */
	private IrUser invitedUser;

	/* Invite message */
	private String inviteMessage;

	/* User sending the invitation */
	private IrUser invitingUser;
	
	/* date the invite info was created */
	private Timestamp createdDate;

	/* Group workspace user is being invited to */
	private GroupWorkspace groupWorkspace;
	
	/**
	 * Package protected constructor
	 */
	GroupWorkspaceOwnerInvite() {}
	
	/**
	 *  Constructor 
	 */
	public GroupWorkspaceOwnerInvite(GroupWorkspace workspace, IrUser invitingUser, String email) {
		
		this.createdDate = new Timestamp(new Date().getTime());
	}

	/**
	 *  Constructor 
	 */
	public GroupWorkspaceOwnerInvite(GroupWorkspace workspace, IrUser invitingUser, IrUser invitedUser) {
		this.createdDate = new Timestamp(new Date().getTime());
	}
	/**
	 * Get the Email ID
	 * 
	 * @return Email Id 
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set the Email Id
	 * 
	 * @param email Email to which invitation was sent
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Get the token
	 * 
	 * @return token 
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Set the token
	 * 
	 * @param token token for user
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * Date the record was created.
	 * 
	 * @return - date the record was created.
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	

	/**
	 * Set the date created.
	 * 
	 * @param dateCreated
	 */
	void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceOwnerInvite)) return false;

		final GroupWorkspaceOwnerInvite other = (GroupWorkspaceOwnerInvite) o;

		if( ( token != null && !token.equals(other.getToken()) ) ||
			( token == null && other.getToken() != null ) ) return false;

		if( ( email != null && !email.equals(other.getEmail()) ) ||
			( email == null && other.getEmail() != null ) ) return false;
		
		if( ( groupWorkspace != null && !groupWorkspace.equals(other.getGroupWorkspace()) ) ||
			( groupWorkspace == null && other.getGroupWorkspace() != null ) ) return false;

		if( ( invitingUser != null && !invitingUser.equals(other.getInvitingUser()) ) ||
			( invitingUser == null && other.getInvitingUser() != null ) ) return false;

		return true;
	}


	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += token == null ? 0 : token.hashCode();
		value += email == null ? 0 : email.hashCode();
		value += inviteMessage == null ? 0 : inviteMessage.hashCode();
		return value;
	}


	/**
	 * Get invitation message
	 *  
	 * @return message in the invitation
	 */
	public String getInviteMessage() {
		return inviteMessage;
	}

	/**
	 * Set message for the invitation
	 * 
	 * @param inviteMessage message for the invitation
	 */
	public void setInviteMessage(String inviteMessage) {
		this.inviteMessage = inviteMessage;
	}
	
	/**
	 * Get the user invited to be an owner.
	 * 
	 * @return
	 */
	public IrUser getInvitedUser() {
		return invitedUser;
	}

	/**
	 * Set the invitied user.
	 * 
	 * @param invitedUser
	 */
	public void setInvitedUser(IrUser invitedUser) {
		this.invitedUser = invitedUser;
	}

	/**
	 * Get the inviting user.
	 * 
	 * @return
	 */
	public IrUser getInvitingUser() {
		return invitingUser;
	}

	/**
	 * Set the inviting user.
	 * 
	 * @param invitingUser
	 */
	public void setInvitingUser(IrUser invitingUser) {
		this.invitingUser = invitingUser;
	}

	/**
	 * Get the group workspace.
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}

	/**
	 * Set the group workspace.
	 * 
	 * @param groupWorkspace
	 */
	public void setGroupWorkspace(GroupWorkspace groupWorkspace) {
		this.groupWorkspace = groupWorkspace;
	}


	/**
	 * To string of the invite info.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ invite info id = ");
		sb.append(id);
		sb.append("email = ");
		sb.append(email);
		sb.append(" token = ");
		sb.append(token);
		sb.append(" invite message = ");
		sb.append(inviteMessage);
		sb.append("]");
		return sb.toString();
	}

}
