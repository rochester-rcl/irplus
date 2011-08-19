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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.item.ItemLink;
import edu.ur.ir.item.ItemLinkDAO;

/**
 * Allows an item link to be saved to the database.
 * 
 * @author Nathan Sarr
 *
 */
public class HbItemLinkDAO implements ItemLinkDAO{
	
	/** eclipse generated id  */
	private static final long serialVersionUID = -4385661707118349660L;

	/** hibernate helper  */
	private final HbCrudDAO<ItemLink> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbItemLinkDAO() {
		hbCrudDAO = new HbCrudDAO<ItemLink>(ItemLink.class);
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
	 * Get a count of the item links in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("itemLinkCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get all item links in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<ItemLink> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(ItemLink.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<ItemLink>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all item links in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<ItemLink> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllItemLinksAsc", startRecord, numRecords);
	}

	public ItemLink getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(ItemLink entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(ItemLink entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
