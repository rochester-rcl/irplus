/**  
   Copyright 2008 University of Rochester

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

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;


/**
 * Data access for deleted institutional items.
 * 
 * @author Sharmila Ranganathan.
 *
 */
public interface DeletedInstitutionalItemDAO extends CrudDAO<DeletedInstitutionalItem>, CountableDAO
{
	/**
	 * Deletes the entire history
	 */
	public void deleteAll();
	
	/**
	 * Get Delete info for institutional item 
	 * 
	 * @param institutionalItemId Id of institutional item
	 * @return Information about deleted institutional item
	 */
	public DeletedInstitutionalItem getDeleteInfoForInstitutionalItem(Long institutionalItemId) ;

	/**
	 * Get number of deleted institutional items for user
	 * 
	 * @param userId Id of user who deleted the item
	 * @return
	 */
	public Long getDeletedInstitutionalItemCountForUser(Long userId) ;
}
