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

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.DuplicateContributorException;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorService;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.user.PersonalWorkspaceSchedulingIndexHelper;

/**
 * Action to allow a user to add files to an item.
 * 
 * @author Nathan Sarr
 *
 */
public class AddContributorsToItem extends ActionSupport implements UserIdAware, Preparable {

	/**  Eclipse generated id */
	private static final long serialVersionUID = 309356905404014230L;
	
	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(AddContributorsToItem.class);
	
	/** Service for loading personal data.  */
	private UserService userService;
	
	/** Service for person.  */
	private PersonService personService;
		
	/**  Id for the user */
	private Long userId;
	
	/** Id of the item to add the files */
	private Long genericItemId;
	
	/** User logged in */
	private IrUser user;

	/** Service for loading contributor type data.  */
	private ContributorTypeService contributorTypeService;

	/** List of all Contributor Types.  */
	private List<ContributorType> contributorTypes;

	/** Number of contributors for the item */
	private int contributorsCount;
	
	/** List of Ids of contributors selected */
	private Long contributorTypeId;				

	/** List of contributors selected */
	private List<ItemContributor> contributors;
	
	/** Generic item being edited */
	private GenericItem item;
	
	/** Person name Id to be added/removed */
	private Long personNameId;
	
	/** Indicates whether the name is added */
	private boolean nameAdded;
	
	/** Message for adding names */
	private String nameMessage;
	
	/** Id of the contributor being edited*/
	private Long contributorId;
	
	/** Service for dealing with user file system. */
	private UserPublishingFileSystemService userPublishingFileSystemService;

	/** Parent collection id */
	private Long parentCollectionId;
	
	/** Service to deal with item */
	private ItemService itemService;
	
	/** Id of institutional item */
	private Long institutionalItemId;
	
	/** Service for dealing with contributors */
	private ContributorService contributorService;
	
	/** service for marking items that need to be indexed */
	private InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService;

	/** index processing type service */
	private IndexProcessingTypeService indexProcessingTypeService;

	/** Institutional item service */
	private InstitutionalItemService institutionalItemService;
	
	/** Quartz scheduler instance to schedule jobs  */
	private Scheduler quartzScheduler;


	/**
	 * Execute method
	 */
	public String execute() {
		
		if (userId != null) {
			user = userService.getUser(userId, false);
		}
		
		if( user == null || (!item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE)))
		{
		    return "accessDenied";
		}
		
		
		return SUCCESS;
	}

	/**
	 * Prepare for action
	 */
	public void prepare() {

		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false);
		}

	}
	
	/**
	 * Get personal files for the selected personal file ids
	 * 
	 */
	public String getItemContributors() {
		
		if (userId != null) {
			user = userService.getUser(userId, false);
		}
		
		if( user == null || (!item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE)))
		{
		    return "accessDenied";
		}
		
		log.debug("getting contriutors for item id = " + genericItemId);
		
		contributors  = item.getContributors();
		
		contributorsCount = contributors.size();
		
		contributorTypes = contributorTypeService.getAll();
		
		return SUCCESS;
	}
	
	/**
	 * Removes the file from the selected files list
	 * 
	 * 
	 */
	public String removeContributor() throws NoIndexFoundException {
		
		log.debug("Remove contributor " + contributorId + "from item:: item id = " + genericItemId);
		if (userId != null) {
			user = userService.getUser(userId, false);
		}
		
		if( user == null || (!item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE)))
		{
		    return "accessDenied";
		}
		
		ItemContributor removeContributor = item.getContributor(contributorId);

		item.removeContributor(removeContributor);

		itemService.makePersistent(item);
		
		// Update personal item index
		PersonalItem personalItem = userPublishingFileSystemService.getPersonalItem(item);

		// Check if personal item exist for this generic item - if not it means that user is editing the institutional item
		// in which case we don't have to update personal item index
		if (personalItem != null) {
			PersonalWorkspaceSchedulingIndexHelper schedulingHelper = new PersonalWorkspaceSchedulingIndexHelper();
			schedulingHelper.scheduleIndexingUpdate(quartzScheduler, personalItem);
		}
		
		List<InstitutionalItem> institutionalItems = institutionalItemService.getInstitutionalItemsByGenericItemId(genericItemId);

		if (institutionalItems != null) {
			IndexProcessingType processingType = indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE_NO_FILE_CHANGE); 

			for(InstitutionalItem i : institutionalItems) {
				institutionalItemIndexProcessingRecordService.save(i.getId(), processingType);
			}
		}

		return SUCCESS;
	}
	
	/**
	 * Adds the selected files to the item and saves the item
	 */
	public String addNameToItem() throws NoIndexFoundException {
		if (userId != null) {
			user = userService.getUser(userId, false);
		}
		
		if( user == null || (!item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE)))
		{
		    return "accessDenied";
		}
		
		PersonName personName = personService.getName(personNameId, false);
		ContributorType contributorType= contributorTypeService.getByUniqueSystemCode("AUTHOR");
		log.debug("Getting contributor for personName = " + personName + " contributor type = "  + contributorType);
		Contributor contributor = contributorService.get(personName, contributorType);
		
		if( contributor == null)
		{
			contributor = new Contributor();
			contributor.setPersonName(personName);
			contributor.setContributorType(contributorType);
		}
		
		try {
			item.addContributor(contributor);
		} catch (DuplicateContributorException e) {
			log.debug("item already contains contributor " + contributor);
		}

		itemService.makePersistent(item);
		
		nameAdded = true;
		
		// Update personal item index
		PersonalItem personalItem = userPublishingFileSystemService.getPersonalItem(item);

		// Check if personal item exist for this generic item - if not it means that user is editing the institutional item
		// in which case we don't have to update personal item index
		if (personalItem != null) {
			PersonalWorkspaceSchedulingIndexHelper schedulingHelper = new PersonalWorkspaceSchedulingIndexHelper();
			schedulingHelper.scheduleIndexingUpdate(quartzScheduler, personalItem);
		}		
		
		List<InstitutionalItem> institutionalItems = institutionalItemService.getInstitutionalItemsByGenericItemId(genericItemId);

		if (institutionalItems != null) {
			
			for(InstitutionalItem i : institutionalItems) {
				IndexProcessingType processingType = indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE); 
				institutionalItemIndexProcessingRecordService.save(i.getId(), processingType);
			}
		}

		
		return SUCCESS;
	}
	
	/**
	 * Adds the selected contributor type to the item and saves the item
	 */
	public String addContributorType() {
		if (userId != null) {
			user = userService.getUser(userId, false);
		}
		
		if( user == null || (!item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE)))
		{
		    return "accessDenied";
		}
		
		ContributorType contributorType= contributorTypeService.get(contributorTypeId, false);
		ItemContributor itemContributor = item.getContributor(contributorId);
		
		// only make the change if the user changed the contributor type
		if(!itemContributor.getContributor().getContributorType().equals(contributorType) )
		{
			PersonName personName = itemContributor.getContributor().getPersonName();
			Contributor contributor = contributorService.get(personName, contributorType);
			log.debug("Found contributor type " + contributor);
		    if( contributor == null)
		    {
		    	log.debug(" contributor type is null ");
		    	contributor = new Contributor();
				contributor.setPersonName(personName);
				contributor.setContributorType(contributorType);
		    }
		    int order = itemContributor.getOrder();
		    boolean removed = item.removeContributor(itemContributor);
		    log.debug("Removed contributor " + itemContributor.getContributor() + " boolean = " + removed);
		    ItemContributor newItemContributor = null;
		    try {
				newItemContributor = item.addContributor(contributor);
				item.moveContributor(newItemContributor, order);
			} catch (DuplicateContributorException e) {
				log.debug("item already contains contributor " + newItemContributor);
			}
			
			if( itemService.getItemCountByContributor(itemContributor.getContributor()) == 0)
			{
				contributorService.delete(itemContributor.getContributor());
			}
			itemService.makePersistent(item);
		}

		return SUCCESS;
	}

	/**
	 * Moves the contributor up for sorting
	 * 
	 */
	public String moveContributorUp() {
		if (userId != null) {
			user = userService.getUser(userId, false);
		}
		
		if( user == null || (!item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE)))
		{
		    return "accessDenied";
		}
		log.debug("Move up contributorId = " + contributorId);
		
		ItemContributor moveUpcontributor = item.getContributor(contributorId);
		item.moveContributor(moveUpcontributor, moveUpcontributor.getOrder() - 1 );
		itemService.makePersistent(item);
	
		
		return SUCCESS;
		
	}
	
	/**
	 * Moves the contributor down for sorting
	 * 
	 */
	public String moveContributorDown() {
		if (userId != null) {
			user = userService.getUser(userId, false);
		}
		
		if( user == null || (!item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE)))
		{
		    return "accessDenied";
		}
		log.debug("Move down contributorId = " + contributorId);
		
		ItemContributor moveDowncontributor = item.getContributor(contributorId);
		item.moveContributor(moveDowncontributor, moveDowncontributor.getOrder() + 1);
		itemService.makePersistent(item);
		return SUCCESS;
		
	}	

	
	/**
	 * Get the user service to perform the action
	 * 
	 * @return
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Set the user service to perform the action
	 * 
	 * @param userService User service
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Get the user id
	 * 
	 * @return Id of the user
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Get the user
	 * 
	 * @return user
	 */
	public IrUser getUser() {
		return user;
	}

	/**
	 * Set the user
	 * 
	 * @param user user
	 */
	public void setUser(IrUser user) {
		this.user = user;
	}

	public ContributorTypeService getContributorTypeService() {
		return contributorTypeService;
	}

	public void setContributorTypeService(
			ContributorTypeService contributorTypeService) {
		this.contributorTypeService = contributorTypeService;
	}

	public List<ContributorType> getContributorTypes() {
		return contributorTypes;
	}

	public void setContributorTypes(List<ContributorType> contributorTypes) {
		this.contributorTypes = contributorTypes;
	}

	public int getContributorsCount() {
		return contributorsCount;
	}

	public void setContributorsCount(int contributorsCount) {
		this.contributorsCount = contributorsCount;
	}

	public Long getContributorTypeId() {
		return contributorTypeId;
	}

	public void setContributorTypeId(Long contributorTypeId) {
		this.contributorTypeId = contributorTypeId;
	}

	public GenericItem getItem() {
		return item;
	}
	public void setItem(GenericItem item) {
		this.item = item;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public Long getPersonNameId() {
		return personNameId;
	}
	public void setPersonNameId(Long personNameId) {
		this.personNameId = personNameId;
	}

	public boolean isNameAdded() {
		return nameAdded;
	}
	public void setNameAdded(boolean nameAdded) {
		this.nameAdded = nameAdded;
	}
	public String getNameMessage() {
		return nameMessage;
	}
	public void setNameMessage(String nameMessage) {
		this.nameMessage = nameMessage;
	}
	public Long getContributorId() {
		return contributorId;
	}
	public void setContributorId(Long contributorId) {
		this.contributorId = contributorId;
	}

	public List<ItemContributor> getContributors() {
		return contributors;
	}

	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

	public Long getGenericItemId() {
		return genericItemId;
	}

	public void setGenericItemId(Long genericItemId) {
		this.genericItemId = genericItemId;
	}

	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public ContributorService getContributorService() {
		return contributorService;
	}

	public void setContributorService(ContributorService contributorService) {
		this.contributorService = contributorService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public Scheduler getQuartzScheduler() {
		return quartzScheduler;
	}

	public void setQuartzScheduler(Scheduler quartzScheduler) {
		this.quartzScheduler = quartzScheduler;
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


}
