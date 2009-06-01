package edu.ur.ir.user.service;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.ReIndexUserService;
import edu.ur.ir.user.UserIndexService;
import edu.ur.ir.user.UserService;
import edu.ur.order.OrderType;

/**
 * Default implementation for re-indexing users in the system.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultReIndexUserService implements ReIndexUserService{
	
	/** Service for dealing with users. */
	private UserService userService;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultReIndexUserService.class);
	
	/** Service for indexing user information */
	private UserIndexService userIndexService;
	
	/**
	 * Re-index all of the users in the system.
	 * 
	 * @see edu.ur.ir.user.ReIndexUserService#reIndexUsers(int)
	 */
	public void reIndexUsers(int batchSize, File userIndexFolder) {
		log.debug("Re-Indexing users");
		
		if(batchSize <= 0 )
		{
			throw new IllegalStateException("Batch size cannot be less than or equal to 0 batch Size = " + batchSize);
		}
		
		int rowStart = 0;
		
		int numberOfUsers = userService.getUserCount().intValue();
		log.debug("processing a total of " + numberOfUsers);
		
		boolean overwriteExistingIndex = true;
		while(rowStart < numberOfUsers)
		{
			log.debug("row start = " + rowStart);
			log.debug("batch size = " +  batchSize);
			
			// notice the minus one because we are starting at 0
			log.debug("processing " + rowStart + " to " + (rowStart + batchSize - 1) );
			
		    List<IrUser> users = userService.getUsers(rowStart, batchSize, "username", OrderType.DESCENDING_ORDER);
		    userIndexService.addUsers(users, userIndexFolder, overwriteExistingIndex);
		    overwriteExistingIndex = false;
		    
		    rowStart = rowStart + batchSize;
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserIndexService getUserIndexService() {
		return userIndexService;
	}

	public void setUserIndexService(UserIndexService userIndexService) {
		this.userIndexService = userIndexService;
	}


}
