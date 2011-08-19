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

package edu.ur.hibernate.ir.item.db;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherDAO;

/**
 * Hibernate data access for a publisher.
 * 
 * @author Nathan Sarr
 * 
 */
public class HbPublisherDAO implements PublisherDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 6563233578313368149L;

	/** hibernate helper  */
	private final HbCrudDAO<Publisher> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbPublisherDAO() {
		hbCrudDAO = new HbCrudDAO<Publisher>(Publisher.class);
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
	 * Get a count of the language types in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("publisherCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get all language types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<Publisher> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(Publisher.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<Publisher>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all language types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<Publisher> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllPublishersNameAsc", startRecord, numRecords);
	}

	public Publisher getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(Publisher entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(Publisher entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	@SuppressWarnings("unchecked")
	public List<Publisher> getPublishersOrderByName(
			final int rowStart, final int numberOfResultsToShow, final String sortType) {
		List<Publisher> publishers = (List<Publisher>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getPublishersOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getPublishersOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
        });
        return publishers;
	}

	/**
	 * Find the Publisher by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public Publisher findByUniqueName(String name) {
		return (Publisher) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPublisherByName", name));
	}

}
