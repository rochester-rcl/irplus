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


package edu.ur.metadata.marc;

import java.io.Serializable;
import java.util.List;

/**
 * Marc type of record service layer.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcTypeOfRecordService extends Serializable{
	
	/**
	 * Get a count of MARC type of records.
	 * 
	 * @return the count of MARC type of records
	 */
	public Long getCount();

	/**
	 * Get all MARC type of records.
	 * 
	 * @return - all MARC marc type of records.
	 */
	public List<MarcTypeOfRecord> getAll();

	/**
	 * Get a MARC type of record by id.
	 * 
	 * @param id - id of the MARC type of record
	 * @param lock - upgrade the lock
	 * 
	 * @return the found MARC type of record
	 */
	public MarcTypeOfRecord getById(Long id, boolean lock);

	/**
	 * Save the MARC type of record.
	 * 
	 * @param entity - save the MARC type of record
	 */
	public void save(MarcTypeOfRecord entity);

	/**
	 * Delete the MARC type of record.
	 * 
	 * @param entity - the MARC type of record
	 */
	public void delete(MarcTypeOfRecord entity);

	/**
	 * Find a MARC type of record by its unique name.
	 * 
	 * @param name - name of the marc type of record
	 * @return - found MARC type of record
	 */
	public MarcTypeOfRecord findByName(String name);
	
	/**
	 * Get by type of record.
	 * 
	 * @param recordType - type of recrod
	 * @return the found type of record or null if it doesn't exist.
	 */
	public MarcTypeOfRecord getByRecordType(char recordType);

}
