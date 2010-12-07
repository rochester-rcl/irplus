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

package edu.ur.hibernate.ir.security.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypeDAO;

/**
 * Hibernate Persist for the class type class.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIrClassTypeDAO implements IrClassTypeDAO {
	
	/** eclipse gnerated id */
	private static final long serialVersionUID = 3869620609269121628L;
	
	/** helper for persistence operations */
	private final HbCrudDAO<IrClassType> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbIrClassTypeDAO() {
		hbCrudDAO = new HbCrudDAO<IrClassType>(IrClassType.class);
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
	 * Get a count of the permissions in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irClassTypeCount"));
	}

	/**
	 * Get all roles in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<IrClassType> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(IrClassType.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<IrClassType>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all roles in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<IrClassType> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllIrClassTypeNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the role by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public IrClassType findByUniqueName(String name) {
		return (IrClassType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrClassTypeByName", name));
	}
	
	/**
	 * Get the class type by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IrClassType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	/**
	 * Make the class type persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IrClassType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	
	/**
	 * Make the class type transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IrClassType entity) {
		hbCrudDAO.makeTransient(entity);
	}
}
