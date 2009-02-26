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
