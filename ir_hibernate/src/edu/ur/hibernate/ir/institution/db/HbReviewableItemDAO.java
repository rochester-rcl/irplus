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

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.institution.ReviewableItem;
import edu.ur.ir.institution.ReviewableItemDAO;

/**
 * Implementation of relational storage for an reviewable item.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbReviewableItemDAO implements ReviewableItemDAO {
	

	/** eclipse generated id */
	private static final long serialVersionUID = 7112022753156800986L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<ReviewableItem> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbReviewableItemDAO() {
		hbCrudDAO = new HbCrudDAO<ReviewableItem>(ReviewableItem.class);
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
	 * Get an reviewable item by it's id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ReviewableItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Add an reviewable item to relational storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ReviewableItem entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	/**
	 * Remove an reviewable item from relational storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ReviewableItem entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get all review pending items
	 * 
	 * @see edu.ur.ir.institution.ReviewableItemDAO#getAllPendingItems()
	 */
	@SuppressWarnings("unchecked")
	public List<ReviewableItem> getAllPendingItems() {
		List<ReviewableItem> items = (List<ReviewableItem>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllPendingItems", ReviewableItem.PENDING_REVIEW);
		return items;
	}
	
	/**
	 * Get review history for specified item id
	 * 
	 * @see edu.ur.ir.institution.ReviewableItemDAO#getReviewHistoryByItem(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ReviewableItem> getReviewHistoryByItem(Long itemId) {
		List<ReviewableItem> items = (List<ReviewableItem>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("geReviewItemByGenericItemId", itemId);
		return items;
	}
}
