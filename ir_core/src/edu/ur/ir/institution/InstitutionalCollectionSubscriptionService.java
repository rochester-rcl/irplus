/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  

package edu.ur.ir.institution;

import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import edu.ur.ir.user.IrUser;

/**
 * Service for dealing with subscription information.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalCollectionSubscriptionService 
{

	/**
	 * Get an institutional collection subscription by id.
	 * 
	 * @param id - id of the subscription
	 * @param lock - upgrade the lock on the subscription object
	 * @return - the subscription if found otherwise null.
	 */
	public InstitutionalCollectionSubscription getById(Long id, boolean lock);

	/**
	 * Save the institutional repository subscription.
	 * 
	 * @param entity
	 */
	public void save(InstitutionalCollectionSubscription entity);

	/**
	 * Delete the institutional repository subscription.
	 * 
	 * @param entity
	 */
	public void delete(InstitutionalCollectionSubscription entity);

	/**
	 * Get all the subscriptions for a given user.
	 * 
	 * @param user - user who is subscribed tothe collection
	 * @return all subscriptions for a user
	 */
	public List<InstitutionalCollectionSubscription> getAllSubscriptionsForUser( IrUser user);

	/**
	 * Get a count of subscribers for a given collection.
	 * 
	 * @param institutionalCollection - institutional collection to get the count for
	 * @return - total number of subscribers for a given collection.
	 */
	public Long getSubscriberCount(InstitutionalCollection institutionalCollection);
	
	/**
	 * Returns true if the user is subscribed to the specified collection.
	 * 
	 * @param collection - collection to check
	 * @param user - user to check
	 * @return - true if the user is subscribed to the specified collection.
	 */
	public boolean isSubscribed(InstitutionalCollection collection, IrUser user);
	
	/**
	 * Get a list of all unique subscriber user id's.  This allows
	 * one single list of all users who have at least one subscription
	 * in the system, the user id is not duplicated in this list even
	 * if they have more than one subscription
	 * 
	 * @return list of unique user id's
	 */
	public List<Long> getUniqueSubsciberUserIds();
	
	/**
	 * Send a subscriber an email for their subscriptions.  This sends items that
	 * were added to a all subscribed to collections between the given start and end date
	 * 
	 * @param user - user who subscribes to institutional collections
	 * @param startDate - start date for items to be sent
	 * @param endDate - date range end for items to be sent
	 * @throws MessagingException
	 */
	public void sendSubscriberEmail(IrUser user, Date startDate, Date endDate) throws MessagingException;
}
