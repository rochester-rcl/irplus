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

package edu.ur.metadata.marc;

import java.io.Serializable;
import java.util.List;

/**
 * Relator code serivce.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcRelatorCodeService extends Serializable{
	
	/**
	 * Get a count of MARC relator codes.
	 * 
	 * @return the count of MARC relator codes
	 */
	public Long getCount();

	/**
	 * Get all MARC relator codes.
	 * 
	 * @return - all MARC marc relator codes.
	 */
	public List<MarcRelatorCode> getAll();

	/**
	 * Get a MARC relator code by id.
	 * 
	 * @param id - id of the MARC relator code
	 * @param lock - upgrade the lock
	 * 
	 * @return the found MARC relator code
	 */
	public MarcRelatorCode getById(Long id, boolean lock);

	/**
	 * Save the MARC relator code.
	 * 
	 * @param entity - save the MARC relator code
	 */
	public void save(MarcRelatorCode entity);

	/**
	 * Delete the MARC relator code.
	 * 
	 * @param entity - the MARC relator code
	 */
	public void delete(MarcRelatorCode entity);

	/**
	 * Find a MARC relator code by its unique name.
	 * 
	 * @param name - name of the marc relator code
	 * @return - found MARC relator code
	 */
	public MarcRelatorCode findByName(String name);

}
