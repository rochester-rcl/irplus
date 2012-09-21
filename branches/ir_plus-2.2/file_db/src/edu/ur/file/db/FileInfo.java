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

import java.io.Serializable;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Set;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.checksum.ChecksumCalculator;
import edu.ur.persistent.LongPersistentId;
import edu.ur.persistent.PersistentVersioned;

/**
 * Represents information for a file in the file system.
 * 
 * @author Nathan Sarr
 */
public interface FileInfo extends Serializable, PathAware,
LongPersistentId, PersistentVersioned{
	
	/**
	 * Get the file name used in the file system
	 * for this file.
	 * 
	 * @return the name of the file
	 */
	public String getName();
	
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
	 * @throws IllegalFileSystemNameException 
	 */
	public void setDisplayName(String displayName) throws IllegalFileSystemNameException;

	/**
	 * Determine if this file is in the
	 * file system.  
	 * 
	 * @return true if the file exists
	 */
	public boolean getExists();
	
	/**
	 * Determine if the file is in the
	 * file system.
	 * 
	 * @return true if the file is in 
	 * the file system.
	 */
	public boolean exists();
	
	/**
	 * Description of the file
	 * 
	 * @return the files description
	 */
	public String getDescription();

	/**
	 * Set the description of the file.
	 * 
	 * @param description of the file
	 */
	public void setDescription(String description);
	
	/**
	 * The date the file was created in the file
	 * system.
	 * 
	 * @return createdDate
	 */
	public Timestamp getCreatedDate();

	/**
	 * Most recent Checksum information for the file info - there should only be one checksum 
	 * per algorithm type in this list
	 * 
	 * @return checksum
	 */
	public Set<FileInfoChecksum> getFileInfoChecksums();
	
	/**
	 * Add the checksum to the file - using the specified calculator.
	 * 
	 * @param fileInfoChecksum
	 */
	public void addFileInfoChecksum(ChecksumCalculator checksumCalculator);
	
	/**
	 * Return the checksum for the algorithm type or null if none found.
	 * 
	 * @param algorithmType
	 */
	public FileInfoChecksum getFileInfoChecksum(String algorithmType);

	/**
	 * Date the file was last modified.
	 * 
	 * @return last modified date
	 */
	public Timestamp getModifiedDate();

	/**
	 * Set the size of the file.
	 * 
	 * @return the size in bytes
	 */
	public Long getSize();

	/**
	 * Get the file extension
	 * 
	 * @return the file extension
	 */
	public String getExtension();

	/**
	 * Set the file extension.
	 * 
	 * @param extension
	 */
	public void setExtension(String extension);
	
	/**
	 * Get the uri of this file.
	 * 
	 * @return the URI
	 */
	public URI getUri();
	
	/**
	 * Check the size of the file and update it.
	 * 
	 */
	public void setSize(Long size);
	
	
	/**
	 * Returns the file name with the extension in the
	 * format  name.extension.  If there is no extension
	 * only the name is returned.
	 * 
	 * @return
	 */
	public String getNameWithExtension();
	
}
