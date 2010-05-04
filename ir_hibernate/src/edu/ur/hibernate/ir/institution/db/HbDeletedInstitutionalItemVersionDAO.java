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

package edu.ur.hibernate.ir.institution.db;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.institution.DeletedInstitutionalItemVersion;
import edu.ur.ir.institution.DeletedInstitutionalItemVersionDAO;

/**
 * Hibernate implementation of the deleted institutional item version.
 * 
 * @author Nathan Sarr
 *
 */
public class HbDeletedInstitutionalItemVersionDAO implements DeletedInstitutionalItemVersionDAO{

	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<DeletedInstitutionalItemVersion> hbCrudDAO;
	
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
	 * Default Constructor
	 */
	public HbDeletedInstitutionalItemVersionDAO() {
		hbCrudDAO = new HbCrudDAO<DeletedInstitutionalItemVersion>(DeletedInstitutionalItemVersion.class);
	}
	
	/**
	 * 
	 * @see edu.ur.ir.institution.DeletedInstitutionalItemVersionDAO#get(java.lang.Long)
	 */
	public DeletedInstitutionalItemVersion get(final Long institutionalItemVersionId) {
		DeletedInstitutionalItemVersion deletedInstitutionalItemVersion = (DeletedInstitutionalItemVersion) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
            	
		        Query q = session.getNamedQuery("getDeleteInstitutionalItemVersionById");
		        q.setParameter("instititutionalItemVersionId", institutionalItemVersionId);
	            return q.uniqueResult();
            }
        });

        return deletedInstitutionalItemVersion;

	}

	/**
	 * 
	 * @see edu.ur.ir.institution.DeletedInstitutionalItemVersionDAO#get(java.lang.Long, java.lang.Long)
	 */
	public DeletedInstitutionalItemVersion get(final Long institutionalItemId, final int versionNumber) {
		DeletedInstitutionalItemVersion deletedInstitutionalItemVersion = (DeletedInstitutionalItemVersion) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
            	
		        Query q = session.getNamedQuery("getDeleteInstitutionalItemVersionByInstVersion");
		        q.setParameter("versionNumber", versionNumber);
		        q.setParameter("institutionalItemId", institutionalItemId);
	            return q.uniqueResult();
            }
        });

        return deletedInstitutionalItemVersion;
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<DeletedInstitutionalItemVersion> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public DeletedInstitutionalItemVersion getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(DeletedInstitutionalItemVersion entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(DeletedInstitutionalItemVersion entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
