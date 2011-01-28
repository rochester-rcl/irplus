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

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
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
	public HbPersonalFileDAO() {
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
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irFileCount"));
	}


	/**
	 * Return all PersonalFile
	 */
	public List<PersonalFile> getAll() {
		return hbCrudDAO.getAll();
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
	public Long getFileWithSpecifiedIrFile(Long irFileId) {
		
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate()
			.findByNamedQuery("getPersonalFilesWithIrFileId", irFileId));
		
		
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
	public List<PersonalFile> getFilesInFolderForUser(Long userId, Long folderId) {
		Object[] values = {userId, folderId};
		return (List<PersonalFile>) (hbCrudDAO.getHibernateTemplate().findByNamedQuery("getFilesInAFolderForUser", values));
		
	}
	
	/**
	 * Get the root files 
	 * 
	 * @param userId
	 * 
	 * @return the found files
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalFile> getRootFiles(Long userId) {
		
		return (List<PersonalFile>) (hbCrudDAO.getHibernateTemplate().
				findByNamedQuery("getRootFiles", userId));
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


}
