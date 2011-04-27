/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.web.action.item.metadata.marc;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;


import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeService;
import edu.ur.ir.marc.MarcContentTypeFieldMapper;
import edu.ur.ir.marc.MarcContentTypeFieldMapperService;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * Manage the marc content type field actions.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageMarcContentTypeField extends ActionSupport 
    implements Comparator<MarcContentTypeFieldMapper>, ServletRequestAware{
	
	// servlet request object
	private HttpServletRequest request;
	
	// eclipse generated id. */
	private static final long serialVersionUID = -8663881337958958315L;

	//  Service for dealing with mapping between content type and marc fields */
	private MarcContentTypeFieldMapperService marcContentTypeFieldMapperService;

	// list of mappings */
	private List<MarcContentTypeFieldMapper> marcContentTypeFieldMappings;

	// id of the content type */
	private Long contentTypeId;

	// Service for dealing with content types */
	private ContentTypeService contentTypeService;
 
	// Content type marc mapping */
    private MarcContentTypeFieldMapper marcContentTypeFieldMapper;

    // Message that can be displayed to the user. */
	private String message;

	// id of the content type mapping  */
	private Long id;
	
	//determine if this is an update */
	private boolean update = false;
	
	//  Logger for managing copyright statements*/
	private static final Logger log = Logger.getLogger(ManageMarcContentTypeField.class);
	
	// Used for sorting name based entities 
	private AscendingNameComparator nameComparator = new AscendingNameComparator();
	
	// hold the marc 006 fields
	private String[] field006 = new String[MarcContentTypeFieldMapper.DATA_006_LENGTH];
	private char[] charField006 = new char[MarcContentTypeFieldMapper.DATA_006_LENGTH];
	
	// hold the marc 007 fields
	private String[] field007 = new String[MarcContentTypeFieldMapper.DATA_007_LENGTH];
	private char[] charField007 = new char[MarcContentTypeFieldMapper.DATA_007_LENGTH];
	
	// hold the marc 008 fields
    private String[] field008 = new String[MarcContentTypeFieldMapper.DATA_008_LENGTH];
    private char[] charField008 = new char[MarcContentTypeFieldMapper.DATA_008_LENGTH];
        
	// leader record status
	private char recordStatus = ' ';

	// leader record type
	private char typeOfRecord = ' ';
	
	// leader bib level
	private char bibliographicLevel = ' ';
	
	// leader type of control
	private char typeOfControl = ' ';
	
	// leader encoding level
	private char encodingLevel = ' ';
	
	// descriptive cataloging form
	private char descriptiveCatalogingForm = ' ';



	public String execute()
	{
	    return SUCCESS;
	}

	/**
	 * Method to create a new content type mapping
	 * 
	 * Create a new copyright statement
	 */
	public String save()
	{
		log.debug("creating a marc content type mapping contentTypeId = " + contentTypeId);
		this.loadFieldsFromRequest();
		if( log.isDebugEnabled() )
		{
			log.debug("field 006 = " + new String(charField006));
		    log.debug("field 007 = " + new String(charField007));
		    log.debug("field 008 = " + new String(charField008));
		    log.debug("bibliogrpahic level = " + bibliographicLevel);
		    log.debug("descriptiove cataloging form = " + descriptiveCatalogingForm);
		    log.debug("encoding level = " + encodingLevel);
		    log.debug("recording status = " + recordStatus);
		    log.debug("type of control = " + typeOfControl);
		    log.debug("type of record = " + typeOfRecord);
		   
		}
		

	    // determine if the mapping already exists
		MarcContentTypeFieldMapper other = marcContentTypeFieldMapperService.getByContentTypeId(contentTypeId);
		log.debug("other = " + other + " id = " + id);
		if( other == null || other.getId().equals(id))
		{
			log.debug("going into save");
			ContentType contentType = contentTypeService.getContentType(contentTypeId, false);
			
			if( contentType != null )
			{
				log.debug("content type is not null");
				if( id != null )
				{
					marcContentTypeFieldMapper = marcContentTypeFieldMapperService.getById(id, false);
					marcContentTypeFieldMapper.setContentType(contentType);
				}
				else
				{
				    marcContentTypeFieldMapper = new MarcContentTypeFieldMapper(contentType);
				}
				marcContentTypeFieldMapper.setControlField006(charField006);
				marcContentTypeFieldMapper.setControlField007(charField007);
				marcContentTypeFieldMapper.setControlField008(charField008);
				
				marcContentTypeFieldMapper.setBibliographicLevel(bibliographicLevel);
				marcContentTypeFieldMapper.setDescriptiveCatalogingForm(descriptiveCatalogingForm);
				marcContentTypeFieldMapper.setEncodingLevel(encodingLevel);
				marcContentTypeFieldMapper.setRecordStatus(recordStatus);
				marcContentTypeFieldMapper.setTypeOfControl(typeOfControl);
				marcContentTypeFieldMapper.setTypeOfRecord(typeOfRecord);
				
				log.debug("marcContentTypeFieldMapper = " + marcContentTypeFieldMapper);
				marcContentTypeFieldMapperService.save(marcContentTypeFieldMapper);
				
			}
			else
			{
				message = getText("contentTypeMarcMappingMissingDataError");
				addFieldError("contentTypeMarcMappingMissingDataError", message);
			}
		}
		else
		{
			message = getText("contentTypeMarcMappingExistsError");
			addFieldError("contentTypeMarcMappingAlreadyExists", message);
			return "addError";
		}
		log.debug("message = " + message);
		log.debug(" returning added");
        return "added";
	}
	
	/**
	 * Method to edit or create a new mapping.
	 * 
	 * @return
	 */
	public String edit()
	{
		if( id != null )
		{
		    marcContentTypeFieldMapper = marcContentTypeFieldMapperService.getById(id, false);
		
	        if( marcContentTypeFieldMapper != null )
	        {
	    	    setFieldsFromMapper();
	        }
		}
		return "edit";
	}
	

	
	/**
	 * Get a contributor type dublin core mapping
	 * 
	 * @return the contributor type dublin core mapping
	 */
	public String get()
	{
		marcContentTypeFieldMapper = marcContentTypeFieldMapperService.getById(id, false);
		return "get";
	}
	
	/**
	 * Removes the selected contributor type dublin core mapping
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete content type marc mappings called id = " + id);
		marcContentTypeFieldMapper = marcContentTypeFieldMapperService.getById(id, false);
		if( marcContentTypeFieldMapper != null)
		{
			marcContentTypeFieldMapperService.delete(marcContentTypeFieldMapper);
		}
		return "deleted";
	}
 
	/**
	 * Get the content type filed mappings.
	 * 
	 * @return
	 */
	public  List<MarcContentTypeFieldMapper> getMarcContentTypeFieldMappings()
	{
		marcContentTypeFieldMappings = marcContentTypeFieldMapperService.getAll();
		Collections.sort(marcContentTypeFieldMappings, this);
		return marcContentTypeFieldMappings;
	}

	public void setContentTypeId(Long contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	
	public void setContentTypeService(
			ContentTypeService contentTypeService) {
		this.contentTypeService = contentTypeService;
	}
	
	
	/**
	 * Get contributor types
	 * 
	 * @return
	 */
	public List<ContentType> getContentTypes() {
		List<ContentType> contentTypes = contentTypeService.getAllContentType();
		Collections.sort(contentTypes, nameComparator);
		return contentTypes;
	}
	

	public MarcContentTypeFieldMapper getMarcContentTypeFieldMapper() {
		return marcContentTypeFieldMapper;
	}

	public void setMarcContentTypeFieldMapperService(
			MarcContentTypeFieldMapperService marcContentTypeFieldMapperService) {
		this.marcContentTypeFieldMapperService = marcContentTypeFieldMapperService;
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
	public int compare(MarcContentTypeFieldMapper o1,
			MarcContentTypeFieldMapper o2) {
		return o1.getContentType().getName().compareToIgnoreCase(o2.getContentType().getName());
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	// load all of the fields passed in from form
	private void loadFieldsFromRequest()
	{
		for( int index = 0; index < MarcContentTypeFieldMapper.DATA_006_LENGTH; index++)
		{
			String value = request.getParameter("cf006_" + index);
			if( value != null && value.length() > 0 )
			{
			    charField006[index] = request.getParameter("cf006_" + index).charAt(0);
			}
			else
			{
				charField006[index] = ' ';
			}
		}
		
		for( int index = 0; index < MarcContentTypeFieldMapper.DATA_007_LENGTH; index++)
		{
			String value = request.getParameter("cf007_" + index);
			if( value != null && value.length() > 0 )
			{
			    charField007[index] = request.getParameter("cf007_" + index).charAt(0);
			}
			else
			{
				charField007[index] = '|';
			}
		}
		
		for( int index = 0; index < MarcContentTypeFieldMapper.DATA_008_LENGTH; index++)
		{
			String value = request.getParameter("cf008_" + index);
			if( value != null && value.length() > 0 )
			{
			    charField008[index] = request.getParameter("cf008_" + index).charAt(0);
			}
			else
			{
				charField008[index] = ' ';
			}
		}
	
		
		String recordStatusStr = request.getParameter("recordStatus");
		if( recordStatusStr != null && recordStatusStr.trim().length() > 0 )
		{
			recordStatus = recordStatusStr.charAt(0);
		}
	
		
		String typeOfRecordStr = request.getParameter("typeOfRecord");
		if(typeOfRecordStr != null && typeOfRecordStr.length() > 0 )
		{
			typeOfRecord = typeOfRecordStr.charAt(0);
		}
		
		String bibliographicLevelStr = request.getParameter("bibliographicLevel");
		if(bibliographicLevelStr != null && bibliographicLevelStr.length() > 0 )
		{
			bibliographicLevel = bibliographicLevelStr.charAt(0);
		}
		
		
		String typeOfControlStr = request.getParameter("typeOfControl");
		if(typeOfControlStr != null && typeOfControlStr.length() > 0 )
		{
			typeOfControl = typeOfControlStr.charAt(0);
		}
		
		String encodingLevelStr = request.getParameter("encodingLevel");
		if(encodingLevelStr != null && encodingLevelStr.length() > 0 )
		{
			encodingLevel = encodingLevelStr.charAt(0);
		}
		
		String descriptiveCatalogingFormStr = request.getParameter("descriptiveCatalogingForm");
		if(descriptiveCatalogingFormStr != null && descriptiveCatalogingFormStr.length() > 0 )
		{
			descriptiveCatalogingForm = descriptiveCatalogingFormStr.charAt(0);
		}
	}
	
	// load fields from the loaded mapper
	private void setFieldsFromMapper()
	{
		char[] mapper006 = marcContentTypeFieldMapper.getControlField006().toCharArray();
		for( int index = 0; index < MarcContentTypeFieldMapper.DATA_006_LENGTH; index++)
		{
	        field006[index] = "" +mapper006[index];
		}
		
		char[] mapper007 = marcContentTypeFieldMapper.getControlField007().toCharArray();
		for( int index = 0; index < MarcContentTypeFieldMapper.DATA_007_LENGTH; index++)
		{
			if( mapper007[index] != '|')
			{
			    field007[index] = "" + mapper007[index];
			}
			else
			{
				field007[index] = " ";
			}
		}
		
		char[] mapper008 = marcContentTypeFieldMapper.getControlField008().toCharArray();
		for( int index = 0; index < MarcContentTypeFieldMapper.DATA_008_LENGTH; index++)
		{
	        field008[index] = "" + mapper008[index];
		}

		
		recordStatus = marcContentTypeFieldMapper.getRecordStatus();
		typeOfRecord = marcContentTypeFieldMapper.getTypeOfRecord();
		bibliographicLevel = marcContentTypeFieldMapper.getBibliographicLevel();
		typeOfControl = marcContentTypeFieldMapper.getTypeOfControl();
		encodingLevel = marcContentTypeFieldMapper.getEncodingLevel();
		descriptiveCatalogingForm = marcContentTypeFieldMapper.getDescriptiveCatalogingForm();
		
		id = marcContentTypeFieldMapper.getId();
		contentTypeId = marcContentTypeFieldMapper.getContentType().getId();
	}
	
	
	/**
	 * Current content type id.
	 * 
	 * @return
	 */
	public Long getContentTypeId() {
		return contentTypeId;
	}

	/**
	 * Record status value.
	 * 
	 * @return
	 */
	public char getRecordStatus() {
		return recordStatus;
	}

	/**
	 * Type of record.
	 * 
	 * @return
	 */
	public char getTypeOfRecord() {
		return typeOfRecord;
	}

	/**
	 * Bibliographic level
	 * 
	 * @return
	 */
	public char getBibliographicLevel() {
		return bibliographicLevel;
	}

	/**
	 * Type of control
	 * 
	 * @return
	 */
	public char getTypeOfControl() {
		return typeOfControl;
	}

	/**
	 * Encoding level.
	 * 
	 * @return
	 */
	public char getEncodingLevel() {
		return encodingLevel;
	}

	/**
	 * Descriptive cataloging form.
	 * 
	 * @return
	 */
	public char getDescriptiveCatalogingForm() {
		return descriptiveCatalogingForm;
	}
	
	/**
	 * Field 008 
	 * 
	 * @return
	 */
	public String[] getField008() 
	{
		return field008;
	}
	
	/**
	 * Get marc 006 field.
	 * 
	 * @return
	 */
	public String[] getField006() 
	{
		return field006;
	}
	/**
	 * Get marc oo7 field.
	 * 
	 * @return
	 */
	public String[] getField007() 
	{
		return field007;
	}

	/**
	 * id of the mapping
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

}
