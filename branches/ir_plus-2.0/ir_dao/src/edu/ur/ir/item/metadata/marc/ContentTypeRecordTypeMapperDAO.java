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

import java.util.List;

import edu.ur.dao.CrudDAO;

/**
 * Data Access layer for the content type record type mapper.
 * 
 * @author Nathan Sarr
 *
 */
public interface ContentTypeRecordTypeMapperDAO extends CrudDAO<ContentTypeRecordTypeMapper>
{
	/**
	 * Get the mapping by content type id.
	 * 
	 * @param contentTypeId - id of the content type
	 * @return - the mapping otherwise null
	 */
	public ContentTypeRecordTypeMapper getByContentType(Long contentTypeId);
	
	/**
	 * Get the mapping by record type.
	 * 
	 * @param recordTypeId - id of the record type
	 * @return the mapping otherwise null.
	 */
	public ContentTypeRecordTypeMapper getByRecordType(Long recordTypeId);
	
	/**
	 * Returns the list of records that have the specified record type code.
	 * 
	 * @param recordTypeCode - record type code 
	 * @return the list of records found with the record type string value
	 */
	public List<ContentTypeRecordTypeMapper> getByRecordType(String recordType);
	
}