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

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.person.BirthDate;
import edu.ur.ir.person.BirthDateDAO;

/**
 * Allows birth date to be saved to the database.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbBirthDateDAO implements BirthDateDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 7232997042568872544L;

	/** hibernate data access object  */
	private final HbCrudDAO<BirthDate> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbBirthDateDAO() {
		hbCrudDAO = new HbCrudDAO<BirthDate>(BirthDate.class);
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
	 * Get a count of the birth date in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("birthDateCount");
		return (Long)q.uniqueResult();
	}

	public BirthDate getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(BirthDate entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(BirthDate entity) {
		hbCrudDAO.makeTransient(entity);
	}
}
