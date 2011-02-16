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
 * group workspace group.
 * 
 * @author Nathan Sarr
 *
 */
public class InvitedGroupWorkspaceGroupUser extends BasePersistent{
	
	/* eclipse generated id */
	private static final long serialVersionUID = -2662145849417211639L;

	/* Group the user was invited to join */
	private GroupWorkspaceGroup group;
	
	/* email of the user invited */
	private String email;
	
	/* Invite message */
	private String inviteMessage;

	/* User sending the invitation */
	private IrUser inviteingUser;

	/* Token sent to the user */
	private String token; 
	
	/* date the invite info was created */
	private Timestamp createdDate;
	
	/**
	 * Package protected constructor
	 */
	InvitedGroupWorkspaceGroupUser(){}
	
	/**
	 * Create the group invite.
	 * 
	 * @param email - email of the user being invited
	 * @param group - group the user is being invited to
	 * @param inviteingUser - user doing the inviting
	 * @param token - unique token for the invite
	 */
	public InvitedGroupWorkspaceGroupUser(String email, 
			GroupWorkspaceGroup group, 
			IrUser inviteingUser, 
			String token )
	{
		this.email = email;
		this.group = group;
		this.inviteingUser = inviteingUser;
		this.token = token;
		this.createdDate = new Timestamp(new Date().getTime());
	}
	
	/**
	 * Get the group this user was invited to.
	 * 
	 * @return group user was invited to
	 */
	public GroupWorkspaceGroup getGroup() {
		return group;
	}

	/**
	 * Email of the user being invited.
	 * 
	 * @return - email of the user being invited.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Get the invite message.
	 * 
	 * @return - invite message
	 */
	public String getInviteMessage() {
		return inviteMessage;
	}

	/**
	 * Set the invite message.
	 * 
	 * @param inviteMessage
	 */
	public void setInviteMessage(String inviteMessage) {
		this.inviteMessage = inviteMessage;
	}

	/**
	 * Get the token for the invite.
	 * 
	 * @return - token for the invite
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Get the created date for the invite.
	 * 
	 * @return - date record was created.
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * Get the user who made the invite.
	 * 
	 * @return - user who did the inviting
	 */
	public IrUser getInviteingUser() {
		return inviteingUser;
	}

	/**
	 * Hash code is based on group and email
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += email == null ? 0 : email.hashCode();
		value += group == null ? 0 : group.hashCode();
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
		if (!(o instanceof InvitedGroupWorkspaceGroupUser)) return false;

		final InvitedGroupWorkspaceGroupUser other = (InvitedGroupWorkspaceGroupUser) o;

		if( ( email != null && !email.equalsIgnoreCase(other.getEmail()) ) ||
			( email == null && other.getEmail() != null ) ) return false;

		if( ( group != null && !group.equals(other.getGroup()) ) ||
			( group == null && other.getGroup() != null ) ) return false;
		
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Invited Group Workspace Group User id = ");
		sb.append(id);
		sb.append( " email = ");
		sb.append(email);
		sb.append(" token  = ");
		sb.append(token);
		sb.append("]");
		return sb.toString();
	}

}
