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

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileDAO;
import edu.ur.ir.user.IrUser;

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
	
	/**
	 * Get all files for the given user that has the specified permission on the file.
	 * 
	 * @param user - user to check
	 * @param permission - permission to check
	 * 
	 * @return all files for which the user has the specified permission.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFile> getAllFilesUserHasPermissionFor(
			IrUser user, String permission) {
		Query q1 = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllFilesUserHasPermissionFor");
		q1.setParameter("className", VersionedFile.class.getName());
		q1.setParameter("permissionName", permission);
		q1.setParameter("userId", user.getId());
		List<GroupWorkspaceFile> files = (List<GroupWorkspaceFile>) q1.list();
		return files;
	}

	/**
	 * Get the files for group workspace id and listed file ids.  If the list of fileIds 
	 * is null no files are returned.
	 * 
	 * @param groupWorkspaceId - id of the group workspace to look in
	 * @param fileIds - list of file ids within the group workspace
	 * 
	 * @return the found files
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFile> getFiles(Long groupWorkspaceId, List<Long> fileIds) {
		
		if( fileIds.size() > 0 )
		{
		    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceFiles");
		    q.setParameter("groupWorkspaceId", groupWorkspaceId);
		    q.setParameterList("fileIds", fileIds);
		    return q.list();
		}
		else
		{
			return new LinkedList<GroupWorkspaceFile>();
		}
	}

	
	/**
	 * Get a count of files with the specified ir file id.
	 * 
	 * @see edu.ur.ir.groupspace.GroupWorkspaceFileDAO#getFileCount(java.lang.Long)
	 */
	public Long getFileCount(Long irFileId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceFilesWithIrFileId");
		q.setLong("irFileId", irFileId);
		return(Long) q.uniqueResult();
	}
	
	/**
	 * Get the files for group workspace id and versioned file id .
	 * 
	 * @param groupWorkspaceId
	 * @param versionedFileId
	 * 
	 * @return the found file
	 */
	public GroupWorkspaceFile getGroupWorkspaceFileWithVersionedFile(Long groupWorkspaceId, Long versionedFileId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceFileWithVersionedFileId");
		q.setLong("groupWorkspaceId", groupWorkspaceId);
		q.setLong("versionedFileId", versionedFileId);
		return(GroupWorkspaceFile) q.uniqueResult();
		
	}


}
