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


import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.ExternalPublishedItemDAO;


/**
 * Data access for External Published Item
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbExternalPublishedItemDAO implements ExternalPublishedItemDAO {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 1136968738283348741L;
	
	/** hibernate helper  */
	private final HbCrudDAO<ExternalPublishedItem> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbExternalPublishedItemDAO() {
		hbCrudDAO = new HbCrudDAO<ExternalPublishedItem>(ExternalPublishedItem.class);
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("sponsorCount");
		return (Long)q.uniqueResult();
	}

	public ExternalPublishedItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(ExternalPublishedItem entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(ExternalPublishedItem entity) {
		hbCrudDAO.makeTransient(entity);
	}
}
