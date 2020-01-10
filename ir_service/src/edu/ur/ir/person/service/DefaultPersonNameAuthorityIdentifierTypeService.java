package edu.ur.ir.person.service;

import java.util.List;

import edu.ur.ir.person.PersonNameAuthorityIdentifierType;
import edu.ur.ir.person.PersonNameAuthorityIdentifierTypeDAO;

public class DefaultPersonNameAuthorityIdentifierTypeService {
	
	/**  Data access class for contributor type information */
	private PersonNameAuthorityIdentifierTypeDAO personNameAuthorityIdentifierTypeDAO;

	/**
	 * Get the contributor type data access.
	 * 
	 * @return
	 */
	public PersonNameAuthorityIdentifierTypeDAO getPersonNameAuthorityIdentifierTypeDAO() {
		return personNameAuthorityIdentifierTypeDAO;
	}

	 
	public void setPersonNameAuthorityIdentifierTypeDAO(PersonNameAuthorityIdentifierTypeDAO personNameAuthorityIdentifierTypeDAO) {
		this.personNameAuthorityIdentifierTypeDAO = personNameAuthorityIdentifierTypeDAO;
	}

	 
	public boolean delete(PersonNameAuthorityIdentifierType contributorType) {
		personNameAuthorityIdentifierTypeDAO.makeTransient(contributorType);
		return true;
	}

	 
	public PersonNameAuthorityIdentifierType get(String name) {
		return personNameAuthorityIdentifierTypeDAO.findByUniqueName(name);
	}

	 
	public PersonNameAuthorityIdentifierType get(Long id, boolean lock) {
		return personNameAuthorityIdentifierTypeDAO.getById(id, lock);
	}

	 
	public List<PersonNameAuthorityIdentifierType> getPersonNameAuthorityIdentifierTypesOrderByName(
			final int rowStart, 
    		final int numberOfResultsToShow, 
    		final String sortType) {
		return personNameAuthorityIdentifierTypeDAO.getIdentifierTypesOrderByName(rowStart, numberOfResultsToShow, sortType);
	}

	 
	public Long getPersonNameAuthorityIdentifierTypesCount() {
		return personNameAuthorityIdentifierTypeDAO.getCount();
	}

	 
	public void save(PersonNameAuthorityIdentifierType contributorType) {
		personNameAuthorityIdentifierTypeDAO.makePersistent(contributorType);
	}

     
    @SuppressWarnings("unchecked")
	public List<PersonNameAuthorityIdentifierType> getAll() {
    	return personNameAuthorityIdentifierTypeDAO.getAll();
    }

	
	 
	public PersonNameAuthorityIdentifierType getByUniqueSystemCode(String systemCode) {
		return personNameAuthorityIdentifierTypeDAO.getByUniqueSystemCode(systemCode);
	}

}