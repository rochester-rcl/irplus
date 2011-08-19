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

import edu.ur.ir.invite.InviteToken;
import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 * Indicates a user who has been invited to be part of a
 * group workspace group.  This includes two different types of users
 * one who is already part of the system and one who has not yet
 * registered as part of the system.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceGroupInvite extends BasePersistent{
	
	/* eclipse generated id */
	private static final long serialVersionUID = -2662145849417211639L;

	/* Group the user was invited to join */
	private GroupWorkspaceGroup group;
	
	 /* token for the invite  */
    private InviteToken inviteToken;
    
    /* user invited  */
	private IrUser invitedUser;



	/**
	 * Package protected constructor
	 */
	GroupWorkspaceGroupInvite(){}
	
	/**
	 * Create the group invite for a user who does not yet
	 * exist in the system.
	 * 
	 * @param group - group the user is being invited to
	 * @param invite token - token for the invite
	 */
	public GroupWorkspaceGroupInvite( GroupWorkspaceGroup group, InviteToken inviteToken)
	{
		this.group = group;
		this.inviteToken = inviteToken;
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
		if (!(o instanceof GroupWorkspaceGroupInvite)) return false;

		final GroupWorkspaceGroupInvite other = (GroupWorkspaceGroupInvite) o;

		if( ( inviteToken != null && !inviteToken.equals(other.getInviteToken()) ) ||
			( inviteToken == null && other.getInviteToken() != null ) ) return false;
		
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Invited Group Workspace Group User id = ");
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

}
