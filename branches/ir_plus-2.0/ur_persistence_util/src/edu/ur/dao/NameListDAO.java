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


package edu.ur.dao;

import java.util.List;

/**
 * Basic DAO for Entities that can be listed by their name
 * 
 * @author Nathan Sarr
 *
 */
public interface NameListDAO {
  
	/**
     * Get all Entites ordered by name
     * 
     * @return all of the entities listed by name
     */
    @SuppressWarnings("unchecked")
	public List getAllNameOrder();
    
    /**
     * Get all Entities starting at the start record and get up to 
     * the endRecords - it will be ordered by name
     *  
     * @param startRecord - the index to start at
     * @param numRecords - the number of records to get
     * @return the records found
     */
    @SuppressWarnings("unchecked")
	public List getAllOrderByName(int startRecord, int numRecords);

}
