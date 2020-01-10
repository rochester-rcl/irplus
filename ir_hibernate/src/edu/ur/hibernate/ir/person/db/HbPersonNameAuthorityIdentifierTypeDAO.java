package edu.ur.hibernate.ir.person.db;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.person.PersonNameAuthorityIdentifierType;
import edu.ur.ir.person.PersonNameAuthorityIdentifierTypeDAO;

public class HbPersonNameAuthorityIdentifierTypeDAO implements PersonNameAuthorityIdentifierTypeDAO{

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 8106567109151451928L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<PersonNameAuthorityIdentifierType> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbPersonNameAuthorityIdentifierTypeDAO() {
		hbCrudDAO = new HbCrudDAO<PersonNameAuthorityIdentifierType>(PersonNameAuthorityIdentifierType.class);
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
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personNameIdentifierTypeCount"));
	}

	@Override
	public PersonNameAuthorityIdentifierType getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	@Override
	public void makePersistent(PersonNameAuthorityIdentifierType entity) {
		hbCrudDAO.makePersistent(entity);
	}

	@Override
	public void makeTransient(PersonNameAuthorityIdentifierType entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@Override
	public List<PersonNameAuthorityIdentifierType> getAll() {
		return hbCrudDAO.getAll();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PersonNameAuthorityIdentifierType> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(PersonNameAuthorityIdentifierType.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<PersonNameAuthorityIdentifierType>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	@Override
	@SuppressWarnings("unchecked")
	public PersonNameAuthorityIdentifierType findByUniqueName(String name) {
		return (PersonNameAuthorityIdentifierType) 
			    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonNameIdentifierTypeByName", name));
	}

	@Override
	public PersonNameAuthorityIdentifierType getByUniqueSystemCode(String uniqueSystemCode) {
		return  (PersonNameAuthorityIdentifierType) 
			    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonNameIdentifierTypeByUniqueSystemCode", uniqueSystemCode));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PersonNameAuthorityIdentifierType> getIdentifierTypesOrderByName(final int rowStart,
			final int numberOfResultsToShow, final String sortType) {
		List<PersonNameAuthorityIdentifierType> identifierTypes = (List<PersonNameAuthorityIdentifierType>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getPersonNameIdentifierTypesOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getPersonNameIdentifierTypesOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();
            }
        });
        return identifierTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersonNameAuthorityIdentifierType> getAllOrderByName(int startRecord, int numRecords) {
		DetachedCriteria dc = DetachedCriteria.forClass(PersonNameAuthorityIdentifierType.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<PersonNameAuthorityIdentifierType>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

}
