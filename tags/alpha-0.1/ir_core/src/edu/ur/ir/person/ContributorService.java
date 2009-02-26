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

package edu.ur.ir.person;

import java.util.List;

/**
 * Service for dealing with contributors.
 * 
 * @author Nathan Sarr
 *
 */
public interface ContributorService {
	
	/**
	 * Get the contributor for the given personName, contributor type
	 * 
	 * @param personName - person name that we want the contributor for
	 * @param contributorType - the type of contribution that the name has made [Author/Illustrator/Editor ...]
	 * 
	 * @return - the found contributor type or null if the contributor is not found
	 */
	public Contributor get(PersonName personName, ContributorType contributorType);
	
	
	/**
	 * Get all contribution types for the given person name.
	 * 
	 * @param personName
	 * @return set of contributions for the person name
	 */
	public List<Contributor> get(PersonName personName);
	
    /**
     * Get a contributor  by id
     * 
     * @param id - unique id of the contributor .
     * @param lock - upgrade the lock on the data
     * @return - the found contributor  or null if the contributor is not found.
     */
    public Contributor getContributor(Long id, boolean lock);
	
	/**
	 * Delete the contributor from the system.
	 * 
	 * @param contributor
	 */
	public void delete(Contributor contributor);
	
	/**
	 * Save the contributor.
	 * 
	 * @param contributor
	 */
	public void save(Contributor contributor);

}
