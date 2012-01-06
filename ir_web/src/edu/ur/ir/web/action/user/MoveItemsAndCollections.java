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


package edu.ur.ir.web.action.user;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.FileSystem;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Deals with moving items and collections to a specified location.
 * 
 * @author Nathan Sarr
 * 
 */
public class MoveItemsAndCollections extends ActionSupport implements UserIdAware {

	/** Eclipse generated id */
	private static final long serialVersionUID = 1406913832958674329L;

	/**  The user id  */
	private Long userId;
	
	/** set of collection ids to move */
	private Long[] collectionIds = {};
	
	/** set of item ids to move */
	private Long[] itemIds = {};
	
	/** collections to move */
	private List<PersonalCollection> collectionsToMove = new LinkedList<PersonalCollection>();
	
	/** items to move */
	private List<PersonalItem> itemsToMove = new LinkedList<PersonalItem> ();
	
	/** location to move the collections and items  */
	private Long destinationId = UserPublishingFileSystemService.ROOT_COLLECTION_ID;
	
	/** Items and collection service */
	private UserPublishingFileSystemService userPublishingFileSystemService;
	
	/** User service */
	private UserService userService;
	
	private IrUser user;
	
	/** path to the destination */
	private List<PersonalCollection> destinationPath;
	
	/** current contents of the destination collection */
	private List<FileSystem> currentDestinationContents = new LinkedList<FileSystem>();
	
	/** boolean to indicate that the action was successful */
	private boolean actionSuccess;
	
	/** current destination */
	private PersonalCollection destination;
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(MoveItemsAndCollections.class);

    /** current root location where all items are being moved from*/
    private Long parentCollectionId;
	
	/**
	 * Takes the user to view the locations that the collection can be moved to.
	 * 
	 * @return
	 */
	public String viewLocations()
	{
		log.debug("view move locations");
		user = userService.getUser(userId, false);

		List<Long> listCollectionIds = new LinkedList<Long>();
		for( Long id : collectionIds)
		{
		    listCollectionIds.add(id);
		}
		
		collectionsToMove = userPublishingFileSystemService.getPersonalCollections(userId, listCollectionIds);
		List<Long> listItemIds = new LinkedList<Long>();
		for( Long id : itemIds)
		{
			    listItemIds.add(id);
		}
		itemsToMove = userPublishingFileSystemService.getPersonalItems(userId, listItemIds);
		
		if( !destinationId.equals(UserPublishingFileSystemService.ROOT_COLLECTION_ID))
		{
		    destination = 
		    	userPublishingFileSystemService.getPersonalCollection(destinationId, false);
		    
		    if( !destination.getOwner().getId().equals(userId))
		    {
		    	return "accessDenied";
		    }
		    
		    // make sure the user has not navigated into a child or itself- this is illegal
		    for(PersonalCollection collection: collectionsToMove)
		    {
		    	if(destination.equals(collection))
		    	{
		    		throw new IllegalStateException("cannot move a collection into itself destination = " + destination
		    				+ " collection = " + collection);
		    	}
		    	else if( collection.getTreeRoot().equals(destination.getTreeRoot()) &&
		    			 destination.getLeftValue() > collection.getLeftValue() &&
		    			 destination.getRightValue() < collection.getRightValue() )
		    	{
		    		throw new IllegalStateException("cannot move a collection into a child destination = " + destination
		    				+ " collection = " + collection);
		    	}
		    }
		    
		    destinationPath = userPublishingFileSystemService.getPersonalCollectionPath(destination.getId());
		    currentDestinationContents.addAll(destination.getChildren());
		    currentDestinationContents.addAll(destination.getPersonalItems());
		}
		else
		{
			currentDestinationContents.addAll(user.getRootPersonalCollections());
			currentDestinationContents.addAll(user.getRootPersonalItems());
		}
		
		return SUCCESS;
	}

	/**
	 * Takes the user to view the locations that the collection can be moved to.
	 * 
	 * @return
	 */
	public String move()
	{
		log.debug("move called");
		user = userService.getUser(userId, false);

		List<FileSystem> notMoved;
		actionSuccess = true;

		List<Long> listCollectionIds = new LinkedList<Long>();
		for( Long id : collectionIds)
		{
		    listCollectionIds.add(id);
		}
		
		// this action is protected because user id is used in fetch
		collectionsToMove = userPublishingFileSystemService.getPersonalCollections(userId, listCollectionIds);
		
		List<Long> listItemIds = new LinkedList<Long>();
		for( Long id : itemIds)
		{
		    listItemIds.add(id);
		}
		
		// this action is protected because user id is used in fetch
		itemsToMove = userPublishingFileSystemService.getPersonalItems(userId, listItemIds);
		
		log.debug( "destination id = " + destinationId);
		if( !destinationId.equals(UserPublishingFileSystemService.ROOT_COLLECTION_ID))
		{
		    destination = 
		    	userPublishingFileSystemService.getPersonalCollection(destinationId, false);
		    if( !destination.getOwner().getId().equals(userId))
		    {
		    	return "accessDenied";
		    }
		    
		    
		    notMoved = 
				userPublishingFileSystemService.moveCollectionSystemInformation(destination, 
						collectionsToMove, itemsToMove);
					    
		}
		else
		{
			notMoved = userPublishingFileSystemService.moveCollectionSystemInformation(user, 
						collectionsToMove, itemsToMove);
		}
		
		if( notMoved.size() > 0 )
		{
			String message = getText("collectionNamesAlreadyExist");
			actionSuccess = false;
			StringBuffer sb = new StringBuffer(message);
			for(FileSystem fileSystem : notMoved)
			{
			    sb.append( " " + fileSystem.getName() );
			}
			addFieldError("moveError", sb.toString());
			//load the data
	        viewLocations();	
	        return ERROR;
		}
		
		
			
		
		return SUCCESS;
	}

	public void setCollectionIds(Long[] collectionIds) {
		this.collectionIds = collectionIds;
	}


	public void setItemIds(Long[] itemIds) {
		this.itemIds = itemIds;
	}


	public void setCollectionsToMove(List<PersonalCollection> collectionsToMove) {
		this.collectionsToMove = collectionsToMove;
	}


	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}

	public List<PersonalCollection> getDestinationPath() {
		return destinationPath;
	}


	public List<FileSystem> getCurrentDestinationContents() {
		return currentDestinationContents;
	}

	public boolean getActionSuccess() {
		return actionSuccess;
	}

	public Long getDestinationId() {
		return destinationId;
	}

	public PersonalCollection getDestination() {
		return destination;
	}

	public void setDestination(PersonalCollection destination) {
		this.destination = destination;
	}

	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	/**
	 * Allow a user id to be passed in.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}

	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	public void setUserPublishingFileSystemService(UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<PersonalItem> getItemsToMove() {
		return itemsToMove;
	}

	public void setItemsToMove(List<PersonalItem> itemsToMove) {
		this.itemsToMove = itemsToMove;
	}

	public List<PersonalCollection> getCollectionsToMove() {
		return collectionsToMove;
	}

	public IrUser getUser() {
		return user;
	}
}