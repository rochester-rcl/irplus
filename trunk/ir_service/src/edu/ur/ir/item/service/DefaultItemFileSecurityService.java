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

import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemFilePermission;
import edu.ur.ir.item.ItemFileSecurityService;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;

/**
 * Default service for dealing with item file permissions
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultItemFileSecurityService implements ItemFileSecurityService {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 7481910383273248125L;
	
	/** Security service */
	private SecurityService securityService;
	
	/**
	 * Get the acl for the given item file.
	 * 
	 * @see edu.ur.ir.item.ItemFileSecurityService#getAcl(edu.ur.ir.item.ItemFile)
	 */
	public IrAcl getAcl(ItemFile itemFile) {
		return securityService.getAcl(itemFile);
	}

	/**
	 * Create Item file ACL
	 * 
	 * @see edu.ur.ir.item.ItemFileSecurityService#createAcl(ItemFile)
	 */
	public IrAcl createAcl(ItemFile itemFile) {
		return securityService.createAclForObject(itemFile);
	}
	
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	/**
	 * Get the list of permissions for a item file.
	 * 
	 * @see edu.ur.ir.item.ItemFileSecurityService#getItemFilePermissions()
	 */
	public List<IrClassTypePermission> getItemFilePermissions()
	{
		List<IrClassTypePermission> permissions = new LinkedList<IrClassTypePermission>();
		
		permissions.add(securityService.getClassTypePermission(ItemFile.class.getName(), 
				ItemFileSecurityService.ITEM_FILE_READ_PERMISSION.getPermission()));
		return permissions;
	}

	/**
	 * Remove the group from the item file acl.
	 * 
	 * @see edu.ur.ir.item.ItemFileSecurityService#removeGroupFromItemFileAcl(edu.ur.ir.item.ItemFile, edu.ur.ir.user.IrUserGroup)
	 */
	public void removeGroupFromItemFileAcl(
			ItemFile itemFile, IrUserGroup userGroup) {
		IrAcl acl = getAcl(itemFile);
		if( (acl != null) && (userGroup != null) )
		{
			if( acl.removeAccessControlEntry(userGroup) )
			{
				securityService.save(acl);
			}

		}

	}
	
	/**
	 * Check if user has given permission on the item file
	 * 
	 * @see edu.ur.ir.item.ItemFileSecurityService#hasPermission(ItemFile, IrUser, edu.ur.ir.item.ItemFilePermission)
	 */
	public boolean hasPermission(ItemFile itemFile, IrUser user, ItemFilePermission permission) {
		return securityService.hasPermission(itemFile, user, permission.getPermission());
	}
	
	/**
	 * Assign permission to item file
	 * 
	 * @see edu.ur.ir.item.ItemFileSecurityService#assignItemFilePermissions(String[], IrUserGroup, ItemFile)
	 */
	public void assignItemFilePermission(ItemFilePermission permission, IrUserGroup userGroup, ItemFile itemFile) {
		IrAcl itemFileAcl = getAcl(itemFile);
		 
		if (itemFileAcl == null) {
			itemFileAcl = createAcl(itemFile);
		}

		IrUserGroupAccessControlEntry entry = itemFileAcl.getGroupAccessControlEntry(userGroup);
		
		if( entry == null )
		{
			entry = itemFileAcl.createGroupAccessControlEntry(userGroup);
		}

		entry.addPermission(securityService.getClassTypePermission("edu.ur.ir.item.ItemFile", permission.getPermission()));
		
		securityService.save(itemFileAcl);
	}
}
