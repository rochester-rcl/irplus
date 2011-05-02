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


package edu.ur.ir.marc;

import java.io.Serializable;
import java.util.List;

/**
 * Data field mapper service.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcDataFieldMapperService extends Serializable{
	
	/**
	 * Get the mapper by data field id.
	 * 
	 * @param dataFieldId - id of the data field.
	 * @param indicator1 - value of the first indicator
	 * @param indicator2 - value of the second indicator
	 * 
	 * @return the mapper if found otherwise null.
	 */
	public MarcDataFieldMapper getByDataFieldIndicatorsId(Long dataFieldId, String indicator1, String indicator2);
	
	
	/**
	 * Get all of the data field mappers.
	 * 
	 * @return all mappers found
	 */
	public List<MarcDataFieldMapper> getAll();

	
	/**
	 * Get the the data field mapper by id.
	 * 
	 * @param id - id of the marc content type field mapper
	 * @param lock - upgrade the lock on the data
	 * @return - the field mapper if found otherwise null.
	 */
	public MarcDataFieldMapper getById(Long id, boolean lock);

	
	/**
	 * Save the mapper.
	 * 
	 * @param entity
	 */
	public void save(MarcDataFieldMapper entity);

	
	/**
	 * Delete the filed mapper.
	 * 
	 * @param entity
	 */
	public void delete(MarcDataFieldMapper entity);


}
