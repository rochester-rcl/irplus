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

package edu.ur.ir.marc.service;

import java.util.List;

import edu.ur.ir.marc.MarcContentTypeFieldMapper;
import edu.ur.ir.marc.MarcContentTypeFieldMapperDAO;
import edu.ur.ir.marc.MarcContentTypeFieldMapperService;

/**
 * Default service implementation for the marc content type field mapper.
 *  
 * @author Nathan Sarr
 *
 */
public class DefaultMarcContentTypeFieldMapperService implements MarcContentTypeFieldMapperService{
	
	private MarcContentTypeFieldMapperDAO marcContentTypeFieldMapperDAO;
	



	/**
	 * Get the field mapper by content type id.
	 * 
	 * @param contentTypeId - id of the content type.
	 * @return the mapper if found otherwise null.
	 */
	public MarcContentTypeFieldMapper getByContentTypeId(Long contentTypeId)
	{
		return marcContentTypeFieldMapperDAO.getByContentTypeId(contentTypeId);
	}
	
	
	/**
	 * Get all of the marc content type field maps
	 * @return all field mappers found
	 */
	@SuppressWarnings("unchecked")
	public List<MarcContentTypeFieldMapper> getAll()
	{
		return marcContentTypeFieldMapperDAO.getAll();
	}

	
	/**
	 * Get the marc content type field mapper by id.
	 * 
	 * @param id - id of the marc content type field mapper
	 * @param lock - upgrade the lock on the data
	 * @return - the field mapper if found otherwise null.
	 */
	public MarcContentTypeFieldMapper getById(Long id, boolean lock)
	{
		return marcContentTypeFieldMapperDAO.getById(id, lock);
	}

	
	/**
	 * Save the mapper.
	 * 
	 * @param entity
	 */
	public void save(MarcContentTypeFieldMapper entity)
	{
		marcContentTypeFieldMapperDAO.makePersistent(entity);
	}

	
	/**
	 * Delete the filed mapper.
	 * 
	 * @param entity
	 */
	public void delete(MarcContentTypeFieldMapper entity)
	{
		marcContentTypeFieldMapperDAO.makeTransient(entity);
	}
	
	/**
	 * Set the data access object.
	 * 
	 * @param marcContentTypeFieldMapperDAO
	 */
	public void setMarcContentTypeFieldMapperDAO(
			MarcContentTypeFieldMapperDAO marcContentTypeFieldMapperDAO) {
		this.marcContentTypeFieldMapperDAO = marcContentTypeFieldMapperDAO;
	}
	 

}
