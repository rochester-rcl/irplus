package edu.ur.ir.person;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;

public interface PersonNameAuthorityIdentifierDAO extends CountableDAO, CrudDAO<PersonNameAuthorityIdentifier>
{
	
	/**
	 * Get the person name identifier by type and value
	 * 
	 * @param identfierType the type of identifier
	 * @param value the value of the identifier.
	 * 
	 * @return the item identifier found.
	 */
	public PersonNameAuthorityIdentifier getByTypeValue(Long identfierType, String value);
	
    /**
	 * Get the item identifier by type and person name authority id
	 * 
	 * @param identfierType the type of identifier
	 * @param unique identifier for the person name authority 
	 * 
	 * @return the item identifier found.
	 */
	public PersonNameAuthorityIdentifier getByTypeAuthority(Long identfierType, Long personNameAuthorityId);


}
