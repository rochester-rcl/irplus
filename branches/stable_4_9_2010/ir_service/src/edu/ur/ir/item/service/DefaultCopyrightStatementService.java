package edu.ur.ir.item.service;
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


import java.util.List;

import edu.ur.ir.item.CopyrightStatement;
import edu.ur.ir.item.CopyrightStatementDAO;
import edu.ur.ir.item.CopyrightStatementService;
import edu.ur.order.OrderType;

/**
 * Default Service for dealing with copyrights.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultCopyrightStatementService implements CopyrightStatementService {
	
	/** Copyright statement data access. */
	private CopyrightStatementDAO copyrightStatementDAO;


	/**
	 * Delete the copyright statement.
	 * 
	 * @see edu.ur.ir.item.CopyrightStatementService#delete(edu.ur.ir.item.CopyrightStatement)
	 */
	public void delete(CopyrightStatement copyrightStatement) 
	{
		copyrightStatementDAO.makeTransient(copyrightStatement);
	}


	/**
	 * Get the copyright statement by name.
	 * 
	 * @see edu.ur.ir.item.CopyrightStatementService#get(java.lang.String)
	 */
	public CopyrightStatement get(String name) {
		return copyrightStatementDAO.findByUniqueName(name);
	}

	/**
	 * Get the copyright statement by id.
	 * 
	 * @see edu.ur.ir.item.CopyrightStatementService#get(java.lang.Long, boolean)
	 */
	public CopyrightStatement get(Long id, boolean lock) {
		return copyrightStatementDAO.getById(id, lock);
	}

	/**
	 * Get all copyright statements.
	 * 
	 * @see edu.ur.ir.item.CopyrightStatementService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<CopyrightStatement> getAll() {
		return copyrightStatementDAO.getAll();
	}


	/**
	 * Get the copyright statements order by name.
	 * 
	 * @see edu.ur.ir.item.CopyrightStatementService#getCopyrightStatementsOrderByName(int, int, edu.ur.order.OrderType)
	 */
	public List<CopyrightStatement> getCopyrightStatementsOrderByName(
			int rowStart, int numberOfResultsToShow, OrderType orderType) {
		return copyrightStatementDAO.getCopyrightStatementsOrderByName(rowStart, numberOfResultsToShow, orderType);
	}

	/**
	 * Get the count of copyright statements.
	 * 
	 * @see edu.ur.ir.item.CopyrightStatementService#getCount()
	 */
	public Long getCount() {
		return copyrightStatementDAO.getCount();
	}

	/**
	 * Save the copyright statement.
	 * 
	 * @see edu.ur.ir.item.CopyrightStatementService#save(edu.ur.ir.item.CopyrightStatement)
	 */
	public void save(CopyrightStatement entity) {
		copyrightStatementDAO.makePersistent(entity);
	}
	
	/**
	 * Get the copyright statement data access object.
	 * 
	 * @return
	 */
	public CopyrightStatementDAO getCopyrightStatementDAO() {
		return copyrightStatementDAO;
	}

	/**
	 * Set the copyright statement data access object.
	 * 
	 * @param copyrightStatementDAO
	 */
	public void setCopyrightStatementDAO(CopyrightStatementDAO copyrightStatementDAO) {
		this.copyrightStatementDAO = copyrightStatementDAO;
	}


}
