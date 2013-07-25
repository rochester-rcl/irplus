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

package edu.ur.metadata.marc.service;

import java.util.List;

import edu.ur.metadata.marc.MarcSubField;
import edu.ur.metadata.marc.MarcSubFieldDAO;
import edu.ur.metadata.marc.MarcSubFieldService;

/**
 * Marc sub field service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultMarcSubFieldService implements MarcSubFieldService {
	
	// Eclipse generated id
	private static final long serialVersionUID = 2132703633588652848L;
	
	// marc data field data access
	private MarcSubFieldDAO marcSubFieldDAO;

	/**
	 * Get a count of MARC sub fields.
	 * 
	 * @return the count of MARC sub fields
	 */
	public Long getCount()
	{
		return marcSubFieldDAO.getCount();
	}

	/**
	 * Get all MARC sub fields.
	 * 
	 * @return - all MARC sub fields.
	 */
	@SuppressWarnings("unchecked")
	public List<MarcSubField> getAll()
	{
		return marcSubFieldDAO.getAll();
	}

	/**
	 * Get a MARC sub field by id.
	 * 
	 * @param id - id of the MARC sub field
	 * @param lock - upgrade the lock
	 * 
	 * @return the found MARC sub field
	 */
	public MarcSubField getById(Long id, boolean lock)
	{
		return marcSubFieldDAO.getById(id, lock);
	}

	/**
	 * Save the MARC sub field.
	 * 
	 * @param entity - save the MARC sub field
	 */
	public void save(MarcSubField entity)
	{
		marcSubFieldDAO.makePersistent(entity);
	}

	/**
	 * Delete the MARC sub field.
	 * 
	 * @param entity - the MARC sub field
	 */
	public void delete(MarcSubField entity)
	{
		marcSubFieldDAO.makeTransient(entity);
	}

	/**
	 * Find a MARC sub field by its unique name.
	 * 
	 * @param name - name of the sub field
	 * @return - found MARC sub field
	 */
	public MarcSubField findByName(String name)
	{
		return marcSubFieldDAO.findByUniqueName(name);
	}
	
	/**
	 * Set the marc sub field data access object.
	 * 
	 * @param marcSubFieldDAO
	 */
	public void setMarcSubFieldDAO(MarcSubFieldDAO marcSubFieldDAO) {
		this.marcSubFieldDAO = marcSubFieldDAO;
	}
}
