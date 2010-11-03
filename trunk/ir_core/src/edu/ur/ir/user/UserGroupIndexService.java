/**  
   Copyright 2008-2010 University of Rochester

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
import java.util.List;

import edu.ur.ir.NoIndexFoundException;

/**
 * Service to allow user groups to be indexed.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserGroupIndexService {
	
	/**
	 * Add the institutional collection to the index.
	 * 
	 * @param userGroup - user group to add.
	 * @param userGroupIndexFolder - folder that holds the user group index.
	 */
	public void addToIndex(IrUserGroup userGroup, File userGroupIndexFolder) throws NoIndexFoundException;
	
	/**
	 * Update the user group in the index.
	 * 
	 * @param userGroup - userGroup to add.
	 * @param userGroupIndexFolder - folder which holds the user groups.
	 */
	public void updateIndex(IrUserGroup userGroup, File userGroupIndexFolder) throws NoIndexFoundException;
	
	/**
	 * Delete the user group in the index.
	 * 
	 * @param userGroupId - id of the user
	 * @param userIndexFolder  - folder location of the collection index
	 */
	public void deleteFromIndex(Long userGroupId, File userGroupIndexFolder);
	
	/**
	 * Re-index the specified collections.  This can be used to re-index 
	 * all user groups
	 * 
	 * @param userGroups - user groups to re index
	 * @param userGroupIndexFolder - folder location of the index
	 * @param overwriteExistingIndex - if set to true, will overwrite the exiting index.
	 */
	public void add(List<IrUserGroup> userGroups, File userGroupIndexFolder,
			boolean overwriteExistingIndex);
	
	/**
	 * Optimize the specified user group index.
	 * 
	 * @param userGroupIndex
	 */
	public void optimize(File userGroupIndex);

}
