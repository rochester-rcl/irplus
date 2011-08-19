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

package edu.ur.hibernate.ir.user.db;

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
import edu.ur.ir.user.Affiliation;
import edu.ur.ir.user.AffiliationDAO;

/**
 * Data access for an affiliation.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbAffiliationDAO implements AffiliationDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 7289931560567873805L;
	
	private final HbCrudDAO<Affiliation> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbAffiliationDAO() {
		hbCrudDAO = new HbCrudDAO<Affiliation>(Affiliation.class);
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
	 * Get a count of the affiliations in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("affiliationCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get all affiliations in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<Affiliation> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(Affiliation.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<Affiliation>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all affiliations in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<Affiliation> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllAffiliationNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the role by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public Affiliation findByUniqueName(String name) {
		return (Affiliation) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAffiliationByName", name));
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	public Affiliation getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(Affiliation entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(Affiliation entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List<Affiliation> getAffiliations(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		List<Affiliation> affiliations = 
			(List<Affiliation>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getAffiliationsOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getAffiliationsOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();

            }
        });

        return affiliations;
	}
}
