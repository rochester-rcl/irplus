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

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;


public class HbIrFileDAO implements IrFileDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -7946714825439790785L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<IrFile> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbIrFileDAO() {
		hbCrudDAO = new HbCrudDAO<IrFile>(IrFile.class);
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
	 * Get a count of the IrFiles
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irFileCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get all the IrFiles with the specified name.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<IrFile> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(IrFile.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<IrFile>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all IrFiles ordered by name.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<IrFile> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllIrFileNameAsc", startRecord, numRecords);
	}

	/**
	 * Find IrFiles listed by name.
	 * 
	 * @see edu.ur.NonUniqueNameDAO#findByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<IrFile> findByName(String name) {
	  	return (List<IrFile>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrFileByName", name);
	}

	/**
	 * Return IrFile by id
	 */
	public IrFile getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IrFile entity) {
		
		hbCrudDAO.makePersistent(entity);
		
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IrFile entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
