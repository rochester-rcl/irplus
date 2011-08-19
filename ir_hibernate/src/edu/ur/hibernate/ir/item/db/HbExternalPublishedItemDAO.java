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


import java.util.List;

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

	/**
	 * Get the extneral published item by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ExternalPublishedItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Add the external published item to persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ExternalPublishedItem entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Remove the external published item from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ExternalPublishedItem entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get the count fo the publisher.
	 * 
	 * @see edu.ur.ir.item.ExternalPublishedItemDAO#getCountForPublisher(java.lang.Long)
	 */
	public Long getCountForPublisher(Long publisherId)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("externalPublishedItemCountByPublisher");
		q.setParameter("publisherId", publisherId);
		return (Long)q.uniqueResult();
	}

	

	/**
	 * Get all publishers.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

}
