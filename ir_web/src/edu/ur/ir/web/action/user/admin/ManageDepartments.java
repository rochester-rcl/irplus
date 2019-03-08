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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.user.Department;
import edu.ur.ir.user.DepartmentService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

/**
 * Action to deal with departments.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManageDepartments extends Pager implements Preparable, UserIdAware{
	
	/** generated version id. */
	private static final long serialVersionUID = -3229962214403823020L;
	
	/** department service */
	private DepartmentService departmentService;
	
	/**  Logger for managing departments*/
	private static final Logger log = LogManager.getLogger(ManageDepartments.class);
	
	/** Set of departments for viewing the departments */
	private Collection<Department> departments;
	
	/**  Department for loading  */
	private Department department;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the department has been added*/
	private boolean added = false;
	
	/** Indicates the departments have been deleted */
	private boolean deleted = false;
	
	/** id of the department  */
	private Long id;
	
	/** Set of department ids */
	private long[] departmentIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of departments */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** service for accessing user information */
	private UserService userService;


	/** id of the user */
	private Long userId;
	
	/** Default constructor */
	public  ManageDepartments() 
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new department.
	 * 
	 * Create a new department
	 */
	public String create()
	{
		log.debug("creating a department = " + department.getName());
		IrUser user = userService.getUser(userId, false);
		if(!user.hasRole(IrRole.RESEARCHER_ROLE) && !user.hasRole(IrRole.ADMIN_ROLE) )
		{
			return "accessDenied";
		}
		
		added = false;
		Department other = departmentService.getDepartment(department.getName());
		if( other == null)
		{
		    departmentService.makeDepartmentPersistent(department);
		    added = true;
		}
		else
		{
			message = getText("departmentAlreadyExists", 
					new String[]{department.getName()});
			addFieldError("departmentAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Method to update an existing department.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updating department id = " + department.getId());
		IrUser user = userService.getUser(userId, false);
		if(!user.hasRole(IrRole.ADMIN_ROLE) )
		{
			return "accessDenied";
		}
		added = false;

		Department other = departmentService.getDepartment(department.getName());
		
		if( other == null || other.getId().equals(department.getId()))
		{
			departmentService.makeDepartmentPersistent(department);
			added = true;
		}
		else
		{
			message = getText("departmentAlreadyExists", 
					new String[]{department.getName()});
			
			addFieldError("departmentAlreadyExists", message);
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
		log.debug("Delete departments called");
		IrUser user = userService.getUser(userId, false);
		if(!user.hasRole(IrRole.ADMIN_ROLE) )
		{
			return "accessDenied";
		}
		if( departmentIds != null )
		{
		    for(int index = 0; index < departmentIds.length; index++)
		    {
			    log.debug("Deleting department with id " + departmentIds[index]);
			    departmentService.deleteDepartment(departmentService.getDepartment(departmentIds[index], false));
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Get the department.
	 */
	public String get()
	{
		department = departmentService.getDepartment(id, false);
		return "get";
	}
	
	/**
	 * Get the departments table data.
	 * 
	 * @return
	 */
	public String viewDepartments()
	{

		rowEnd = rowStart + numberOfResultsToShow;
	    
		departments = departmentService.getDepartmentsOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = departmentService.getDepartmentCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	}

	/**
	 * Get the department service.
	 * 
	 * @return
	 */
	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	/**
	 * Set the department service.
	 * 
	 * @param departmentService
	 */
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
	/**
	 * List of departments for display.
	 * 
	 * @return
	 */
	public Collection<Department> getDepartments() {
		return departments;
	}
	/**
	 * Set the list of departments.
	 * 
	 * @param departments
	 */
	public void setDepartments(Collection<Department> departments) {
		this.departments = departments;
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

	public long[] getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(long[] departmentIds) {
		this.departmentIds = departmentIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void prepare() throws Exception {
		if( id != null)
		{
			department = departmentService.getDepartment(id, false);
		}
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

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
