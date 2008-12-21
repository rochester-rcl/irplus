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

package edu.ur.ir.web.action.institution;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;


/**
 * Delete an institutional item.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DeleteInstitutionalItem extends ActionSupport implements UserIdAware{

	/** Eclipse generated id. */
	private static final long serialVersionUID = 8663464316939434871L;

	/**  Logger for preview publication action */
	private static final Logger log = Logger.getLogger(DeleteInstitutionalItem.class);
	
	/** Id of the institutional item being viewed.  */
	private Long institutionalItemId;
	
	/** Service for dealing with user file system. */
	private InstitutionalItemService institutionalItemService;
	
	/** Service for accessing user information */
	private UserService userService;
	
	/** User id */
	private Long userId;
	
	/**
	 * Prepare for action
	 */
	public String execute(){
		log.debug("Institutional ItemId = " + institutionalItemId);
        IrUser user = userService.getUser(userId, false);
		
		if (institutionalItemId != null) {
			InstitutionalItem institutionalItem = institutionalItemService.getInstitutionalItem(institutionalItemId, false);
			
			institutionalItemService.deleteInstitutionalItem(institutionalItem, user);
		}		
		return SUCCESS;		
	}

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
		
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
