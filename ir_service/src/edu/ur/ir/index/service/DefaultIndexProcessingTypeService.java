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

package edu.ur.ir.index.service;

import java.util.List;

import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.index.IndexProcessingTypeDAO;

/**
 * Default Service for dealing with index processing types.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultIndexProcessingTypeService implements IndexProcessingTypeService{

	/** Data access for index processing */
	private IndexProcessingTypeDAO indexProcessingTypeDAO;
	
	/**
	 * Delete the index processing type
	 * 
	 * @see edu.ur.ir.index.IndexProcessingTypeService#delete(edu.ur.ir.index.IndexProcessingType)
	 */
	public void delete(IndexProcessingType entity) {
		indexProcessingTypeDAO.makeTransient(entity);
	}

	
	/**
	 * Get the index processing type.
	 * 
	 * @see edu.ur.ir.index.IndexProcessingTypeService#get(java.lang.String)
	 */
	public IndexProcessingType get(String name) {
		return indexProcessingTypeDAO.findByUniqueName(name);
	}

	
	/**
	 * Get the index processing type by id.
	 * 
	 * @see edu.ur.ir.index.IndexProcessingTypeService#get(java.lang.Long, boolean)
	 */
	public IndexProcessingType get(Long id, boolean lock) {
		return indexProcessingTypeDAO.getById(id, lock);
	}

	
	/**
	 * 
	 * @see edu.ur.ir.index.IndexProcessingTypeService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<IndexProcessingType> getAll() {
		return (List<IndexProcessingType>)indexProcessingTypeDAO.getAll();
	}

	
	/**
	 * Get the count of index processing types.
	 * 
	 * @see edu.ur.ir.index.IndexProcessingTypeService#getCount()
	 */
	public Long getCount() {
		return indexProcessingTypeDAO.getCount();
	}

	/**
	 * Save the index processing type.
	 * 
	 * @see edu.ur.ir.index.IndexProcessingTypeService#save(edu.ur.ir.index.IndexProcessingType)
	 */
	public void save(IndexProcessingType entity) {
		indexProcessingTypeDAO.makeTransient(entity);
	}

	public IndexProcessingTypeDAO getIndexProcessingTypeDAO() {
		return indexProcessingTypeDAO;
	}

	public void setIndexProcessingTypeDAO(
			IndexProcessingTypeDAO indexProcessingTypeDAO) {
		this.indexProcessingTypeDAO = indexProcessingTypeDAO;
	}

}
