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

package edu.ur.ir.groupspace.service;

import java.util.List;

import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceFolderDAO;

/**
 * Group workspace file system service to allow for the management and
 * retrieval for group workspace file system information.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGroupWorkspaceFileSystemService implements GroupWorkspaceFileSystemService {
	
	/* eclipse generated id */
	private static final long serialVersionUID = -1638366740720252837L;

	/* Group workspace folder data access object. */
	private GroupWorkspaceFolderDAO groupWorkspaceFolderDAO;

	/* Group workspace file data access object */
	private GroupWorkspaceFileDAO groupWorkspaceFileDAO;
		
	/**
	 * Get the group workspace folder.
	 * 
	 * @param id - id of the group workspace folder
 	 * @param lock - if set to true upgrade the lock mode.
 	 * 
	 * @return - the group workspace folder
	 */
	public GroupWorkspaceFolder getFolder(Long id, boolean lock)
	{
		return groupWorkspaceFolderDAO.getById(id, lock);
	}
	
	/**
	 * Get the path to the folder.
	 * 
	 * @param parentFolderId - id of the parent folder
	 * 
	 * @return - list of all folders in order - parent to child
	 */
	public List<GroupWorkspaceFolder> getFolderPath(GroupWorkspaceFolder folder)
	{
		return groupWorkspaceFolderDAO.getFolderPath(folder);
	}
	
	/**
	 * Get sub folders within parent folder for a group workspace
	 * 
	 * @param workspaceId Id of the group workspace containing the folders
	 * @param parentFolderId Id of the parent folder to start at - can be at any point
	 * in the tree
	 * 
	 * @return List of sub folders within the parent folder
	 */
	public List<GroupWorkspaceFolder> getFolders(Long workspaceId, Long parentFolderId )
	{
		return groupWorkspaceFolderDAO.getFolders(workspaceId, parentFolderId);
	}

	/**
	 * Get personal files for a group workspace in the specified folder.  Using the root folder
	 * id indicates Group workspace files
	 * 
	 * @param workspaceId Id of the user having the files
	 * @param parentFolderId Id of the folder containing the files
	 * 
	 * @return List of files in the folder
	 */
	public List<GroupWorkspaceFile> getFiles(Long workspaceId, Long parentFolderId)
	{
		if(  parentFolderId == null || parentFolderId == ROOT_FOLDER_ID )
		{
		    return groupWorkspaceFileDAO.getRootFiles(workspaceId);
		}
		else
		{
		    return groupWorkspaceFileDAO.getFiles(workspaceId, parentFolderId);
		}
	}
	
	/**
	 * Set the group workspace folder data access object.
	 * 
	 * @param groupWorkspaceFolderDAO
	 */
	public void setGroupWorkspaceFolderDAO(
			GroupWorkspaceFolderDAO groupWorkspaceFolderDAO) {
		this.groupWorkspaceFolderDAO = groupWorkspaceFolderDAO;
	}

	/**
	 * Set the group workspace file data access object.
	 * 
	 * @param groupWorkspaceFileDAO
	 */
	public void setGroupWorkspaceFileDAO(GroupWorkspaceFileDAO groupWorkspaceFileDAO) {
		this.groupWorkspaceFileDAO = groupWorkspaceFileDAO;
	}

}
