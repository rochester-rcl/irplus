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

/**
 * File database information for a default file database.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultFileDatabaseInfo implements FileDatabaseInfo{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 4883952283836066725L;
	
	/** Server id for the file database to be created for */
	private Long fileServerId;
	
	/** display name to be given to the file database  */
	private String fileDatabaseDisplayName;
	
	/** Folder name to be used on the file system. */
	private String fileSystemFolderName;
	
	/** path to create the file system folder name.  */
	private String path;
	
	/** desciption of the file database.  */
	private String description;
	
	/** Name of the default folder to create.  */
	private String defaultFolderDisplayName;
	
	/** unique name to give to the default folder on the file system */
	private String defaultFolderUniqueName;

	/**
	 * Default constructor for default file database information.  These are the required
	 * fields.
	 * 
	 * @param fileServerId - id of the file server to create the file database with
	 * @param fileDatabaseDisplayName - display name of the file database
	 * @param fileSystemFolderName - the name on the file system to create the file database with
	 * @param path - path of where to creat the folder
	 * @param defaultFolderDisplayName - display name of the default file database folder
	 * @param defaultFolderUniqueName - unique name of the default file folder.
	 */
	public DefaultFileDatabaseInfo(Long fileServerId,
			String fileDatabaseDisplayName,
			String fileSystemFolderName, 
			String path, 
			String defaultFolderDisplayName,
			String defaultFolderUniqueName)
	{
		setFileServerId(fileServerId);
		setFileSystemFolderName(fileSystemFolderName);
		setFileDatabaseDisplayName(fileDatabaseDisplayName);
		setPath(path);
		setDefaultFolderDisplayName(defaultFolderDisplayName);
		setDefaultFolderUniqueName(defaultFolderUniqueName);
	}
	
	/**
	 * Get the description information.
	 * 
	 * @return description information
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of this file database.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the display name.
	 * 
	 * @return
	 */
	public String getFileDatabaseDisplayName() {
		return fileDatabaseDisplayName;
	}

	/**
	 * Set the display name.
	 * 
	 * @param displayName
	 */
	public void setFileDatabaseDisplayName(String displayName) {
		this.fileDatabaseDisplayName = displayName;
	}

	/**
	 * Get the file server id.
	 * 
	 * @return
	 */
	public Long getFileServerId() {
		return fileServerId;
	}

	/**
	 * Set the file server id.
	 * 
	 * @param fileServerId
	 */
	public void setFileServerId(Long fileServerId) {
		this.fileServerId = fileServerId;
	}

	/**
	 * Get the file system folder name.
	 * 
	 * @return
	 */
	public String getFileSystemFolderName() {
		return fileSystemFolderName;
	}

	/**
	 * Set the file system folder name.
	 * 
	 * @param fileSystemFolderName
	 */
	public void setFileSystemFolderName(String fileSystemFolderName) {
		this.fileSystemFolderName = fileSystemFolderName;
	}

	/**
	 * Get the path.
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set the path.
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}


	/**
	 * Unique name of the folder.
	 * 
	 * @return
	 */
	public String getDefaultFolderUniqueName() {
		return defaultFolderUniqueName;
	}

	/**
	 * Default folder unique name.
	 * 
	 * @param defaultFolderUniqueName
	 */
	public void setDefaultFolderUniqueName(String defaultFolderUniqueName) {
		this.defaultFolderUniqueName = defaultFolderUniqueName;
	}

	/**
	 * Folder name to be shown to users.
	 * 
	 * @return
	 */
	public String getDefaultFolderDisplayName() {
		return defaultFolderDisplayName;
	}

	/**
	 * Folder name to be shown to users.
	 * 
	 * @param defaultFolderDisplayName
	 */
	public void setDefaultFolderDisplayName(String defaultFolderDisplayName) {
		this.defaultFolderDisplayName = defaultFolderDisplayName;
	}

}
