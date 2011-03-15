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
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeDAO;


/**
 * Data access for a Language Type.
 * 
 * @author Nathan Sarr
 *
 */
public class HbLanguageTypeDAO implements LanguageTypeDAO {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -2715604545677048653L;
	
	/** hibernate helper */
	private final HbCrudDAO<LanguageType> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbLanguageTypeDAO() {
		hbCrudDAO = new HbCrudDAO<LanguageType>(LanguageType.class);
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("languageTypeCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get all language types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<LanguageType> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(LanguageType.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<LanguageType>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all language types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<LanguageType> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllLanguageTypeNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the language type by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public LanguageType findByUniqueName(String name) {
		return (LanguageType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getLanguageTypeByName", name));
	}

	public LanguageType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(LanguageType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(LanguageType entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get the language types order by name.
	 * 
	 * @see edu.ur.ir.item.LanguageTypeDAO#getLanguageTypesOrderByName(int, int, String)
	 */
	@SuppressWarnings("unchecked")
	public List<LanguageType> getLanguageTypesOrderByName(
			final int rowStart, final int numberOfResultsToShow, final String sortType) {
		List<LanguageType> languageTypes = (List<LanguageType>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getLanguageTypesOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getLanguageTypesOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
        });

        return languageTypes;

	}

	/**
	 * Get the unique system code for this language type
	 * 
	 * @see edu.ur.dao.UniqueSystemCodeDAO#getByUniqueSystemCode(java.lang.String)
	 */
	public LanguageType getByUniqueSystemCode(String uniqueSystemCode) {
		return (LanguageType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getLanguageTypeByUniqueSystemCode", uniqueSystemCode));
	}
}
