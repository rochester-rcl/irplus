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

package edu.ur.ir.web.action.user;

import org.apache.log4j.Logger;


import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;

/**
 * Re-Index all users with a workspace.
 * 
 * @author Nathan Sarr
 *
 */
public class ReIndexAllUsersWithWorkspace  extends ActionSupport{

	/* eclipse generated id */
	private static final long serialVersionUID = -2472474629623058036L;
	
	/*  Get the logger for this class */
	private static final Logger log = Logger.getLogger(ReIndexAllUsersWithWorkspace.class);
	
	/* the user service for accessing user information */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;

	/* Service for dealing with index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;


	public String execute() throws Exception
	{
		log.debug("re index users called");
		IndexProcessingType updateType = indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE);
		userWorkspaceIndexProcessingRecordService.reIndexAllWorkspaceUsers(updateType);
		return SUCCESS;
	}
	
	/**
	 * Set the user workspace index processing record service.
	 * 
	 * @param userWorkspaceIndexProcessingRecordService
	 */
	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}

	/**
	 * Set the index processing type service.
	 * 
	 * @param indexProcessingTypeService
	 */
	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}
	

}
