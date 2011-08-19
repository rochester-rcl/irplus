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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSubscriptionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.institution.InstitutionalCollectionPictureHelper.PictureFileLocation;
import edu.ur.order.OrderType;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * Loads data for public view of institutional collection information
 * 
 * @author Nathan Sarr
 *
 */
public class PublicCollectionView extends ActionSupport implements UserIdAware {

	/**  Eclipse generated id */
	private static final long serialVersionUID = 7238213751595032189L;

	/**  Logger. */
	private static final Logger log = Logger.getLogger(PublicCollectionView.class);

	/** Repository service for dealing with institutional repository information */
	private RepositoryService repositoryService;
	
	/** Id of the collection */
	private InstitutionalCollection institutionalCollection;
	
	/** Id of the collection */
	private Long collectionId;
	
	/** set of institutional collections that are the path for the current 
	 * institutional collection
	 */
    private Collection <InstitutionalCollection> collectionPath;
    
	/** Institutional Collection service */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Service for dealing with institutional items */
	private InstitutionalItemService institutionalItemService;
	
	/** Download Statistics service */
	private DownloadStatisticsService downloadStatisticsService;
	
	/** Number of items within a collection and its sub-collection */
	private Long institutionalItemCount;
	
	/** Number of items within a collection */
	private Long institutionalItemsCountForACollection;
	
	/** Count of sub collections */
	private int subcollectionCount;
	
	/** Count of the subcollection and its children */
	private Long allSubcollectionCount;
	
	/** File download count for this collection */
	private Long fileDownloadCountForCollection; 

	/** File download count for this collection */
	private Long fileDownloadCountForCollectionAndItsChildren; 
	
	/**  List of most recent submissions to a collection */
	private List<InstitutionalItem> mostRecentSubmissions = new LinkedList<InstitutionalItem>();
	
	/** The institutional repository */
	private Repository repository;
	
	/** Used for sorting name based entities */
	private AscendingNameComparator nameComparator = new AscendingNameComparator();
	
	/** list of children in name order */
	private LinkedList<InstitutionalCollection> nameOrderedChildren = new LinkedList<InstitutionalCollection>();

	/** id of the user accessing the collection  */
	private Long userId;
	
	/** User service */
	private UserService userService;
	
	/** Service for dealing with institutional collection subscription service */
	private InstitutionalCollectionSubscriptionService institutionalCollectionSubscriptionService;

	/** Indicates whether the user has subscribed to this collection */
	private boolean isSubscriber;
	
	/**  Ir file that should be shown for picture. */
	private IrFile irPictureFile;
	
	/**  Current picture location */
	private int currentPictureLocation;

	/** number of collection pictures */
	private int numCollectionPictures = 0;
	

	/**
	 * Get the rss feed.
	 * 
	 * @return
	 */
	public String viewRss()
	{
		log.debug("veiwRss called");
		repository = 
			 repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, 
					 false);
		 
		if( collectionId != null )
		{
		institutionalCollection = 
			institutionalCollectionService.getCollection(collectionId, false);
		}
		if( institutionalCollection != null )
		{
		    // get the 10 most recent submissions
		    mostRecentSubmissions = institutionalItemService.getItemsOrderByDate(0, 50, institutionalCollection, OrderType.DESCENDING_ORDER);
		    collectionPath = institutionalCollectionService.getPath(institutionalCollection);
		    return "view";
		}
		return "notFound";
		
	}
	
	/**
	 * View the institutional collection.
	 * 
	 * @return
	 */
	public String view()
	{
		log.debug("view called");
		 repository = 
			 repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, 
					 false);
		 

		if( collectionId != null )
		{
		    institutionalCollection = 
			    institutionalCollectionService.getCollection(collectionId, false);
		}
		
		if( institutionalCollection != null )
		{
		    if(userId != null)
		    {
		    	IrUser user = userService.getUser(userId, false);
		    	isSubscriber = institutionalCollectionSubscriptionService.isSubscribed(institutionalCollection, user);
			}
			
			nameOrderedChildren.addAll(institutionalCollection.getChildren());
			Collections.sort(nameOrderedChildren, nameComparator);
		    // get the 10 most recent submissions
		    mostRecentSubmissions = institutionalItemService.getItemsOrderByDate(0, 5, institutionalCollection, OrderType.DESCENDING_ORDER);
		    collectionPath = institutionalCollectionService.getPath(institutionalCollection);
		    
			institutionalItemCount = institutionalCollectionService.getInstitutionalItemCountForCollectionAndChildren(institutionalCollection);
			institutionalItemsCountForACollection = institutionalCollectionService.getInstitutionalItemCountForCollection(institutionalCollection); 
			subcollectionCount = institutionalCollection.getChildCount();
			allSubcollectionCount =institutionalCollectionService.getTotalSubcollectionCount(institutionalCollection); 
			fileDownloadCountForCollection = downloadStatisticsService.getNumberOfDownloadsForCollection(institutionalCollection);
			fileDownloadCountForCollectionAndItsChildren = downloadStatisticsService.getNumberOfDownloadsForCollectionAndItsChildren(institutionalCollection);
		    
	        InstitutionalCollectionPictureHelper institutionalCollectionPictureHelper = new InstitutionalCollectionPictureHelper();
	        PictureFileLocation locationInfo = institutionalCollectionPictureHelper.nextPicture(institutionalCollection, 0, InstitutionalCollectionPictureHelper.INIT);
	        if( locationInfo != null )
	        {
	        	numCollectionPictures = locationInfo.getNumPictures();
	        	irPictureFile = locationInfo.getIrFile();
	        	currentPictureLocation = locationInfo.getCurrentLocation();
	        }
			
		    return "view";
		}
		return "notFound";
		
	}

	/**
	 * Repository service to access repository and institutional information.
	 * 
	 * @return
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	/**
	 * Set the repository service 
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * Get the institutional collection.
	 * 
	 * @return
	 */
	public InstitutionalCollection getInstitutionalCollection() {
		return institutionalCollection;
	}

	/**
	 * Set the institutional collection 
	 * 
	 * @param collection
	 */
	public void setInstitutionalCollection(
			InstitutionalCollection institutionalCollection) {
		this.institutionalCollection = institutionalCollection;
	}

	/**
	 * Collection id to load
	 * 
	 * @return
	 */
	public Long getCollectionId() {
		return collectionId;
	}

	/**
	 * Set the collection id.
	 * 
	 * @param collectionId
	 */
	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}
	
	/**
	 * The number of collection pictures.
	 * 
	 * @return
	 */
	public int getNumberOfCollectionPictures() {
		return institutionalCollection.getPictures().size();
	}
	
	/**
	 * Get the number of children.
	 * 
	 * @return the number of children.
	 */
	public int getNumberOfChildren()
	{
		return institutionalCollection.getChildren().size();
	}

	/**
	 * Path to this collection
	 * 
	 * @return
	 */
	public Collection<InstitutionalCollection> getCollectionPath() {
		return collectionPath;
	}

	/**
	 * Set the path to this collection.
	 * 
	 * @param collectionPath
	 */
	public void setCollectionPath(Collection<InstitutionalCollection> collectionPath) {
		this.collectionPath = collectionPath;
	}

	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}
	
	public Long getInstitutionalItemCount() {
		return institutionalItemCount;
	}


	public Long getInstitutionalItemsCountForACollection() {
		return institutionalItemsCountForACollection;
	}

	public int getSubcollectionCount() {
		return subcollectionCount;
	}

	public Long getAllSubcollectionCount() {
		return allSubcollectionCount;
	}

	public Long getFileDownloadCountForCollection() {
		return fileDownloadCountForCollection;
	}

	public Long getFileDownloadCountForCollectionAndItsChildren() {
		return fileDownloadCountForCollectionAndItsChildren;
	}

	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}

	public List<InstitutionalItem> getMostRecentSubmissions() {
		return mostRecentSubmissions;
	}

	public void setMostRecentSubmissions(
			List<InstitutionalItem> mostRecentSubmissions) {
		this.mostRecentSubmissions = mostRecentSubmissions;
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public Repository getRepository() {
		return repository;
	}

	public LinkedList<InstitutionalCollection> getNameOrderedChildren() {
		return nameOrderedChildren;
	}

	/**
	 * Set the user id if user exists
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getUserId() {
		return userId;
	}

	public boolean isSubscriber() {
		return isSubscriber;
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setInstitutionalCollectionSubscriptionService(
			InstitutionalCollectionSubscriptionService institutionalCollectionSubscriptionService) {
		this.institutionalCollectionSubscriptionService = institutionalCollectionSubscriptionService;
	}

	/**
	 * Picture for the institutional collection
	 * @return
	 */
	public IrFile getIrPictureFile() {
		return irPictureFile;
	}

	public int getNumCollectionPictures() {
		return numCollectionPictures;
	}
	
	public int getCurrentPictureLocation() {
		return currentPictureLocation;
	}
	
	public boolean getShowStats()
	{
		return 
		institutionalItemCount != 0 ||
		institutionalItemsCountForACollection != 0 ||
		subcollectionCount != 0 ||
		allSubcollectionCount != 0 ||
		fileDownloadCountForCollection != 0 ||
		fileDownloadCountForCollectionAndItsChildren != 0;
	}

}
