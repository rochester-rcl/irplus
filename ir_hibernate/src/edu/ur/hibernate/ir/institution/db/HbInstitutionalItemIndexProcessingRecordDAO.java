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

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;


import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO;

/**
 * Interface for dealing with institutional item index processing 
 * 
 * @author Nathan Sarr
 *
 */
public class HbInstitutionalItemIndexProcessingRecordDAO implements InstitutionalItemIndexProcessingRecordDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -1245600447718553275L;
	
	/**  Helper for persisting information using hibernate.  */
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
		return (List<InstitutionalItemIndexProcessingRecord>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getInstitutionalItemIndexProcessingRecordOrderByIdDate");
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

	/**
	 * Get the institutional item processing record by item id and processing type.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#get(java.lang.Long, edu.ur.ir.index.IndexProcessingType)
	 */
	public InstitutionalItemIndexProcessingRecord get(Long itemId,
			IndexProcessingType processingType) {
		
		Object[] values = {itemId, processingType.getId()};
		return (InstitutionalItemIndexProcessingRecord) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("instItemProcessingRecByItemIdProcessingType", values));
	}

	
	/**
	 * Insert all items for collection.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForCollection(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForCollection(
			final InstitutionalCollection institutionalCollection,
			final IndexProcessingType processingType) {
		
		
		return (Long) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		      
		        Query q = session.getNamedQuery("insertAllItemsForCollection");
		       
		        q.setParameter("leftValue", institutionalCollection.getLeftValue());
		        q.setParameter("rightValue", institutionalCollection.getRightValue());
			    q.setParameter("treeRootId", institutionalCollection.getTreeRoot().getId());
			    q.setParameter("processingTypeId", processingType.getId());
			    return Long.valueOf(q.executeUpdate());
            }
		});
	}
	
	/**
	 * Insert all items for a repository
	 * 
	 */
	public Long insertAllItemsForRepository(final IndexProcessingType processingType) {
		return (Long) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		      
		        Query q = session.getNamedQuery("insertAllItemsForRepository");
			    q.setParameter("processingTypeId", processingType.getId());
			    return Long.valueOf(q.executeUpdate());
            }
		});
	}

	/**
	 * Set all items for re-indexing for a given content type
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForContentType(long)
	 */
	public Long insertAllItemsForContentType(Long contentTypeId, IndexProcessingType processingType) {
		Long numCreated = 0l;
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForPrimaryContentType");
		q.setParameter("contentTypeId", contentTypeId);
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		
		q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForSecondaryContentType");
		q.setParameter("contentTypeId", contentTypeId);
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		
		return numCreated;
	}

	/**
	 * Insert all institutional items with associated with a given contributor type.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForContributorType(java.lang.Long, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForContributorType(Long contributorTypeId,
			IndexProcessingType processingType) {
        Long numCreated = 0l;
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForContributorType");
		q.setParameter("contributorTypeId", contributorTypeId);
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		return numCreated;
	}

}
