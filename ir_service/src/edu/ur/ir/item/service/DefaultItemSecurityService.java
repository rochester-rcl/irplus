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


package edu.ur.ir.item.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemFileSecurityService;
import edu.ur.ir.item.ItemPermission;
import edu.ur.ir.item.ItemSecurityService;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;

/**
 * Default service for dealing with item permissions
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultItemSecurityService implements ItemSecurityService {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -4761222312161134579L;

	/** Security service */
	private SecurityService securityService;
	
	private ItemFileSecurityService itemFileSecurityService;
	
	/**  Logger for view personal collections action */
	private static final Logger log = Logger.getLogger(DefaultItemSecurityService.class);
	

	/**
	 * Assign permission to item
	 * 
	 * @see edu.ur.ir.item.ItemSecurityService#assignItemPermissions(ItemPermission[], IrUserGroup, GenericItem)
	 */
	public void assignItemPermissions(ItemPermission[] permissions, IrUserGroup userGroup, GenericItem item) {

		IrAcl itemAcl = getAcl(item);
		 
		if (itemAcl == null) {
			itemAcl = createAcl(item);
		}

		IrUserGroupAccessControlEntry entry = itemAcl.getGroupAccessControlEntry(userGroup);
		
		if( entry == null )
		{
			entry = itemAcl.createGroupAccessControlEntry(userGroup);
		}

		for (ItemPermission permission:permissions) {
			entry.addPermission(securityService.getClassTypePermission("edu.ur.ir.item.GenericItem", permission.getPermission()));
		}
		
		securityService.save(itemAcl);
	}
	

	/**
	 * Assign single permission to item
	 * 
	 * @see edu.ur.ir.item.ItemSecurityService#assignItemPermission(ItemPermission, IrUserGroup, GenericItem)
	 */
	public void assignItemPermission(ItemPermission permission, IrUserGroup userGroup, GenericItem item) {

		IrAcl itemAcl = getAcl(item);
		 
		if (itemAcl == null) {
			itemAcl = createAcl(item);
		}

		IrUserGroupAccessControlEntry entry = itemAcl.getGroupAccessControlEntry(userGroup);
		
		if( entry == null )
		{
			entry = itemAcl.createGroupAccessControlEntry(userGroup);
		}

		entry.addPermission(securityService.getClassTypePermission("edu.ur.ir.item.GenericItem", permission.getPermission()));
		
		securityService.save(itemAcl);
	}


	/**
	 * Assign user groups to Item and Item files
	 * 
	 * @see 
	 */
	public void assignGroupsToItem(GenericItem item, InstitutionalCollection collection) {
		log.debug("assigning groups to item");
		
		IrAcl collectionAcl = securityService.getAcl(collection);
		
		// users who can view the user groups
		List<IrUserGroup> viewUserGroups = collectionAcl.getIrUserGroupsWithPermission(securityService.getClassTypePermission("edu.ur.ir.institution.InstitutionalCollection", InstitutionalCollectionSecurityService.VIEW_PERMISSION.getPermission())); 
		
		
		// Access control list for the item
		IrAcl itemAcl = securityService.getAcl(item);
		 
		// if one does not exist create one
		if (itemAcl == null) {
			itemAcl = createAcl(item);
		}

		// user group control entry
		IrUserGroupAccessControlEntry entry = null;
	
		
		// Add view user groups
		for (IrUserGroup userGroup : viewUserGroups) {
			log.debug("adding view to user group " + userGroup);
			entry = itemAcl.getGroupAccessControlEntry(userGroup);
			
			if( entry == null )
			{
				entry = itemAcl.createGroupAccessControlEntry(userGroup);
			}

		    entry.addPermission(securityService.getClassTypePermission("edu.ur.ir.item.GenericItem", ItemSecurityService.ITEM_METADATA_READ_PERMISSION.getPermission()));	
		    

			for(ItemFile file : item.getItemFiles()) {
				itemFileSecurityService.assignItemFilePermission(ItemFileSecurityService.ITEM_FILE_READ_PERMISSION, userGroup, file);
			}
		}
		

		// users who have admin privileges
		List<IrUserGroup> adminUserGroups = collectionAcl.getIrUserGroupsWithPermission(securityService.getClassTypePermission("edu.ur.ir.institution.InstitutionalCollection", InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION.getPermission()));

		// Add admin user groups
		for (IrUserGroup userGroup : adminUserGroups) {
			log.debug("adding admin to user group " + userGroup);
			entry = itemAcl.getGroupAccessControlEntry(userGroup);
			
			if( entry == null ) {
				entry = itemAcl.createGroupAccessControlEntry(userGroup);
				
			}

			for(IrClassTypePermission permission: securityService.getClassTypePermissions("edu.ur.ir.item.GenericItem")) {
				entry.addPermission(permission);
			}

			for(ItemFile file : item.getItemFiles()) {
				itemFileSecurityService.assignItemFilePermission(ItemFileSecurityService.ITEM_FILE_READ_PERMISSION, userGroup, file);
			}
		}
		
		securityService.save(itemAcl);

	}

	/**
	 * Get the acl for the given item.
	 * 
	 * @see edu.ur.ir.item.ItemSecurityService#getAcl(edu.ur.ir.item.GenericItem)
	 */

	public IrAcl getAcl(GenericItem item) {
		return securityService.getAcl(item);
	}

	/**
	 * 
	 * @see edu.ur.ir.item.ItemSecurityService#createAcl(GenericItem)
	 */
	public IrAcl createAcl(GenericItem item) {
		return securityService.createAclForObject(item);
	}
	
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	/**
	 * Get the list of permissions for a item.
	 * 
	 * @see edu.ur.ir.item.ItemSecurityService#getItemPermissions()
	 */
	public List<IrClassTypePermission> getItemPermissions()
	{
		List<IrClassTypePermission> permissions = new LinkedList<IrClassTypePermission>();
		
		permissions.add(securityService.getClassTypePermission(GenericItem.class.getName(), 
				ItemSecurityService.ITEM_FILE_EDIT_PERMISSION.getPermission()));

		permissions.add(securityService.getClassTypePermission(GenericItem.class.getName(), 
				ItemSecurityService.ITEM_METADATA_EDIT_PERMISSION.getPermission()));

		permissions.add(securityService.getClassTypePermission(GenericItem.class.getName(), 
				ItemSecurityService.ITEM_METADATA_READ_PERMISSION.getPermission()));
		return permissions;
	}

	/**
	 * Remove the group from the item acl.
	 * 
	 * @see edu.ur.ir.item.ItemSecurityService#removeGroupFromItemAcl(edu.ur.ir.item.GenericItem, edu.ur.ir.user.IrUserGroup)
	 */
	public IrAcl removeGroupFromItemAcl(
			GenericItem item, IrUserGroup userGroup) {
		IrAcl acl = getAcl(item);
		if( (acl != null) && (userGroup != null) )
		{
			
			if( acl.removeAccessControlEntry(userGroup) )
			{
				securityService.save(acl);
			}
		}
		return acl;
	}
	
	/**
	 * Check if user has given permission on the item
	 * 
	 * @see edu.ur.ir.item.ItemSecurityService#hasPermission(GenericItem, IrUser, ItemPermission)
	 */
	public boolean hasPermission(GenericItem item, IrUser user, ItemPermission permission) {
		return securityService.hasPermission(item, user, permission.getPermission());
	}



	public void setItemFileSecurityService(
			ItemFileSecurityService itemFileSecurityService) {
		this.itemFileSecurityService = itemFileSecurityService;
	}
	
	/**
	 * Delete item acl
	 * 
	 * @see edu.ur.ir.item.ItemSecurityService#deleteItemAcl(GenericItem)
	 */
	public void deleteItemAcl(GenericItem item) {
		
		// Delete Item Acl
		IrAcl itemAcl = getAcl(item);
		if (itemAcl != null) {
			securityService.deleteAcl(itemAcl);
		}

		for (ItemFile itemFile : item.getItemFiles()) {
			
			// Delete Item file Acl
			IrAcl fileAcl = securityService.getAcl(itemFile);
			if (fileAcl != null) {
				securityService.deleteAcl(fileAcl);
			}
		}
	}
}
