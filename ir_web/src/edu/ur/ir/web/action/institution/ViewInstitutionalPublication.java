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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.DeletedInstitutionalItem;
import edu.ur.ir.institution.DeletedInstitutionalItemService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemObject;
import edu.ur.ir.item.ItemSecurityService;
import edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider;
import edu.ur.ir.oai.metadata.provider.OaiService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.order.AscendingOrderComparator;


/**
 * View a public institutional item.
 * 
 * @author Nathan Sarr
 *
 */
public class ViewInstitutionalPublication extends ActionSupport implements UserIdAware {

	/** Eclipse generated id. */
	private static final long serialVersionUID = -1195251150185063000L;
	
	/**  Logger for preview publication action */
	private static final Logger log = Logger.getLogger(ViewInstitutionalPublication.class);
	
	/** Id of the institutional item being viewed.  */
	private Long institutionalItemId;
	
	/** version of the item the user wishes to get  */
	private Integer versionNumber;
	
	/** Institutional Item being viewed */
	private InstitutionalItem institutionalItem; 
	
	/** Generic item for the institutional item version */
	private GenericItem item;
	
	/** Service for dealing with user file system. */
	private InstitutionalItemService institutionalItemService;
	
	/** Service for dealing with deleted file system information */
	private DeletedInstitutionalItemService deletedInstitutionalItemService;


	/** Service to deal with institutional collection information */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Item object sorted for display */
	private List<ItemObject> itemObjects;
	
	/**  Generic Item being viewed */
	private InstitutionalItemVersion institutionalItemVersion;
	
	/** pat to the publication */
	private List<InstitutionalCollection> path;
	
	/** Information related to deleted institutional item */
	private DeletedInstitutionalItem deleteInfo;
	
	/** Long user id */
	private Long userId;
	
	/** Item Security service */
	private ItemSecurityService itemSecurityService;
	
	/** User service */
	private UserService userService;
	
	private boolean showPublication = false;
	
	/** Indicates user the reason for error */
	private String message;
	
	/** Id of item version */
	private Long institutionalItemVersionId;
	
	/** institutional repository object */
	private Repository repository;
	
	/** Service to deal with oai information */
	private OaiService oaiService;
	
	/** Service to provide meta-data information */
	private OaiMetadataServiceProvider oaiMetadataServiceProvider;
	


	/** Service for dealing with institutional item version inforamtion */
	private InstitutionalItemVersionService institutionalItemVersionService;
	


	/**
	 * Loads the institutional item.
	 * 
	 * Prepare for action
	 */
	public String execute(){
		log.debug("Institutional ItemId = " + institutionalItemId + " institutional item version id = " + institutionalItemVersionId);

		// load a specific version - already specified in the request
		if (institutionalItemVersionId != null) 
		{
			institutionalItemVersion = institutionalItemVersionService.getInstitutionalItemVersion(institutionalItemVersionId, false);
			
			if (institutionalItemVersion == null) 
			{
	        	log.debug("Institutional Item Version does not exist for InstitutionalItemVersionId :" + institutionalItemVersionId);
	        	message = "The publication does not exist or has been deleted";
	        	showPublication = false;
	        	return "not_found";
			}
			institutionalItem = institutionalItemVersion.getVersionedInstitutionalItem().getInstitutionalItem();

		} 
		// item and version seperated in request
		else if (institutionalItemId != null) 
		{
			institutionalItem = institutionalItemService.getInstitutionalItem(institutionalItemId, false);
		
			if (institutionalItem != null) {
				if( versionNumber == null || versionNumber <= 0)
				{
					institutionalItemVersion = institutionalItem.getVersionedInstitutionalItem().getCurrentVersion();
				}
				else
				{
					institutionalItemVersion =  institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(versionNumber);
				}
				
				if(institutionalItemVersion == null )
				{
					message = "The publication does not exist";
		        	return "not_found";
				}
			} 
			else 
			{
				
				deleteInfo =  deletedInstitutionalItemService.getDeleteInfoForInstitutionalItem(institutionalItemId);
				
				// Check if Institutional item is deleted
				if (deleteInfo != null) {
		        	log.debug("Institutional Item is deleted. Delete Info :" + deleteInfo);
		        	message = "The publication \"" + deleteInfo.getInstitutionalItemName() + "\"  has been deleted.";
		        	showPublication = false;
		        	return "deleted";
				} else {
		        	log.debug("Institutional Item does not exist for InstitutionalItemId :" + institutionalItemId);
		        	message = "The publication does not exist";
		        	showPublication = false;
		        	return "not_found";
				}
			}
		} 
		else 
		{
        	log.debug("institutional Item id is null");
        	showPublication = false;
        	message = "The publication does not exist";
        	return "not_found";
		}
		
		repository = institutionalItem.getInstitutionalCollection().getRepository();

		path = institutionalCollectionService.getPath(institutionalItem.getInstitutionalCollection());
		
		IrUser user = null;
		if (userId != null) 
        {
         	user = userService.getUser(userId, false);
        }
		
		
		
		if (!institutionalItemVersion.getItem().isEmbargoed()) 
		{
			if (!institutionalItemVersion.getItem().isPubliclyViewable()) 
			{
	            if (user != null) 
	            {
	            	if (!user.hasRole(IrRole.ADMIN_ROLE)) 
	            	{
	            		// check user permissions if they are not an administrator
	            	    if (itemSecurityService.hasPermission(institutionalItemVersion.getItem(), user, ItemSecurityService.ITEM_METADATA_READ_PERMISSION) <= 0
	            			&& itemSecurityService.hasPermission(institutionalItemVersion.getItem(), user, ItemSecurityService.ITEM_METADATA_EDIT_PERMISSION) <= 0)  {
	            		
	            	    	log.debug("User has no Read / edit metadata permission for this item");
	            	    	message = "Restricted Access";
	            	    	showPublication = false;
	            	    	return SUCCESS;
	            	    }
	            	}
	            } 
	            else 
	            {
                	log.debug("User is null. Publication is private.");
                	showPublication = false;
                	message = "Restricted Access";
                	return SUCCESS;
	            }
			}
		} 
		else 
		{
			log.debug("The publication is available for public from date:" + institutionalItemVersion.getItem().getReleaseDate());
			if (user != null) 
            {
            	if (!user.hasRole(IrRole.ADMIN_ROLE)  && !user.equals(institutionalItemVersion.getItem().getOwner())) 
            	{
            		// check user permissions if they are not an administrator - view privileges override embargo date
            	    if (itemSecurityService.hasPermission(institutionalItemVersion.getItem(), user, ItemSecurityService.ITEM_METADATA_READ_PERMISSION) <= 0
            			&& itemSecurityService.hasPermission(institutionalItemVersion.getItem(), user, ItemSecurityService.ITEM_METADATA_EDIT_PERMISSION) <= 0)  {
            		
            	    	log.debug("User has no Read / edit metadata permission for this item and it is embargoed");
            	    	message = "The publication will be available to view starting on date : " + institutionalItemVersion.getItem().getReleaseDate();
            	    	showPublication = false;
            	    	return SUCCESS;
            	    }
            	}
            } 
			else 
            {
				log.debug("User has no Read / edit metadata permission for this item and it is embargoed");
    	    	message = "The publication will be available to view starting on date : " + institutionalItemVersion.getItem().getReleaseDate();
    	    	showPublication = false;
    	    	return SUCCESS;
            }
		}

    	showPublication = true;
    	
		itemObjects = institutionalItemVersion.getItem().getItemObjects();
		item = institutionalItemVersion.getItem();
		
		// Sort item objects by order
		Collections.sort(itemObjects,   new AscendingOrderComparator());
		
		return SUCCESS;		
	}
	
	/**
	 * Get the set of metadata prefixes.
	 * 
	 * @return - a set of metadata prefixes.
	 */
	public Set<String> getMetadataPrefixes()
	{
		return oaiMetadataServiceProvider.getSupportedMetadataPrefixes();
	}
	
	/**
	 * Returns the oai namespace identifier.
	 * 
	 * @return
	 */
	public String getOaiNamespaceIdentifier()
	{
		return oaiService.getNamespaceIdentifier();
	}

	/**
	 * The institutional item id.
	 * 
	 * @return - id of the institutional item
	 */
	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	/**
	 * Set the institutional item id.
	 * 
	 * @param institutionalItemId
	 */
	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	/**
	 * The version number of the institutional item.
	 * 
	 * @return - the version number of the institutional item.
	 */
	public Integer getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Set the version number of the institutional item.
	 * 
	 * @param versionNumber - version number of the institutional item
	 */
	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * Get the loaded institutional item.
	 * 
	 * @return
	 */
	public InstitutionalItem getInstitutionalItem() {
		return institutionalItem;
	}

	public void setInstitutionalItem(InstitutionalItem institutionalItem) {
		this.institutionalItem = institutionalItem;
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public List<ItemObject> getItemObjects() {
		return itemObjects;
	}

	public void setItemObjects(List<ItemObject> itemObjects) {
		this.itemObjects = itemObjects;
	}

	public List<InstitutionalCollection> getPath() {
		return path;
	}

	public void setPath(List<InstitutionalCollection> path) {
		this.path = path;
	}

	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	public InstitutionalItemVersion getInstitutionalItemVersion() {
		return institutionalItemVersion;
	}

	public void setInstitutionalItemVersion(
			InstitutionalItemVersion institutionalItemVersion) {
		this.institutionalItemVersion = institutionalItemVersion;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public boolean isShowPublication() {
		return showPublication;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setItemSecurityService(ItemSecurityService itemSecurityService) {
		this.itemSecurityService = itemSecurityService;
	}

	public void setInstitutionalItemVersionId(long institutionalItemVersionId) {
		this.institutionalItemVersionId = institutionalItemVersionId;
	}

	public GenericItem getItem() {
		return item;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	
	public DeletedInstitutionalItem getDeleteInfo() {
		return deleteInfo;
	}
	
	public void setOaiService(OaiService oaiService) {
		this.oaiService = oaiService;
	}
	
	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}

	public DeletedInstitutionalItemService getDeletedInstitutionalItemService() {
		return deletedInstitutionalItemService;
	}

	public void setDeletedInstitutionalItemService(
			DeletedInstitutionalItemService deletedInstitutionalItemService) {
		this.deletedInstitutionalItemService = deletedInstitutionalItemService;
	}
	
	/**
	 * Set the oai metadata provider.
	 * 
	 * @param oaiMetadataServiceProvider
	 */
	public void setOaiMetadataServiceProvider(
			OaiMetadataServiceProvider oaiMetadataServiceProvider) {
		this.oaiMetadataServiceProvider = oaiMetadataServiceProvider;
	}
}
