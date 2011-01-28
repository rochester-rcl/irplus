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
import edu.ur.ir.institution.VersionedInstitutionalItem;
import edu.ur.ir.institution.VersionedInstitutionalItemDAO;

/**
 * Implementation for data access for versioned institutional item
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbVersionedInstitutionalItemDAO implements VersionedInstitutionalItemDAO{


	/** eclipse generated id */
	private static final long serialVersionUID = 3432763787065433867L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<VersionedInstitutionalItem> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbVersionedInstitutionalItemDAO() {
		hbCrudDAO = new HbCrudDAO<VersionedInstitutionalItem>(VersionedInstitutionalItem.class);
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
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("versionedItemCount"));
	}

	public VersionedInstitutionalItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(VersionedInstitutionalItem entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(VersionedInstitutionalItem entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

}
