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

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;

/**
 * Manage group permissions on a collection
 * 
 * @author Nathan Sarr
 *
 */
public class EditGroupPermissionsOnCollection extends ActionSupport
{
	/** eclipse generated id */
	private static final long serialVersionUID = 172932284797983273L;

	/** id of the collection */
	private Long collectionId;
	
	/** id of the group  */
	private Long groupId;
	
	/** Service to get user groups */
	private UserGroupService userGroupService;
	
	/** security service */
	private SecurityService securityService;

	/** Access control entries */
	private Set<IrUserGroupAccessControlEntry> entries = new HashSet<IrUserGroupAccessControlEntry>();
    
	/** User groups that can be added to a institutional collection */
	private List<IrUserGroup> userGroups = new LinkedList<IrUserGroup>();
	
	/** Repository service for dealing with institutional repository information */
	InstitutionalCollectionSecurityService institutionalCollectionSecurityService;
	
	/** Institutional collection */
	private InstitutionalCollection collection;
	
	/** User group   */
	private IrUserGroup userGroup;
	
 	/** Institutional Collection service */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** set of ids for the permissions */
	private Long[] permissionIds = new Long[]{};
	
	/**  Logger. */
	private static final Logger log = Logger.getLogger(EditGroupPermissionsOnCollection.class);

	/** permissions that can be given to a collection */
	private List<IrClassTypePermission> permissions = new LinkedList<IrClassTypePermission>();

	/** access control list for the collection */
	private IrAcl acl;


	/**
	 * Loads the groups and collection.
	 * 
	 * @return
	 */
	public String addGroupsToCollection()
	{
		collection = 
			institutionalCollectionService.getCollection(collectionId, false);
		userGroups = userGroupService.getAllNameOrder();
		acl = institutionalCollectionSecurityService.getAcl(collection);
		
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
		collection = 
			institutionalCollectionService.getCollection(collectionId, false);
		userGroup = userGroupService.get(groupId, false);
		acl = institutionalCollectionSecurityService.getAcl(collection);
		loadPermissions();
		return SUCCESS;
	}
	
	/**
	 * add the permissions to the user group
	 */
	public String addCollectionPermissionsToGroup()
	{
		log.debug("add permissions called");
	    collection = 
			institutionalCollectionService.getCollection(collectionId, false);
		acl = institutionalCollectionSecurityService.getAcl(collection);
		
		if(acl == null)
		{
			acl = institutionalCollectionSecurityService.createAcl(collection);
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
	 * Update permissions on a group that already has permissions on a 
	 * collection.
	 * 
	 * @return
	 */
	public String updatePermissions()
	{
		log.debug("update permissions called");
		collection = 
			institutionalCollectionService.getCollection(collectionId, false);
		acl = institutionalCollectionSecurityService.getAcl(collection);
		
		userGroup = userGroupService.get(groupId, false);
		
		
		IrUserGroupAccessControlEntry entry = acl.getGroupAccessControlEntry(userGroup);
		Set<IrClassTypePermission> currentPermissions = new HashSet<IrClassTypePermission>();
		
		// remove all current permissions
		currentPermissions.addAll(entry.getIrClassTypePermissions());
		for( IrClassTypePermission permission : currentPermissions)
		{
			entry.removePermission(permission);
		}
		
		// add the current permissions assigned
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
	 * Remove all permissions for the user group on the institutional collection.
	 * 
	 * @return
	 */
	public String removeGroupFromCollection()
	{
		log.debug("add permissions called");
		InstitutionalCollection collection = 
			institutionalCollectionService.getCollection(collectionId, false);
		userGroup = userGroupService.get(groupId, false);
		if( userGroup != null)
		{
		    acl = institutionalCollectionSecurityService.removeGroupFromCollectionAcl(collection, userGroup);
		    entries = acl.getGroupEntries();
		}
	    return SUCCESS;
	}
	
	/**
	 * Load the permissions allowed for an institutional collection
	 */
	private void loadPermissions()
	{
		permissions = institutionalCollectionSecurityService.getCollectionPermissions();
	}

	public Long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
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

	public SecurityService getSecurityService() {
		return securityService;
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

	public InstitutionalCollectionSecurityService getInstitutionalCollectionSecurityService() {
		return institutionalCollectionSecurityService;
	}

	public void setInstitutionalCollectionSecurityService(
			InstitutionalCollectionSecurityService institutionalCollectionSecurityService) {
		this.institutionalCollectionSecurityService = institutionalCollectionSecurityService;
	}

	public InstitutionalCollection getCollection() {
		return collection;
	}

	public void setCollection(InstitutionalCollection collection) {
		this.collection = collection;
	}

	public IrUserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(IrUserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
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

	
    
}
