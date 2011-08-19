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

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.user.ExternalAccountType;
import edu.ur.ir.user.ExternalUserAccount;
import edu.ur.ir.user.ExternalUserAccountDAO;

/**
 * Hibernate implementation of a User External Account.
 * 
 * @author Nathan Sarr
 *
 */
public class HbExternalUserAccountDAO implements ExternalUserAccountDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -539869641706311546L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<ExternalUserAccount> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbExternalUserAccountDAO() {
		hbCrudDAO = new HbCrudDAO<ExternalUserAccount>(ExternalUserAccount.class);
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
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ExternalUserAccount getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	/**
	 * Make the external user account persistent
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ExternalUserAccount entity) {
		hbCrudDAO.makePersistent(entity);		
	}

	
	/**
	 * Delete the external user account.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ExternalUserAccount entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	/**
	 * Get the external user accounts for a given user name.
	 * 
	 * @see edu.ur.ir.user.ExternalUserAccountDAO#getByExternalUserName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<ExternalUserAccount> getByExternalUserName(
			String externalUserName) {
		return (List<ExternalUserAccount>) hbCrudDAO.getHibernateTemplate()
			.findByNamedQuery("getExternalUserAccountByUsername", externalUserName);
	}

	
	/**
	 * Returns the external user account or null if a user account is not found.
	 * 
	 * @see edu.ur.ir.user.ExternalUserAccountDAO#getByExternalUserNameAccountType(java.lang.String, edu.ur.ir.user.ExternalAccountType)
	 */
	public ExternalUserAccount getByExternalUserNameAccountType(
			String externalUserName, ExternalAccountType externalAccountType) {
		Object[] values = new Object[] {externalUserName, externalAccountType.getId()};
		return (ExternalUserAccount) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getExternalUserAccountByUsernameType", 
				values));
	}

}
