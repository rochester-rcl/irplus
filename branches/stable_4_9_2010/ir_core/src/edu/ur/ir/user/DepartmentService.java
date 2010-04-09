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
 * Interface for managing with department information.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface DepartmentService {
	
	/**
	 * Get a count of the departments in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getDepartmentCount();
	
	/**
	 * Get all departments in name order.
	 */
	public List<Department> getAllDepartmentsNameOrder();

	/**
	 * Get all departments in name order.
	 */
	public List<Department> getAllDepartmentsOrderByName(int startRecord, int numRecords);

	/**
	 * Find the department by unique name.
	 * 
	 * @param name - name of the department.
	 * @return the department or null if no department was found.
	 */
	public Department getDepartment(String name);
	
	
	/**
	 * Get the list of departments. 
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * 
	 * @return List of departments for the specified information.
	 */
	public List<Department> getDepartmentsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

	/**
	 * Get all departments.
	 * 
	 * @return
	 */
	public List<Department> getAllDepartments();

	/**
	 * Get a department by id.
	 * 
	 * @param id - id of the department
	 * @param lock - lock mode
	 *  
	 * @return the Department if found.
	 */
	public Department getDepartment(Long id, boolean lock);

	/**
	 * Make the department information persistent.
	 * 
	 * @param department to add/save
	 */
	public void makeDepartmentPersistent(Department entity);

	/**
	 * Delete a department from the system
	 * 
	 * @param department to delete
	 */
	public void deleteDepartment(Department entity);
	
	/**
	 * Delete a set of departments from the system
	 * 
	 * @param departments to delete
	 */
	public void deleteDepartments(List<Department> departments);

}
