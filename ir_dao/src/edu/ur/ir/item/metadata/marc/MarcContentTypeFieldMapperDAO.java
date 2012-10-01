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
 * Data access object for marc to content types field mapping
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcContentTypeFieldMapperDAO extends CrudDAO<MarcContentTypeFieldMapper>
{
	/**
	 * Get the mapper by content type.
	 * 
	 * @param contentTypeId
	 * @return
	 */
	public MarcContentTypeFieldMapper getByContentTypeId(Long contentTypeId);
	
	/**
	 * Get the list of all content types with the specified data field name and indicator settings.
	 * 
	 * @param code - code of the data field (100, 200, etc)
	 * @param indicator1 - first indicator value
	 * @param indicator2 - second indicator value
	 * @param subField - subfield value.
	 * 
	 * @return list of content type sub filed mappings.
	 */
	public List<MarcContentTypeFieldMapper> getByDataField(String code, 
			String indicator1, 
			String indicator2,
			String subField);
	
	/**
	 * Get the mapper by record type
	 * 
	 * @param record type - leader 06 record type
	 * @return list of record types attached to marc content type fields
	 */
	public List<MarcContentTypeFieldMapper> getByRecordType(char recordType);
}
