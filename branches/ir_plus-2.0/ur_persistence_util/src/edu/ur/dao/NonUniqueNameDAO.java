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
 * Returns a list of Entities based on the specified
 * name
 * 
 * @author Nathan Sarr
 *
 * @param <T>
 */
public interface NonUniqueNameDAO<T> {
	
    /**
     * Find the entities by the unique name
     * 
     * @param name of the general item type find
     * @return the general item type or null if no name is found
     */
    @SuppressWarnings("unchecked")
	public List findByName(String name);
}
