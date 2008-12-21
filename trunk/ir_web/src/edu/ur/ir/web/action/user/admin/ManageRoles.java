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


package edu.ur.ir.web.action.user.admin;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.web.table.Pager;

/**
 * Roles for the user.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageRoles extends Pager implements Preparable{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 8348693135414067344L;

	/** role service */
	private RoleService roleService;
	
	/**  Logger for managing users */
	private static final Logger log = Logger.getLogger(ManageRoles.class);
	
	/** Set of language types for viewing the users */
	private Collection<IrRole> roles;
	
	/** user to edit */
	private IrRole role = new IrRole();
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the user has been added*/
	private boolean added = false;
	
	/** Indicates the roles have been deleted */
	private boolean deleted = false;
	
	/** id of the role type  */
	private Long id;
	
	/** Set of role type ids */
	private long[] roleIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of roles */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public  ManageRoles() 
	{
		numberOfResultsToShow = 50;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new role type.
	 * 
	 * Create a new role type
	 */
	public String create()
	{
		log.debug("creating a role type = " + role.getName());
		added = false;
		IrRole myIrRole = 
			roleService.getRole(role.getName());
		if( myIrRole == null)
		{
			roleService.makeRolePersistent(role);
		    added = true;
		}
		else
		{
			message = getText("roleAlreadyExists", 
					new String[]{role.getName()});
		}
        return "added";
	}
	
	/**
	 * Method to update an existing role type.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updating role id = " + id);
		added = false;
		
		IrRole other = 
			roleService.getRole(role.getName());
		
		// if the role is found and the id's are not the same
		// then they are trying to rename it to the same name.
		if(other == null || other.getId().equals(role.getId()))
		{
			roleService.makeRolePersistent(role);
			added = true;
		}
		else
		{
			message = getText("roleAlreadyExists",
					new String[]{role.getName()});
		}
        return "added";
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete roles called");
		if( roleIds != null )
		{
			List<Long> myIds = new LinkedList<Long>();
		    for(int index = 0; index < roleIds.length; index++)
		    {
		    	myIds.add(roleIds[index]);
		    }
			
		    List<IrRole> roles = roleService.getRoles(myIds); 
 			roleService.deleteRoles(roles);
		    
		}
		deleted = true;
		return "deleted";
	}
 
	/**
	 * Get the roles table data.
	 * 
	 * @return
	 */
	public String viewRoles()
	{
	
		rowEnd = rowStart + numberOfResultsToShow;
	    
		roles = roleService.getRolesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = roleService.getRoleCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;

	}
	
	/**
	 * List of roles for display.
	 * 
	 * @return
	 */
	public Collection<IrRole> getRoles() {
		return roles;
	}
	/**
	 * Set the list of roles.
	 * 
	 * @param roles
	 */
	public void setRoles(Collection<IrRole> roles) {
		this.roles = roles;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isAdded() {
		return added;
	}
	public void setAdded(boolean added) {
		this.added = added;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(long[] roleIds) {
		this.roleIds = roleIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
	public void prepare() throws Exception {
		if( id != null)
		{
			role = roleService.getRole(id, false);
		}
	}

	public IrRole getRole() {
		return role;
	}

	public void setRole(IrRole role) {
		this.role = role;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}
	
}
