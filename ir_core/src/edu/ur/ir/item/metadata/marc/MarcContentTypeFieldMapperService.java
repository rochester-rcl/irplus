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
 * Interface for marc content type field mapping.
 * 
 * @author Nathan Sarr
 *
 */
public interface  MarcContentTypeFieldMapperService extends Serializable {
	
	/**
	 * Get the field mapper by content type id.
	 * 
	 * @param contentTypeId - id of the content type.
	 * @return the mapper if found otherwise null.
	 */
	public MarcContentTypeFieldMapper getByContentTypeId(Long contentTypeId);
	
	
	/**
	 * Get all of the marc content type field maps
	 * @return all field mappers found
	 */
	public List<MarcContentTypeFieldMapper> getAll();

	
	/**
	 * Get the marc content type field mapper by id.
	 * 
	 * @param id - id of the marc content type field mapper
	 * @param lock - upgrade the lock on the data
	 * @return - the field mapper if found otherwise null.
	 */
	public MarcContentTypeFieldMapper getById(Long id, boolean lock);

	
	/**
	 * Save the mapper.
	 * 
	 * @param entity
	 */
	public void save(MarcContentTypeFieldMapper entity);

	
	/**
	 * Delete the filed mapper.
	 * 
	 * @param entity
	 */
	public void delete(MarcContentTypeFieldMapper entity);
	
	/**
	 * Get the mapper by record type
	 * 
	 * @param record type - leader 06 record type
	 * @return list of record types attached to marc content type fields
	 */
	public List<MarcContentTypeFieldMapper> getByRecordType(char recordType);

}
