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


package edu.ur.ir.security.service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.cgLib.CgLibHelper;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrAclDAO;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypeDAO;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrClassTypePermissionDAO;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.security.IrUserAccessControlEntryDAO;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.IrUserGroupAccessControlEntryDAO;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.security.Sid;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;


/**
 * Base Service Class for accessing security information
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class DefaultSecurityService implements SecurityService {

	/** eclipse generated id */
	private static final long serialVersionUID = 8614907507229543784L;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultSecurityService.class);

	/* Data access for ClassType*/
	private IrClassTypeDAO irClassTypeDAO;

	/* Data access for  access control access */
	private IrAclDAO irAclDAO;
	
	/* Data access for ClassTypePermission */
	private IrClassTypePermissionDAO irClassTypePermissionDAO;
	
	/* Data access for UserAccessControlDAO */
	private IrUserAccessControlEntryDAO irUserAccessControlEntryDAO;
	
	/* Data access for UserAccessControlDAO */
	private IrUserGroupAccessControlEntryDAO irUserGroupAccessControlEntryDAO;

	
	/**
	 * Get the ACL for specified domain instance and Principal
	 * 
	 * @param domainInstance object that has to be secured
	 * @param sid Id for user
	 */
	public IrAcl getAcl(Object domainInstance, Sid sid) {
		return irAclDAO.getAcl(getObjectId(domainInstance), 
				CgLibHelper.cleanClassName(domainInstance.getClass().getName()), sid);
	}

	/**
	 * Get the ACL for specified domain instance 
	 * 
	 * @param domainInstance 
	 */
	public IrAcl getAcl(Object domainInstance) {
		if(log.isDebugEnabled())
		{
			log.debug("getting infor for domain instance id = " + 
					getObjectId(domainInstance) + " name = " + CgLibHelper.cleanClassName(domainInstance.getClass().getName()) );
		}
		return irAclDAO.getAcl(getObjectId(domainInstance), 
				CgLibHelper.cleanClassName(domainInstance.getClass().getName()));
	}	

	/**
	 * Get the id of the object.
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Long getObjectId(Object object) {
		Long objectId = null;
		
        try {
        	String classTypeName = object.getClass().getName();
        	Class theClass = Class.forName(CgLibHelper.cleanClassName(classTypeName));
        	
        	Method method = theClass.getMethod("getId", new Class[] {});
            objectId = (Long)method.invoke(object, new Object[] {});
        } catch (Exception e) {
            throw new IllegalStateException("Could not extract identity from object " + object, e);
        }
        
        return objectId;
	}
	
	/**
	 * Get Class type
	 * 
	 * @param domainInstance domain instance
	 * 
	 * @return class type of the domain instance
	 */
	private IrClassType getClassType(Object domainInstance) {
		IrClassType classType = 
			irClassTypeDAO.findByUniqueName(CgLibHelper.cleanClassName(domainInstance.getClass().getName()));
		log.debug("in default security service classType = " + classType );
		return classType;
	}

	/**
	 * Create permissions for the object
	 * @throws DuplicateAccessControlEntryException 
	 * 
	 * @see edu.ur.ir.security.IrAclManager#createPermissions(Object secureObject, IrUser user, List<String> permissions) 
	 */
	public void createPermissions(Object domainInstance, IrUser user, 
			Collection<IrClassTypePermission> permissions) {
		
		log.debug("Create Permission for " + user.getUsername() 
				+ ". Permissions = " + permissions);
		IrAcl irAcl = getAcl(domainInstance);
		
		if (irAcl == null) {
			irAcl = new IrAcl(domainInstance, getClassType(domainInstance));
		}
		
		IrUserAccessControlEntry userAccessControlEntry = 
			irAcl.createUserAccessControlEntry(user);
		
		for (IrClassTypePermission classTypePermission: permissions) {
			// add permission
			userAccessControlEntry.addPermission(classTypePermission);
		}
		irAclDAO.makePersistent(irAcl);
	}
	
	/**
	 * Creates an ACL object for the specified domain instance.
	 * 
	 * @param domainInstance - domain instance to create an acl for
	 * @return the created ACL object.
	 */
	public IrAcl createAclForObject(Object domainInstance)
	{		
		log.debug("Create acl for object " + domainInstance ); 
			
	    IrAcl irAcl = getAcl(domainInstance);
	
	    if (irAcl == null) {
		    irAcl = new IrAcl(domainInstance, getClassType(domainInstance));
		    irAclDAO.makePersistent(irAcl);
	    }

		return irAcl;
	}
	
	
	
	/**
	 * Create permissions for the object assigning them to the given user group.
	 * 
	 * @see edu.ur.ir.security.IrAclManager#createPermissions(Object secureObject, IrUser userGroup, List<String> permissions) 
	 */
	public void createPermissions(Object domainInstance, IrUserGroup userGroup, 
			Collection<IrClassTypePermission> permissions){
		
		log.debug("Create Permission for " + userGroup 
				+ ". Permissions = " + permissions);
		IrAcl irAcl = getAcl(domainInstance);
		
		if (irAcl == null) {
			irAcl = new IrAcl(domainInstance, getClassType(domainInstance));
		}
		
		IrUserGroupAccessControlEntry userGroupAccessControlEntry = 
			irAcl.createGroupAccessControlEntry(userGroup);
		
		for (IrClassTypePermission permission: permissions) {
			// add permission
			userGroupAccessControlEntry.addPermission(permission);
		}
		irAclDAO.makePersistent(irAcl);
		
	}
	
	/**
	 * Permissions for a sid on a particular domain instance
	 * 
	 *  @see edu.ur.ir.security.SecurityService#getPermissions(Object, Sid)
	 */
	public Set<IrClassTypePermission> getPermissions(Object domainInstance, Sid sid) {
		IrAcl acl = getAcl(domainInstance);
		return acl.getPermissions(sid);
	}

	/**
	 * Assign all permissions for the owner of the object
	 * @throws DuplicateAccessControlEntryException 
	 * 
	 * @see edu.ur.ir.security.SecurityService#assignOwnerPermissions(Object domainObject, IrUser user) 
	 */
	public void assignOwnerPermissions(Object domainObject, 
			IrUser user) {
		List<IrClassTypePermission> permissions = 
			getClassTypePermissions(CgLibHelper.cleanClassName(domainObject.getClass().getName()));
		createPermissions(domainObject, user, permissions);
	}

	/**
	 * Delete permissions
	 * 
	 * @see edu.ur.ir.security.SecurityService#deletePermissions(Long secureObjectId, String className, IrUser user) 
	 */
	public void deletePermissions(Long domainObjectId, String className, IrUser user) {
		log.debug("Delete permission for user " + user + " for object id " 
				+ domainObjectId + " of class " + className);
		
		IrAcl acl = irAclDAO.getAcl(domainObjectId, className, user);
		
		if (acl != null) {
			IrUserAccessControlEntry entry = acl.getUserAccessControlEntry(user.getUsername());
			acl.removeUserAccessControlEntry(entry);
			irAclDAO.makePersistent(acl);
		}
	}
	
	/**
	 * Delete permissions
	 * 
	 * @see edu.ur.ir.security.SecurityService#deletePermissions(Long secureObjectId, String className, IrUser user) 
	 */
	public void deletePermissions(Long domainObjectId, String className, IrUserGroup userGroup) {
		log.debug("Delete permission for user group " + userGroup + " for object id " + 
				domainObjectId + " of class " + className);
		
		IrAcl acl = irAclDAO.getAcl(domainObjectId, 
				CgLibHelper.cleanClassName(className), userGroup);
		
		if (acl != null) {
			IrUserGroupAccessControlEntry userGroupEntry = acl.getGroupAccessControlEntryByGroupId(userGroup.getId());
			log.debug("user group entry = " + userGroupEntry);
            boolean removed = acl.removeGroupAccessControlEntry(userGroupEntry);
            log.debug(" removed = " + removed);
			irAclDAO.makePersistent(acl);
		}
	}

	/**
	 * Delete access control list for an object identity
	 * 
	 * @see edu.ur.ir.security.SecurityService#deleteAcl(Long secureObjectId, String className)
	 */
	public void deleteAcl(Long domainObjectId, String className) {
		log.debug( " deleting domain object id = " + domainObjectId + 
				" className = " + className);
		IrAcl acl = irAclDAO.getAcl(domainObjectId, CgLibHelper.cleanClassName(className));
		log.debug("Delete ACL " + acl);
		
		irAclDAO.makeTransient(acl);
	}	
	
	/**
	 * Get the permissions for specified class type
	 */
	public List<IrClassTypePermission> getClassTypePermissions(String classType){
		
		List<IrClassTypePermission> classTypePermissions = 
			irClassTypePermissionDAO.getClassTypePermissionByClassType(CgLibHelper.cleanClassName(classType));
		return classTypePermissions;
	}
	
	/**
	 * Get the data access for ClassType
	 * @return
	 */
	public IrClassTypeDAO getIrClassTypeDAO() {
		return irClassTypeDAO;
	}

	/**
	 * Set data access for ClassType
	 * 
	 * @param irClassTypeDAO
	 */
	public void setIrClassTypeDAO(IrClassTypeDAO irClassTypeDAO) {
		this.irClassTypeDAO = irClassTypeDAO;
	}

	/**
	 * Get data access for ACL
	 *  
	 * @return
	 */
	public IrAclDAO getIrAclDAO() {
		return irAclDAO;
	}

	/**
	 * Set data access for ACL 
	 * 
	 * @param irAclDAO
	 */
	public void setIrAclDAO(IrAclDAO irAclDAO) {
		this.irAclDAO = irAclDAO;
	}

	/**
	 * Get data access for ClassTypePermission
	 * 
	 * @return
	 */
	public IrClassTypePermissionDAO getIrClassTypePermissionDAO() {
		return irClassTypePermissionDAO;
	}

	/**
	 * Set data access for ClassTypePermission
	 * 
	 * @param irClassTypePermissionDAO
	 */
	public void setIrClassTypePermissionDAO(IrClassTypePermissionDAO irClassTypePermissionDAO) {
		this.irClassTypePermissionDAO = irClassTypePermissionDAO;
	}
		
	/**
	 * Persistent for ClassType
	 * 
	 * @param entity
	 */
	public void makeClassTypePersistent(IrClassType entity) {
		irClassTypeDAO.makePersistent(entity);
	}

	/**
	 * Persistent for ClassTypePermission
	 * 
	 * @param entity
	 */
	public void makeClassTypePermissionPersistent(IrClassTypePermission entity) {
		irClassTypePermissionDAO.makePersistent(entity);
	}

	public IrUserAccessControlEntryDAO getIrUserAccessControlEntryDAO() {
		return irUserAccessControlEntryDAO;
	}

	public void setIrUserAccessControlEntryDAO(
			IrUserAccessControlEntryDAO irUserAccessControlEntryDAO) {
		this.irUserAccessControlEntryDAO = irUserAccessControlEntryDAO;
	}

	
	/**
	 * Get the permission for the given class type
	 * 
	 * @see edu.ur.ir.security.SecurityService#getPermissionForClass(java.lang.String, java.lang.String)
	 */
	public IrClassTypePermission getPermissionForClass(Object domainInstance,
			String name) {
		String classType = CgLibHelper.cleanClassName(domainInstance.getClass().getName());
		log.debug("Getting permission for class name " + classType + " and permission name " + name);
		return irClassTypePermissionDAO.getClassTypePermissionByNameAndClassType(classType,
				name);
	}

	
	/**
	 * Get the class type permission by id.
	 * 
	 * @see edu.ur.ir.security.SecurityService#getIrClassTypePermissionById(java.lang.Long)
	 */
	public IrClassTypePermission getIrClassTypePermissionById(
			Long classTypePermissionId, boolean lock) {
		
		return irClassTypePermissionDAO.getById(classTypePermissionId, lock);
	}

	public IrUserGroupAccessControlEntryDAO getIrUserGroupAccessControlEntryDAO() {
		return irUserGroupAccessControlEntryDAO;
	}

	public void setIrUserGroupAccessControlEntryDAO(
			IrUserGroupAccessControlEntryDAO irUserGroupAccessControlEntryDAO) {
		this.irUserGroupAccessControlEntryDAO = irUserGroupAccessControlEntryDAO;
	}

	/**
	 * Get all acls for the specified sid.
	 * 
	 * @see edu.ur.ir.security.SecurityService#getAcls(edu.ur.ir.security.Sid)
	 */
	public List<IrAcl> getAcls(Sid sid) {
		return irAclDAO.getAllAclsForSid(sid);
	}

	/**
	 * Save the specified access control list
	 * 
	 * @see edu.ur.ir.security.SecurityService#save(edu.ur.ir.security.IrAcl)
	 */
	public void save(IrAcl acl) {
		irAclDAO.makePersistent(acl);
	}

	/**
	 * 
	 * @see edu.ur.ir.security.SecurityService#getClassTypePermission(java.lang.String, java.lang.String)
	 */
	public IrClassTypePermission getClassTypePermission(String className,
			String permissionName) {
		if( log.isDebugEnabled())
		{
		    log.debug("Tring to find by name and class type name = " + 
				CgLibHelper.cleanClassName(className) + " permission name = " + permissionName );
		}
		return irClassTypePermissionDAO.getClassTypePermissionByNameAndClassType(CgLibHelper.cleanClassName(className), permissionName);
	}

	/**
	 * Delete the specified acl
	 * 
	 * @see edu.ur.ir.security.SecurityService#deleteAcl(edu.ur.ir.security.IrAcl)
	 */
	public void deleteAcl(IrAcl acl) {
		irAclDAO.makeTransient(acl);
	}

	public Long hasPermission(Object domainInstance, Sid sid, String permission) {
		Long objectId = getObjectId(domainInstance);
		String className = CgLibHelper.cleanClassName(domainInstance.getClass().getName());
		return irAclDAO.hasPermission(objectId, className, sid, permission);
	}

	/**
	 * Get sids that have the specified permission on the given object.
	 * 
	 * @see edu.ur.ir.security.SecurityService#getSidsWithPermissionForObject(java.lang.Object, java.lang.String)
	 */
	public Set<Sid> getSidsWithPermissionForObject(Object domainInstance,
			String permission) {
		Long objectId = getObjectId(domainInstance);
		String className = CgLibHelper.cleanClassName(domainInstance.getClass().getName());
		return irAclDAO.getSidsWithPermissionForObject(objectId, className, permission);
	}

	/**
	 * Get the specified sids with the spcified id.
	 * 
	 * @see edu.ur.ir.security.SecurityService#getSidsWithPermissionForObject(java.lang.Object, java.lang.String, java.util.List)
	 */
	public Set<Sid> getSidsWithPermissionForObject(Object domainInstance,
			String permission, List<Sid> specificSids) {
		Long objectId = getObjectId(domainInstance);
		String className = CgLibHelper.cleanClassName(domainInstance.getClass().getName());
		return irAclDAO.getSidsWithPermissionForObject(objectId, className, permission, specificSids);
	}

	
	/**
	 * Create the permissions for user control entries.  This is a bulk operation.
	 * 
	 * @param entries - list of entries
	 * @param permissions - list of permissions to give to each entry
	 * 
	 * @return number of entries created
	 */
	public int createPermissionsForUserControlEntries(List<IrUserAccessControlEntry> entries,
			List<IrClassTypePermission> permissions)
	{
		return irUserAccessControlEntryDAO.createPermissionsForUserControlEntries(entries, permissions);
	}
	
	/**
	 * Create user control entries for the list of users for the
	 * specified acls.  This is a bulk operation
	 * 
	 * @param users - list of users to create the entries for
	 * @param acl - acl to add the entries to
	 * 
	 * @return number of entries created
	 */
	public int createUserControlEntriesForUsers(List<IrUser> users, List<IrAcl> acls )
	{
		return irUserAccessControlEntryDAO.createUserControlEntriesForUsers(users, acls);
	}
	
	/**
	 * Get the list of users for the given access control list.
	 * 
	 * @param acl - acl to the the access control entries for
	 * @param users - list of users to get for the acl
	 * 
	 * @return - list of users found.
	 */
	public List<IrUserAccessControlEntry> getUserControlEntriesForUsers(IrAcl acl, List<IrUser> users)
	{
		return irUserAccessControlEntryDAO.getUserControlEntriesForUsers(acl, users);
	}

}
