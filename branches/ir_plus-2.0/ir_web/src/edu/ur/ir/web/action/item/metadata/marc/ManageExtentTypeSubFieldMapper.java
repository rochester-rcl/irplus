package edu.ur.ir.web.action.item.metadata.marc;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.ExtentTypeService;
import edu.ur.ir.marc.ExtentTypeSubFieldMapper;
import edu.ur.ir.marc.ExtentTypeSubFieldMapperService;
import edu.ur.ir.marc.MarcDataFieldMapper;
import edu.ur.ir.marc.MarcDataFieldMapperService;
import edu.ur.metadata.marc.MarcSubField;
import edu.ur.metadata.marc.MarcSubFieldService;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * Helper to deal with extent type mappings.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageExtentTypeSubFieldMapper extends ActionSupport  
implements Preparable{

	
	//eclipse gerneated id
	private static final long serialVersionUID = 7821484067262840954L;

	
	//  Logger for managing copyright statements*/
	private static final Logger log = Logger.getLogger(ManageExtentTypeSubFieldMapper.class);
	
	// id of the maranger marc data field
	private Long id;
	
	// id of the data field
	private Long marcDataFieldMapperId;
	
	// parent marc data field
	private MarcDataFieldMapper marcDataFieldMapper;

	// id of the subfield to set
	private Long marcSubFieldId;
	
	// id of the extent type
	private Long extentTypeId;
	
	// mapper for the extent type
	ExtentTypeSubFieldMapper extentTypeSubFieldMapper;

	// extent type service.
	private ExtentTypeService extentTypeService;

	// service to deal with marc relator code information
	private MarcSubFieldService marcSubFieldService;
	
	// service for dealing with extent type sub field mappers
	private ExtentTypeSubFieldMapperService extentTypeSubFieldMapperService;

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
		log.debug("Save Extent Type Sub Field Mapper field alled");
		MarcSubField marcSubField = null;
		ExtentType extentType = null;
		if( marcSubFieldId != null )
		{
		    marcSubField = marcSubFieldService.getById(marcSubFieldId, false);
		}
		
		if( extentTypeId != null )
		{
			extentType = extentTypeService.getExtentType(extentTypeId, false);
		}
		
		if( marcDataFieldMapper == null || marcSubField == null || extentType == null)
		{
			 message = getText("marcDataFieldMissingDataError");
			 addFieldError("marcDataFieldMissingDataError", message);
			 return "addError";
		}
		
		 
		 if( extentTypeSubFieldMapper != null )
		 {
			
			    extentTypeSubFieldMapper.setExtentType(extentType);
			    extentTypeSubFieldMapper.setMarcSubField(marcSubField);
			    extentTypeSubFieldMapper.setPostString(postString);
			    extentTypeSubFieldMapper.setPreString(preString);
			    extentTypeSubFieldMapperService.save(extentTypeSubFieldMapper);
		 }
		 else
		 {
			    // new mapper
			    extentTypeSubFieldMapper = marcDataFieldMapper.add(extentType, marcSubField);
			    extentTypeSubFieldMapper.setPostString(postString);
			    extentTypeSubFieldMapper.setPreString(preString);
			    extentTypeSubFieldMapperService.save(extentTypeSubFieldMapper);
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
		if(extentTypeSubFieldMapper != null)
		{
			extentTypeSubFieldMapperService.delete(extentTypeSubFieldMapper);
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
			extentTypeSubFieldMapper = extentTypeSubFieldMapperService.getById(id, false);
		    marcDataFieldMapper = extentTypeSubFieldMapper.getMarcDataFieldMapper();
		}
		
		if (marcDataFieldMapperId != null)
		{
			marcDataFieldMapper = marcDataFieldMapperService.getById(marcDataFieldMapperId, false);
		}
	}
	

	
	/**
	 * Get the id for the extent type sub field.
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the id for the extent type sub field.
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
	 * Get the extent type id.
	 * 
	 * @return
	 */
	public Long getExtentTypeId() {
		return extentTypeId;
	}

	/**
	 * Set the extent type id.
	 * 
	 * @param extentTypeId
	 */
	public void setExtentTypeId(Long extentTypeId) {
		this.extentTypeId = extentTypeId;
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
	 * Get the extent type sub field mapper.
	 * 
	 * @return
	 */
	public ExtentTypeSubFieldMapper getExtentTypeSubFieldMapper() {
		return extentTypeSubFieldMapper;
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
	 * Set the extent type service.
	 * 
	 * @param extentTypeService
	 */
	public void setExtentTypeService(ExtentTypeService extentTypeService) {
		this.extentTypeService = extentTypeService;
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
	 * @param extentTypeSubFieldMapperService
	 */
	public void setExtentTypeSubFieldMapperService(
			ExtentTypeSubFieldMapperService extentTypeSubFieldMapperService) {
		this.extentTypeSubFieldMapperService = extentTypeSubFieldMapperService;
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
	 * Ascending sort of extent types by name.
	 * 
	 * @return - list of sorted name extents
	 */
	public List<ExtentType> getExtentTypes()
	{
		List<ExtentType> extentTypes = extentTypeService.getAllExtentTypes();
		Collections.sort(extentTypes, nameComparator);
		return extentTypes;
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
