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

import edu.ur.ir.item.metadata.marc.IdentifierTypeSubFieldMapper;
import edu.ur.ir.item.metadata.marc.IdentifierTypeSubFieldMapperDAO;
import edu.ur.ir.item.metadata.marc.IdentifierTypeSubFieldMapperService;

/**
 * Service to deal with identifier type sub field mappings.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultIdentifierTypeSubFieldMapperService implements IdentifierTypeSubFieldMapperService {
	
	/* eclipse generated id */
	private static final long serialVersionUID = 7990629282383838505L;
	
	private IdentifierTypeSubFieldMapperDAO identifierTypeSubFieldMapperDAO;

	/**
	 * Delete the identifier type sub field mapper.
	 * 
	 * @see edu.ur.ir.marc.IdentifierTypeSubFieldMapperService#delete(edu.ur.ir.marc.IdentifierTypeSubFieldMapper)
	 */
	public void delete(IdentifierTypeSubFieldMapper entity) {
		identifierTypeSubFieldMapperDAO.makeTransient(entity);
	}

	/**
	 * Get all of the identifier type sub field values.
	 * 
	 * @see edu.ur.ir.marc.IdentifierTypeSubFieldMapperService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<IdentifierTypeSubFieldMapper> getAll() {
		return identifierTypeSubFieldMapperDAO.getAll();
	}

	/**
	 * Get the identifier type sub field mapper by id.
	 * 
	 * @see edu.ur.ir.marc.IdentifierTypeSubFieldMapperService#getById(java.lang.Long, boolean)
	 */
	public IdentifierTypeSubFieldMapper getById(Long id, boolean lock) {
		return identifierTypeSubFieldMapperDAO.getById(id, lock);
	}

	/**
	 * Save the identifier type sub field mapper.
	 * 
	 * @see edu.ur.ir.marc.IdentifierTypeSubFieldMapperService#save(edu.ur.ir.marc.IdentifierTypeSubFieldMapper)
	 */
	public void save(IdentifierTypeSubFieldMapper entity) {
		identifierTypeSubFieldMapperDAO.makePersistent(entity);
	}
	
	/**
	 * Set the database access for the identifier type sub field mapper.
	 * 
	 * @param identifierTypeSubFieldMapperDAO
	 */
	public void setIdentifierTypeSubFieldMapperDAO(
			IdentifierTypeSubFieldMapperDAO identifierTypeSubFieldMapperDAO) {
		this.identifierTypeSubFieldMapperDAO = identifierTypeSubFieldMapperDAO;
	}

	/**
	 * Get the list of identifier type sub fields by identifier type id.
	 * 
	 * @see edu.ur.ir.marc.IdentifierTypeSubFieldMapperService#getByIdentifierTypeId(java.lang.Long)
	 */
	@Override
	public List<IdentifierTypeSubFieldMapper> getByIdentifierTypeId(Long id) {
		return identifierTypeSubFieldMapperDAO.getByIdentifierTypeId(id);
	}
	
	/**
	 * Get the list of all identifiers with the specified data field name and indicator settings.
	 * 
	 * @param code - name of the data field (100, 200, etc)
	 * @param indicator1 - first indicator value
	 * @param indicator2 - second indicator value
	 * @param subField - sub field value
	 * 
	 * @return list of identifier sub filed mappings.
	 */
	public List<IdentifierTypeSubFieldMapper> getByDataField(String code, 
			String indicator1, String indicator2, String subField)
	{
	    return identifierTypeSubFieldMapperDAO.getByDataField(code, indicator1, indicator2, 
	    		subField);
    }

}
