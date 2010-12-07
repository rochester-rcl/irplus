/**  
   Copyright 2008-2010 University of Rochester

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
import edu.ur.ir.item.ItemContentType;
import edu.ur.ir.item.ItemContentTypeDAO;

/**
 * Hibernate implementation of the item content type Data access object.
 * 
 * @author Nathan Sarr
 *
 */
public class HbItemContentTypeDAO implements ItemContentTypeDAO {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -7738822339808851908L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<ItemContentType> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbItemContentTypeDAO() {
		hbCrudDAO = new HbCrudDAO<ItemContentType>(ItemContentType.class);
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
	 * Get the item content type by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ItemContentType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Add the item content type to the database.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ItemContentType entity) {
		hbCrudDAO.makePersistent(entity);	
	}

	/**
	 * Remove the item content type from the database.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ItemContentType entity) {
		hbCrudDAO.makeTransient(entity);	
	}

}
