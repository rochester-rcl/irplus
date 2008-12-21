
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

package edu.ur.hibernate.ir.researcher.db;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.dao.CriteriaHelper;
import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherDAO;


/**
 * Database access for a researcher.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbResearcherDAO implements ResearcherDAO {
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<Researcher> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbResearcherDAO() {
		hbCrudDAO = new HbCrudDAO<Researcher>(Researcher.class);
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
	 * Get a count of the researchers.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("researcherCount"));
	}
	
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	public Researcher getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(Researcher entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	public void makeTransient(Researcher entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get all researchers having public page.
	 * 
	 * @return List of researchers 
	 */
	@SuppressWarnings("unchecked")
	public List<Researcher> getAllPublicResearchers() {
		return (List<Researcher>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllPublicResearchers");
	}
	
	/**
	 * Get the researchers ordered by id starting at the given offset.
	 * 
	 * @param offset
	 * @param maxNumToFetch
	 * @return list of found researchers
	 */
	@SuppressWarnings("unchecked")
	public List<Researcher> getPublicResearchersOrderedByLastFirstName( final int offset, final int maxNumToFetch)
	{
		List<Researcher> foundResearchers = (List<Researcher>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    q = session.getNamedQuery("getAllPublicByLastFirstName");
			    q.setFirstResult(offset);
			    q.setMaxResults(maxNumToFetch);
			    q.setReadOnly(true);
			    q.setFetchSize(maxNumToFetch);
	            return q.list();
		    }
	    });
		
		return foundResearchers;
	}

	/**
	 * Get researchers starting from the specified row and end at specified row.
     * The rows will be sorted by the specified parameter in given order.
	 *  
     * @return List of researchers 
     */
	@SuppressWarnings("unchecked")
	public List<Researcher> getResearchers(final int rowStart, final int maxResults, final String propertyName, final String orderType) {
		List<Researcher> researchers  = 
			(List<Researcher>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                criteria.setFirstResult(rowStart);
                criteria.setMaxResults(maxResults);
                criteria.setFetchSize(maxResults);
                
                if (orderType.equalsIgnoreCase(CriteriaHelper.ASC)) {
                	criteria.createCriteria("user").addOrder(Order.asc(propertyName).ignoreCase());
                } else {
                	criteria.createCriteria("user").addOrder(Order.desc(propertyName).ignoreCase());
                }
                return criteria.list();
            }
        });

        return researchers;
	}

	
	/**
	 * Get a count of public researchers
	 * 
	 * @see edu.ur.ir.researcher.ResearcherDAO#getPublicResearcherCount()
	 */
	public Long getPublicResearcherCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("publicResearcherCount"));

	}


	/**
	 * Get researchers by given ids
	 * 
	 * @see edu.ur.ir.researcher.ResearcherDAO#getResearchers(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<Researcher> getResearchers(final List<Long> researcherIds) {
		
		List<Researcher> foundResearchers = new LinkedList<Researcher>();
		if( researcherIds.size() > 0 )
        {
			foundResearchers = (List<Researcher>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
		    public Object doInHibernate(Session session)
                throws HibernateException, SQLException {
                    Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                    criteria.add(Restrictions.in("id",researcherIds));
                return criteria.list();
                }
             });
        }
		return foundResearchers;

	}
}
