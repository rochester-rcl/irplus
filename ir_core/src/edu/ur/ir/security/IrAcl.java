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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.util.Assert;

import edu.ur.cgLib.CgLibHelper;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.persistent.BasePersistent;
import java.util.Collections;

/**
 * Represents a access control list for a specific object.  This
 * access control list contains access control 
 * entries for a specific object.
 * 
 * @author Nathan Sarr
 *
 */
public class IrAcl extends BasePersistent implements Acl 
{
	/**  Eclipse generated id. */
	private static final long serialVersionUID = -9129654335946520747L;
	
	/**  Logger */
	private static final Logger log = LogManager.getLogger(IrAcl.class);
	
	/**  The parent acl if one exists. */
	private IrAcl irParentAcl;
	
	/**  Indicates if this acl inherits.  */
	private boolean entriesInheriting;
	
	/**  Class type for this object. */
	private IrClassType classType;
	
	/**  The object id. */
	private Long objectId;

	/**  Set of user access control entries.  */
	private Set<IrUserAccessControlEntry> userEntries = new HashSet<IrUserAccessControlEntry>(); 
	
	/**  Set of role access control entries.  */
	private Set<IrRoleAccessControlEntry> roleEntries = new HashSet<IrRoleAccessControlEntry>(); 
	
	/**  Set of Group access control entries */
	private Set<IrUserGroupAccessControlEntry> groupEntries = new HashSet<IrUserGroupAccessControlEntry>();
	
	/**  Version of the irAcl */
	private int version;
	

	/**
	 * Package protected constructor
	 */
	public IrAcl(){}
	
	/**
	 * Constructor
	 * 
	 * @param irObjectIdentity
	 */
	@SuppressWarnings("unchecked")
	public IrAcl(Object object, IrClassType classType)
	{
        Assert.notNull(object, "object cannot be null");

        //remove any CGLIB reference.
        String className = CgLibHelper.cleanClassName(object.getClass().getName());
        
        if(!className.equals(classType.getJavaType().getName()))
        {
        	throw new IllegalStateException("object.getClass() = " + object.getClass().getName() +
        			" Class type must equal classType.getJavaType() = " 
        			+ classType.getJavaType().toString());
        }
        
        this.classType = classType;

        try {
        	String classTypeName = this.classType.getJavaType().getName();
        	Class theClass = Class.forName(CgLibHelper.cleanClassName(classTypeName));
        	Method method = theClass.getMethod("getId", new Class[] {});
            this.objectId = (Long)method.invoke(object, new Object[] {});
        } catch (Exception e) {
            throw new IllegalStateException("Could not extract identity from object " + object, e);
        }
        log.debug("Created ACL with objectId = " + objectId + " and classType = " + classType.toString());
	}
	
	/**
	 * Get user groups with given class type permission
	 * 
	 * @param classTypePermission
	 * @return
	 */
	public List<IrUserGroup> getIrUserGroupsWithPermission(IrClassTypePermission classTypePermission) {
		
		List<IrUserGroup> userGroups = new ArrayList<IrUserGroup>();
		
		for(IrUserGroupAccessControlEntry entry: groupEntries) {
			if (entry.getIrClassTypePermissions().contains(classTypePermission)) {
				userGroups.add(entry.getUserGroup());
			}
		}
		
		return userGroups;
	}
	
	/**
	 * Create a mew role access control entry for this acl.  if the user
	 * already exists, the existing user access control entry is returned
	 * 
	 * @param user - user to create an access control entry for
	 * @return the newly created role with this acl as the parent.
	 * 
	 */
	public IrUserAccessControlEntry createUserAccessControlEntry(IrUser user) 
	{
		IrUserAccessControlEntry uace = null; 
		uace = this.getUserAccessControlEntry(user.getUsername());
		if( uace == null)
		{
			uace = new IrUserAccessControlEntry(user, this);
			userEntries.add(uace);
		}

		return uace;
	}

	/**
	 * Return the user access control entry if found otherwise null.
	 * 
	 * @param id - id for the user access control entry
	 * @return the access control entry or null if the entry is not found.
	 */
	public IrUserAccessControlEntry getUserAccessControlEntry(Long id)
	{
		for(IrUserAccessControlEntry uac : userEntries)
		{
			if( uac.getId().equals(id) )
			{
				return uac;
			}
		}
		
		return null;
	}
	
	/**
	 * Return the user access control entry if found by user id otherwise null.
	 * 
	 * @param id - id for the user 
	 * @return the access control entry or null if the entry is not found.
	 */
	public IrUserAccessControlEntry getUserAccessControlEntryByUserId(Long userId)
	{
		for(IrUserAccessControlEntry uac : userEntries)
		{
			if( uac.getSid().getId().equals(id) )
			{
				return uac;
			}
		}
		
		return null;
	}
	
	/**
	 * Return the user access control entry if found otherwise null.
	 * 
	 * @param username - username for the user 
	 * @return the access control entry or null if the entry is not found.
	 */
	public IrUserAccessControlEntry getUserAccessControlEntry(String username)
	{
		for(IrUserAccessControlEntry uac : userEntries)
		{
			if( uac.getSid().getUsername().equals(username) )
			{
				return uac;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Remove the access control entry for the specified security id.
	 * 
	 * @param sid - secure id.
	 * @return true if the access control entry is removed.
	 */
	public boolean removeAccessControlEntry(Sid sid)
	{
		if( sid.getSidType().equals(IrUser.USER_SID_TYPE))
		{
			IrUser user = (IrUser) sid;
			return userEntries.remove(this.getUserAccessControlEntry(user.getUsername()));
		}
		else if( sid.getSidType().equals(IrUserGroup.GROUP_SID_TYPE))
		{
			IrUserGroup userGroup = (IrUserGroup) sid;
			return groupEntries.remove(this.getGroupAccessControlEntryByGroupId(userGroup.getId()));
		}
		else if( sid.getSidType().equals(IrRole.ROLE_SID_TYPE))
		{
			IrRole role = (IrRole)sid;
			return roleEntries.remove(this.getRoleAccessControlEntry(role.getName()));
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Remove the user access control entry.
	 * 
	 * @param userControlEntry
	 * 
	 * @return true if the control entry is removed
	 */
	public boolean removeUserAccessControlEntry(IrUserAccessControlEntry userControlEntry)
	{
		return userEntries.remove(userControlEntry);
	}
	
	/**
	 * Create a new role access control entry for this acl.  If the role already 
	 * exists for this acl, the existing role access control entry is returned.
	 * 
	 * @param role - role to add to this acl
	 * @return the newly created role with this acl as the parent.
	 */
	public IrRoleAccessControlEntry createRoleAccessControlEntry(IrRole role) 
	    
	{
		IrRoleAccessControlEntry race = null;
		race = getRoleAccessControlEntry(role.getName());
		
		if( race == null)
		{
			race = new IrRoleAccessControlEntry(role, this);
			roleEntries.add(race);
		}
		
		return race;
	}
	
	/**
	 * Return the role access control entry if found otherwise null.
	 * 
	 * @param id - id for the Role access control entry
	 * @return the role access control entry or null if not found
	 */
	public IrRoleAccessControlEntry getRoleAccessControlEntry(Long id)
	{
		for(IrRoleAccessControlEntry rac : roleEntries)
		{
			if( rac.getId().equals(id) )
			{
				return rac;
			}
		}
		
		return null;
	}
	
	/**
	 * Return the role access control entry if found otherwise null.
	 * 
	 * @param name - name of the Role 
	 * @return the role access control entry or null if not found
	 */
	public IrRoleAccessControlEntry getRoleAccessControlEntry(String name)
	{
		for(IrRoleAccessControlEntry rac : roleEntries)
		{
			if( rac.getSid().getName().equals(name) )
			{
				return rac;
			}
		}
		
		return null;
	}
	
	/**
	 * Remove the role entry for this object.
	 * 
	 * @param roleControlEntry
	 * @return true if the role access control entry is removed
	 */
	public boolean removeRoleAccessControlEntry(IrRoleAccessControlEntry roleControlEntry)
	{
		return roleEntries.remove(roleControlEntry);
	}
	
	/**
	 * Get the acl entries 
	 * 
	 * @see edu.ur.ir.security.Acl#getEntries()
	 */
	public List<AccessControlEntry> getEntries() {
		
		ArrayList<AccessControlEntry> acls = new ArrayList<AccessControlEntry>();
		acls.addAll(userEntries);
		acls.addAll(roleEntries);
		acls.addAll(groupEntries);
		return acls;
	}
	
	/**
	 * Get all permissions granted for this sid on this object identity.
	 * 
	 * @param sid
	 * @return the set of permissions or an empty set.
	 */
	public Set<IrClassTypePermission> getPermissions(Sid sid)
	{
		
		if( sid.getSidType().equals(IrUser.USER_SID_TYPE))
		{
			log.debug("checking user");
			for(IrUserAccessControlEntry entry: userEntries )
			{
				if( entry.getIrUser().equals(sid))
				{
					return entry.getIrClassTypePermissions();
				}
			}
		
		}
		else if( sid.getSidType().equals(IrRole.ROLE_SID_TYPE))
		{
			for(IrRoleAccessControlEntry entry: roleEntries )
			{
				if( entry.getIrRole().equals(sid))
				{
					return entry.getIrClassTypePermissions();
				}
			}
			
		}
		else if(  sid.getSidType().equals(IrUserGroup.GROUP_SID_TYPE))
		{
			for(IrUserGroupAccessControlEntry entry: groupEntries )
			{
				if( entry.getUserGroup().equals(sid))
				{
					return entry.getIrClassTypePermissions();
				}
			}
			
		}
		return new HashSet<IrClassTypePermission>();
	}
	
	/**
	 * Create a mew group access control entry for this acl.  If an access control
	 * entry for this group already exists, the existing group access control
	 * entry is returned
	 * 
	 * @param group to create an access control entry for
	 * @return the newly created group access control entry with this acl as the parent.
	 */
	public IrUserGroupAccessControlEntry createGroupAccessControlEntry(IrUserGroup group) 
	{
		IrUserGroupAccessControlEntry gace = null;
		gace = getGroupAccessControlEntry(group);
		
		if( gace == null)
		{
			gace = new IrUserGroupAccessControlEntry(group, this);
			groupEntries.add(gace);
		}
		
		return gace;
	}
	
	/**
	 * Return the group access control entry if found otherwise null.
	 * 
	 * @param id - id for the group access control entry
	 * @return the found entry or null if no entry found
	 */
	public IrUserGroupAccessControlEntry getGroupAccessControlEntry(IrUserGroup g)
	{
		for(IrUserGroupAccessControlEntry gac : groupEntries)
		{
			if( gac.getSid().equals(g) )
			{
				return gac;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Return the group access control entry if found otherwise null.
	 * 
	 * @param id - id for the group access control entry
	 * @return the found entry or null if no entry found
	 */
	public IrUserGroupAccessControlEntry getGroupAccessControlEntry(Long id)
	{
		for(IrUserGroupAccessControlEntry gac : groupEntries)
		{
			if( gac.getId().equals(id) )
			{
				return gac;
			}
		}
		
		return null;
	}
	
	/**
	 * Return the group access control entry by group id if found otherwise null.
	 * 
	 * @param id - id for the group access control entry
	 * @return the found entry or null if no entry found
	 */
	public IrUserGroupAccessControlEntry getGroupAccessControlEntryByGroupId(Long id)
	{
		for(IrUserGroupAccessControlEntry gac : groupEntries)
		{
			if( gac.getSid().getId().equals(id) )
			{
				return gac;
			}
		}
		
		return null;
	}
	
	/**
	 * Remove the group entry for this object.
	 * 
	 * @param groupControlEntry
	 * @return true if the group entry is removed
	 */
	public boolean removeGroupAccessControlEntry(IrUserGroupAccessControlEntry groupControlEntry)
	{
		return groupEntries.remove(groupControlEntry);
	}

	/**
	 * Get the parent acl.
	 * 
	 * @see edu.ur.ir.security.Acl#getParentAcl()
	 */
	public Acl getParentAcl() {
		return irParentAcl;
	}

	/**
	 * Indicates if this entry inherits permissions from it's parent
	 * object.
	 * 
	 * @see edu.ur.ir.security.Acl#isEntriesInheriting()
	 */
	public boolean isEntriesInheriting() {
		return entriesInheriting;
	}
	
	/**
	 * Get the version of the data.
	 * 
	 * @see edu.ur.persistent.BasePersistent#getVersion()
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Set the version of the data.
	 * 
	 * @see edu.ur.persistent.BasePersistent#setVersion(int)
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * See if the permission is granted.  It is expected each user
	 * will have only one entry in the ACL.
	 * 
	 * @see edu.ur.ir.security.Acl#isGranted(String, edu.ur.ir.security.Sid, boolean)
     */
	public boolean isGranted(String permission, Sid sid, boolean administrativeMode){
		
		log.debug("Sid Class = " + sid.getClass());
		
		if( sid.getSidType().equals(IrUser.USER_SID_TYPE))
		{
			log.debug("checking user");
			if( checkUserEntries(permission, (IrUser)sid) )
			{
				log.debug("user returned true");
				return true;
			}
			if( checkGroupEntries(permission, (IrUser)sid))
			{
				log.debug("checked groups true");
				return true;
			}
		}
		else if( sid.getSidType().equals(IrRole.ROLE_SID_TYPE))
		{
			log.debug("checking role");
			if( checkRoleEntries(permission, (IrRole)sid) )
			{
				log.debug("role returned true");
				return true;
			}
		}
		else if(  sid.getSidType().equals(IrUserGroup.GROUP_SID_TYPE))
		{
			log.debug("checking group");
			if( checkGroupEntries(permission, (IrUserGroup)sid))
			{
				log.debug("group returned true");
				return true;
			}
		}
		
		if(isEntriesInheriting() && irParentAcl != null )
		{
			return irParentAcl.isGranted(permission, sid, administrativeMode); 
		}
		
		return false;
	} 

	/**
	 * User access control entry as an unmodifiable set.
	 * 
	 * @return
	 */
	public Set<IrUserAccessControlEntry> getUserEntries() {
		return Collections.unmodifiableSet(userEntries);
	}

	/**
	 * Set the user access control entries.
	 * 
	 * @param userEntries
	 */
	void setUserEntries(Set<IrUserAccessControlEntry> userEntries) {
		this.userEntries = userEntries;
	}

	/**
	 * Set teh parent ACL.
	 * 
	 * @param parentAcl
	 */
	public void setIrParentAcl(IrAcl parentAcl) {
		this.irParentAcl = parentAcl;
	}
	
	/**
	 * Get teh parent acl.
	 * 
	 * @return
	 */
	public IrAcl getIrParentAcl() {
		return irParentAcl;
	}

	/**
	 * Get the role entries.
	 * 
	 * @return
	 */
	public Set<IrRoleAccessControlEntry> getRoleEntries() {
		return Collections.unmodifiableSet(roleEntries);
	}

	/**
	 * Set the role entries.
	 * 
	 * @param roleEntries
	 */
	void setRoleEntries(Set<IrRoleAccessControlEntry> roleEntries) {
		this.roleEntries = roleEntries;
	}
	
	/**
	 * Get the group entries.
	 * 
	 * @return
	 */
	public Set<IrUserGroupAccessControlEntry> getGroupEntries() {
		return Collections.unmodifiableSet(groupEntries);
	}

	/**
	 * Set the group entries.
	 * 
	 * @param groupEntries
	 */
	public void setGroupEntries(Set<IrUserGroupAccessControlEntry> groupEntries) {
		this.groupEntries = groupEntries;
	}

	/**
	 * Set this to true if the entries inherity the permissions of the parent.
	 * 
	 * @param entriesInheriting
	 */
	public void setEntriesInheriting(boolean entriesInheriting) {
		this.entriesInheriting = entriesInheriting;
	}	
	
	/**
	 * Determine if this acl inherits.
	 * 
	 * @param entriesInheriting
	 */
	public boolean getEntriesInheriting() {
		return entriesInheriting;
	}

	/**
	 * Check the user entries for the specified permission.
	 * 
	 * @param permission
	 * @param sid
	 * @return true if the permission is found for the user
	 */ 
	private boolean checkUserEntries(String permission, IrUser sid )
	{
		for(IrUserAccessControlEntry userEntry : userEntries)
		{
			if( userEntry.getIrUser().equals(sid) )
			{
				return userEntry.isGranting(permission, sid);
			}
		}
		return false;
	}
	
	/**
	 * Check the role entry for the specified permission.
	 * 
	 * @param permission
	 * @param sid
	 * @return
	 */
	private boolean checkRoleEntries(String permission, IrRole sid )
	{
		for(IrRoleAccessControlEntry roleEntry : roleEntries)
		{
			if( roleEntry.getIrRole().equals(sid))
			{
				return roleEntry.isGranting(permission, sid);
			}
		}
		return false;
	}
	 
	/**
	 * Check the group entry for the specified permission.
	 * 
	 * @param permission
	 * @param sid
	 * @return
	 */
	private boolean checkGroupEntries(String permission, IrUserGroup sid )
	{
		for(IrUserGroupAccessControlEntry groupEntry : groupEntries)
		{
			if( groupEntry.getUserGroup().equals(sid))
			{
				return groupEntry.isGranting(permission, sid);
			}
		}
		return false;
	}
	
	/**
	 * Check the groups for the specified user.  If the group 
	 * contains the user check to see if the group grants the permission.
	 * 
	 * @param permission
	 * @param sid - user to check
	 * @return true if one of the groups grants the permission otherwise false.
	 */
	private boolean checkGroupEntries(String permission, IrUser sid )
	{
		log.debug("checking group entries");
		for(IrUserGroupAccessControlEntry groupEntry : groupEntries)
		{
			log.debug("checking group " + groupEntry);
			log.debug( "user = " + sid + " contains user = " + groupEntry.getUserGroup().getUsers().contains(sid));
			log.debug( " contains permission " + permission  + " is granting = " + groupEntry.isGranting(permission, groupEntry.getUserGroup()));
			if( groupEntry.getUserGroup().getUsers().contains(sid) && 
					groupEntry.isGranting(permission, groupEntry.getUserGroup()))
			{
				return true;
			}
		}
		return false;
	}
	

	/**
	 * The type of class this object represents.
	 * 
	 * @return
	 */
	public IrClassType getClassType() {
		return classType;
	}

	/**
	 * Set the class type this object represents.
	 * 
	 * @param classType
	 */
	void setClassType(IrClassType classType) {
		this.classType = classType;
	}

	/**
	 * Get the object id for this object.
	 * 
	 * @return
	 */
	public Long getObjectId() {
		return objectId;
	}

	/**
	 * Set the object id for this identity.
	 * 
	 * @param objectId
	 */
	void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	/**
	 * Get the hash code.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += classType == null ? 0 : classType.hashCode();
		value += objectId == null ? 0 : objectId.hashCode();
		return value;
	}
	
	/**
	 * Determine if two ACL's are equal
	 * 
	 * @return
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IrAcl)) return false;

		final IrAcl other = (IrAcl) o;

		if( ( objectId != null && !objectId.equals(other.getObjectId()) ) ||
			( objectId == null && other.getObjectId() != null ) ) return false;

		if( ( classType != null && !classType.equals(other.getClassType()) ) ||
				( classType == null && other.getClassType() != null ) ) return false;

		
		return true;
	}
	
	/**
	 * To String method.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[");
		sb.append(" id = ");
		sb.append(id);
		sb.append("Class Type = ");
		sb.append(classType.getName());
		sb.append(" objectId = ");
		sb.append(objectId);
		sb.append("]");
		return sb.toString();
	}

	
}
