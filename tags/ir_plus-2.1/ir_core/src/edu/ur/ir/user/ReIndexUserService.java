package edu.ur.ir.user;

import java.io.File;
import java.io.Serializable;

/**
 * This will re-index all users
 * 
 * @author Nathan Sarr
 *
 */
public interface ReIndexUserService extends Serializable{
	
	/**
	 * Re-Index the users in the institutional repository
	 * 
	 * @param batchSize - number of users to index at a time
	 * @param userIndexFolder - location of the user index
	 * @return total number of users processed.
	 */
	public int reIndexUsers(int batchSize, File userIndexFolder);


}
