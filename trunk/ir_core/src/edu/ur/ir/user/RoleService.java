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

import java.util.List;

/**
 * Interface for managing with role information.
 * 
 * @author Nathan Sarr
 *
 */
public interface RoleService {
	/**
	 * Get a count of the roles in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getRoleCount();
	
	/**
	 * Get all roles in name order.
	 */
	public List<IrRole> getAllRolesNameOrder();

	/**
	 * Get all roles in name order.
	 */
	public List<IrRole> getAllRolesOrderByName(int startRecord, int numRecords);

	/**
	 * Find the role by unique name.
	 * 
	 * @param name - name of the role.
	 * @return the role or null if no role was found.
	 */
	public IrRole getRole(String name);
	
	
	/**
	 * Get the list of roles. 
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * 
	 * @return List of roles
	 */
	public List<IrRole> getRolesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

   
	/**
	 * Get a set of roles with the given ids.
	 * 
	 * @param userIds - set of user ids.
	 * 
	 * @return the list of roles.
	 */
	public List<IrRole> getRoles(List<Long> roleIds);

	/**
	 * Get all roles.
	 * 
	 * @return
	 */
	public List<IrRole> getAllRoles();

	/**
	 * Get a role by id.
	 * 
	 * @param id - id of the role
	 * @param lock - lock mode
	 *  
	 * @return the IrRole if found.
	 */
	public IrRole getRole(Long id, boolean lock);

	/**
	 * Make the role information persistent.
	 * 
	 * @param role to add/save
	 */
	public void makeRolePersistent(IrRole entity);

	/**
	 * Delete a role from the system
	 * 
	 * @param role to delete
	 */
	public void deleteRole(IrRole entity);
	
	/**
	 * Delete a set of roles from the system
	 * 
	 * @param roles to delete
	 */
	public void deleteRoles(List<IrRole> roles);

}
