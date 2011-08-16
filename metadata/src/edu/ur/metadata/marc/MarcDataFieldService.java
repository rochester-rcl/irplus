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


public interface MarcDataFieldService extends Serializable{
	
	/**
	 * Get a count of MARC data fields.
	 * 
	 * @return the count of MARC data fields
	 */
	public Long getCount();

	/**
	 * Get all MARC data fields.
	 * 
	 * @return - all MARC data fields.
	 */
	public List<MarcDataField> getAll();

	/**
	 * Get a MARC data field by id.
	 * 
	 * @param id - id of the MARC data field
	 * @param lock - upgrade the lock
	 * 
	 * @return the found MARC data field
	 */
	public MarcDataField getById(Long id, boolean lock);

	/**
	 * Save the MARC data field.
	 * 
	 * @param entity - save the MARC data field
	 */
	public void save(MarcDataField entity);

	/**
	 * Delete the MARC data field.
	 * 
	 * @param entity - the MARC data field
	 */
	public void delete(MarcDataField entity);

	/**
	 * Find a MARC data field by its unique name.
	 * 
	 * @param name - name of the data field
	 * @return - found MARC data field
	 */
	public MarcDataField findByCode(String code);

}
