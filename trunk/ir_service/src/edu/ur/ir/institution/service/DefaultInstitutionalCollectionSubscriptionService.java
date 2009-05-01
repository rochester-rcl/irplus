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

package edu.ur.ir.institution.service;

import java.util.List;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSubscription;
import edu.ur.ir.institution.InstitutionalCollectionSubscriptionDAO;
import edu.ur.ir.institution.InstitutionalCollectionSubscriptionService;
import edu.ur.ir.user.IrUser;

/**
 * Institutional Collection subscription service implementation.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalCollectionSubscriptionService implements InstitutionalCollectionSubscriptionService{

	/**
	 * Institutional collection subscription data access object
	 */
	private InstitutionalCollectionSubscriptionDAO institutionalCollectionSubscriptionDAO;
	
	/**
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#delete(edu.ur.ir.institution.InstitutionalCollectionSubscription)
	 */
	public void delete(InstitutionalCollectionSubscription entity) {
		institutionalCollectionSubscriptionDAO.makeTransient(entity);
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#getAllSubscriptionsForUser(edu.ur.ir.user.IrUser)
	 */
	public List<InstitutionalCollectionSubscription> getAllSubscriptionsForUser(
			IrUser user) {
		return institutionalCollectionSubscriptionDAO.getAllSubscriptionsForUser(user);
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#getById(java.lang.Long, boolean)
	 */
	public InstitutionalCollectionSubscription getById(Long id, boolean lock) {
		return institutionalCollectionSubscriptionDAO.getById(id, lock);
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#getSubscriberCount(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public Long getSubscriberCount(
			InstitutionalCollection institutionalCollection) {
		return institutionalCollectionSubscriptionDAO.getSubscriberCount(institutionalCollection);
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#save(edu.ur.ir.institution.InstitutionalCollectionSubscription)
	 */
	public void save(InstitutionalCollectionSubscription entity) {
		institutionalCollectionSubscriptionDAO.makePersistent(entity);
	}

	public InstitutionalCollectionSubscriptionDAO getInstitutionalCollectionSubscriptionDAO() {
		return institutionalCollectionSubscriptionDAO;
	}

	public void setInstitutionalCollectionSubscriptionDAO(
			InstitutionalCollectionSubscriptionDAO institutionalCollectionSubscriptionDAO) {
		this.institutionalCollectionSubscriptionDAO = institutionalCollectionSubscriptionDAO;
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#isSubscribed(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.user.IrUser)
	 */
	public boolean isSubscribed(InstitutionalCollection collection, IrUser user) {
		return institutionalCollectionSubscriptionDAO.isSubscriberCount(collection, user) == 1l;
	}

	
	public void sendSubribersEmails() {
		// TODO Auto-generated method stub
		
	}

}
