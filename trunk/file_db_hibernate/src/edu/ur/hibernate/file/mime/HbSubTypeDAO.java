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

package edu.ur.hibernate.file.mime;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.file.mime.SubType;
import edu.ur.file.mime.SubTypeDAO;
import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;

/**
 * Hibernate implementation for the Sub type 
 * DAO data access.
 * 
 * @author Nathan Sarr
 *
 */
public class HbSubTypeDAO implements SubTypeDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 6346766289834658725L;
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<SubType> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbSubTypeDAO() {
		hbCrudDAO = new HbCrudDAO<SubType>(SubType.class);
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
     * Get the number of Mime Sub types in the system
     * 
     * @return the number of sub types in the system
     */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("subTypeCount"));
	}

    /**
     * Return all sub types orderd by Name.  This list 
     * can be extreamly large and it is recomened that
     * paging is used instead
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<SubType> getAllNameOrder() {
    	DetachedCriteria dc = DetachedCriteria.forClass(SubType.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<SubType>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);

	}

    /**
     * Get all Sub types starting at the start record and get up to 
     * the numRecords - it will be ordered by name
     *  
     * @param startRecord - the index to start at
     * @param numRecords - the number of records to get
     * @return the records found
     */
	public List<SubType> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllSubTypesByName", startRecord, numRecords);
	}

	/** 
     * Find a sub type by it's name and top media type id.
     * 
     * @param name - name of the mime sub type.
     * @return the sub type found
	 */

	public SubType findByUniqueName(String name, Long topMediaTypeId) {
		Object[] values = {name, topMediaTypeId};
		return (SubType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getSubTypeByName", values));
	}

	/**
	 * Get a sub type by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public SubType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the sub type persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(SubType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the sub type transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(SubType entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List<SubType> getSubTypes(final Long topMediaTypeId,
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		List<SubType> subTypes = 
			(List<SubType>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getSubTypesOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getSubTypesOrderByNameDesc");
		        }
			    q.setParameter("topMediaTypeId", topMediaTypeId);
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();

            }
        });

        return subTypes;
	}

	
	public Long getSubTypesCount(final Long topMediaTypeId) {
		Long count = (Long) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = session.getNamedQuery("getSubTypesCountForTopMediaType");
		    
			    q.setParameter("topMediaTypeId", topMediaTypeId);

	            return q.uniqueResult();
            }
        });
    	
    	return count;
	}
}
