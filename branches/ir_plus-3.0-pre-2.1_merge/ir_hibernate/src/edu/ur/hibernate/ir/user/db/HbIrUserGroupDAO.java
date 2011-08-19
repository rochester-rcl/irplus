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

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
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
	
	/** eclipse generated id */
	private static final long serialVersionUID = 4325803282703890716L;

	/** hibernate helper */
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get all groups in group name order ascending.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<IrUserGroup> getAllNameOrder() {
	    Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
	    Query  q = session.getNamedQuery("getUserGroupsOrderByNameAsc");
	    return q.list();
    	
	}

	/**
	 * Get all groups in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<IrUserGroup> getAllOrderByName(int startRecord, int numRecords) {
		return this.getUserGroups(startRecord, numRecords, "asc");
	}

	/**
	 * Find the group by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public IrUserGroup findByUniqueName(String name) {
		
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
	    Query  q = session.getNamedQuery("getGroupByName");
	    q.setParameter("name", name);
	    return (IrUserGroup)q.uniqueResult();
	}

	/**
	 * Get the user group by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IrUserGroup getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the user group persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
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
			int rowStart, 
    		int numberOfResultsToShow, String sortType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
		if (sortType.equalsIgnoreCase("asc")) 
		{
		    q = session.getNamedQuery("getUserGroupsOrderByNameAsc");
		} 
		else 
		{
		    q = session.getNamedQuery("getUserGroupsOrderByNameDesc");
		}
			    
		q.setFirstResult(rowStart);
		q.setMaxResults(numberOfResultsToShow);
		q.setReadOnly(true);
		q.setFetchSize(numberOfResultsToShow);
	    return q.list();      
	}

	
	/**
	 * Get the user groups for the specified user.
	 * 
	 * @see edu.ur.ir.user.IrUserGroupDAO#getUserGroupsForUser(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<IrUserGroup> getUserGroupsForUser(Long userId) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
	    Query  q = session.getNamedQuery("getGroupsForUser");
	    q.setParameter("userId", userId);
	    return (List<IrUserGroup>)q.list();
	}

	
	/**
	 * Get User for group
	 * 
	 * @see edu.ur.ir.user.IrUserGroupDAO#getUserForGroup(java.lang.Long, java.lang.Long)
	 */
	public IrUser getUserForGroup(Long groupId, Long userId) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
	    Query  q = session.getNamedQuery("getUserForGroup");
	    q.setParameter("userId", userId);
	    q.setParameter("userGroupId", groupId);
	    return (IrUser)q.uniqueResult();
	}
	

}
