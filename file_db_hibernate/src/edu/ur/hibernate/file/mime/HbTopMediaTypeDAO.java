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

import edu.ur.file.mime.TopMediaType;
import edu.ur.file.mime.TopMediaTypeDAO;
import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;

/**
 * Implemenation for saving Top Media types to database.
 * 
 * @author Nathan Sarr
 *
 */
public class HbTopMediaTypeDAO implements TopMediaTypeDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 7548325646698651995L;
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<TopMediaType> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbTopMediaTypeDAO() {
		hbCrudDAO = new HbCrudDAO<TopMediaType>(TopMediaType.class);
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
     * Get the number of Top media types in the system
     * 
     * @return the number of top media types
     */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("topMediaTypeCount"));
	}

    /**
     * Return all top media types by Name.  This list 
     * can be extreamly large and it is recomened that
     * paging is used instead
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<TopMediaType> getAllNameOrder() {
    	DetachedCriteria dc = DetachedCriteria.forClass(TopMediaType.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<TopMediaType>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);

	}

    /**
     * Get all top media types starting at the start record and get up to 
     * the numRecords - it will be ordered by name
     *  
     * @param startRecord - the index to start at
     * @param numRecords - the number of records to get
     * @return the records found
     */
	public List<TopMediaType> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllTopMediaTypesByName", startRecord, numRecords);
	}

	/** 
     * Find a file by it's file name
     * 
     * @param name - name of the top media type.
     * @return the top media type found
	 */

	public TopMediaType findByUniqueName(String name) {
		return (TopMediaType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getTopMeidaTypeByName", name));
	}

	/**
	 * Get the top media type by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public TopMediaType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the top media type persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(TopMediaType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the top media type transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(TopMediaType entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * @see edu.ur.file.mime.TopMediaTypeDAO#getTopMediaTypes(int, int, String)
	 */
	@SuppressWarnings("unchecked")
	public List<TopMediaType> getTopMediaTypes(
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		List<TopMediaType> topMediaTypes = (List<TopMediaType>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getTopMediaTypesOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getTopMediaTypesOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
        });

        return topMediaTypes;
	}

}
