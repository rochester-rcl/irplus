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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * This action allows a personal collection to be added to a given user.
 * If the passed in collection id is null or 0, the collection is assumed to 
 * be a root collection.
 * 
 * @author Nathan Sarr
 *
 */
public class AddPersonalCollection extends ActionSupport implements UserIdAware{
	
	/** Serial id for a personal collection  */
	private static final long serialVersionUID = -4571784880362342893L;

	/**  Collection data access  */
	private UserService userService;
	
	/** the name of the collection to add */
	private String collectionName;
	
	/**  Description of the collection */
	private String collectionDescription;
	
	/** Current parent collection the user is looking at  */
	private Long parentCollectionId = 0L;
	
	/** id used when updating a collection */
	private Long updateCollectionId;
	
	/**  Logger for add personal collection action */
	private static final Logger log = LogManager.getLogger(AddPersonalCollection.class);
	
	/**  User object */
	private Long userId;

	/** Message that can be displayed to the user. */
	private String collectionMessage;
	
	/** Indicates if the collection has been added or not */
	private boolean collectionAdded = false;
	
	/** Service for dealing with user file system. */
	private UserPublishingFileSystemService userPublishingFileSystemService;
	
	/** process for setting up personal workspace information to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;

	/** service for accessing index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;

	
	/**
	 * Create the new collection
	 */
	public String add() throws Exception
	{
		log.debug("creating a personal collection parent collectionId = " +
				parentCollectionId);
		collectionAdded = false;
		IrUser thisUser = userService.getUser(userId, true);
		
		PersonalCollection personalCollection = null;
		
		// user must be an author to use this aspect
		if( !thisUser.hasRole(IrRole.AUTHOR_ROLE))
		{
			return "accessDenied";
		}
		
		// assume that if the current collection id is null or equal to 0
		// then we are adding a root collection to the user.
		if(parentCollectionId == null || parentCollectionId == 0)
		{
			 if( thisUser.getRootPersonalCollection(collectionName) == null )
		     {
				 try 
				 {
					 personalCollection = 
						 thisUser.createRootPersonalCollection(collectionName);
			         personalCollection.setDescription(collectionDescription);
					 userService.makeUserPersistent(thisUser);
					 
					 collectionAdded = true;
				 }
				 catch(DuplicateNameException e)
				 {
					 log.debug("Duplicate name execption ", e);
				 }
	         }
		}
		else
		{
			
			PersonalCollection collection = userPublishingFileSystemService.getPersonalCollection(parentCollectionId, 
					true);
			
			if( !collection.getOwner().getId().equals(userId))
			{
				return "accessDenied";
			}
			if( collection.getChild(collectionName) == null)
			{
				try 
				{
					personalCollection = 
						collection.createChild(collectionName);
					personalCollection.setDescription(collectionDescription);
					userPublishingFileSystemService.makePersonalCollectionPersistent(collection);
					collectionAdded = true;
				} catch(DuplicateNameException e) {
					log.debug("Duplicate name execption ", e);
				}
			}
		}
		
		if( !collectionAdded)
		{
			collectionMessage = getText("personalCollectionAlreadyExists", 
					new String[]{collectionName});
			addFieldError("personalCollectionAlreadyExists", collectionMessage);
		}
		else if( personalCollection != null)
		{
			userWorkspaceIndexProcessingRecordService.save(personalCollection.getOwner().getId(), personalCollection, 
	    			indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
		}
        return "added";
	}
	
	/**
	 * Update the collection with the collection information.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String update()
	{
		log.debug("updating a personal collection collectionId = " + parentCollectionId);
		collectionAdded = false;
		
		PersonalCollection other = null;
		
		if( parentCollectionId == null || parentCollectionId == 0L)
		{
			other = userPublishingFileSystemService.getRootPersonalCollection(collectionName, userId);
		}
		else
		{
			other = userPublishingFileSystemService.getPersonalCollection(collectionName, parentCollectionId);
		}
		
		// make sure name does not already exist
		if( other == null)
		{
			PersonalCollection  existingCollection = 
				userPublishingFileSystemService.getPersonalCollection(updateCollectionId, true);
			
			if( !existingCollection.getOwner().getId().equals(userId))
			{
				return "accessDenied";
			}
			
			try {
				existingCollection.reName(collectionName);
				existingCollection.setDescription(collectionDescription);
				userPublishingFileSystemService.makePersonalCollectionPersistent(existingCollection);
				userWorkspaceIndexProcessingRecordService.save(existingCollection.getOwner().getId(), existingCollection, 
		    			indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
				collectionAdded = true;
			} catch (DuplicateNameException e) {
				collectionAdded = false;
			}
			
		}
		// updating existing with the same name - description change
		else if(other.getId().equals(updateCollectionId))
		{
			if( !other.getOwner().getId().equals(userId))
			{
				return "accessDenied";
			}
			//it was found by name so we don't need to add it.
			other.setDescription(collectionDescription);
			userPublishingFileSystemService.makePersonalCollectionPersistent(other);
			userWorkspaceIndexProcessingRecordService.save(other.getOwner().getId(), other, 
	    			indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
			collectionAdded = true;
		}

		if( !collectionAdded)
		{
			collectionMessage = getText("personalCollectionAlreadyExists", new String[]{collectionName});
			addFieldError("personalCollectionAlreadyExists", collectionMessage);
		}

        return "added";
		
	}

	/**
	 * Loads the collection
	 * 
	 * @return
	 */
	public String get()
	{
		log.debug("get called");
	
		PersonalCollection collection = userPublishingFileSystemService.getPersonalCollection(updateCollectionId, true);
		if( !collection.getOwner().getId().equals(userId))
		{
			return "accessDenied";
		}
		collectionName= collection.getName();
		collectionDescription = collection.getDescription();
		
	    return "get";
	}
	
	
	/**
	 * The user service for dealing with actions.
	 * 
	 * @return
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * The user service for dealing with actions.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Get the user for this collection
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Set the user for this user.
	 * 
	 * @see edu.ur.ir.web.action.UserAware#setOwner(edu.ur.ir.user.IrUser)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Get the name of the collection to add.
	 * 
	 * @return
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * Set the name of the collection to add.
	 * 
	 * @param collectionName
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	/**
	 * Current collection the user is looking at.
	 * 
	 * @return
	 */
	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	/**
	 * The current collection the user is looking at.
	 * 
	 * @param currentCollectionId
	 */
	public void setParentCollectionId(Long currentCollectionId) {
		this.parentCollectionId = currentCollectionId;
	}

	/**
	 * Get the collection added message.
	 * 
	 * @return
	 */
	public String getCollectionMessage() {
		return collectionMessage;
	}

	/**
	 * Determine if the collection was added.
	 * 
	 * @return
	 */
	public boolean getCollectionAdded() {
		return collectionAdded;
	}


	public void setCollectionAdded(boolean collectionAdded) {
		this.collectionAdded = collectionAdded;
	}


	public String getCollectionDescription() {
		return collectionDescription;
	}


	public void setCollectionDescription(String collectionDescription) {
		this.collectionDescription = collectionDescription;
	}

	public Long getUpdateCollectionId() {
		return updateCollectionId;
	}

	public void setUpdateCollectionId(Long updateCollectionId) {
		this.updateCollectionId = updateCollectionId;
	}

	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	/**
	 * Set the user publishing file system service.
	 * 
	 * @param userPublishingFileSystemService
	 */
	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

	/**
	 * Service for dealing with workspace indexing.
	 * 
	 * @param userWorkspaceIndexProcessingRecordService
	 */
	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}
	
	/**
	 * Service to help with dealing with index processing types.
	 * 
	 * @param indexProcessingTypeService
	 */
	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

}
