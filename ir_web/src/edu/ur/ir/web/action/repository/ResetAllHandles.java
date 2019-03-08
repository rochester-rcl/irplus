/**  
   Copyright 2008 University of Rochester

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

package edu.ur.ir.web.action.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.repository.Repository;

/**
 * Reset all institutional item handles.
 * 
 * @author Nathan Sarr
 *
 */
public class ResetAllHandles extends ActionSupport{
	
	/** eclipse gnerated id */
	private static final long serialVersionUID = 62384015681849512L;

	/**  Get the logger for this class */
	private static final Logger log = LogManager.getLogger( ResetAllHandles.class);
	
	/** processing service for indexing institutional items */
	private InstitutionalItemService institutionalItemService;
	

	public String execute()
	{
		log.debug("reset all handles called");
		institutionalItemService.resetAllHandles(25, Repository.DEFAULT_REPOSITORY_ID);
		return SUCCESS;
	}


	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}


	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}



}
