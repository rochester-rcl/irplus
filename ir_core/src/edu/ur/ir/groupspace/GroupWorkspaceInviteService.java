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

import java.util.List;



/**
 * Service to deal with users who are invited to join a group
 * of a group workspace.  This provides an interface to deal with
 * both users who are already existing members of the system as 
 * well as those who have not yet become members of the system.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceInviteService 
{
	/**
	 * Find the Invite information for a specified token
	 * 
	 * @param token user token
	 * @return User token information
	 */
	public GroupWorkspaceEmailInvite findByToken(String token);
	
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
	 * Get the invited group workspace user invite by id.
	 * 
	 * @param id - id of the invite id
	 * @param lock - upgrade the lock.
	 * 
	 * @return the invite if found.
	 */
	public GroupWorkspaceUserInvite getUserInviteById(Long id, boolean lock);

	
	/**
	 * Make the invite record persistent.
	 * 
	 * @param entity
	 */
	public void save(GroupWorkspaceUserInvite entity);

	/**
	 * Delete the invite 
	 * 
	 * @param entity
	 */
	public void delete(GroupWorkspaceUserInvite entity);
	
	/**
	 * Make the invite record persistent.
	 * 
	 * @param entity
	 */
	public void save(GroupWorkspaceEmailInvite entity);

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
	public Long getUserInviteCount();
	
	/**
	 * Get a count of the number of invite records.
	 * 
	 */
	public Long getEmailInviteCount();
	
	/**
	 * Sends an email notifying the specified user that  they have been invited
	 * to join a group workspace.
	 * 
	 * @param invite
	 */
	public void sendEmailInvite(GroupWorkspaceUserInvite invite);
	
	/**
	 * Sends an email notifying the specified user that  they have been invited
	 * to join a group workspace.  This also notifies the user they must join
	 * the system to access the group workspace.
	 * 
	 * @param invite
	 */
	public void sendEmailInvite(GroupWorkspaceEmailInvite invite);
	
	
}
