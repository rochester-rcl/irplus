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

package edu.ur.ir.web.action.item;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexService;
import edu.ur.ir.institution.ReviewableItem;
import edu.ur.ir.institution.ReviewableItemService;
import edu.ur.ir.item.ItemObject;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.order.AscendingOrderComparator;

/**
 * Action to view items pending review
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ViewReviewableItems  extends ActionSupport implements UserIdAware {

	/** Eclipse generated id  */
	private static final long serialVersionUID = 3979004894627290097L;

	/** Service to access Reviewable item */
	private ReviewableItemService reviewableItemService;
	
	/** Service to access institutional item index */
	private InstitutionalItemIndexService institutionalItemIndexService;
	
	/** Service to access institutional item */
	private RepositoryService repositoryService;
	
	/** User id */
	private Long userId;

	/** User id*/
	private UserService userService;
	
	/** List of items pending review */
	private List<ReviewableItem> items;
	
	/** Reviewable item id */
	private Long reviewableItemId;
	
	private ReviewableItem reviewableItem;

	/** Item object sorted for display */
	private List<ItemObject> itemObjects;
	
	/** Reason for rejection */
	private String reason;

	/**
	 * View all pending items
	 */
	public String viewPendingItems() {
		
		IrUser user = userService.getUser(userId, false);
		
		items = reviewableItemService.getPendingReviewableItems(user);
		
		return SUCCESS;
	}
	
	/**
	 * View item preview
	 * 
	 * @return
	 */
	public String reviewItem() {
		
		reviewableItem = reviewableItemService.getReviewableItem(reviewableItemId, false);
		
		itemObjects = reviewableItem.getItem().getItemObjects();
		
		// Sort item objects by order
		Collections.sort(itemObjects,   new AscendingOrderComparator());
		
		return SUCCESS;
	}
	
	/**
	 * Accept item
	 * 
	 * @return
	 */
	public String accept() throws NoIndexFoundException {
		IrUser user = userService.getUser(userId, false);
		
		reviewableItem = reviewableItemService.getReviewableItem(reviewableItemId, false);
		InstitutionalItem institutionalItem = reviewableItemService.acceptItem(reviewableItem, user); 
		
		reviewableItemService.sendItemAcceptanceEmail(reviewableItem);

		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		String indexFolder = repository.getInstitutionalItemIndexFolder().getFullPath();
		institutionalItemIndexService.addItem(institutionalItem, new File(indexFolder));		
		
		return SUCCESS;
	}

	/**
	 * Reject item
	 * 
	 * @return
	 */
	public String reject()  {
		IrUser user = userService.getUser(userId, false);
		
		reviewableItem = reviewableItemService.getReviewableItem(reviewableItemId, false);
		reviewableItem.reject(user, reason); 
		
		reviewableItemService.saveReviewableItem(reviewableItem);
		reviewableItemService.sendItemRejectionEmail(reviewableItem);
		return SUCCESS;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public ReviewableItemService getReviewableItemService() {
		return reviewableItemService;
	}

	public void setReviewableItemService(ReviewableItemService reviewableItemService) {
		this.reviewableItemService = reviewableItemService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<ReviewableItem> getItems() {
		return items;
	}

	public Long getReviewableItemId() {
		return reviewableItemId;
	}

	public void setReviewableItemId(Long reviewableItemId) {
		this.reviewableItemId = reviewableItemId;
	}

	public ReviewableItem getReviewableItem() {
		return reviewableItem;
	}

	public List<ItemObject> getItemObjects() {
		return itemObjects;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void setInstitutionalItemIndexService(
			InstitutionalItemIndexService institutionalItemIndexService) {
		this.institutionalItemIndexService = institutionalItemIndexService;
	}

}
