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

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.user.IrUser;

/**
 * Persistence for a user access control entry.
 * 
 * @author Nathan Sarr
 *
 */
public interface IrUserAccessControlEntryDAO extends CountableDAO, 
CrudDAO<IrUserAccessControlEntry>{
	
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
	 * Create the permissions for user control entries.
	 * 
	 * @param entries - list of entries
	 * @param permissions - list of permissions to give to each entry
	 * 
	 * @return
	 */
	public int createPermissionsForUserControlEntries(List<IrUserAccessControlEntry> entries,
			List<IrClassTypePermission> permissions);
	
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
