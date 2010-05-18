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

package edu.ur.ir.institution.service;

import java.util.Date;

import edu.ur.ir.institution.DeletedInstitutionalItem;
import edu.ur.ir.institution.DeletedInstitutionalItemDAO;
import edu.ur.ir.institution.DeletedInstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.user.IrUser;

/**
 * 
 * Service to deal with deleted institutional item information.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultDeletedInstitutionalItemService implements DeletedInstitutionalItemService{
	
	/** eclipse generated id  */
	private static final long serialVersionUID = 5306382222484241340L;

	/** Deleted Institutional item data access */
	private DeletedInstitutionalItemDAO deletedInstitutionalItemDAO;
	

	
	/**
	 * Adds delete history for item
	 */
	public void addDeleteHistory(InstitutionalItem institutionalItem, IrUser deletingUser) {
		
		DeletedInstitutionalItem i = new DeletedInstitutionalItem(institutionalItem);

		i.setDeletedBy(deletingUser);
		i.setDeletedDate(new Date());
		
		deletedInstitutionalItemDAO.makePersistent(i);
		
	}

	/**
	 * Delete institutional item history
	 * 
	 * @param deletedInstitutionalItem
	 */
	public void deleteInstitutionalItemHistory(DeletedInstitutionalItem entity) {
		deletedInstitutionalItemDAO.makeTransient(entity);
	}

	/**
	 * Get Delete info for institutional item 
	 * 
	 * @param institutionalItemId Id of institutional item
	 * @return Information about deleted institutional item
	 */
	public DeletedInstitutionalItem getDeleteInfoForInstitutionalItem(Long institutionalItemId) {
		return deletedInstitutionalItemDAO.getDeleteInfoForInstitutionalItem(institutionalItemId);
	}
	
	/**
	 * Delete institutional item history
	 * 
	 * @param deletedInstitutionalItem
	 */
	public void deleteAllInstitutionalItemHistory() {
		deletedInstitutionalItemDAO.deleteAll();
	}
	
	/**
	 * Get the deleted institutional item data access object.
	 * 
	 * @return
	 */
	public DeletedInstitutionalItemDAO getDeletedInstitutionalItemDAO() {
		return deletedInstitutionalItemDAO;
	}

	/**
	 * Set the deleted institutional item data acess object 
	 * 
	 * @param deletedInstitutionalItemDAO
	 */
	public void setDeletedInstitutionalItemDAO(
			DeletedInstitutionalItemDAO deletedInstitutionalItemDAO) {
		this.deletedInstitutionalItemDAO = deletedInstitutionalItemDAO;
	}
	
	/**
	 * Get number of deleted institutional items for user
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getDeletedInstitutionalItemCountForUser(Long)
	 */
	public Long getDeletedInstitutionalItemCountForUser(Long userId) {
		return deletedInstitutionalItemDAO.getDeletedInstitutionalItemCountForUser(userId);
	}


}
