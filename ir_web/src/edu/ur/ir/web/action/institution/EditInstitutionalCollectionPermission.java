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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;

/**
 * Manage an institutional collection permission.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class EditInstitutionalCollectionPermission extends ActionSupport {

	/**  Logger. */
	private static final Logger log = LogManager.getLogger(EditInstitutionalCollectionPermission.class);

	/**  Generated version id */
	private static final long serialVersionUID = -6356386996693654277L;
	
	/** Id of the collection */
	private InstitutionalCollection collection;
	
	/** Id of the collection */
	private Long collectionId;
	
 	/** Institutional Collection service */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Indicates whether the collection's child publication's permissions have to be updated */
	private boolean updateChildrenPermission;


	/**
	 * Execute get collection.
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() {
		log.debug("collectionId::"+ collectionId);
		collection = 
			institutionalCollectionService.getCollection(collectionId, false);
		
		return SUCCESS;
	}
	
	/**
	 * Sets the researcher page public / hidden
	 * 
	 * @return
	 */
	public String setPubliclyViewableStatus() {
		
		log.debug("updateChildrenPermission::"+ updateChildrenPermission);

		
		collection = 
			institutionalCollectionService.getCollection(collectionId, false);
		
		if(collection.isPubliclyViewable()) {
			collection.setPubliclyViewable(false);
			if (updateChildrenPermission) {
				
				log.debug("isChildren private::"+ updateChildrenPermission);
				institutionalCollectionService.setAllItemsWithinCollectionPrivate(collection);
			}
			
		} else {
			collection.setPubliclyViewable(true);
			if (updateChildrenPermission) {
				log.debug("isChildren public:"+ updateChildrenPermission);
				institutionalCollectionService.setAllItemsWithinCollectionPublic(collection);
			}
		}
		
		institutionalCollectionService.saveCollection(collection);
		
		return SUCCESS;
		
	}
	/**
	 * Get the institutional collection.
	 * 
	 * @return
	 */
	public InstitutionalCollection getCollection() {
		return collection;
	}

	/**
	 * Set the institutional collection 
	 * 
	 * @param collection
	 */
	public void setCollection(
			InstitutionalCollection institutionalCollection) {
		this.collection = institutionalCollection;
	}

	/**
	 * Set the collection id.
	 * 
	 * @param collectionId
	 */
	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}
	
	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}


	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}


	public void setUpdateChildrenPermission(boolean updateChildrenPermission) {
		this.updateChildrenPermission = updateChildrenPermission;
	}

	public boolean isUpdateChildrenPermission() {
		return updateChildrenPermission;
	}


}
