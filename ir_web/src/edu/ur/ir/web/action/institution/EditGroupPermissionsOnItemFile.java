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

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemFileSecurityService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;

/**
 * Manage group permissions on a item file
 * 
 * @author Sharmila Ranganathan
 *
 */
public class EditGroupPermissionsOnItemFile extends ActionSupport implements Preparable
{
	/** eclipse generated id */
	private static final long serialVersionUID = -3025646481281519678L;

	/** id of the item file */
	private Long itemFileId;
	
	/** id of the item */
	private Long itemId;
	
	/** Id of the Institutional item */
	private Long institutionalItemId;
	
	/** id of the group  */
	private Long groupId;
	
	/** Service to get user groups */
	private UserGroupService userGroupService;
	
	/** User groups that can be added to a institutional item */
	private List<IrUserGroup> userGroups = new LinkedList<IrUserGroup>();

	/** Item file */
	private ItemFile itemFile;
	
	/** User group   */
	private IrUserGroup userGroup;
	
	/** Item service */
	private ItemService itemService;
	
	/** set of ids for the permissions */
	private Long[] permissionIds = new Long[]{};
	
	/**  Logger. */
	private static final Logger log = LogManager.getLogger(EditGroupPermissionsOnItemFile.class);

	/** permissions that can be given to a itemFile */
	private List<IrClassTypePermission> permissions = new LinkedList<IrClassTypePermission>();

	/** access control list for the itemFile */
	private IrAcl acl;

	/** Institutional Item holding the files */
	private GenericItem item;
	
	/** Indicates whether to the  Item file is publicaly viewable or not */
	private boolean isPublic = false;
	
	/** Item file security service */
	private ItemFileSecurityService itemFileSecurityService;

	/**
	 * prepares the action
	 */
	public void prepare() {
		log.debug("Prepare for Item File permissions:: itemFileId = "+ itemFileId + "  item id=" + itemId);
		if (itemId != null) {
			item = itemService.getGenericItem(itemId, false);
			itemFile = item.getItemFile(itemFileId);
		} else if (itemFileId != null) {
			itemFile = itemService.getItemFile(itemFileId, false);
		}
	}
	
	/**
	 * Loads the groups and itemFile.
	 * 
	 * @return
	 */
	public String viewGroups()
	{
		log.debug("View user groups for Item File");
		userGroups = userGroupService.getAllNameOrder();
		acl = itemFileSecurityService.getAcl(itemFile);
		
		return SUCCESS;
	}
	
	/**
	 * add the permissions to the user group
	 */
	public String addGroupToItemFile()
	{
		log.debug("add permissions called");
		
		userGroup = userGroupService.get(groupId, false);
						
		itemFileSecurityService.assignItemFilePermission(ItemFileSecurityService.ITEM_FILE_READ_PERMISSION, userGroup, itemFile);
		
		userGroups = userGroupService.getAllNameOrder();
		
		acl = itemFileSecurityService.getAcl(itemFile);
		
		return SUCCESS;
		
	}
	
	/**
	 * Remove all permissions for the user group on the institutional item.
	 * 
	 * @return
	 */
	public String removeGroupFromItemFile()
	{
		log.debug("add permissions called");
		userGroup = userGroupService.get(groupId, false);

		itemFileSecurityService.removeGroupFromItemFileAcl(itemFile, userGroup);
		return SUCCESS;
	}

	/**
	 * Updates the view status for item file 
	 * 
	 * @return
	 */
	public String updateItemFilePublicView() {
		
		itemFile.setPublic(isPublic);
		itemService.saveItemFile(itemFile);

		return SUCCESS;
	}
	
	public Long getItemFileId() {
		return itemFileId;
	}

	public void setItemFileId(Long itemFileId) {
		this.itemFileId = itemFileId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public UserGroupService getUserGroupService() {
		return userGroupService;
	}

	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

	public List<IrUserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<IrUserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	public ItemFile getItemFile() {
		return itemFile;
	}

	public IrUserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(IrUserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public Long[] getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(Long[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public List<IrClassTypePermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<IrClassTypePermission> permissions) {
		this.permissions = permissions;
	}

	public IrAcl getAcl() {
		return acl;
	}

	public void setAcl(IrAcl acl) {
		this.acl = acl;
	}

	public GenericItem getItem() {
		return item;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(
			ItemService itemService) {
		this.itemService = itemService;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public void setItemFileSecurityService(
			ItemFileSecurityService itemFileSecurityService) {
		this.itemFileSecurityService = itemFileSecurityService;
	}

	
    
}
