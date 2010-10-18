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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemSecurityService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;

/**
 * Manage group permissions on a item
 * 
 * @author Sharmila Ranganathan
 *
 */
public class EditGroupPermissionsOnItem extends ActionSupport
{
	/** eclipse generated id */
	private static final long serialVersionUID = 398234971838172385L;

	/** id of the item */
	private Long itemId;

	/** Id of the Institutional item */
	private Long institutionalItemId;
	
	/** id of the group  */
	private Long groupId;
	
	/** Service to get user groups */
	private UserGroupService userGroupService;
	
	/** security service */
	private SecurityService securityService;
	
	/** Item security service*/
	private ItemSecurityService itemSecurityService;
	
	/** Access control entries */
	private Set<IrUserGroupAccessControlEntry> entries = new HashSet<IrUserGroupAccessControlEntry>();
    
	/** User groups that can be added to a institutional item */
	private List<IrUserGroup> userGroups = new LinkedList<IrUserGroup>();

	/** Institutional item */
	private GenericItem item;
	
	/** User group   */
	private IrUserGroup userGroup;
	
 	/** Item service */
	private ItemService itemService;
	
	/** set of ids for the permissions */
	private Long[] permissionIds = new Long[]{};
	
	/**  Logger. */
	private static final Logger log = Logger.getLogger(EditGroupPermissionsOnItem.class);

	/** permissions that can be given to a item */
	private List<IrClassTypePermission> permissions = new LinkedList<IrClassTypePermission>();

	/** access control list for the item */
	private IrAcl acl;
	
	/** Indicates whether to the  Item is publicly viewable or not */
	private boolean isPublic = false;

	/**
	 * Loads the groups and item.
	 * 
	 * @return
	 */
	public String addGroupsToItem()
	{
		item = 
			itemService.getGenericItem(itemId, false);
		userGroups = userGroupService.getAllNameOrder();
		acl = itemSecurityService.getAcl(item);
		
		loadPermissions();
		return SUCCESS;
	}
	
	/**
	 * Get the permissions for the specified group
	 * 
	 * @return
	 */
	public String getGroupPermissions()
	{
		item = 
			itemService.getGenericItem(itemId, false);
		userGroup = userGroupService.get(groupId, false);
		acl = itemSecurityService.getAcl(item);
		loadPermissions();
		return SUCCESS;
	}
	
	/**
	 * add the permissions to the user group
	 */
	public String addItemPermissionsToGroup()
	{
		log.debug("add permissions called");
		item = 
			itemService.getGenericItem(itemId, false);
		acl = itemSecurityService.getAcl(item);
		if(acl == null)
		{
			acl = itemSecurityService.createAcl(item);
		}
		
		userGroup = userGroupService.get(groupId, false);
		
		
		IrUserGroupAccessControlEntry entry = acl.getGroupAccessControlEntry(userGroup);
		
		if( entry != null )
		{
		    Set<IrClassTypePermission> currentPermissions = new HashSet<IrClassTypePermission>();
		    currentPermissions.addAll(entry.getIrClassTypePermissions());
		    for( IrClassTypePermission permission : currentPermissions)
		    {
			    entry.removePermission(permission);
		    }
		}
		else
		{
			entry = acl.createGroupAccessControlEntry(userGroup);
		}
		
		log.debug("permission ids size = " + permissionIds.length);
		for(Long id : permissionIds)
		{
			IrClassTypePermission permission = securityService.getIrClassTypePermissionById(id, false);
			log.debug("adding permission " + permission);
			entry.addPermission(permission);
		}

		securityService.save(acl);
		
		entries = acl.getGroupEntries();
		userGroups = userGroupService.getAllNameOrder();
		
		return SUCCESS;
		
	}
	
	/**
	 * Updates the view status for item file 
	 * 
	 * @return
	 */
	public String updateItemPublicView() {
		
		item = 
			itemService.getGenericItem(itemId, false);
		
		item.setPubliclyViewable(isPublic);
		itemService.makePersistent(item);

		return SUCCESS;
	}
	
	/**
	 * Update permissions on a group that already has permissions on a 
	 * item.
	 * 
	 * @return
	 */
	public String updatePermissions()
	{
		log.debug("update permissions called");
		item = 
			itemService.getGenericItem(itemId, false);
		acl = itemSecurityService.getAcl(item);
		
		userGroup = userGroupService.get(groupId, false);
		
		
		IrUserGroupAccessControlEntry entry = acl.getGroupAccessControlEntry(userGroup);
		
		Set<IrClassTypePermission> currentPermissions = new HashSet<IrClassTypePermission>();
		currentPermissions.addAll(entry.getIrClassTypePermissions());
		for( IrClassTypePermission permission : currentPermissions)
		{
			entry.removePermission(permission);
		}
		
		log.debug("permission ids size = " + permissionIds.length);
		for(Long id : permissionIds)
		{
			IrClassTypePermission permission = securityService.getIrClassTypePermissionById(id, false);
			log.debug("adding permission " + permission);
			entry.addPermission(permission);
		}

		securityService.save(acl);
		
		entries = acl.getGroupEntries();
		userGroups = userGroupService.getAllNameOrder();
		
		return SUCCESS;
	}
	
	/**
	 * Remove all permissions for the user group on the institutional item.
	 * 
	 * @return
	 */
	public String removeGroupFromItem()
	{
		log.debug("add permissions called");
		item = itemService.getGenericItem(itemId, false);
		userGroup = userGroupService.get(groupId, false);
		
		acl = itemSecurityService.removeGroupFromItemAcl(item, userGroup);

		entries = acl.getGroupEntries();
	    return SUCCESS;
	}
	
	/**
	 * Load the permissions allowed for an institutional item
	 */
	private void loadPermissions()
	{
		permissions = itemSecurityService.getItemPermissions();
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public Set<IrUserGroupAccessControlEntry> getEntries() {
		return entries;
	}

	public void setEntries(Set<IrUserGroupAccessControlEntry> entries) {
		this.entries = entries;
	}

	public List<IrUserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<IrUserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	public GenericItem getItem() {
		return item;
	}

	public void setItem(GenericItem item) {
		this.item = item;
	}

	public IrUserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(IrUserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(
			ItemService itemService) {
		this.itemService = itemService;
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

	public void setItemSecurityService(ItemSecurityService itemSecurityService) {
		this.itemSecurityService = itemSecurityService;
	}

	
    
}
