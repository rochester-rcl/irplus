package edu.ur.hibernate.ir.item.metadata.marc.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.marc.MarcContributorTypeRelatorCode;
import edu.ur.ir.marc.MarcContributorTypeRelatorCodeDAO;


/**
 * Hibernate impelmentation of mapping marc contributor type relator codes
 * 
 * @author Nathan Sarr
 *
 */
public class HbMarcContributorTypeRelatorCodeDAO implements MarcContributorTypeRelatorCodeDAO{
	
	
	// eclipse generated id
	private static final long serialVersionUID = -4732942685566691684L;
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<MarcContributorTypeRelatorCode> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbMarcContributorTypeRelatorCodeDAO() {
		hbCrudDAO = new HbCrudDAO<MarcContributorTypeRelatorCode>(MarcContributorTypeRelatorCode.class);
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


	public MarcContributorTypeRelatorCode getByContributorType(
			Long contributorId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcContributorTypeRelatorCodeByContributorTypeId");
		q.setParameter("contributorId", contributorId);
		return (MarcContributorTypeRelatorCode)q.uniqueResult();
	}

	public MarcContributorTypeRelatorCode getByRelatorCode(Long relatorCodeId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcContributorTypeRelatorCodeByRelatorCodeId");
		q.setParameter("relatorCodeId", relatorCodeId);
		return (MarcContributorTypeRelatorCode)q.uniqueResult();
	}

	public List<MarcContributorTypeRelatorCode> getAll() {
		return hbCrudDAO.getAll();
	}

	public MarcContributorTypeRelatorCode getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(MarcContributorTypeRelatorCode entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(MarcContributorTypeRelatorCode entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
