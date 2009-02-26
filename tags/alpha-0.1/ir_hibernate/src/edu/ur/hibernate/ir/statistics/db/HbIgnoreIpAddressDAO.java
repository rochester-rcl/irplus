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

	/**
	 * Helper for persisting information using hibernate. 
	 */
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

	public List<IgnoreIpAddress> getAll() {
		return hbCrudDAO.getAll();
	}

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
}
