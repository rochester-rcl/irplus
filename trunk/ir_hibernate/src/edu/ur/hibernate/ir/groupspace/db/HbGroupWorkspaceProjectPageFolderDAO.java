package edu.ur.hibernate.ir.groupspace.db;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFile;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLink;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolderDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageInstitutionalItem;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPagePublication;

public class HbGroupWorkspaceProjectPageFolderDAO implements GroupWorkspaceProjectPageFolderDAO{
	

	// eclipse generated id
	private static final long serialVersionUID = -1965989489074571195L;

	// Logger */
	private static final Logger log = Logger.getLogger(HbGroupWorkspaceProjectPageFolderDAO.class);
	
	/** helper for dealing with database */
	private final HbCrudDAO<GroupWorkspaceProjectPageFolder> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceProjectPageFolderDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceProjectPageFolder>(GroupWorkspaceProjectPageFolder.class);
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupWorkspaceProjectPageFolderCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Find the folder for the specified groupWorkspaceProjectPageId and specified 
	 * folder name.
	 * 
	 * @param name of the folder
	 * @param researcherId - id of the researcher
	 * @return the found collection or null if the collection is not found.
	 */
	public GroupWorkspaceProjectPageFolder getRootFolder(String name, Long groupWorkspaceProjectPageId)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getRootGroupWorkspaceProjectPageFolderByNameProject");
		q.setString("name", name);
		q.setLong("projectPageId", groupWorkspaceProjectPageId);
		return (GroupWorkspaceProjectPageFolder) q.uniqueResult();
		
	}
	
	/**
	 * Find the group workspace project page folder folder for the specified folder name and 
	 * parent id.
	 * 
	 * @param name of the folder
	 * @param parentId id of the parent folder
	 * @return the found folder or null if the folder is not found.
	 */
	public GroupWorkspaceProjectPageFolder getFolder(String name, Long parentId)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWrokspaceProjectPageFolderByNameParent");
		q.setString("name", name);
		q.setLong("parentId", parentId);
		return (GroupWorkspaceProjectPageFolder) q.uniqueResult();
	}
	
	/**
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getNodesLeftRightGreaterEqual(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFolder> getNodesLeftRightGreaterEqual(Long value, Long rootId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllGroupWorkspaceProjectPageFoldersValueGreaterEqual");
		q.setLong("leftValue", value);
		q.setLong("rightValue", value);
		q.setLong("rootId", rootId);
		return (List<GroupWorkspaceProjectPageFolder>) q.list();
	}
	
	/**
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getAllNodesNotInChildTree(edu.ur.ir.repository.ResearcherFolder)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFolder> getAllNodesNotInChildFolder(GroupWorkspaceProjectPageFolder folder)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllGroupWorkspaceProjectPageFoldersNotInChildTree");
		q.setLong("leftValue", folder.getLeftValue());
		q.setLong("rightValue", folder.getRightValue());
		q.setLong("rootId", folder.getTreeRoot().getId());
		return (List<GroupWorkspaceProjectPageFolder>) q.list();
	}
	
	/**
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getAllResearcherFoldersForResearcher(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFolder> getAllFolders(Long researcherId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceProjectPageFolders");
		q.setLong("id", researcherId);
		return (List<GroupWorkspaceProjectPageFolder>)q.list();
	}
	
	/**
	 * Get sub folders for the parent folder
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getSubFoldersForFolder(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFolder> getSubFoldersForFolder(Long projectPageId, Long parentFolderId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getSubFoldersForGroupWorkspaceProjectPageFolder");
		q.setLong("projectPageId", projectPageId);
		q.setLong("parentFolderId", parentFolderId);
		return (List<GroupWorkspaceProjectPageFolder>)q.list();
	}
	
	/**
	 * Get root folders 
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getRootFolders(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFolder> getRootFolders(Long projectPageId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getRootGroupWorkspaceProjectPageFolders");
		q.setLong("projectPageId", projectPageId);
		return (List<GroupWorkspaceProjectPageFolder>)q.list();
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
	public List<GroupWorkspaceProjectPageFolder> getPath(GroupWorkspaceProjectPageFolder folder)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getResearcherFolderPath");
		q.setLong("leftValue", folder.getLeftValue());
		q.setLong("rootId", folder.getTreeRoot().getId());
		q.setLong("projectPageId", folder.getGroupWorkspaceProjectPage().getId());
		return (List<GroupWorkspaceProjectPageFolder>) q.list();
	}
	
	/**
	 * Get all researcher folders by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public GroupWorkspaceProjectPageFolder getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the researcher folder persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceProjectPageFolder entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the researcher folder transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceProjectPageFolder entity) 
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
	public List<GroupWorkspaceProjectPageFile> getAllFiles(GroupWorkspaceProjectPageFolder folder) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllGroupWorkspaceProjectPageFilesForFolder");
		q.setLong("leftValue", folder.getLeftValue());
		q.setLong("rightValue", folder.getRightValue());
		q.setLong("rootId", folder.getTreeRoot().getId());
		return (List<GroupWorkspaceProjectPageFile>)q.list();
	}
	
	/**
	 * This returns all researcher files for the specified parent folder.  This
	 * includes all files in sub folders.
	 * 
	 * @param researcherFolder
	 * @return researcher files found.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPagePublication> getAllPublications(GroupWorkspaceProjectPageFolder folder) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllGroupWorkspaceProjectPagePublicationsForFolder");
		q.setLong("leftValue", folder.getLeftValue());
		q.setLong("rightValue", folder.getRightValue());
		q.setLong("rootId", folder.getTreeRoot().getId());
		return (List<GroupWorkspaceProjectPagePublication>)q.list();
	}

	/**
	 * This returns all institutional items for the specified parent folder.  This
	 * includes all institutional items in sub folders.
	 * 
	 * @param researcherFolder
	 * @return researcher files found.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageInstitutionalItem> getAllInstitutionalItemsForFolder(GroupWorkspaceProjectPageFolder folder) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllResearcherInstitutionalItemsForFolder");
		q.setLong("leftValue", folder.getLeftValue());
		q.setLong("rightValue", folder.getRightValue());
		q.setLong("rootId", folder.getTreeRoot().getId());
		return (List<GroupWorkspaceProjectPageInstitutionalItem>) q.list();
	}

	
	/**
	 * This returns all researcher files for the specified parent folder.  This
	 * includes all files in sub folders.
	 * 
	 * @param researcherFolder
	 * @return researcher files found.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFileSystemLink> getAllLinks(GroupWorkspaceProjectPageFolder folder) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllResearcherLinksForFolder");
		q.setLong("leftValue", folder.getLeftValue());
		q.setLong("rightValue", folder.getRightValue());
		q.setLong("rootId", folder.getTreeRoot().getId());
		return (List<GroupWorkspaceProjectPageFileSystemLink>) q.list();
	}
	
	/**
	 * This returns all researcher files for the specified parent folder.  This
	 * includes all files in sub folders.
	 * 
	 * @param researcherFolder
	 * @return versioned files found
	 */
	@SuppressWarnings("unchecked")
	public List<IrFile> getAllIrFiles(GroupWorkspaceProjectPageFolder folder) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllIrFilesForGroupWorkspaceProjectPageFolder");
		q.setLong("leftValue", folder.getLeftValue());
		q.setLong("rightValue", folder.getRightValue());
		q.setLong("rootId", folder.getTreeRoot().getId());
		return (List<IrFile>) q.list();
	}

	
	/**
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getAllNodesNotInChildTrees(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFolder> getAllFoldersNotInChildFolders(
			final List<GroupWorkspaceProjectPageFolder> folders, final Long projectPageId, 
			final Long rootFolderId) {
		
		List<GroupWorkspaceProjectPageFolder> foundFolders = new LinkedList<GroupWorkspaceProjectPageFolder>();
		if( folders.size() > 0 )
		{
			for(GroupWorkspaceProjectPageFolder f : folders)
			{
				Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllGroupWorkspaceProjectPageFoldersNotInChildFolders");
				q.setLong("projectPageId", projectPageId);
				q.setLong("rootId", rootFolderId);
				q.setLong("leftValue", f.getLeftValue());
				q.setLong("rightValue", f.getRightValue());
			    foundFolders.addAll((List<GroupWorkspaceProjectPageFolder>) q.list());
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
	public List<GroupWorkspaceProjectPageFolder> getAllOtherRootFolders(final List<Long> rootFolderIds, 
			final Long projectPageId) {
		List<GroupWorkspaceProjectPageFolder> foundFolders = new LinkedList<GroupWorkspaceProjectPageFolder>();
		if( rootFolderIds.size() > 0 )
		{
		
		    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllOtherRootGroupWorkspaceProjectPageFolders");
		    q.setLong("projectPageId", projectPageId);
		    q.setParameterList("folders",  rootFolderIds);
		    foundFolders = (List<GroupWorkspaceProjectPageFolder>) q.list();
		}
		return foundFolders;
	}

	
	/**
	 * Find the specified folders.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFolderDAO#getFolders(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFolder> getFolders(final Long projectPageId, final List<Long> folderIds) {
		List<GroupWorkspaceProjectPageFolder> foundFolders = new LinkedList<GroupWorkspaceProjectPageFolder>();
		if( folderIds.size() > 0 )
		{
		    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllGroupWorkspaceProjectPageFoldersInList");
		    q.setLong("projectPageId", projectPageId);
		    q.setParameterList("folders", folderIds);
		    foundFolders = (List<GroupWorkspaceProjectPageFolder>)q.list();
		}
		return foundFolders;
	}

}
