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

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;
import edu.ur.order.OrderType;

/**
 * Persist person information into the database.
 * 
 * @author Nathan Sarr
 *
 */
public class HbPersonNameAuthorityDAO implements PersonNameAuthorityDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -3472332062844173345L;
	
	/** Helper for persisting information using hibernate.*/
	private final HbCrudDAO<PersonNameAuthority> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbPersonNameAuthorityDAO() {
		hbCrudDAO = new HbCrudDAO<PersonNameAuthority>(PersonNameAuthority.class);
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
	 * Get the number of people in the system.
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personCount"));
	}
	
	/**
	 * Get all people ordered by authoritative name.
	 * 
	 * @param startRecord
	 * @param numRecords
	 * @return all people starting from start record up to number of records.
	 */
	public List<PersonNameAuthority> getAllAuthoritativeNameAsc(int startRecord, int numRecords)
	{
		return hbCrudDAO.getByQuery("getAllAuthoritativeNameAsc", startRecord, numRecords);
	}

	/**
	 * (non-Javadoc)
	 * @see edu.ur.ir.person.PersonNameAuthorityDAO#getPersonNameAuthorityByLastName(int, int, edu.ur.order.OrderType)
	 */
	public List<PersonNameAuthority> getPersonNameAuthorityByLastName(int rowStart,
			int maxResults, OrderType orderType) {
		
		if( orderType.equals(OrderType.DESCENDING_ORDER))
		{
			return hbCrudDAO.getByQuery("getAllPersonNameAuthorityDesc", rowStart, maxResults);
		}
		else
		{
			return hbCrudDAO.getByQuery("getAllPersonNameAuthorityAsc", rowStart, maxResults);
		}
		
	}

	public PersonNameAuthority getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(PersonNameAuthority entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(PersonNameAuthority entity) {
		hbCrudDAO.makeTransient(entity);
	}
	

}
