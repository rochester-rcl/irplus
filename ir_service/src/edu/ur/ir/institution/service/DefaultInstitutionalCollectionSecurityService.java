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


package edu.ur.ir.institution.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionPermission;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.security.Sid;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;

/**
 * Creates access controls for institutional collection user groups
 * and repository related items.  This is to be used in conjunction with
 * the Security service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalCollectionSecurityService implements InstitutionalCollectionSecurityService{
	
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInstitutionalCollectionSecurityService.class);
	
	/** Deals with institutional collection information  */
	private InstitutionalCollectionService institutionalCollectionService; 
	
	/** Access control list service for dealing with permissions */
	private SecurityService securityService;
	
	/**
	 * Gives permissions to all parent collections.  This makes sure administrators
	 * in parent collections can manage children collections.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSecurityService#givePermissionsToParentCollections(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public void givePermissionsToParentCollections(InstitutionalCollection child) {
		if(log.isDebugEnabled())
		{
			log.debug("givePermissionsToParentCollections called");
		}
		List<InstitutionalCollection> parents = institutionalCollectionService.getPath(child);
		parents.remove(child);
		
		IrClassTypePermission viewPermission = 
			securityService.getPermissionForClass(child, VIEW_PERMISSION.getPermission());

		IrClassTypePermission adminPermission = 
			securityService.getPermissionForClass(child, ADMINISTRATION_PERMISSION.getPermission());

		IrClassTypePermission directSubmitPermission = 
			securityService.getPermissionForClass(child, DIRECT_SUBMIT_PERMISSION.getPermission());
		
		IrClassTypePermission reviewerPermission = 
			securityService.getPermissionForClass(child, REVIEWER_PERMISSION.getPermission());
		
		LinkedList<IrClassTypePermission> permissions = new LinkedList<IrClassTypePermission>();
		permissions.add(viewPermission);
		permissions.add(adminPermission);
		permissions.add(directSubmitPermission);
		permissions.add(reviewerPermission);
		
		
		Set<Sid> sids = new HashSet<Sid>();
		
		// get all secure ids that have admin privileges on the child
		sids.addAll(securityService.getSidsWithPermissionForObject(child, 
				InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION.getPermission()));
		
		// get all secure ids that have administration privileges in parents
		for( InstitutionalCollection collection : parents)
		{
			sids.addAll(securityService.getSidsWithPermissionForObject(collection,
					InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION.getPermission()));
		}
		
		// get all children including sub children to update with the new permissions
		List<InstitutionalCollection> collectionsToUpdate = institutionalCollectionService.getAllChildrenForCollection(child);
		collectionsToUpdate.add(child);
		
		
		// for each sid that is a group
		// give them permissions on the sub groups
		for(Sid sid: sids)
		{
			if( sid.getSidType().equals(IrUserGroup.GROUP_SID_TYPE))
			{
				for(InstitutionalCollection collection : collectionsToUpdate)
				{
					securityService.createPermissions(collection, (IrUserGroup)sid, 
							permissions);
				}
			}
			
		}
		
	}
	
	/**
	 * Delete the specified acl.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSecurityService#deleteAcl(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public void deleteAcl(InstitutionalCollection institutionalCollection) {
		 IrAcl acl = securityService.getAcl(institutionalCollection);
		 if( acl != null )
		 {
		    securityService.deleteAcl(acl);
		 }
	}
	
	/**
	 * Get the sids who have the specified permission on the given collection.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSecurityService#getSidsWithPermission(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.institution.InstitutionalCollectionPermission)
	 */
	public Set<Sid> getSidsWithPermission(InstitutionalCollection c,
			InstitutionalCollectionPermission collectionPermission) {
		return securityService.getSidsWithPermissionForObject(c, collectionPermission.getPermission());
		
	}
	
	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}


	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	

	
	/**
	 * Deetermins if the permission is granted for the specified user on the given collection.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSecurityService#isGranted(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.user.IrUser, edu.ur.ir.institution.InstitutionalCollectionPermission)
	 */
	public boolean isGranted(InstitutionalCollection collection, IrUser user,
			InstitutionalCollectionPermission collectionPermission) {
		IrAcl acl = securityService.getAcl(collection);
		if (acl != null)
		{
			if(acl.isGranted(collectionPermission.getPermission(), user, false) )
			{
		         return true;
			}
		}
		return false;
	}

	
	/**
	 * Get the acl for the given institutional colleciton.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSecurityService#getAcl(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public IrAcl getAcl(InstitutionalCollection collection) {
		return securityService.getAcl(collection);
	}

	
	/**
	 * Create an access control list for the specified collection
	 * @see edu.ur.ir.institution.InstitutionalCollectionSecurityService#createAcl(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public IrAcl createAcl(InstitutionalCollection collection) {
		return securityService.createAclForObject(collection);
	}

	
	/**
	 * Remove the group from the collection acl.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSecurityService#removeGroupFromCollectionAcl(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.user.IrUserGroup)
	 */
	public IrAcl removeGroupFromCollectionAcl(
			InstitutionalCollection collection, IrUserGroup userGroup) {
		IrAcl acl = securityService.getAcl(collection);
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
	 * Get the list of permissions for a collection.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSecurityService#getCollectionPermissions()
	 */
	public List<IrClassTypePermission> getCollectionPermissions()
	{
		List<IrClassTypePermission> permissions = new LinkedList<IrClassTypePermission>();
		permissions.add(securityService.getClassTypePermission(InstitutionalCollection.class.getName(), 
				InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION.getPermission()));

		permissions.add(securityService.getClassTypePermission(InstitutionalCollection.class.getName(), 
				InstitutionalCollectionSecurityService.REVIEWER_PERMISSION.getPermission()));

		permissions.add(securityService.getClassTypePermission(InstitutionalCollection.class.getName(), 
				InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION.getPermission()));
		
		permissions.add(securityService.getClassTypePermission(InstitutionalCollection.class.getName(), 
				InstitutionalCollectionSecurityService.REVIEW_SUBMIT_PERMISSION.getPermission()));

		permissions.add(securityService.getClassTypePermission(InstitutionalCollection.class.getName(), 
				InstitutionalCollectionSecurityService.VIEW_PERMISSION.getPermission()));
		return permissions;
	}

	
	/**
	 * Return the count of the number of times the user has the collection permission.
	 * @see edu.ur.ir.institution.InstitutionalCollectionSecurityService#hasPermission(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.user.IrUser, edu.ur.ir.institution.InstitutionalCollectionPermission)
	 */
	public long hasPermission(InstitutionalCollection collection, IrUser user,
			InstitutionalCollectionPermission collectionPermission) {
		 return securityService.hasPermission( collection,
					user, collectionPermission.getPermission());
	}

	
	/**
	 * Add the permission to the collection.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSecurityService#givePermission(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.user.IrUserGroup, edu.ur.ir.institution.InstitutionalCollectionPermission)
	 */
	public IrAcl givePermission(InstitutionalCollection collection,
			IrUserGroup userGroup, InstitutionalCollectionPermission permission) {
		IrAcl acl = securityService.getAcl(collection);
		
		if(acl == null)
		{
			acl = securityService.createAclForObject(collection);
		}
		IrUserGroupAccessControlEntry entry = acl.getGroupAccessControlEntry(userGroup);
		
		// get the class type permission for the collection permission
		IrClassTypePermission classTypePermission = securityService.getClassTypePermission(InstitutionalCollection.class.getName(), 
				permission.getPermission());
		
		if( entry != null )
		{
		   if( !entry.getPermissions().contains(classTypePermission))
		   {
			   entry.addPermission(classTypePermission);
		   }
		}
		else
		{
			entry = acl.createGroupAccessControlEntry(userGroup);
			entry.addPermission(classTypePermission);
		}
		
		securityService.save(acl);
		return acl;
		
	}

	




	


	
}
