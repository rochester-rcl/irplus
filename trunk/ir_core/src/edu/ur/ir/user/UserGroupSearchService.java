package edu.ur.ir.user;

import java.io.File;
import java.io.Serializable;

import edu.ur.ir.SearchResults;

/**
 * Service to allows searching across the user group index.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserGroupSearchService extends Serializable {
	
	/**
	 * Returns search results for the user groups.
	 * 
	 * @param userGroupIndexFolder - location where the index folder is location 
	 * @param query - query to execute
	 * @param offset - offset to start at
	 * @param numResults - number of results.
	 * 
	 * @return - set of users groups found for the query.
	 */
	public SearchResults<IrUserGroup> search(File userGroupIndexFolder, String query, int offset, int numResults);

}
 