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
import edu.ur.ir.user.UserRepositoryLicense;
import edu.ur.ir.user.UserRepositoryLicenseDAO;

/**
 * Hibernate implementation of User Repository License DAO.
 * 
 * @author Nathan Sarr
 *
 */
public class HbUserRepositoryLicenseDAO implements UserRepositoryLicenseDAO{

	/**
	 * Helper for persisting information using hibernate. 
	 */	
	private final HbCrudDAO<UserRepositoryLicense> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbUserRepositoryLicenseDAO() {
		hbCrudDAO = new HbCrudDAO<UserRepositoryLicense>(UserRepositoryLicense.class);
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
	 * Get all user repository license information.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<UserRepositoryLicense> getAll() {
		return hbCrudDAO.getAll();
	}
	
	/**
	 * Make the user repository license perisistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(UserRepositoryLicense entity) {
		hbCrudDAO.makePersistent(entity);
	}

	
	/**
	 * Make the user repository license transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(UserRepositoryLicense entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	/**
	 * Get the user repository license by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public UserRepositoryLicense getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

}
