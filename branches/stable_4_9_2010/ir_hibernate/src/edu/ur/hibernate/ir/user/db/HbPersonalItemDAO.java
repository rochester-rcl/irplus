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

package edu.ur.hibernate.ir.user.db;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.PersonalItemDAO;

/**
 * Hibernate implementation of personal item data access and storage.
 * 
 * @author Nathan Sarr
 *
 */
public class HbPersonalItemDAO implements PersonalItemDAO {

	/** Logger */
	private static final Logger log = Logger.getLogger( HbPersonalItemDAO.class);
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<PersonalItem> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbPersonalItemDAO() {
		hbCrudDAO = new HbCrudDAO<PersonalItem>(PersonalItem.class);
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
	 * Return all PersonalItems in the system
	 */
	public List<PersonalItem> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Return PersonalItem by id
	 */
	public PersonalItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(PersonalItem entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(PersonalItem entity) {
		hbCrudDAO.makeTransient(entity);
	}
	

	/**
	 * Find the specified items for the given user.
	 * 
	 * @see edu.ur.ir.user.PersonalItemDAO#getPersonalItems(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalItem> getPersonalItems(final Long userId, final List<Long> itemIds) {
		
		String[] parameters = {"ownerId", "itemIds"};
		Object[] values = {userId, itemIds};
		List<PersonalItem> foundItems = new LinkedList<PersonalItem>();
		if( itemIds.size() > 0 )
		{
		    foundItems = 
		        (List<PersonalItem>) hbCrudDAO.getHibernateTemplate().findByNamedQueryAndNamedParam("getAllPersonalItemsInList", parameters, values);
		}
		
	
		return foundItems;
	}

	
	/**
	 * Get the items in the collection for the specified user.
	 * 
	 * @see edu.ur.ir.user.PersonalItemDAO#getItemsInCollectionForUser(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalItem> getPersonalItemsInCollectionForUser(Long userId,
			Long collectionId) {
		Object[] values = {userId, collectionId};
		return (List<PersonalItem>) (hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalItemsInCollectionForUser", values));
	}

	
	/**
	 * Get the root items for the specified user id.
	 * 
	 * @see edu.ur.ir.user.PersonalItemDAO#getRootItems(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalItem> getRootPersonalItems(Long userId) {
		return (List<PersonalItem>) (hbCrudDAO.getHibernateTemplate().
				findByNamedQuery("getRootPersonalItems", userId));
	}

	/**
	 * Get personal item which has specified generic item id as the latest version 
	 * 
	 * @param genericItemId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PersonalItem getPersonalItem(Long genericItemId) {
		// this can return multiple personal items if the generic item is contaned twice within the personal item  - for example
	    // for reverting to an older version so we will only return the first one.
		// it is an error to have the same generic item (unique id) within more than one personal item
		List<PersonalItem> items = hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalItemByGenericId", genericItemId);
		log.debug("found " + items.size() + " personal items ");
		if( items.size() == 0 )
		{
			return null;
		}
		else
		{
			return items.get(0);
		}
		
	}
}
