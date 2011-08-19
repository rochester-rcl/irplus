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
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.item.VersionedItemDAO;
import edu.ur.ir.user.IrUser;

/**
 * Represents an item that has one or more versions in the system.
 * 
 * @author Nathan Sarr
 *
 */
public class HbVersionedItemDAO implements VersionedItemDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -5349746984347671168L;

	/** Helper for persisting information using hibernate.  */
	private final HbCrudDAO<VersionedItem> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbVersionedItemDAO() {
		hbCrudDAO = new HbCrudDAO<VersionedItem>(VersionedItem.class);
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


	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("versionedItemCount");
		return (Long)q.uniqueResult();
	}

	public VersionedItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(VersionedItem entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(VersionedItem entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List<VersionedItem> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(VersionedItem.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<VersionedItem>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}


	@SuppressWarnings("unchecked")
	public List<VersionedItem> getAllVersionedItemsForUser(IrUser user) {
		return (List<VersionedItem>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllVersionedItemsForUser", user.getId());
	}

}
