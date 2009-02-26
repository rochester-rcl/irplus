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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ur.persistent.BasePersistent;

/**
 * Represents a single access control entry which are
 * the access rights to a specified object for a specific sid (Security Identity).  
 * 
 * A object is identified by an id and the class type.
 * 
 * @author Nathan Sarr
 *
 */
public abstract class IrAbstractAccessControlEntry extends BasePersistent implements AccessControlEntry{
	
	/**  The class type permissions for this sid */
	protected Set<IrClassTypePermission> irClassTypePermissions = new HashSet<IrClassTypePermission>();
	
	/**  The access control list this access control entry belongs to. */
	protected IrAcl irAcl;
	
	/**
	 * Get the class type permissions available for this Sid
	 * 
	 * @return
	 */
	public Set<IrClassTypePermission> getIrClassTypePermissions() {
		return irClassTypePermissions;
	}
	
	/**
	 * Add a permission to this access control entry.  Throws an 
	 * Illegal State exception of the class type permission is not
	 * the same class type as the <code>IrAcl</code> class type
	 * 
	 * @param classTypePermission
	 */
	public void addPermission(IrClassTypePermission classTypePermission)
	{
		if( !irAcl.getClassType().equals(classTypePermission.getIrClassType()))
		{
			throw new IllegalStateException("Class type permission class type must be the same" +
			 " as the IrAcl class type classTyperPermission = " + classTypePermission.getIrClassType() +
			 " acl class type = " + irAcl.getClassType());
		}
		irClassTypePermissions.add(classTypePermission);
	}
	
	/**
	 * Remove the class type permission for the sid.  Returns true if the class 
	 * type permission is removed.
	 * 
	 * @param classTypePermission
	 * @return true if the permission is removed for this sid
	 */
	public boolean removePermission(IrClassTypePermission classTypePermission)
	{
		return irClassTypePermissions.remove(classTypePermission);
	}

	/**
	 * Set the permissions for this sid.
	 * 
	 * @param irClassTypePermissions
	 */
	public void setIrClassTypePermissions(
			Set<IrClassTypePermission> irClassTypePermissions) {
		this.irClassTypePermissions = irClassTypePermissions;
	}

	/**
	 * Get the acl for this access control entry.
	 * 
	 * @see edu.ur.ir.security.AccessControlEntry#getAcl()
	 */
	public IrAcl getAcl() {
		return irAcl;
	}

	/**
	 * Get the permissions for this Access control list
	 * 
	 * @see edu.ur.ir.security.AccessControlEntry#getPermissions()
	 */
	public List<String> getPermissionNames() {
		ArrayList<String> permissions = new ArrayList<String>();
		
		for(IrClassTypePermission p : irClassTypePermissions )
		{
			permissions.add(p.getName());
		}
		
		return permissions;
	}
	
	/**
	 * Get the permissions for this Access control list
	 * 
	 * @see edu.ur.ir.security.AccessControlEntry#getPermissions()
	 */
	public List<String> getPermissions() {
		ArrayList<String> permissions = new ArrayList<String>();
		
		for(IrClassTypePermission p : irClassTypePermissions )
		{
			permissions.add(p.getName());
		}
		
		return permissions;
	}

	/**
	 * Determine if a sid has permissions based on a permission
	 * name.  For example read, write, modify, full control.
	 * 
	 * @param permissionName
	 * 
	 * @param sid
	 * 
	 * @return true if the sid has the permission name on the object
	 * this access control list protects.
	 */
	public boolean isGranting(String permissionName, Sid sid) {
		
		if (sid == null || !sid.equals(getSid())) {
			return false;
		}

		if (getPermissions().contains(permissionName)) {
				return true;
		}
		
		return false;
	}
	
	/**
	 * Get the access control list
	 * 
	 * @return ir acl
	 */
	public IrAcl getIrAcl() {
		return irAcl;
	}

	/**
	 * Set the access control list for this sid.
	 * 
	 * @param irAcl
	 */
	public void setIrAcl(IrAcl irAcl) {
		this.irAcl = irAcl;
	}
}
