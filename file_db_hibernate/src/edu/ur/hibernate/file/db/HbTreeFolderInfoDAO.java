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

package edu.ur.hibernate.file.db;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.file.db.TreeFolderInfoDAO;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;

/**
 * Hibernate data access for saving folders
 * 
 * @author Nathan Sarr
 *
 */
public class HbTreeFolderInfoDAO extends HbCrudDAO<TreeFolderInfo> 
 implements TreeFolderInfoDAO {
	
	/**
	 * Default Constructor
	 */
	public HbTreeFolderInfoDAO() {
		super(TreeFolderInfo.class);
	}
    
    /**
     * Return all folders orderd by Name.  This list 
     * can be extreamly large and it is recomened that
     * paging is used instead
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TreeFolderInfo> getAllNameOrder()
    {
    	DetachedCriteria dc = DetachedCriteria.forClass(TreeFolderInfo.class);
    	dc.addOrder(Order.asc("displayName"));
    	return (List<TreeFolderInfo>) hibernateTemplate.findByCriteria(dc);
    }
    
    /**
     * Get the number of folders in the system
     * 
     * @return the number of folders
     */
    public Long getCount()
    {
    	return (Long)HbHelper.getUnique(hibernateTemplate.findByNamedQuery("folderCount"));
    }
    
    /**
     * Get all folders starting at the start record and get up to 
     * the numRecords - it will be ordered by display name
     *  
     * @param startRecord - the index to start at
     * @param numRecords - the number of records to get
     * @return the records found
     */
    public List<TreeFolderInfo> getAllOrderByName(int startRecord, int numRecords)
    {
    	return getByQuery("getTreeFolderInfoByDisplayName", startRecord, numRecords);
    }

	/** 
     * Find a set of folders by their display name
     * 
     * @param name - name of the folder.
     * @return the list of folders found with the specified name.
	 */
    @SuppressWarnings("unchecked")
	public List<TreeFolderInfo> findByName(String name) {
    	return (List<TreeFolderInfo>) hibernateTemplate.findByNamedQuery("getTreeFolderInfoByDisplayName", name);
	}

	/** 
     * Find a folders by it's unique name
     * 
     * @param name - name of the folder.
     * @return the folder
	 */
	public TreeFolderInfo findByUniqueName(String name) {
		return (TreeFolderInfo) 
	    HbHelper.getUnique(hibernateTemplate.findByNamedQuery("getTreeFolderInfoByFolderName", name));
	}
	
	/**
	 * Load the entire tree for the selected node.  Only returns the 
	 * root tree.
	 * 
	 * @param id of the parent node we want the tree for.
	 * @param rootId the root of the tree - this is for a table that
	 *    has multiple trees in it.
	 * 
	 * @return the node and all it's children
	 */
	@SuppressWarnings("unchecked")
	public TreeFolderInfo getTree(TreeFolderInfo tree)
	{
		Long[] ids = new Long[] {tree.getLeftValue(), tree.getRightValue(), tree.getTreeRoot().getId()};
		
		List<TreeFolderInfo> listTree =  (List<TreeFolderInfo>) hibernateTemplate.findByNamedQuery("getTreeForNode", ids);
	    for(TreeFolderInfo t : listTree)
	    {
	    	if(t.getId().equals(tree.getId()))
	    	{
	    		return t;
	    	}
	    }
	    
	    return null;
	}

	/**
	 * Get all nodes who have a left or right value greater than the specified value in 
	 * the tree with the specified root id.
	 * 
	 * @see edu.ur.file.db.TreeFolderInfoDAO#getNodesLeftRightGreaterEqual(edu.ur.file.db.TreeFolderInfo)
	 */
	@SuppressWarnings("unchecked")
	public List<TreeFolderInfo> getNodesLeftRightGreaterEqual(Long value, Long treeRootId) {
		 Long[] values = new Long[] {value, value, treeRootId};
		 return (List<TreeFolderInfo>) hibernateTemplate.findByNamedQuery("getAllNodesValueGreaterEqual", values);
	}
	
	/**
	 * Gets all folders not within the specified tree.
	 * 
	 * @see edu.ur.file.db.TreeFolderInfoDAO#getAllNodesNotInChildTree(edu.ur.file.db.TreeFolderInfo)
	 */
	@SuppressWarnings("unchecked")
	public List<TreeFolderInfo> getAllNodesNotInChildTree(TreeFolderInfo tree)
	{
		Long[] values = new Long[] {tree.getLeftValue(), 
		    		tree.getRightValue(), tree.getTreeRoot().getId()};
		
		return (List<TreeFolderInfo>) hibernateTemplate.findByNamedQuery("getAllNodesNotInChildTree", values);
	}
	
	/**
	 * 
	 * @see edu.ur.file.db.TreeFolderInfoDAO#getAllNodesNotInChildTreeDb(edu.ur.file.db.TreeFolderInfo)
	 */
	@SuppressWarnings("unchecked")
	public List<TreeFolderInfo> getAllNodesNotInChildTreeDb(TreeFolderInfo folder) {
		Long[] ids = new Long[] {folder.getLeftValue(), folder.getRightValue(), 
				folder.getTreeRoot().getId(), folder.getFileDatabase().getId(),
				folder.getFileDatabase().getId(), folder.getTreeRoot().getId()};
		List<TreeFolderInfo> listTree =  (List<TreeFolderInfo>) hibernateTemplate.findByNamedQuery("getAllNodesNotInChildTreeDb", ids);
		return listTree;
	}

	/**
	 * @see edu.ur.file.db.TreeFolderInfoDAO#getAllFoldersInDatabase(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<TreeFolderInfo> getAllFoldersInDatabase(Long databaseId) {
		List<TreeFolderInfo> listTree =  (List<TreeFolderInfo>) hibernateTemplate.findByNamedQuery("getFoldersInDatabase", databaseId);
		return listTree;
	}
	
	/**
	 * 
	 * @see edu.ur.file.db.TreeFolderInfoDAO#findRootByDisplayName(java.lang.String, java.lang.Long)
	 */
	public TreeFolderInfo findRootByDisplayName( String name, Long databaseId)
	{
		Object[] values = {name, databaseId};
		TreeFolderInfo folder = (TreeFolderInfo) HbHelper.getUnique(hibernateTemplate.findByNamedQuery("findRootByDisplayName", values));
		return folder;
	}
	
	/**
	 * 
	 * @see edu.ur.file.db.TreeFolderInfoDAO#findByDisplayName(java.lang.String, java.lang.Long)
	 */
	public TreeFolderInfo findByDisplayName( String name, Long parentFolderId)
	{
		Object[] values = {name, parentFolderId};
		TreeFolderInfo folder = (TreeFolderInfo) HbHelper.getUnique(hibernateTemplate.findByNamedQuery("findByDisplayName", values));
		return folder;
	}

	/* (non-Javadoc)
	 * @see edu.ur.file.db.TreeFolderInfoDAO#getFileCount(java.lang.Long)
	 */
	public Long getFileCount(Long id) {
		return (Long) HbHelper.getUnique(hibernateTemplate.findByNamedQuery("getFolderFileCount", id));
	}
}
