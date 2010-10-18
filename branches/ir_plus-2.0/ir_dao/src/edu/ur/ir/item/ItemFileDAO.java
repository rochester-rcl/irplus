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

package edu.ur.ir.item;

import edu.ur.dao.CrudDAO;
import edu.ur.ir.item.ItemFile;


/**
 * Interface for saving Item files.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ItemFileDAO extends CrudDAO<ItemFile> {
	
	/**
	 * Get a count of the item files containing this irFile
	 * 
	 * @param irFileId Id of the irFile
	 * 
	 * @return Count of item files containing this ir file
	 */
	public Long getItemFileCount(Long irFileId);
}

