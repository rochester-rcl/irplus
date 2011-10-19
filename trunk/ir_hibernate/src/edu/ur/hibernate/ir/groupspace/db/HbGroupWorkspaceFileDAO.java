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
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileDAO;

/**
 * Group workspace data access for file data.
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceFileDAO implements GroupWorkspaceFileDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -8181551931356055781L;
	
	/** hibernate helper  */
	private final HbCrudDAO<GroupWorkspaceFile> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceFileDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceFile>(GroupWorkspaceFile.class);
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
	public GroupWorkspaceFile getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}
	
	/**
	 * Get personal files for a group workspace in the specified folder
	 * 
	 * @param workspaceId Id of the user having the files
	 * @param parentFolderId Id of the folder contaiing the files
	 * 
	 * @return List of files in the folder
	 */
	 @SuppressWarnings("unchecked")
	public List<GroupWorkspaceFile> getFiles(Long workspaceId, Long parentFolderId)
	{
		 Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getFilesInAFolderForGroupWorkspace");
		 q.setParameter("workspaceId", workspaceId);
		 q.setParameter("folderId", parentFolderId );
		 return q.list();
	}
	 
    /**
	  * Get all files for the workspace.
	  * 
	  * @param groupWorkspaceId - id of the group workspace
	  * @return all the files within the group workspace
	  */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFile> getAllFiles(Long groupWorkspaceId)
	{
		 Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllFilesInGroupWorkspace");
		 q.setParameter("workspaceId", groupWorkspaceId);
		 return q.list();
	}
	
	/**
	  * Get the root files at the top workspace level
	  * 
	  * @param workspaceId - workspace to get the root files from
	  * 
	  * @return the found files
	  */
	 @SuppressWarnings("unchecked")
	public List<GroupWorkspaceFile> getRootFiles(Long workspaceId)
	 {
		 Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getRootWorkspaceFiles");
		 q.setParameter("workspaceId", workspaceId);
		 return q.list();
		 
	 }

	/**
	 * Make the group folder persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceFile entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the group folder transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceFile entity) {
		hbCrudDAO.makeTransient(entity);
	}
	


}
