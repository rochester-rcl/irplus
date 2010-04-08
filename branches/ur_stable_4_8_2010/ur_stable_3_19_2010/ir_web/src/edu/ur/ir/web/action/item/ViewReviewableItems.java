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

import java.util.Collections;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.handle.UniqueHandleNameGenerator;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.ReviewableItem;
import edu.ur.ir.institution.ReviewableItemService;
import edu.ur.ir.institution.service.InstitutionalItemVersionUrlGenerator;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemObject;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.order.AscendingOrderComparator;
import org.apache.log4j.Logger;

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
	
	/** service for marking items that need to be indexed */
	private InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService;
	
	/** Institutional item service */
	private InstitutionalItemService institutionalItemService;

	/** index processing type service */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/** generates unique handle names */
	private UniqueHandleNameGenerator uniqueHandleNameGenerator;
	
	/** url generator for institutional items. */
	private InstitutionalItemVersionUrlGenerator institutionalItemVersionUrlGenerator;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(ViewReviewableItems.class);
	
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
	
	/**  Repository information data access  */
	private RepositoryService repositoryService;
	
	/** item within the reviewable item */
	private GenericItem item;



	/**
	 * View all pending items
	 */
	public String viewPendingItems() {
		log.debug("view pending items");
		IrUser user = userService.getUser(userId, false);
		
		items = reviewableItemService.getPendingReviewableItems(user);
		log.debug("number of reviewable items = " + items.size());
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
		item = reviewableItem.getItem();
		
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
		HandleInfo info = null;
		
		reviewableItem = reviewableItemService.getReviewableItem(reviewableItemId, false);
		InstitutionalItem institutionalItem = reviewableItemService.acceptItem(reviewableItem, user); 
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		// set the default license if one exists for the repository
		if( repository.getDefaultLicense() != null)
		{
		    institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().addRepositoryLicense(repository.getDefaultLicense(), user);
		}
		
		// save the item 
		institutionalItemService.saveInstitutionalItem(institutionalItem);
		
		// add a handle if the handle service is available
		HandleNameAuthority handleNameAuthority = repository.getDefaultHandleNameAuthority();
		if( handleNameAuthority != null)
		{
			String nextHandleName = uniqueHandleNameGenerator.nextName();
		    InstitutionalItemVersion itemVersion = institutionalItem.getVersionedInstitutionalItem().getCurrentVersion();
			
			String url = institutionalItemVersionUrlGenerator.createUrl(institutionalItem, itemVersion.getVersionNumber());
			info = new HandleInfo(nextHandleName, url, handleNameAuthority);
			
		    itemVersion.setHandleInfo(info);
		    
		    // save the item with the new handle information.
			institutionalItemService.saveInstitutionalItem(institutionalItem);
		}
		
		reviewableItemService.sendItemAcceptanceEmail(reviewableItem);

		IndexProcessingType processingType = indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE); 
		institutionalItemIndexProcessingRecordService.save(institutionalItem.getId(), processingType);
		
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

	public InstitutionalItemIndexProcessingRecordService getInstitutionalItemIndexProcessingRecordService() {
		return institutionalItemIndexProcessingRecordService;
	}

	public void setInstitutionalItemIndexProcessingRecordService(
			InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService) {
		this.institutionalItemIndexProcessingRecordService = institutionalItemIndexProcessingRecordService;
	}

	public IndexProcessingTypeService getIndexProcessingTypeService() {
		return indexProcessingTypeService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

	public void setUniqueHandleNameGenerator(
			UniqueHandleNameGenerator uniqueHandleNameGenerator) {
		this.uniqueHandleNameGenerator = uniqueHandleNameGenerator;
	}

	public void setInstitutionalItemVersionUrlGenerator(
			InstitutionalItemVersionUrlGenerator institutionalItemVersionUrlGenerator) {
		this.institutionalItemVersionUrlGenerator = institutionalItemVersionUrlGenerator;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}
	
	public GenericItem getItem() {
		return item;
	}

}
