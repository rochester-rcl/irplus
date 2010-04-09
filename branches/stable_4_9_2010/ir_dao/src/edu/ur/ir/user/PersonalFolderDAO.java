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
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.user.PersonalFolder;

/**
 * Interface for persisting folders
 * 
 * @author Nathan Sarr
 */
public interface PersonalFolderDAO extends CountableDAO, 
CrudDAO<PersonalFolder>
{
	/**
	 * Find the root folder for the specified folder name and 
	 * user id.
	 * 
	 * @param name of the folder
	 * @param userId id of the user
	 * @return the found root folder or null if the folder is not found.
	 */
	public PersonalFolder getRootPersonalFolder(String name, Long userId);
	
	/**
	 * Find the folder for the specified folder name and 
	 * parent folder id.
	 * 
	 * @param name of the folder
	 * @param parentId id of the parent folder
	 * @return the found folder or null if the folder is not found.
	 */
	public PersonalFolder getPersonalFolder(String name, Long parentId);
	
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
	public List<PersonalFolder> getNodesLeftRightGreaterEqual(Long value, Long rootId);

	/**
	 * Get all nodes not within the specified tree.
	 * 
	 * @param personalFolder
	 * @return all nodes not within the specified tree.
	 */
	public List<PersonalFolder> getAllNodesNotInChildFolder(PersonalFolder personalFolder);
	
	/**
	 * Get all nodes not within the specified trees.  This helps with selecting
	 * folders users can move other folders to.  This only works on a single root
	 * folder at a time and assumes the list of personal folders all belon to the same 
	 * root folder.
	 * 
	 * @param personalFolders - list of folders that folders cannot be in
	 * @param userId - owner of the folders
	 * @param rootFolderId - the id of the root folder 
	 * 
	 * 
	 * @return all nodes not within the specified folder trees - including themselves.
	 */
	public List<PersonalFolder> getAllFoldersNotInChildFolders(List<PersonalFolder> folders, 
			Long userId, Long rootFolderId);
	
	/**
	 * Get all folder for the specified user.
	 * 
	 * @param userId id of the repository
	 * @return all folders for the specified user.
	 */
	public List<PersonalFolder> getAllPersonalFoldersForUser(Long userId);
	
	/**
	 * This returns all personal files for the specified parent folder.  This
	 * includes files in sub folders.
	 * 
	 * @param personalFolder
	 * @return
	 */
	public List<PersonalFile> getAllFilesForFolder(PersonalFolder personalFolder);
	
	/**
	 * This returns all versioned files for the specified parent folder.  This
	 * includes versioned files in sub folders.
	 * 
	 * @param personalFolder
	 * @return
	 */
	public List<VersionedFile> getAllVersionedFilesForFolder(PersonalFolder personalFolder);
	
	
	/**
	 * Gets the path to the folder starting from the top parent all the way
	 * down to the specified child.  Only includes parents of the specified 
	 * personal folder.  The list is ordered highest level parent and includes the specified 
	 * personal folder.  This is useful for displaying the path to a given child folder.
	 * 
	 * @param personal folder
	 * @return list of parent folders.
	 */
	public List<PersonalFolder> getPath(PersonalFolder personalFolder);
	


	/**
	 * Get all other root folders for the user except the ones with the specified id's
	 * 
	 * @param rootFolderIds - root folders
	 * @param userId - id of the user
	 * 
	 * @return list of root folders.
	 */
	public List<PersonalFolder> getAllOtherRootFolders(final List<Long> rootFolderIds, final Long userId);

	/**
	 * Get the personal folders in the given list with the specified ids.  If the list
	 * of folderIds is empty, no folders are returned.
	 * 
	 * @param userId
	 * @param folderIds
	 * 
	 * @return the found folders
	 */
	public List<PersonalFolder> getFolders(Long userId, List<Long> folderIds);
	
	/**
	 * Get sub folders for the parent folder
	 * 
	 * @param userId Id of the user having the folder 
	 * @param parentFolderId Id of the parent folder
	 * 
	 * @return List of sub folders
	 */
	public List<PersonalFolder> getSubFoldersForFolder(Long userId, Long parentFolderId);
	
	/**
	 * Get root folders 
	 * 
	 * @param userId User holding the files
	 * 
	 * @return List of root folders
	 */
	public List<PersonalFolder> getRootFolders(Long userId);

	/**
	 * Get folder size 
	 * 
	 * @param userId User Id the folder belongs to 
	 * @param folderId Folder id to get the size
	 * 
	 * @return Size of folder
	 */
	public Long getFolderSize(Long userId, Long folderId) ;
}
