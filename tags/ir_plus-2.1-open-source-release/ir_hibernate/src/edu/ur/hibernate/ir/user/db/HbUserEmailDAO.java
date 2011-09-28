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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserEmailDAO;

/**
 * Hibernate persistance of the User email
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbUserEmailDAO implements UserEmailDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = -6965948106521929911L;
	
	/**  Helper for persisting information using hibernate.  */	
	private final HbCrudDAO<UserEmail> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbUserEmailDAO() {
		hbCrudDAO = new HbCrudDAO<UserEmail>(UserEmail.class);
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
	 * Get a count of the emails in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("userEmailCount"));
	}


	/**
	 * Get the emails.
	 * 
	 * @see edu.ur.ir.user.UserEmailDAO#getEmails(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<UserEmail> getEmails(final List<Long> emailIds) {
		List<UserEmail> foundEmails = new LinkedList<UserEmail>();
		if( emailIds.size() > 0 )
        {
			foundEmails = (List<UserEmail>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
		    public Object doInHibernate(Session session)
                throws HibernateException, SQLException {
                    Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                    criteria.add(Restrictions.in("id",emailIds));
                return criteria.list();
                }
             });
        }
		return foundEmails;
	}


	
	/**
	 * Get the user for the specified email
	 * 
	 * @see edu.ur.ir.user.IrUserDAO#getUserByEmail(String email)
	 */
	public UserEmail getUserByEmail(String email) {
		return (UserEmail)	HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getUserByEmail", email));
	}	

	/**
	 * Get the user email for the specified token
	 * 
	 * @see edu.ur.ir.user.IrUserDAO#getUserEmailByToken(String)
	 */
	public UserEmail getUserEmailByToken(String token) {
		return (UserEmail)	HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getUserEmailByToken", token));
	}	

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	public UserEmail getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(UserEmail entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(UserEmail entity) {
		hbCrudDAO.makeTransient(entity);
	}


}