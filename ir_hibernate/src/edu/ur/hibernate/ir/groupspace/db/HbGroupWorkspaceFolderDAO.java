/**  
   Copyright 2008-2011 University of Rochester

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

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceFolderDAO;

/**
 * Persistent storage for group folder data.
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceFolderDAO implements GroupWorkspaceFolderDAO{

	/** eclipse generated id*/
	private static final long serialVersionUID = 7151402796946586912L;
	
	/** hibernate helper  */
	private final HbCrudDAO<GroupWorkspaceFolder> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceFolderDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceFolder>(GroupWorkspaceFolder.class);
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
	 * Get the group folder by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public GroupWorkspaceFolder getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the group folder persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceFolder entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the group folder transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceFolder entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
