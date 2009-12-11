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
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.IrUserGroupDAO;

/**
 * Group persistence
 * 
 * @author Nathan Sarr
 *
 */
public class HbIrUserGroupDAO implements IrUserGroupDAO{
	
	private final HbCrudDAO<IrUserGroup> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbIrUserGroupDAO() {
		hbCrudDAO = new HbCrudDAO<IrUserGroup>(IrUserGroup.class);
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
	 * Get a count of the groups in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("groupCount"));
	}

	/**
	 * Get all groups in group name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<IrUserGroup> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(IrUserGroup.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<IrUserGroup>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all groups in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<IrUserGroup> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllGroupNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the group by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public IrUserGroup findByUniqueName(String name) {
		return (IrUserGroup) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getGroupByName", name));
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	public IrUserGroup getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(IrUserGroup entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(IrUserGroup entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.user.IrUserGroupDAO#getUserGroups(int, int, String)
	 */
	@SuppressWarnings("unchecked")
	public List<IrUserGroup> getUserGroups(
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		List<IrUserGroup> userGroups = 
			(List<IrUserGroup>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getUserGroupsOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getUserGroupsOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();            }
        });

        return userGroups;
	}

	
	/**
	 * Get the user groups for the specified user.
	 * 
	 * @see edu.ur.ir.user.IrUserGroupDAO#getUserGroupsForUser(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<IrUserGroup> getUserGroupsForUser(Long userId) {
		return (List<IrUserGroup>)hbCrudDAO.getHibernateTemplate().findByNamedQuery("getGroupsForUser", userId);
	}

	
	/**
	 * Get User for group
	 * 
	 * @see edu.ur.ir.user.IrUserGroupDAO#getUserForGroup(java.lang.Long, java.lang.Long)
	 */
	public IrUser getUserForGroup(Long groupId, Long userId) {
		
		String[] paramNames = {"userId", "userGroupId"};
		Object[] values = {userId, groupId};
		return (IrUser)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQueryAndNamedParam("getUserForGroup", paramNames, values));
	}
	

}
