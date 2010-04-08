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


package edu.ur.ir.researcher.service;

import java.util.List;

import edu.ur.ir.researcher.Field;
import edu.ur.ir.researcher.FieldDAO;
import edu.ur.ir.researcher.FieldService;

/**
 * Service for fields
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultFieldService implements FieldService{
	
	/** Field data access object */
	private FieldDAO fieldDAO;
	
	/**
	 * Get the field by unique name.
	 * 
	 * @see edu.ur.ir.researcher.Field#getField(java.lang.String)
	 */
	public Field getField(String name) {
		return fieldDAO.findByUniqueName(name);
	}

	/**
	 * Get all fields.
	 * 
	 * @see edu.ur.ir.researcher.Field#getAllFields()
	 */
	@SuppressWarnings("unchecked")
	public List<Field> getAllFields() {
		return fieldDAO.getAll();
	}

	/** 
	 * Get all fields ordered by name.
	 * 
	 * @see edu.ur.ir.researcher.Field#getAllFieldsNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<Field> getAllFieldsNameOrder() {
		return fieldDAO.getAllNameOrder();
	}


	/**
	 * Get all fields within the specified range.
	 * 
	 * @see edu.ur.ir.researcher.Field#getAllFieldsOrderByName(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<Field> getAllFieldsOrderByName(int startRecord, int numRecords) {
		return fieldDAO.getAllOrderByName(startRecord, numRecords);
	}

	/**
	 * Get the field with the specified id.
	 * 
	 * @see edu.ur.ir.researcher.Field#getField(java.lang.Long, boolean)
	 */
	public Field getField(Long id, boolean lock) {
		return fieldDAO.getById(id, lock);
	}

	/**
	 * Save the field
	 * 
	 * @see edu.ur.ir.researcher.Field#makeFieldPersistent(edu.ur.ir.user.Field)
	 */
	public void makeFieldPersistent(Field entity) {
		fieldDAO.makePersistent(entity);
	}

	/**
	 * Delete the field.
	 * 
	 * @see edu.ur.ir.researcher.Field#deleteField(edu.ur.ir.user.Field)
	 */
	public void deleteField(Field entity) {
		fieldDAO.makeTransient(entity);
	}

	/**
	 * Get the field data access object.
	 * 
	 * @return
	 */
	public FieldDAO getFieldDAO() {
		return fieldDAO;
	}

	/**
	 * Set the field data access object.
	 * 
	 * @param fieldDAO
	 */
	public void setFieldDAO(FieldDAO fieldDAO) {
		this.fieldDAO = fieldDAO;
	}

	/**
	 * Get the field count.
	 * 
	 * @see edu.ur.ir.researcher.Field#getFieldCount()
	 */
	public Long getFieldCount() {
		return fieldDAO.getCount();
	}

	/**
	 * Get the fields based on the specified criteria, start and end position.
	 * 
	 * @see edu.ur.ir.researcher.Field#getFields(java.util.List, int, int)
	 */
	public List<Field> getFieldsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return fieldDAO.getFields(rowStart, numberOfResultsToShow, sortType);
	}

	/**
	 * Delete the fields.
	 * 
	 * @see edu.ur.ir.researcher.Field#deleteFields(java.util.List)
	 */
	public void deleteFields(List<Field> researcherFields) {
		for(Field researcherField : researcherFields)
		{
			fieldDAO.makeTransient(researcherField);
		}
		
	}


}
