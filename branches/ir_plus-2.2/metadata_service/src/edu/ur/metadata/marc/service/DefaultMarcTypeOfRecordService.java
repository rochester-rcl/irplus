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

import edu.ur.metadata.marc.MarcTypeOfRecord;
import edu.ur.metadata.marc.MarcTypeOfRecordDAO;
import edu.ur.metadata.marc.MarcTypeOfRecordService;

/**
 * Implements the default marc type of record service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultMarcTypeOfRecordService implements MarcTypeOfRecordService {
	
	//eclipse generated id
	private static final long serialVersionUID = -8264742391091936067L;
	
	//Data access class for marc type of record.
	private MarcTypeOfRecordDAO marcTypeOfRecordDAO;
	


	/**
	 * Get a count of MARC type of records.
	 * 
	 * @return the count of MARC type of records
	 */
	public Long getCount()
	{
		return marcTypeOfRecordDAO.getCount();
	}

	/**
	 * Get all MARC type of records.
	 * 
	 * @return - all MARC marc type of records.
	 */
	@SuppressWarnings("unchecked")
	public List<MarcTypeOfRecord> getAll()
	{
		return marcTypeOfRecordDAO.getAll();
	}

	/**
	 * Get a MARC type of record by id.
	 * 
	 * @param id - id of the MARC type of record
	 * @param lock - upgrade the lock
	 * 
	 * @return the found MARC type of record
	 */
	public MarcTypeOfRecord getById(Long id, boolean lock)
	{
		return marcTypeOfRecordDAO.getById(id, lock);
	}

	/**
	 * Save the MARC type of record.
	 * 
	 * @param entity - save the MARC type of record
	 */
	public void save(MarcTypeOfRecord entity)
	{
		marcTypeOfRecordDAO.makePersistent(entity);
	}

	/**
	 * Delete the MARC type of record.
	 * 
	 * @param entity - the MARC type of record
	 */
	public void delete(MarcTypeOfRecord entity)
	{
		marcTypeOfRecordDAO.makeTransient(entity);
	}

	/**
	 * Find a MARC type of record by its unique name.
	 * 
	 * @param name - name of the marc type of record
	 * @return - found MARC type of record
	 */
	public MarcTypeOfRecord findByName(String name)
	{
		return marcTypeOfRecordDAO.findByUniqueName(name);
	}
	
	/**
	 * Get by type of record.
	 * 
	 * @param recordType - type of recrod
	 * @return the found type of record or null if it doesn't exist.
	 */
	public MarcTypeOfRecord getByRecordType(char recordType)
	{
		return marcTypeOfRecordDAO.getByRecordType(recordType);
	}


	/**
	 * Set the data access object.
	 * 
	 * @param marcTypeOfRecordDAO
	 */
	public void setMarcTypeOfRecordDAO(MarcTypeOfRecordDAO marcTypeOfRecordDAO) {
		this.marcTypeOfRecordDAO = marcTypeOfRecordDAO;
	}

}
