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

package edu.ur.file.db;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.file.db.TreeFolderInfo;
import java.util.List;


public interface TreeFolderInfoDAO extends CountableDAO, 
CrudDAO<TreeFolderInfo>, NameListDAO {
	
	/**
	 * Get the entire tree for the specified node.
	 * 
	 * @param id - id of node we want the tree/subtree for.
	 * 
	 * @param rootId - root id for the entire tree.  This is needed
	 * to isolate the correct tree to locate the node.  This allows 
	 * multiple trees to be stored in the same table
	 * 
	 * @return the tree or null if no tree found
	 */
	public TreeFolderInfo getTree(TreeFolderInfo tree);
	
	/**
	 * Return a list of all nodes that have a left and 
	 * right value greater than or equal to the value specified.
	 * 
	 * @param value
	 * 
	 * @param root for the entire tree.
	 * @return all nodes that have a right or left value greater than or 
	 * equal to the value
	 */
	public List<TreeFolderInfo> getNodesLeftRightGreaterEqual(Long value, Long treeRootId);
	
	/**
	 * Get all nodes except for the node and its children.  This stays
	 * withn it's own file database  
	 * 
	 * @param id - id of node we want the tree/subtree to exclude.
	 * 
	 * @param rootId - root id for the entire tree.  This is needed
	 * to isolate the correct tree when the table holds multiple trees.  
	 * This allows multiple trees to be stored in the same table
	 * 
	 * @return the tree or null if no tree found
	 */
	public List<TreeFolderInfo> getAllNodesNotInChildTree(TreeFolderInfo folder);
	
	/**
	 * Get all folders except for the folder.  If there are multiple 
	 * databases it returns folders from all other databases as well.
	 * 
	 * @param id - id of node we want the tree/subtree to exclude.
	 * 
	 * @param rootId - root id for the entire tree.  This is needed
	 * to isolate the correct tree when the table holds multiple trees.  
	 * This allows multiple trees to be stored in the same table
	 * 
	 * @return the tree or null if no tree found
	 */
	public List<TreeFolderInfo> getAllNodesNotInChildTreeDb(TreeFolderInfo folder);
	
	/**
	 * Get all folders for the tree.
	 * 
	 * @param rootId for the entire tree.
	 * @return the list of folders found or an empty list if no folders fond
	 */
	public List<TreeFolderInfo> getAllFoldersInDatabase(Long databaseId);
	
	/**
	 *  Find a root folder with the name in the file database
	 *
	 * @param name to look for
	 * @param databaseId database to look in 
	 *
	 * @return the tree folder or null if nothing is found 
	 */
	public TreeFolderInfo findRootByDisplayName( String name, Long databaseId);
	
	/**
	 *  Find a folder with the name in the file database for the specified folder
	 *
	 * @param name to look for
	 * @param folderId folder to look in 
	 *
	 * @return the folder or null if nothing is found 
	 */
	public TreeFolderInfo findByDisplayName( String name, Long parentFolderId);
	
	/**
	 * Get the number of files in this folder
	 * 
	 * @param id - of the folder
	 * 
	 * @return number of files in the folder
	 */
	public Long getFileCount(Long id);
	
	/** 
     * Find a folders by it's unique name
     * 
     * @param name - name of the folder.
     * @return the folder
	 */
	public TreeFolderInfo findByUniqueName(String name);
}
