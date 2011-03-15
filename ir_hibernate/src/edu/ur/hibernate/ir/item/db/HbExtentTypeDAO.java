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
import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.ExtentTypeDAO;


/**
 * Data access for a extent type.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbExtentTypeDAO implements ExtentTypeDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = -3896900593325279312L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<ExtentType> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbExtentTypeDAO() {
		hbCrudDAO = new HbCrudDAO<ExtentType>(ExtentType.class);
	}

	/**
	 * Get a count of the extent types in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("extentTypeCount");
		return (Long)q.uniqueResult();
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
	 * Get all extent types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<ExtentType> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(ExtentType.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<ExtentType>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all extent types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<ExtentType> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllExtentTypeNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the extent type by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public ExtentType findByUniqueName(String name) {
		return (ExtentType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getExtentTypeByName", name));
	}

	public ExtentType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(ExtentType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(ExtentType entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}
	
	/**
	 * Get extent types.
	 * 
	 * @see edu.ur.ir.item.ExtentTypeDAO#getExtentTypesOrderByName(int, int, String)
	 */
	@SuppressWarnings("unchecked")
	public List<ExtentType> getExtentTypesOrderByName(
			final int rowStart, final int numberOfResultsToShow, final String sortType) {
		List<ExtentType> extentTypes = (List<ExtentType>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getExtentTypesOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getExtentTypesOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
        });
		
        return extentTypes;
	}

}
