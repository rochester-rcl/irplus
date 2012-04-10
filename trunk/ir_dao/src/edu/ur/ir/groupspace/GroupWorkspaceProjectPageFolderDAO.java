/**  
   Copyright 2008 - 2012 University of Rochester

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

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.file.IrFile;

/**
 * Interface for persisting researcher folders
 * 
 * @author Nathan Sarr
 */
public interface GroupWorkspaceProjectPageFolderDAO extends CountableDAO, 
CrudDAO<GroupWorkspaceProjectPageFolder>
{
	/**
	 * Find the root folder for the specified folder name and 
	 * group workspace project page id.
	 * 
	 * @param name of the folder
	 * @param groupWorkspaceProjectPageId id of the project page
	 * @return the found root folder or null if the folder is not found.
	 */
	public GroupWorkspaceProjectPageFolder getRootFolder(String name, Long groupWorkspaceProjectPageId);
	
	/**
	 * Find the folder for the specified folder name and 
	 * parent folder id.
	 * 
	 * @param name of the folder
	 * @param parentId id of the parent folder
	 * @return the found folder or null if the folder is not found.
	 */
	public GroupWorkspaceProjectPageFolder getFolder(String name, Long parentId);
	
	/**
	 * Get all nodes that have a right and left value grater than the
	 * specified value.  This is for pre-loading all nodes that are to
	 * the right of the specified node.  This can help when re-numbering 
	 * nodes when a new node is added.  This returns nodes only in the
	 * specified root tree. 
	 * 
	 * @param value the left or right value of the node must be grater than this
	 * @param rootId tree to look in
	 * @return the list of nodes found.
	 */
	public List<GroupWorkspaceProjectPageFolder> getNodesLeftRightGreaterEqual(Long value, Long rootId);

	/**
	 * Get all nodes not within the specified tree.
	 * 
	 * @param folder
	 * @return all nodes not within the specified tree.
	 */
	public List<GroupWorkspaceProjectPageFolder> getAllNodesNotInChildFolder(GroupWorkspaceProjectPageFolder folder);
	
	/**
	 * Get all nodes not within the specified trees.  This helps with selecting
	 * folders researchers can move other folders to.  This only works on a single root
	 * folder at a time and assumes the list of researcher folders all belon to the same 
	 * root folder.
	 * 
	 * @param researcherFolders - list of folders that folders cannot be in
	 * @param groupWorkspaceProjectPageId - owner of the folders
	 * @param rootFolderId - the id of the root folder 
	 * 
	 * 
	 * @return all nodes not within the specified folder trees - including themselves.
	 */
	public List<GroupWorkspaceProjectPageFolder> getAllFoldersNotInChildFolders(List<GroupWorkspaceProjectPageFolder> folders, 
			Long groupWorkspaceProjectPageId, Long rootFolderId);
	
	/**
	 * Get all folder for the specified group workspace project page.
	 * 
	 * @param groupWorkspaceProjectPageId id of the group workspace project page
	 * @return all folders for the specified group workspace.
	 */
	public List<GroupWorkspaceProjectPageFolder> getAllFolders(Long groupWorkspaceProjectPageId);
	
	/**
	 * This returns all group workspace project page files for the specified parent folder.  This
	 * includes files in sub folders.
	 * 
	 * @param folder
	 * @return list of all files within the folder including sub folders
	 */
	public List<GroupWorkspaceProjectPageFile> getAllFiles(GroupWorkspaceProjectPageFolder folder);

	/**
	 * This returns all group workspace project page publications for the specified parent folder.  This
	 * includes files in sub folders.
	 * 
	 * @param folder
	 * @return list of all publications found
	 */
	public List<GroupWorkspaceProjectPagePublication> getAllPublications(GroupWorkspaceProjectPageFolder folder);

	/**
	 * This returns all links for the specified parent folder.  This
	 * includes files in sub folders.
	 * 
	 * @param folder
	 * @return all links found in the folder and sub folders
	 */
	public List<GroupWorkspaceProjectPageFileSystemLink> getAllLinks(GroupWorkspaceProjectPageFolder researcherFolder);

	/**
	 * This returns all ir files for the specified parent folder.  This
	 * includes ir files in sub folders.
	 * 
	 * @param folder
	 * @return all ir files found
	 */
	public List<IrFile> getAllIrFiles(GroupWorkspaceProjectPageFolder folder);
	
	
	/**
	 * Gets the path to the folder starting from the top parent all the way
	 * down to the specified child.  Only includes parents of the specified 
	 * researcher folder.  The list is ordered highest level parent and includes the specified 
	 * researcher folder.  This is useful for displaying the path to a given child folder.
	 * 
	 * @param researcher folder
	 * @return list of parent folders.
	 */
	public List<GroupWorkspaceProjectPageFolder> getPath(GroupWorkspaceProjectPageFolder researcherFolder);
	
	/**
	 * Get all other root folders for the researcher except the ones with the specified id's
	 * 
	 * @param rootFolderIds - root folders
	 * @param researcherId - id of the researcher
	 * 
	 * @return list of root folders.
	 */
	public List<GroupWorkspaceProjectPageFolder> getAllOtherRootFolders(final List<Long> rootFolderIds, final Long researcherId);

	/**
	 * Get the researcher folders in the given list with the specified ids.  If the list
	 * of folderIds is empty, no folders are returned.
	 * 
	 * @param researcherId
	 * @param folderIds
	 * 
	 * @return the found folders
	 */
	public List<GroupWorkspaceProjectPageFolder> getFolders(Long researcherId, List<Long> folderIds);
	
	/**
	 * Get sub folders for the parent folder
	 * 
	 * @param researcherId Id of the researcher having the folder 
	 * @param parentFolderId Id of the parent folder
	 * 
	 * @return List of sub folders
	 */
	public List<GroupWorkspaceProjectPageFolder> getSubFoldersForFolder(Long researcherId, Long parentFolderId);
	
	/**
	 * Get root folders 
	 * 
	 * @param researcherId researcher holding the files
	 * 
	 * @return List of root folders
	 */
	public List<GroupWorkspaceProjectPageFolder> getRootFolders(Long researcherId);

	/**
	 * This returns all institutional items for the specified parent folder.  This
	 * includes all institutional items in sub folders.
	 * 
	 * @param researcherFolder
	 * @return researcher institutional items found.
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getAllInstitutionalItemsForFolder(GroupWorkspaceProjectPageFolder researcherFolder);

}
