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

package edu.ur.hibernate.ir.news.db;

import java.sql.SQLException;
import java.util.Date;
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
import edu.ur.ir.news.NewsDAO;
import edu.ur.ir.news.NewsItem;


/**
 * Data access for news information.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbNewsDAO implements NewsDAO {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 3662526225500083876L;
	
	/**  Helper for persisting information using hibernate.  */	
	private final HbCrudDAO<NewsItem> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbNewsDAO() {
		hbCrudDAO = new HbCrudDAO<NewsItem>(NewsItem.class);
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
	 * Get a count of the roles in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("newsCount"));
	}

	/**
	 * Get all roles in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<NewsItem> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(NewsItem.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<NewsItem>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all News in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<NewsItem> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllNewsNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the news by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public NewsItem findByUniqueName(String name) {
		return (NewsItem) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getNewsByName", name));
	}

	/**
	 * Get all news items.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get a news item by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public NewsItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the news item persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(NewsItem entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the news item transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(NewsItem entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get all news items where date d is between start and end remove date
	 * 
	 *@param date the news items should be available
	 */
	public Long getAvailableNewsItemsCount(Date d)
	{
		Object[] values = {d, d};
		return (Long) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("countAvailableNewsItemsForDate", values));
	}
	

	/**
	 * Get the availabe news items.
	 * 
	 * @param d - date the news items should be available
	 * @param offset - where to start in the list
	 * @param numToFetch - maximum number of items to fetch
	 * @return- available news items
	 */
	@SuppressWarnings("unchecked")
	public List<NewsItem> getAvailableNewsItems(final Date d, final int offset, final int numToFetch)
	{
		return (List<NewsItem>) hbCrudDAO.getHibernateTemplate().executeFind( new HibernateCallback() 
		{
	         public Object doInHibernate(Session session)
	         {
	     	    Query query = session.getNamedQuery("getAvailableNewsItemsForDate");
	     	    query.setParameter(0, d);
	     	    query.setParameter(1, d);
	     		query.setFirstResult(offset);
	     		query.setMaxResults(numToFetch);
	     		query.setFetchSize(numToFetch);
                return query.list();
	         }
		});
		
	}

	@SuppressWarnings("unchecked")
	public List<NewsItem> getNewsItems(
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		List<NewsItem> newsItems = (List<NewsItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getNewsItemsOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getNewsItemsOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
        });

        return newsItems;
	}


}
