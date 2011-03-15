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

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrClassTypePermissionDAO;

/**
 * Hibernate Persist for the class type permission class.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIrClassTypePermissionDAO implements IrClassTypePermissionDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = 780132132307695771L;
	
	/** helper for persistence operations */
	private final HbCrudDAO<IrClassTypePermission> hbCrudDAO;
	
	
	/**
	 * Default Constructor
	 */
	public HbIrClassTypePermissionDAO() {
		hbCrudDAO = new HbCrudDAO<IrClassTypePermission>(IrClassTypePermission.class);
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irClassTypePermissionCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get all roles in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<IrClassTypePermission> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(IrClassTypePermission.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<IrClassTypePermission>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all roles in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<IrClassTypePermission> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllIrClassTypePermissionNameAsc", startRecord, numRecords);
	}

	/**
	 * Get permissions for class type
	 * 
	 * @see edu.ur.ir.security.IrClassTypePermissionDAO#getClassTypePermissionByClassType(String classType) 
	 */
	@SuppressWarnings("unchecked")
	public List<IrClassTypePermission> getClassTypePermissionByClassType(String classType) {
		return (List<IrClassTypePermission>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrClassTypePermissionByClassType", classType);
	}
	
	
	/**
	 * Find the class type permission by name and class type.
	 * 
	 * @see edu.ur.ir.security.IrClassTypePermissionDAO#getClassTypePermissionByNameAndClassType(java.lang.String, java.lang.String)
	 */
	public IrClassTypePermission getClassTypePermissionByNameAndClassType(
			String classType, String name) {
		Object[] values = new Object[] {name, classType};
		return (IrClassTypePermission) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrClassTypePermissionByNameAndClass", 
				values));
	}
	
	/**
	 * Get the class type permission by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IrClassTypePermission getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	/**
	 * Save the class type permission.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IrClassTypePermission entity) {
		hbCrudDAO.makePersistent(entity);
	}

	
	/**
	 * Make the class type permission transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IrClassTypePermission entity) {
		hbCrudDAO.makeTransient(entity);
	}


}
