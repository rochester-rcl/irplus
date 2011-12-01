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

package edu.ur.ir.item;

import java.io.Serializable;
import java.util.List;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;

/**
 * Service for dealing with item related security issues.  
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ItemSecurityService extends Serializable{
	
	/** permissions that can be granted on institutional collections */
	public static final ItemPermission ITEM_METADATA_READ_PERMISSION = new ItemPermission("ITEM_METADATA_READ");
	
	/** permission to edit item metadata */
	public static final ItemPermission ITEM_METADATA_EDIT_PERMISSION =  new ItemPermission("ITEM_METADATA_EDIT");
	
	/** permission to edit a file */
	public static final ItemPermission ITEM_FILE_EDIT_PERMISSION =  new ItemPermission("ITEM_FILE_EDIT");
	
	/**
	 * Assigns single permission for item
	 * 
	 * @param permissions permission to assign
	 * @param userGroup usergroup to assign the permissions to 
	 * @param item Item to assign the user group permission
	 */
	public void assignItemPermission(ItemPermission permission, IrUserGroup userGroup, GenericItem item);

	/**
	 * Assigns permissions for item
	 * 
	 * @param permissions permissions to assign
	 * @param userGroup usergroup to assign the permissions to 
	 * @param item Item to assign the user group permission
	 */
	public void assignItemPermissions(ItemPermission[] permissions, IrUserGroup userGroup, GenericItem item);

	/**
	 * Assign user groups to Item and Item files
	 * 
	 * @param institutionalItem
	 */
	public void assignGroupsToItem(GenericItem item, InstitutionalCollection collection) ;
	
	/**
	 * Get the acl for the given item.
	 * 
	 * @param item Item to get the acl
	 * @return ACL for item
	 */
	public IrAcl getAcl(GenericItem item) ;

	/**
	 * Create item acl
	 * 
	 * @param item Item to create the acl
	 * @return ACL created
	 */
	public IrAcl createAcl(GenericItem item) ;
	
	/**
	 * Get the list of permissions for a item.
	 * 
	 * @return List of item permissions
	 */
	public List<IrClassTypePermission> getItemPermissions();
	
	/**
	 * Check if user has given permission on the item
	 * 
	 * @param item item to see if the user has the permission
	 * @param user  user to check
	 * @param permission  permission to check
	 * 
	 * @return true if the user has permission on the given item
	 */
	public boolean hasPermission(GenericItem item, IrUser user, ItemPermission permission);

	/**
	 * Remove the group from the item acl.
	 * 
	 * @param item Item from which user group has to be removed
	 * @param userGroup user group to be removed
	 * 
	 * @return ACL after group is removed
	 */
	public IrAcl removeGroupFromItemAcl(GenericItem item, IrUserGroup userGroup);

	/**
	 * Delete item acl
	 * 
	 * @param item Item to delete ACL
	 */
	public void deleteItemAcl(GenericItem item);
}
