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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;

/**
 * Save a versioned file in the system
 * 
 * @author Nathan Sarr
 *
 */
public class HbVersionedFileDAO implements VersionedFileDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 4483615867091465203L;
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<VersionedFile> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbVersionedFileDAO() {
		hbCrudDAO = new HbCrudDAO<VersionedFile>(VersionedFile.class);
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
	 * Get a count of the VersionedIrFiles
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("versionedIrFileCount"));
	}

	/**
	 * Get all the VersionedIrFiles with the specified name.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<VersionedFile> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(VersionedFile.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<VersionedFile>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all VersionedIrFiles ordered by name.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<VersionedFile> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllVersionedIrFileNameAsc", startRecord, numRecords);
	}

	/**
	 * Find VersionedIrFiles listed by name.
	 * 
	 * @see edu.ur.NonUniqueNameDAO#findByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<VersionedFile> findByName(String name) {
	  	return (List<VersionedFile>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getVersionedIrFileByName", name);
	}

	public List<VersionedFile> getAll() {
		return hbCrudDAO.getAll();
	}

	public VersionedFile getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(VersionedFile entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(VersionedFile entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the versioned file containing the given IrFile id
	 * 
	 * @param irFileId file id to get the VersionedFile
	 * 
	 * @return VersionedFile containing the IrFile
	 */
	public VersionedFile getVersionedFileByIrFileId(Long irFileId) {
		return (VersionedFile) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getVersionedFileByIrFileId", irFileId));
		
	}
	
	/**
	 * Get the versioned files containing the given IrFile ids
	 * 
	 * @param itemId item ids to get the VersionedFile
	 * 
	 * @return VersionedFiles containing the IrFile
	 */
	@SuppressWarnings("unchecked")
	public List<VersionedFile> getVersionedFilesForItem(Long itemId) {
		return (List<VersionedFile>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getVersionedFilesForItemId", itemId);
		
	}
	
	/**
	 * Get the sum of versioned file size for a user
	 * 
	 * @param userId user id the VersionedFile belongs to
	 * 
	 * @return sum of versioned files size
	 */
	public Long getSumOfVersionedFilesSizeForUser(Long userId) {
		return (Long) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getSumOfVersionedFilesSizeForUser", userId));
		
	}	
}
