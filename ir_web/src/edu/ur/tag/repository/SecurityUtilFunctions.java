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


package edu.ur.tag.repository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.security.AccessControlEntry;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;

/**
 * Helper functions for security
 * 
 * @author Nathan Sarr
 *
 */
public class SecurityUtilFunctions {
	
	/** Security service  */
	private static SecurityService securityService = null;
	
	/**
	 * Determine if the access control entry has the permission with the specified name
	 */
	public static boolean entryHasPermission(AccessControlEntry accessControlEntry, String permission)
	{
		// a null access control entry cannot have any permissions
		if( accessControlEntry == null )
		{
			return false;
		}
	
		return accessControlEntry.getPermissionNames().contains(permission);
	}
	
	/**
	 * Returns true if the group has been added as an access control entry
	 * 
	 * @param acl - acl for the collection
	 * @param collection - the collection
	 * @param group - the group to check
	 * 
	 * @return - true if an acl is found
	 */
	public static boolean groupHasAceForCollection(IrAcl acl, 
			InstitutionalCollection collection, IrUserGroup group)
	{
		boolean hasPermissions = false;
		if( acl == null )
		{
			return false;
		}
		if( acl.getClassType().getName().equals(InstitutionalCollection.class.getName()))
		{
			if( acl.getObjectId().equals(collection.getId()))
			{
				if (acl.getGroupAccessControlEntry(group) != null)
				{
					hasPermissions = true;
				}
			}
		}
		
		return hasPermissions;
	}
	
	/**
	 * Returns true if the group has been added as an access control entry
	 * 
	 * @param acl - acl for the collection
	 * @param collection - the collection
	 * @param group - the group to check
	 * 
	 * @return - true if an acl is found
	 */
	public static boolean groupHasPermissionForCollection(IrAcl acl, 
			InstitutionalCollection collection, IrUserGroup group, 
			IrClassTypePermission permission)
	{
		boolean hasPermissions = false;
		if( acl == null )
		{
			return false;
		}
		if( acl.getClassType().getName().equals(InstitutionalCollection.class.getName()))
		{
			if( acl.getObjectId().equals(collection.getId()))
			{
				IrUserGroupAccessControlEntry entry = acl.getGroupAccessControlEntry(group);
				if ( entry != null)
				{
					if( entry.getIrClassTypePermissions().contains(permission))
					{
					    hasPermissions = true;
					}
				}
			}
		}
		
		return hasPermissions;
	}
	
	/**
	 * Returns true if the group has been added as an access control entry
	 * 
	 * @param acl - acl for the collection
	 * @param item - the item
	 * @param group - the group to check
	 * 
	 * @return - true if an acl is found
	 */
	public static boolean groupHasPermissionForItem(IrAcl acl, 
			GenericItem item, IrUserGroup group, 
			IrClassTypePermission permission)
	{
		boolean hasPermissions = false;
		if( acl == null )
		{
			return false;
		}
		if( acl.getClassType().getName().equals(GenericItem.class.getName()))
		{
			if( acl.getObjectId().equals(item.getId()))
			{
				IrUserGroupAccessControlEntry entry = acl.getGroupAccessControlEntry(group);
				if ( entry != null)
				{
					if( entry.getIrClassTypePermissions().contains(permission))
					{
					    hasPermissions = true;
					}
				}
			}
		}
		
		return hasPermissions;
	}

	/**
	 * Returns true if the group has been added as an access control entry
	 * 
	 * @param acl - acl for the item
	 * @param item - the item
	 * @param group - the group to check
	 * 
	 * @return - true if an acl is found
	 */
	public static boolean groupHasAceForItem(IrAcl acl, 
			GenericItem item, IrUserGroup group)
	{
		boolean hasPermissions = false;
		if( acl == null )
		{
			return false;
		}
		if( acl.getClassType().getName().equals(GenericItem.class.getName()))
		{
			if( acl.getObjectId().equals(item.getId()))
			{
				if (acl.getGroupAccessControlEntry(group) != null)
				{
					hasPermissions = true;
				}
			}
		}
		
		return hasPermissions;
	}

	/**
	 * Returns true if the group has been added as an access control entry
	 * 
	 * @param acl - acl for the item
	 * @param item - the item
	 * @param group - the group to check
	 * 
	 * @return - true if an acl is found
	 */
	public static boolean groupHasAceForItemFile(IrAcl acl, 
			ItemFile itemFile, IrUserGroup group)
	{
		boolean hasPermissions = false;
		if( acl == null )
		{
			return false;
		}
		if( acl.getClassType().getName().equals(ItemFile.class.getName()))
		{
			if( acl.getObjectId().equals(itemFile.getId()))
			{
				if (acl.getGroupAccessControlEntry(group) != null)
				{
					hasPermissions = true;
				}
			}
		}
		
		return hasPermissions;
	}

	/**
	 * Checks if the user has specified permission for given object
	 * 
	 * @param permissions
	 * @param domainObject
	 * @return
	 */
	public static boolean hasPermission(String permissions, Object domainObject) {

		boolean granted = false;
        
    	if ((null == permissions) || permissions.length() == 0) {
            granted = false;
        }

        if (domainObject == null) {
            // Of course they have access to a null object!
           granted = true;
        }
        else
        {
              IrUser user = null;
             
             final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     		
             if( auth != null) {
     			 if(auth.getPrincipal() instanceof UserDetails) 
     			 {
     				 user = (IrUser)auth.getPrincipal();
     			 }
             } 
             else 
             {
            	 return false;
             }
     		
             if( user != null )
             {
            	 if( user.hasRole(IrRole.ADMIN_ROLE))
            	 {
            		 granted = true;
            	 }
            	 else
            	 {
                     if( securityService.hasPermission(domainObject, user, permissions))
                     {
                	     granted = true;
                     }
            	 }
             }

		}
        return granted;
        
	}

	public  void setSecurityService(SecurityService securityService) {
		SecurityUtilFunctions.securityService = securityService;
	}

	
}
