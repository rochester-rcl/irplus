package edu.ur.ir.institution;

import java.util.List;

import edu.ur.dao.CrudDAO;
import edu.ur.ir.user.IrUser;

/**
 * Institutional collection subscrition data access object interface.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalCollectionSubscriptionDAO extends
CrudDAO<InstitutionalCollectionSubscription>{
	
	/**
	 * Get all subscriptions for a given user.
	 * 
	 * @param user - user to get all subscriptions for
	 * @return - set of subscriptions for a given user.
	 */
	public List<InstitutionalCollectionSubscription> getAllSubscriptionsForUser(IrUser user);

}
