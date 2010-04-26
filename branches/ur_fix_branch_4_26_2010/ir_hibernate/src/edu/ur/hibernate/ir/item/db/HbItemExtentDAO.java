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
import edu.ur.ir.item.ExtentType;
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

	/**
	 * Get the item extent by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ItemExtent getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Save the item extent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ItemExtent entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Delete the item extent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ItemExtent entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get all of the item extents
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<ItemExtent> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get the count of items that use specified extent type.
	 * 
	 * @see edu.ur.ir.item.ItemExtentDAO#getItemCount(edu.ur.ir.item.ExtentType)
	 */
	public Long getItemCount(ExtentType extentType) {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("extentTypeItemCount", extentType.getId()));
	}

}
