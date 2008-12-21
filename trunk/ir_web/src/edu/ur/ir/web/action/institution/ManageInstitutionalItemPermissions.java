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

import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemSecurityService;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;

/**
 * Manage an institutional item permissions.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManageInstitutionalItemPermissions extends ActionSupport {

	/**  Logger. */
	private static final Logger log = Logger.getLogger(ManageInstitutionalItemPermissions.class);

	/**  Generated version id */
	private static final long serialVersionUID = -3299577209727988331L;

	/** Repository service for dealing with institutional repository information */
	RepositoryService repositoryService;
	
	/** Institutional item */
	private GenericItem item;
	
	/** Id of the Institutional item */
	private Long institutionalItemId;
	
	/** Id of the item */
	private Long itemId;
    
 	/** Institutional Item service */
	private InstitutionalItemService institutionalItemService;
	
	/** Access control entries */
	private Set<IrUserGroupAccessControlEntry> entries = new HashSet<IrUserGroupAccessControlEntry>();

	/** User groups that can be added to a institutional collection */
	private List<IrUserGroup> userGroups = new LinkedList<IrUserGroup>();
	
	/** Service to get user groups */
	private UserGroupService userGroupService;
	
	/** permissions that can be given to a collection */
	private List<IrClassTypePermission> permissions = new LinkedList<IrClassTypePermission>();
	
	/** Indicates whether to show Item / Item file tab */
	private boolean showItemFileTab = false;
	
	/** Item sercurity service */
	private ItemSecurityService itemSecurityService;
	

	/**
	 * View the institutional collection.
	 * 
	 * @return
	 */
	public String view()
	{
		log.debug("View Permissions for item");
		InstitutionalItem institutionalItem = 
			institutionalItemService.getInstitutionalItem(institutionalItemId, false);
		
		item = institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getItem();
		
		IrAcl acl = itemSecurityService.getAcl(item);
		if( acl != null )
		{
		    entries = acl.getGroupEntries();
		}
		userGroups = userGroupService.getAllNameOrder();
		this.loadPermissions();
		return "view";
	}

	/**
	 * Load the permissions allowed for an institutional collection
	 */
	private void loadPermissions()
	{
		permissions = itemSecurityService.getItemPermissions();

	}


	/**
	 * Get the institutional item.
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Item id to load
	 * 
	 * @return
	 */
	public Long getItemId() {
		return itemId;
	}

	/**
	 * Set the item id.
	 * 
	 * @param itemId
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}


	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public Set<IrUserGroupAccessControlEntry> getEntries() {
		return entries;
	}

	public void setEntries(Set<IrUserGroupAccessControlEntry> entries) {
		this.entries = entries;
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

	public List<IrClassTypePermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<IrClassTypePermission> permissions) {
		this.permissions = permissions;
	}

	public boolean isShowItemFileTab() {
		return showItemFileTab;
	}

	public void setShowItemFileTab(boolean showItemFileTab) {
		this.showItemFileTab = showItemFileTab;
	}

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public void setItemSecurityService(ItemSecurityService itemSecurityService) {
		this.itemSecurityService = itemSecurityService;
	}

}
