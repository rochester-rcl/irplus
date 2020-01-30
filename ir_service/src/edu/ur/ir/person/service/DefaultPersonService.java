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

import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;
import edu.ur.ir.person.PersonNameAuthorityIdentifier;
import edu.ur.ir.person.PersonNameAuthorityIdentifierDAO;
import edu.ur.ir.person.PersonNameAuthorityIdentifierType;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameDAO;
import edu.ur.ir.person.PersonService;
import edu.ur.order.OrderType;

/**
 * Default implementation of the person service
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultPersonService implements PersonService {

	/** eclipse generated id  */
	private static final long serialVersionUID = -2548228336438130571L;

	/** deals with person related data access.  */
	private PersonNameAuthorityDAO personNameAuthorityDAO;
	
	/** data access class for person names */
	private PersonNameDAO personNameDAO;
	
	/** service for accessing person anem authority identifiers */
	private PersonNameAuthorityIdentifierDAO personNameAuthorityIdentifierDAO;

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
	public List<PersonName> get(  int rowStart, int rowEnd,
			Long personId) {
		
		return personNameDAO.getPersonNames( rowStart, rowEnd, personId);
	}

	/**
	 * @see edu.ur.ir.person.PersonService#getCount(java.util.List, java.lang.Long)
	 */
	public Integer getCount(Long personId) {
		return personNameDAO.getPersonNamesCount(personId);
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

	/**
	 * Get the person name authority identifier.
	 * 
	 * @param id - of the person name authority identifier
	 * @param lock
	 * @return
	 */
	public PersonNameAuthorityIdentifier getPersonNameAuthorityIdentifier(Long id, boolean lock) {
		return personNameAuthorityIdentifierDAO.getById(id, lock);
	}
	
	/**
	 * Delete the person name authority identifier
	 * 
	 * @param personNameAuthorityIdentifier
	 */
	public void deletePersonNameAuthority(PersonNameAuthorityIdentifier personNameAuthorityIdentifier) {
		personNameAuthorityIdentifierDAO.makeTransient(personNameAuthorityIdentifier);
	}
		
	
	public PersonNameAuthorityIdentifierDAO getPersonNameAuthorityIdentifierDAO() {
		return personNameAuthorityIdentifierDAO;
	}

	public void setPersonNameAuthorityIdentifierDAO(PersonNameAuthorityIdentifierDAO personNameAuthorityIdentifierDAO) {
		this.personNameAuthorityIdentifierDAO = personNameAuthorityIdentifierDAO;
	}
	
	
	/**
	 * Get the person name authorities ordered by last name.
	 * 
	 * @see edu.ur.ir.person.PersonService#getPersonNameAuthorityByLastName(int, int, edu.ur.order.OrderType)
	 */
	public List<PersonNameAuthority> getPersonNameAuthorityByLastName(
			int rowStart, int maxResults, OrderType orderType) {
		return personNameAuthorityDAO.getPersonNameAuthorityByLastName(rowStart, maxResults, orderType);
	}
	
	/**
	 * Get unused authority types available for the specified authority id.
	 * 
	 * @param authorityId - unique authority id of the person name
	 * @return
	 */
	public List<PersonNameAuthorityIdentifierType> getPossibleIdentifierTypes(Long authorityId){
		return personNameAuthorityDAO.getPossibleIdentifierTypes(authorityId);
	}
	
	/**
	 * Get the identifier for the specified person name authority if it exists.
	 * 
	 * @param identifierTypeId - id for the type of identifier
	 * @param personNameAuthorityId - id for the person name authority
	 * 
	 * @return - person name authority identifier
	 */
	public PersonNameAuthorityIdentifier getByTypeAuthority(Long identifierTypeId, Long personNameAuthorityId)
	{
		return personNameAuthorityIdentifierDAO.getByTypeAuthority(identifierTypeId, personNameAuthorityId);
	}

	@Override
	public PersonNameAuthorityIdentifier getByTypeValue(Long identfierTypeId, String value) {
		return personNameAuthorityIdentifierDAO.getByTypeValue(identfierTypeId, value);
	}

}
