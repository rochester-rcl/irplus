/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.institution;

import edu.ur.dao.CrudDAO;

/**
 * Database access interface for institutional item versions
 * 
 * @author Nathan Sarr
 *
 */
public interface DeletedInstitutionalItemVersionDAO extends CrudDAO<DeletedInstitutionalItemVersion>{
	
	/**
	 * Get Delete info for institutional item version by it's original institutional item version id
	 * 
	 * @param institutionalItemVersionId Id of institutional item version
	 * @return Information about deleted institutional item version
	 */
	public DeletedInstitutionalItemVersion get(Long institutionalItemVersionId);
	
	/**
	 * Get Delete info for institutional item version by it's version number and the
	 * institutional item id.
	 * 
	 * @param institutionalItemId - id of the institutional item
	 * @param versionNumber - version number 
	 * 
	 * @return - the deleted institutional item version
	 */
	public DeletedInstitutionalItemVersion get(Long institutionalItemId, int versionNumber);

}
