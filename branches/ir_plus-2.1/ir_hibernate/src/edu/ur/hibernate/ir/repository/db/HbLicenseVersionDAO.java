package edu.ur.hibernate.ir.repository.db;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.repository.LicenseVersion;
import edu.ur.ir.repository.LicenseVersionDAO;

/**
 * Hibernate implementation of the license version DAO
 * 
 * @author Nathan Sarr
 *
 */
public class HbLicenseVersionDAO implements LicenseVersionDAO{

	/**  eclipse generated id*/
	private static final long serialVersionUID = -3821515297732061734L;

	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<LicenseVersion> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbLicenseVersionDAO() {
		hbCrudDAO = new HbCrudDAO<LicenseVersion>(LicenseVersion.class);
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
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	
	/**
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public LicenseVersion getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(LicenseVersion entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	/**
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(LicenseVersion entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
