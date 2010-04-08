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


package edu.ur.ir.user.service;

import java.util.List;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.IrUserGroupDAO;
import edu.ur.ir.user.UserGroupService;

/**
 * Default implementation for the user group service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserGroupService implements UserGroupService {
	
	/** User group data access.  */
	private IrUserGroupDAO irUserGroupDAO;

	
	/**
	 * Find a user group with the specified unique name.
	 * 
	 * @see edu.ur.ir.user.UserGroupService#get(java.lang.String)
	 */
	public IrUserGroup get(String name) {
		return irUserGroupDAO.findByUniqueName(name);
	}

	/**
	 * Get all user groups.
	 * 
	 * @see edu.ur.ir.user.UserGroupService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<IrUserGroup> getAll() {
		return irUserGroupDAO.getAll();
	}

	/**
	 * Get all user groups name order.
	 * 
	 * @see edu.ur.ir.user.UserGroupService#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<IrUserGroup> getAllNameOrder() {
		return irUserGroupDAO.getAllNameOrder();
	}

	/**
	 * Get all user groups name order within the specified range.
	 * 
	 * @see edu.ur.ir.user.UserGroupService#getAllOrderByName(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<IrUserGroup> getAllOrderByName(int startRecord, int numRecords) {
		return irUserGroupDAO.getAllOrderByName(startRecord, numRecords);
	}

	/**
	 * Get a user group with the specified id.
	 * 
	 * @see edu.ur.ir.user.UserGroupService#getAuthority(java.lang.Long, boolean)
	 */
	public IrUserGroup get(Long id, boolean lock) {
		return irUserGroupDAO.getById(id, lock);
	}

	/**
	 * Get a count of the user groups.
	 * 
	 * @see edu.ur.ir.user.UserGroupService#getUserGroupsCount()
	 */
	public Long getUserGroupsCount() {
		return irUserGroupDAO.getCount();
	}

	
	/**
	 * Save the user group.
	 * 
	 * @see edu.ur.ir.user.UserGroupService#save(edu.ur.ir.user.IrUserGroup)
	 */
	public void save(IrUserGroup entity) {
		irUserGroupDAO.makePersistent(entity);
	}

	/**
	 * Delete the user group.
	 * 
	 * @see edu.ur.ir.user.UserGroupService#delete(edu.ur.ir.user.IrUserGroup)
	 */
	public void delete(IrUserGroup entity) {
		irUserGroupDAO.makeTransient(entity);
	}

	
	/**
	 * Get the user group data access object.
	 * 
	 * @return
	 */
	public IrUserGroupDAO getIrUserGroupDAO() {
		return irUserGroupDAO;
	}

	/**
	 * Set the user group data access object.
	 *  
	 * @param irUserGroupDAO
	 */
	public void setIrUserGroupDAO(IrUserGroupDAO irUserGroupDAO) {
		this.irUserGroupDAO = irUserGroupDAO;
	}

	/**
	 * Get the list of user groups ordered by name.
	 * 
	 * @see edu.ur.ir.user.UserGroupService#getUserGroupsOrderByName(int, int, java.lang.String)
	 */
	public List<IrUserGroup> getUserGroupsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return irUserGroupDAO.getUserGroups(rowStart, numberOfResultsToShow, sortType);
	}

	
	/**
	 * Get all groups for user.
	 * 
	 * @see edu.ur.ir.user.UserGroupService#getUserGroupsForUser(java.lang.Long)
	 */
	public List<IrUserGroup> getUserGroupsForUser(Long userId) {
		return irUserGroupDAO.getUserGroupsForUser(userId);
	}

	
	/**
	 * Get the user for a particular user group
	 * 
	 * @param userGroupId - id of the user group
	 * @param userId - id of the user
	 * 
	 * @return the found user or null if the user is not found.
	 */
	public IrUser getUser(Long userGroupId, Long userId) {
		return irUserGroupDAO.getUserForGroup(userGroupId, userId);
	}

}
