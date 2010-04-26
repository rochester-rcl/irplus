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
import edu.ur.ir.item.Series;
import edu.ur.ir.item.SeriesDAO;


/**
 * Data access for series
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbSeriesDAO implements SeriesDAO {
	
	private final HbCrudDAO<Series> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbSeriesDAO() {
		hbCrudDAO = new HbCrudDAO<Series>(Series.class);
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
	 * Get a count of the series in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("seriesCount"));
	}

	/**
	 * Get all sponsors in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<Series> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(Series.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<Series>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all sponsors in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<Series> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllSeriesNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the sponsor by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public Series findByUniqueName(String name) {
		return (Series) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getSeriesByName", name));
	}

	public Series getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(Series entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(Series entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	@SuppressWarnings("unchecked")
	public List<Series> getSeriesOrderByName(
			final int rowStart, final int numberOfResultsToShow, final String sortType) {
		List<Series> series = (List<Series>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getSeriesOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getSeriesOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
        });
        return series;
	}
}
