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

import edu.ur.dao.CriteriaHelper;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameDAO;
import edu.ur.ir.person.PersonService;

/**
 * Default implementation of the person service
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultPersonService implements PersonService {

	/** deals with person related data access.  */
	private PersonNameAuthorityDAO personNameAuthorityDAO;
	
	/** data access class for person names */
	private PersonNameDAO personNameDAO;
	
	/**
	 * @see edu.ur.ir.person.PersonService#delete(edu.ur.ir.person.PersonNameAuthority)
	 */
	public void delete(PersonNameAuthority p) {
		personNameAuthorityDAO.makeTransient(p);
	}
	
	/**
	 * @see edu.ur.ir.person.PersonService#getAuthority(java.lang.Long, boolean)
	 */
	public PersonNameAuthority getAuthority(Long id, boolean lock) {
		return personNameAuthorityDAO.getById(id, lock);
	}

	/**
	 * @see edu.ur.ir.person.PersonService#get(int, int, String, String)
	 */
	public List<PersonNameAuthority> get(int rowStart, 
    		int numberOfResultsToShow, String sortElement, String sortType) {
		return personNameAuthorityDAO.getPersons(rowStart, 
	    		numberOfResultsToShow, sortElement, sortType);
	}

	/**
	 * @see edu.ur.ir.person.PersonService#getCount()
	 */
	public Long getCount() {
		return personNameAuthorityDAO.getCount();
	}

	/**
	 * @see edu.ur.ir.person.PersonService#save(edu.ur.ir.person.PersonNameAuthority)
	 */
	public void save(PersonNameAuthority personNameAuthority) {
		personNameAuthorityDAO.makePersistent(personNameAuthority);
	}

	/**
	 * Person data access.
	 * 
	 * @return
	 */
	public PersonNameAuthorityDAO getPersonNameAuthorityDAO() {
		return personNameAuthorityDAO;
	}

	/**
	 * Set person data access.
	 * 
	 * @param personNameAuthorityDAO
	 */
	public void setPersonNameAuthorityDAO(PersonNameAuthorityDAO personNameAuthorityDAO) {
		this.personNameAuthorityDAO = personNameAuthorityDAO;
	}

	
	/**
	 * @see edu.ur.ir.person.PersonService#delete(edu.ur.ir.person.PersonName)
	 */
	public void delete(PersonName name) {
		personNameDAO.makeTransient(name);
	}

	/**
	 * @see edu.ur.ir.person.PersonService#getName(java.lang.Long, boolean)
	 */
	public PersonName getName(Long id, boolean lock) {
		return personNameDAO.getById(id, lock);
	}

	/**
	 * @see edu.ur.ir.person.PersonService#get(java.util.List, int, int, java.lang.Long)
	 */
	public List<PersonName> get(
			List<CriteriaHelper> criteriaHelpers, int rowStart, int rowEnd,
			Long personId) {
		
		return personNameDAO.getPersonNames(criteriaHelpers, rowStart, rowEnd, personId);
	}

	/**
	 * @see edu.ur.ir.person.PersonService#getCount(java.util.List, java.lang.Long)
	 */
	public Integer getCount(List<CriteriaHelper> criteriaHelpers,
			Long personId) {
		return personNameDAO.getPersonNamesCount(criteriaHelpers, personId);
	}

	/**
	 * Save the person name.
	 * 
	 * @see edu.ur.ir.person.PersonService#save(edu.ur.ir.person.PersonName)
	 */
	public void save(PersonName name) {
		personNameDAO.makePersistent(name);
		
	}

	/**
	 * Get the person name data access object
	 * 
	 * @return
	 */
	public PersonNameDAO getPersonNameDAO() {
		return personNameDAO;
	}

	/**
	 * Set the person name data access.
	 * 
	 * @param personNameDAO
	 */
	public void setPersonNameDAO(PersonNameDAO personNameDAO) {
		this.personNameDAO = personNameDAO;
	}
	
	/**
	 * Search the person name having the specified first and last name.
	 * 
	 * @param firstName first name to search for
	 * @param lastName last name to search for
	 * 
	 * @return List of Person names containing the specified first and last name.
	 */
	public List<PersonName> search(String firstName, String lastName) {
		return personNameDAO.findPersonLikeFirstLastName(firstName, lastName, 0, 30);
	}

}
