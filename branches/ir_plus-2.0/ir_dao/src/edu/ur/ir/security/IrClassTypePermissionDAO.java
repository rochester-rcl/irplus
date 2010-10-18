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
import edu.ur.dao.NameListDAO;

/**
 * Interface for persisting an IrClassTypePermission
 * 
 * @author Nathan Sarr
 *
 */
public interface IrClassTypePermissionDAO extends CountableDAO, 
CrudDAO<IrClassTypePermission>, NameListDAO {
	
	/**
	 * Get permissions for class type
	 * 
	 * @param classType class type of the permission
	 * @return List of permissions for the specified class type 
	 */
	public List<IrClassTypePermission> getClassTypePermissionByClassType(String classType);
	
	/**
	 * Get a specified permissions by name for class type
	 * 
	 * @param classType class type of the permission
	 * @param name name of the permission to get
	 * @return The found irClassTypePermission or null if no class type permission found 
	 */
	public IrClassTypePermission getClassTypePermissionByNameAndClassType(String classType, String name);
}
