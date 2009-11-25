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


package edu.ur.ir.person.service;

import java.util.List;

import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeDAO;
import edu.ur.ir.person.ContributorTypeService;

/**
 * Service class for dealing with contributor types.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultContributorTypeService implements ContributorTypeService{
	
	/**  Data access class for contributor type information */
	private ContributorTypeDAO contributorTypeDAO;

	/**
	 * Get the contributor type data access.
	 * 
	 * @return
	 */
	public ContributorTypeDAO getContributorTypeDAO() {
		return contributorTypeDAO;
	}

	/**
	 * Set the contributor type data access.
	 * 
	 * @param contributorTypeDAO
	 */
	public void setContributorTypeDAO(ContributorTypeDAO contributorTypeDAO) {
		this.contributorTypeDAO = contributorTypeDAO;
	}

	/**
	 * @see edu.ur.ir.person.ContributorTypeService#delete(java.lang.Long)
	 */
	public boolean delete(ContributorType contributorType) {
		contributorTypeDAO.makeTransient(contributorType);
		return true;
	}

	/**
	 * @see edu.ur.ir.person.ContributorTypeService#get(java.lang.String)
	 */
	public ContributorType get(String name) {
		return contributorTypeDAO.findByUniqueName(name);
	}

	/**
	 * @see edu.ur.ir.person.ContributorTypeService#get(java.lang.Long, boolean)
	 */
	public ContributorType get(Long id, boolean lock) {
		return contributorTypeDAO.getById(id, lock);
	}

	/**
	 * @see edu.ur.ir.person.ContributorTypeService#getContributorTypes(java.util.List, int, int)
	 */
	public List<ContributorType> getContributorTypesOrderByName(
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return contributorTypeDAO.getContributorTypes(rowStart, numberOfResultsToShow, sortType);
	}

	/**
	 * @see edu.ur.ir.person.ContributorTypeService#getContributorTypesCount()
	 */
	public Long getContributorTypesCount() {
		return contributorTypeDAO.getCount();
	}

	/**
	 * @see edu.ur.ir.person.ContributorTypeService#save(edu.ur.ir.person.ContributorType)
	 */
	public void save(ContributorType contributorType) {
		contributorTypeDAO.makePersistent(contributorType);
	}

    /**
     * Get all the contributor types.
     * 
     * @see edu.ur.ir.person.ContributorTypeService#getAllContributorType()
     */
    @SuppressWarnings("unchecked")
	public List<ContributorType> getAll() {
    	return contributorTypeDAO.getAll();
    }

	
	/**
	 * Get the contributor type by unique system code.
	 * 
	 * @see edu.ur.ir.person.ContributorTypeService#getByUniqueSystemCode(java.lang.String)
	 */
	public ContributorType getByUniqueSystemCode(String systemCode) {
		return contributorTypeDAO.getByUniqueSystemCode(systemCode);
	}

}
