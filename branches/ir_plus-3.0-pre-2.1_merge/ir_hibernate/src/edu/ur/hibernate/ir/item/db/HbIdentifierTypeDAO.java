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
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeDAO;


/**
 * Data access for a identifier type.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIdentifierTypeDAO implements IdentifierTypeDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = -8941283937307667712L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<IdentifierType> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbIdentifierTypeDAO() {
		hbCrudDAO = new HbCrudDAO<IdentifierType>(IdentifierType.class);
	}

	/**
	 * Get a count of the identifier types in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("identifierTypeCount");
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
	 * Get all identifier types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<IdentifierType> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(IdentifierType.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<IdentifierType>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all identifier types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<IdentifierType> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllIdentifierTypeNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the identifier type by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public IdentifierType findByUniqueName(String name) {
		return (IdentifierType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIdentifierTypeByName", name));
	}

	public IdentifierType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(IdentifierType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(IdentifierType entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}
	
	/**
	 * Get identifier types.
	 * 
	 * @see edu.ur.ir.item.IdentifierTypeDAO#getIdentifierTypesOrderByName( int, int, String)
	 */
	@SuppressWarnings("unchecked")
	public List<IdentifierType> getIdentifierTypesOrderByName(
			final int rowStart, final int numberOfResultsToShow, final String sortType) {
		List<IdentifierType> identifierTypes = (List<IdentifierType>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getIdentifierTypesOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getIdentifierTypesOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
        });
        return identifierTypes;
	}

	
	/**
	 * Get the identifier type by the unique systme code.
	 * 
	 * @see edu.ur.dao.UniqueSystemCodeDAO#getByUniqueSystemCode(java.lang.String)
	 */
	public IdentifierType getByUniqueSystemCode(String uniqueSystemCode) {
		return (IdentifierType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIdentifierTypeByUniqueSystemCode", uniqueSystemCode));
	}
}
