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
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorDAO;
import edu.ur.order.OrderType;


/**
 * Data access for Sponsor
 * 
 * @author Sharmila Ranganathan
 * @contributor Nathan Sarr
 *
 */
public class HbSponsorDAO implements SponsorDAO {
	
	private final HbCrudDAO<Sponsor> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbSponsorDAO() {
		hbCrudDAO = new HbCrudDAO<Sponsor>(Sponsor.class);
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
	 * Get a count of the sponsors in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("sponsorCount"));
	}

	/**
	 * Get all sponsors in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<Sponsor> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(Sponsor.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<Sponsor>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all sponsors in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<Sponsor> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllSponsorNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the sponsor by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public Sponsor findByUniqueName(String name) {
		return (Sponsor) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getSponsorByName", name));
	}

	public Sponsor getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(Sponsor entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(Sponsor entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	@SuppressWarnings("unchecked")
	public List<Sponsor> getSponsorsOrderByName(
			final int rowStart, final int numberOfResultsToShow, final OrderType orderType) {
		List<Sponsor> sponsors = (List<Sponsor>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (  orderType.equals(OrderType.ASCENDING_ORDER)) {
		        	q = session.getNamedQuery("getSponsorsOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getSponsorsOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();            }
        });
        return sponsors;
	}

	/**
	 *  
	 * @see edu.ur.ir.item.SponsorDAO#getByNameFirstChar(int, int, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<Sponsor> getByNameFirstChar(final int rowStart,final int maxResults,
			final char firstChar, final OrderType orderType) {
		List<Sponsor> sponsors = (List<Sponsor>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException 
            {
		        Query q = null;
		        if (  orderType.equals(OrderType.ASCENDING_ORDER)) {
		        	q = session.getNamedQuery("getSponsorsOrderByNameFirstCharAsc");
		        } else {
		        	q = session.getNamedQuery("getSponsorsOrderByNameFirstCharDesc");
		        }
			    q.setCharacter("sponsorFirstChar", Character.toLowerCase(firstChar));
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setReadOnly(true);
			    q.setFetchSize(maxResults);
	            return q.list();
	        }
        });
        return sponsors;
	}

	/**
	 * 
	 * @see edu.ur.ir.item.SponsorDAO#getCount(char)
	 */
	public Long getCount(char nameFirstChar) {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getSponsorsNameFirstCharCount", Character.toLowerCase(nameFirstChar)));
	}

	/**
	 * 
	 * @see edu.ur.ir.item.SponsorDAO#getCount(char, char)
	 */
	public Long getCount(char firstCharRange, char lastCharRange) {
		Object[] values = new Object[]{Character.valueOf(Character.toLowerCase(firstCharRange)), Character.valueOf(Character.toLowerCase(lastCharRange))};
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getSponsorsOrderByNameFirstCharRangeCount", values));

	}

	/**
	 * 
	 * @see edu.ur.ir.item.SponsorDAO#getSponsorsByNameBetweenChar(int, int, char, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<Sponsor> getSponsorsByNameBetweenChar(final int rowStart,
			final int maxResults, final char firstChar, final char lastChar,
			final OrderType orderType) 
	{
		List<Sponsor> sponsors = (List<Sponsor>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException 
            {
		        Query q = null;
		        if (  orderType.equals(OrderType.ASCENDING_ORDER)) {
		        	q = session.getNamedQuery("getSponsorsOrderByNameFirstCharRangeAsc");
		        } else {
		        	q = session.getNamedQuery("getSponsorsOrderByNameFirstCharRangeDesc");
		        }
			    q.setCharacter("firstChar", Character.toLowerCase(firstChar));
			    q.setCharacter("secondChar", Character.toLowerCase(lastChar));
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setReadOnly(true);
			    q.setFetchSize(maxResults);
	            return q.list();
	        }
        });
        return sponsors;
	}

	/**
	 * 
	 * @see edu.ur.ir.item.SponsorDAO#getCollectionSponsorsBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<Sponsor> getCollectionSponsorsBetweenChar(final int rowStart,
			final int maxResults, final InstitutionalCollection collection, final char firstChar,
			final char lastChar, final OrderType orderType) {
			
		 List<Sponsor> sponsors  = (List<Sponsor>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getCollectionSponsorNameByCharRangeOrderDesc");
			    }
		 	    else
			    {		 	    	                           
			        q = session.getNamedQuery("getCollectionSponsorNameByCharRangeOrderAsc");
			    }
			    
			    q.setLong(0, collection.getLeftValue());
			    q.setLong(1, collection.getRightValue());
			    q.setLong(2, collection.getTreeRoot().getId());
			    q.setCharacter( 3, Character.toLowerCase(firstChar) );
			    q.setCharacter( 4, Character.toLowerCase(lastChar) );
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
	            return q.list();
		    }
	    });
        return sponsors;	
	}

	/**
	 * @see edu.ur.ir.item.SponsorDAO#getCollectionSponsorsByChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<Sponsor> getCollectionSponsorsByChar(final int rowStart,
			final int maxResults, final InstitutionalCollection collection,
			final char firstChar, final OrderType orderType) {

		List<Sponsor> sponsors = (List<Sponsor>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getCollectionSponsorNameByCharOrderDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getCollectionSponsorNameByCharOrderAsc");
			    }
			    
			    q.setLong(0, collection.getLeftValue());
			    q.setLong(1, collection.getRightValue());
			    q.setLong(2, collection.getTreeRoot().getId());
			    q.setCharacter( 3, Character.toLowerCase(firstChar) );
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
	            return q.list();
		    }
	    });
        return sponsors;	
	}

	/**
	 * 
	 * @see edu.ur.ir.item.SponsorDAO#getCollectionSponsorsByName(int, int, edu.ur.ir.institution.InstitutionalCollection, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<Sponsor> getCollectionSponsorsByName(final int rowStart,
			final int maxResults, final InstitutionalCollection collection,
			final OrderType orderType) {
		
		 List<Sponsor> sponsors = (List<Sponsor>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getCollectionSponsorNameByNameOrderDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getCollectionSponsorNameByNameOrderAsc");
			    }
			    
			    q.setLong(0, collection.getLeftValue());
			    q.setLong(1, collection.getRightValue());
			    q.setLong(2, collection.getTreeRoot().getId());
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
	            return q.list();
		    }
	    });
	
        return sponsors;	
	}

	/**
	 * @see edu.ur.ir.item.SponsorDAO#getCount(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public Long getCount(InstitutionalCollection collection) {
		Object[] values = new Object[]{collection.getLeftValue(), collection.getRightValue(), 
				collection.getTreeRoot().getId()};

		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("sponsorCollectionNameCount", values));

	}

	/**
	 * 
	 * @see edu.ur.ir.item.SponsorDAO#getCount(edu.ur.ir.institution.InstitutionalCollection, char)
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstChar) {
		Object[] values = new Object[]{collection.getLeftValue(), collection.getRightValue(), 
				collection.getTreeRoot().getId(), Character.valueOf(Character.toLowerCase(nameFirstChar))};

		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("collectionSponsorNameCountByChar", values));

	}

	public Long getCount(InstitutionalCollection collection,
			char nameFirstCharRange, char nameLastCharRange) {
		Object[] values = new Object[]{collection.getLeftValue(), collection.getRightValue(), 
				collection.getTreeRoot().getId(), Character.valueOf(Character.toLowerCase(nameFirstCharRange)), Character.valueOf(Character.toLowerCase(nameLastCharRange))};

		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("sponsorCollectionNameCountByCharRange", values));

	}

}
