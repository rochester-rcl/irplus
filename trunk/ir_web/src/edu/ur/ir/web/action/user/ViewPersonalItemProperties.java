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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.ItemVersion;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserPublishingFileSystemService;

/**
 * Loads the item properties
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ViewPersonalItemProperties extends ActionSupport {
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 6266345166053778172L;

	/** Id of the item */
	private Long personalItemId;

	/** Personal item the user wishes to look at */
	private PersonalItem personalItem;
	
	private UserPublishingFileSystemService userPublishingFileSystemService;
	
    /** set of items that are the path for the current item */
	private Collection <PersonalCollection> collectionPath;
    
    /** List of versions and collections submitted to */
    private List<ItemVersionInfo> itemVersionInfo = new LinkedList<ItemVersionInfo>(); 
    
    /** Institutional item service */
    private InstitutionalItemService institutionalItemService;

    
	/**
	 * View personal item properties
	 */
	public String execute()
	{
	    personalItem = userPublishingFileSystemService.getPersonalItem(personalItemId, false);
	    PersonalCollection parentCollection = personalItem.getPersonalCollection();
	    if( parentCollection != null )
	    {
	    	collectionPath = userPublishingFileSystemService.getPersonalCollectionPath(parentCollection.getId());
	    }
	    
	    for(ItemVersion v: personalItem.getVersionedItem().getItemVersions()) {
	    	List<InstitutionalCollection> collectionsSubmittedTo 
	    		= institutionalItemService.getInstitutionalCollectionsSubmittedForGenericItem(v.getItem().getId()); 
	    	ItemVersionInfo i = new ItemVersionInfo(v, collectionsSubmittedTo);
	    	itemVersionInfo.add(i);
	    }
	    
		return SUCCESS;
	}


	/**
	 * Simple class to help with the display of the 
	 * version and the collections its been published to
	 * 
	 * @author Sharmila Ranganathan
	 *
	 */
	public class ItemVersionInfo
	{
		private ItemVersion version;
		
		private List<InstitutionalCollection> collections;
		
		public ItemVersionInfo(ItemVersion version, List<InstitutionalCollection> collections)
		{
			this.collections = collections;
			this.version = version;
		}

		public ItemVersion getVersion() {
			return version;
		}

		public List<InstitutionalCollection> getCollections() {
			return collections;
		}


	}
	public Long getPersonalItemId() {
		return personalItemId;
	}

	public void setPersonalItemId(Long itemId) {
		this.personalItemId = itemId;
	}

	public PersonalItem getPersonalItem() {
		return personalItem;
	}

	public void setPersonalItem(PersonalItem personalItem) {
		this.personalItem = personalItem;
	}

	public Collection<PersonalCollection> getCollectionPath() {
		return collectionPath;
	}

	public void setCollectionPath(Collection<PersonalCollection> collectionPath) {
		this.collectionPath = collectionPath;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

	public List<ItemVersionInfo> getItemVersionInfo() {
		return itemVersionInfo;
	}

	public void setItemVersionInfo(List<ItemVersionInfo> itemVersionInfo) {
		this.itemVersionInfo = itemVersionInfo;
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

}
