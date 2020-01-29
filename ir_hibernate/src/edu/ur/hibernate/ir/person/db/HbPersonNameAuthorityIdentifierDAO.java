package edu.ur.hibernate.ir.person.db;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.person.PersonNameAuthorityIdentifier;
import edu.ur.ir.person.PersonNameAuthorityIdentifierDAO;

public class HbPersonNameAuthorityIdentifierDAO implements PersonNameAuthorityIdentifierDAO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7456183561910736463L;
	
	/** hibernate helper  */
	private final HbCrudDAO<PersonNameAuthorityIdentifier> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbPersonNameAuthorityIdentifierDAO() {
		hbCrudDAO = new HbCrudDAO<PersonNameAuthorityIdentifier>(PersonNameAuthorityIdentifier.class);
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


	@Override
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personNameAuthorityIdentifierCount"));
	}

	@Override
	public PersonNameAuthorityIdentifier getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	@Override
	public void makePersistent(PersonNameAuthorityIdentifier entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	@Override
	public void makeTransient(PersonNameAuthorityIdentifier entity) {
		hbCrudDAO.makeTransient(entity);
		
	}

	@Override
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	@Override
	public PersonNameAuthorityIdentifier getByTypeValue(Long identfierTypeId, String value) {
		Object[] values = new Object[] {identfierTypeId, value};
		return (PersonNameAuthorityIdentifier)HbHelper.getUnique( hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonNameAuthorityIdentifier", values));
	
	}

	@Override
	public PersonNameAuthorityIdentifier getByTypeAuthority(Long identfierTypeId, Long personNameAuthorityId) {
		Object[] values = new Object[] {identfierTypeId, personNameAuthorityId};
		return (PersonNameAuthorityIdentifier)HbHelper.getUnique( hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonNameAuthorityIdentifierByPersonAuthorityId", values));
	}

}
