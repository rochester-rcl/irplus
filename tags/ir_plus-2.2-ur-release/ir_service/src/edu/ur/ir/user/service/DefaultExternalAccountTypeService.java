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



package edu.ur.ir.user.service;

import java.util.List;

import edu.ur.ir.user.ExternalAccountType;
import edu.ur.ir.user.ExternalAccountTypeDAO;
import edu.ur.ir.user.ExternalAccountTypeService;
import edu.ur.order.OrderType;

/**
 * Default service to deal with external account types.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultExternalAccountTypeService implements ExternalAccountTypeService{

	/** eclipse generated id */
	private static final long serialVersionUID = -9032119409419030798L;
	
	/** Data access for account type information*/
	private ExternalAccountTypeDAO externalAccountTypeDAO;

	
	/**
	 * Delete the external acocunt type from persistent storage.
	 * 
	 * @see edu.ur.ir.user.ExternalAccountTypeService#delete(edu.ur.ir.user.ExternalAccountType)
	 */
	public void delete(ExternalAccountType externalAccountType) {
		externalAccountTypeDAO.makeTransient(externalAccountType);
	}

	
	/**
	 * Get the extrenal account type by name.
	 * 
	 * @see edu.ur.ir.user.ExternalAccountTypeService#get(java.lang.String)
	 */
	public ExternalAccountType get(String name) {
		return externalAccountTypeDAO.findByUniqueName(name);
	}

	
	/**
	 * Get the external account type by id.
	 * 
	 * @see edu.ur.ir.user.ExternalAccountTypeService#get(java.lang.Long, boolean)
	 */
	public ExternalAccountType get(Long id, boolean lock) {
		return externalAccountTypeDAO.getById(id, lock);
	}

	
	/**
	 * Get all external account types.
	 * 
	 * @see edu.ur.ir.user.ExternalAccountTypeService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<ExternalAccountType> getAll() {
		return externalAccountTypeDAO.getAll();
	}

	/**
	 * Get the count of external count types.
	 * 
	 * @see edu.ur.ir.user.ExternalAccountTypeService#getCount()
	 */
	public Long getCount() {
		return externalAccountTypeDAO.getCount();
	}

	
	/**
	 * Get the external account types ordered by name.
	 * 
	 * @see edu.ur.ir.user.ExternalAccountTypeService#getExternalAccountTypesOrderByName(int, int, edu.ur.order.OrderType)
	 */
	public List<ExternalAccountType> getExternalAccountTypesOrderByName(
			int rowStart, int numberOfResultsToShow, OrderType orderType) {
		return externalAccountTypeDAO.getOrderByName(rowStart, numberOfResultsToShow, orderType);
	}

	
	/**
	 * Save the external account type.
	 * 
	 * @see edu.ur.ir.user.ExternalAccountTypeService#save(edu.ur.ir.user.ExternalAccountType)
	 */
	public void save(ExternalAccountType externalAccountType) {
		externalAccountTypeDAO.makePersistent(externalAccountType);
		
	}

	public ExternalAccountTypeDAO getExternalAccountTypeDAO() {
		return externalAccountTypeDAO;
	}

	public void setExternalAccountTypeDAO(
			ExternalAccountTypeDAO externalAccountTypeDAO) {
		this.externalAccountTypeDAO = externalAccountTypeDAO;
	}

}
