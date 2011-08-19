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

import edu.ur.dao.CrudDAO;
import edu.ur.dao.ListAllDAO;

/**
 * Interface for dealing with marc data field data.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcDataFieldMapperDAO extends CrudDAO<MarcDataFieldMapper>, ListAllDAO
{
	/**
	 * Get the mapper by marc data field id.
	 * 
	 * @param marcDataFieldId - id of the marc data field
	 * @param indicator1 - indicator1 value
	 * @param indicator2 - indicator2 value
	 * 
	 * @return the MarcDataFieldMapper
	 */
	public MarcDataFieldMapper getByMarcDataFieldIndicatorsId(Long marcDataFieldId, String indicator1, String indicator2);
}
