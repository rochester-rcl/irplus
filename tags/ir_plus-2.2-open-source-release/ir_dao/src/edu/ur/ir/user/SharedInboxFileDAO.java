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

/**
 * @author Nathan Sarr interface for dealing with shared inbox files
 *
 */
public interface SharedInboxFileDAO extends CountableDAO, 
CrudDAO<SharedInboxFile>{
	
	/**
	 * Get a count of the shared inbox files for the 
	 * specified user.
	 * 
	 * @param user - user to get the inbox file count for.
	 * @return the number of shared inbox files for the user.
	 */
	public Long getSharedInboxFileCount(IrUser user);
	
	/**
	 * Get the inbox files for a specified user
	 * 
	 * @param user - user to get the inbox file count for.
	 * @return the shared inbox files for the user.
	 */
	public List<SharedInboxFile> getSharedInboxFiles(IrUser user);
	
	/**
	 * Get shared inbox files for specified user and ids
	 * 
	 * @param userId User having the files in shared inbox files
	 * @param fileIds Id of Shared inbox files
	 *  
	 * @return List of shared inbox files
	 */
	public List<SharedInboxFile> getSharedInboxFiles(final Long userId, final List<Long> fileIds);
}
