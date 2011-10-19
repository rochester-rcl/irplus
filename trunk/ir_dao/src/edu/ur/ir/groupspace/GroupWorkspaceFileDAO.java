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

import java.util.List;

import edu.ur.dao.CrudDAO;

/**
 * @author Nathan Sarr
 * 
 * Represents a group file data access object
 */
public interface GroupWorkspaceFileDAO extends CrudDAO<GroupWorkspaceFile>
{
	/**
	 * Get personal files for a group workspace in the specified folder
	 * 
	 * @param workspaceId Id of the user having the files
	 * @param parentFolderId Id of the folder contaiing the files
	 * 
	 * @return List of files in the folder
	 */
	 public List<GroupWorkspaceFile> getFiles(Long workspaceId, Long parentFolderId);
	 
	/**
	  * Get the root files 
	  * 
	  * @param workspaceId - get the root files
	  * 
	  * @return the found files
	  */
	 public List<GroupWorkspaceFile> getRootFiles(Long workspaceId);
	 
	/**
	  * Get all files for the workspace.
	  * 
	  * @param groupWorkspaceId - id of the group workspace
	  * @return all the files within the group workspace
	  */
	public List<GroupWorkspaceFile> getAllFiles(Long groupWorkspaceId);
}
