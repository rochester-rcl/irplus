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

package edu.ur.ir.web.action.institution;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalCollectionSubscriptionService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to subscribe to collection.  This is only for users.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManageCollectionSubscription extends ActionSupport implements UserIdAware, Preparable {

	/** Eclipse generated id */
	private static final long serialVersionUID = 979475905589306686L;
	
	/**  Logger. */
	private static final Logger log = Logger.getLogger(ManageCollectionSubscription.class);
	
	/** Id of the user to subscribe/unsubscribe */
	private Long subscribeUserId;
	
	/** Id of user logged in */
	private Long userId;
	
	/** Id of institutional collection */
	private Long collectionId;
	
	/** Institutional collection service */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Service for dealing with institutional collection subscription service */
	private InstitutionalCollectionSubscriptionService institutionalCollectionSubscriptionService;
	
	/** User service */
	private UserService userService;
	
	/** Indicates whether to subscribe to all its sub collections */
	private boolean includeSubCollections;
	
	/** Indicates whether the user has subscribed to this collection */
	private boolean isSubscriber;
	
	/** Institutional collection */
	private InstitutionalCollection collection;
	
	/**
	 * Prepare for action
	 */
	public void prepare() {
		collection = institutionalCollectionService.getCollection(collectionId, false);
	}
	
	/**
	 * Subscribe to the collection
	 * 
	 * @return
	 */
	public String subscribe() {
		
		
		log.debug("userId="+userId);
		log.debug("subscribeUserId="+userId);
		log.debug("collectionId="+collectionId);
		IrUser user = userService.getUser(userId, false);
		
		IrUser subscribeUser = userService.getUser(subscribeUserId, false);
		
		if(!subscribeUserId.equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE) && !user.hasRole(IrRole.COLLECTION_ADMIN_ROLE))
    	{
			//destination does not belong to user
    		log.error("user does not have subscirbe privileges for user = " + user);
    		return("accessDenied");
    		
    	}
		
		collection.addSuscriber(subscribeUser);
		institutionalCollectionService.saveCollection(collection);

		if (includeSubCollections) {
			List<InstitutionalCollection> subCollections = institutionalCollectionService.getAllChildrenForCollection(collection);
			
			for (InstitutionalCollection collection : subCollections) {
				collection.addSuscriber(user);
				institutionalCollectionService.saveCollection(collection);
			}
		}
		
		isSubscriber = institutionalCollectionSubscriptionService.isSubscribed(collection, user);
		return SUCCESS;
		
	}

	/**
	 * Unsubscribe user from the collection
	 * 
	 * @return
	 */
	public String unSubscribe() {

		log.debug("userId="+userId);
		log.debug("collectionId="+collectionId);
		IrUser user = userService.getUser(userId, false);
		IrUser subscribeUser = userService.getUser(subscribeUserId, false);
		
		if(!subscribeUserId.equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE) && !user.hasRole(IrRole.COLLECTION_ADMIN_ROLE))
    	{
			//destination does not belong to user
    		log.error("user does not have subscirbe privileges for user = " + user);
    		return("accessDenied");
    		
    	}
		
		collection.removeSubscriber(subscribeUser);
		institutionalCollectionService.saveCollection(collection);

		isSubscriber = institutionalCollectionSubscriptionService.isSubscribed(collection, subscribeUser);
		return SUCCESS;
	}
	
	/**
	 * Get whether the user has subscribed to this collection
	 * 
	 * @return
	 */
	public String getUserSubscriptionForThisCollection() {
		
		log.debug("userId="+userId);
		log.debug("collectionId="+collectionId);
		IrUser user = userService.getUser(userId, false);
		IrUser subscribeUser = userService.getUser(subscribeUserId, false);
		
		if(!subscribeUserId.equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE) && !user.hasRole(IrRole.COLLECTION_ADMIN_ROLE))
    	{
			//destination does not belong to user
    		log.error("user does not have subscirbe privileges for user = " + user);
    		return("accessDenied");
    		
    	}
		
		isSubscriber = institutionalCollectionSubscriptionService.isSubscribed(collection, subscribeUser);
		log.debug("isSubscriber="+isSubscriber);
		
		return SUCCESS;
	}
	
	
	public Long getCollectionId() {
		return collectionId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}

	public void setIncludeSubCollections(boolean includeSubCollections) {
		this.includeSubCollections = includeSubCollections;
	}

	public boolean isSubscriber() {
		return isSubscriber;
	}

	public void setSubscriber(boolean isSubscriber) {
		this.isSubscriber = isSubscriber;
	}

	public InstitutionalCollectionSubscriptionService getInstitutionalCollectionSubscriptionService() {
		return institutionalCollectionSubscriptionService;
	}

	public void setInstitutionalCollectionSubscriptionService(
			InstitutionalCollectionSubscriptionService institutionalCollectionSubscriptionService) {
		this.institutionalCollectionSubscriptionService = institutionalCollectionSubscriptionService;
	}

	public Long getSubscribeUserId() {
		return subscribeUserId;
	}

	public void setSubscribeUserId(Long unsubscribeUserId) {
		this.subscribeUserId = unsubscribeUserId;
	}

	public InstitutionalCollection getCollection() {
		return collection;
	}

	public void setCollection(InstitutionalCollection collection) {
		this.collection = collection;
	}

}
