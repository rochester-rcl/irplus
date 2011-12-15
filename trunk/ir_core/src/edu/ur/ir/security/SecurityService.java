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

package edu.ur.ir.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;

/**
 * Obtains the <code>AclEntry</code> instances that apply to a particular
 * domain object instance.
 *
 * @author Nathan Sarr
 */
public interface SecurityService extends Serializable{
 
    /**
     * Obtains the ACLs that apply to the specified domain instance.
     *
     * @param domainInstance the instance for which ACL information is required (never <code>null</code>)
     *
     * @return the ACLs that apply, or <code>null</code> if no ACLs apply to the specified domain instance
     */
    public IrAcl getAcl(Object domainInstance);
    
    /**
     * Save the specified acl to the database.
     * 
     * @param acl
     */
    public void save(IrAcl acl);

    /**
     * Obtains the ACLs that apply to the specified domain instance, but only including those ACLs which have
     * been granted to the presented <code>Authentication</code> object
     *
     * @param domainInstance the instance for which ACL information is required (never <code>null</code>)
     * @param the sid for which ACL information should be filtered (never <code>null</code>)
     *
     * @return only those ACLs applying to the domain instance that have been granted to the principal (or
     *         <code>null</code>) if no such ACLs are found
     */
    public IrAcl getAcl(Object domainInstance, Sid sid);
    
    
    /**
     * Determine if the user has the specified permission.
     * 
     * @param domainInstance - domain instance to check
     * @param sid - secure id 
     * @param permission - permission to check for
     * 
     * @return true if the user has the specified permission for the given domain instance.
     */
    public boolean hasPermission(Object domainInstance, Sid sid, String permission);
    
    /**
     * Determine if the user has the specified permission.
     * 
     * @param domainInstance - domain instance to check
     * @param sid - secure id 
     * @param permission - permission to check for
     * 
     * @return the number of times the sid has the given permission - 0 indicates the sid does
     * not have the permission
     */
    public Long getPermissionCount(Object domainInstance, Sid sid, String permission);
    
	/**
	 * Creates an ACL object for the specified domain instance if
	 * one does not already exist
	 * 
	 * @param domainInstance - domain instance to create an acl for
	 * @return the created ACL object.
	 */
	public IrAcl createAclForObject(Object domainInstance);

    
	/**
	 * Create permissions for the object
	 * 
	 * @param secureObject object that has to be secured
	 * @param user accessing the object
	 * @param permissions type of permission
	 * 
     * @throws DuplicateAccessControlEntryException - if the user already exists 
     */
    public void createPermissions(Object secureObject, IrUser user, 
    		Collection<IrClassTypePermission> permissions);
    
	/**
	 * Create permissions for the object
	 * 
	 * @param secureObject object that has to be secured
	 * @param group to give permissions to
	 * @param permissions type of permissions to give
     */
    public void createPermissions(Object secureObject, IrUserGroup userGroup, 
    		Collection<IrClassTypePermission> permissions);
    
	/**
	 * Delete all permissions on an object for a user
	 * 
	 * @param domainObjectId id of the domain object for which the permission has to be deleted
	 * @param className class name of the domain object for which the permission has to be deleted
	 * @param user user for whom the permissions need to be removed 
	 */
    public void deletePermissions(Long domainObjectId, String className, IrUser user) ;
    
	/**
	 * Delete all permissions on an object for a user group
	 * 
	 * @param domainObjectId id of the domain object for which the permission has to be deleted
	 * @param className class name of the domain object for which the permission has to be deleted
	 * @param user user for whom the permissions need to be removed 
	 */
    public void deletePermissions(Long domainObjectId, String className, IrUserGroup user) ;
    
    /**
     * Get permissions for a class type
     * 
     * @param classType fully qualified class type name
     * @return List of permissions for class type
     */
    public List<IrClassTypePermission> getClassTypePermissions(String classType);

    /**
     * Get a permission for a class type
     * 
     * @param classType fully qualified class type name
     * @return List of permissions for class type
     */
    public IrClassTypePermission getClassTypePermission(String classType, String permissionName);

    
    /**
     * Assigns all the permissions that has to be given to the owner of the domain object
     * 
     * @param domainObject object to assign permission to
     * @param user user holding permission
     */
    public void assignOwnerPermissions(Object domainObject, IrUser user);

	/**
	 * Delete access control list for an object identity
	 * 
	 * @param domainObjectId Id of the domain object
	 * @param className Class name of the domain object
	 */
	public void deleteAcl(Long domainObjectId, String className);
	
	/**
	 * Delete access control list
	 * 
	 * @param acl - Access control list to delete.
	 */
	public void deleteAcl(IrAcl acl);

	
	/**
	 * Permissions for a user on a particular domain instance
	 * 
	 *  @param domainInstance object to get the permission for
	 *  @param sid User for whom the permissions has to be retrieved
	 *  
	 *  @return permissions for a user on a file
	 */
	public Set<IrClassTypePermission> getPermissions(Object domainInstance, Sid sid);


	/**
	 *  Get the specified permission for the specified class
	 * 
	 *  @param domainInstance to get the permission for
	 *  @param name - name of the permission
	 *  
	 *  @return permissions for a user on a file
	 */
	public IrClassTypePermission getPermissionForClass(Object domainInstance, String name);
	
	/**
	 *  Get the Access Control Lists (ACL's) that the specified sid belongs to.
	 * 
	 *  @param Sid( Secure id) to get the acl's for 
	 *  
	 *  @return permissions for a user on a file
	 */
	public List<IrAcl> getAcls(Sid sid);	
	
	/**
	 * Get a class type permission by id.
	 * 
	 * @param classTypePermissionId - id of the class type permission
	 * @param lock - upgrade the lock mode
	 * @return the class type permission.
	 */
	public IrClassTypePermission getIrClassTypePermissionById(Long classTypePermissionId,
			boolean lock);
	
	/**
	 * Returns all secure id's with the specified permission on the domain instance.  This
	 * should only return sids with explicit permissions set on them.  This will not
	 * return a user who has the permission by being within a group that is given the permission
	 * instead the group will be returned.
	 * 
	 * @param domainInstance - domain instance to check
	 * @param permission - permissions the sid must have
	 * 
	 * @return List of sids with the specified permissions.
	 */
	public Set<Sid> getSidsWithPermissionForObject(Object domainInstance, String permission);
	
	/**
	 * Returns all users with the specified permission on the domain instance.  This
	 * should only return users with explicit permissions set on them.  This will NOT
	 * return a user who has the permission by being within a group that is given the permission.
	 * 
	 * @param domainInstance - domain instance to check
	 * @param permission - permissions the sid must have
	 * 
	 * @return List of sids with the specified permissions.
	 */
	public Set<IrUser> getUsersWithPermissionForObject(Object domainInstance, String permission);
	
	/**
	 * Return all secure id's who have the identified permission and are within
	 * the specified set of sids.
	 * 
	 * @param objectId - object they must have permissions on
	 * @param className - object class
	 * @param permission - permission the sid must have
	 * @param specificSids - set of sids to check
	 * 
	 * @return all sids who meet the specified criteria
	 */
	public Set<Sid> getSidsWithPermissionForObject(Object domainInstance, String permission, List<Sid> specificSids);

	/**
	 * Create the permissions for user control entries.  This is a bulk operation.
	 * 
	 * @param entries - list of entries
	 * @param permissions - list of permissions to give to each entry
	 * 
	 * @return number of entries created
	 */
	public int createPermissionsForUserControlEntries(List<IrUserAccessControlEntry> entries,
			List<IrClassTypePermission> permissions);
	
	/**
	 * Create user control entries for the list of users for the
	 * specified acls.  This is a bulk operation
	 * 
	 * @param users - list of users to create the entries for
	 * @param acl - acl to add the entries to
	 * 
	 * @return number of entries created
	 */
	public int createUserControlEntriesForUsers(List<IrUser> users, List<IrAcl> acls );
	
	/**
	 * Get the list of users for the given access control list.
	 * 
	 * @param acl - acl to the the access control entries for
	 * @param users - list of users to get for the acl
	 * 
	 * @return - list of users found.
	 */
	public List<IrUserAccessControlEntry> getUserControlEntriesForUsers(IrAcl acl, List<IrUser> users);
}

