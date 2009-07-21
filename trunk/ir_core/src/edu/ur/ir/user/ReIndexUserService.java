package edu.ur.ir.user;

import java.io.File;

/**
 * This will re-index all users
 * 
 * @author Nathan Sarr
 *
 */
public interface ReIndexUserService {
	
	/**
	 * Re-Index the institutional items in an institutional repository.
	 * 
	 * @param batchSize - number of users to index at a time
	 * @param userIndexFolder - location of the user index
	 */
	public void reIndexUsers(int batchSize, File userIndexFolder);


}
