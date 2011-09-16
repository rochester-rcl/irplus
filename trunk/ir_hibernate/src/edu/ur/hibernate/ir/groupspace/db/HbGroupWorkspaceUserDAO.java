/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.hibernate.ir.groupspace.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.groupspace.GroupWorkspaceUserDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;

/**
 * Hibernate implementation of group space group persistence
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceUserDAO implements GroupWorkspaceUserDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 7129354264082297025L;
	
	/** hibernate helper */
	private final HbCrudDAO<GroupWorkspaceUser> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceUserDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceUser>(GroupWorkspaceUser.class);
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
	 * Get the list of user groups for a group workspace.
	 * 
	 * 
	 * @param groupWorkspaceId - id of the group workspace
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param orderType - sort order(Asc/desc)
	 * 
	 * @return list of user groups.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceUser> getGroupsByName(Long groupWorkspaceId, int rowStart,
			int numberOfResultsToShow, OrderType orderType) 
	{
		Query q = null;
	    Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
	    if(orderType.equals(OrderType.ASCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getGroupWorkspaceGroupsOrderByNameAsc");
	    } 
	    else
	    {
	        q = session.getNamedQuery("getGroupWorkspaceGroupsOrderByNameDesc");
	    } 
	    q.setParameter("groupWorkspaceId", groupWorkspaceId);
	    q.setFirstResult(rowStart);
	    q.setMaxResults(numberOfResultsToShow);
		q.setFetchSize(numberOfResultsToShow);
	    return q.list();
	}

	/**
	 * Get all the user groups for a given group space and a given user.
	 *
	 * @param groupSpaceId - groupSpace
	 * @param userId - id of the user to get the groups for
	 * @return - list of groups the user is in.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceUser> getGroups(Long groupWorkspaceId, Long userId) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = session.getNamedQuery("getGroupWorkspaceGroupsForUser");
		q.setParameter("groupWorkspaceId", groupWorkspaceId);
		q.setParameter("userId", userId);
		return (List<GroupWorkspaceUser>)q.list();
	}

	/**
	 * Get a user for the specified group 
	 * 
	 * @param groupId - id of the group
	 * @param userId - id of the user
	 * 
	 * @return the user if found - otherwise null.
	 */
	public IrUser getUserForGroup(Long groupId, Long userId) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = session.getNamedQuery("getUserForGroupWorkspaceGroup");
		q.setParameter("userId", userId);
		q.setParameter("groupId", groupId);
		return (IrUser)q.uniqueResult();
	}

	/**
	 * Get the group workspace by id.
	 *  
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public GroupWorkspaceUser getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Add the group workspace group to persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceUser entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Remove the group workspace from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceUser entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
