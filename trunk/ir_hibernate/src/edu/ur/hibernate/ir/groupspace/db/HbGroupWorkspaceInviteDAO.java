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
import edu.ur.ir.groupspace.GroupWorkspaceInvite;
import edu.ur.ir.groupspace.GroupWorkspaceInviteDAO;
import edu.ur.order.OrderType;

/**
 * Default hibernate implementation to store invites to join a group workspace
 * group.
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceInviteDAO implements GroupWorkspaceInviteDAO{
	
	/* eclipse generated id. */
	private static final long serialVersionUID = -865713437299976405L;
	
	/**  Helper for persisting information using hibernate.  */	
	private final HbCrudDAO<GroupWorkspaceInvite> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceInviteDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceInvite>(GroupWorkspaceInvite.class);
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
	 * Find the Invite information for a specified token
	 * 
	 * @param token user token
	 * @return User token information
	 */
	public GroupWorkspaceInvite findInviteInfoForToken(String token)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("findGroupWorkspaceInviteForToken");
		q.setParameter("token", token);
		return (GroupWorkspaceInvite) q.uniqueResult();
	}
	
	/**
	 * Find the Invite information for a specified email
	 * 
	 * @param email email address shared with
	 * @return List of invite information
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceInvite> getInviteInfoByEmail(String email)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("findGroupWorkspaceInviteForEmail");
		q.setParameter("email", email.trim().toLowerCase());
		return q.list();
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
	public List<GroupWorkspaceInvite> getInviteInfosOrderByGroup(int rowStart,
			int maxResults, OrderType orderType)
	{
		Query q = null;
		if (orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getGroupWorkspaceInviteOrderByInvitorDesc");
		} else {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getGroupWorkspaceInviteOrderByInvitorAsc");
		}

		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return (List<GroupWorkspaceInvite>) q.list();
	}

	
	/**
	 * Get the invited group workspace group user by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public GroupWorkspaceInvite getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	/**
	 * Make the invite record persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceInvite entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	/**
	 * Make the invite record transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceInvite entity) {
		hbCrudDAO.makeTransient(entity);
		
	}

	/**
	 * Get a count of the number of invite records.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupWorkspaceInviteCount").uniqueResult();
	}

}
