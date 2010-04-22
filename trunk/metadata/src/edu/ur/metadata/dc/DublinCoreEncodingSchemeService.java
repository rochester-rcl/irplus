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

import java.util.List;

/**
 * Service for dealing with dublin core encoding schemes.
 * 
 * @author Nathan Sarr
 *
 */
public interface DublinCoreEncodingSchemeService 
{
	/**
	 * Get a count of dublin core encoding schemes.
	 * 
	 * @return the count of dublin core encoding schemes
	 */
	public Long getCount();

	/**
	 * Get all dublin core encoding schemes.
	 * 
	 * @return - all dublin core encoding schemes.
	 */
	public List<DublinCoreEncodingScheme> getAll();

	/**
	 * Get a dublin core encoding scheme by id.
	 * 
	 * @param id - id of the dublin core encoding scheme
	 * @param lock - upgrade the lock
	 * 
	 * @return the found dublin core encoding scheme
	 */
	public DublinCoreEncodingScheme getById(Long id, boolean lock);

	/**
	 * Save the dublin core encoding scheme.
	 * 
	 * @param entity - save the dublin core encoding scheme
	 */
	public void save(DublinCoreEncodingScheme entity);

	/**
	 * Delete the dublin core encoding scheme.
	 * 
	 * @param entity - the dublin core encoding scheme
	 */
	public void delete(DublinCoreEncodingScheme entity);

	/**
	 * Find a dublin core encoding scheme by its unique name.
	 * 
	 * @param name - name of the encoding scheme
	 * @return - found dublin core encoding scheme
	 */
	public DublinCoreEncodingScheme findByName(String name);

}
