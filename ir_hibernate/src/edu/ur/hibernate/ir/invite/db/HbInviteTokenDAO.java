/**  
   Copyright 2008- 2011 University of Rochester

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


package edu.ur.hibernate.ir.invite.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.invite.InviteToken;
import edu.ur.ir.invite.InviteTokenDAO;

/**
 * Hibernate implementation of the invite token data access object.
 * 
 * @author Nathan Sarr
 *
 */
public class HbInviteTokenDAO implements InviteTokenDAO{
	
	/* eclipse generated id. */
	private static final long serialVersionUID = -8420999507765533823L;
	
	/*  Helper for persisting information using hibernate.  */	
	private final HbCrudDAO<InviteToken> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbInviteTokenDAO() {
		hbCrudDAO = new HbCrudDAO<InviteToken>(InviteToken.class);
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
	 * Get the invite token by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public InviteToken getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the invite token persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(InviteToken entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the invite token transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(InviteToken entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	/**
	 * Get the invite by the string token.
	 * 
	 * @see edu.ur.ir.invite.InviteTokenDAO#getByToken(java.lang.String)
	 */
	public InviteToken getByToken(String token) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("findInviteTokenByToken");
		q.setParameter("token", token);
		return (InviteToken) q.uniqueResult();
	}

	
	/**
	 * Get the list of invite tokens for a given email.
	 * 
	 * @see edu.ur.ir.invite.InviteTokenDAO#getByEmail(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<InviteToken> getByEmail(String email) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("findInviteTokenByEmail");
		q.setParameter("email", email.trim().toLowerCase());
		return q.list();
	}

}
