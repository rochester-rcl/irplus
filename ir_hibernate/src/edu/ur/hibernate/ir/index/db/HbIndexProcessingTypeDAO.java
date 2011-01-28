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


package edu.ur.hibernate.ir.index.db;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeDAO;

/**
 * Hibernate implementation for accessing and storing index processing types.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIndexProcessingTypeDAO implements IndexProcessingTypeDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = 1651892446128435565L;

	/** Helper for persisting information using hibernate. */
	private final HbCrudDAO<IndexProcessingType> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public  HbIndexProcessingTypeDAO() {
		hbCrudDAO = new HbCrudDAO<IndexProcessingType>(IndexProcessingType.class);
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
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("indexProcessingTypeCount"));
	}

	public List<IndexProcessingType> getAll() {
		return hbCrudDAO.getAll();
	}

	public IndexProcessingType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(IndexProcessingType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(IndexProcessingType entity) {
		hbCrudDAO.makeTransient(entity);
	}

	public IndexProcessingType findByUniqueName(String name) {
		return (IndexProcessingType) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIndexProcessingTypeByName", name));
	}

}
