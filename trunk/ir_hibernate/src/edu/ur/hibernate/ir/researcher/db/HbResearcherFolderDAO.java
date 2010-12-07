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

package edu.ur.hibernate.ir.researcher.db;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherFolderDAO;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherPublication;

/**
 * Hibernate implementation of persisting a researcher folder.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbResearcherFolderDAO implements ResearcherFolderDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -2139557520714885135L;

	/** Logger */
	private static final Logger log = Logger.getLogger(HbResearcherFolderDAO.class);
	
	/** helper for dealing with database */
	private final HbCrudDAO<ResearcherFolder> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbResearcherFolderDAO() {
		hbCrudDAO = new HbCrudDAO<ResearcherFolder>(ResearcherFolder.class);
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
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("researcherFolderCount"));
	}

	/**
	 * Find the folder for the specified researcherId and specified 
	 * folder name.
	 * 
	 * @param name of the collection
	 * @param researcherId - id of the researcher
	 * @return the found collection or null if the collection is not found.
	 */
	public ResearcherFolder getRootResearcherFolder(String name, Long researcherId)
	{
		Object[] values = new Object[] {name, researcherId};
		return (ResearcherFolder) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRootResearcherFolderByNameResearcher", 
				values));
	}
	
	/**
	 * Find the researcher folder for the specified folder name and 
	 * parent id.
	 * 
	 * @param name of the folder
	 * @param parentId id of the parent folder
	 * @return the found folder or null if the folder is not found.
	 */
	public ResearcherFolder getResearcherFolder(String name, Long parentId)
	{
		Object[] values = new Object[] {name, parentId};
		return (ResearcherFolder) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getResearcherFolderByNameParent", 
				values));
	}
	
	/**
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getNodesLeftRightGreaterEqual(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFolder> getNodesLeftRightGreaterEqual(Long value, Long rootId) {
		Long[] values = new Long[] {value, value, rootId};
		return (List<ResearcherFolder>) 
		hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllResearcherFoldersValueGreaterEqual", values);
	}
	
	/**
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getAllNodesNotInChildTree(edu.ur.ir.repository.ResearcherFolder)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFolder> getAllNodesNotInChildFolder(ResearcherFolder researcherFolder)
	{
		Long[] ids = new Long[] {researcherFolder.getLeftValue(),
				researcherFolder.getRightValue(), 
				researcherFolder.getTreeRoot().getId()};
		List<ResearcherFolder> listTree =  
			(List<ResearcherFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllResearcherFoldersNotInChildTree", ids);
		return listTree;
	}
	
	/**
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getAllResearcherFoldersForResearcher(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFolder> getAllResearcherFoldersForResearcher(Long researcherId) {
		List<ResearcherFolder> collections =  
			(List<ResearcherFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getResearcherFoldersForResearcher", 
				researcherId);
		return collections;
	}
	
	/**
	 * Get sub folders for the parent folder
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getSubFoldersForFolder(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFolder> getSubFoldersForFolder(Long researcherId, Long parentFolderId) {
		
		Long[] values = new Long[] {researcherId, parentFolderId};
		
		List<ResearcherFolder> collections =  
			(List<ResearcherFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getSubFoldersForResearcherFolder", 
					values);
		return collections;
	}
	
	/**
	 * Get root folders 
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getRootFolders(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFolder> getRootFolders(Long researcherId) {
		
		List<ResearcherFolder> collections =  
			(List<ResearcherFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRootResearcherFolders", 
					researcherId);
		return collections;
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
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getPath(edu.ur.ir.repository.ResearcherFolder)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFolder> getPath(ResearcherFolder researcherFolder)
	{
		Long[] values = new Long[] {researcherFolder.getLeftValue(),
				researcherFolder.getTreeRoot().getId(), 
				researcherFolder.getResearcher().getId()};
		return (List<ResearcherFolder>) 
		hbCrudDAO.getHibernateTemplate().findByNamedQuery("getResearcherFolderPath", values);
	}
	
	/**
	 * Get all researcher folders by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ResearcherFolder getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the researcher folder persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ResearcherFolder entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the researcher folder transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ResearcherFolder entity) 
	{
		log.debug("Deleteing researcher folder " + entity);
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * This returns all researcher files for the specified parent folder.  This
	 * includes all files in sub folders.
	 * 
	 * @param researcherFolder
	 * @return researcher files found.
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFile> getAllFilesForFolder(ResearcherFolder researcherFolder) {
		Long[] ids = new Long[] {researcherFolder.getLeftValue(),
				researcherFolder.getRightValue(), 
				researcherFolder.getTreeRoot().getId()};
		List<ResearcherFile> files =  
			(List<ResearcherFile>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllResearcherFilesForFolder", 
					ids);
		return files;
	}
	
	/**
	 * This returns all researcher files for the specified parent folder.  This
	 * includes all files in sub folders.
	 * 
	 * @param researcherFolder
	 * @return researcher files found.
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherPublication> getAllPublicationsForFolder(ResearcherFolder researcherFolder) {
		Long[] ids = new Long[] {researcherFolder.getLeftValue(),
				researcherFolder.getRightValue(), 
				researcherFolder.getTreeRoot().getId()};
		List<ResearcherPublication> publications =  
			(List<ResearcherPublication>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllResearcherPublicationsForFolder", 
					ids);
		return publications;
	}

	/**
	 * This returns all institutional items for the specified parent folder.  This
	 * includes all institutional items in sub folders.
	 * 
	 * @param researcherFolder
	 * @return researcher files found.
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherInstitutionalItem> getAllInstitutionalItemsForFolder(ResearcherFolder researcherFolder) {
		Long[] ids = new Long[] {researcherFolder.getLeftValue(),
				researcherFolder.getRightValue(), 
				researcherFolder.getTreeRoot().getId()};
		List<ResearcherInstitutionalItem> institutionalItems =  
			(List<ResearcherInstitutionalItem>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllResearcherInstitutionalItemsForFolder", 
					ids);
		return institutionalItems;
	}

	
	/**
	 * This returns all researcher files for the specified parent folder.  This
	 * includes all files in sub folders.
	 * 
	 * @param researcherFolder
	 * @return researcher files found.
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherLink> getAllLinksForFolder(ResearcherFolder researcherFolder) {
		Long[] ids = new Long[] {researcherFolder.getLeftValue(),
				researcherFolder.getRightValue(), 
				researcherFolder.getTreeRoot().getId()};
		List<ResearcherLink> links =  
			(List<ResearcherLink>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllResearcherLinksForFolder", 
					ids);
		return links;
	}
	
	/**
	 * This returns all researcher files for the specified parent folder.  This
	 * includes all files in sub folders.
	 * 
	 * @param researcherFolder
	 * @return versioned files found
	 */
	@SuppressWarnings("unchecked")
	public List<IrFile> getAllIrFilesForFolder(ResearcherFolder researcherFolder) {
		Long[] ids = new Long[] {researcherFolder.getLeftValue(), 
				researcherFolder.getRightValue(), 
				researcherFolder.getTreeRoot().getId()};
		List<IrFile> files =  
			(List<IrFile>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllIrFilesForFolder", 
					ids);
		return files;
	}

	
	/**
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getAllNodesNotInChildTrees(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFolder> getAllFoldersNotInChildFolders(
			final List<ResearcherFolder> folders, final Long researcherId, 
			final Long rootFolderId) {
		
		List<ResearcherFolder> foundFolders = new LinkedList<ResearcherFolder>();
		if( folders.size() > 0 )
		{
			for(ResearcherFolder f : folders)
			{
				Object[] values = {researcherId, rootFolderId, f.getLeftValue(), f.getRightValue()};
			     foundFolders.addAll((List<ResearcherFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllResearcherFoldersNotInChildFolders", 
					values));
			}
		}
		return foundFolders;
	}
	
	
	/**
	 * Get all root folders not within the given set.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getAllOtherRootFolders(java.util.List, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFolder> getAllOtherRootFolders(final List<Long> rootFolderIds, 
			final Long researcherId) {
		String[] parameters = {"researcherId", "folders"};
		Object[] values = {researcherId, rootFolderIds};
		List<ResearcherFolder> foundFolders = new LinkedList<ResearcherFolder>();
		if( rootFolderIds.size() > 0 )
		{
		   foundFolders = (List<ResearcherFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQueryAndNamedParam("getAllOtherRootResearcherFolders", parameters, values);;
		}
		return foundFolders;
	}

	
	/**
	 * Find the specified folders.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getFolders(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFolder> getFolders(final Long researcherId, final List<Long> folderIds) {
		String[] parameters = {"researcherId", "folders"};
		Object[] values = {researcherId, folderIds};
		List<ResearcherFolder> foundFolders = new LinkedList<ResearcherFolder>();
		if( folderIds.size() > 0 )
        {
			foundFolders = (List<ResearcherFolder>) hbCrudDAO.getHibernateTemplate().findByNamedQueryAndNamedParam("getAllResearcherFoldersInList", parameters, values); 
        }
		return foundFolders;
	}
	
}
