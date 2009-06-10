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
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeDAO;

/**
 * Data access for content type information.
 * 
 * @author Nathan Sarr
 *
 */
public class HbContentTypeDAO implements ContentTypeDAO {

	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<ContentType> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbContentTypeDAO() {
		hbCrudDAO = new HbCrudDAO<ContentType>(ContentType.class);
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
	 * Get a count of the item content types in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("contentTypeCount"));
	}

	/**
	 * Get all content types by name ascending order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<ContentType> getAllNameOrder() {
    	return (List<ContentType>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getContentTypesOrderByNameAsc");
	}

	/**
	 * Get all contributor types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<ContentType> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllContentTypeNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the contributor type by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public ContentType findByUniqueName(String name) {
		return (ContentType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getContentTypeByName", name));
	}

	/**
	 * Get all content types.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<ContentType> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get a content type by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ContentType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the content type persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ContentType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the content type persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ContentType entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the content type order by name.
	 * 
	 * @see edu.ur.ir.item.ContentTypeDAO#getContentTypesOrderByName(int, int, String)
	 */
	@SuppressWarnings("unchecked")
	public List<ContentType> getContentTypesOrderByName(
			final int rowStart, final int numberOfResultsToShow, final String sortType) {
		List<ContentType> contentTypes  = 
			(List<ContentType>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

	            Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getContentTypesOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getContentTypesOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
			
		});

        return contentTypes;
	}

	/**
	 * Get the content type by the unique system code.
	 * 
	 * @see edu.ur.ir.item.ContentTypeDAO#getByUniqueSystemCode(java.lang.String)
	 */
	public ContentType getByUniqueSystemCode(String systemCode) {
		return (ContentType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getContentTypeByUniqueSystemCode", systemCode));

	}
}
