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

import org.apache.log4j.Logger;
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

	/** Logger */
	private static final Logger log = Logger.getLogger(HbPersonalFolderDAO.class);
	
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
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personalFolderCount"));
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
	public List<PersonalFolder> getSubFoldersForFolder(Long userId, Long parentFolderId) {
		
		Long[] values = new Long[] {userId, parentFolderId};
		
		List<PersonalFolder> folders =  
			(List<PersonalFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalSubFoldersForFolder", 
					values);
		return folders;
	}
	
	/**
	 * Get root folders 
	 * 
	 * @see edu.ur.ir.user.PersonalFolderDAO#getRootFolders(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFolder> getRootFolders(Long userId) {
		
		List<PersonalFolder> folders =  
			(List<PersonalFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalRootFolders", 
					userId);
		return folders;
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
	 * collection.  The list is ordered highest level parent to last child.  This
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
		Long[] values = new Long[] {personalFolder.getLeftValue(),
				personalFolder.getTreeRoot().getId(), 
				personalFolder.getOwner().getId()};
		return (List<PersonalFolder>) 
		hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalFolderPath", values);
	}
	

	/**
	 * Get all personal folders in the system.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
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
		
		log.debug("deleting folder " + entity);
		
		Long[] values = new Long[]{entity.getTreeRoot().getId(), entity.getLeftValue(), entity.getRightValue()};
		
		String deleteFiles = "DELETE PersonalFile AS file " +
		"WHERE file.id IN " +
		"( " +
		"  SELECT aFile.id " +
		"  FROM PersonalFile aFile " +
		"  WHERE aFile.personalFolder.treeRoot.id = ? " +
		"  and aFile.personalFolder.leftValue between ? and ? " +
		")";

		
		int numDeleted = hbCrudDAO.getHibernateTemplate().bulkUpdate(deleteFiles, values);
		
		if(log.isDebugEnabled())
		{
		    log.debug("deleted " + numDeleted + 
		    		" files from root folder id = " 
		    		+ entity.getTreeRoot().getId() + 
		    		" where left value between " + entity.getLeftValue() + 
		    		" and " + entity.getRightValue());
		}
	    
		
		String deleteFolders = "delete PersonalFolder pf where pf.treeRoot.id = ? and " +
		"pf.leftValue between ? and ?";
		
		numDeleted = hbCrudDAO.getHibernateTemplate().bulkUpdate(deleteFolders, values);
		
		if(log.isDebugEnabled())
		{
		    log.debug("deleted " + numDeleted + 
		    		" folders from root folder id = " 
		    		+ entity.getTreeRoot().getId() + 
		    		" where left value between " + entity.getLeftValue() + 
		    		" and " + entity.getRightValue());
		}
		
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
		Long[] ids = new Long[] {personalFolder.getLeftValue(),
				personalFolder.getRightValue(), 
				personalFolder.getTreeRoot().getId()};
		List<PersonalFile> files =  
			(List<PersonalFile>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllPersonalFilesForFolder", 
					ids);
		return files;
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
	
}
