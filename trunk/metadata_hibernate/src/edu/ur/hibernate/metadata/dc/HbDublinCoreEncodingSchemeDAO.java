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

package edu.ur.hibernate.metadata.dc;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.metadata.dc.DublinCoreEncodingScheme;
import edu.ur.metadata.dc.DublinCoreEncodingSchemeDAO;

/**
 * @author NathanS
 *
 */
public class HbDublinCoreEncodingSchemeDAO implements DublinCoreEncodingSchemeDAO{
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<DublinCoreEncodingScheme> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbDublinCoreEncodingSchemeDAO() {
		hbCrudDAO = new HbCrudDAO<DublinCoreEncodingScheme>(DublinCoreEncodingScheme.class);
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
	 * Get the count of dublin core elements.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("dublinCoreEncodingSchemeCount"));
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<DublinCoreEncodingScheme> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public DublinCoreEncodingScheme getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(DublinCoreEncodingScheme entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(DublinCoreEncodingScheme entity) {
		hbCrudDAO.makeTransient(entity);	
	}

	/**
	 * @see edu.ur.dao.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public DublinCoreEncodingScheme findByUniqueName(String name) {
		return (DublinCoreEncodingScheme) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getDublinCoreEncodingSchemeByName", name));
	}


}
