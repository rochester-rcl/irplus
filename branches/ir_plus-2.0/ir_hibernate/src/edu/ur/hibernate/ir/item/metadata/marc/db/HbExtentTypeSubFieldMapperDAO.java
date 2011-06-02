package edu.ur.hibernate.ir.item.metadata.marc.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.item.metadata.marc.ExtentTypeSubFieldMapper;
import edu.ur.ir.item.metadata.marc.ExtentTypeSubFieldMapperDAO;

/**
 * Hibernate implementation of the extent type sub field mapper.
 * 
 * @author Nathan Sarr
 *
 */
public class HbExtentTypeSubFieldMapperDAO implements ExtentTypeSubFieldMapperDAO{
	
	// Eclipse generated id.
	private static final long serialVersionUID = -4380483371665857060L;
	
	// Helper for persisting information using hibernate.  */	
	private final HbCrudDAO<ExtentTypeSubFieldMapper> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public  HbExtentTypeSubFieldMapperDAO() {
		hbCrudDAO = new HbCrudDAO<ExtentTypeSubFieldMapper>(ExtentTypeSubFieldMapper.class);
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

	@SuppressWarnings("unchecked")
	public List<ExtentTypeSubFieldMapper> getByExtentTypeId(Long id) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getExtentTypeMapperByExtentTypeId");
		q.setParameter("extentTypeId", id);
		return q.list();
	}

	public List<ExtentTypeSubFieldMapper> getAll() {
		return hbCrudDAO.getAll();
	}

	public ExtentTypeSubFieldMapper getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(ExtentTypeSubFieldMapper entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(ExtentTypeSubFieldMapper entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
