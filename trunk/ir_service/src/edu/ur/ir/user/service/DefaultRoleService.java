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

import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrRoleDAO;
import edu.ur.ir.user.RoleService;

public class DefaultRoleService implements RoleService{
	
	/** Role data access object */
	private IrRoleDAO irRoleDAO;
	
	/**
	 * Get the role by unique name.
	 * 
	 * @see edu.ur.ir.user.RoleService#getRole(java.lang.String)
	 */
	public IrRole getRole(String name) {
		return irRoleDAO.findByUniqueName(name);
	}

	/**
	 * Get all roles.
	 * 
	 * @see edu.ur.ir.user.RoleService#getAllRoles()
	 */
	@SuppressWarnings("unchecked")
	public List<IrRole> getAllRoles() {
		return irRoleDAO.getAll();
	}

	/**
	 * Get all roles ordered by name.
	 * 
	 * @see edu.ur.ir.user.RoleService#getAllRolesNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<IrRole> getAllRolesNameOrder() {
		return irRoleDAO.getAllNameOrder();
	}


	/**
	 * Get all roles within the specified range.
	 * 
	 * @see edu.ur.ir.user.RoleService#getAllRolesOrderByName(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<IrRole> getAllRolesOrderByName(int startRecord, int numRecords) {
		return irRoleDAO.getAllOrderByName(startRecord, numRecords);
	}

	/**
	 * Get the role with the specified id.
	 * 
	 * @see edu.ur.ir.user.RoleService#getRole(java.lang.Long, boolean)
	 */
	public IrRole getRole(Long id, boolean lock) {
		return irRoleDAO.getById(id, lock);
	}

	/**
	 * Get a count of roles in the system.
	 * 
	 * @see edu.ur.ir.user.RoleService#getRoleCount()
	 */
	public Long getRoleCount() {
		return irRoleDAO.getCount();
	}

	/**
	 * Save the role
	 * 
	 * @see edu.ur.ir.user.RoleService#makeRolePersistent(edu.ur.ir.user.IrRole)
	 */
	public void makeRolePersistent(IrRole entity) {
		irRoleDAO.makePersistent(entity);
	}

	/**
	 * Delete the role.
	 * 
	 * @see edu.ur.ir.user.RoleService#deleteRole(edu.ur.ir.user.IrRole)
	 */
	public void deleteRole(IrRole entity) {
		irRoleDAO.makeTransient(entity);
	}

	/**
	 * Get the role data access object.
	 * 
	 * @return
	 */
	public IrRoleDAO getIrRoleDAO() {
		return irRoleDAO;
	}

	/**
	 * Set the role data access object.
	 * 
	 * @param irRoleDAO
	 */
	public void setIrRoleDAO(IrRoleDAO irRoleDAO) {
		this.irRoleDAO = irRoleDAO;
	}

	/**
	 * Get the roles based on the specified criteria, start and end position.
	 * 
	 * @see edu.ur.ir.user.RoleService#getRolesOrderByName(int, int, String)
	 */
	public List<IrRole> getRolesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return irRoleDAO.getRoles(rowStart, numberOfResultsToShow, sortType);
	}

	/**
	 * Get roles with the specified ids.
	 * 
	 * @see edu.ur.ir.user.RoleService#getRoles(java.util.List)
	 */
	public List<IrRole> getRoles(List<Long> roleIds) {
		return irRoleDAO.getRoles(roleIds);
	}

	/**
	 * Delete the roles.
	 * 
	 * @see edu.ur.ir.user.RoleService#deleteRoles(java.util.List)
	 */
	public void deleteRoles(List<IrRole> roles) {
		for(IrRole role : roles)
		{
			irRoleDAO.makeTransient(role);
		}
		
	}


}
