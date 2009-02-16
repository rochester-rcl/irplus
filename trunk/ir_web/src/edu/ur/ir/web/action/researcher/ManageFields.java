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


package edu.ur.ir.web.action.researcher;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.researcher.Field;
import edu.ur.ir.researcher.FieldService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

/**
 * Action to deal with fields.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManageFields extends Pager implements Preparable, UserIdAware {
	
	/** generated version id. */
	private static final long serialVersionUID = -3229962214403823020L;
	
	/** field service */
	private FieldService fieldService;
	
	/**  Logger for managing fields*/
	private static final Logger log = Logger.getLogger(ManageFields.class);
	
	/** Set of fields for viewing the fields */
	private Collection<Field> fields;
	
	/** service for users */
	private UserService userService;
	
	/**  ResearcherField for loading  */
	private Field field;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the field has been added*/
	private boolean added = false;
	
	/** Indicates the fields have been deleted */
	private boolean deleted = false;
	
	/** id of the field  */
	private Long id;
	
	/** id of the user making the change */
	private Long userId;
	
	/** Set of field ids */
	private long[] fieldIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of fields */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public  ManageFields() 
	{
		numberOfResultsToShow = 50;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new field.
	 * 
	 * Create a new field
	 */
	public String create()
	{
		log.debug("creating a field = " + field.getName());
		IrUser user = userService.getUser(userId, false);
		if(!user.hasRole(IrRole.RESEARCHER_ROLE) || !user.hasRole(IrRole.ADMIN_ROLE) )
		{
			return "accessDenied";
		}
		
		added = false;
		Field other = fieldService.getField(field.getName());
		if( other == null)
		{
		    fieldService.makeFieldPersistent(field);
		    added = true;
		}
		else
		{
			message = getText("fieldAlreadyExists", 
					new String[]{field.getName()});
			addFieldError("fieldAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Method to update an existing field.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing field id = " + field.getId());
		added = false;

		Field other = fieldService.getField(field.getName());
		
		if( other == null || other.getId().equals(field.getId()))
		{
			fieldService.makeFieldPersistent(field);
			added = true;
		}
		else
		{
			message = getText("fieldAlreadyExists", 
					new String[]{field.getName()});
			
			addFieldError("fieldAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete fields called");
		if( fieldIds != null )
		{
		    for(int index = 0; index < fieldIds.length; index++)
		    {
			    log.debug("Deleting field with id " + fieldIds[index]);
			    fieldService.deleteField(fieldService.getField(fieldIds[index], false));
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Get the fields
	 */
	public String get()
	{
		field = fieldService.getField(id, false);
		return "get";
	}
 

	
	/**
	 * Get the fields table data.
	 * 
	 * @return
	 */
	public String viewFields()
	{

		rowEnd = rowStart + numberOfResultsToShow;
	    
		fields = fieldService.getFieldsOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = fieldService.getFieldCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	}

	/**
	 * Get the field service.
	 * 
	 * @return
	 */
	public FieldService getFieldService() {
		return fieldService;
	}

	/**
	 * Set the field service.
	 * 
	 * @param fieldService
	 */
	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}
	
	/**
	 * List of fields for display.
	 * 
	 * @return
	 */
	public Collection<Field> getFields() {
		return fields;
	}
	/**
	 * Set the list of fields.
	 * 
	 * @param fields
	 */
	public void setFields(Collection<Field> fields) {
		this.fields = fields;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isAdded() {
		return added;
	}
	public void setAdded(boolean added) {
		this.added = added;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long[] getFieldIds() {
		return fieldIds;
	}

	public void setFieldIds(long[] fieldIds) {
		this.fieldIds = fieldIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public void prepare() throws Exception {
		if( id != null)
		{
			field = fieldService.getField(id, false);
		}
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
