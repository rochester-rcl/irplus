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

package edu.ur.hibernate.ir.statistics.db;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.statistics.IgnoreIpAddress;
import edu.ur.ir.statistics.IgnoreIpAddressDAO;

/**
 * IP address persistance
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbIgnoreIpAddressDAO implements IgnoreIpAddressDAO {

	/** eclipse generated id  */
	private static final long serialVersionUID = 5106488416473298525L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<IgnoreIpAddress> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbIgnoreIpAddressDAO() {
		hbCrudDAO = new HbCrudDAO<IgnoreIpAddress>(IgnoreIpAddress.class);
	}
	
	/**
	 * Get a count of the ip addresses in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("ipCount");
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
	 * Get all ip addresses.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<IgnoreIpAddress> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * GEt the ignore ip address by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IgnoreIpAddress getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Save ignore ip address
	 *  
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IgnoreIpAddress entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Delete ignore ip address
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IgnoreIpAddress entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the Ignore IpAddresses based on the criteria.
	 * 
	 * @see edu.ur.ir.statistics.IgnoreIpAddressDAO#getIgnoreIpAddresses(int, int, String)
	 */
	@SuppressWarnings("unchecked")
	public List<IgnoreIpAddress> getIgnoreIpAddresses(
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		
		Query q = null;
		if (sortType.equalsIgnoreCase("asc")) 
		{
		    q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getIgnoreIPOrderByNameAsc");
		} else 
		{
		    q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getIgnoreIPOrderByNameDesc");
		}
			    
		q.setFirstResult(rowStart);
		q.setMaxResults(numberOfResultsToShow);
		q.setReadOnly(true);
		q.setFetchSize(numberOfResultsToShow);
	    return q.list();
	}

	/**
	 * Get the count of times the ip address shows up in ignore addresses.  A count of 0 means that
	 * it should not be ignored.
	 * 
	 * @param ipAddress
	 * @return the count of times this address was found to be within a given ignore range
	 */
	public IgnoreIpAddress getByAddress(String ipAddress)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getIgnoreIpAddress");
		q.setParameter("address", ipAddress);
	    return (IgnoreIpAddress)q.uniqueResult();
	}
	



}
