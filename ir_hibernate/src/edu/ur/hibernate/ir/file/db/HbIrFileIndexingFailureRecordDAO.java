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

package edu.ur.hibernate.ir.file.db;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.file.IrFileIndexingFailureRecord;
import edu.ur.ir.file.IrFileIndexingFailureRecordDAO;

public class HbIrFileIndexingFailureRecordDAO implements IrFileIndexingFailureRecordDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -5597220663120843105L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<IrFileIndexingFailureRecord> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbIrFileIndexingFailureRecordDAO() {
		hbCrudDAO = new HbCrudDAO<IrFileIndexingFailureRecord>(IrFileIndexingFailureRecord.class);
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
	 * Get a count of all records.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irFileIndexingFailureRecordCount"));
	}

	/**
	 * Get record by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IrFileIndexingFailureRecord getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Add the record to persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IrFileIndexingFailureRecord entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Delete the record from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IrFileIndexingFailureRecord entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
