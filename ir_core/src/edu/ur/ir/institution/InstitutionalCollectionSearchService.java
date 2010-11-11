package edu.ur.ir.institution;

import java.io.File;
import java.io.Serializable;

import edu.ur.ir.SearchResults;

/**
 * Interface to perform searching over the institutional collection index. 
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalCollectionSearchService extends Serializable {
	
	/**
	 * Returns search results for institutional collections.
	 * 
	 * @param institutionalCollectionIndexFolder - folder for the institutional collections
	 * @param query - query to execute
	 * @param offset - offset to start at
	 * @param numResults - number of results.
	 * 
	 * @return - set of users found for the query.
	 */
	public SearchResults<InstitutionalCollection> search(File institutionalCollectionIndexFolder, String query, int offset, int numResults);

}
