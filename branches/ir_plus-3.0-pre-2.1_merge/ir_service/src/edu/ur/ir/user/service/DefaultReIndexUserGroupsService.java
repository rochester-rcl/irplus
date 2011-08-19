/**  
   Copyright 2008 - 2011 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  


package edu.ur.ir.user.service;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.ReIndexUserGroupsService;
import edu.ur.ir.user.UserGroupIndexService;
import edu.ur.ir.user.UserGroupService;
import edu.ur.order.OrderType;

/**
 * @author Nathan Sarr
 *
 */
public class DefaultReIndexUserGroupsService implements ReIndexUserGroupsService{

	/* eclipse gernerated id */
	private static final long serialVersionUID = 8437498671003379512L;

	/* Service for dealing with users. */
	private UserGroupService userGroupService;
	

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultReIndexUserGroupsService.class);
	
	/** Service for indexing user information */
	private UserGroupIndexService userGroupIndexService;
	
	/**
	 * Re-index all of the users in the system.
	 * 
	 * @see edu.ur.ir.user.ReIndexUserService#reIndexUsers(int)
	 */
	public int reIndexUserGroups(int batchSize, File userGroupIndexFolder) {
		log.debug("Re-Indexing users");
		
		if(batchSize <= 0 )
		{
			throw new IllegalStateException("Batch size cannot be less than or equal to 0 batch Size = " + batchSize);
		}
		
		int rowStart = 0;
		
		int numberOfUserGroups = userGroupService.getUserGroupsCount().intValue();
		log.debug("processing a total of " + numberOfUserGroups);
		
		boolean overwriteExistingIndex = true;
		
		int numProcessed = 0;
		
		// increase number of users by batch size to make sure 
		// all users are processed 
		while(rowStart <= numberOfUserGroups )
		{
			log.debug("row start = " + rowStart);
			log.debug("batch size = " +  batchSize);
			
			// notice the minus one because we are starting at 0
			log.debug("processing " + rowStart + " to " + (rowStart + (batchSize - 1)) );
			
		    List<IrUserGroup> userGroups = userGroupService.getUserGroupsOrderByName(rowStart, batchSize, OrderType.DESCENDING_ORDER.getType());
		    numProcessed = numProcessed + userGroups.size();
		    userGroupIndexService.add(userGroups, userGroupIndexFolder, overwriteExistingIndex);
		    overwriteExistingIndex = false;
		    
		    rowStart = rowStart + batchSize;
		}
		
		return numProcessed;
	}
	


	/**
	 * Set the user group service.
	 * 
	 * @param userGroupService
	 */
	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

	/**
	 * Set the user group index service.
	 * 
	 * @param userGroupIndexService
	 */
	public void setUserGroupIndexService(UserGroupIndexService userGroupIndexService) {
		this.userGroupIndexService = userGroupIndexService;
	}


}
