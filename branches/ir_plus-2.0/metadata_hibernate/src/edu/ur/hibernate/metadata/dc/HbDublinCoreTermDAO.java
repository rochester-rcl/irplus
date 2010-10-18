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

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.metadata.dc.DublinCoreTerm;
import edu.ur.metadata.dc.DublinCoreTermDAO;

public class HbDublinCoreTermDAO implements DublinCoreTermDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -807065997465252196L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<DublinCoreTerm> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbDublinCoreTermDAO() {
		hbCrudDAO = new HbCrudDAO<DublinCoreTerm>(DublinCoreTerm.class);
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
		return (Long)hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("dublinCoreTermCount").uniqueResult();
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<DublinCoreTerm> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public DublinCoreTerm getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(DublinCoreTerm entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(DublinCoreTerm entity) {
		hbCrudDAO.makeTransient(entity);	
	}

	/**
	 * @see edu.ur.dao.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public DublinCoreTerm findByUniqueName(String name) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDublinCoreTermByName");
		q.setParameter(0, name);
		return (DublinCoreTerm)q.uniqueResult();
	}

}
