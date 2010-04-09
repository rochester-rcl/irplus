package edu.ur.hibernate.ir.handle.db;

import java.util.List;

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
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("handleNameAuthorityCount"));
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
