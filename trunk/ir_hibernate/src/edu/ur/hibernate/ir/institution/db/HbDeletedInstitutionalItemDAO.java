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
import edu.ur.ir.institution.DeletedInstitutionalItem;
import edu.ur.ir.institution.DeletedInstitutionalItemDAO;

/**
 * Implementation for data access for deleted institutional item
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbDeletedInstitutionalItemDAO implements DeletedInstitutionalItemDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = 1822458624569481338L;
	
	/**  Helper for persisting information using hibernate.   */
	private final HbCrudDAO<DeletedInstitutionalItem> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbDeletedInstitutionalItemDAO() {
		hbCrudDAO = new HbCrudDAO<DeletedInstitutionalItem>(DeletedInstitutionalItem.class);
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


	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("deletedInstitutionalItemCount"));
	}

	/**
	 * Get Delete info for institutional item 
	 * 
	 * @param institutionalItemId Id of institutional item
	 * @return Information about deleted institutional item
	 */
	public DeletedInstitutionalItem getDeleteInfoForInstitutionalItem(Long institutionalItemId) {
		return (DeletedInstitutionalItem)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("deleteInstitutionalItemInfo", institutionalItemId));
	}

	
	public DeletedInstitutionalItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(DeletedInstitutionalItem entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(DeletedInstitutionalItem entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}
	
	/**
	 * Deletes the entire history
	 */
	public void deleteAll() {
		
		hbCrudDAO.getHibernateTemplate().deleteAll(getAll());
		
	}

	/**
	 * Get number of deleted institutional items for user
	 * 
	 */
	public Long getDeletedInstitutionalItemCountForUser(Long userId) {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getDeletedInstitutionalItemCountForUser", userId));
	}
}
