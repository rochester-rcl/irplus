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

package edu.ur.hibernate.ir.institution.db;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionDAO;
import edu.ur.ir.institution.InstitutionalItemVersionDownloadCount;
import edu.ur.order.OrderType;

/**
 * Persistence of Institutional item version data.
 * 
 * @author Nathan Sarr
 *
 */
public class HbInstitutionalItemVersionDAO implements InstitutionalItemVersionDAO{
	
	/** Helper for persisting information using hibernate.  */
	private final HbCrudDAO<InstitutionalItemVersion> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbInstitutionalItemVersionDAO() {
		hbCrudDAO = new HbCrudDAO<InstitutionalItemVersion>(InstitutionalItemVersion.class);
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
	 * Get all Institutional item version data.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get a Institutional item version by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public InstitutionalItemVersion getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the Institutional item version persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(InstitutionalItemVersion entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the Institutional item version transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(InstitutionalItemVersion entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the  publications for a given set of names ordered by title.
	 * 
	 * @param rowStart - start position
	 * @param maxResults - maximum number of results to return
	 * @param personNameIds - set of name ids to get 
	 * @param orderType - way to order the set
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByTitle(final int rowStart,
			final int maxResults, 
			final List<Long> personNameIds, 
			final OrderType orderType)
	{
		List<InstitutionalItemVersionDownloadCount> foundItems = new LinkedList<InstitutionalItemVersionDownloadCount>();
		foundItems = (List<InstitutionalItemVersionDownloadCount>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getPublicationVersionsByPersonNameIdTitleDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getPublicationVersionsByPersonNameIdTitleAsc");
			    }
			    
			    q.setParameterList("personNameIds", personNameIds);
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
			    return q.list();
			    
		    }
	    });
        return foundItems;	
	}
	
	/**
	 * Get the  publications for a given set of names by title.
	 * 
	 * @param rowStart - start position
	 * @param maxResults - maximum number of results to return
	 * @param personNameIds - set of name ids to get 
	 * @param orderType - way to order the set
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByDownload(final int rowStart,
			final int maxResults, 
			final List<Long> personNameIds, 
			final OrderType orderType)
	{
		List<InstitutionalItemVersionDownloadCount> foundItems = new LinkedList<InstitutionalItemVersionDownloadCount>();
		foundItems = (List<InstitutionalItemVersionDownloadCount>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getPublicationVersionsByPersonNameIdDownloadDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getPublicationVersionsByPersonNameIdDownloadAsc");
			    }
			    
			    q.setParameterList("personNameIds", personNameIds);
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
			    return q.list();
			    
		    }
	    });
        return foundItems;	
	}

	/**
	 * Get the institutional item version by handle id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemVersionByHandleId(java.lang.Long)
	 */
	public InstitutionalItemVersion getItemVersionByHandleId(Long handleId) {
		return (InstitutionalItemVersion)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getInstitutionalItemByHandleId", handleId));
	}
    
	/**
	 * Get the download count for a sponsor
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getDownloadCountForSponsor(java.lang.Long)
	 */
	public Long getDownloadCountForSponsor(Long sponsorId) {
		
		Query q = hbCrudDAO.getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("getDownloadCountBySponsor");
		q.setLong(0, sponsorId);
	    Long count = (Long)q.uniqueResult();
	    if( count == null )
	    {
	    	count = Long.valueOf(0l);
	    }
	    return count;
	}
	
	/**
	 *  (non-Javadoc)
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getItemsBySponsorItemNameOrder(int, int, long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemNameOrder(final int rowStart,
			final int maxResults, final long sponsorId, final OrderType orderType) {
		List<InstitutionalItemVersionDownloadCount> foundItems = new LinkedList<InstitutionalItemVersionDownloadCount>();
		foundItems = (List<InstitutionalItemVersionDownloadCount>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getPublicationsForSponsorDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getPublicationsForSponsorAsc");
			    }
			    
			    q.setLong("sponsorId", sponsorId);
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
			    return q.list();
			    
		    }
	    });
        return foundItems;	
	}

	
	/**
	 * Get a count of the number of items sponsored by a particular person.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getItemsBySponsorCount(long)
	 */
	public Long getItemsBySponsorCount(long sponsorId) {
        Query q = hbCrudDAO.getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("getPublicationsForSponsorCount");
	    q.setLong("sponsorId", sponsorId);
	    Long count = (Long)q.uniqueResult();
	    if( count == null )
	    {
	    	count = Long.valueOf(0l);
	    }
	    return count;

	}

	/**
	 * Get items by sponsor item deposit date order.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsBySponsorItemDepositDateOrder(int, int, long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemDepositDateOrder(
			final int rowStart, final int maxResults, final long sponsorId, final OrderType orderType) {
		List<InstitutionalItemVersionDownloadCount> foundItems = new LinkedList<InstitutionalItemVersionDownloadCount>();
		
		foundItems = (List<InstitutionalItemVersionDownloadCount>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getPublicationsForSponsorDepositDateDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getPublicationsForSponsorDepositDateAsc");
			    }
			    
			    q.setLong("sponsorId", sponsorId);
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
	            return q.list();
		    }
	    });
        return foundItems;	
	}
	
	/**
	 * Get items by sponsor item deposit download order.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsBySponsorItemDepositDateOrder(int, int, long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemDownloadOrder(
			final int rowStart, final int maxResults, final long sponsorId, final OrderType orderType) {
		List<InstitutionalItemVersionDownloadCount> foundItems = new LinkedList<InstitutionalItemVersionDownloadCount>();
		
		foundItems =  (List<InstitutionalItemVersionDownloadCount>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getPublicationsSponsorDownloadCountOrderByCountDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getPublicationsSponsorDownloadCountOrderByCountAsc");
			    }
			    
			    q.setLong("sponsorId", sponsorId);
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
			    return q.list();
		    }
	    });
        return foundItems;	
	}

	
	/**
	 * Get the download counts for a set of person names.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getDownloadCountByPersonName(java.util.List)
	 */
	public Long getDownloadCountByPersonName(List<Long> personNameIds) {
		Query q = hbCrudDAO.getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("getDownloadCountByPersonNames");
	    q.setParameterList("personNameIds", personNameIds);
	    Long count = (Long)q.uniqueResult();
	    if( count == null )
	    {
	    	count = Long.valueOf(0l);
	    }
	    return count;
	}


}
