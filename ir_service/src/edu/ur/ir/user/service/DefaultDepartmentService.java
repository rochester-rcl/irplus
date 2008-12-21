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

import edu.ur.ir.user.Department;
import edu.ur.ir.user.DepartmentDAO;
import edu.ur.ir.user.DepartmentService;

/**
 * Service for department
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultDepartmentService implements DepartmentService{
	
	/** Department data access object */
	private DepartmentDAO departmentDAO;
	
	/**
	 * Get the department by unique name.
	 * 
	 * @see edu.ur.ir.user.DepartmentService#getDepartment(java.lang.String)
	 */
	public Department getDepartment(String name) {
		return departmentDAO.findByUniqueName(name);
	}

	/**
	 * Get all departments.
	 * 
	 * @see edu.ur.ir.user.DepartmentService#getAllDepartments()
	 */
	@SuppressWarnings("unchecked")
	public List<Department> getAllDepartments() {
		return departmentDAO.getAll();
	}

	/**
	 * Get all departments ordered by name.
	 * 
	 * @see edu.ur.ir.user.DepartmentService#getAllDepartmentsNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<Department> getAllDepartmentsNameOrder() {
		return departmentDAO.getAllNameOrder();
	}


	/**
	 * Get all departments within the specified range.
	 * 
	 * @see edu.ur.ir.user.DepartmentService#getAllDepartmentsOrderByName(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<Department> getAllDepartmentsOrderByName(int startRecord, int numRecords) {
		return departmentDAO.getAllOrderByName(startRecord, numRecords);
	}

	/**
	 * Get the department with the specified id.
	 * 
	 * @see edu.ur.ir.user.DepartmentService#getDepartment(java.lang.Long, boolean)
	 */
	public Department getDepartment(Long id, boolean lock) {
		return departmentDAO.getById(id, lock);
	}

	/**
	 * Get a count of departments in the system.
	 * 
	 * @see edu.ur.ir.user.DepartmentService#getDepartmentCount()
	 */
	public Long getDepartmentCount() {
		return departmentDAO.getCount();
	}

	/**
	 * Save the department
	 * 
	 * @see edu.ur.ir.user.DepartmentService#makeDepartmentPersistent(edu.ur.ir.user.Department)
	 */
	public void makeDepartmentPersistent(Department entity) {
		departmentDAO.makePersistent(entity);
	}

	/**
	 * Delete the department.
	 * 
	 * @see edu.ur.ir.user.DepartmentService#deleteDepartment(edu.ur.ir.user.Department)
	 */
	public void deleteDepartment(Department entity) {
		departmentDAO.makeTransient(entity);
	}

	/**
	 * Get the department data access object.
	 * 
	 * @return
	 */
	public DepartmentDAO getDepartmentDAO() {
		return departmentDAO;
	}

	/**
	 * Set the department data access object.
	 * 
	 * @param departmentDAO
	 */
	public void setDepartmentDAO(DepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
	}

	/**
	 * Get the departments based on the specified criteria, start and end position.
	 * 
	 * @see edu.ur.ir.user.DepartmentService#getDepartmentsOrderByName(int, int, String)
	 */
	public List<Department> getDepartmentsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return departmentDAO.getDepartments(rowStart, numberOfResultsToShow, sortType);
	}

	/**
	 * Delete the departments.
	 * 
	 * @see edu.ur.ir.user.DepartmentService#deleteDepartments(java.util.List)
	 */
	public void deleteDepartments(List<Department> departments) {
		for(Department department : departments)
		{
			departmentDAO.makeTransient(department);
		}
		
	}


}
