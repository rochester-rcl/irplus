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

package edu.ur.hibernate.ir.institution.db;

import java.util.List;

import org.hibernate.SessionFactory;


import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO;

/**
 * Interface for dealing with institutional item index processing 
 * 
 * @author Nathan Sarr
 *
 */
public class HbInstitutionalItemIndexProcessingRecordDAO implements InstitutionalItemIndexProcessingRecordDAO{

	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<InstitutionalItemIndexProcessingRecord> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbInstitutionalItemIndexProcessingRecordDAO() {
		hbCrudDAO = new HbCrudDAO<InstitutionalItemIndexProcessingRecord>(InstitutionalItemIndexProcessingRecord.class);
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
	 * Get all items order by id then by updated date.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#getAllOrderByItemIdUpdatedDate()
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemIndexProcessingRecord> getAllOrderByItemIdUpdatedDate() {
		return (List<InstitutionalItemIndexProcessingRecord>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getInstitutionalItemIndexProcessingRecordByIdDate");
	}

	/**
	 * Get all processing records.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<InstitutionalItemIndexProcessingRecord> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get the record by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public InstitutionalItemIndexProcessingRecord getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Add the record to persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(InstitutionalItemIndexProcessingRecord entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Remove the record from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(InstitutionalItemIndexProcessingRecord entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the count of institutional item index processing records.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemIndexProcessingRecordCount"));
	}

}
