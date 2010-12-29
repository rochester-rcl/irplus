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

package edu.ur.hibernate.ir.groupspace.db;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupSpace;
import edu.ur.ir.groupspace.GroupSpaceDAO;
import edu.ur.order.OrderType;

/**
 * Hibernate DAO implementation for group spaces.
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupSpaceDAO implements GroupSpaceDAO
{

	/** eclipse generated id*/
	private static final long serialVersionUID = -7945008201300712513L;
	
	/** hibernate helper  */
	private final HbCrudDAO<GroupSpace> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupSpaceDAO() {
		hbCrudDAO = new HbCrudDAO<GroupSpace>(GroupSpace.class);
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
	 * Get the group space by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public GroupSpace getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Save the group to persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupSpace entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Remove the group space from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupSpace entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the count for the groups.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupSpaceCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get the group space by it's unique name.
	 * 
	 * @see edu.ur.dao.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public GroupSpace findByUniqueName(String name) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupSpaceByName");
		q.setParameter("lowerCaseName", name.toLowerCase());
		return (GroupSpace)q.uniqueResult();
	}
	
	/**
	 * Get the list of group spaces ordered by name.
	 * 
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param orderType - Order (Desc/Asc) 
	 * 
	 * @return list of group spaces found.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupSpace> getGroupspacesNameOrder(int rowStart, int numberOfResultsToShow, OrderType orderType)
	{	
	    Query q = null;
	    Session session = hbCrudDAO.getSessionFactory() .getCurrentSession();
	    if(orderType.equals(OrderType.ASCENDING_ORDER))
	    {
	        q = session.getNamedQuery("groupSpaceByNameAsc");
	    } 
	    else
	    {
	        q = session.getNamedQuery("groupSpaceByNameDesc");
	    } 
			    
	    q.setFirstResult(rowStart);
	    q.setMaxResults(numberOfResultsToShow);
		q.setFetchSize(numberOfResultsToShow);
	    return q.list();
	}


}
