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

package edu.ur.hibernate.ir.person.db;

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
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeDAO;

/**
 * Data access for a contributor type.
 * 
 * @author Nathan Sarr
 *
 */
public class HbContributorTypeDAO implements ContributorTypeDAO {
	
	/* eclipse generated id */
	private static final long serialVersionUID = 8813243420440799249L;

	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<ContributorType> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbContributorTypeDAO() {
		hbCrudDAO = new HbCrudDAO<ContributorType>(ContributorType.class);
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
	 * Get a count of the contributors in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("contributorTypeCount"));
	}

	/**
	 * Get all contributor types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<ContributorType> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(ContributorType.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<ContributorType>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all contributor types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<ContributorType> getAllOrderByName(int startRecord, int numRecords) {
	    return hbCrudDAO.getByQuery("getAllContributorTypeNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the contributor type by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public ContributorType findByUniqueName(String name) {
		return (ContributorType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getContributorTypeByName", name));
	}

	@SuppressWarnings("unchecked")
	public List<ContributorType> getContributorTypes(
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType){
		
			List<ContributorType> contributorTypes = (List<ContributorType>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
	            public Object doInHibernate(Session session)
	                    throws HibernateException, SQLException {
			        Query q = null;
			        if (sortType.equalsIgnoreCase("asc")) {
			        	q = session.getNamedQuery("getContributorTypesOrderByNameAsc");
			        } else {
			        	q = session.getNamedQuery("getContributorTypesOrderByNameDesc");
			        }
				    
				    q.setFirstResult(rowStart);
				    q.setMaxResults(numberOfResultsToShow);
				    q.setReadOnly(true);
				    q.setFetchSize(numberOfResultsToShow);
		            return q.list();
	            }
	        });
			
			 return contributorTypes;
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	public ContributorType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(ContributorType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(ContributorType entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the contributor type by the unique system code.
	 * 
	 * @see edu.ur.ir.person.ContributorTypeDAO#getByUniqueSystemCode(java.lang.String)
	 */
	public ContributorType getByUniqueSystemCode(String systemCode) {
		return (ContributorType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getContributorTypeByUniqueSystemCode", systemCode));

	}
}
