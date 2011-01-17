/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.groupspace;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import edu.ur.ir.NoIndexFoundException;

/**
 * Interface to index group workspaces.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceIndexService extends Serializable{

	/**
	 * Add the group workspace to the index.
	 * 
	 * @param collection - group workspace to add.
	 * @param indexFolder - folder that holds the group workspace index.
	 */
	public void add(GroupWorkspace groupWorkspace, File indexFolder) throws NoIndexFoundException;
	
	/**
	 * Update the group workspace in the index.
	 * 
	 * @param groupWorkspace - group workspace to add.
	 * @param indexFolder - folder which holds the group workspace index.
	 */
	public void update(GroupWorkspace groupWorkspace, File indexFolder) throws NoIndexFoundException;
	
	/**
	 * Delete the group workspace in the index.
	 * 
	 * @param groupWorkspaceId - id of the group workspace
	 * @param indexFolder  - folder location of the index
	 */
	public void delete(Long groupWorkspaceId, File indexFolder);
	
	/**
	 * Re-index the specified workspace.  This can be used to re-index 
	 * all workspaces
	 * 
	 * @param groupWorkspace - group workspace to re index
	 * @param indexFolder - folder location of the index
	 * @param overwriteExistingIndex - if set to true, will overwrite the existing index.
	 */
	public void add(List<GroupWorkspace> groupWorkspaces, File indexFolder,
			boolean overwriteExistingIndex);
	
	/**
	 * Optimize the specified group index.
	 * 
	 * @param index
	 */
	public void optimize(File index);

}
