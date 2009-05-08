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

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.user.InviteInfo;
import edu.ur.ir.user.InviteInfoDAO;

/**
 * Persistence for invite information
 * 
 * @author Sharmila Ranganathan 
 *
 */
public class HbInviteInfoDAO implements InviteInfoDAO {

	/**
	 * Helper for persisting information using hibernate. 
	 */	
	private final HbCrudDAO<InviteInfo> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbInviteInfoDAO() {
		hbCrudDAO = new HbCrudDAO<InviteInfo>(InviteInfo.class);
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
	 * Returns all invitaion details for specified token.
	 * 
	 * @param token
	 */
	public InviteInfo findInviteInfoForToken(String token)
	{
		return (InviteInfo) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("findInviteInfoForToken", token));
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	public InviteInfo getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(InviteInfo entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(InviteInfo entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Find the Invite information for a specified email
	 * 
	 * @param email email address shared with
	 * @return List of invite information
	 */
	@SuppressWarnings("unchecked")
	public List<InviteInfo> getInviteInfoByEmail(String email) {
		return hbCrudDAO.getHibernateTemplate().findByNamedQuery("findInviteInfoForEmail", email);
	}

}
