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

import edu.ur.file.mime.SubTypeExtension;
import edu.ur.file.mime.SubTypeExtensionDAO;
import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;

/**
 * Hibernate implementation for the Sub type 
 * Extension DAO data access.
 * 
 * @author Nathan Sarr
 *
 */
public class HbSubTypeExtensionDAO implements SubTypeExtensionDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -9178204905470253275L;
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<SubTypeExtension> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbSubTypeExtensionDAO() {
		hbCrudDAO = new HbCrudDAO<SubTypeExtension>(SubTypeExtension.class);
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
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("subTypeExtensionCount"));
	}

    /**
     * Return all sub types orderd by Name.  This list 
     * can be extreamly large and it is recomened that
     * paging is used instead
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<SubTypeExtension> getAllNameOrder() {
    	DetachedCriteria dc = DetachedCriteria.forClass(SubTypeExtension.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<SubTypeExtension>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);

	}

    /**
     * Get all Sub types starting at the start record and get up to 
     * the numRecords - it will be ordered by name
     *  
     * @param startRecord - the index to start at
     * @param numRecords - the number of records to get
     * @return the records found
     */
	public List<SubTypeExtension> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllSubTypeExtensionsByName", startRecord, numRecords);
	}

	/** 
     * Find a sub type by it's name
     * 
     * @param name - name of the mime sub type.
     * @return the sub type found
	 */

	public SubTypeExtension findByUniqueName(String name) {
		return (SubTypeExtension) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getSubTypeExtensionByName", name));
	}

	/**
	 * Get the sub type extension by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public SubTypeExtension getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the subTypeExtension persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(SubTypeExtension entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the subtype extension transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(SubTypeExtension entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	/**
	 * @see edu.ur.file.mime.SubTypeExtensionDAO#getSubTypeExtensions(Long, int, int, String)
	 */
	@SuppressWarnings("unchecked")
	public List<SubTypeExtension> getSubTypeExtensions(final Long subTypeId,
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		List<SubTypeExtension> subTypeExtensions = 
			(List<SubTypeExtension>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

            	Query q = null;
 		        if (sortType.equalsIgnoreCase("asc")) {
 		        	q = session.getNamedQuery("getSubTypeExtensionsOrderByNameAsc");
 		        } else {
 		        	q = session.getNamedQuery("getSubTypeExtensionsOrderByNameDesc");
 		        }
 			    q.setParameter("subTypeId", subTypeId);
 			    q.setFirstResult(rowStart);
 			    q.setMaxResults(numberOfResultsToShow);
 			    q.setReadOnly(true);
 			    q.setFetchSize(numberOfResultsToShow);
 	            return q.list();
            }
        });

        return subTypeExtensions;
	}

	/**
	 * @see edu.ur.file.mime.SubTypeExtensionDAO#getSubTypeExtensionsCount(java.lang.Long)
	 */
	public Long getSubTypeExtensionsCount(final Long subTypeId)
			{
		Long count = (Long) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
               	Query q = null;
 	        	q = session.getNamedQuery("getSubTypeExtensionsCountForSubTypeId");
 			    q.setParameter("subTypeId", subTypeId);
 	            return q.uniqueResult();

            }
        });
    	
    	return count;
	}
}
