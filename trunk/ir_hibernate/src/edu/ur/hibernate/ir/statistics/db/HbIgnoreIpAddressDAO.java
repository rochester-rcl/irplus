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

import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
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
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("ipCount"));
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
		List<IgnoreIpAddress> ipAddresses = (List<IgnoreIpAddress>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getIgnoreIPOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getIgnoreIPOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();

            }
        });

        return ipAddresses;
	}

	/**
	 * Get ip address for specified range
	 * 
	 * @see edu.ur.ir.statistics.IgnoreIpAddressDAO#getIgnoreIpAddress(IgnoreIpAddress)
	 */
	public IgnoreIpAddress getIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress) {
		Integer[] ipaddress = new Integer[] {ignoreIpAddress.getFromAddress1(), 
				ignoreIpAddress.getFromAddress2(), ignoreIpAddress.getFromAddress3(), ignoreIpAddress.getFromAddress4(), ignoreIpAddress.getToAddress4()};
		
		return (IgnoreIpAddress) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIgnoreIpAddress", ipaddress));

	}
	
	
	/**
	 * Get the count of times the ip address shows up in ignore addresses.  A count of 0 means that
	 * it should not be ignored.
	 * 
	 * @param ipAddress
	 * @return the count of times this address was found to be within a given ingore range
	 */
	public Long getIgnoreCountForIp(String ipAddress, boolean storeCount)
	{
        StringTokenizer token = new StringTokenizer(ipAddress, ".");
		
		Integer ipAddressPart1 = Integer.parseInt(token.nextToken());
		Integer ipAddressPart2 = Integer.parseInt(token.nextToken());
		Integer ipAddressPart3 = Integer.parseInt(token.nextToken());
		Integer ipAddressPart4 = Integer.parseInt(token.nextToken());
		
		return getIgnoreCountForIp(ipAddressPart1, ipAddressPart2, ipAddressPart3, ipAddressPart4, storeCount);
	}
	
	/**
	 * Get the count of times the ip address shows up in ignore addresses.  A count of 0 means that
	 * it should not be ignored.
	 * 
	 * the address is expected in the format NNN.NNN.NNN.NNN
	 * one example would be 192.9.44.23
	 * 
	 * @param part1 - first part of the ip address (192)
	 * @param part2 - second part of the ip address (9)
	 * @param part3 - third part of the ip address  (44)
	 * @param part4 - forth part of the ip address (23)
	 * 
	 * @return the count of times this address was found to be within a given ingore range
	 */
	public Long getIgnoreCountForIp(final Integer part1, 
			final Integer part2,
			final Integer part3, 
			final Integer part4, 
			final boolean storeCount)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getIgnoreCountForIp");
		q.setParameter("part1", part1);
		q.setParameter("part2", part2);
		q.setParameter("part3", part3);
		q.setParameter("part4", part4);
		q.setParameter("storeCount", storeCount);
	    return (Long)q.uniqueResult();
	}


}
