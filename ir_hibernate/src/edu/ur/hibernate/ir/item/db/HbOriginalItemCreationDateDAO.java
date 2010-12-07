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

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.item.OriginalItemCreationDate;
import edu.ur.ir.item.OriginalItemCreationDateDAO;

/**
 * Allows release date to be saved to the database.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbOriginalItemCreationDateDAO implements OriginalItemCreationDateDAO{
	
	/**  eclipse generated id */
	private static final long serialVersionUID = 3272740631478860735L;
	
	/** hibernate helper*/
	private final HbCrudDAO<OriginalItemCreationDate> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbOriginalItemCreationDateDAO() {
		hbCrudDAO = new HbCrudDAO<OriginalItemCreationDate>(OriginalItemCreationDate.class);
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
	 * Get a count of the published date in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("originalItemCreationDateCount"));
	}

	public OriginalItemCreationDate getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(OriginalItemCreationDate entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(OriginalItemCreationDate entity) {
		hbCrudDAO.makeTransient(entity);
	}
}
