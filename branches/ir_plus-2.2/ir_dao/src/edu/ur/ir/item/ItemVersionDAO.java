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

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;

public interface ItemVersionDAO extends CountableDAO, CrudDAO<ItemVersion>{
	
	/**
	 * Get the list of Item Versions that has the specified IrFile
	 * 	
	 * @param irFileId Id of the IrFile
	 * @return list of ItemVersions.
	 */
	public List<ItemVersion> getItemVersionContainingIrFile(Long irFileId);

	/**
	 * Get the list of ItemVersions that has this specified GenericItem
	 * 	
	 * @param itemId Id of GenericItem
	 * @return Count of ItemVersion.
	 */
	public Long getItemVersionCount(Long itemId) ;
}
