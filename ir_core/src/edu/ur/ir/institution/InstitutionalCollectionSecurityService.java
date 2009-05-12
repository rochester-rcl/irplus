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

package edu.ur.ir.institution;

import java.util.List;
import java.util.Set;

import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.Sid;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;



/**
 * Service for dealing with institutional collection
 * and repository related security issues.  To apply security,
 * A created collection should be passed through these methods.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalCollectionSecurityService {
	
	/** permissions that can be granted on institutional collections */
	
	/** this user has full control over the collection */
	public static final InstitutionalCollectionPermission ADMINISTRATION_PERMISSION = new InstitutionalCollectionPermission("ADMINISTRATION");

	/** this user has direct submission into the collection */
	public static final InstitutionalCollectionPermission DIRECT_SUBMIT_PERMISSION = new InstitutionalCollectionPermission("DIRECT_SUBMIT");

	
	/** this user's submissions must first be reviewed before submission is allowed */
	public static final InstitutionalCollectionPermission REVIEW_SUBMIT_PERMISSION = new InstitutionalCollectionPermission("REVIEW_SUBMIT");

	
	/** this user has view privileges */
	public static final InstitutionalCollectionPermission VIEW_PERMISSION = new InstitutionalCollectionPermission("VIEW");

	/** this user has reviewer permission on the collection and can approve / reject 
	 *  submissions into the collection
	 */
	public static final InstitutionalCollectionPermission REVIEWER_PERMISSION = new InstitutionalCollectionPermission("REVIEWER");

	
	
	/**
	 * Gives permissions to all groups in parent collections that currently have
	 * administration privileges.  This makes sure administrators
	 * in parent collections can manage children collections.  This should be called every time
	 * a child collection is added
	 * 
	 * @param child - child collection to add permissions to all it's parents
	 */
	public void giveAdminPermissionsToParentCollections(InstitutionalCollection child);
	
	/**
	 * Gives permissions to all child collections for the specified user group.  This makes sure when a group in a parent collection is updated with
	 * administration permissions all children are updated with the group.  
	 * This should be called any time administration privileges  are given to a particular group for a particular collection
	 * 
	 * @param userGroup - group given admin privileges
	 * @param parent - parent collection 
	 */
	public void giveAdminPermissionsToChildCollections(IrUserGroup userGroup, InstitutionalCollection parent);

	
	/**
	 * Delete the access control list for the specified institutional collection.
	 * 
	 * @param institutionalCollection
	 */
	public void deleteAcl(InstitutionalCollection institutionalCollection);
	
	/**
	 * Get all secure ids with the specified permission for the specified collection.
	 * 
	 * @param c - collection that the sid has to have permission on
	 * @param permission - the permission the sid should have on the collection
	 * @return - set of sids that have the specified permission on the specified collection
	 */
	public Set<Sid> getSidsWithPermission(InstitutionalCollection c, InstitutionalCollectionPermission permission);
	
	/**
	 * Get the class type permission for the given institutiona collection permission.
	 * 
	 * @param collectionPermission - class type permission to get
	 * @return found IrClassTypePermission.
	 */
	public IrClassTypePermission getClassTypePermission(InstitutionalCollectionPermission collectionPermission);
	
	/**
	 * Determine if the user is granted the specified permission.
	 * 
	 * @param c - the collection the permission should be granted to
	 * @param user - the user to check
	 * @param permission - the permission the user should have
	 * @return - true if the user has the specified permission otherwise false
	 */
	public boolean isGranted(InstitutionalCollection c, IrUser user, InstitutionalCollectionPermission permission);
	
	/**
	 * Get the access control list for the specified institutional collection.
	 * 
	 * @param collection - institutional collection to get the access control list for
	 * @return the found access control list for the collection
	 */
	public IrAcl getAcl(InstitutionalCollection collection);
	
	/**
	 * Create an access control list for the specified collection.  If one already exists
	 * the existing access control list is returned.
	 * 
	 * @param collection - collection the acl should be created for
	 * @return - the created access control list.
	 */
	public IrAcl createAcl(InstitutionalCollection collection);
	
	/**
	 * Remove the grop from the collection access control list.
	 * 
	 * @param collection - collection to remove the group from
	 * @param userGroup - the user group to remove from the collection
     * @return the updated acl entries for the collection
	 */
	public IrAcl removeGroupFromCollectionAcl(InstitutionalCollection collection, IrUserGroup userGroup);

	/**
	 * Returns a count of the number of times the specified user has the permission.  A count greater than 0
	 * means the user has the permission.
	 * 
	 * @param collection - collection to see if the user has the permission
	 * @param user - user to check
	 * @param permission - permission to check
	 * 
	 * @return - number of times the permission was found for the user a count greater than one means the user has
	 * the specified permission.
	 * 
	 */
	public long hasPermission(InstitutionalCollection collection, IrUser user, InstitutionalCollectionPermission permission);
	
	/**
	 * Give the specified permission to the institutional collection for the given group.
	 * 
	 * @param collection - institutional collection to give the permissions on
	 * @param userGroup - user group to get the permissions
	 * @param permission - the permission to be given
	 * 
	 * @return - the updated access control list
	 */
	public IrAcl givePermission(InstitutionalCollection collection, IrUserGroup userGroup, InstitutionalCollectionPermission permission);
	
	/**
	 * Get the list of permissions for collections as IrClassTypePermissions.
	 * 
	 * @return - list of permissions
	 */
	public List<IrClassTypePermission> getCollectionPermissions();
}
