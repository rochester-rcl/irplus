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

package edu.ur.ir.item.metadata.marc;

import java.io.Serializable;
import java.util.List;

/**
 * Service to deal with extent type information.
 * 
 * @author Nathan Sarr
 *
 */
public interface ExtentTypeSubFieldMapperService extends Serializable{
	
	/**
	 * Get all extent type sub field mappers.
	 * 
	 * @return all extent type sub filed mappers
	 */
	public List<ExtentTypeSubFieldMapper> getAll();

	/**
	 * Get extent type sub field mapper by id.
	 * 
	 * @param id - id of the sub field mapper
	 * @param lock - upgrade the lock mode
	 * 
	 * @return the found extent type otherwise null.
	 */
	public ExtentTypeSubFieldMapper getById(Long id, boolean lock);

	/**
	 * Save the extent type sub field mapper.
	 * 
	 * @param entity
	 */
	public void save(ExtentTypeSubFieldMapper entity);

	/**
	 * Delete the extent type sub field mapper from persistence.
	 * 
	 * @param entity
	 */
	public void delete(ExtentTypeSubFieldMapper entity);
	
	/**
	 * Get by the extent type id.
	 * 
	 * @param id - extent type id.
	 * @return the mapper if found otherwise null
	 */
	public List<ExtentTypeSubFieldMapper> getByExtentTypeId(Long id);
	
	/**
	 * Get the list of all extent types with the specified data field name and indicator settings.
	 * 
	 * @param code - name of the data field (100, 200, etc)
	 * @param indicator1 - first indicator value
	 * @param indicator2 - second indicator value
	 * @param subField - subfield value
	 * 
	 * @return list of extent type sub filed mappings.
	 */
	public List<ExtentTypeSubFieldMapper> getByDataField(String code, 
			String indicator1, 
			String indicator2, 
			String subField);

}
