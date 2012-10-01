package edu.ur.hibernate.ir.item.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.PlaceOfPublicationDAO;
import edu.ur.order.OrderType;

public class HbPlaceOfPublicationDAO implements PlaceOfPublicationDAO{

	//Eclipse generated id
	private static final long serialVersionUID = -3265165298170217242L;
	
	/** hibernate helper */
	private final HbCrudDAO<PlaceOfPublication> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbPlaceOfPublicationDAO() {
		hbCrudDAO = new HbCrudDAO<PlaceOfPublication>(PlaceOfPublication.class);
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
	 * Get the place of publication by letter code.
	 * 
	 * @see edu.ur.ir.item.PlaceOfPublicationDAO#getByLetterCode(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<PlaceOfPublication> getByLetterCode(String letterCode) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getPlaceOfPublicationByLetterCode");
		q.setParameter("letterCode", letterCode);
		return q.list();
	}

	/**
	 * Get the list of publications ordered by name.
	 * 
	 * @see edu.ur.ir.item.PlaceOfPublicationDAO#getOrderByName(int, int, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<PlaceOfPublication> getOrderByName(int rowStart,
			int numberOfResultsToShow, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();

		Query q = null;
		if (orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = session.getNamedQuery("getPlaceOfPublicationOrderByNameDesc");
		} else {
			q = session.getNamedQuery("getPlaceOfPublicationOrderByNameAsc");
		}
		
		q.setFirstResult(rowStart);
		q.setMaxResults(numberOfResultsToShow);
		q.setCacheable(false);
		q.setReadOnly(true);
		q.setFetchSize(numberOfResultsToShow);
		return q.list();

	}

	/**
	 * Get the count of places of publication.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("placeOfPublicationCount").uniqueResult();
	}

	/**
	 * Get a list of all publications.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<PlaceOfPublication> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get the place of publication by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public PlaceOfPublication getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the publication persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(PlaceOfPublication entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Remove the place of publication from storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(PlaceOfPublication entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the place of publication by unique name.
	 * 
	 * @see edu.ur.dao.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public PlaceOfPublication findByUniqueName(String name) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getPlaceOfPublicationByName");
		q.setParameter("name", name);
		return (PlaceOfPublication) q.uniqueResult();
	}

}
