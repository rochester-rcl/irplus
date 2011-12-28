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
import edu.ur.ir.user.IrUser;

/**
 * Interface for persisting group folder information
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceFolderDAO extends CrudDAO<GroupWorkspaceFolder>
{
	/**
	 * Find the root folder for the specified folder name and 
	 * group workspace id.
	 * 
	 * @param name of the folder
	 * @param groupWorkspaceId - id of the group workspace
	 * @return the found root folder or null if the folder is not found.
	 */
	public GroupWorkspaceFolder getRootFolder(String name, Long groupWorkspaceId);
	
	/**
	 * Find the folder for the specified folder name and 
	 * group workspace folder id.
	 * 
	 * @param name of the folder
	 * @param parentId id of the parent folder
	 * @return the found folder or null if the folder is not found.
	 */
	public GroupWorkspaceFolder getFolder(String name, Long parentId);
	
	/**
	 * Get the path to the folder.
	 * 
	 * @param parentFolderId - id of the parent folder
	 * 
	 * @return - list of all folders in order - parent to child
	 */
	public List<GroupWorkspaceFolder> getFolderPath(GroupWorkspaceFolder folder);
	
	/**
	 * Get sub folders within parent folder for a group workspace
	 * 
	 * @param workspaceId Id of the group workspace containing the folders
	 * @param parentFolderId Id of the parent folder to start at - can be at any point
	 * in the tree
	 * 
	 * @return List of sub folders within the parent folder
	 */
	public List<GroupWorkspaceFolder> getFolders(Long workspaceId, Long parentFolderId );
	
	/**
	 * Get all the folders for a group workspace
	 * @param workspaceId - id of the group workspace.
	 * 
	 * @return list of group workspace folders
	 */
	public List<GroupWorkspaceFolder> getAllFolders(Long workspaceId);
	
	
	/**
	 * Get the root folders for a given workspace.
	 * 
	 * @param workspaceId - id of the workspace
	 * @return list of folders at the root of the given workspace.
	 */
	public List<GroupWorkspaceFolder> getRootFolders(Long workspaceId);
	
	/**
	 * This returns all personal files for the specified parent folder.  This
	 * includes all files in sub folders.
	 * 
	 * @param groupFolder - group workspace folder to find all files in (including sub folders)
	 * @return all group files found.
	 */
	public List<GroupWorkspaceFile> getAllFilesForFolder(GroupWorkspaceFolder groupFolder);
	
	/**
	 * This returns all group workspace folders for the specified parent folder.  This
	 * includes all folders in sub folders.
	 * 
	 * @param groupFolder - group workspace folder to find all folders in (including sub folders)
	 * @return all group folders found.
	 */
	public List<GroupWorkspaceFolder> getAllFoldersForFolder(GroupWorkspaceFolder groupFolder);
	

	/**
	 * Get all folders for the given user that has the specified permission on the folder.
	 * 
	 * @param user - user to check
	 * @param permission - permission to check
	 * 
	 * @return all folders for which the user has the specified permission.
	 */
	public List<GroupWorkspaceFolder> getAllFoldersUserHasPermissionFor(IrUser user, String permission);
}
