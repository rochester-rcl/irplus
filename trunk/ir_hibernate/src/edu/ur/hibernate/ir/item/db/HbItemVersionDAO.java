
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
import edu.ur.ir.item.ItemVersion;
import edu.ur.ir.item.ItemVersionDAO;

/**
 * Represents a single version of an item.
 * 
 * @author Nathan Sarr
 *
 */
public class HbItemVersionDAO implements ItemVersionDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -7055162835621939185L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<ItemVersion> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbItemVersionDAO() {
		hbCrudDAO = new HbCrudDAO<ItemVersion>(ItemVersion.class);
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

	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("itemVersionCount"));
	}

	public ItemVersion getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(ItemVersion entity) {
        hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(ItemVersion entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the list of ItemVersions that has this specified IrFile
	 * 	
	 * @param irFileId Id of the IrFile
	 * @return list of ItemVersion.
	 */
	@SuppressWarnings("unchecked")
	public List<ItemVersion> getItemVersionContainingIrFile(Long irFileId)
	{
		return (List<ItemVersion>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getItemVersionContainingIrFile", irFileId);
	}
	
	/**
	 * Get the list of ItemVersions that has this specified GenericItem
	 * 	
	 * @param itemId Id of GenericItem
	 * @return Count of ItemVersion.
	 */
	public Long getItemVersionCount(Long itemId) 
	{
		return (Long) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getItemVersionCountContainingGenericItem", itemId));
	}

}
