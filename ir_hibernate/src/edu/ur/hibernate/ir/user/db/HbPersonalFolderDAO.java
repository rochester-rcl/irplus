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

package edu.ur.hibernate.ir.user.db;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalFolderDAO;

/**
 * Hibernate implementation of persisting a personal folder.
 * 
 * @author Nathan Sarr
 *
 */
public class HbPersonalFolderDAO implements PersonalFolderDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 1529661878676331576L;


	/** helper for dealing with database */
	private final HbCrudDAO<PersonalFolder> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbPersonalFolderDAO() {
		hbCrudDAO = new HbCrudDAO<PersonalFolder>(PersonalFolder.class);
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
	 * Get a count of the folders
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("personalFolderCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Find the folder for the specified userId and specified 
	 * folder name.
	 * 
	 * @param name of the collection
	 * @param userId - id of the user
	 * @return the found collection or null if the collection is not found.
	 */
	public PersonalFolder getRootPersonalFolder(String name, Long userId)
	{
		Object[] values = new Object[] {name, userId};
		return (PersonalFolder) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRootPersonalFolderByNameUser", 
				values));
	}
	
	/**
	 * Find the personal folder for the specified folder name and 
	 * parent id.
	 * 
	 * @param name of the folder
	 * @param parentId id of the parent folder
	 * @return the found folder or null if the folder is not found.
	 */
	public PersonalFolder getPersonalFolder(String name, Long parentId)
	{
		Object[] values = new Object[] {name, parentId};
		return (PersonalFolder) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalFolderByNameParent", 
				values));
	}
	
	/**
	 * 
	 * @see edu.ur.ir.user.PersonalFolderDAO#getNodesLeftRightGreaterEqual(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFolder> getNodesLeftRightGreaterEqual(Long value, Long rootId) {
		Long[] values = new Long[] {value, value, rootId};
		return (List<PersonalFolder>) 
		hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllPersonalFoldersValueGreaterEqual", values);
	}
	
	/**
	 * @see edu.ur.ir.user.PersonalFolderDAO#getAllNodesNotInChildTree(edu.ur.ir.repository.ResearcherFolder)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFolder> getAllNodesNotInChildFolder(PersonalFolder personalFolder)
	{
		Long[] ids = new Long[] {personalFolder.getOwner().getId(),
				personalFolder.getTreeRoot().getId(),
				personalFolder.getLeftValue(),
				personalFolder.getRightValue()};
		List<PersonalFolder> listTree =  
			(List<PersonalFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllPersonalFoldersNotInChildTree", ids);
		return listTree;
	}
	
	/**
	 * @see edu.ur.ir.user.PersonalFolderDAO#getAllPersonalFoldersForUser(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFolder> getAllPersonalFoldersForUser(Long userId) {
		List<PersonalFolder> collections =  
			(List<PersonalFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalFoldersForUser", 
				userId);
		return collections;
	}
	
	/**
	 * Get sub folders for the parent folder
	 * 
	 * @see edu.ur.ir.user.PersonalFolderDAO#getSubFoldersForFolder(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFolder> getSubFoldersForFolder(Long userId, Long parentFolderId) 
	{		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getPersonalFoldersForFolder");
		q.setParameter("userId", userId);
		q.setParameter("parentId", parentFolderId);
		return q.list();
	}
	
	/**
	 * Get root folders 
	 * 
	 * @see edu.ur.ir.user.PersonalFolderDAO#getRootFolders(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFolder> getRootFolders(Long userId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getPersonalRootFolders");
		q.setParameter("userId", userId);
		return q.list();
	}

	/**
	 * Get folder size 
	 * 
	 * @see edu.ur.ir.user.PersonalFolderDAO#getFolderSize(Long, Long)
	 */
	public Long getFolderSize(Long userId, Long folderId) {
		
		Long[] values = new Long[] {userId, folderId};
		
		return 	(Long) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalFolderSize", 
				values));

	}
	
	/**
	 * Gets the path to the collection starting from the top parent all the way
	 * down to the specified child.  Only includes parents of the specified 
	 * folder.  The list is ordered highest level parent to last child.  This
	 * is useful for displaying the path to a given collection.
	 * 
	 * @param collection 
	 * @return list of parent collections.
	 * 
	 * @see edu.ur.ir.user.PersonalFolderDAO#getPath(edu.ur.ir.repository.ResearcherFolder)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFolder> getPath(PersonalFolder personalFolder)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getPersonalFolderPath");
		q.setParameter("leftValue", personalFolder.getLeftValue());
		q.setParameter("rootId",personalFolder.getTreeRoot().getId());
		q.setParameter("userId", personalFolder.getOwner().getId());
		return q.list();
	}
	
	/**
	 * Get all personal folders by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public PersonalFolder getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the personal folder persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(PersonalFolder entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the personal folder transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(PersonalFolder entity) {
		
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * This returns all personal files for the specified parent folder.  This
	 * includes all files in sub folders.
	 * 
	 * @param personalFolder
	 * @return personal files found.
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFile> getAllFilesForFolder(PersonalFolder personalFolder) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllPersonalFilesForFolder");
		q.setParameter("leftValue", personalFolder.getLeftValue());
		q.setParameter("rightValue", personalFolder.getRightValue());
		q.setParameter("rootId", personalFolder.getTreeRoot().getId());
		return (List<PersonalFile>) q.list();
	}
	
	/**
	 * This returns all folders for the specified parent folder.  This
	 * includes all children including those within sub folders.
	 * 
	 * @param personalFolder - to get all children folders from
	 * @return list of all children folders
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFolder> getAllChildrenForFolder(PersonalFolder personalFolder)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllChildrenFoldersForFolder");
		q.setParameter("leftValue", personalFolder.getLeftValue());
		q.setParameter("rightValue", personalFolder.getRightValue());
		q.setParameter("rootId", personalFolder.getTreeRoot().getId());
		return (List<PersonalFolder>) q.list();
	}
	
	/**
	 * This returns all personal files for the specified parent folder.  This
	 * includes all files in sub folders.
	 * 
	 * @param personalFolder
	 * @return versioned files found
	 */
	@SuppressWarnings("unchecked")
	public List<VersionedFile> getAllVersionedFilesForFolder(PersonalFolder personalFolder) {
		Long[] ids = new Long[] {personalFolder.getLeftValue(), 
				personalFolder.getRightValue(), 
				personalFolder.getTreeRoot().getId()};
		List<VersionedFile> files =  
			(List<VersionedFile>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllVersionedFilesForFolder", 
					ids);
		return files;
	}

	
	/**
	 * 
	 * @see edu.ur.ir.user.PersonalFolderDAO#getAllNodesNotInChildTrees(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFolder> getAllFoldersNotInChildFolders(
			final List<PersonalFolder> folders, final Long userId, 
			final Long rootFolderId) {
		
		List<PersonalFolder> foundFolders = new LinkedList<PersonalFolder>();
		if( folders.size() > 0 )
		{
			for(PersonalFolder f : folders)
			{
				Object[] values = {userId, rootFolderId, f.getLeftValue(), f.getRightValue()};
			     foundFolders.addAll((List<PersonalFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllPersonalFoldersNotInChildTree", 
					values));
			}
		}
		return foundFolders;
		
		
	}
	
	
	/**
	 * Get all root folders not within the given set.
	 * 
	 * @see edu.ur.ir.user.PersonalFolderDAO#getAllOtherRootFolders(java.util.List, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFolder> getAllOtherRootFolders(final List<Long> rootFolderIds, 
			final Long userId) {
		String[] parameters = {"ownerId", "folders"};
		Object[] values = {userId, rootFolderIds}; 
		List<PersonalFolder> foundFolders = 
			(List<PersonalFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQueryAndNamedParam("getAllOtherRootPersonalFolders", parameters, values);
		
		return foundFolders;
	}

	
	/**
	 * Find the specified folders.
	 * 
	 * @see edu.ur.ir.user.PersonalFolderDAO#getFolders(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFolder> getFolders(final Long userId, final List<Long> folderIds) {
		List<PersonalFolder> foundFolders = new LinkedList<PersonalFolder>();
		if( folderIds.size() > 0 )
        {
			String[] parameters = {"ownerId", "folders"};
			Object[] values = {userId, folderIds}; 
			foundFolders = 
				(List<PersonalFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQueryAndNamedParam("getPersonalFoldersInList", parameters, values);
			
			return foundFolders;
        }
		return foundFolders;
	}
	
	/**
	 * Get all personal collections in the system.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}
	
}
