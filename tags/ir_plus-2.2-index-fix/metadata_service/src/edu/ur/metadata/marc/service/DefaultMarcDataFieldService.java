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

import edu.ur.metadata.marc.MarcDataField;
import edu.ur.metadata.marc.MarcDataFieldDAO;
import edu.ur.metadata.marc.MarcDataFieldService;

/**
 * Default marc data field service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultMarcDataFieldService implements MarcDataFieldService {

	// eclipse generated id.
	private static final long serialVersionUID = 350901365677263962L;
	
	// marc data field data access
	private MarcDataFieldDAO marcDataFieldDAO;

	/**
	 * Get a count of MARC data fields.
	 * 
	 * @return the count of MARC data fields
	 */
	public Long getCount()
	{
		return marcDataFieldDAO.getCount();
	}

	/**
	 * Get all MARC data fields.
	 * 
	 * @return - all MARC data fields.
	 */
	@SuppressWarnings("unchecked")
	public List<MarcDataField> getAll()
	{
		return marcDataFieldDAO.getAll();
	}

	/**
	 * Get a MARC data field by id.
	 * 
	 * @param id - id of the MARC data field
	 * @param lock - upgrade the lock
	 * 
	 * @return the found MARC data field
	 */
	public MarcDataField getById(Long id, boolean lock)
	{
		return marcDataFieldDAO.getById(id, lock);
	}

	/**
	 * Save the MARC data field.
	 * 
	 * @param entity - save the MARC data field
	 */
	public void save(MarcDataField entity)
	{
		marcDataFieldDAO.makePersistent(entity);
	}

	/**
	 * Delete the MARC data field.
	 * 
	 * @param entity - the MARC data field
	 */
	public void delete(MarcDataField entity)
	{
		marcDataFieldDAO.makeTransient(entity);
	}

	/**
	 * Find a MARC data field by its unique code.
	 * 
	 * @param name - name of the data field
	 * @return - found MARC data field
	 */
	public MarcDataField findByCode(String code)
	{
		return marcDataFieldDAO.findByUniqueName(code);
	}

	/**
	 * Set the data access for the marc data field.
	 * 
	 * @param marcDataFieldDAO
	 */
	public void setMarcDataFieldDAO(MarcDataFieldDAO marcDataFieldDAO) {
		this.marcDataFieldDAO = marcDataFieldDAO;
	}

}
