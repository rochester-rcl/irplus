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
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceUserInvite;
import edu.ur.ir.groupspace.GroupWorkspaceUserInviteDAO;
import edu.ur.order.OrderType;

/**
 * Data persistence for an invite to join a group workspace
 * to a user who already exists in the system.
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceUserInviteDAO implements GroupWorkspaceUserInviteDAO {

	/* eclipse generated id */
	private static final long serialVersionUID = -2939643218227510701L;
	
	/**  Helper for persisting information using hibernate.  */	
	private final HbCrudDAO<GroupWorkspaceUserInvite> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceUserInviteDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceUserInvite>(GroupWorkspaceUserInvite.class);
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
	 * Get the list of invite infos ordered by inviteor
	 * 
	 * @param rowStart - start position in the list
	 * @param maxResults - maximum number of results to retrieve
	 * @param orderType - ascending/descending order
	 * 
	 * @return list of invite infos found
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceUserInvite> getInviteInfosOrderByGroup(int rowStart,
			int maxResults, OrderType orderType)
	{
		Query q = null;
		if (orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getGroupWorkspaceUserInviteOrderByInvitorDesc");
		} else {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getGroupWorkspaceUserInviteOrderByInvitorAsc");
		}

		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return (List<GroupWorkspaceUserInvite>) q.list();
	}

	
	/**
	 * Get the invited group workspace group user by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public GroupWorkspaceUserInvite getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	/**
	 * Make the invite record persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceUserInvite entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the invite record transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceUserInvite entity) {
		hbCrudDAO.makeTransient(entity);		
	}

	/**
	 * Get a count of the number of invite records.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupWorkspaceUserInviteCount").uniqueResult();
	}

}
