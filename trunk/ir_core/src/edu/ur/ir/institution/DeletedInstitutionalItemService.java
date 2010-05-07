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

import java.io.Serializable;

import edu.ur.ir.user.IrUser;

/**
 * Service interface to deal with deleted institutional item
 * information.
 * 
 * @author Nathan Sarr
 *
 */
public interface DeletedInstitutionalItemService extends Serializable{
	
	/**
	 * Delete institutional item history
	 * 
	 * @param deletedInstitutionalItem
	 */
	public void deleteInstitutionalItemHistory(DeletedInstitutionalItem entity) ;

	/**
	 * Delete institutional item history
	 * 
	 * @param deletedInstitutionalItem
	 */
	public void deleteAllInstitutionalItemHistory();
	
	/**
	 * Get Delete info for institutional item 
	 * 
	 * @param institutionalItemId Id of institutional item
	 * @return Information about deleted institutional item
	 */
	public DeletedInstitutionalItem getDeleteInfoForInstitutionalItem(Long institutionalItemId);
	
	/**
	 * Get number of deleted institutional items for user
	 * 
	 * @param userId Id of user who deleted institutional item
	 * @return Number of institutional items deleted
	 */
	public Long getDeletedInstitutionalItemCountForUser(Long userId);
	
	/**
	 * Gets a deleted institutional item version by the original institutional item version.
	 * 
	 * @param institutionalItemVersionId - the original institutional item version id.
	 * 
	 * @return the deleted institutional item version record.
	 */
	public DeletedInstitutionalItemVersion getDeletedVersionByItemVersionId(Long institutionalItemVersionId);

	
	/**
	 * Get the deleted institutional item version by the original institutional item id and version number.
	 * 
	 * @param institutionalItemId - original institutional item id
	 * @param versionNumber - version number
	 * 
	 * @return - the deleted institutional item version record.
	 */
	public DeletedInstitutionalItemVersion getDeletedVersionByItemVersion(Long institutionalItemId, int versionNumber);

	/**
	 * Adds delete history for item
     *
	 * @param institutionalItem - institutional item being deleted
	 * @param deletingUser - user performing the deletion
	 */
	public void addDeleteHistory(InstitutionalItem institutionalItem, IrUser deletingUser);

}
