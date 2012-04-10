
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

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherDAO;
import edu.ur.order.OrderType;


/**
 * Database access for a researcher.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbResearcherDAO implements ResearcherDAO {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 360225302639821186L;

	/**  Helper for persisting information using hibernate.   */
	private final HbCrudDAO<Researcher> hbCrudDAO;
	
	/**  Logger  */
	private static final Logger log = Logger.getLogger(HbResearcherDAO.class);

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
	 * Get a count of the researchers.  This includes researchers
	 * who have set their pages to private or public
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("researcherCount");
		return (Long)q.uniqueResult();
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
		return (List<Researcher>) hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllPublicResearchers").list();
	}
	

	/**
	 * This will return all researchers including those who have set their researcher pages to private.
	 * 
	 * Get researchers starting from the specified row and end at specified row.
     * The rows will be sorted by the specified parameter in given order.
	 *  
     * @return List of researchers 
     */
	@SuppressWarnings("unchecked")
	public List<Researcher> getResearchersByLastFirstName(final int rowStart, final int maxResults, final OrderType orderType) {
		
		log.debug("order type = " + orderType);

		Query q = null;
		if (orderType.equals(OrderType.ASCENDING_ORDER)) {
		    q =  hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllResearcherByLastFirstNameAsc");
		} else {
		    q =  hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllResearcherByLastFirstNameDesc");
		}
		        
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setReadOnly(true);
		q.setFetchSize(maxResults);
	    return q.list();
   
	}
	
	/**
	 * This returns only researchers who have set their page to public.
	 * 
	 * Get researchers starting from the specified row and end at specified row.
     * The rows will be sorted by the specified parameter in given order.
	 *  
     * @return List of researchers 
     */
	@SuppressWarnings("unchecked")
	public List<Researcher> getPublicResearchersByLastFirstName(final int rowStart, final int maxResults, final OrderType orderType) 
	{
	    Query q = null;
		if (orderType.equals(OrderType.ASCENDING_ORDER)) {
		    q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllPublicResearcherByLastFirstNameAsc");
		} else {
		    q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllPublicResearcherByLastFirstNameDesc");
		}
		        
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setReadOnly(true);
		q.setFetchSize(maxResults);
	    return q.list();
   
	}

	/**
	 * Get a count of public researchers
	 * 
	 * @see edu.ur.ir.researcher.ResearcherDAO#getPublicResearcherCount()
	 */
	public Long getPublicResearcherCount() {
		return (Long)hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("publicResearcherCount").uniqueResult();
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
          Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
          criteria.add(Restrictions.in("id",researcherIds));
          foundResearchers = criteria.list();
        }
		return foundResearchers;
	}
}
