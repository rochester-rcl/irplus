package edu.ur.hibernate.ir.handle.db;

import java.util.List;

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
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("handleInfoCount"));
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
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

}
