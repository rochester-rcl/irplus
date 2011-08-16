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
 * Links a personal folder, versioned file and the owning user together
 * If the personal folder is null this indicates the file is at the root
 * or held by the user 
 * 
 * @author Nathan Sarr
 *
 */
public class PersonalFile extends BasePersistent implements FileSystem{
	
	/**Eclipse generated id. */
	private static final long serialVersionUID = -5268786460100546815L;

	/** Versioned file to link to. */
	private VersionedFile versionedFile;

	/**  Parent Personal folder for this file information */
	private PersonalFolder personalFolder;
	
	/** The file system type  */
	private FileSystemType fileSystemType = FileSystemType.PERSONAL_FILE;
	
	/** User who owns the personal file */
	private IrUser owner;
	
	/**
	 * Default constructor
	 */
	PersonalFile(){}
	
	/**
	 * Create a personal file.
	 * 
	 * @param owner - owner of the personal file.
	 * @param versionedFile - versioned file.
	 */
	PersonalFile(IrUser owner, VersionedFile versionedFile)
	{
		setOwner(owner);
		setVersionedFile(versionedFile);
	}
	
	/**
	 * Create a personal file with the specified versioned file and folder.
	 * 
	 * @param owner - owner of the personal file
	 * @param versionedFile - versioned file this personal file wraps
	 * @param personalFolder - personal folder that owns the personal file
	 */
	PersonalFile(IrUser owner, VersionedFile versionedFile, PersonalFolder personalFolder)
	{
		setOwner(owner);
		setVersionedFile(versionedFile);
		setPersonalFolder(personalFolder);
	}

	/**
	 * Get the parent personal folder for this file.
	 * 
	 * @return
	 */
	public PersonalFolder getPersonalFolder() {
		return personalFolder;
	}

	/**
	 * Set the personal folder.
	 * 
	 * @param personalFolder
	 */
	void setPersonalFolder(PersonalFolder personalFolder) {
		this.personalFolder = personalFolder;
	}

	/**
	 * Get the versioned file.
	 * 
	 * @return
	 */
	public VersionedFile getVersionedFile() {
		return versionedFile;
	}

	/**
	 * Set the versioned file.
	 * 
	 * @param versionedFile
	 */
	void setVersionedFile(VersionedFile versionedFile) {
		this.versionedFile = versionedFile;
	}

	/**
	 * Hashcode method.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int hashCode = 0;
		hashCode += getName() == null ? 0 : getName().hashCode();
		hashCode += owner == null ? 0 : owner.hashCode();
		hashCode += personalFolder == null ? 0 : personalFolder.hashCode();
		return hashCode;
	}
	
	/**
	 * Equals object.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if( this == o ) return true;
		
		if( !(o instanceof PersonalFile ) ) return false;
		final PersonalFile other = (PersonalFile)o;
		

		if( (other.getOwner() != null && !other.getOwner().equals(owner)) ||
			(other.getOwner() == null && owner != null )) return false;
		
		if( (other.getName() != null && !other.getName().equals(getName())) ||
			(other.getName() == null && getName() != null )	) return false;
		
		if( (other.getPersonalFolder() != null && !other.getPersonalFolder().equals(getPersonalFolder())) ||
			(other.getPersonalFolder() == null && getPersonalFolder() != null )	) return false;
		
		return true;
			
	}	
	
	/**
	 * To string method for personal file.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" version = ");
		sb.append(version);
		sb.append("versioned file = " + versionedFile);
		sb.append(" personal folder = " + personalFolder);
		sb.append(" path = ");
		sb.append(getPath());
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Get the path for this personal file this does
	 * not include the path name.
	 *
	 * @see edu.ur.ir.FileSystem#getPath()
	 */
	public String getPath() {
		String path = null;
		if(personalFolder == null)
		{
			path = PATH_SEPERATOR;
		}
		else
		{
			path = personalFolder.getFullPath();
		}
		
		return path;
	}
	
	/**
	 * Get the full path for the versioned file.
	 * 
	 * @return
	 */
	public String getFullPath()
	{
		if( versionedFile == null )
		{
			throw new IllegalStateException(toString());
		}
		return getPath() + versionedFile.getNameWithExtension();
	}

	/**
	 * Get the file system type for this object.
	 * 
	 * @see edu.ur.ir.FileSystem#getFileSystemType()
	 */
	public FileSystemType getFileSystemType() {
		return fileSystemType;
	}

	/**
	 * Set the description for this personal file.
	 * 
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return versionedFile.getDescription();
	}

	/**
	 * Get the name for this personal file.
	 * 
	 * @see edu.ur.simple.type.NameAware#getName()
	 */
	public String getName() {
		return versionedFile.getNameWithExtension();
	}

	/**
	 * Get the owner of the personal file.
	 * 
	 * @return
	 */
	public IrUser getOwner() {
		return owner;
	}

	/**
	 * Set the owner of the personal file.
	 * 
	 * @param owner
	 */
	void setOwner(IrUser owner) {
		this.owner = owner;
	}

}
