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
package edu.ur.file.db;


import java.util.Date;

import edu.ur.persistent.LongPersistentId;
import edu.ur.persistent.PersistentVersioned;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;


/**
 * Interface for getting information about a folder in the 
 * system.
 * 
 * @author Nathan Sarr
 *
 */
public interface FolderInfo extends PathAware, LongPersistentId, PersistentVersioned,
DescriptionAware, NameAware {
	
	/**
	 * The display name to be shown to the user.
	 * 
	 * @return Returns the displayName.
	 */
	public String getDisplayName();

	/**
	 * Set the display name to be shown to the user.
	 * 
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName);

	/**
	 * Determine if this folder is in the
	 * folder system.  
	 * 
	 * @return true if the folder exists
	 */
	public boolean getExists();
	

	/**
	 * Set the description of the folder.
	 * 
	 * @param description of the folder
	 */
	public void setDescription(String description);
	
	/**
	 * The date the folder was created in the folder
	 * system.
	 * 
	 * @return createdDate
	 */
	public Date getCreatedDate();

	/**
	 * Date the folder was last modified.
	 * 
	 * @return last modified date
	 */
	public Date getModifiedDate();

	/**
	 * Set the size of the folder.
	 * 
	 * @return the size in bytes
	 */
	public Long getSize();
	


}
