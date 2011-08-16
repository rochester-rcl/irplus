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

package edu.ur.ir.user;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.dao.UniqueNameDAO;

/**
 * Interface for persisting an IrRole
 * 
 * @author Nathan Sarr
 *
 */
public interface IrRoleDAO extends CountableDAO, 
CrudDAO<IrRole>, NameListDAO, UniqueNameDAO<IrRole>
{
	/**
	 * Get users in the given list with the specified ids.  If the list
	 *  is empty, no roles are returned.
	 * 
	 * @param list of role ids.
	 * 
	 * @return the found roles
	 */
	public List<IrRole> getRoles(List<Long> roleIds);
	
	/**
	 * Get a list of roles
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param sortType - sort order(Asc/desc)
	 * 
	 * @return the list of roles found.
	 */
	public List<IrRole> getRoles(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);
}
