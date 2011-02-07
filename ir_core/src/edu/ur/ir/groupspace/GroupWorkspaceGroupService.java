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

import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;

/**
 * Service to manage group workspace groups.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceGroupService extends Serializable{
	
	
	/**
	 * Get the list of user groups for a group workspace.
	 * 
	 * 
	 * @param groupSpaceId - id of the group space
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param orderType - sort order(Asc/desc)
	 * 
	 * @return list of user groups.
	 */
	public List<GroupWorkspaceGroup> getGroupsByName(Long groupSpaceId, int rowStart, 
    		int numberOfResultsToShow, OrderType orderType);
	
	/**
	 * Get all the user groups for a given group space and a given user.
	 *
	 * @param groupSpaceId - groupSpace
	 * @param userId - id of the user to get the groups for
	 * @return - list of groups the user is in.
	 */
	public List<GroupWorkspaceGroup> getGroups(Long groupSpaceId, Long userId);
	
	/**
	 * Get a user for the specified group 
	 * 
	 * @param groupId - id of the group
	 * @param userId - id of the user
	 * 
	 * @return the user if found - otherwise null.
	 */
	public IrUser getUserForGroup(Long groupId, Long userId);

	/**
	 * Save the group workspace group.
	 * 
	 * @param group - group to save
	 */
	public void save(GroupWorkspaceGroup group);
	
	/**
	 * Delete the group workspace.
	 * 
	 * @param group
	 */
	public void delete(GroupWorkspaceGroup group);
	
	/**
	 * Get the group workspace group by id.
	 * 
	 * @param id - unique id of the group
	 * @param lock - upgrade the lock mode.
	 * 
	 * @return - the group if found.
	 */
	public GroupWorkspaceGroup get(Long id, boolean lock);
	
	
}
