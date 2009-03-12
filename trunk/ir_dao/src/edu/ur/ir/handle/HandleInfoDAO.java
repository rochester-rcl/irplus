package edu.ur.ir.handle;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;

/**
 * DAO interface for accessing handle information.
 * 
 * @author Nathan Sarr
 *
 */
public interface HandleInfoDAO extends CountableDAO, CrudDAO<HandleInfo>
{
	/**
	 * Get a handle info object by authority name and local name.
	 * 
	 * @param authorityName - authority name for the handle
	 * @param localName - local name for the handle
	 * 
	 * @return the found handle or null if the handle does not exist
	 */
	HandleInfo get(String authorityName, String localName);
	
	/**
	 * Get a count of the handles with the specified name authority.
	 *  
	 * @param nameAuthorityId - id for the name authority
	 * @return - count of handles found for the name authority
	 */
	public Long getHandleCountForNameAuthority(Long nameAuthorityId);
}
