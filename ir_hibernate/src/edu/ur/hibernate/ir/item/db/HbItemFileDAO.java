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

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemFileDAO;


/**
 * Data access for Item files
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbItemFileDAO implements ItemFileDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 7758370554190005246L;
	
	/** hibernate helper */
	private final HbCrudDAO<ItemFile> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbItemFileDAO() {
		hbCrudDAO = new HbCrudDAO<ItemFile>(ItemFile.class);
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
	 * Find a item file by id
	 * 
	 * @see edu.ur.dao.CrudDAO#findById(java.lang.Long, boolean)
	 */
	public ItemFile findById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Get a item file by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ItemFile getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make a item file persistent. 
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ItemFile entity) {
		hbCrudDAO.makePersistent((ItemFile)entity);
	}


	/**
	 * Make a item file transient.  
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ItemFile entity) {
		hbCrudDAO.makeTransient((ItemFile)entity);
	}

	/**
	 * Get a count of the item files containing this irFile
	 * 
	 * @see edu.ur.ir.item.ItemFileDAO#getItemFileCount(Long)
	 */
	public Long getItemFileCount(Long irFileId) {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getItemFileCount", irFileId));
	}
}
