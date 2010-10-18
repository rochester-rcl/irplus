/**  
   Copyright 2008-2010 University of Rochester

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

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.user.ExternalAccountType;
import edu.ur.ir.user.ExternalAccountTypeDAO;
import edu.ur.order.OrderType;

/**
 * Hibernate implementation of the external account type data access object.
 * 
 * @author Nathan Sarr
 *
 */
public class HbExternalAccountTypeDAO implements ExternalAccountTypeDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 1642482159037617969L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<ExternalAccountType> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbExternalAccountTypeDAO() {
		hbCrudDAO = new HbCrudDAO<ExternalAccountType>(ExternalAccountType.class);
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
	 * Get external account types ordered by name.
	 * 
	 * @see edu.ur.ir.user.ExternalAccountTypeDAO#getOrderByName(int, int, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<ExternalAccountType> getOrderByName(final int rowStart,
			final int numberOfResultsToShow, final OrderType orderType) {
		List<ExternalAccountType> externalAccountTypes  = 
			(List<ExternalAccountType>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

	            Query q = null;
		        if (orderType.equals(OrderType.ASCENDING_ORDER)) {
		        	q = session.getNamedQuery("getExternalAccountTypesOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getExternalAccountTypesOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
			
		});

        return externalAccountTypes;
	}

	
	/***
	 * Get the count of account types in the system.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("externalAccountTypeCount"));
	}

	
	/**
	 * Return all content types in the system.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	
	/**
	 * Get the external account type by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ExternalAccountType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	/**
	 * Save the external account type.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ExternalAccountType entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	
	/**
	 * Delete the external account type.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ExternalAccountType entity) {
		hbCrudDAO.makeTransient(entity);
		
	}

	
	/**
	 * Get all external account types by name order.
	 * 
	 * @see edu.ur.dao.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List getAllNameOrder() {
		return (List<ExternalAccountType>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getExternalAccountTypesOrderByNameAsc");
	}

	/**
	 * Get all external account types ordered by name.
	 * 
	 * @see edu.ur.dao.NameListDAO#getAllOrderByName(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getExternalAccountTypesOrderByNameAsc", startRecord, numRecords);
	}


	/**
	 * Find the external account type by name.
	 * 
	 * @see edu.ur.dao.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public ExternalAccountType findByUniqueName(String name) {
		return (ExternalAccountType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getExternalAccountTypeByName", name));
	}

}
