/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.hibernate.ir.item.metadata.dc.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMapping;
import edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMappingDAO;

/**
 * Implementation for hibernate persistence of identifier type dublin core mappings.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIdentifierTypeDublinCoreMappingDAO implements IdentifierTypeDublinCoreMappingDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -8994829502707235031L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<IdentifierTypeDublinCoreMapping> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbIdentifierTypeDublinCoreMappingDAO() {
		hbCrudDAO = new HbCrudDAO<IdentifierTypeDublinCoreMapping>(IdentifierTypeDublinCoreMapping.class);
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
	 * Get a specified dublin core mapping by identifier type id.
	 * 
	 * @see edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMappingDAO#get(java.lang.Long)
	 */
	public IdentifierTypeDublinCoreMapping get(Long identifierTypeId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getIdentifierTypeDublinCoreMappingByIdentifierTypeId");
		q.setParameter("identifierTypeId", identifierTypeId);
        return (IdentifierTypeDublinCoreMapping)q.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMappingDAO#get(java.lang.Long, java.lang.Long)
	 */
	public IdentifierTypeDublinCoreMapping get(Long identifierTypeId,
			Long dublinCoreTermId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getIdentifierTypeDublinCoreMappingByIds");
		q.setParameter("identifierTypeId", identifierTypeId);
	    q.setParameter("dublinCoreTermId", dublinCoreTermId);
        return (IdentifierTypeDublinCoreMapping)q.uniqueResult();
	}

	/**
	 * Get a count of the identifier types.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("identifierTypeDublinCoreMappingCount").uniqueResult();
	}

	/**
	 * Get all identifier type dublin core mappings.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<IdentifierTypeDublinCoreMapping> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get the identifier type dublin core mapping.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IdentifierTypeDublinCoreMapping getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Save the identifier type dublin core mapping
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IdentifierTypeDublinCoreMapping entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/* (non-Javadoc)
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IdentifierTypeDublinCoreMapping entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
