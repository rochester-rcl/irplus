/**  
   Copyright 2008-2010 University of Rochester

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
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.user.FolderInviteInfo;
import edu.ur.ir.user.FolderInviteInfoDAO;
import edu.ur.order.OrderType;

/**
 * Represents an invitation to work on a shared folder.
 * 
 * @author Nathan Sarr
 *
 */
public class HbFolderInviteInfoDAO implements FolderInviteInfoDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = -8501513230671420734L;
	
	/**  Helper for persisting information using hibernate.  */	
	private final HbCrudDAO<FolderInviteInfo> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbFolderInviteInfoDAO() {
		hbCrudDAO = new HbCrudDAO<FolderInviteInfo>(FolderInviteInfo.class);
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
	 * 
	 * @see edu.ur.ir.user.FolderInviteInfoDAO#getInviteInfoByEmail(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<FolderInviteInfo> getInviteInfoByEmail(String email) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("findFolderInviteInfoForEmail");
		q.setParameter("email", email.trim().toLowerCase());
		return (List<FolderInviteInfo>)q.list();
	}

	/**
	 *  Get the list of folder invite infos ordered by inviteor
	 * 
	 * @param rowStart - start position in the list
	 * @param maxResults - maximum number of results to retrieve
	 * @param orderType - ascending/decending order
	 * 
	 * @see edu.ur.ir.user.FolderInviteInfoDAO#getInviteInfosOrderByInviteor(int, int, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<FolderInviteInfo> getInviteInfosOrderByInviteor(int rowStart,
			int maxResults, OrderType orderType) {
		Query q = null;
		if (orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getFolderInviteInfosOrderByInvitorDesc");
		} else {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getFolderInviteInfosOrderByInvitorAsc");
		}

		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return (List<FolderInviteInfo>) q.list();
	}

	/**
	 * Get the invite info by id
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public FolderInviteInfo getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Save the folder invite information.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(FolderInviteInfo entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Delete the folder invite information from the database.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(FolderInviteInfo entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the count of folder invite infos.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	@Override
	public Long getCount() {
		return (Long)hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("folderInviteInfoCount").uniqueResult();
	}

}
