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

package edu.ur.ir.user;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.file.VersionedFile;
import edu.ur.persistent.BasePersistent;

/**
 * A file that has been shared with a user this will 
 * be an in box file.  It is expected that an inbox file
 * will be moved into the users files and folders.
 * 
 * @author Nathan Sarr
 *
 */
public class SharedInboxFile extends BasePersistent implements FileSystem{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -4996109611696195354L;

	/** Versioned file to link to. */
	private VersionedFile versionedFile;
	
	/** User who's in-box this is in */
	private IrUser sharedWithUser;
	
	/** User who shared the file */
	private IrUser sharingUser;
	
	/** The file system type  */
	private FileSystemType fileSystemType = FileSystemType.SHARED_INBOX_FILE;
	
	
	/**
	 * Package protected default constructor. 
	 */
	SharedInboxFile(){}

	/**
	 * Constructor 
	 * 
	 * @param user
	 * @param versionedFile
	 */
	SharedInboxFile(IrUser sharingUser, IrUser sharedWithUser, 
			VersionedFile versionedFile)
	{
		this.sharingUser = sharingUser;
		this.versionedFile = versionedFile;
		this.sharedWithUser = sharedWithUser;
	}

	/**
	 * Get the versioned file that was shared
	 * 
	 * @return Shared version file
	 */
	public VersionedFile getVersionedFile() {
		return versionedFile;
	}

	/**
	 * Get the sharedWithUser this versioned file was shared with.
	 * 
	 * @return
	 */
	public IrUser getSharedWithUser() {
		return sharedWithUser;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int hashCode = 0;
		
		hashCode += versionedFile == null ? 0 : versionedFile.hashCode();
		hashCode += sharedWithUser == null ? 0 : sharedWithUser.hashCode();
		return hashCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof SharedInboxFile)) return false;

		final SharedInboxFile other = (SharedInboxFile) o;

		if( ( versionedFile != null && !versionedFile.equals(other.getVersionedFile()) ) ||
			( versionedFile == null && other.getVersionedFile() != null ) ) return false;

		if( ( sharedWithUser != null && !sharedWithUser.equals(other.getSharedWithUser()) ) ||
			( sharedWithUser == null && other.getSharedWithUser() != null ) ) return false;
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ Shared Inbox File id = " + id);
		sb.append(" versioned file = " + versionedFile);
		sb.append(" sharedWithUser = " + sharedWithUser);
		sb.append(" sharingUser = " + sharingUser);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * The user sharing the file
	 * 
	 * @return
	 */
	public IrUser getSharingUser() {
		return sharingUser;
	}

	
	/**
	 * Returns the file system type
	 * @see edu.ur.ir.FileSystem#getFileSystemType()
	 */
	public FileSystemType getFileSystemType() {
		return fileSystemType;
	}

	
	/**
	 * Returns the path for this inbox file.
	 * 
	 * @see edu.ur.ir.FileSystem#getPath()
	 */
	public String getPath() {
		return "/sharedInbox/" + sharedWithUser.getUsername() + "/" + versionedFile.getNameWithExtension();
	}
	
	/**
	 * Get the description
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return versionedFile.getDescription();
	}
	
	/**
	 * Returns the versioned file name.
	 * 
	 * @see edu.ur.simple.type.NameAware#getName()
	 */
	public String getName() {
		return versionedFile.getNameWithExtension();
	}
}
