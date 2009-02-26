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

package edu.ur.ir.researcher;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.file.IrFile;

/**
 * Interface for persisting researcher folders
 * 
 * @author Sharmila Ranganathan
 */
public interface ResearcherFolderDAO extends CountableDAO, 
CrudDAO<ResearcherFolder>
{
	/**
	 * Find the root folder for the specified folder name and 
	 * researcher id.
	 * 
	 * @param name of the folder
	 * @param researcherId id of the researcher
	 * @return the found root folder or null if the folder is not found.
	 */
	public ResearcherFolder getRootResearcherFolder(String name, Long researcherId);
	
	/**
	 * Find the folder for the specified folder name and 
	 * parent folder id.
	 * 
	 * @param name of the folder
	 * @param parentId id of the parent folder
	 * @return the found folder or null if the folder is not found.
	 */
	public ResearcherFolder getResearcherFolder(String name, Long parentId);
	
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
	public List<ResearcherFolder> getNodesLeftRightGreaterEqual(Long value, Long rootId);

	/**
	 * Get all nodes not within the specified tree.
	 * 
	 * @param researcherFolder
	 * @return all nodes not within the specified tree.
	 */
	public List<ResearcherFolder> getAllNodesNotInChildFolder(ResearcherFolder researcherFolder);
	
	/**
	 * Get all nodes not within the specified trees.  This helps with selecting
	 * folders researchers can move other folders to.  This only works on a single root
	 * folder at a time and assumes the list of researcher folders all belon to the same 
	 * root folder.
	 * 
	 * @param researcherFolders - list of folders that folders cannot be in
	 * @param researcherId - owner of the folders
	 * @param rootFolderId - the id of the root folder 
	 * 
	 * 
	 * @return all nodes not within the specified folder trees - including themselves.
	 */
	public List<ResearcherFolder> getAllFoldersNotInChildFolders(List<ResearcherFolder> folders, 
			Long researcherId, Long rootFolderId);
	
	/**
	 * Get all folder for the specified researcher.
	 * 
	 * @param researcherId id of the repository
	 * @return all folders for the specified researcher.
	 */
	public List<ResearcherFolder> getAllResearcherFoldersForResearcher(Long researcherId);
	
	/**
	 * This returns all researcher files for the specified parent folder.  This
	 * includes files in sub folders.
	 * 
	 * @param researcherFolder
	 * @return
	 */
	public List<ResearcherFile> getAllFilesForFolder(ResearcherFolder researcherFolder);

	/**
	 * This returns all researcher files for the specified parent folder.  This
	 * includes files in sub folders.
	 * 
	 * @param researcherFolder
	 * @return
	 */
	public List<ResearcherPublication> getAllPublicationsForFolder(ResearcherFolder researcherFolder);

	/**
	 * This returns all researcher files for the specified parent folder.  This
	 * includes files in sub folders.
	 * 
	 * @param researcherFolder
	 * @return
	 */
	public List<ResearcherLink> getAllLinksForFolder(ResearcherFolder researcherFolder);

	/**
	 * This returns all ir files for the specified parent folder.  This
	 * includes ir files in sub folders.
	 * 
	 * @param researcherFolder
	 * @return
	 */
	public List<IrFile> getAllIrFilesForFolder(ResearcherFolder researcherFolder);
	
	
	/**
	 * Gets the path to the folder starting from the top parent all the way
	 * down to the specified child.  Only includes parents of the specified 
	 * researcher folder.  The list is ordered highest level parent and includes the specified 
	 * researcher folder.  This is useful for displaying the path to a given child folder.
	 * 
	 * @param researcher folder
	 * @return list of parent folders.
	 */
	public List<ResearcherFolder> getPath(ResearcherFolder researcherFolder);
	
	/**
	 * Get all other root folders for the researcher except the ones with the specified id's
	 * 
	 * @param rootFolderIds - root folders
	 * @param researcherId - id of the researcher
	 * 
	 * @return list of root folders.
	 */
	public List<ResearcherFolder> getAllOtherRootFolders(final List<Long> rootFolderIds, final Long researcherId);

	/**
	 * Get the researcher folders in the given list with the specified ids.  If the list
	 * of folderIds is empty, no folders are returned.
	 * 
	 * @param researcherId
	 * @param folderIds
	 * 
	 * @return the found folders
	 */
	public List<ResearcherFolder> getFolders(Long researcherId, List<Long> folderIds);
	
	/**
	 * Get sub folders for the parent folder
	 * 
	 * @param researcherId Id of the researcher having the folder 
	 * @param parentFolderId Id of the parent folder
	 * 
	 * @return List of sub folders
	 */
	public List<ResearcherFolder> getSubFoldersForFolder(Long researcherId, Long parentFolderId);
	
	/**
	 * Get root folders 
	 * 
	 * @param researcherId researcher holding the files
	 * 
	 * @return List of root folders
	 */
	public List<ResearcherFolder> getRootFolders(Long researcherId);

	/**
	 * This returns all institutional items for the specified parent folder.  This
	 * includes all institutional items in sub folders.
	 * 
	 * @param researcherFolder
	 * @return researcher institutional items found.
	 */
	public List<ResearcherInstitutionalItem> getAllInstitutionalItemsForFolder(ResearcherFolder researcherFolder);
}
