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

package edu.ur.hibernate.ir.researcher.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.researcher.Field;
import edu.ur.ir.researcher.FieldDAO;

/**
 * Data access for an field.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbFieldDAO implements FieldDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 1814388152590889431L;
	
	private final HbCrudDAO<Field> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbFieldDAO() {
		hbCrudDAO = new HbCrudDAO<Field>(Field.class);
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
	 * Get a count of the fields in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("fieldCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get all fields in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<Field> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(Field.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<Field>) dc.getExecutableCriteria(hbCrudDAO.getSessionFactory().getCurrentSession()).list();
 	}

	/**
	 * Get all fields in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<Field> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllFieldNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the role by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public Field findByUniqueName(String name) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getFieldByName");
		q.setString("name", name);
		return (Field)  q.uniqueResult();
	}

	public Field getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(Field entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(Field entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	@SuppressWarnings("unchecked")
	public List<Field> getFields(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
	
        Query q = null;
		if (sortType.equalsIgnoreCase("asc")) {
		    q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getFieldsOrderByNameAsc");
		} else {
		    q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getFieldsOrderByNameDesc");
		}
			    
		q.setFirstResult(rowStart);
		q.setMaxResults(numberOfResultsToShow);
		q.setReadOnly(true);
		q.setFetchSize(numberOfResultsToShow);
	    return q.list();
	}
}
