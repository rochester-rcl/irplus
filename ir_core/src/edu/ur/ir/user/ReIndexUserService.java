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
	 * @param repository
	 */
	public void reIndexUsers(int batchSize, File userIndexFolder);


}
