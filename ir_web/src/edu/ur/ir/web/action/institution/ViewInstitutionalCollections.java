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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * Returns the set of collections for the specified parent.  this only 
 * returns the first level of children.
 * 
 * @author Nathan Sarr
 *
 */
public class ViewInstitutionalCollections extends ActionSupport{
	
	/** eclipse generated id. */
	private static final long serialVersionUID = 310973564500274882L;
	
	/** default the parent collection id to 0 */
	private Long parentCollectionId = InstitutionalCollectionService.ROOT_COLLECTION_ID;
	
	/** Repository service */
	private RepositoryService repositoryService;
	
	/**  Logger. */
	private static final Logger log = LogManager.getLogger(ViewInstitutionalCollections.class);

	/** Set of institutional collections to display.  */
	private LinkedList<InstitutionalCollection> institutionalCollections = new LinkedList<InstitutionalCollection>();
	
	/** Repository for accessing institutional information. */
	private Repository repository;
	
	/** Institutional Collection service */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Used for sorting name based entities */
	private AscendingNameComparator nameComparator = new AscendingNameComparator();

	
	/**
	 * Execute viewing collections.
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		log.debug("view inst collections execute called");
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);

		if( repository != null )
		{
		    if( parentCollectionId.equals(InstitutionalCollectionService.ROOT_COLLECTION_ID))
		    {
			    institutionalCollections.addAll(repository.getInstitutionalCollections());
			    
		    }
		    else
		    {
		    	InstitutionalCollection parent = 
		    		institutionalCollectionService.getCollection(parentCollectionId, false);
		    	institutionalCollections.addAll(parent.getChildren());
		    }
		    Collections.sort(institutionalCollections, nameComparator);
		}
		
		
		return SUCCESS;
	}
	
	/**
	 * Parent id of the collection.
	 * 
	 * @return
	 */
	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	/**
	 * Set the parent id.
	 * 
	 * @param parentCollectionId
	 */
	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	/**
	 * Repository service for dealing with institutional collections.
	 * 
	 * @return
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	/**
	 * Set the repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * Get the institutional collections
	 * 
	 * @return
	 */
	public Collection<InstitutionalCollection> getInstitutionalCollections() {
		return institutionalCollections;
	}
	
	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}


}
