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

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UnVerifiedEmailException;



/**
 * Service to deal with users who are invited to join a group
 * of a group workspace.  This provides an interface to deal with
 * both users who are already existing members of the system as 
 * well as those who have not yet become members of the system.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceInviteService extends Serializable
{
	/**
	 * Find the Invite information for a specified token
	 * 
	 * @param token user token
	 * @return User token information
	 */
	public GroupWorkspaceEmailInvite getByToken(String token);
	
	/**
	 * Find the Invite information for a specified email
	 * 
	 * @param email email address shared with
	 * @return List of invite information
	 */
	public List<GroupWorkspaceEmailInvite> getByEmail(String email);
	
	
	/**
	 * Get the invited group workspace email invite by id.
	 * 
	 * @param id - id of the invite id
	 * @param lock - upgrade the lock.
	 * 
	 * @return the invite if found.
	 */
	public GroupWorkspaceEmailInvite getEmailInviteById(Long id, boolean lock);
	
	
	/**
	 * Invite the specified users with the given emails.  This will determine if the emails
	 * already exist for a user in the system.  If the user already exists in the system a
	 * group workspace invite will be added to the users group display for approval
	 * 
	 * @param invitingUser - user doing the inviting
	 * @param emails - list of emails of users to invite to join the group.
	 * @param setAsOwner - set the users as owners of the group
	 * @param permissions - permissions to give the group
	 * @param groupWorkspace - group workspaces to add the user to
	 * @param inviteMessage - message to send to users
	 * 
	 * @return list of emails that were found to be invalid or could not be sent a message
	 * @throws PermissionNotGrantedException - if the user does not have permissions to invite users into a group
	 */
	public List<String> inviteUsers(IrUser invitingUser, 
			List<String> emails, 
			boolean setAsOwner,
			Set<IrClassTypePermission> permissions, 
			GroupWorkspace groupWorkspace, 
			String inviteMessage) throws PermissionNotGrantedException;
	
	/**
	 * Make the invite record persistent.
	 * 
	 * @param entity
	 */
	public void save(GroupWorkspaceEmailInvite entity);
	
	/**
	 * Sends an email notifying the specified user that  they have been add
	 * to a group workspace.  This should only be used for existing users of
	 * the IR+ system.  If they are not yet users use sendEmailInvite(GroupWorkspaceEmailInvite invite)
	 *
	 * @param invitingUser - user doing the inviting
	 * @param groupWorkspace - group workspace the user has been invited to
	 * @param email - email used by the inviting user to invite the other user
	 * @param inviteMessage - message to the user.
	 */
	public void sendEmailInvite(IrUser invitingUser, GroupWorkspace groupWorkspace, String email, String inviteMessage);

	/**
	 * Delete the invite 
	 * 
	 * @param entity
	 */
	public void delete(GroupWorkspaceEmailInvite entity);

	
	/**
	 * Get a count of the number of invite records.
	 * 
	 */
	public Long getEmailInviteCount();
	
	/**
	 * Sends an email notifying the specified user that  they have been invited
	 * to join a group workspace.  This also notifies the user they must join
	 * the system to access the group workspace.
	 * 
	 * @param invite
	 */
	public void sendEmailInvite(GroupWorkspaceEmailInvite invite);
	
	/**
	 * Add users to all invited groups for a given email.  This email must be verified as
	 * valid.
	 * 
	 * @param email - email for user to be verified
	 * @throws UnVerifiedEmailException - if the email has not yet been verified.
	 */
	public void addUserToGroupsForEmail(String email) throws UnVerifiedEmailException;
	

	
}
