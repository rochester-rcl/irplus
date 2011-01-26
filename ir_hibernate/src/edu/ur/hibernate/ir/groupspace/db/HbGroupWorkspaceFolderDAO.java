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

package edu.ur.hibernate.ir.groupspace.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceFolderDAO;

/**
 * Persistent storage for group folder data.
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceFolderDAO implements GroupWorkspaceFolderDAO{

	/** eclipse generated id*/
	private static final long serialVersionUID = 7151402796946586912L;
	
	/** hibernate helper  */
	private final HbCrudDAO<GroupWorkspaceFolder> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceFolderDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceFolder>(GroupWorkspaceFolder.class);
	}
	
	/**
	 * Set the session factory.  
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
    {
        hbCrudDAO.setSessionFactory(sessionFactory);
    }

	/**
	 * Get the group folder by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public GroupWorkspaceFolder getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}
	
	/**
	 * Get the path to the folder.
	 * 
	 * @param parentFolderId - id of the parent folder
	 * 
	 * @return - list of all folders in order - parent to child
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFolder> getFolderPath(GroupWorkspaceFolder folder)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceFolderPath");
		q.setParameter("leftValue", folder.getLeftValue());
		q.setParameter("rootId",folder.getTreeRoot().getId());
		q.setParameter("userId", folder.getOwner().getId());
		return q.list();
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
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFolder> getFolders(Long workspaceId, Long parentFolderId )
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getWorkspaceFolders");
		q.setParameter("workspaceId", workspaceId);
		q.setParameter("parentId", parentFolderId);
		return q.list();
	}

	/**
	 * Make the group folder persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceFolder entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the group folder transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceFolder entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
