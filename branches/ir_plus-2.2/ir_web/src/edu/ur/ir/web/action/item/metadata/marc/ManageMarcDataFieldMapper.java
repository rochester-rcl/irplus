package edu.ur.ir.web.action.item.metadata.marc;



import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.item.metadata.marc.MarcDataFieldMapper;
import edu.ur.ir.item.metadata.marc.MarcDataFieldMapperComparator;
import edu.ur.ir.item.metadata.marc.MarcDataFieldMapperService;
import edu.ur.metadata.marc.MarcDataFieldService;
import edu.ur.metadata.marc.MarcDataField;

/**
 * Manages creating and editing marc data field mappings.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageMarcDataFieldMapper extends ActionSupport  
    implements Preparable, Comparator<MarcDataField>{
	
	//eclipse generated id
	private static final long serialVersionUID = -3190077043329308504L;
	
	//  Logger for managing copyright statements*/
	private static final Logger log = Logger.getLogger(ManageMarcDataFieldMapper.class);
	
	// id of the maranger marc data field
	private Long id;
	
	// id of the data field
	private Long dataFieldId;

	// current marc contributor type relator code
    private MarcDataFieldMapper marcDataFieldMapper;

	// service to deal with marc relator code information
	private MarcDataFieldService marcDataFieldService;

    // Message that can be displayed to the user. */
	private String message;

	// service to deal with marc data field information
	private MarcDataFieldMapperService marcDataFieldMapperService;

	//indicator 1
	private String indicator1;

	//indicator 2
	private String indicator2;

	

	// sorting of marc data field mappers
	private MarcDataFieldMapperComparator marcDataFieldMapperComparator = new MarcDataFieldMapperComparator();

	/**
	 * Method to create a new marc data field.
	 * 
	 * Create a new copyright statement
	 */
	public String save()
	{
		log.debug("Save marc data field mapper called");
		MarcDataField marcDataField = null;
		if( dataFieldId != null )
		{
		    marcDataField = marcDataFieldService.getById(dataFieldId, false);
		}
		
		if( marcDataField == null )
		{
			 message = getText("marcDataFieldMissingDataError");
			 addFieldError("marcDataFieldMissingDataError", message);
			 return "addError";
		}
		
		MarcDataFieldMapper other = marcDataFieldMapperService.getByDataFieldIndicatorsId(dataFieldId, indicator1, indicator2);
		if( marcDataFieldMapper != null )
		{
			if(other == null ||  marcDataFieldMapper.getId().equals(other.getId()))
			{
				marcDataFieldMapper.setMarcDataField(marcDataField);
				marcDataFieldMapper.setIndicator1(indicator1);
				marcDataFieldMapper.setIndicator2(indicator2);
				marcDataFieldMapperService.save(marcDataFieldMapper);
			}
			else
			{
				message = getText("marcDataFieldMapperExistsError");
				addFieldError("marcDataFieldMapperExists", message);
				return "addError";
			}
		}
		else
		{
			// new contributor type relator code
			if( other != null )
			{
				message = getText("marcDataFieldMapperExistsError");
				addFieldError("marcDataFieldMapperExists", message);
				return "addError";
			}
			else
			{
				marcDataFieldMapper = new MarcDataFieldMapper(marcDataField);
				marcDataFieldMapper.setIndicator1(indicator1);
				marcDataFieldMapper.setIndicator2(indicator2);
				marcDataFieldMapperService.save(marcDataFieldMapper);
			}
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
		if(marcDataFieldMapper != null )
		{
			marcDataFieldMapperService.delete(marcDataFieldMapper);
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
		if( marcDataFieldMapper != null )
		{
		    indicator1 = marcDataFieldMapper.getIndicator1();
		    indicator2 = marcDataFieldMapper.getIndicator2();
		}
		return "edit";
	}
	

	
	public String execute()
	{
	    return SUCCESS;
	}
	
	/**
	 * Get the list of marc data fields
	 * 
	 * @return
	 */
	public List<MarcDataField> getMarcDataFields()
	{
		List<MarcDataField> dataFields = marcDataFieldService.getAll();
		Collections.sort(dataFields, this);
		return dataFields;
	}
	
	/**
	 * Get the list of marc data field mappers.
	 * 
	 * @return
	 */
	public List<MarcDataFieldMapper> getMarcDataFieldMappers()
	{
		List<MarcDataFieldMapper> mappers = marcDataFieldMapperService.getAll();
		Collections.sort(mappers, marcDataFieldMapperComparator);
		return mappers;
	}
	
	/**
	 * Set the MARC data field service.
	 * 
	 * @param marcDataFieldService
	 */
	public void setMarcDataFieldService(MarcDataFieldService marcDataFieldService) {
		this.marcDataFieldService = marcDataFieldService;
	}

	
	/**
	 * compare the data fields by code.
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(MarcDataField o1, MarcDataField o2) {
		return o1.getCode().compareToIgnoreCase(o2.getCode());
	}

	/**
	 * Set the marc data field mapper service.
	 * 
	 * @param marcDataFieldMapperService
	 */
	public void setMarcDataFieldMapperService(
			MarcDataFieldMapperService marcDataFieldMapperService) {
		this.marcDataFieldMapperService = marcDataFieldMapperService;
	}

	@Override
	public void prepare() throws Exception {
		if( id != null )
		{
			marcDataFieldMapper = marcDataFieldMapperService.getById(id, false);
		}
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
	 * Get indicator 1.
	 * 
	 * @return
	 */
	public String getIndicator1() {
		return indicator1;
	}

	/**
	 * Set indicator 1.
	 * 
	 * @param indicator1
	 */
	public void setIndicator1(String indicator1) {
		this.indicator1 = indicator1;
	}

	/**
	 * Get indicator 2.
	 * 
	 * @return
	 */
	public String getIndicator2() {
		return indicator2;
	}

	/**
	 * Set indicator 2.
	 * 
	 * @param indicator2
	 */
	public void setIndicator2(String indicator2) {
		this.indicator2 = indicator2;
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
	 * Set the id.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Set the data field id.
	 * 
	 * @param dataFieldId
	 */
	public void setDataFieldId(Long dataFieldId) {
		this.dataFieldId = dataFieldId;
	}
}
