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
 * of a group workspace.
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
	public GroupWorkspaceInvite findByToken(String token);
	
	/**
	 * Find the Invite information for a specified email
	 * 
	 * @param email email address shared with
	 * @return List of invite information
	 */
	public List<GroupWorkspaceInvite> getByEmail(String email);
	
	
	/**
	 * Get the invited group workspace group user by id.
	 * 
	 * @param id - id of the invite id
	 * @param lock - upgrade the lock.
	 * 
	 * @return the invite if found.
	 */
	public GroupWorkspaceInvite getById(Long id, boolean lock);

	
	/**
	 * Make the invite record persistent.
	 * 
	 * @param entity
	 */
	public void save(GroupWorkspaceInvite entity);

	/**
	 * Delete the invite 
	 * 
	 * @param entity
	 */
	public void delete(GroupWorkspaceInvite entity);

	/**
	 * Get a count of the number of invite records.
	 * 
	 */
	public Long getCount();
	
	/**
	 * Sends an email notifying the specified user that  they have been invited
	 * to join a group workspace.
	 * 
	 * @param invite
	 */
	public void sendEmailInvite(GroupWorkspaceInvite invite);
	
	
}
