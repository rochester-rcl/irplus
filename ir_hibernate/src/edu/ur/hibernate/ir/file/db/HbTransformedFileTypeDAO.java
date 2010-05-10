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

package edu.ur.hibernate.ir.file.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.TransformedFileTypeDAO;

/**
 * Data access for transformed file type.
 * 
 * @author Nathan Sarr
 *
 */
public class HbTransformedFileTypeDAO implements TransformedFileTypeDAO {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 2173533136453286418L;
	
	/** hibernate helper  */
	private final HbCrudDAO<TransformedFileType> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbTransformedFileTypeDAO() {
		hbCrudDAO = new HbCrudDAO<TransformedFileType>(TransformedFileType.class);
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
	 * Get a count of the transformed file types in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("transformedFileTypeCount"));
	}

	/**
	 * Get all transformed file types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<TransformedFileType> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(TransformedFileType.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<TransformedFileType>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all transformed file types in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<TransformedFileType> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllTransformedFileTypeNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the transformed file type by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public TransformedFileType findByUniqueName(String name) {
		return (TransformedFileType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getTransformedFileTypeByName", name));
	}
	
	/**
	 * Find the transformed file type by unique system code.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public TransformedFileType findBySystemCode(String systemCode) {
		return (TransformedFileType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getTransformedFileTypeBySystemCode", systemCode));
	}


	/**
	 * Get a transformed file type by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public TransformedFileType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make a transformed file type persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(TransformedFileType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make a transformed file type transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(TransformedFileType entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

}
