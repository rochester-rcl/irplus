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


package edu.ur.ir.item.service;

import java.util.List;

import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.ExtentTypeDAO;
import edu.ur.ir.item.ExtentTypeService;

/**
 * Default service for dealing with extent types.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultExtentTypeService implements ExtentTypeService{
	
	/**  extent type data access. */
	private ExtentTypeDAO extentTypeDAO;


	/**
	 * Delete a extent type with the specified id.
	 * 
	 * @see edu.ur.ir.item.ExtentTypeService#deleteExtentType(java.lang.Long)
	 */
	public boolean deleteExtentType(Long id) {
		ExtentType extentType = this.getExtentType(id, false);
		if( extentType != null)
		{
			extentTypeDAO.makeTransient(extentType);
		}
		return true;
	}

	/**
	 * Delete a extent type with the specified name.
	 * 
	 * @see edu.ur.ir.item.ExtentTypeService#deleteExtentType(java.lang.String)
	 */
	public boolean deleteExtentType(String name) {
		ExtentType extentType = this.getExtentType(name);
		if( extentType != null)
		{
			extentTypeDAO.makeTransient(extentType);
		}
		return true;
	}

	/**
	 * Get the extent type with the name.
	 * 
	 * @see edu.ur.ir.item.ExtentTypeService#getExtentType(java.lang.String)
	 */
	public ExtentType getExtentType(String name) {
		return extentTypeDAO.findByUniqueName(name);
	}

	/**
	 * Get the extent type by id.
	 * 
	 * @see edu.ur.ir.item.ExtentTypeService#getExtentType(java.lang.Long, boolean)
	 */
	public ExtentType getExtentType(Long id, boolean lock) {
		return extentTypeDAO.getById(id, lock);
	}

	/**
	 * Get extent types order by name
	 * 
	 * @see edu.ur.ir.item.ExtentTypeService#getExtentTypesOrderByName(int, int, String)
	 */
	public List<ExtentType> getExtentTypesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return extentTypeDAO.getExtentTypesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	}
	
	/**
	 * Get the extent types based 
	 * 
	 * @see edu.ur.ir.item.ExtentTypeService#getExtentTypesCount()
	 */
	public Long  getExtentTypesCount() {
		return extentTypeDAO.getCount();
	}

	/**
	 * Extent type data access.
	 * 
	 * @return
	 */
	public ExtentTypeDAO getExtentTypeDAO() {
		return extentTypeDAO;
	}

	/**
	 * Set the extent type data access.
	 * 
	 * @param extentTypeDAO
	 */
	public void setExtentTypeDAO(ExtentTypeDAO extentTypeDAO) {
		this.extentTypeDAO = extentTypeDAO;
	}

	/**
	 * Save the extent type.
	 * 
	 * @see edu.ur.ir.item.ExtentTypeService#saveExtentType(edu.ur.ir.item.ExtentType)
	 */
	public void saveExtentType(ExtentType extentType) {
		extentTypeDAO.makePersistent(extentType);
	}

    /**
     * Get all extent types
     * 
     * @see edu.ur.ir.item.ExtentTypeService#getAllExtentTypes()
     */
	@SuppressWarnings("unchecked")
	public List<ExtentType> getAllExtentTypes() {
		return extentTypeDAO.getAll();
	}
}
