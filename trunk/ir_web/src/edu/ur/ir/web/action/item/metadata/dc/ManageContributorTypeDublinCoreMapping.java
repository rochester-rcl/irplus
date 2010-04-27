/**  
   Copyright 2008-2010 University of Rochester

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
package edu.ur.ir.web.action.item.metadata.dc;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMapping;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingService;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeService;
import edu.ur.metadata.dc.DublinCoreElement;
import edu.ur.metadata.dc.DublinCoreElementService;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * 
 * Manage information for a mapping between a contributor and dublin core identifier.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageContributorTypeDublinCoreMapping extends ActionSupport implements Comparator<ContributorTypeDublinCoreMapping>{

	/**  eclipse generated id */
	private static final long serialVersionUID = -247941538791834361L;
	
	/**  Service for dealing with contributor type dublin core mapping */
	private ContributorTypeDublinCoreMappingService contributorTypeDublinCoreMappingService;
	

	/** list of contributor type mappings */
	private List<ContributorTypeDublinCoreMapping> contributorTypeDublinCoreMappings;

	/** id of the contributor type */
	private Long contributorTypeId;

	/** id of the dublin core element id */
	private Long dublinCoreElementId;
	
	/**  Service for dealing with contributor types */
	private ContributorTypeService contributorTypeService;

    /** Service for dealing with Dublin core elements */
    private DublinCoreElementService dublinCoreElementService;
    
    /** Contributor Type dublin core mapping */
    private ContributorTypeDublinCoreMapping contributorTypeDublinCoreMapping;


	/** Message that can be displayed to the user. */
	private String message;

	/**  Indicates the copyright statement has been added*/
	private boolean added = false;
	
	/** Indicates the copyright statements have been deleted */
	private boolean deleted = false;
	
	/** id of the contributor type mapping  */
	private Long id;
	
	/** determine if this is an update */
	private boolean update = false;
	

	/**  Logger for managing copyright statements*/
	private static final Logger log = Logger.getLogger(ManageContributorTypeDublinCoreMapping.class);
	
	/** Used for sorting name based entities */
	private AscendingNameComparator nameComparator = new AscendingNameComparator();

	
	public String execute()
	{
	    viewAll();
	    return SUCCESS;
	}

	/**
	 * Method to create a new copyright statement.
	 * 
	 * Create a new copyright statement
	 */
	public String create()
	{
		log.debug("creating a dc contributor type mapping contributorTypeId = " + contributorTypeId +
				" dublin core elment id = " + dublinCoreElementId);
		
		added = false;
		
	    // determine if the mapping already exists
		ContributorTypeDublinCoreMapping other = contributorTypeDublinCoreMappingService.get(contributorTypeId);
		
		if( other == null)
		{
			ContributorType contributorType = contributorTypeService.get(contributorTypeId, false);
			DublinCoreElement dublinCoreElement = dublinCoreElementService.getById(dublinCoreElementId, false);
			
			if( contributorType != null && dublinCoreElement != null)
			{
				contributorTypeDublinCoreMapping = new ContributorTypeDublinCoreMapping(contributorType, dublinCoreElement);
				contributorTypeDublinCoreMappingService.save(contributorTypeDublinCoreMapping);
				added = true;
			}
			else
			{
				message = getText("contributorTypeDublinCoreElementMappingMissingDataError");
				addFieldError("contributorTypeDublinCoreElementMappingMissingData", message);
			}
		}
		else
		{
			message = getText("contributorTypeDublinCoreElementMappingExistsError");
			addFieldError("contributorTypeDublinCoreElementMappingAlreadyExists", message);
		}
		log.debug("message = " + message);
		log.debug(" returning added");
        return "added";
	}
	
	/**
	 * Method to update an mapping
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updating a dc contributor type mapping contributorTypeId = " + contributorTypeId +
				" dublin core elment id = " + dublinCoreElementId + " id = " + id);

		added = false;

	    // determine if the mapping already exists
		contributorTypeDublinCoreMapping = contributorTypeDublinCoreMappingService.getById(id, false);

		ContributorTypeDublinCoreMapping other = contributorTypeDublinCoreMappingService.get(contributorTypeId);
		log.debug("other = " + other  + " other.getId().equals(id) = " + other.getId().equals(id));
		
		if( other == null  || other.getId().equals(id))
		{
			ContributorType contributorType = contributorTypeService.get(contributorTypeId, false);
			DublinCoreElement dublinCoreElement = dublinCoreElementService.getById(dublinCoreElementId, false);
			
			if( contributorType != null && dublinCoreElement != null)
			{
				
				log.debug("contributor type dublinc core mapping = " + contributorTypeDublinCoreMapping);
				contributorTypeDublinCoreMapping.setContributorType(contributorType);
				contributorTypeDublinCoreMapping.setDublinCoreElement(dublinCoreElement);
				
				log.debug("contributor type dublinc**2** core mapping with changes = " + contributorTypeDublinCoreMapping);

				contributorTypeDublinCoreMappingService.save(contributorTypeDublinCoreMapping);
				added = true;
			}
			else
			{
				message = getText("contributorTypeDublinCoreElementMappingMissingDataError");
				addFieldError("contributorTypeDublinCoreElementMappingMissingData", message);
			}
		}
		else			
		{
			message = getText("contributorTypeDublinCoreElementMappingExistsError");
			addFieldError("contributorTypeDublinCoreElementMappingAlreadyExists", message);
		}        
		return "added";
	}
	
	/**
	 * Get a contributor type dublin core mapping
	 * 
	 * @return the contributor type dublin core mapping
	 */
	public String get()
	{
		contributorTypeDublinCoreMapping = contributorTypeDublinCoreMappingService.getById(id, false);
		return "get";
	}
	
	/**
	 * Removes the selected contributor type dublin core mapping
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete contributor type dublin core mapping called id = " + id);
		contributorTypeDublinCoreMapping = contributorTypeDublinCoreMappingService.getById(id, false);
		if( contributorTypeDublinCoreMapping != null)
		{
			contributorTypeDublinCoreMappingService.delete(contributorTypeDublinCoreMapping);
		}
		deleted = true;
		return "deleted";
	}
 
	/**
	 * Get the copyright statements table data.
	 * 
	 * @return
	 */
	public String viewAll()
	{
		contributorTypeDublinCoreMappings = contributorTypeDublinCoreMappingService.getAll();
		Collections.sort(contributorTypeDublinCoreMappings, this);
		return SUCCESS;
	}

	public void setContributorTypeId(Long contributorTypeId) {
		this.contributorTypeId = contributorTypeId;
	}

	public void setDublinCoreElementId(Long dublinCoreElementId) {
		this.dublinCoreElementId = dublinCoreElementId;
	}
	
	public void setContributorTypeService(
			ContributorTypeService contributorTypeService) {
		this.contributorTypeService = contributorTypeService;
	}

	public void setDublinCoreElementService(
			DublinCoreElementService dublinCoreElementService) {
		this.dublinCoreElementService = dublinCoreElementService;
	}

	public void setContributorTypeDublinCoreMappingService(
			ContributorTypeDublinCoreMappingService contributorTypeDublinCoreMappingService) {
		this.contributorTypeDublinCoreMappingService = contributorTypeDublinCoreMappingService;
	}

	public void setContributorTypeDublinCoreMapping(
			ContributorTypeDublinCoreMapping contributorTypeDublinCoreMapping) {
		this.contributorTypeDublinCoreMapping = contributorTypeDublinCoreMapping;
	}
	
	public boolean getAdded() {
		return added;
	}

	public boolean getDeleted() {
		return deleted;
	}
	
	public Collection<ContributorTypeDublinCoreMapping> getContributorTypeDublinCoreMappings() {
		return contributorTypeDublinCoreMappings;
	}
	
	/**
	 * Get contributor types
	 * 
	 * @return
	 */
	public List<ContributorType> getContributorTypes() {
		List<ContributorType> contributorTypes = contributorTypeService.getAll();
		Collections.sort(contributorTypes, nameComparator);
		return contributorTypes;
	}
	
	/**
	 * Get dublin core elements
	 * 
	 * @return
	 */
	public List<DublinCoreElement> getDublinCoreElements() {
		List<DublinCoreElement> dublinCoreElements = dublinCoreElementService.getAll();
		Collections.sort(dublinCoreElements, nameComparator);
		return dublinCoreElements;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMessage() {
		return message;
	}


	public boolean getUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(ContributorTypeDublinCoreMapping o1,
			ContributorTypeDublinCoreMapping o2) {
		return o1.getContributorType().getName().compareToIgnoreCase(o2.getContributorType().getName());
	}


}
