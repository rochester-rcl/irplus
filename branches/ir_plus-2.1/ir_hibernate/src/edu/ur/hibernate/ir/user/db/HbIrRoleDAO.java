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

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrRoleDAO;

/**
 * Data access for an ir role.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIrRoleDAO implements IrRoleDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = -5626825096552629162L;
	
	/** hibernate helper  */
	private final HbCrudDAO<IrRole> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbIrRoleDAO() {
		hbCrudDAO = new HbCrudDAO<IrRole>(IrRole.class);
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
	 * Get a count of the roles in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irRoleCount"));
	}

	/**
	 * Get all roles in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<IrRole> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(IrRole.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<IrRole>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all roles in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<IrRole> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllIrRoleNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the role by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public IrRole findByUniqueName(String name) {
		return (IrRole) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrRoleByName", name));
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	public IrRole getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(IrRole entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(IrRole entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List<IrRole> getRoles(final List<Long> roleIds) {
		List<IrRole> foundRoles = new LinkedList<IrRole>();
		if( roleIds.size() > 0 )
        {
			foundRoles = (List<IrRole>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
		    public Object doInHibernate(Session session)
                throws HibernateException, SQLException {
                    Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                    criteria.add(Restrictions.in("id",roleIds));
                return criteria.list();
                }
             });
        }
		return foundRoles;
	}

	@SuppressWarnings("unchecked")
	public List<IrRole> getRoles(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		List<IrRole> roles = 
			(List<IrRole>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getRolesOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getRolesOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
        });

        return roles;
	}
}
