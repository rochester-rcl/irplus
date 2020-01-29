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
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.metadata.marc.MarcSubField;
import edu.ur.metadata.marc.MarcSubFieldService;
import edu.ur.simple.type.AscendingNameComparator;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeService;
import edu.ur.ir.item.metadata.marc.IdentifierTypeSubFieldMapper;
import edu.ur.ir.item.metadata.marc.IdentifierTypeSubFieldMapperService;
import edu.ur.ir.item.metadata.marc.MarcDataFieldMapper;
import edu.ur.ir.item.metadata.marc.MarcDataFieldMapperService;

/**
 * Allow a user to manage the identifier type sub filed mappings.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageIdentifierTypeSubFieldMapper extends ActionSupport  
implements Preparable{

	
	//eclipse gerneated id
	private static final long serialVersionUID = -1906137285921347780L;

	//  Logger for managing copyright statements*/
	private static final Logger log = LogManager.getLogger(ManageIdentifierTypeSubFieldMapper.class);
	
	// id of the maranger marc data field
	private Long id;
	
	// id of the data field
	private Long marcDataFieldMapperId;
	
	// parent marc data field
	private MarcDataFieldMapper marcDataFieldMapper;

	// id of the subfield to set
	private Long marcSubFieldId;
	
	// id of the identifier type
	private Long identifierTypeId;
	
	// mapper for the identifier type
	IdentifierTypeSubFieldMapper identifierTypeSubFieldMapper;

	// identifier type service.
	private IdentifierTypeService identifierTypeService;

	// service to deal with marc relator code information
	private MarcSubFieldService marcSubFieldService;
	
	// service for dealing with identifier type sub field mappers
	private IdentifierTypeSubFieldMapperService identifierTypeSubFieldMapperService;

    // Message that can be displayed to the user. */
	private String message;

	// service to deal with marc data field information
	private MarcDataFieldMapperService marcDataFieldMapperService;

	//pre String value
	private String preString;

	//post String value
	private String postString;

	// Used for sorting name based entities 
	private AscendingNameComparator nameComparator = new AscendingNameComparator();
	/**
	 * Method to create a new marc data field.
	 * 
	 * Create a new copyright statement
	 */
	public String save()
	{
		log.debug("Save Identifier Type Sub Field Mapper field alled");
		MarcSubField marcSubField = null;
		IdentifierType identifierType = null;
		if( marcSubFieldId != null )
		{
		    marcSubField = marcSubFieldService.getById(marcSubFieldId, false);
		}
		
		if( identifierTypeId != null )
		{
			identifierType = identifierTypeService.get(identifierTypeId, false);
		}
		
		if( marcDataFieldMapper == null || marcSubField == null || identifierType == null)
		{
			 message = getText("marcDataFieldMissingDataError");
			 addFieldError("marcDataFieldMissingDataError", message);
			 return "addError";
		}
		
		 
		 if( identifierTypeSubFieldMapper != null )
		 {
			
			    identifierTypeSubFieldMapper.setIdentifierType(identifierType);
			    identifierTypeSubFieldMapper.setMarcSubField(marcSubField);
			    identifierTypeSubFieldMapperService.save(identifierTypeSubFieldMapper);
		 }
		 else
		 {
			    // new mapper
			    identifierTypeSubFieldMapper = marcDataFieldMapper.add(identifierType, marcSubField);
			    identifierTypeSubFieldMapperService.save(identifierTypeSubFieldMapper);
		 }
		

		return "added";
	}
	
	/**
	 * Delete the contributor type code.
	 * 
	 * @return
	 */
	public String delete()
	{
		if(identifierTypeSubFieldMapper != null)
		{
			identifierTypeSubFieldMapperService.delete(identifierTypeSubFieldMapper);
		}
		
		return "deleted";
	}
	
	/**
	 * Edit the selected element.
	 * 
	 * @return
	 */
	public String edit()
	{
		
		return "edit";
	}
	

	
	public String execute()
	{
	    return SUCCESS;
	}

	
	public void prepare() throws Exception {
		if( id != null)
		{
			identifierTypeSubFieldMapper = identifierTypeSubFieldMapperService.getById(id, false);
		    marcDataFieldMapper = identifierTypeSubFieldMapper.getMarcDataFieldMapper();
		}
		
		if (marcDataFieldMapperId != null)
		{
			marcDataFieldMapper = marcDataFieldMapperService.getById(marcDataFieldMapperId, false);
		}
	}
	

	
	/**
	 * Get the id for the identifier type sub field.
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the id for the identifier type sub field.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the marc data field mapper id.
	 * 
	 * @return
	 */
	public Long getMarcDataFieldMapperId() {
		return marcDataFieldMapperId;
	}

	/**
	 * Set the marc data field mapper id.
	 * 
	 * @param marcDataFieldMapperId
	 */
	public void setMarcDataFieldMapperId(Long marcDataFieldMapperId) {
		this.marcDataFieldMapperId = marcDataFieldMapperId;
	}

	/**
	 * Get the marc sub field id.
	 * 
	 * @return
	 */
	public Long getMarcSubFieldId() {
		return marcSubFieldId;
	}

	/**
	 * Set the marc sub field id.
	 * 
	 * @param marcSubFieldId
	 */
	public void setMarcSubFieldId(Long marcSubFieldId) {
		this.marcSubFieldId = marcSubFieldId;
	}

	/**
	 * Get the identifier type id.
	 * 
	 * @return
	 */
	public Long getIdentifierTypeId() {
		return identifierTypeId;
	}

	/**
	 * Set the identifier type id.
	 * 
	 * @param identifierTypeId
	 */
	public void setIdentifierTypeId(Long identifierTypeId) {
		this.identifierTypeId = identifierTypeId;
	}

	/**
	 * Get the pre string.
	 * 
	 * @return
	 */
	public String getPreString() {
		return preString;
	}

	/**
	 * Set the pre string.
	 * 
	 * @param preString
	 */
	public void setPreString(String preString) {
		this.preString = preString;
	}

	/**
	 * Get the post string.
	 * 
	 * @return
	 */
	public String getPostString() {
		return postString;
	}

	/**
	 * Set the post string.
	 * 
	 * @param postString
	 */
	public void setPostString(String postString) {
		this.postString = postString;
	}

	/**
	 * Get the identifier type sub field mapper.
	 * 
	 * @return
	 */
	public IdentifierTypeSubFieldMapper getIdentifierTypeSubFieldMapper() {
		return identifierTypeSubFieldMapper;
	}

	/**
	 * Get the message.
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set the identifier type service.
	 * 
	 * @param identifierTypeService
	 */
	public void setIdentifierTypeService(IdentifierTypeService identifierTypeService) {
		this.identifierTypeService = identifierTypeService;
	}

	/**
	 * Set the marc sub field service.
	 * 
	 * @param marcSubFieldService
	 */
	public void setMarcSubFieldService(MarcSubFieldService marcSubFieldService) {
		this.marcSubFieldService = marcSubFieldService;
	}

	/**
	 * Set the idntifier type sub field mapper service.
	 * 
	 * @param identifierTypeSubFieldMapperService
	 */
	public void setIdentifierTypeSubFieldMapperService(
			IdentifierTypeSubFieldMapperService identifierTypeSubFieldMapperService) {
		this.identifierTypeSubFieldMapperService = identifierTypeSubFieldMapperService;
	}

	/**
	 * Set the amrc data field mapper service.
	 * 
	 * @param marcDataFieldMapperService
	 */
	public void setMarcDataFieldMapperService(
			MarcDataFieldMapperService marcDataFieldMapperService) {
		this.marcDataFieldMapperService = marcDataFieldMapperService;
	}
	
	
	/**
	 * Get the marc data field mapper.
	 * 
	 * @return
	 */
	public MarcDataFieldMapper getMarcDataFieldMapper() {
		return marcDataFieldMapper;
	}
	
	/**
	 * Ascending sort of identifier types by name.
	 * 
	 * @return - list of sorted name identifiers
	 */
	public List<IdentifierType> getIdentifierTypes()
	{
		List<IdentifierType> identifierTypes = identifierTypeService.getAll();
		Collections.sort(identifierTypes, nameComparator);
		return identifierTypes;
	}
	
	/**
	 * Get the list of subfields.
	 * 
	 * @return - list of subfields
	 */
	public List<MarcSubField> getMarcSubFields()
	{
		List<MarcSubField> subFields = marcSubFieldService.getAll();
		Collections.sort(subFields, nameComparator);
		return subFields;
	}

}
