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

package edu.ur.hibernate.file.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.FileDatabaseDAO;
import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;

/**
 * Persist the information for a default file database to the
 * SQL database.
 * 
 * @author Nathan Sarr
 *
 */
public class HbDefaultFileDatabaseDAO implements FileDatabaseDAO{
	
	/* eclipse generated id */
	private static final long serialVersionUID = 408333848976757952L;
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<DefaultFileDatabase> hbCrudDAO;
	
	
	/**
	 * Default Constructor
	 */
	public HbDefaultFileDatabaseDAO() {
		hbCrudDAO = new HbCrudDAO<DefaultFileDatabase>(DefaultFileDatabase.class);
	}

    /**
     * Get the number of file databases in the system
     * 
     * @return the number of file databases 
     */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("fileDatabaseCount"));
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
     * Return all file databases orderd by Name.  This list 
     * can be extreamly large and it is recomened that
     * paging is used instead
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<DefaultFileDatabase> getAllNameOrder() {
    	DetachedCriteria dc = DetachedCriteria.forClass(DefaultFileDatabase.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<DefaultFileDatabase>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

    /**
     * Get all file databases starting at the start record and get up to 
     * the numRecords - it will be ordered by name
     *  
     * @param startRecord - the index to start at
     * @param numRecords - the number of records to get
     * @return the records found
     */
	public List<DefaultFileDatabase> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllfileDatabasesNameAsc", startRecord, numRecords);
	}

	/** 
     * Find a file database by name
     * 
     * @param name - name of the database.
	 */
	public DefaultFileDatabase findByName(String name, Long fileServerId) {
		Object[] values = {name, fileServerId};
	  	return (DefaultFileDatabase) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getFileDatabaseByName", values));
	}
	
	/** 
     * Find a file database by display name
     * 
     * @param name - name of the database.
	 */
	public DefaultFileDatabase findByDisplayName(String name, Long fileServerId) {
		Object[] values = {name, fileServerId};
	  	return (DefaultFileDatabase) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getFileDatabaseByDisplayName", values));
	}
	

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get the file database by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public DefaultFileDatabase getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Throws a class cast exception if the entity is not an
	 * instance of DefaultFileDatabase
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(FileDatabase entity) {
		hbCrudDAO.makePersistent((DefaultFileDatabase)entity);
	}

	/**
	 * Throws a class cast exception if the entity is not an
	 * instance of DefaultFileDatabase
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(FileDatabase entity) {
		hbCrudDAO.makeTransient((DefaultFileDatabase)entity);
	}
}
