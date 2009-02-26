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

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.item.ItemExtent;
import edu.ur.ir.item.ItemExtentDAO;

/**
 * Hibernate implementation of Item Extent type.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbItemExtentDAO implements ItemExtentDAO{
	
	private final HbCrudDAO<ItemExtent> hbCrudDAO;


	/**
	 * Default Constructor
	 */
	public HbItemExtentDAO() {
		hbCrudDAO = new HbCrudDAO<ItemExtent>(ItemExtent.class);
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
	 * Get the item extent by type and value
	 * 
	 * @param extentTypeId the type of extent
	 * @param value the value of the extent.
	 * 
	 * @return the item extent found.
	 */
	public ItemExtent getByTypeValue(Long extentTypeId, String value) {
		Object[] values = new Object[] {extentTypeId, value};
		return (ItemExtent)HbHelper.getUnique( hbCrudDAO.getHibernateTemplate().findByNamedQuery("getItemExtent", values));
	}

	/**
	 * Get a count of the extent types in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("itemExtentCount"));
	}

	public ItemExtent getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(ItemExtent entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(ItemExtent entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

}
