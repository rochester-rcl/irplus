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

package edu.ur.metadata.dc;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a service class for dublin core term information.
 * 
 * @author Nathan Sarr
 *
 */
public interface DublinCoreTermService  extends Serializable
{
	
	/**
	 * Get a count of dublin core terms.
	 * 
	 * @return the count of dublin core terms
	 */
	public Long getCount();

	/**
	 * Get all dublin core terms.
	 * 
	 * @return - all dublin core terms.
	 */
	public List<DublinCoreTerm> getAll();

	/**
	 * Get a dublin core term by id.
	 * 
	 * @param id - id of the dublin core term
	 * @param lock - upgrade the lock
	 * 
	 * @return the found dublin core term
	 */
	public DublinCoreTerm getById(Long id, boolean lock);

	/**
	 * Save the dublin core term.
	 * 
	 * @param entity - save the dublin core term
	 */
	public void save(DublinCoreTerm entity);

	/**
	 * Delete the dublin core term.
	 * 
	 * @param entity - the dublin core term
	 */
	public void delete(DublinCoreTerm entity);

	/**
	 * Find a dublin core term by its unique name.
	 * 
	 * @param name - name of the term
	 * @return - found dublin core term
	 */
	public DublinCoreTerm findByName(String name);

}
