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
 * Basic (C)reat (R)ead (U)pdate (D)elete interface for
 * entities.
 * 
 * @author Nathan Sarr
 *
 */
public interface CrudDAO<T> {
    
    /**
     * Get the Entity by id.  Use this method 
     * if you are unsure that the object exists
     * 
     * @param id the unique if of the general item type
     * @param lock if true upgrades the lock
     * @return the general item type found or null
     */
     public T getById(Long id, boolean lock);

    /**
     * Make the entity persistent
     * 
     * @param entity the Work item type to persist
     */
     public void makePersistent(T entity);

    /**
     * Delete the entity form persistent storage
     * 
     * @param entity the entity to save
     */
     public void makeTransient(T entity);
    
    /**
     * Get all of the existing Entities
     * 
     * @return all of the entities the list is unordered.
     */
     @SuppressWarnings("unchecked")
	public List getAll();
}
