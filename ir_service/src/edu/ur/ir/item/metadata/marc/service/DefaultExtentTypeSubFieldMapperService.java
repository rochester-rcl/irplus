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

package edu.ur.ir.item.metadata.marc.service;

import java.util.List;

import edu.ur.ir.item.metadata.marc.ExtentTypeSubFieldMapperService;
import edu.ur.ir.item.metadata.marc.ExtentTypeSubFieldMapper;
import edu.ur.ir.item.metadata.marc.ExtentTypeSubFieldMapperDAO;

/**
 * Service implementation for the extent type sub field mapper service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultExtentTypeSubFieldMapperService implements ExtentTypeSubFieldMapperService{

	/* eclipse generated id */
	private static final long serialVersionUID = 2873580730193057836L;
	
	// Extent type sub field mapper
	private ExtentTypeSubFieldMapperDAO extentTypeSubFieldMapperDAO;

	/**
	 * Delete the extent type sub field mapper.
	 * 
	 * @see edu.ur.ir.marc.ExtentTypeSubFieldMapperService#delete(edu.ur.ir.marc.ExtentTypeSubFieldMapper)
	 */
	public void delete(ExtentTypeSubFieldMapper entity) {
		extentTypeSubFieldMapperDAO.makeTransient(entity);
	}

	/**
	 * Get all of the extent type sub field values.
	 * 
	 * @see edu.ur.ir.marc.ExtentTypeSubFieldMapperService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<ExtentTypeSubFieldMapper> getAll() {
		return extentTypeSubFieldMapperDAO.getAll();
	}

	/**
	 * Get the extent type sub field mapper by id.
	 * 
	 * @see edu.ur.ir.marc.ExtentTypeSubFieldMapperService#getById(java.lang.Long, boolean)
	 */
	public ExtentTypeSubFieldMapper getById(Long id, boolean lock) {
		return extentTypeSubFieldMapperDAO.getById(id, lock);
	}

	/**
	 * Save the extent type sub field mapper.
	 * 
	 * @see edu.ur.ir.marc.ExtentTypeSubFieldMapperService#save(edu.ur.ir.marc.ExtentTypeSubFieldMapper)
	 */
	public void save(ExtentTypeSubFieldMapper entity) {
		extentTypeSubFieldMapperDAO.makePersistent(entity);
	}
	
	/**
	 * Set the database access for the extent type sub field mapper.
	 * 
	 * @param extentTypeSubFieldMapperDAO
	 */
	public void setExtentTypeSubFieldMapperDAO(
			ExtentTypeSubFieldMapperDAO extentTypeSubFieldMapperDAO) {
		this.extentTypeSubFieldMapperDAO = extentTypeSubFieldMapperDAO;
	}

	public List<ExtentTypeSubFieldMapper> getByExtentTypeId(Long id) {
		return extentTypeSubFieldMapperDAO.getByExtentTypeId(id);
	}
	
	/**
	 * Get the list of all extent types with the specified data field name and indicator settings.
	 * 
	 * @param code - name of the data field (100, 200, etc)
	 * @param indicator1 - first indicator value
	 * @param indicator2 - second indicator value
	 * @param subField - subfield value
	 * 
	 * @return list of extent type sub filed mappings.
	 */
	public List<ExtentTypeSubFieldMapper> getByDataField(String code, 
			String indicator1, 
			String indicator2, 
			String subField)
	{
		return extentTypeSubFieldMapperDAO.getByDataField(code, indicator1, indicator2, subField);
	}
	
}
