package edu.ur.hibernate.ir.groupspace.db;


import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupSpace;
import edu.ur.ir.groupspace.GroupSpaceDAO;

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

}
