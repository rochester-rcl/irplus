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

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameDAO;
import edu.ur.ir.person.PersonNameService;
import edu.ur.order.OrderType;

/**
 * Implementation of the person name service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultPersonNameServce implements PersonNameService{

	private PersonNameDAO personNameDAO;
	

	/**
	 * @see edu.ur.ir.person.PersonNameService#getCollectionPersonNamesBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, char, edu.ur.order.OrderType)
	 */
	public List<PersonName> getCollectionPersonNamesBetweenChar(int rowStart,
			int maxResults, InstitutionalCollection collection,
			char firstChar, char lastChar, OrderType orderType) {
		return personNameDAO.getCollectionPersonNamesBetweenChar(rowStart, maxResults, collection, firstChar, lastChar, orderType);
	}

	/**
	 * @see edu.ur.ir.person.PersonNameService#getCollectionPersonNamesOrderByLastName(int, int, edu.ur.ir.institution.InstitutionalCollection, edu.ur.order.OrderType)
	 */
	public List<PersonName> getCollectionPersonNamesOrderByLastName(
			int rowStart, int rowEnd, InstitutionalCollection collection,
			OrderType orderType) {
		return personNameDAO.getCollectionPersonNamesByLastName(rowStart, rowEnd, collection, orderType);
	}


	/**
	 * @see edu.ur.ir.person.PersonNameService#getCount()
	 */
	public Long getCount() {
		return personNameDAO.getCount();
	}

	
	/**
	 * @see edu.ur.ir.person.PersonNameService#getCount(char)
	 */
	public Long getCount(char lastNameFirstChar) {
		return personNameDAO.getCount(lastNameFirstChar);
	}

	
	/**
	 * @see edu.ur.ir.person.PersonNameService#getCount(char, char)
	 */
	public Long getCount(char lastNameFirstCharRange, char lastNamelastCharRange) {
		return personNameDAO.getCount(lastNameFirstCharRange, lastNamelastCharRange);
	}

	
	/**
	 * @see edu.ur.ir.person.PersonNameService#getCount(edu.ur.ir.institution.InstitutionalCollection, char)
	 */
	public Long getCount(InstitutionalCollection collection,
			char lastNameFirstChar) {
		return personNameDAO.getCount(collection, lastNameFirstChar);
	}

	
	/**
	 * @see edu.ur.ir.person.PersonNameService#getCount(edu.ur.ir.institution.InstitutionalCollection, char, char)
	 */
	public Long getCount(InstitutionalCollection collection,
			char lastNameFirstCharRange, char nameLastCharRange) {
		return personNameDAO.getCount(collection, lastNameFirstCharRange, nameLastCharRange);
	}

	
	/**
	 * @see edu.ur.ir.person.PersonNameService#getPersonNamesBetweenChar(int, int, char, char, edu.ur.order.OrderType)
	 */
	public List<PersonName> getPersonNamesBetweenChar(int rowStart,
			int maxResults, char lastNamefirstChar, char lastNamelastChar,
			OrderType orderType) {
		return personNameDAO.getPersonNamesBetweenChar(rowStart, maxResults, lastNamefirstChar, lastNamelastChar, orderType);
	}

	
	/**
	 * @see edu.ur.ir.person.PersonNameService#getPersonNamesByChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, java.lang.String)
	 */
	public List<PersonName> getPersonNamesByChar(int rowStart, int maxResults,
			InstitutionalCollection collection, char firstChar,
			OrderType orderType) {
		return personNameDAO.getCollectionPersonNamesByChar(rowStart, maxResults, collection, firstChar, orderType);
	}


	/**
	 * @see edu.ur.ir.person.PersonNameService#getPersonNamesByLastNameChar(int, int, char, edu.ur.order.OrderType)
	 */
	public List<PersonName> getPersonNamesByLastNameChar(int rowStart,
			int maxResults, char firstChar, OrderType orderType) {
		return personNameDAO.getPersonNamesByChar(rowStart, maxResults, firstChar, orderType);
	}


	/**
	 * @see edu.ur.ir.person.PersonNameService#getPersonNamesOrderByLastName(int, int, edu.ur.order.OrderType)
	 */
	public List<PersonName> getPersonNamesOrderByLastName(int rowStart,
			int maxResults, OrderType orderType) {
		return personNameDAO.getPersonNamesByLastName(rowStart, maxResults, orderType);
	}
	
	public PersonNameDAO getPersonNameDAO() {
		return personNameDAO;
	}

	public void setPersonNameDAO(PersonNameDAO personNameDAO) {
		this.personNameDAO = personNameDAO;
	}

	@Override
	public Long getCount(InstitutionalCollection collection) {
		// TODO Auto-generated method stub
		return null;
	}


}
