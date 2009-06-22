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

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedList;
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
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeService;
import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.ExtentTypeService;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.FirstAvailableDate;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeService;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemObject;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeService;
import edu.ur.ir.item.OriginalItemCreationDate;
import edu.ur.ir.item.PublishedDate;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherService;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.SeriesService;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.user.PersonalWorkspaceSchedulingIndexHelper;
import edu.ur.order.AscendingOrderComparator;


/**
 * Action to add item metadata
 *  
 * @author Sharmila Ranganathan
 *
 */
public class AddItemMetadata extends ActionSupport implements Preparable, UserIdAware {

	/**
	 * Eclipse generated id
	 */
	private static final long serialVersionUID = 1847379201528051595L;
	
	/** sue making the change */
	private Long userId;
	
	/** service for dealing with users */
	private UserService userService;

	/**  Logger for add metadata to item action */
	private static final Logger log = Logger.getLogger(AddItemMetadata.class);
	
	/** Service for loading content type data.  */
	private ContentTypeService contentTypeService;
	
	/** Service for loading language type data.  */
	private LanguageTypeService languageTypeService;

	/** Service for loading identifier type data.  */
	private IdentifierTypeService identifierTypeService;
	
	/** Service for loading extent type data.  */
	private ExtentTypeService extentTypeService;

	/** Service for loading publisher data.  */
	private PublisherService publisherService;

	/** Service for loading sponsor data.  */
	private SponsorService sponsorService;
	
	/** Service for loading series data.  */
	private SeriesService seriesService;

	/** Service for item.  */
	private ItemService itemService;
	
	/** List of all Content types .  */
	private List<ContentType> contentTypes;
	
	/** List of all Series.  */
	private List<Series> seriesList;

	/** List of all identifier types  */
	private List<IdentifierType> identifierTypes;

	/** List of all extent types  */
	private List<ExtentType> extentTypes;
	
	/** List of all language types  */
	private List<LanguageType> languages;
	
	/** List of all sponsors   */
	private List<Sponsor> sponsors;
	
	/** List of all publishers  */
	private List<Publisher> publishers;
	
	/**  Generic Item being edited */
	private GenericItem item;
	
	/** Abstract for the item */
	private String itemAbstract;
	
	/** Keywords for the item */
	private String keywords;
	
	/** Id of the content type selected */
	private Long contentTypeId;
	
	/** Id of the language type selected */
	private Long languageTypeId;
	
	/** Id of the sponsor selected */
	private Long sponsorId;
	
	/** List of series ids selected */
	private Long[] seriesIds;
	
	/** List od report number for the item */
	private String[] reportNumbers;

	/** List sub titles for the item */
	private String[] subTitles;

	/** Number of reports for the item */
	private int reportsCount;
	
	/** List of identifier Ids selected */
	private Long[] identifierIds;

	/** List of extent Ids selected */
	private Long[] extentIds;
	
	/** List of sponsor Ids selected */
	private Long[] sponsorIds;
	
	/** List of content type Ids selected */
	private Long[] typeIds;
	
	/** List of identifier values entered for the item */
	private String[] identifierValues;

	/** List of extent values entered for the item */
	private String[] extentValues;

	/** List of sponsor description entered for the item */
	private String[] sponsorDescriptions;
	
	/** Number of identifiers for the item */
	private int itemIdentifiersCount;

	/** Number of extents for the item */
	private int itemExtentsCount;

	/** Number of sponsors for the item */
	private int itemSponsorsCount;
	
	/** List of Ids  of persona name selected */
	private Long[] personNameIds;
	
	/** Information about the external published item */
	private ExternalPublishedItem externalPublishedItem;
	
	/** Indicates whether the item is externally published or not */
	private Boolean isExternallyPublished = false;
	
	/** Item name */
	private String itemName;
	
	/** Description fot the item */
	private String itemDescription;
	
	/** Service for dealing with user file system. */
	private UserPublishingFileSystemService userPublishingFileSystemService;
	
	/** Item object sorted for display */
	private List<ItemObject> itemObjects;
	
	/** Number of subtitles */
	private int subTitlesCount;
	
	/** Month the publication was first available */
	private int firstAvailableMonth;
	
	/** Day the publication was first available */
	private int firstAvailableDay;
	
	/** Year the publication was first available */
	private int firstAvailableYear;

	/** Month the publication was released */
	private int releaseMonth;
	
	/** Day the publication was released */
	private int releaseDay;
	
	/** Year the publication was released */
	private int releaseYear;

	/** Month the publication was originally created */
	private int createdMonth;
	
	/** Day the publication was originally created */
	private int createdDay;
	
	/** Year the publication was originally created */
	private int createdYear;

	/** Month the publication was released */
	private int publishedMonth;
	
	/** Day the publication was released */
	private int publishedDay;
	
	/** Year the publication was released */
	private int publishedYear;
	
	/** Citation for the external published item */
	private String citation;
	
	/** Publisher Id for the external published item */
	private Long publisherId;

	/** Primary file id  */
	private Long primaryFileId;
	
	/** List of files that has thumbnail */
	private List<ItemFile> thumbnailFiles = new LinkedList<ItemFile>();
	
	/** Generic item id */
	private Long genericItemId;

	/** Reviewable item id */
	private Long reviewableItemId;
	
	/** Parent collection id */
	private Long parentCollectionId;
	
	/** Id of institutional item being edited */
	private Long institutionalItemId;
	
	/** Indicates whether the publication is a thesis */
	private boolean thesis;
	
	/** service for marking items that need to be indexed */
	private InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService;

	/** index processing type service */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/** Institutional item service */
	private InstitutionalItemService institutionalItemService;
	
	/** Quartz scheduler instance to schedule jobs  */
	private Scheduler quartzScheduler;

	
	/**
	 * Prepare for action
	 */
	public void prepare(){
		log.debug("Generic ItemId = " + genericItemId);

		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false); 
		} 
		
	}
	
	/**
	 * Action to view the item metadata page
	 * 
	 * @return
	 */
	public String viewItemMetadata() {
		log.debug("view item metadata item id =  " + item.getId() + " user id = " + userId + " owner id = " + item.getOwner().getId());
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
			return "accessDenied";
		}

		contentTypes = contentTypeService.getAllContentTypeByNameOrder();
		
		seriesList = seriesService.getAllSeries();

		identifierTypes = identifierTypeService.getAll();

		extentTypes = extentTypeService.getAllExtentTypes();

		languages = languageTypeService.getAll();
		
		sponsors = sponsorService.getAllSponsor();
		
		publishers = publisherService.getAllPublisher();

		itemObjects = item.getItemObjects();
		
		if (item.getReleaseDate() != null) {
			GregorianCalendar releaseDate = new GregorianCalendar();
			releaseDate.setTime(item.getReleaseDate());
	
			releaseDay = releaseDate.get(Calendar.DAY_OF_MONTH);
			releaseMonth = releaseDate.get(Calendar.MONTH) + 1;
			releaseYear = releaseDate.get(Calendar.YEAR);
		}
		
		for (ItemFile itemFile: item.getItemFiles()) {
			if (itemFile.getIrFile().getTransformedFileBySystemCode("PRIMARY_THUMBNAIL") != null ) {
				thumbnailFiles.add(itemFile);
			}
		}
		
		// Sort item objects by order
		Collections.sort(itemObjects,   new AscendingOrderComparator());
		reportsCount = item.getItemReports().size();
		itemIdentifiersCount = item.getItemIdentifiers().size();
		itemExtentsCount = item.getItemExtents().size();
		itemSponsorsCount = item.getItemSponsors().size();
		subTitlesCount = item.getSubTitles().size();
		externalPublishedItem = item.getExternalPublishedItem();
		
		if (externalPublishedItem != null) {
			isExternallyPublished = true;
		}
		
		return SUCCESS;
	}

	/**
	 * Action to update the series
	 * 
	 * @return
	 */
	public String getSeriesInformation() {
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
			return "accessDenied";
		}

		
		reportsCount = item.getItemReports().size();
		
		seriesList = seriesService.getAllSeries();
		
		return SUCCESS;
	}

	/**
	 * Action to update the identifier
	 * 
	 * @return
	 */
	public String getIdentifierInformation() 
	{
	
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
			return "accessDenied";
		}

		itemIdentifiersCount = item.getItemIdentifiers().size();
		
		identifierTypes = identifierTypeService.getAll();
		
		return SUCCESS;
	}

	/**
	 * Action to update the extent
	 * 
	 * @return
	 */
	public String getExtentInformation() 
	{
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
			return "accessDenied";
		}
	
		itemExtentsCount = item.getItemExtents().size();
		
		extentTypes = extentTypeService.getAllExtentTypes();
		
		return SUCCESS;
	}
	
	/**
	 * Action to update the sponsor
	 * 
	 * @return
	 */
	public String getSponsorInformation() 
	{
	
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
			return "accessDenied";
		}

		itemSponsorsCount = item.getItemSponsors().size();
		
		sponsors = sponsorService.getAllSponsor();
		
		return SUCCESS;
	}

	/**
	 * Action to update the publisher
	 * 
	 * @return
	 */
	public String getPublisherInformation() 
	{
		publishers = publisherService.getAllPublisher();
		return SUCCESS;
	}
	
	public String saveContentType() 
	{
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
			return "accessDenied";
		}

		item.setPrimaryContentType(contentTypeService.getContentType(contentTypeId, false));
		item.removeAllSecondaryContentTypes();
		itemService.makePersistent(item);

		// Add secondary types
		if (typeIds != null) {
			for (int i =0; i < typeIds.length; i++ ) {
				if (typeIds[i] != null) {
					ContentType contentType = contentTypeService.getContentType(typeIds[i], false);
					item.addSecondaryContentType(contentType);
				}
			}
		}
		
		itemService.makePersistent(item);
		contentTypes = contentTypeService.getAllContentTypeByNameOrder();
		return SUCCESS;
	}

	/**
	 * Saves the metadata for the publication 
	 * 
	 * @return
	 */
	public String saveItemMetadata() throws NoIndexFoundException 
	{
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().getId().equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
			return "accessDenied";
		}

		// Sets the item metadata
		item.setName(itemName);
		item.setDescription(itemDescription);
		item.setItemAbstract(itemAbstract);
		item.setItemKeywords(keywords);
		item.setThesis(thesis);

		if (releaseDay != 0 && releaseMonth != 0 && releaseYear != 0) {
			GregorianCalendar c = new GregorianCalendar(releaseYear, releaseMonth - 1, releaseDay);
			item.setReleaseDate(c.getTime());
		} else {
			item.setReleaseDate(null);
		}
		
		OriginalItemCreationDate cd = item.getOriginalItemCreationDate();
		if (cd == null) {
			item.addOriginalItemCreationDate(createdMonth, createdDay, createdYear);
		} else {
			cd.setDay(createdDay);
			cd.setMonth(createdMonth);
			cd.setYear(createdYear);
		}
		
		FirstAvailableDate fd = item.getFirstAvailableDate();
		if (fd == null) {
			item.addFirstAvailableDate(firstAvailableMonth, firstAvailableDay, firstAvailableYear);
		} else {
			fd.setDay(firstAvailableDay);
			fd.setMonth(firstAvailableMonth);
			fd.setYear(firstAvailableYear);
		}
		
		item.setPrimaryContentType(contentTypeService.getContentType(contentTypeId, false));
		item.setLanguageType(languageTypeService.get(languageTypeId, false));
		
		// Removes all existing reports and identifiers
		item.removeAllItemReports();
		item.removeAllItemIdentifiers();
		item.removeAllItemSubTitles();
		item.removeAllItemExtents();
		item.removeAllItemSponsors();
		item.removeAllSecondaryContentTypes();

		ExternalPublishedItem oldPublishedData = null;
		
		if (!isExternallyPublished) {
			oldPublishedData = item.getExternalPublishedItem();
			item.setExternalPublishedItem(null);
		}
		
		if (primaryFileId != null && !primaryFileId.equals(0l)) {
			item.addPrimaryImageFile(item.getItemFile(primaryFileId));
		}
		else if( item.getPrimaryImageFile() != null )
		{
			item.removePrimaryImageFile();
		}
			
		
		itemService.makePersistent(item);

		// Adds the selected report numbers
		if (seriesIds != null) {
			for (int i =0; i < seriesIds.length; i++ ) {
				if (seriesIds[i] != 0) {
					Series s = seriesService.getSeries(seriesIds[i], false);
					item.addReport(s, reportNumbers[i]);
				}
			}
		}

		// Adds the selected identifiers
		if (identifierIds != null) {
			for (int i =0; i < identifierIds.length; i++ ) {
				if (identifierIds[i] != 0) {
					IdentifierType identifierType = identifierTypeService.get(identifierIds[i], false);
					item.addItemIdentifier(identifierValues[i], identifierType);
				}
			}
		}

		// Adds the selected extents
		if (extentIds != null) {
			for (int i =0; i < extentIds.length; i++ ) {
				if (extentIds[i] != 0) {
					ExtentType extentType = extentTypeService.getExtentType(extentIds[i], false);
					item.addItemExtent(extentType, extentValues[i]);
				}
			}
		}
		
		// Add subtitles
		if (subTitles != null) {
			for (int i =0; i < subTitles.length; i++ ) {
				if (subTitles[i] != null) {
					item.addSubTitle(subTitles[i]);
				}
			}
		}

		// Add sponsors
		if (sponsorIds != null ) {
			for (int i =0; i < sponsorIds.length; i++ ) {
				if (sponsorIds[i] != 0) {
					Sponsor sponsor = sponsorService.getSponsor(sponsorIds[i], false);
					item.addItemSponsor(sponsor, sponsorDescriptions[i]);
				}
			}
		}
		
		// Add secondary types
		if (typeIds != null) {
			for (int i =0; i < typeIds.length; i++ ) {
				if (typeIds[i] != 0) {
					ContentType contentType = contentTypeService.getContentType(typeIds[i], false);
					item.addSecondaryContentType(contentType);
				}
			}
		}
		
		// Sets the externally published information
		if (isExternallyPublished) {
			
			ExternalPublishedItem ex = item.getExternalPublishedItem();

			/* Create new externally published data */
			if (ex == null) {
				ExternalPublishedItem newExternalPublishedItem = new ExternalPublishedItem();
				newExternalPublishedItem.setCitation(citation);
				newExternalPublishedItem.setPublisher(publisherService.getPublisher(publisherId, false));
				newExternalPublishedItem.addPublishedDate(publishedMonth, publishedDay, publishedYear);

				item.setExternalPublishedItem(newExternalPublishedItem);
			} else {
				if (publisherId.equals(0l)) {
					ex.setPublisher(null);
				} else {
					ex.setPublisher(publisherService.getPublisher(publisherId, false));
				}
				
				PublishedDate pd = ex.getPublishedDate();
				
				if (pd == null) {
					ex.addPublishedDate(publishedMonth, publishedDay, publishedYear);
				} else {
					pd.setDay(publishedDay);
					pd.setMonth(publishedMonth);
					pd.setYear(publishedYear);
				}
				
				ex.setCitation(citation);
				
			}
		} 

		// Save the item
		itemService.makePersistent(item);
		
		// Delete old externally published data
		if (oldPublishedData != null) {
			itemService.deleteExternalPublishedItem(oldPublishedData);
		}
		
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
			IndexProcessingType processingType = indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE); 

			for(InstitutionalItem i : institutionalItems) {
				institutionalItemIndexProcessingRecordService.save(i.getId(), processingType);
			}
		}


		return SUCCESS;
	}

	/**
	 * Get all Series
	 * 
	 * @return All series
	 */
	public String getSeries() 
	{
		seriesList = seriesService.getAllSeries();
		
		return SUCCESS;
	}
	
	/**
	 * Get all Identifiers
	 * 
	 * @return All Identifiers
	 */
	public String getIdentifiers() 
	{
		identifierTypes = identifierTypeService.getAll();
	
		return SUCCESS;
	}

	/**
	 * Get all extent
	 * 
	 * @return All extents
	 */
	public String getExtents() 
	{
		extentTypes = extentTypeService.getAllExtentTypes();
	
		return SUCCESS;
	}

	/**
	 * Get all sponsors
	 * 
	 * @return All extents
	 */
	public String getAllSponsors() 
	{
		sponsors = sponsorService.getAllSponsor();
	
		return SUCCESS;
	}
	
	/**
	 * Get content type service
	 * 
	 * @return
	 */
	public ContentTypeService getContentTypeService() {
		return contentTypeService;
	}

	/**
	 * Set content type service
	 * 
	 * @param contentTypeService
	 */
	public void setContentTypeService(ContentTypeService contentTypeService) {
		this.contentTypeService = contentTypeService;
	}

	/**
	 * Get Series service
	 * 
	 * @return
	 */
	public SeriesService getSeriesService() {
		return seriesService;
	}

	/**
	 * Set series service
	 * 
	 * @param seriesService
	 */
	public void setSeriesService(SeriesService seriesService) {
		this.seriesService = seriesService;
	}

	/**
	 * Get item service
	 * 
	 * @return
	 */
	public ItemService getItemService() {
		return itemService;
	}
	
	/**
	 * Set item service
	 * 
	 * @param itemService
	 */
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	/**
	 * Get content types
	 * 
	 * @return
	 */
	public List<ContentType> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Set content types
	 * 
	 * @param contentTypes
	 */
	public void setContentTypes(List<ContentType> contentTypes) {
		this.contentTypes = contentTypes;
	}

	/**
	 * Get List of all series
	 * 
	 * @return
	 */
	public List<Series> getSeriesList() {
		return seriesList;
	}
	
	/**
	 * Set list of all series
	 * 
	 * @param seriesList
	 */
	public void setSeriesList(List<Series> seriesList) {
		this.seriesList = seriesList;
	}

	/**
	 * Get language type service
	 * 
	 * @return
	 */
	public LanguageTypeService getLanguageTypeService() {
		return languageTypeService;
	}

	/**
	 * Set language type service
	 *
	 * @param languageTypeService
	 */
	public void setLanguageTypeService(LanguageTypeService languageTypeService) {
		this.languageTypeService = languageTypeService;
	}

	/**
	 * Get identifier type service
	 * 
	 * @return
	 */
	public IdentifierTypeService getIdentifierTypeService() {
		return identifierTypeService;
	}

	/**
	 * Set identifier type service
	 * 
	 * @param identifierTypeService
	 */
	public void setIdentifierTypeService(IdentifierTypeService identifierTypeService) {
		this.identifierTypeService = identifierTypeService;
	}

	/**
	 * Get identifier types
	 * 
	 * @return
	 */
	public List<IdentifierType> getIdentifierTypes() {
		return identifierTypes;
	}

	/**
	 * Set identifier types
	 * 
	 * @param identifierTypes
	 */
	public void setIdentifierTypes(List<IdentifierType> identifierTypes) {
		this.identifierTypes = identifierTypes;
	}

	/**
	 * Get languages
	 * 
	 * @return
	 */
	public List<LanguageType> getLanguages() {
		return languages;
	}

	/**
	 * Set languages
	 * 
	 * @param languages
	 */
	public void setLanguages(List<LanguageType> languages) {
		this.languages = languages;
	}

	/**
	 * Get published service
	 * 
	 * @return
	 */
	public PublisherService getPublisherService() {
		return publisherService;
	}

	/**
	 * Set publisher service
	 * 
	 * @param publisherService
	 */
	public void setPublisherService(PublisherService publisherService) {
		this.publisherService = publisherService;
	}

	/**
	 * Get sponsor service 
	 * @return
	 */
	public SponsorService getSponsorService() {
		return sponsorService;
	}

	/**
	 * Set sponsor service
	 * @param sponsorService
	 */
	public void setSponsorService(SponsorService sponsorService) {
		this.sponsorService = sponsorService;
	}

	/**
	 * Get sponsors
	 * 
	 * @return
	 */
	public List<Sponsor> getSponsors() {
		return sponsors;
	}

	/**
	 * Set sponsors
	 * 
	 * @param sponsors
	 */
	public void setSponsors(List<Sponsor> sponsors) {
		this.sponsors = sponsors;
	}

	/**
	 * Get publishers
	 * 
	 * @return
	 */
	public List<Publisher> getPublishers() {
		return publishers;
	}

	/**
	 * Set publishers
	 * 
	 * @param publishers
	 */
	public void setPublishers(List<Publisher> publishers) {
		this.publishers = publishers;
	}

	/**
	 * Get item that is being edited 
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set item that is being edited
	 *  
	 * @param item
	 */
	public void setItem(GenericItem item) {
		this.item = item;
	}

	/**
	 * Get item abstract
	 * 
	 * @return
	 */
	public String getItemAbstract() {
		return itemAbstract;
	}
	
	/**
	 * Set item abstract
	 * 
	 * @param itemAbstract
	 */
	public void setItemAbstract(String itemAbstract) {
		this.itemAbstract = itemAbstract;
	}

	/**
	 * Get keywords
	 * 
	 * @return
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * Set keywords
	 * 
	 * @param keywords
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	/**
	 * Get content type id
	 * 
	 * @return
	 */
	public Long getContentTypeId() {
		return contentTypeId;
	}
	
	/**
	 * Set content type id
	 * 
	 * @param contentTypeId
	 */
	public void setContentTypeId(Long contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	/**
	 * Get language type id
	 * 
	 * @return
	 */
	public Long getLanguageTypeId() {
		return languageTypeId;
	}

	/**
	 * Set language type id
	 * 
	 * @param languageTypeId
	 */
	public void setLanguageTypeId(Long languageTypeId) {
		this.languageTypeId = languageTypeId;
	}

	/**
	 * Get sponsor id
	 * 
	 * @return
	 */
	public Long getSponsorId() {
		return sponsorId;
	}

	/**
	 * Set sponsor id
	 * 
	 * @param sponsorId
	 */
	public void setSponsorId(Long sponsorId) {
		this.sponsorId = sponsorId;
	}

	/**
	 * Get series ids
	 * 
	 * @return
	 */
	public Long[] getSeriesIds() {
		return seriesIds;
	}

	/**
	 * Set series ids
	 * 
	 * @param seriesIds
	 */
	public void setSeriesIds(Long[] seriesIds) {
		this.seriesIds = seriesIds;
	}

	/**
	 * Get report numbers
	 * 
	 * @return
	 */
	public String[] getReportNumbers() {
		return reportNumbers;
	}

	/**
	 * Set report numbers
	 * 
	 * @param reportNumbers
	 */
	public void setReportNumbers(String[] reportNumbers) {
		this.reportNumbers = reportNumbers;
	}

	/**
	 * Get number of reports
	 * 
	 * @return
	 */
	public int getReportsCount() {
		return reportsCount;
	}

	/**
	 * Set number of reports
	 * 
	 * @param reportsCount
	 */
	public void setReportsCount(int reportsCount) {
		this.reportsCount = reportsCount;
	}

	/**
	 * Get identifiers ids
	 * 
	 * @return
	 */
	public Long[] getIdentifierIds() {
		return identifierIds;
	}

	/**
	 * Set identifiers ids
	 * @param identifierIds
	 */
	public void setIdentifierIds(Long[] identifierIds) {
		this.identifierIds = identifierIds;
	}

	/**
	 * Get number of identifiers 
	 * 
	 * @return
	 */
	public int getItemIdentifiersCount() {
		return itemIdentifiersCount;
	}

	/**
	 *  Set number of identifiers
	 *  
	 * @param itemIdentifiersCount
	 */
	public void setItemIdentifiersCount(int itemIdentifiersCount) {
		this.itemIdentifiersCount = itemIdentifiersCount;
	}

	/**
	 * Set identifiers values
	 * 
	 * @param identifierValues
	 */
	public void setIdentifierValues(String[] identifierValues) {
		this.identifierValues = identifierValues;
	}

	/**
	 * Get person name ids
	 * 
	 * @return
	 */
	public Long[] getPersonNameIds() {
		return personNameIds;
	}

	/**
	 * Set person name ids
	 * 
	 * @param personNameIds
	 */
	public void setPersonNameIds(Long[] personNameIds) {
		this.personNameIds = personNameIds;
	}
	
	/**
	 * Get external published item
	 * 
	 * @return
	 */
	public ExternalPublishedItem getExternalPublishedItem() {
		return externalPublishedItem;
	}

	/**
	 * Set external published item
	 *  
	 * @param externalPublishedItem
	 */
	public void setExternalPublishedItem(ExternalPublishedItem externalPublishedItem) {
		this.externalPublishedItem = externalPublishedItem;
	}

	/**
	 * Indicates whether item is externally published or not
	 * 
	 * @return
	 */
	public Boolean getIsExternallyPublished() {
		return isExternallyPublished;
	}

	/**
	 * Sets whether item is externally published or not
	 * 
	 * @param isExternallyPublished
	 */
	public void setIsExternallyPublished(Boolean isExternallyPublished) {
		this.isExternallyPublished = isExternallyPublished;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}


	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

	public List<ItemObject> getItemObjects() {
		return itemObjects;
	}

	public String[] getSubTitles() {
		return subTitles;
	}

	public void setSubTitles(String[] subTitles) {
		this.subTitles = subTitles;
	}

	public int getSubTitlesCount() {
		return subTitlesCount;
	}

	public int getFirstAvailableMonth() {
		return firstAvailableMonth;
	}

	public void setFirstAvailableMonth(int firstAvailableMonth) {
		this.firstAvailableMonth = firstAvailableMonth;
	}

	public int getFirstAvailableDay() {
		return firstAvailableDay;
	}

	public void setFirstAvailableDay(int firstAvailableDay) {
		this.firstAvailableDay = firstAvailableDay;
	}

	public int getFirstAvailableYear() {
		return firstAvailableYear;
	}

	public void setFirstAvailableYear(int firstAvailableYear) {
		this.firstAvailableYear = firstAvailableYear;
	}

	public int getReleaseMonth() {
		return releaseMonth;
	}

	public void setReleaseMonth(int releaseMonth) {
		this.releaseMonth = releaseMonth;
	}

	public int getReleaseDay() {
		return releaseDay;
	}

	public void setReleaseDay(int releaseDay) {
		this.releaseDay = releaseDay;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

	public int getPublishedMonth() {
		return publishedMonth;
	}

	public void setPublishedMonth(int publishedMonth) {
		this.publishedMonth = publishedMonth;
	}

	public int getPublishedDay() {
		return publishedDay;
	}

	public void setPublishedDay(int publishedDay) {
		this.publishedDay = publishedDay;
	}

	public int getPublishedYear() {
		return publishedYear;
	}

	public void setPublishedYear(int publishedYear) {
		this.publishedYear = publishedYear;
	}


	public String getCitation() {
		return citation;
	}

	public void setCitation(String citation) {
		this.citation = citation;
	}

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

	public Long getPrimaryFileId() {
		return primaryFileId;
	}

	public void setPrimaryFileId(Long primaryFileId) {
		this.primaryFileId = primaryFileId;
	}

	public List<ItemFile> getThumbnailFiles() {
		return thumbnailFiles;
	}

	public void setThumbnailFiles(List<ItemFile> thumbnailFiles) {
		this.thumbnailFiles = thumbnailFiles;
	}
	
	public int getThumbnailFilesCount() {
		return thumbnailFiles.size();
	}

	public ExtentTypeService getExtentTypeService() {
		return extentTypeService;
	}

	public void setExtentTypeService(ExtentTypeService extentTypeService) {
		this.extentTypeService = extentTypeService;
	}

	public Long[] getExtentIds() {
		return extentIds;
	}

	public void setExtentIds(Long[] extentIds) {
		this.extentIds = extentIds;
	}

	public List<ExtentType> getExtentTypes() {
		return extentTypes;
	}

	public void setExtentTypes(List<ExtentType> extentTypes) {
		this.extentTypes = extentTypes;
	}

	public String[] getExtentValues() {
		return extentValues;
	}

	public void setExtentValues(String[] extentValues) {
		this.extentValues = extentValues;
	}

	public int getItemExtentsCount() {
		return itemExtentsCount;
	}

	public void setItemExtentsCount(int itemExtentsCount) {
		this.itemExtentsCount = itemExtentsCount;
	}

	public Long getGenericItemId() {
		return genericItemId;
	}

	public void setGenericItemId(Long genericItemId) {
		this.genericItemId = genericItemId;
	}

	public Long getReviewableItemId() {
		return reviewableItemId;
	}

	public void setReviewableItemId(Long reviewableItemId) {
		this.reviewableItemId = reviewableItemId;
	}

	public Long[] getSponsorIds() {
		return sponsorIds;
	}

	public void setSponsorIds(Long[] sponsorIds) {
		this.sponsorIds = sponsorIds;
	}

	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public boolean isThesis() {
		return thesis;
	}

	public void setThesis(boolean thesis) {
		this.thesis = thesis;
	}

	public Long[] getTypeIds() {
		return typeIds;
	}

	public void setTypeIds(Long[] typeIds) {
		this.typeIds = typeIds;
	}

	public void setSponsorDescriptions(String[] sponsorDescriptions) {
		this.sponsorDescriptions = sponsorDescriptions;
	}

	public int getItemSponsorsCount() {
		return itemSponsorsCount;
	}

	public void setCreatedMonth(int createdMonth) {
		this.createdMonth = createdMonth;
	}

	public void setCreatedDay(int createdDay) {
		this.createdDay = createdDay;
	}

	public void setCreatedYear(int createdYear) {
		this.createdYear = createdYear;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
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
