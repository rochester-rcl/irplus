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


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.groupspace.GroupWorkspaceUserDAO;

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

	/**
     * Get the group workspace user for the given user id and group workspace id.
     * 
     * @param userId - user id
     * @param groupWorkspaceId - group workspace id
     * 
     * @return the group workspace user or null if the group workspace user is not found.
     */
	public GroupWorkspaceUser getGroupWorkspaceUser(Long userId,
			Long groupWorkspaceId) {
		
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = session.getNamedQuery("getUserForGroupWorkspaceGroup");
		q.setParameter("userId", userId);
		q.setParameter("workspaceId", groupWorkspaceId);
		return (GroupWorkspaceUser)q.uniqueResult();
	}

}
