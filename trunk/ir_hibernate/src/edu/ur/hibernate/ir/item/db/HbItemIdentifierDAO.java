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
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemIdentifierDAO;

/**
 * Hibernate implementation of Item Identifier.
 * 
 * @author Nathan Sarr
 *
 */
public class HbItemIdentifierDAO implements ItemIdentifierDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -6596312372131845233L;
	
	/** hibernate helper  */
	private final HbCrudDAO<ItemIdentifier> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbItemIdentifierDAO() {
		hbCrudDAO = new HbCrudDAO<ItemIdentifier>(ItemIdentifier.class);
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
	 * Get the item identifier by type and value
	 * 
	 * @param identfierType the type of identifier
	 * @param value the value of the identifier.
	 * 
	 * @return the item identifier found.
	 */
	public ItemIdentifier getByTypeValue(Long identifierTypeId, String value) {
		Object[] values = new Object[] {identifierTypeId, value};
		return (ItemIdentifier)HbHelper.getUnique( hbCrudDAO.getHibernateTemplate().findByNamedQuery("getItemIdentifier", values));
	}

	/**
	 * Get a count of the identifier types in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("itemIdentifierCount"));
	}

	public ItemIdentifier getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(ItemIdentifier entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(ItemIdentifier entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

}
