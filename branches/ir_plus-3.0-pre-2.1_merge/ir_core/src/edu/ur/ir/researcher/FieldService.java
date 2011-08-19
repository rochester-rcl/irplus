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

package edu.ur.ir.researcher;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for managing with  field information.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface FieldService extends Serializable{
	
	/**
	 * Get a count of the  fields in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getFieldCount();
	
	/**
	 * Get all  fields in name order.
	 */
	public List<Field> getAllFieldsNameOrder();

	/**
	 * Get all  fields in name order.
	 */
	public List<Field> getAllFieldsOrderByName(int startRecord, int numRecords);

	/**
	 * 
	 * @param name - name of the Field.
	 * @return the Field or null if no Field was found.
	 */
	public Field getField(String name);
	
	
	/**
	 * Get the list of  fields. 
	 * 
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * 
	 * @return List of users for the specified information.
	 */
	public List<Field> getFieldsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

	/**
	 * Get a Field by id.
	 * 
	 * @param id - id of the Field
	 * @param lock - lock mode
	 *  
	 * @return the Field if found.
	 */
	public Field getField(Long id, boolean lock);

	/**
	 * Make the Field information persistent.
	 * 
	 * @param Field to add/save
	 */
	public void makeFieldPersistent(Field entity);

	/**
	 * Delete a Field from the system
	 * 
	 * @param entity - field to delete
	 */
	public void deleteField(Field entity);
	
	/**
	 * Delete a set of  fields from the system
	 * 
	 * @param fields to delete
	 */
	public void deleteFields(List<Field> fields);

}
