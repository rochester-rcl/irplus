/**  
   Copyright 2008 University of Rochester

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

package edu.ur.ir.user;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for managing user groups.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserGroupService extends Serializable{
	
	/**
	 * Get user groups sorting according to the sort and filter information .  
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * @return List of user groups.
	 */
	public List<IrUserGroup> getUserGroupsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

   /**
	 * Get a count of the user groups in the system.
	 * 
	 * @return count of user groups in the system.
	 */
	public Long getUserGroupsCount();

	/**
	 * Get all user groups in name order.
	 * 
	 * @return list of user groups in name order.
	 */
	public List<IrUserGroup> getAllNameOrder();

	/**
	 * Get all user groups in name order.
	 * 
	 * @param startRecord - start position
	 * @param numRecords - number of records to retrieve.
	 * 
	 * @return - user groups ordered by name.
	 */
	public List<IrUserGroup> getAllOrderByName(int startRecord, int numRecords);

	/**
	 * Find a user group by unique name.
	 * 
	 * @param name
	 * @return
	 */
	public IrUserGroup get(String name);

	/**
	 * Get all user groups.
	 * 
	 * @return
	 */
	public List<IrUserGroup> getAll();

	/**
	 * Get a user group by id.
	 * 
	 * @param id
	 * @param lock
	 * @return
	 */
	public IrUserGroup get(Long id, boolean lock);

	/**
	 * Save the user group.
	 * 
	 * @param entity
	 */
	public void save(IrUserGroup entity);

	/**
	 * Delete the user group
	 * 	 
	 * @param entity
	 */
	public void delete(IrUserGroup entity);
	
	/**
	 * Get all user groups for a given user.
	 * 
	 * @param userId - id of the user to get the groups for
	 * @return - list of groups the user is in.
	 * 
	 */
	public List<IrUserGroup> getUserGroupsForUser(Long userId);
	
	/**
	 * Get the user for a particular user group
	 * 
	 * @param userGroupId - id of the user group
	 * @param userId - id of the user
	 * 
	 * @return the found user or null if the user is not found.
	 */
	public IrUser getUser(Long userGroupId, Long userId);


}
