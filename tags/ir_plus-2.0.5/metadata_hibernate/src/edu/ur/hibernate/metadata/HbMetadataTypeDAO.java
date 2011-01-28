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

package edu.ur.hibernate.metadata;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.metadata.MetadataType;
import edu.ur.metadata.MetadataTypeDAO;

/**
 * Hibernate implementation for creating a metadata type.
 * 
 * @author Nathan Sarr
 *
 */
public class HbMetadataTypeDAO implements MetadataTypeDAO {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 3305183134967994571L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<MetadataType> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbMetadataTypeDAO() {
		hbCrudDAO = new HbCrudDAO<MetadataType>(MetadataType.class);
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
	 * Get a count of the metadata types
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("metadataTypeCount").uniqueResult();
	}

	/**
	 * Return all metadata types.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<MetadataType> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get a metadata type by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public MetadataType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Save the metadata type.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(MetadataType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Remove the metadata from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(MetadataType entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/* (non-Javadoc)
	 * @see edu.ur.dao.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public MetadataType findByUniqueName(String name) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMetadataTypeByName");
		q.setParameter(0, name);
		return (MetadataType)q.uniqueResult();
	}

}
