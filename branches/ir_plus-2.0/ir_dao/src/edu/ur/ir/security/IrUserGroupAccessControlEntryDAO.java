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

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.user.IrUserGroup;

/**
 * User group access control entry.
 * 
 * @author Nathan Sarr
 *
 */
public interface IrUserGroupAccessControlEntryDAO extends CountableDAO, 
CrudDAO<IrUserGroupAccessControlEntry>{
	
	/**
	 * Determine the count of permissions for the specified user group, class type
	 * and classType permission 
	 * 
	 * @param userGroup - user group to check 
	 * @param classTypePermission - permission that group should have
	 * @param classType - class type  
	 * 
	 * @return count of the class types
	 */
	public Long getUserGroupPermissionCountForClassType(IrUserGroup userGroup, 
			IrClassTypePermission classTypePermission, IrClassType classType);

	/**
	 * Determine if the user group has the specified permission for the specified object id
	 * 
	 * @param userGroup - user group to check
	 * @param classTypePermission - permission the user group should have
	 * @param classType - class type of the object 
	 * @param objectId - id of the object the user group should have the object for
	 * 
	 * 
	 * @return
	 */
	public Long getUserGroupPermissionCountForClassTypeObject(IrUserGroup userGroup, 
			IrClassTypePermission classTypePermission, IrClassType classType, Long objectId);

}
