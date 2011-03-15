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
import edu.ur.ir.security.IrRoleAccessControlEntry;
import edu.ur.ir.security.IrRoleAccessControlEntryDAO;


/**
 * Persistance for a role access control entry.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIrRoleAccessControlEntryDAO implements 
IrRoleAccessControlEntryDAO {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -3421413538671447301L;
	
	/** helper for persistence operations */
	private final HbCrudDAO<IrRoleAccessControlEntry> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbIrRoleAccessControlEntryDAO() {
		hbCrudDAO = new HbCrudDAO<IrRoleAccessControlEntry>(IrRoleAccessControlEntry.class);;
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
	 * Get a count of the role access control entries.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irRoleAccessEntryCount");
		return (Long)q.uniqueResult();
	}
	
	/**
	 * Get the role access cotnrol entry by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IrRoleAccessControlEntry getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	/**
	 * Make the role access control entry persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IrRoleAccessControlEntry entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	
	/**
	 * Make the role access control entry transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IrRoleAccessControlEntry entity) {
		hbCrudDAO.makeTransient(entity);
	}
}
