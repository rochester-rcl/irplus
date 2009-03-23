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

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionDAO;

/**
 * Persistence of Institutional item version data.
 * 
 * @author Nathan Sarr
 *
 */
public class HbInstitutionalItemVersionDAO implements InstitutionalItemVersionDAO{
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<InstitutionalItemVersion> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbInstitutionalItemVersionDAO() {
		hbCrudDAO = new HbCrudDAO<InstitutionalItemVersion>(InstitutionalItemVersion.class);
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
	 * Get all Institutional item version data.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get a Institutional item version by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public InstitutionalItemVersion getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the Institutional item version persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(InstitutionalItemVersion entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the Institutional item version transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(InstitutionalItemVersion entity) {
		hbCrudDAO.makeTransient(entity);
	}



	/**
	 * get the publications for given name id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getPublicationVersionsByPersonName(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersion> getPublicationVersionsByPersonName(List<Long> personNameIds) {
		
	    Query q = hbCrudDAO.getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("getPublicationVersionsByPersonNameId");
		
	    q.setParameterList("personNameIds", personNameIds);
	
	    List<InstitutionalItemVersion> versions = (List<InstitutionalItemVersion>)q.list();
	    
	    return versions;

	}

	/**
	 * Get the institutional item version by handle id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemVersionByHandleId(java.lang.Long)
	 */
	public InstitutionalItemVersion getItemVersionByHandleId(Long handleId) {
		return (InstitutionalItemVersion)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getInstitutionalItemByHandleId", handleId));
	}

}
