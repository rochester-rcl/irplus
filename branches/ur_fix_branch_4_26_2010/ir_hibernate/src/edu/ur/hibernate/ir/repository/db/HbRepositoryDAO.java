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

package edu.ur.hibernate.ir.repository.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.repository.LicenseVersion;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryDAO;

/**
 * Hibernate implementation for storing a repository.
 * 
 * @author Nathan Sarr
 *
 */
public class HbRepositoryDAO implements RepositoryDAO {

	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<Repository> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbRepositoryDAO() {
		hbCrudDAO = new HbCrudDAO<Repository>(Repository.class);
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
	 * Get the count of repositories.
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("repositoryCount"));
	}

	/**
	 * Find all repositories name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<Repository> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(Repository.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<Repository>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Find a repository by the specified name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public Repository findByUniqueName(String name) {
		return (Repository) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRepositoryByName", name));
	}

	/**
	 * Get all repositories.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get the repository by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public Repository getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the repository persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(Repository entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the repository transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(Repository entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	/**
	 * Get licenses that can still be added to the repository.
	 * 
	 * @see edu.ur.ir.repository.RepositoryDAO#getAvailableRepositoryLicenses()
	 */
	@SuppressWarnings("unchecked")
	public List<LicenseVersion> getAvailableRepositoryLicenses(Long repositoryId) {
		return (List<LicenseVersion>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAvailableLicensesForRepository", repositoryId);
	}

	
	/**
	 * Get the download count for the repository.
	 * 
	 * @see edu.ur.ir.repository.RepositoryDAO#getDownloadCount()
	 */
	public Long getDownloadCount() {
		Long value =   (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRepositoryDownloadCount"));
		if( value != null )
		{
		    return value;
		}
		else
		{
		    return 0l;
		}
	}
}
