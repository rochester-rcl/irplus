package edu.ur.hibernate.ir.institution.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSubscription;
import edu.ur.ir.institution.InstitutionalCollectionSubscriptionDAO;
import edu.ur.ir.user.IrUser;

/**
 * Hibernate implementation of data access object for institutional collection subscriptions
 * @author Nathan Sarr
 *
 */
public class HbInstitutionalCollectionSubscriptionDAO implements InstitutionalCollectionSubscriptionDAO{

	
	/** eclipse generated id */
	private static final long serialVersionUID = -152511656680666524L;
	
	/** Helper for persisting information using hibernate.  */
	private final HbCrudDAO<InstitutionalCollectionSubscription> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbInstitutionalCollectionSubscriptionDAO() {
		hbCrudDAO = new HbCrudDAO<InstitutionalCollectionSubscription>(InstitutionalCollectionSubscription.class);
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
	 * Get institutional collection subscription by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public InstitutionalCollectionSubscription getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	/**
	 * Save the institutional collection subscription.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(InstitutionalCollectionSubscription entity) {
		hbCrudDAO.makePersistent(entity);
	}

	
	/**
	 * Delete the institutional collection subscription.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(InstitutionalCollectionSubscription entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	/**
	 * Get all institutional collection subscriptions for a user.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionDAO#getAllSubscriptionsForUser(edu.ur.ir.user.IrUser)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollectionSubscription> getAllSubscriptionsForUser(
			IrUser user) {
		return (List<InstitutionalCollectionSubscription>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getInstitutionalSubscriptionsForUser", user.getId());

	}

	
	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionDAO#getSubscriberCount(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public Long getSubscriberCount(
			InstitutionalCollection institutionalCollection) {
		return (Long) HbHelper.getUnique( hbCrudDAO.getHibernateTemplate().findByNamedQuery("getInstitutionalCollectionSubscriptionCount", institutionalCollection.getId()) );
	}

	
	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionDAO#isSubscriberCount(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.user.IrUser)
	 */
	public Long isSubscriberCount(
			InstitutionalCollection institutionalCollection, IrUser user) {
		Object[] values = new Object[] {institutionalCollection.getId(), user.getId()};
		return (Long) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getInstitutionalCollectionSubscriptionCountUser", 
				values));
	}

	
	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionDAO#getUniqueSubsciberUserIds()
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getUniqueSubsciberUserIds() {
		return (List<Long>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getUniqueUserIdsForSubscriptions");
	}

	/**
	 * Get the subscriber for the specified user in the given collection.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionDAO#getSubscriptionForUser(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.user.IrUser)
	 */
	public InstitutionalCollectionSubscription getSubscriptionForUser(
			InstitutionalCollection institutionalCollection, IrUser user) {
		Query q =  hbCrudDAO.getSessionFactory().getCurrentSession()
					.getNamedQuery("getInstitutionalCollectionSubscriptionUser");
		q.setLong("collectionId", institutionalCollection.getId());
		q.setLong("userId", user.getId());
		return (InstitutionalCollectionSubscription)q.uniqueResult();
	}

}
