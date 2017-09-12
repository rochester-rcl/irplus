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

package edu.ur.ir.web.action.item;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemObject;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.order.AscendingOrderComparator;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.util.InstitutionalCollectionPermissionHelper;


/**
 * Action to preview publication
 *  
 * @author Sharmila Ranganathan
 *
 */
public class ViewPersonalPublication extends ActionSupport implements UserIdAware {

	/**  Eclipse generated id  */
	private static final long serialVersionUID = -1202241827964719011L;
	
	/**  Logger for preview publication action */
	private static final Logger log = Logger.getLogger(ViewPersonalPublication.class);
	
	/** Parent collection id */
	private Long parentCollectionId;
	
	/** Id of the generic item.  */
	private Long genericItemId;
	
	private Long researcherId;
	
	/** id of the user trying to access the document */
	private Long userId;
	
	/**  Generic Item being viewed */
	private GenericItem item;
	
	/** Personal Item Id  */
	private Long personalItemId; 
	
	/** Service for dealing with user file system. */
	private UserPublishingFileSystemService userPublishingFileSystemService;
	
	/** Service for the researcher */
	private ResearcherService researcherService;
	
	/** researcher who has the item */
	private Researcher researcher;
	
	/** Service for dealing with item. */
	private ItemService itemService;

	/** Item object sorted for display */
	private List<ItemObject> itemObjects;
	
	/** Id of institutional item being edited */
	private Long institutionalItemId;
	
	/** Service for accessing user information */
	private UserService userService;
	
	private InstitutionalCollectionPermissionHelper institutionalCollectionPermissionHelper;

	

	/**
	 * Prepare for action
	 */
	public String execute(){
		log.debug("Peronal ItemId = " + genericItemId);

		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false);
		}

		IrUser owner = item.getOwner();
		
		IrUser accessingUser = userService.getUser(userId, false);
		
		// make sure the user is the owner or administrator.
		if( userId == null || (!owner.getId().equals(userId) && !accessingUser.hasRole(IrRole.ADMIN_ROLE) && 
				!institutionalCollectionPermissionHelper.isInstitutionalCollectionAdmin(accessingUser, genericItemId)) )
		{
			return "accessDenied";
		}
		
		
		if( researcherId != null )
		{
			researcher = researcherService.getResearcher(researcherId, false);
		}

		itemObjects = item.getItemObjects();
		
		// Sort item objects by order
		Collections.sort(itemObjects,   new AscendingOrderComparator());
		
		return SUCCESS;		
	}

	/**
	 * Get item that is being edited 
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set item that is being edited
	 *  
	 * @param item
	 */
	public void setItem(GenericItem item) {
		this.item = item;
	}

	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}


	public List<ItemObject> getItemObjects() {
		return itemObjects;
	}

	public Long getGenericItemId() {
		return genericItemId;
	}

	public void setGenericItemId(Long genericItemId) {
		this.genericItemId = genericItemId;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public Long getResearcherId() {
		return researcherId;
	}

	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}

	public ResearcherService getResearcherService() {
		return researcherService;
	}

	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	public Researcher getResearcher() {
		return researcher;
	}

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public Long getPersonalItemId() {
		return personalItemId;
	}

	public void setPersonalItemId(Long personalItemId) {
		this.personalItemId = personalItemId;
	}
	
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setInstitutionalCollectionPermissionHelper(
			InstitutionalCollectionPermissionHelper institutionalCollectionPermissionHelper) {
		this.institutionalCollectionPermissionHelper = institutionalCollectionPermissionHelper;
	}



}
