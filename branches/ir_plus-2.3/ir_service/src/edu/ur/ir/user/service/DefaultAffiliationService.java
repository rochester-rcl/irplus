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

import edu.ur.ir.user.Affiliation;
import edu.ur.ir.user.AffiliationDAO;
import edu.ur.ir.user.AffiliationService;

/**
 * Service for affiliation
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultAffiliationService implements AffiliationService{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -5471187881741739268L;
	
	/** Affiliation data access object */
	private AffiliationDAO affiliationDAO;
	
	/**
	 * Get the affiliation by unique name.
	 * 
	 * @see edu.ur.ir.user.UserService#getAffiliation(java.lang.String)
	 */
	public Affiliation getAffiliation(String name) {
		return affiliationDAO.findByUniqueName(name);
	}

	/**
	 * Get all affiliations.
	 * 
	 * @see edu.ur.ir.user.UserService#getAllAffiliations()
	 */
	@SuppressWarnings("unchecked")
	public List<Affiliation> getAllAffiliations() {
		return affiliationDAO.getAll();
	}

	/**
	 * Get all affiliations ordered by name.
	 * 
	 * @see edu.ur.ir.user.UserService#getAllAffiliationsNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<Affiliation> getAllAffiliationsNameOrder() {
		return affiliationDAO.getAllNameOrder();
	}


	/**
	 * Get all affiliations within the specified range.
	 * 
	 * @see edu.ur.ir.user.UserService#getAllAffiliationsOrderByName(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<Affiliation> getAllAffiliationsOrderByName(int startRecord, int numRecords) {
		return affiliationDAO.getAllOrderByName(startRecord, numRecords);
	}

	/**
	 * Get the affiliation with the specified id.
	 * 
	 * @see edu.ur.ir.user.UserService#getAffiliation(java.lang.Long, boolean)
	 */
	public Affiliation getAffiliation(Long id, boolean lock) {
		return affiliationDAO.getById(id, lock);
	}

	/**
	 * Save the affiliation
	 * 
	 * @see edu.ur.ir.user.UserService#makeAffiliationPersistent(edu.ur.ir.user.Affiliation)
	 */
	public void makeAffiliationPersistent(Affiliation entity) {
		affiliationDAO.makePersistent(entity);
	}

	/**
	 * Delete the affiliation.
	 * 
	 * @see edu.ur.ir.user.UserService#deleteAffiliation(edu.ur.ir.user.Affiliation)
	 */
	public void deleteAffiliation(Affiliation entity) {
		affiliationDAO.makeTransient(entity);
	}

	/**
	 * Get the affiliation data access object.
	 * 
	 * @return
	 */
	public AffiliationDAO getAffiliationDAO() {
		return affiliationDAO;
	}

	/**
	 * Set the affiliation data access object.
	 * 
	 * @param affiliationDAO
	 */
	public void setAffiliationDAO(AffiliationDAO affiliationDAO) {
		this.affiliationDAO = affiliationDAO;
	}

	/**
	 * Get the affiliation count.
	 * 
	 * @see edu.ur.ir.user.UserService#getAffiliationCount()
	 */
	public Long getAffiliationCount() {
		return affiliationDAO.getCount();
	}

	/**
	 * Get the affiliations based on the specified criteria, start and end position.
	 * 
	 * @see edu.ur.ir.user.UserService#getAffiliationsOrderByName(int, int, String)
	 */
	public List<Affiliation> getAffiliationsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return affiliationDAO.getAffiliations(rowStart, numberOfResultsToShow, sortType);
	}

	/**
	 * Delete the affiliations.
	 * 
	 * @see edu.ur.ir.user.UserService#deleteAffiliations(java.util.List)
	 */
	public void deleteAffiliations(List<Affiliation> affiliations) {
		for(Affiliation affiliation : affiliations)
		{
			affiliationDAO.makeTransient(affiliation);
		}
		
	}


}
