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

import java.io.File;
import java.io.Serializable;
import java.util.List;

import edu.ur.ir.NoIndexFoundException;

/**
 * Service for indexing users within the system.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserIndexService extends Serializable{
	
	/**
	 * Add a set of items to the index - this is generally used for batch processing of multiple institutional items.
	 * This can also be used to re-index a set the existing set of items.
	 * 
	 * @param items - set of items to add
	 * @param institutionalItemIndex - index to add it to
	 * @param overwriteExistingIndex - indicates this should overwrite the current index
	 */
	public void addUsers(List<IrUser> users, File userIndexFolder, boolean overwriteExistingIndex);
	
	/**
	 * Add the person name to the index.
	 * 
	 * @param personNameAuthority
	 * @param repo repository 
	 */
	public void addToIndex(IrUser user, File userIndexFolder) throws NoIndexFoundException;
	
	/**
	 * Update the user in the index.
	 * 
	 * @param user - user in the index
	 * @param userIndexFolder - folder location of the user 
	 */
	public void updateIndex(IrUser user, File userIndexFolder) throws NoIndexFoundException;
	
	/**
	 * Delete the user from index.
	 * 
	 * @param userId - id of the user
	 * @param userIndexFolder - location of the user index folder 
	 */
	public void deleteFromIndex(Long userId, File userIndexFolder);

}
