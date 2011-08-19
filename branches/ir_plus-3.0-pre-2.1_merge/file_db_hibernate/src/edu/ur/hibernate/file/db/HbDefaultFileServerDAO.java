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

import edu.ur.file.db.FileServer;
import edu.ur.file.db.DefaultFileServer;
import edu.ur.file.db.FileServerDAO;
import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;

/**
 * Data access for a file server.
 * 
 * @author Nathan Sarr
 *
 */
public class HbDefaultFileServerDAO implements FileServerDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = 5501358709497834203L;
	
	private final HbCrudDAO<DefaultFileServer> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbDefaultFileServerDAO() {
		hbCrudDAO = new HbCrudDAO<DefaultFileServer>(DefaultFileServer.class);
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
	 * Return a count of the number of file servers in the
	 * database.
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("fileServerCount"));
	}

    /**
     * Return all file database servers orderd by Name.
     * 
     * @return the found file database servers.
     */
    @SuppressWarnings("unchecked")
	public List<DefaultFileServer> getAllNameOrder() {
    	DetachedCriteria dc = DetachedCriteria.forClass(DefaultFileServer.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<DefaultFileServer>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

    /**
     * Get all file databases servers starting at the start record and get up to 
     * the numRecords - it will be ordered by name
     *  
     * @param startRecord - the index to start at
     * @param numRecords - the number of records to get
     * @return the records found
     */
	public List<DefaultFileServer> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllFileServersNameAsc", startRecord, numRecords);
	}

	/** 
     * Find a file database server by the unqiue name
     * 
     * @param name - name of the database.
	 */
	public DefaultFileServer findByUniqueName(String name) {
	  	return (DefaultFileServer) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getFileServerByName", name));
	}

	/**
	 * Find a file server impl by id
	 * 
	 * @see edu.ur.dao.CrudDAO#findById(java.lang.Long, boolean)
	 */
	public DefaultFileServer findById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Get alll file servers
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<DefaultFileServer> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get a file server by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public DefaultFileServer getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make a file server persistent. Throws a class cast
	 * exception if the entity is not an instance of 
	 * DefaultFileServer
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(FileServer entity) {
		hbCrudDAO.makePersistent((DefaultFileServer)entity);
	}

	/**
	 * Make a file server transient.  Throws a class cast
	 * exception if the file server is not an instance of 
	 * DefaultFileServer
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(FileServer entity) {
		hbCrudDAO.makeTransient((DefaultFileServer)entity);
	}

}
