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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeService;
import edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMapping;
import edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMappingService;
import edu.ur.metadata.dc.DublinCoreEncodingScheme;
import edu.ur.metadata.dc.DublinCoreEncodingSchemeService;
import edu.ur.metadata.dc.DublinCoreTerm;
import edu.ur.metadata.dc.DublinCoreTermService;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * @author NathanS
 *
 */
public class ManageIdentifierTypeDublinCoreMapping extends ActionSupport implements Comparator<IdentifierTypeDublinCoreMapping>{

	/**  eclipse generated id */
	private static final long serialVersionUID = -247941538791834361L;
	
	/**  Service for dealing with identifier type dublin core mapping */
	private IdentifierTypeDublinCoreMappingService identifierTypeDublinCoreMappingService;

	/** list of identifier type mappings */
	private List<IdentifierTypeDublinCoreMapping> identifierTypeDublinCoreMappings;

	/** id of the identifier type */
	private Long identifierTypeId;

	/** id of the dublin core element id */
	private Long dublinCoreTermId;
	
	/** id of the dublin core encoding scheme  */
	private Long dublinCoreEncodingSchemeId;

	/**  Service for dealing with identifier types */
	private IdentifierTypeService identifierTypeService;

    /** Service for dealing with Dublin core elements */
    private DublinCoreTermService dublinCoreTermService;
    
    /** Service for dealing with dublin core encoding scheme data */
    private DublinCoreEncodingSchemeService dublinCoreEncodingSchemeService;  

	/** Contributor Type dublin core mapping */
    private IdentifierTypeDublinCoreMapping identifierTypeDublinCoreMapping;



	/** Message that can be displayed to the user. */
	private String message;

	/**  Indicates the copyright statement has been added*/
	private boolean added = false;
	
	/** Indicates the copyright statements have been deleted */
	private boolean deleted = false;
	
	/** id of the identifier type mapping  */
	private Long id;
	
	/** determine if this is an update */
	private boolean update = false;
	
	/**  Logger for managing copyright statements*/
	private static final Logger log = LogManager.getLogger(ManageIdentifierTypeDublinCoreMapping.class);
	
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
		log.debug("creating a dc identifier type mapping identifierTypeId = " + identifierTypeId +
				" dublin core elment id = " + dublinCoreTermId);
		
		added = false;
		
	    // determine if the mapping already exists
		IdentifierTypeDublinCoreMapping other = identifierTypeDublinCoreMappingService.get(identifierTypeId);
		
		if( other == null)
		{
			IdentifierType identifierType = identifierTypeService.get(identifierTypeId, false);
			DublinCoreTerm dublinCoreTerm = dublinCoreTermService.getById(dublinCoreTermId, false);
			
			DublinCoreEncodingScheme dublinCoreEncodingScheme = null;
			
			if( dublinCoreEncodingSchemeId != null )
			{
			    dublinCoreEncodingScheme = dublinCoreEncodingSchemeService.getById(dublinCoreEncodingSchemeId, false);
			}
			if( identifierType != null && dublinCoreTerm != null)
			{
				identifierTypeDublinCoreMapping = new IdentifierTypeDublinCoreMapping(identifierType, dublinCoreTerm);
				identifierTypeDublinCoreMapping.setDublinCoreEncodingScheme(dublinCoreEncodingScheme);
				identifierTypeDublinCoreMappingService.save(identifierTypeDublinCoreMapping);
				
				added = true;
			}
			else
			{
				message = getText("identifierTypeDublinCoreTermMappingMissingDataError");
				addFieldError("identifierTypeDublinCoreTermMappingMissingData", message);
			}
		}
		else
		{
			message = getText("identifierTypeDublinCoreTermMappingExistsError");
			addFieldError("identifierTypeDublinCoreTermMappingAlreadyExists", message);
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
		log.debug("updating a dc identifier type mapping identifierTypeId = " + identifierTypeId +
				" dublin core elment id = " + dublinCoreTermId + " id = " + id);

		added = false;

	    // determine if the mapping already exists
		identifierTypeDublinCoreMapping = identifierTypeDublinCoreMappingService.getById(id, false);

		IdentifierTypeDublinCoreMapping other = identifierTypeDublinCoreMappingService.get(identifierTypeId);
		log.debug("other = " + other );
		
		
		if( other == null  || other.getId().equals(id))
		{
			IdentifierType identifierType = identifierTypeService.get(identifierTypeId, false);
			DublinCoreTerm dublinCoreTerm = dublinCoreTermService.getById(dublinCoreTermId, false);
            DublinCoreEncodingScheme dublinCoreEncodingScheme = null;
			
			if( dublinCoreEncodingSchemeId != null )
			{
			    dublinCoreEncodingScheme = dublinCoreEncodingSchemeService.getById(dublinCoreEncodingSchemeId, false);
			}
			if( identifierType != null && dublinCoreTerm != null)
			{
				
				log.debug("identifier type dublinc core mapping = " + identifierTypeDublinCoreMapping);
				identifierTypeDublinCoreMapping.setIdentifierType(identifierType);
				identifierTypeDublinCoreMapping.setDublinCoreTerm(dublinCoreTerm);
				identifierTypeDublinCoreMapping.setDublinCoreEncodingScheme(dublinCoreEncodingScheme);
				
				log.debug("identifier type dublinc**2** core mapping with changes = " + identifierTypeDublinCoreMapping);

				identifierTypeDublinCoreMappingService.save(identifierTypeDublinCoreMapping);
				added = true;
			}
			else
			{
				message = getText("identifierTypeDublinCoreTermMappingMissingDataError");
				addFieldError("identifierTypeDublinCoreTermMappingMissingData", message);
			}
		}
		else			
		{
			message = getText("identifierTypeDublinCoreTermMappingExistsError");
			addFieldError("identifierTypeDublinCoreTermMappingAlreadyExists", message);
		}        
		return "added";
	}
	
	/**
	 * Get a identifier type dublin core mapping
	 * 
	 * @return the identifier type dublin core mapping
	 */
	public String get()
	{
		identifierTypeDublinCoreMapping = identifierTypeDublinCoreMappingService.getById(id, false);
		return "get";
	}
	
	/**
	 * Removes the selected identifier type dublin core mapping
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete identifier type dublin core mapping called id = " + id);
		identifierTypeDublinCoreMapping = identifierTypeDublinCoreMappingService.getById(id, false);
		if( identifierTypeDublinCoreMapping != null)
		{
			identifierTypeDublinCoreMappingService.delete(identifierTypeDublinCoreMapping);
		}
		deleted = true;
		return "deleted";
	}
 
	/**
	 * Get the identifier type table data.
	 * 
	 * @return
	 */
	public String viewAll()
	{
		identifierTypeDublinCoreMappings = identifierTypeDublinCoreMappingService.getAll();
		Collections.sort(identifierTypeDublinCoreMappings, this);
		return SUCCESS;
	}

	public void setIdentifierTypeId(Long identifierTypeId) {
		this.identifierTypeId = identifierTypeId;
	}

	public void setDublinCoreTermId(Long dublinCoreTermId) {
		this.dublinCoreTermId = dublinCoreTermId;
	}
	
	public void setIdentifierTypeService(
			IdentifierTypeService identifierTypeService) {
		this.identifierTypeService = identifierTypeService;
	}

	public void setDublinCoreTermService(
			DublinCoreTermService dublinCoreTermService) {
		this.dublinCoreTermService = dublinCoreTermService;
	}

	public void setIdentifierTypeDublinCoreMappingService(
			IdentifierTypeDublinCoreMappingService identifierTypeDublinCoreMappingService) {
		this.identifierTypeDublinCoreMappingService = identifierTypeDublinCoreMappingService;
	}

	public IdentifierTypeDublinCoreMapping getIdentifierTypeDublinCoreMapping() {
		return identifierTypeDublinCoreMapping;
	}
	
	public boolean getAdded() {
		return added;
	}

	public boolean getDeleted() {
		return deleted;
	}
	
	public Collection<IdentifierTypeDublinCoreMapping> getIdentifierTypeDublinCoreMappings() {
		return identifierTypeDublinCoreMappings;
	}
	
	/**
	 * Get identifier types
	 * 
	 * @return
	 */
	public List<IdentifierType> getIdentifierTypes() {
		List<IdentifierType> identifierTypes = identifierTypeService.getAll();
		Collections.sort(identifierTypes, nameComparator);
		return identifierTypes;
	}
	
	/**
	 * Get dublin core elements
	 * 
	 * @return
	 */
	public List<DublinCoreTerm> getDublinCoreTerms() {
		List<DublinCoreTerm> dublinCoreTerms = dublinCoreTermService.getAll();
		Collections.sort(dublinCoreTerms, nameComparator);
		return dublinCoreTerms;
	}
	
	/**
	 * Get dublin core encoding Schemes
	 * 
	 * @return
	 */
	public List<DublinCoreEncodingScheme> getDublinCoreEncodingSchemes() {
		List<DublinCoreEncodingScheme> dublinCoreEncodingScheme = dublinCoreEncodingSchemeService.getAll();
		Collections.sort(dublinCoreEncodingScheme, nameComparator);
		return dublinCoreEncodingScheme;
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
	public int compare(IdentifierTypeDublinCoreMapping o1,
			IdentifierTypeDublinCoreMapping o2) {
		return o1.getIdentifierType().getName().compareToIgnoreCase(o2.getIdentifierType().getName());
	}


    public DublinCoreEncodingSchemeService getDublinCoreEncodingSchemeService() {
		return dublinCoreEncodingSchemeService;
	}

	public void setDublinCoreEncodingSchemeService(
			DublinCoreEncodingSchemeService dublinCoreEncodingSchemeService) {
		this.dublinCoreEncodingSchemeService = dublinCoreEncodingSchemeService;
	}
	
	public Long getDublinCoreEncodingSchemeId() {
		return dublinCoreEncodingSchemeId;
	}

	public void setDublinCoreEncodingSchemeId(Long dublinCoreEncodingSchemeId) {
		this.dublinCoreEncodingSchemeId = dublinCoreEncodingSchemeId;
	}

}
