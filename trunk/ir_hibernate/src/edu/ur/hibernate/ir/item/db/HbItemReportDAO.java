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

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.item.ItemReport;
import edu.ur.ir.item.ItemReportDAO;


/**
 * Data access for Item report
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbItemReportDAO implements ItemReportDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 1684509493457805143L;

	/** hibernate helper  */
	private final HbCrudDAO<ItemReport> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbItemReportDAO() {
		hbCrudDAO = new HbCrudDAO<ItemReport>(ItemReport.class);
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
	 * Find a item report by id
	 * 
	 * @see edu.ur.dao.CrudDAO#findById(java.lang.Long, boolean)
	 */
	public ItemReport findById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}


	/**
	 * Get all item report
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<ItemReport> getAll() {
		return hbCrudDAO.getAll();
	}

	
	/**
	 * Get a item report by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ItemReport getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make a item report persistent. 
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ItemReport entity) {
		hbCrudDAO.makePersistent((ItemReport)entity);
	}


	/**
	 * Make a item report transient.  
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ItemReport entity) {
		hbCrudDAO.makeTransient((ItemReport)entity);
	}
}
