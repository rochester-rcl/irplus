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
import edu.ur.ir.user.FolderAutoShareInfo;
import edu.ur.ir.user.FolderAutoShareInfoDAO;

/**
 * Hibernate implementation of the FolderAutoShareInfoDAO
 * 
 * @author Nathan Sarr
 *
 */
public class HbFolderAutoShareInfoDAO implements FolderAutoShareInfoDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -737921571573125223L;
	
	/**  Helper for persisting information using hibernate.  */	
	private final HbCrudDAO<FolderAutoShareInfo> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbFolderAutoShareInfoDAO() {
		hbCrudDAO = new HbCrudDAO<FolderAutoShareInfo>(FolderAutoShareInfo.class);
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
	 * Get the folder auto share by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public FolderAutoShareInfo getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the folder auto share persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(FolderAutoShareInfo entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Remove the folder from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(FolderAutoShareInfo entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get all user external accounts.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<FolderAutoShareInfo> getAll() {
		return hbCrudDAO.getAll();
	}

}
