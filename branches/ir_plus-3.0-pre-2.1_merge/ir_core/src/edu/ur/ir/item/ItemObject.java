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

import java.io.Serializable;

import edu.ur.order.Orderable;
import edu.ur.persistent.LongPersistentId;
import edu.ur.persistent.PersistentVersioned;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;

/**
 * Interface for the objects in the item
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ItemObject extends LongPersistentId, 
PersistentVersioned, Serializable, DescriptionAware, NameAware, Orderable{
	
	/**
	 * Get Order with respect to other item objects
	 * 
	 * @return int value
	 */
	public int getOrder() ;
	
	/**
	 * Set Order with respect to other item objects
	 * 
	 * @param order
	 */
	public void setOrder(int order) ;
	
	/**
	 * Get the type of the object
	 * 
	 * @return type
	 */
	public String getType();
	
	/**
	 * Set the description for item object
	 * 
	 * @param description
	 */
	public void setDescription(String description);

	
	

}
