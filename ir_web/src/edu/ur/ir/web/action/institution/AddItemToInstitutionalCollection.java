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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.ReviewableItem;
import edu.ur.ir.institution.ReviewableItemService;
import edu.ur.ir.institution.service.InstitutionalItemVersionUrlGenerator;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.handle.UniqueHandleNameGenerator;

/**
 * Action to submit items to institutional collection
 * 
 * @author Sharmila Ranganathan
 *
 */ 
public class AddItemToInstitutionalCollection extends ActionSupport implements  
	UserIdAware{

	/** eclipse generated id. */
	private static final long serialVersionUID = -7874283785994896401L;
	
	/**  Id of the user who has the collections  */
	private Long userId;
	
	/**  Repository information data access  */
	private RepositoryService repositoryService;
	
	/**  Repository information data access  */
	private UserService userService;
	
	/** A collection of personal collections and items for a user in a given location of
	    their personal directory.*/
	private Collection<InstitutionalCollectionSubmitPermission> collectionsPermission;
	
	/** set of personal collections that are the path for the current personal collection */
	private Collection <InstitutionalCollection> collectionPath;
	
	/** The collection that owns the listed items and personal collections */
	private Long parentCollectionId;
	
	/** The parent institutional collection id */
	private Long parentInstitutionalCollectionId = new Long(0);
	
	/** Institutional Collection service */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/**  Logger for view personal collections action */
	private static final Logger log = Logger.getLogger(AddItemToInstitutionalCollection.class);
	
	/** Id of the generic item being edited.  */
	private Long genericItemId;

	/** Generic Item being edited */
	private GenericItem item; 
	
	/**  Security service for institutional collections */
	private InstitutionalCollectionSecurityService institutionalCollectionSecurityService;

	/** Id of the institutional collection  */
	private Long institutionalCollectionId;
	
	/** Comma separated list of selected collection Ids */
	private String selectedCollectionIds;
	
	/** Collections selected to submit the publication */
	private Collection<InstitutionalCollectionSubmitPermission> selectedCollectionsPermission;

	/** Service for dealing with item . */
	private ItemService itemService;
	
	/** service for marking items that need to be indexed */
	private InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService;

	/** index processing type service */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/** Institutional item service */
	private InstitutionalItemService institutionalItemService;
	
	/** generates unique handle names */
	private UniqueHandleNameGenerator uniqueHandleNameGenerator;
	
	/** url generator for institutional items. */
	private InstitutionalItemVersionUrlGenerator institutionalItemVersionUrlGenerator;
	
	/** Institutional items created during the submission */
	private List<InstitutionalItem> institutionalItems = new LinkedList<InstitutionalItem>(); 
	
	/** list of reviewable items created during the submission */
	private List<ReviewableItem> reviewableItems = new LinkedList<ReviewableItem>();
	
	/** Service for dealing with reviewable items */
	private ReviewableItemService reviewableItemService;


	
	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Returns success.
	 * 
	 * @return
	 */
	public String execute()
	{
		log.debug("Generic ItemId = " + genericItemId + " user id = " + userId);

		IrUser user = null;
		if( userId != null )
		{
			user = userService.getUser(userId, false);
		}
		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false);
		}
		
        if(log.isDebugEnabled())
        {
		    log.debug(" owner id = " + item.getOwner().getId());
	        log.debug("user id = " + userId);
		    log.debug("User == null " + (user == null));
		    log.debug("!item.getOwner().getId().equals(userId) = " + (!item.getOwner().getId().equals(userId)));
		    log.debug("!user.hasRole(IrRole.ADMIN_ROLE) = " + !user.hasRole(IrRole.ADMIN_ROLE) );
		    log.debug("all together = " + (user == null || (!item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE) ) ) );
        }
        
		// make sure user has permissions to submit the publication
		if(user == null || (!item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE)) )
		{
			return "accessDenied";
		}
		
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);

		log.debug("checking user has license = " + user.getAcceptedLicense(repository.getDefaultLicense()));
		
		//make the user accept the license if the user does not have
		//the most current license.
		if( repository.getDefaultLicense() != null && user.getAcceptedLicense(repository.getDefaultLicense()) == null)
		{
			return "acceptLicense";
		}
		
		return SUCCESS;
	}
	
	/**
	 * Get the table of institutional collections
	 * 
	 * @return
	 */
	public String getCollectionsTable()
	{
		log.debug("getTableCalled");
		
		item = itemService.getGenericItem(genericItemId, false);
		
		IrUser user = null;
		if( userId != null )
		{
			user = userService.getUser(userId, false);
		}
		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false);
		}
		
		if(user == null || (!item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE)) )
		{
			return "accessDenied";
		}
		
		Collection<InstitutionalCollection> institutionalCollections;

		// don't hit the database unless we need to
		if(parentInstitutionalCollectionId != null && parentInstitutionalCollectionId > 0)
		{
			InstitutionalCollection parent = 
				institutionalCollectionService.getCollection(parentInstitutionalCollectionId, false);
		    collectionPath = institutionalCollectionService.getPath(parent);
		    institutionalCollections = parent.getChildren();
		}
		else
		{
			Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			institutionalCollections = repository.getInstitutionalCollections();
		}
				
		collectionsPermission = getSubmitPermissionForCollections(institutionalCollections, user, item);

		return SUCCESS;
	}

	/*
	 * Retrieves submit permission for the user on each collection
	 */
	private Collection<InstitutionalCollectionSubmitPermission> getSubmitPermissionForCollections(
			Collection<InstitutionalCollection> institutionalCollections, IrUser user, GenericItem item) {
		 
		Collection<InstitutionalCollectionSubmitPermission> collectionSubmitPermission 
			= new LinkedHashSet<InstitutionalCollectionSubmitPermission>();
		
		boolean hasReviewSubmit = false;
		boolean hasDirectSubmit = false;
		
		for(InstitutionalCollection collection: institutionalCollections) {

			log.debug("collection Name:"+collection.getName());
			
			// Check if the publication is already submitted to this collection
			if (institutionalCollectionService.isItemPublishedToCollection(collection.getId(), item.getId())) {
				InstitutionalCollectionSubmitPermission p 
					= new InstitutionalCollectionSubmitPermission(collection, "ALREADY_SUBMITTED" );
				collectionSubmitPermission.add(p);
			} // Check if the publication is  submitted to this collection and pending review 
			else if (collection.getReviewPendingItem(item) != null) {
				InstitutionalCollectionSubmitPermission p 
					= new InstitutionalCollectionSubmitPermission(collection, "REVIEW_PENDING" );
				collectionSubmitPermission.add(p);
			} // Check the submit  permission for this user for this collection
			else {
			
				
				hasReviewSubmit = false;
				hasDirectSubmit = false;

				if( institutionalCollectionSecurityService.isGranted(collection, user, InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION))
				{
					hasDirectSubmit = true;
				}
				
				if( institutionalCollectionSecurityService.isGranted(collection, user, InstitutionalCollectionSecurityService.REVIEW_SUBMIT_PERMISSION))
				{
					hasReviewSubmit = true;
				}
				
				
				if (hasDirectSubmit) {
					InstitutionalCollectionSubmitPermission p 
						= new InstitutionalCollectionSubmitPermission(collection, InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION.getPermission() );
					collectionSubmitPermission.add(p);
				} else if (hasReviewSubmit) {
					InstitutionalCollectionSubmitPermission p 
						= new InstitutionalCollectionSubmitPermission(collection, InstitutionalCollectionSecurityService.REVIEW_SUBMIT_PERMISSION.getPermission() );
					collectionSubmitPermission.add(p);
				} else {
					InstitutionalCollectionSubmitPermission p 
						= new InstitutionalCollectionSubmitPermission(collection, "NO_PERMISSION" );
					collectionSubmitPermission.add(p);
				}
				
			}
			
		}
		
		return collectionSubmitPermission;
		
	}
	
	/**
	 * Add institutional collection to selected list
	 * 
	 */
	public String addInstitutionalCollection() {
		
		item = itemService.getGenericItem(genericItemId, false);
		
		IrUser user = null;
		if( userId != null )
		{
			user = userService.getUser(userId, false);
		}
		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false);
		}
		
		if(user == null || (!item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE)) )
		{
			return "accessDenied";
		}
		
		log.debug("Institutional Collection Id: "+ institutionalCollectionId);

		// Add the collection id only if its not in the selected list 
		if (!(selectedCollectionIds.equals(Long.toString(institutionalCollectionId.longValue()))) 
				&& !(selectedCollectionIds.startsWith(Long.toString(institutionalCollectionId.longValue())+",")) 
				&& !(selectedCollectionIds.endsWith(","+Long.toString(institutionalCollectionId.longValue()))) 
				&& (selectedCollectionIds.indexOf(","+Long.toString(institutionalCollectionId.longValue())+",") == -1) ) {
			selectedCollectionIds = selectedCollectionIds.concat(",").concat(Long.toString(institutionalCollectionId.longValue()));
		}
		
		List<Long> collectionIds = getSelectedCollectionIdList();
		
		Collection<InstitutionalCollection> collections = institutionalCollectionService.getCollections(collectionIds);
		
		selectedCollectionsPermission = getSubmitPermissionForCollections(collections, user, item);
		
		return SUCCESS;
	}

	/**
	 * Submit publication to the institutional collection
	 * 
	 * @throws NoIndexFoundException 
	 * @throws CollectionDoesNotAcceptItemsException 
	 * 
	 */
	public String submitPublication() throws NoIndexFoundException, CollectionDoesNotAcceptItemsException {

		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		item = itemService.getGenericItem(genericItemId, false);

		
		IrUser user = null;
		if( userId != null )
		{
			user = userService.getUser(userId, false);
		}
		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false);
		}
		
		if(user == null || (!item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE)) )
		{
			return "accessDenied";
		}
		
		log.debug("Institutional Collection selectedCollectionIds: "+ selectedCollectionIds);
		
		List<Long> collectionIds = getSelectedCollectionIdList();
		List<InstitutionalCollection> privateCollections = new LinkedList<InstitutionalCollection>();

		boolean publicCollectionExist = false;
		boolean isItemPublished = item.isPublishedToSystem();
		
		Collection<InstitutionalCollection> collections = institutionalCollectionService.getCollections(collectionIds);
		
		// if no collections selected then we don't care
		if( collections.size() <= 0 )
		{
			return SUCCESS;
		}
		else
		{
			//user should have never made it this far but just in case
			//make the user accept the license if the user does not have
			//the most current license.
			if( repository.getDefaultLicense() != null && user.getAcceptedLicense(repository.getDefaultLicense()) == null)
			{
				return "acceptLicense";
			}
		}

		
		for(InstitutionalCollection institutionalCollection: collections) {
			if( !institutionalCollectionService.isItemPublishedToCollection(institutionalCollection.getId(), item.getId()))
			{   
				log.debug("Institutional Collection Id: "+ institutionalCollection.getId());
			
			    InstitutionalItem institutionalItem = null;
			    if( institutionalCollectionSecurityService.isGranted(institutionalCollection, user, InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION))
			    {
                    institutionalItem = new InstitutionalItem(institutionalCollection, item);
                   
				    if (institutionalCollection.isPubliclyViewable()) 
				    {
					    publicCollectionExist = true;
				    } 
				    else 
				    {
					    privateCollections.add(institutionalCollection);
				    }
				
				    // make the files public if this is the first submit
				    // and the item going into at least one public collection
				    if(publicCollectionExist && !isItemPublished )
				    {
					    for(ItemFile file:item.getItemFiles()) 
					    {
						    file.setPublic(true);
					    }
					    item.setPubliclyViewable(true);
				    }
				
				    // set the default license if one exists for the repository
				    if( repository.getDefaultLicense() != null)
				    {
				        institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().addRepositoryLicense(repository.getDefaultLicense(), user);
				    }
				    // generates the id and persists the item.
                    institutionalItemService.saveInstitutionalItem(institutionalItem);
                    
				    // add a handle if the handle service is available
				    HandleNameAuthority handleNameAuthority = repository.getDefaultHandleNameAuthority();
				    log.debug("handle name authority = " + handleNameAuthority);
				    if( handleNameAuthority != null)
				    {
					    this.addHandleInfo(handleNameAuthority, institutionalItem);
				    }
				    
				    // save the item 
				    institutionalItemService.saveInstitutionalItem(institutionalItem);
				    institutionalItems.add(institutionalItem);
				    
				    // only index if the item was added directly to the collection
				    IndexProcessingType processingType = indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE); 
				    institutionalItemIndexProcessingRecordService.save(item.getId(), processingType);
 			    }
			
			    else if(institutionalCollectionSecurityService.isGranted(institutionalCollection, user, InstitutionalCollectionSecurityService.REVIEW_SUBMIT_PERMISSION))
			    { 
				    ReviewableItem reviewableItem = institutionalCollection.addReviewableItem(item);
				    reviewableItems.add(reviewableItem);
				    reviewableItemService.saveReviewableItem(reviewableItem);
				    institutionalCollectionService.sendEmailToReviewer(institutionalCollection, item.getName());
			    }
			    else
			    {
				    return "accessDenied";
			    }
			
		    }
			/*
			 * Assign group permission only when the item is being submitted for the first time and
			 * If one of the collection(s) are private collection
			 */
			if (!publicCollectionExist && !isItemPublished && privateCollections.size() > 0) {
				institutionalItemService.setItemPrivatePermissions(item, privateCollections);
			}
		}
		
		

		return SUCCESS;
	}
	

	
	private void addHandleInfo(HandleNameAuthority handleNameAuthority, InstitutionalItem institutionalItem)
	{
		String nextHandleName = uniqueHandleNameGenerator.nextName();
	    InstitutionalItemVersion itemVersion = institutionalItem.getVersionedInstitutionalItem().getCurrentVersion();
		
		String url = institutionalItemVersionUrlGenerator.createUrl(institutionalItem, itemVersion.getVersionNumber());
		log.debug(" url = " + url);
		HandleInfo info = new HandleInfo(nextHandleName, url, handleNameAuthority);
		log.debug( " info = " + info);
	    itemVersion.setHandleInfo(info);
	}
	
	/**
	 * Removes the institutional collection from the selected collections list
	 * 
	 */
	public String removeInstitutionalCollection(){
		
		log.debug("Remove FileId = " + institutionalCollectionId);
		
		item = itemService.getGenericItem(genericItemId, false);
		
		IrUser user = null;
		if( userId != null )
		{
			user = userService.getUser(userId, false);
		}
		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false);
		}
		
		if(user == null || (!item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE)) )
		{
			return "accessDenied";
		}

		StringTokenizer tokenizer = null;
		List<Long> collectionIds = null; 
		StringBuffer buffer = new StringBuffer();
		
		// Remove the institutional collection id from the selected list
		if( selectedCollectionIds != null )
		{
			collectionIds = new LinkedList<Long>();
		    tokenizer = new StringTokenizer(selectedCollectionIds, ",");
		    while(tokenizer.hasMoreElements())
		    {
		    	Long collectionId = new Long(tokenizer.nextToken());
		    	if ((!collectionIds.contains(collectionId)) && (!collectionId.equals(institutionalCollectionId))) {
		    		buffer.append(collectionId);
		    		buffer.append(",");
		    		collectionIds.add(collectionId);
		    	}
			    
		    }
		}

		selectedCollectionIds = buffer.toString();
		
		Collection<InstitutionalCollection> collections = institutionalCollectionService.getCollections(collectionIds);
		
		selectedCollectionsPermission = getSubmitPermissionForCollections(collections, user, item);
		
		return SUCCESS;
	}
	

	/**
	 * Converts the String list of collection ids to List of Long collection ids
	 * 
	 * @return List of collection ids
	 */
	private List<Long> getSelectedCollectionIdList() {
		
		StringTokenizer tokenizer = null;
		List<Long> parsedCollectionIds = null; 
		
		if( selectedCollectionIds != null )
		{
			parsedCollectionIds = new LinkedList<Long>();
		    tokenizer = new StringTokenizer(selectedCollectionIds, ",");
		    while(tokenizer.hasMoreElements())
		    {
		    	Long collectionId = new Long(tokenizer.nextToken());
		    	if (!parsedCollectionIds.contains(collectionId)) {
		    		parsedCollectionIds.add(collectionId);
		    	}
			    
		    }
		}

		return parsedCollectionIds;
	}
		
	/**
	 * Get the parent collection id.
	 * 
	 * @return
	 */
	public Long getParentCollectionId() {
		return parentCollectionId;
	}
	
	/**
	 * Set the parent collection id.
	 * 
	 * @param parentCollectionId
	 */
	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}
	
	/**
	 * Get the set of collections that make up the path to this personal collection.
	 * 
	 * @return
	 */
	public Collection<InstitutionalCollection> getCollectionPath() {
		return collectionPath;
	}
	
	/**
	 * Get the user who owns the personal collections
	 * 
	 * @return
	 */
	public IrUser getUser() {
		return userService.getUser(userId,false);
	}
	
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}
	
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	public UserService getUserService() {
		return userService;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setCollectionPath(Collection<InstitutionalCollection> collectionPath) {
		this.collectionPath = collectionPath;
	}
	
	public Repository getRepository() {
		return repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
	}
	
	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}
	
	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	/**
	 * Simple class to help with the display of the 
	 * submit permissions the user have on the institutional collection.
	 * 
	 * @author Sharmila Ranganathan
	 *
	 */
	public class InstitutionalCollectionSubmitPermission
	{
		private InstitutionalCollection institutionalCollection;
		
		private String permission;
		
		public InstitutionalCollectionSubmitPermission(InstitutionalCollection institutionalCollection, String permission)
		{
			this.institutionalCollection = institutionalCollection;
			this.permission = permission;
		}

		public InstitutionalCollection getInstitutionalCollection() {
			return institutionalCollection;
		}

		public String getPermission() {
			return permission;
		}

	}

	public Collection<InstitutionalCollectionSubmitPermission> getCollectionsPermission() {
		return collectionsPermission;
	}

	public void setCollectionsPermission(
			Collection<InstitutionalCollectionSubmitPermission> collectionsPermission) {
		this.collectionsPermission = collectionsPermission;
	}

	public Long getInstitutionalCollectionId() {
		return institutionalCollectionId;
	}

	public void setInstitutionalCollectionId(Long institutionalCollectionId) {
		this.institutionalCollectionId = institutionalCollectionId;
	}


	public Collection<InstitutionalCollectionSubmitPermission> getSelectedCollectionsPermission() {
		return selectedCollectionsPermission;
	}

	public void setSelectedCollectionsPermission(
			Collection<InstitutionalCollectionSubmitPermission> selectedCollectionsPermission) {
		this.selectedCollectionsPermission = selectedCollectionsPermission;
	}

	public String getSelectedCollectionIds() {
		return selectedCollectionIds;
	}

	public void setSelectedCollectionIds(String selectedCollectionIds) {
		this.selectedCollectionIds = selectedCollectionIds;
	}

	public Long getGenericItemId() {
		return genericItemId;
	}

	public void setGenericItemId(Long genericItemId) {
		this.genericItemId = genericItemId;
	}

	public GenericItem getItem() {
		return item;
	}

	public void setItem(GenericItem item) {
		this.item = item;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
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

	public Long getParentInstitutionalCollectionId() {
		return parentInstitutionalCollectionId;
	}

	public void setParentInstitutionalCollectionId(
			Long parentInstitutionalCollectionId) {
		this.parentInstitutionalCollectionId = parentInstitutionalCollectionId;
	}
	
	public UniqueHandleNameGenerator getUniqueHandleNameGenerator() {
		return uniqueHandleNameGenerator;
	}

	public void setUniqueHandleNameGenerator(
			UniqueHandleNameGenerator uniqueHandleNameGenerator) {
		this.uniqueHandleNameGenerator = uniqueHandleNameGenerator;
	}
	
	public InstitutionalItemVersionUrlGenerator getInstitutionalItemVersionUrlGenerator() {
		return institutionalItemVersionUrlGenerator;
	}

	public void setInstitutionalItemVersionUrlGenerator(
			InstitutionalItemVersionUrlGenerator institutionalItemVersionUrlGenerator) {
		this.institutionalItemVersionUrlGenerator = institutionalItemVersionUrlGenerator;
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

	public List<InstitutionalItem> getInstitutionalItems() {
		return institutionalItems;
	}

	public List<ReviewableItem> getReviewableItems() {
		return reviewableItems;
	}
	
	public ReviewableItemService getReviewableItemService() {
		return reviewableItemService;
	}

	public void setReviewableItemService(ReviewableItemService reviewableItemService) {
		this.reviewableItemService = reviewableItemService;
	}


}
