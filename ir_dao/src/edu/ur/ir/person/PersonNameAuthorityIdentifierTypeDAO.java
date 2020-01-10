package edu.ur.ir.person;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.dao.UniqueNameDAO;
import edu.ur.dao.UniqueSystemCodeDAO;
import edu.ur.ir.item.IdentifierType;

public interface PersonNameAuthorityIdentifierTypeDAO extends CountableDAO, 
CrudDAO<PersonNameAuthorityIdentifierType>, NameListDAO, UniqueNameDAO<PersonNameAuthorityIdentifierType>,UniqueSystemCodeDAO<PersonNameAuthorityIdentifierType> {
	
	/**
	 * Get the list of identifier types.
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param sortType - Order (Desc/Asc) 
	 * 
	 * @return list of identifiers found.
	 */
	public List<PersonNameAuthorityIdentifierType> getIdentifierTypesOrderByName(
			final int rowStart, final int numberOfResultsToShow, final String sortType);

}
