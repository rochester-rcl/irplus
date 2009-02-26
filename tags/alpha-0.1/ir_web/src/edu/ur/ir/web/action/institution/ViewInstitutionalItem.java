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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemObject;
import edu.ur.order.AscendingOrderComparator;

/**
 * View an institutional item.
 * 
 * @author Nathan Sarr
 *
 */
public class ViewInstitutionalItem extends ActionSupport {

	/** Eclipse generated id. */
	private static final long serialVersionUID = -1195251150185063000L;
	
	/**  Logger for preview publication action */
	private static final Logger log = Logger.getLogger(ViewInstitutionalItem.class);
	
	/** Id of the institutional item being viewed.  */
	private Long institutionalItemId;
	
	/** version of the item the user wishes to get  */
	private Integer versionNumber;
	
	/** Institutional Item being viewed */
	private InstitutionalItem institutionalItem; 
	
	/** Service for dealing with user file system. */
	private InstitutionalItemService institutionalItemService;
	
	/** Item object sorted for display */
	private List<ItemObject> itemObjects;
	
	/**  Generic Item being viewed */
	private GenericItem item;
	
	/**
	 * Prepare for action
	 */
	public String execute(){
		log.debug("Institutional ItemId = " + institutionalItemId);

		
		if (institutionalItemId != null) {
			institutionalItem = institutionalItemService.getInstitutionalItem(institutionalItemId, false);
			
			if( versionNumber == null || versionNumber <= 0)
			{
			    item = institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getItem();
			}
			else
			{
				item =  institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(versionNumber).getItem();
			}
		}

		itemObjects = item.getItemObjects();
		
		// Sort item objects by order
		Collections.sort(itemObjects,   new AscendingOrderComparator());
		
		return SUCCESS;		
	}

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

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

	public GenericItem getItem() {
		return item;
	}

	public void setItem(GenericItem item) {
		this.item = item;
	}
	


}
