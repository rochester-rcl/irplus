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
 * Interface for user groups.
 * 
 * @author Nathan Sarr
 *
 */
public interface IrUserGroupDAO extends CountableDAO, 
CrudDAO<IrUserGroup>, NameListDAO, UniqueNameDAO<IrUserGroup>
{
	/**
	 * Get the list of user groups.
	 * 
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param sortType - sort order(Asc/desc)
	 * 
	 * @return list of user groups.
	 */
	public List<IrUserGroup> getUserGroups(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);
	
	/**
	 * Get all the user groups for a given user.
	 * 
	 * @param userId - id of the user to get the groups for
	 * @return - list of groups the user is in.
	 */
	public List<IrUserGroup> getUserGroupsForUser(Long userId);
	
	/**
	 * Get a user for the specified group 
	 * 
	 * @param groupId - id of the group
	 * @param userId - id of the user
	 * 
	 * @return the user if found - otherwise null.
	 */
	public IrUser getUserForGroup(Long groupId, Long userId);

}

