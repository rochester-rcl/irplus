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
import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleInfoDAO;

/**
 * Implementation of handle information.
 * 
 * @author Nathan Sarr
 *
 */
public class HbHandleInfoDAO implements HandleInfoDAO{

	
	/** eclipse generated id */
	private static final long serialVersionUID = -6672015070747082325L;
	
	/**  Helper for persisting information using hibernate. */
	private final HbCrudDAO<HandleInfo> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbHandleInfoDAO() {
		hbCrudDAO = new HbCrudDAO<HandleInfo>(HandleInfo.class);
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("handleInfoCount");
		return (Long)q.uniqueResult();
	}
	
	/**
	 * Get the handle by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public HandleInfo getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the handle persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(HandleInfo entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the handle transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(HandleInfo entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * @see edu.ur.ir.handle.HandleInfoDAO#get(java.lang.String, java.lang.String)
	 */
	public HandleInfo get(String authorityName, String localName) {
		Object[] values = new Object[] {authorityName, localName};
		return (HandleInfo)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("handleByNameAuthorityLocalName", values));
	}
	
	/**
	 * @see edu.ur.ir.handle.HandleInfoDAO#getAllHandlesForAuthority(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<HandleInfo> getAllHandlesForAuthority(String authorityName)
	{
		return (List<HandleInfo>)hbCrudDAO.getHibernateTemplate().findByNamedQuery("findAllHandlesByNameAuthority", authorityName);
	}
	
	/**
	 * Get a count of the handles with the specified name authority.
	 *  
	 * @param nameAuthorityId - id for the name authority
	 * @return - count of handles found for the name authority
	 */
	public Long getHandleCountForNameAuthority(Long nameAuthorityId)
	{		
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("countHandleByNameAuthority", nameAuthorityId));
	}

}
