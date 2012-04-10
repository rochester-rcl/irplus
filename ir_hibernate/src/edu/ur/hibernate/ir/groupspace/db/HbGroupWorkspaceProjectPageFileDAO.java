package edu.ur.hibernate.ir.groupspace.db;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFile;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileDAO;

public class HbGroupWorkspaceProjectPageFileDAO implements GroupWorkspaceProjectPageFileDAO{


	// eclipse generated id
	private static final long serialVersionUID = 2051373587143446913L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<GroupWorkspaceProjectPageFile> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceProjectPageFileDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceProjectPageFile>(GroupWorkspaceProjectPageFile.class);
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
	 * Return GroupWorkspaceProjectPageFile by id
	 */
	public GroupWorkspaceProjectPageFile getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceProjectPageFile entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceProjectPageFile entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get the files for researcher id and ir file id .
	 * 
	 * @param researcherId
	 * @param irFileId
	 * 
	 * @return the found files
	 */
	public GroupWorkspaceProjectPageFile getWithSpecifiedIrFile(Long projectPageId, Long irFileId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getFileWithSpecifiedGroupWorskpaceProjectPageIdAndIrFileId");
		q.setLong("projectPageId", projectPageId);
		q.setLong("fileId", irFileId);
		return (GroupWorkspaceProjectPageFile) q.uniqueResult();
	}

	
	/**
	 * Get the files for researcher id and folder id .
	 * 
	 * @param researcherId
	 * @param folderId
	 * 
	 * @return the found files
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFile> getFiles(Long projectPageId, Long folderId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getFilesInFolderForGroupWorkspaceProjectPage");
		q.setLong("projectPageId", projectPageId);
		q.setLong("parentId", folderId);
		return (List<GroupWorkspaceProjectPageFile>) q.list();
	}
	
	/**
	 * Get the root files 
	 * 
	 * @param researcherId
	 * 
	 * @return the found files
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFile> getRootFiles(Long projectPageId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getRootGroupWorkspaceProjectPageFiles");
		q.setLong("projectPageId", projectPageId);
		return  (List<GroupWorkspaceProjectPageFile>) q.list();
	}
	
	/**
	 * Find the specified files for the given researcher.
	 * 
	 * @see edu.ur.ir.researcher.GroupWorkspaceProjectPageFileDAO#getFiles(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFile> getFiles(final Long projectPageId, final List<Long> fileIds) {
		List<GroupWorkspaceProjectPageFile> foundFiles = new LinkedList<GroupWorkspaceProjectPageFile>();
		if( fileIds.size() > 0 )
		{
		    Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
		    criteria.createCriteria("groupWorkspaceProjectPage").add(Restrictions.idEq(projectPageId));
            criteria.add(Restrictions.in("id", fileIds));
            foundFiles = criteria.list();
		}
		return foundFiles;
	}

	/**
	 * Get a count of the researcher files containing this irFile
	 * 
	 * @see edu.ur.ir.researcher.GroupWorkspaceProjectPageFileDAO#getGroupWorkspaceProjectPageFileCount(Long)
	 */
	public Long getCountWithSpecifiedIrFile(Long irFileId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceProjectPageFileCount");
		q.setLong("fileId", irFileId);
		return (Long)q.uniqueResult();
	}


}
