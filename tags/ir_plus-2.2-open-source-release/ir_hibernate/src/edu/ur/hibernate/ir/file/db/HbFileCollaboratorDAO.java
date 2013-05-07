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

package edu.ur.hibernate.ir.file.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.file.FileCollaboratorDAO;

/**
 * Save a file collaborator in the system
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbFileCollaboratorDAO implements FileCollaboratorDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 197525471224770471L;
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<FileCollaborator> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbFileCollaboratorDAO() {
		hbCrudDAO = new HbCrudDAO<FileCollaborator>(FileCollaborator.class);
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
	 * Get a count of the FileCollaborator
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("fileCollaboratorCount"));
	}
	
	/**
	 * Find Collaborators for a Versioned file.
	 * 
	 * @see edu.ur.ir.file.FileCollaboratorDAO#findCollaboratorsForVerionedFileId(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<FileCollaborator> findCollaboratorsForVerionedFileId(Long versionedFileId) {
	  	return (List<FileCollaborator>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("findCollaboratorsForVersionedFileId", versionedFileId);
	}
	
	/**
	 * Get the file collaborator for the user id and versioned file id.
	 * 
	 * @param userId - id of the user who is a collaborator
	 * @param versionedFileId - id of the versioned file.
	 * 
	 * @return the file collaborator.
	 */
	public FileCollaborator findByUserIdVersionedFileId(Long userId, Long versionedFileId)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("findCollaboratorForUserVersionedFileId");
		q.setParameter("versionedFileId", versionedFileId);
		q.setParameter("userId", userId);
		return (FileCollaborator)q.uniqueResult();
	}
	
	/**
	 * Get the list for file collaborator objects for the user id and versioned file ids.
	 * 
	 * @param userId - id of the user who is a collaborator
	 * @param versionedFileIds - List of versioned files to check for
	 * 
	 * @return the file collaborator.
	 */
	@SuppressWarnings("unchecked")
	public List<FileCollaborator> findByUserIdVersionedFileId(Long userId, List<Long> versionedFileIds)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("findCollaboratorForUserVersionedFileIdList");
		q.setParameterList("versionedFileIds", versionedFileIds);
		q.setParameter("userId", userId);
		return (List<FileCollaborator>)q.list();
	}

	public List<FileCollaborator> getAll() {
		return hbCrudDAO.getAll();
	}

	public FileCollaborator getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Save FileCollaborator
	 *  
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(FileCollaborator entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Delete FileCollaborator
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(FileCollaborator entity) {
		hbCrudDAO.makeTransient(entity);
	}


}
