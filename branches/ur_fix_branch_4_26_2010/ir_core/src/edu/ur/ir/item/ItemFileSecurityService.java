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

import java.util.List;

import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;

/**
 * Service for dealing with item file related security issues.  
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ItemFileSecurityService {
	
	/** permission to read a file */
	public static final ItemFilePermission ITEM_FILE_READ_PERMISSION =  new ItemFilePermission("ITEM_FILE_READ");
	
	/**
	 * Get the acl for the given item file.
	 * 
	 * @param itemFile Item to get the acl
	 * @return ACL for item File
	 */
	public IrAcl getAcl(ItemFile itemFile) ;

	/**
	 * Create item file acl
	 * 
	 * @param itemFile Item file to create the acl
	 * @return ACL created
	 */
	public IrAcl createAcl(ItemFile itemFile) ;
	
	/**
	 * Get the list of permissions for a item file.
	 * 
	 * @return List of item file permissions
	 */
	public List<IrClassTypePermission> getItemFilePermissions();
	
	/**
	 * Check if user has given permission on the item file
	 * 
	 * @param itemFile item File to see if the user has the permission
	 * @param user  user to check
	 * @param permission  permission to check
	 * 
	 * @return number of times the permission was found for the user a count greater than one means the user has
	 * the specified permission.
	 */
	public long hasPermission(ItemFile itemFile, IrUser user, ItemFilePermission permission);

	
	/**
	 * Assigns permissions for item file
	 * 
	 * @param permissions permissions to assign
	 * @param userGroup usergroup to assign the permissions to 
	 * @param itemFile Item to assign the user group permission
	 */
	public void assignItemFilePermission(ItemFilePermission permission, IrUserGroup userGroup, ItemFile itemFile);

	/**
	 * Remove the group from the item file acl.
	 * 
	 * @param itemFile Item file from which user group has to be removed
	 * @param userGroup user group that has to be removed
	 * 
	 */
	public void removeGroupFromItemFileAcl(ItemFile itemFile, IrUserGroup userGroup) ;
}
