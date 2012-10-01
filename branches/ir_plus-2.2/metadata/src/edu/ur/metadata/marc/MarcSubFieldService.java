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
 * Marc sub field service.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcSubFieldService extends Serializable{
	
	/**
	 * Get a count of MARC sub fields.
	 * 
	 * @return the count of MARC sub fields
	 */
	public Long getCount();

	/**
	 * Get all MARC sub fields.
	 * 
	 * @return - all MARC sub fields.
	 */
	public List<MarcSubField> getAll();

	/**
	 * Get a MARC sub field by id.
	 * 
	 * @param id - id of the MARC sub field
	 * @param lock - upgrade the lock
	 * 
	 * @return the found MARC sub field
	 */
	public MarcSubField getById(Long id, boolean lock);

	/**
	 * Save the MARC sub field.
	 * 
	 * @param entity - save the MARC sub field
	 */
	public void save(MarcSubField entity);

	/**
	 * Delete the MARC sub field.
	 * 
	 * @param entity - the MARC sub field
	 */
	public void delete(MarcSubField entity);

	/**
	 * Find a MARC sub field by its unique name.
	 * 
	 * @param name - name of the sub field
	 * @return - found MARC sub field
	 */
	public MarcSubField findByName(String name);

}
