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

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMapping;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingDAO;

/**
 * Hibernate implementation of the contributor type dublin core mapping.
 * 
 * @author Nathan Sarr
 *
 */
public class HbContributorTypeDublinCoreMappingDAO implements ContributorTypeDublinCoreMappingDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 9198877928342664795L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<ContributorTypeDublinCoreMapping> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbContributorTypeDublinCoreMappingDAO() {
		hbCrudDAO = new HbCrudDAO<ContributorTypeDublinCoreMapping>(ContributorTypeDublinCoreMapping.class);
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
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("contributorTypeDublinCoreMapping"));
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<ContributorTypeDublinCoreMapping> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ContributorTypeDublinCoreMapping getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ContributorTypeDublinCoreMapping entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ContributorTypeDublinCoreMapping entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * 
	 * @see edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingDAO#get(java.lang.Long, java.lang.Long)
	 */
	public ContributorTypeDublinCoreMapping get(final Long contributorTypeId,
			final Long dublinCoreTermId) {
		ContributorTypeDublinCoreMapping mapping = (ContributorTypeDublinCoreMapping) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
            	
		        Query q = session.getNamedQuery("getContributorTypeDublinCoreMappingByIds");
		        q.setParameter("contributorTypeId", contributorTypeId);
		        q.setParameter("dublinCoreTermId", dublinCoreTermId);
	            return q.uniqueResult();
            }
        });

        return mapping;
	}
	
	/**
	 * 
	 * @see edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingDAO#get(java.lang.Long)
	 */
	public ContributorTypeDublinCoreMapping get(final Long contributorTypeId) {
		ContributorTypeDublinCoreMapping mapping = (ContributorTypeDublinCoreMapping) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
            	
		        Query q = session.getNamedQuery("getContributorTypeDublinCoreMappingByContributorTypeId");
		        q.setParameter("contributorTypeId", contributorTypeId);
	            return q.uniqueResult();
            }
        });

        return mapping;
	}


}
