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

package edu.ur.hibernate.ir.security.db;


import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.security.IrUserAccessControlEntryDAO;

/**
 * Persistance for a user access control entry.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIrUserAccessControlEntryDAO implements 
IrUserAccessControlEntryDAO 
{
	/** eclipse generated id */
	private static final long serialVersionUID = 7996579141250699790L;
	
	/** helper for persistence operations */
	private final HbCrudDAO<IrUserAccessControlEntry> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbIrUserAccessControlEntryDAO() {
		hbCrudDAO = new HbCrudDAO<IrUserAccessControlEntry>(IrUserAccessControlEntry.class);
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
	 * Get a count of the user access cotnrol entries.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irUserAccessEntryCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get the user access control entry by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IrUserAccessControlEntry getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the user access control entry persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IrUserAccessControlEntry entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the user access control entry transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IrUserAccessControlEntry entity) {
		hbCrudDAO.makeTransient(entity);
	}
}
