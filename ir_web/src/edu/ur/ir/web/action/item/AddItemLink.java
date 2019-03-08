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


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemLink;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.util.InstitutionalCollectionPermissionHelper;

/**
 * Action to add a link to the item.
 * 
 * @author Sharmila Ranganarhan
 *
 */
public class AddItemLink extends ActionSupport implements Preparable, UserIdAware {
	
	/** the name of the folder to add */
	private String linkName;
	
	/** Description of the folder */
	private String linkDescription;
	
	/** id of the user */
	private Long userId;
	
	/** URL */
	private String linkUrl;
	
	/** Item id to add the link to */
	private Long genericItemId;
	
	/** Id of the folder to update for updating  */
	private Long updateLinkId;
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = 1355765084143781189L;
	
	/**  Logger for add researcher folder action */
	private static final Logger log = LogManager.getLogger(AddItemLink.class);
	
	/** Generic item being edited */
	private GenericItem item;
	
	/** Service for item */
	private ItemService itemService;
	
	/** User Publishing File System Service */
	private UserPublishingFileSystemService userPublishingFileSystemService;
	
	/** index processing type service */
	private IndexProcessingTypeService indexProcessingTypeService;

	/** Institutional item service */
	private InstitutionalItemService institutionalItemService;
	
	/** process for setting up personal workspace information to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/** service for dealing with institutional item version information */
	private InstitutionalItemVersionService institutionalItemVersionService;
	
	/** Service to deal with user information */
	private UserService userService;

	/** Id of institutional item being edited */
	private Long institutionalItemId;

	private InstitutionalCollectionPermissionHelper institutionalCollectionPermissionHelper;


	/**
	 * Prepare for action
	 */
	public void prepare() {
		log.debug("Item Id:"+ genericItemId);
		
		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false);
		}
	}
	
	/**
	 * Create the new link
	 */
	public String save() throws NoIndexFoundException
	{
		log.debug("Add link " );
		
        IrUser user = userService.getUser(userId, false);
		
		// make sure the user is the owner.
		if( !item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE) && 
				!institutionalCollectionPermissionHelper.isInstitutionalCollectionAdmin(user, genericItemId))
		{
			return "accessDenied";
		}

		ItemLink itemLink = item.createLink(linkName, linkUrl);
		itemLink.setDescription(linkDescription);
		
		itemService.makePersistent(item);

		// Update personal item index
		PersonalItem personalItem = userPublishingFileSystemService.getPersonalItem(item);

		// Check if personal item exist for this generic item - if not it means that user is editing the institutional item
		// in which case we don't have to update personal item index
		if (personalItem != null) {
			userWorkspaceIndexProcessingRecordService.save(personalItem.getOwner().getId(), personalItem, 
	    			indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		}
		
		institutionalItemService.markAllInstitutionalItemsForIndexing(genericItemId, indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		institutionalItemVersionService.setAllVersionsAsUpdated(user, genericItemId, "Item Links Modified");

		return SUCCESS;
	}
	
	/**
	 * Update a link with the given information.
	 * 
	 * @return success if the link is updated.
	 * 
	 */
	public String updateLink() throws NoIndexFoundException
	{
        IrUser user = userService.getUser(userId, false);
		
		// make sure the user is the owner.
		if( !item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE) && 
				!institutionalCollectionPermissionHelper.isInstitutionalCollectionAdmin(user, genericItemId))
		{
			return "accessDenied";
		}

		ItemLink  link = item.getItemLink(updateLinkId);
		link.setName(linkName);
		link.setDescription(linkDescription);
		link.setUrlValue(linkUrl);
		itemService.makePersistent(item);


		// Update personal item index
		PersonalItem personalItem = userPublishingFileSystemService.getPersonalItem(item);

		// Check if personal item exist for this generic item - if not it means that user is editing the institutional item
		// in which case we don't have to update personal item index
		if (personalItem != null) {
			userWorkspaceIndexProcessingRecordService.save(personalItem.getOwner().getId(), personalItem, 
	    			indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		}

		institutionalItemService.markAllInstitutionalItemsForIndexing(genericItemId, indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		institutionalItemVersionService.setAllVersionsAsUpdated(user, genericItemId, "Item Links Modified");

		return SUCCESS;
		
	}

	public Long getUpdateLinkId() {
		return updateLinkId;
	}

	public void setUpdateLinkId(Long updateLinkId) {
		this.updateLinkId = updateLinkId;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkDescription() {
		return linkDescription;
	}

	public void setLinkDescription(String linkDescription) {
		this.linkDescription = linkDescription;
	}

	public GenericItem getItem() {
		return item;
	}

	public void setItem(GenericItem item) {
		this.item = item;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public Long getGenericItemId() {
		return genericItemId;
	}

	public void setGenericItemId(Long genericItemId) {
		this.genericItemId = genericItemId;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public IndexProcessingTypeService getIndexProcessingTypeService() {
		return indexProcessingTypeService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

	public UserWorkspaceIndexProcessingRecordService getUserWorkspaceIndexProcessingRecordService() {
		return userWorkspaceIndexProcessingRecordService;
	}

	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}
	
	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Get institutional item id.
	 * 
	 * @return - the institutional item id.
	 */
	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	/**
	 * Set the institutional item id.
	 * 
	 * @param institutionalItemId
	 */
	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}
	


	public void setInstitutionalCollectionPermissionHelper(
			InstitutionalCollectionPermissionHelper institutionalCollectionPermissionHelper) {
		this.institutionalCollectionPermissionHelper = institutionalCollectionPermissionHelper;
	}

}
