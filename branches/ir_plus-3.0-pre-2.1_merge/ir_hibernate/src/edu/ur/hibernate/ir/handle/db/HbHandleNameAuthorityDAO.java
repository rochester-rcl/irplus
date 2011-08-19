/**  
   Copyright 2008 - 2010 University of Rochester

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

package edu.ur.hibernate.ir.handle.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.handle.HandleNameAuthorityDAO;
import edu.ur.ir.handle.HandleNameAuthority;

/**
 * Implementation of hibernate for handle name authority.
 * 
 * 
 * @author Nathan Sarr
 *
 */
public class HbHandleNameAuthorityDAO implements HandleNameAuthorityDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = 4061595155359791146L;
	
	/**  Helper for persisting information using hibernate. */
	private final HbCrudDAO<HandleNameAuthority> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbHandleNameAuthorityDAO() {
		hbCrudDAO = new HbCrudDAO<HandleNameAuthority>(HandleNameAuthority.class);
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("handleNameAuthorityCount");
		return (Long)q.uniqueResult();
	}

	
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	
	public HandleNameAuthority getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	public void makePersistent(HandleNameAuthority entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(HandleNameAuthority entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	public HandleNameAuthority findByUniqueName(String nameAuthority) {
		return (HandleNameAuthority) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getHandleNameAuthorityByName", nameAuthority));
	}

}
