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

package edu.ur.hibernate.ir.item.db;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.item.CopyrightStatement;
import edu.ur.ir.item.CopyrightStatementDAO;
import edu.ur.order.OrderType;

/**
 * Hibernate implementation of copyright storage.
 * 
 * @author Nathan Sarr
 *
 */
public class HbCopyrightStatementDAO implements CopyrightStatementDAO {

	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<CopyrightStatement> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbCopyrightStatementDAO() {
		hbCrudDAO = new HbCrudDAO<CopyrightStatement>(CopyrightStatement.class);
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
	 * Get the count of copyright statements in the system.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("copyrightStatementCount"));
	}


	/**
	 * Get all copyright statements 
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<CopyrightStatement> getAll() {
		return hbCrudDAO.getAll();
	}

	
	/**
	 * Get the copyright statement by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public CopyrightStatement getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	/**
	 * Save the copyright statement.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(CopyrightStatement entity) {
		hbCrudDAO.makePersistent(entity);
	}

	
	/**
	 * Remove the copyright from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(CopyrightStatement entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	/**
	 * Get all the copyright statements in name order.
	 * 
	 * @see edu.ur.dao.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<CopyrightStatement> getAllNameOrder() {
		return (List<CopyrightStatement>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getCopyrightStatementByNameAsc");
	}

	
	
	/**
	 * Get the copyright statement by unique name.
	 * 
	 * @see edu.ur.dao.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public CopyrightStatement findByUniqueName(String name) {
		return (CopyrightStatement) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getCopyrightStatmentByName", name));
	}
	
	/**
	 * Get the copyright statements ordered by name.
	 * 
	 * @see edu.ur.ir.item.ContentTypeDAO#getContentTypesOrderByName(int, int, String)
	 */
	@SuppressWarnings("unchecked")
	public List<CopyrightStatement> getCopyrightStatementsOrderByName(
			final int rowStart, final int numberOfResultsToShow,  final OrderType orderType) {
		List<CopyrightStatement> copyrightStatements  = 
			(List<CopyrightStatement>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

	            Query q = null;
		        if (orderType.equals(OrderType.ASCENDING_ORDER)) {
		        	q = session.getNamedQuery("getCopyrightStatementByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getCopyrightStatementByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
			
		});

        return copyrightStatements;
	}

}
