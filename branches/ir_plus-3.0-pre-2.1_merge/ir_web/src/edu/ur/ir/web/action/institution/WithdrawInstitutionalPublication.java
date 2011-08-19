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

import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;


/**
 * Withdraw an institutional item.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class WithdrawInstitutionalPublication extends ActionSupport implements UserIdAware{

	/** Eclipse generated id. */
	private static final long serialVersionUID = -2600747248386611252L;
	
	/**  Logger for preview publication action */
	private static final Logger log = Logger.getLogger(WithdrawInstitutionalPublication.class);
	
	/** Id of the institutional item being viewed.  */
	private Long institutionalItemId;
	
	/** Reason to withdraw publication */
	private String withdrawReason;
	
	/** Indicates whether metadata should be shown */
	private Boolean showMetadata = false;
	
	/** Indicates whether to withdraw all versions */
	private Boolean withdrawAllVersions = false;
	
	/** Service for dealing with user file system. */
	private InstitutionalItemService institutionalItemService;
	
	/** Service for accessing user information */
	private UserService userService;
	
	/** User id */
	private Long userId;
	
	/** Version number to be withdrawn */
	private int versionNumber;
	
	
	/**
	 * Prepare for action
	 */
	public String execute(){
		log.debug("Institutional ItemId = " + institutionalItemId);
        IrUser user = userService.getUser(userId, false);
        
 		
		if (institutionalItemId != null) {
			InstitutionalItem institutionalItem = institutionalItemService.getInstitutionalItem(institutionalItemId, false);
			
			if( !institutionalItem.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE))
			{
				return "accessDenied";
			}
			
		    if (withdrawAllVersions) {
				Set<InstitutionalItemVersion> versions = institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersions();
				for (InstitutionalItemVersion version : versions) {
					version.withdraw(user, withdrawReason, showMetadata);
					Set<ItemFile> files = version.getItem().getItemFiles();
					for( ItemFile f : files )
				    {
				    	f.setPublic(false);
				    }
				}
			} else {
				institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(versionNumber).withdraw(user, withdrawReason, showMetadata);
			    Set<ItemFile> files = institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(versionNumber).getItem().getItemFiles();
			    for( ItemFile f : files )
			    {
			    	f.setPublic(false);
			    }
			}
			
			institutionalItemService.saveInstitutionalItem(institutionalItem);
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

	public void setWithdrawReason(String withdrawReason) {
		this.withdrawReason = withdrawReason;
	}

	public void setShowMetadata(Boolean showMetadata) {
		this.showMetadata = showMetadata;
	}

	public void setWithdrawAllVersions(Boolean withdrawAllVersions) {
		this.withdrawAllVersions = withdrawAllVersions;
	}

	public void injectUserId(Long userId) {
		this.userId = userId;
		
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
}
