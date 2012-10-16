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
import edu.ur.ir.file.IrFile;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFileDAO;



/**
 * Hibernate implementation of personal file data access and storage.
 * 
 * @author Nathan Sarr
 *
 */
public class HbPersonalFileDAO implements PersonalFileDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = 5154598954233397102L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<PersonalFile> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbPersonalFileDAO() 
	{
		hbCrudDAO = new HbCrudDAO<PersonalFile>(PersonalFile.class);
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
	 * Get a count of the IrFiles
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irFileCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Return PersonalFile by id
	 */
	public PersonalFile getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(PersonalFile entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(PersonalFile entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get the files for user id and versioned file id .
	 * 
	 * @param userId
	 * @param versionedFileId
	 * 
	 * @return the found files
	 */
	public PersonalFile getFileForUserWithSpecifiedVersionedFile(Long userId, Long versionedFileId) {
		Object[] values = {userId, versionedFileId};
		return (PersonalFile)
		(HbHelper.getUnique(hbCrudDAO.getHibernateTemplate()
			.findByNamedQuery("getFileWithSpecifiedUserIdAndVerionFileID", values)));
		
	}
	
	/**
	 * Get the files for user id and ir file id .
	 * 
	 * @param userId
	 * @param irFileId
	 * 
	 * @return the found files
	 */
	public PersonalFile getFileForUserWithSpecifiedIrFile(Long userId, Long irFileId) {
		Object[] values = {userId, irFileId};
		return (PersonalFile)
		(HbHelper.getUnique(hbCrudDAO.getHibernateTemplate()
			.findByNamedQuery("getFileWithSpecifiedUserIdAndIrFileID", values)));
		
		
	}

	/**
	 * Get the files with specified ir file id .
	 * 
	 * @param irFileId
	 * 
	 * @return the found files
	 */
	public Long getPersonalFileCount(Long irFileId) {
		
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate()
			.findByNamedQuery("getPersonalFileCountWithIrFileId", irFileId));
		
		
	}
	
	
	/**
	 * Get all item files uses the specified ir file.
	 * 
	 * @param irFile - ir file being used
	 * @return the list of item files being used.
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFile> getPersonalFilesWithIrFile(IrFile irFile) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getPersonalFilesWithIrFileId");
		q.setParameter("irFileId", irFile.getId());
		return q.list();
	}
	
	/**
	 * Get the files for user id and folder id .
	 * 
	 * @param userId
	 * @param folderId
	 * 
	 * @return the found files
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFile> getFilesInFolderForUser(Long userId, Long folderId) 
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getFilesInAFolderForUser");
		q.setParameter("userId", userId);
		q.setParameter("folderId", folderId );
		return q.list();
	}
	
	/**
	 * Get the root files - at the user level
	 * 
	 * @param userId - id of the user to get the root files for
	 * 
	 * @return the found files at the root user level
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFile> getRootFiles(Long userId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getRootFiles");
		q.setParameter("userId", userId);
		return q.list();
	}

	/**
	 * Find the specified files for the given user.
	 * 
	 * @see edu.ur.ir.user.PersonalFileDAO#getFiles(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFile> getFiles(final Long userId, final List<Long> fileIds) {

		String[] params = {"userId", "fileIds"};
		Object[] values = {userId, fileIds};
		List<PersonalFile> foundFiles = new LinkedList<PersonalFile>();
		if( fileIds.size() > 0 )
		{
		   foundFiles = hbCrudDAO.getHibernateTemplate().findByNamedQueryAndNamedParam("getFiles", params, values);
		}
		return foundFiles;
	}

	/**
	 * Get a list of personal files shared witht he given user.
	 * 
	 * @param rowStart - start position in the list
	 * @param maxResults - maximum number of results
	 * @param ownerId - owner of the personal files.
	 * @param sharedWithUserId - id of the user who files are shared with
	 * 
	 * @return list of files shared with the user.
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFile> getFilesSharedWithUser(int rowStart,
			int maxResults, Long ownerId, Long sharedWithUserId)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getPersonalFilesSharedWithUser");
		q.setParameter("ownerId", ownerId);
		q.setParameter("collaboratorId", sharedWithUserId );
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		return q.list();
	}
	
	/**
	 * Get the count of files shared with a given user.
	 * 
	 * @param ownerId - owner of the personal file sto check
	 * @param sharedWithUserId - id of the shared with user id.
	 * 
	 * @return count of files shared with the given shared with user id
	 */
	public Long getFilesSharedWithUserCount(Long ownerId, Long sharedWithUserId)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getPersonalFilesSharedWithUserCount");
		q.setParameter("ownerId", ownerId);
		q.setParameter("collaboratorId", sharedWithUserId );
		return (Long) q.uniqueResult();
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
