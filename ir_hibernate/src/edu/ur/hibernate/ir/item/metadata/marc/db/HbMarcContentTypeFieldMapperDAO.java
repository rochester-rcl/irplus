package edu.ur.hibernate.ir.item.metadata.marc.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.marc.MarcContentTypeFieldMapper;
import edu.ur.ir.marc.MarcContentTypeFieldMapperDAO;

/**
 * Implementation of the Marc Content type field mapper data access object.
 * 
 * @author Nathan Sarr
 *
 */
public class HbMarcContentTypeFieldMapperDAO implements MarcContentTypeFieldMapperDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -5853383312640313089L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<MarcContentTypeFieldMapper> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbMarcContentTypeFieldMapperDAO() {
		hbCrudDAO = new HbCrudDAO<MarcContentTypeFieldMapper>(MarcContentTypeFieldMapper.class);
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


	
	public MarcContentTypeFieldMapper getByContentTypeId(Long contentTypeId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcContentTypeFieldMapperByContentTypeId");
		q.setParameter("contentTypeId", contentTypeId);
        return (MarcContentTypeFieldMapper)q.uniqueResult();
	}

	
	public List<MarcContentTypeFieldMapper> getAll() {
		return hbCrudDAO.getAll();
	}

	
	public MarcContentTypeFieldMapper getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	public void makePersistent(MarcContentTypeFieldMapper entity) {
		hbCrudDAO.makePersistent(entity);
	}

	
	public void makeTransient(MarcContentTypeFieldMapper entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
