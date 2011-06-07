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
 * Service to deal with content type record type mappings.
 * 
 * @author Nathan Sarr
 *
 */
public interface ContentTypeRecordTypeMapperService extends Serializable{
	
	/**
	 * Get the contributor type relator code by contributor type id.
	 * 
	 * @param contentTypeId - id of the content type.
	 * @return the mapper if found otherwise null.
	 */
	public ContentTypeRecordTypeMapper getByContentTypeId(Long contentTypeId);
	
	/**
	 * Returns the list of content types record type mappers that have the specified record type.
	 * 
	 * @param record type - record type 
	 * @return the list of records found with the content type with the specified record type.
	 */
	public List<ContentTypeRecordTypeMapper> getByRecordType(String recordType);
	
	
	/**
	 * Get all of the content type record types
	 * @return all mappers found
	 */
	public List<ContentTypeRecordTypeMapper> getAll();

	
	/**
	 * Get the content type record type by id.
	 * 
	 * @param id - id of the mapper
	 * @param lock - upgrade the lock on the data
	 * @return - the  if found otherwise null.
	 */
	public ContentTypeRecordTypeMapper getById(Long id, boolean lock);

	
	/**
	 * Save the mapper
	 * 
	 * @param entity
	 */
	public void save(ContentTypeRecordTypeMapper entity);

	
	/**
	 * Delete the mapper
	 * 
	 * @param entity
	 */
	public void delete(ContentTypeRecordTypeMapper entity);


}
