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

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;

import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.ReviewableItem;
import edu.ur.ir.institution.ReviewableItemDAO;
import edu.ur.ir.institution.ReviewableItemService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryLicenseNotAcceptedException;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.user.IrUser;

/**
 * Service methods for reviewable item
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultReviewableItemService implements ReviewableItemService {
	

	/** eclipse generated id */
	private static final long serialVersionUID = 2933648309047077151L;

	/** Reviewable item Data access. */
	private ReviewableItemDAO reviewableItemDAO;

	/** Access control for collection permissions */
	private InstitutionalCollectionSecurityService institutionalCollectionSecurityService;
	
	/* Mail sender */
	private MailSender mailSender;
	
	/** Mail message for admin to verify the user affiliation */
	private SimpleMailMessage itemRejectedMessage;
	
	/** Mail message for user with pending affiliation approval*/
	private SimpleMailMessage itemAcceptedMessage;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultReviewableItemService.class);

	/** Service to save institutional item */
	private InstitutionalItemService institutionalItemService;
	
	/** service to deal with institutional collection information */
	private InstitutionalCollectionService institutionalCollectionService;
	

	/**
	 * Get all items pending for review for specified user
	 * 
	 * @see edu.ur.ir.institution.ReviewableItemService#getPendingReviewableItems(IrUser)
	 */
	public List<ReviewableItem> getPendingReviewableItems(IrUser user) {
		
		List<ReviewableItem> pendingItems = reviewableItemDAO.getAllPendingItems();
		
		List<ReviewableItem> itemsForReview = new LinkedList<ReviewableItem>();
		
		log.debug("Checking reviewer permissions for " + pendingItems.size() );
		for(ReviewableItem item: pendingItems) {
			long permission = institutionalCollectionSecurityService.hasPermission(item.getInstitutionalCollection(),
					user, InstitutionalCollectionSecurityService.REVIEWER_PERMISSION);
			
			if (permission > 0) {
				itemsForReview.add(item);
			}
		}
		
		return itemsForReview;
	}

	/**
	 * Send publication acceptance email
	 * 
	 * @see edu.ur.ir.institution.ReviewableItemService#sendItemAcceptanceEmail(ReviewableItem)
	 */
	public void sendItemAcceptanceEmail(ReviewableItem item) {

		SimpleMailMessage message = new SimpleMailMessage(itemAcceptedMessage);
		message.setTo(item.getItem().getOwner().getDefaultEmail().getEmail());
		String text = message.getText();
		text = StringUtils.replace(text, "%FIRSTNAME%", item.getItem().getOwner().getFirstName());
		text = StringUtils.replace(text, "%LASTNAME%", item.getItem().getOwner().getLastName());
		text = StringUtils.replace(text, "%ITEMNAME%", item.getItem().getName());
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}	

	/**
	 * Send publication rejection email
	 * 
	 * @see edu.ur.ir.user.UserService#sendItemRejectionEmail(ReviewableItem)
	 */
	public void sendItemRejectionEmail(ReviewableItem item) {

		SimpleMailMessage message = new SimpleMailMessage(itemRejectedMessage);
		message.setTo(item.getItem().getOwner().getDefaultEmail().getEmail());
		String text = message.getText();
		text = StringUtils.replace(text, "%FIRSTNAME%", item.getItem().getOwner().getFirstName());
		text = StringUtils.replace(text, "%LASTNAME%", item.getItem().getOwner().getLastName());
		text = StringUtils.replace(text, "%ITEMNAME%", item.getItem().getName());
		text = StringUtils.replace(text, "%REASON%", item.getReasonForRejection());
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Get the reviewable item.
	 * 
	 * @see edu.ur.ir.institution.ReviewableItemService#getInstitutionalItem(java.lang.Long, boolean)
	 */
	public ReviewableItem getReviewableItem(Long id, boolean lock) {
		return reviewableItemDAO.getById(id, lock);
	}
	
	public ReviewableItemDAO getReviewableItemDAO() {
		return reviewableItemDAO;
	}

	public void setReviewableItemDAO(ReviewableItemDAO reviewableItemDAO) {
		this.reviewableItemDAO = reviewableItemDAO;
	}

	/**
	 * Save reviewable Item
	 * 
	 * @param reviewableItem
	 */
	public void saveReviewableItem(ReviewableItem reviewableItem) {
		reviewableItemDAO.makePersistent(reviewableItem);
	}


	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}


	public void setItemRejectedMessage(SimpleMailMessage itemRejectedMessage) {
		this.itemRejectedMessage = itemRejectedMessage;
	}


	public void setItemAcceptedMessage(SimpleMailMessage itemAcceptedMessage) {
		this.itemAcceptedMessage = itemAcceptedMessage;
	}

	/**
	 * Accept item and publish to collection
	 * 
	 * @param reviewableItem
	 * @param reviewer
	 * @throws CollectionDoesNotAcceptItemsException 
	 */
	public InstitutionalItem acceptItem(ReviewableItem reviewableItem, IrUser reviewer) throws CollectionDoesNotAcceptItemsException {
		reviewableItem.accept(reviewer); 
		
		saveReviewableItem(reviewableItem);
		
		InstitutionalItem institutionalItem = new InstitutionalItem(reviewableItem.getInstitutionalCollection(), reviewableItem.getItem());
		
		institutionalItemService.saveInstitutionalItem(institutionalItem);
		
		/**
		 * If item is not yet published and the collection being published to is private
		 * then set item as private and assign the collection's user group permission to item
		 */
		if (!reviewableItem.getItem().isPublishedToSystem() && !reviewableItem.getInstitutionalCollection().isPubliclyViewable()) {
			List<InstitutionalCollection> collections = new LinkedList<InstitutionalCollection>();
			collections.add(reviewableItem.getInstitutionalCollection());
			institutionalItemService.setItemPrivatePermissions(reviewableItem.getItem(), collections);
		}
		
		return institutionalItem;

	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public InstitutionalCollectionSecurityService getInstitutionalCollectionSecurityService() {
		return institutionalCollectionSecurityService;
	}

	public void setInstitutionalCollectionSecurityService(
			InstitutionalCollectionSecurityService institutionalCollectionSecurityService) {
		this.institutionalCollectionSecurityService = institutionalCollectionSecurityService;
	}
	
	/**
	 * Get review history for item
	 * 
	 * @param item
	 * @return
	 */
	public List<ReviewableItem> getReviewHistoryForItem(GenericItem item) {
		return reviewableItemDAO.getReviewHistoryByItem(item.getId());
	}
	
	/**
	 * Delete review history for item
	 * 
	 * @param item 
	 * @return
	 */
	public void deleteReviewHistoryForItem(GenericItem item) {
		List<ReviewableItem> reviewHistory = getReviewHistoryForItem(item);
		
		for(ReviewableItem i : reviewHistory) {
			reviewableItemDAO.makeTransient(i);
		}
	}
	
	/**
	 * Add the reviewable item to the specified collection.
	 * 
	 * @throws CollectionDoesNotAcceptItemsException 
	 * @throws RepositoryLicenseNotAcceptedException 
	 * @throws PermissionNotGrantedException 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#addReviewableItemToCollection(edu.ur.ir.user.IrUser, edu.ur.ir.item.GenericItem, edu.ur.ir.institution.InstitutionalCollection)
	 */
	public ReviewableItem addReviewableItemToCollection(IrUser user,
			GenericItem item, InstitutionalCollection institutionalCollection) throws CollectionDoesNotAcceptItemsException, RepositoryLicenseNotAcceptedException, PermissionNotGrantedException {
		ReviewableItem reviewableItem = null;
		if( !institutionalCollectionService.isItemPublishedToCollection(institutionalCollection.getId(), item.getId()))
		{   
		    if(institutionalCollectionSecurityService.isGranted(institutionalCollection, user, InstitutionalCollectionSecurityService.REVIEW_SUBMIT_PERMISSION))
		    { 
		    	// set the default license if one exists for the repository
                Repository repository = institutionalCollection.getRepository();
                if( repository.getDefaultLicense() != null && user.getAcceptedLicense(repository.getDefaultLicense()) == null)
    			{
    				throw new RepositoryLicenseNotAcceptedException(user, repository);
    			}
			    reviewableItem = institutionalCollection.addReviewableItem(item);
			    
			    saveReviewableItem(reviewableItem);
			    institutionalCollectionService.sendEmailToReviewer(institutionalCollection, item.getName());
		    }
		    else
		    {
		    	throw new PermissionNotGrantedException(InstitutionalCollectionSecurityService.REVIEW_SUBMIT_PERMISSION.getPermission());
		    }
	    }
		return reviewableItem;
	}
	
	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

}
