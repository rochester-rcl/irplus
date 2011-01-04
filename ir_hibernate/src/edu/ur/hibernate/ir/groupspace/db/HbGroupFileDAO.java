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
import edu.ur.ir.groupspace.GroupFile;
import edu.ur.ir.groupspace.GroupFileDAO;

public class HbGroupFileDAO implements GroupFileDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -8181551931356055781L;
	
	/** hibernate helper  */
	private final HbCrudDAO<GroupFile> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupFileDAO() {
		hbCrudDAO = new HbCrudDAO<GroupFile>(GroupFile.class);
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
	public GroupFile getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the group folder persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupFile entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the group folder transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupFile entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
