/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.groupspace;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.file.VersionedFile;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a file within a group space.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupFile extends BasePersistent implements FileSystem{

	/* eclipse generated id */
	private static final long serialVersionUID = 1399705843691737746L;
	
	/* group folder this file belongs to  */
	private GroupFolder groupFolder;
	
	/* Versioned file to link to. */
	private VersionedFile versionedFile;
	
	/* Group space this file belongs to */
	private GroupSpace groupSpace;
 

	/**
     * Package protected constructor
     */
    GroupFile(){}
    
    /**
     * Default constructor.
     * 
     * @param versionedFile
     * @param groupSpace
     */
    public GroupFile(VersionedFile versionedFile, GroupSpace groupSpace)
    {
    	setVersionedFile(versionedFile);
    	setGroupSpace(groupSpace);
    }
    
    /**
     * Default constructor.
     * 
     * @param owner - owner of the group file 
     * @param groupSpace - group space the file belongs to
     * @param versionedFile - versioned file wrapped by this group file
     * @param groupFolder 
     */
    public GroupFile( VersionedFile versionedFile, GroupFolder groupFolder)
    {
    	this(versionedFile, groupFolder.getGroupSpace());
    	setGroupFolder(groupFolder);
    }

	/**
	 * Get the file system type 
	 * 
	 * @see edu.ur.ir.FileSystem#getFileSystemType()
	 */
	public FileSystemType getFileSystemType() {
		return FileSystemType.GROUP_FILE;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.FileSystem#getPath()
	 */
	public String getPath() {
		String path = null;
		if(groupFolder != null)
		{
			path = groupFolder.getFullPath();
		}
		
		return path;
	}

	/**
	 * Get the description of the versioned file.
	 * 
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return versionedFile.getDescription();
	}

	/**
	 * Get the name of the versioned file.
	 * 
	 * @see edu.ur.simple.type.NameAware#getName()
	 */
	public String getName() {
		return versionedFile.getNameWithExtension();
	}
	
	/**
	 * Get the group folder this file belongs to.
	 * 
	 * @return
	 */
	public GroupFolder getGroupFolder() {
		return groupFolder;
	}

	/**
	 * Set the group folder this file belongs to.
	 * 
	 * @param groupFolder
	 */
	public void setGroupFolder(GroupFolder groupFolder) {
		this.groupFolder = groupFolder;
	}
	
	/**
	 * Get the versioned file for this group file
	 * @return
	 */
	public VersionedFile getVersionedFile() {
		return versionedFile;
	}

	/**
	 * Set the version
	 * @param versionedFile
	 */
	void setVersionedFile(VersionedFile versionedFile) {
		this.versionedFile = versionedFile;
	}
	

	/**
	 * Get the group space.
	 * 
	 * @return
	 */
	public GroupSpace getGroupSpace() {
		return groupSpace;
	}

	/**
	 * Set the group space.
	 * 
	 * @param groupSpace
	 */
	public void setGroupSpace(GroupSpace groupSpace) {
		this.groupSpace = groupSpace;
	}
	
	/**
	 * Get the full path for the group file.
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
	 * Hash code method.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int hashCode = 0;
		hashCode += groupSpace == null ? 0 : groupSpace.hashCode();
		hashCode += getFullPath() == null ? 0 : getFullPath().hashCode();
		return hashCode;
	}

	
	/**
	 * Equals 
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if( this == o ) return true;
		
		if( !(o instanceof GroupFile ) ) return false;
		final GroupFile other = (GroupFile)o;
		

		if( (other.getGroupSpace() != null && !other.getGroupSpace().equals(groupSpace)) ||
			(other.getGroupSpace() == null && groupSpace != null )) return false;
		
		if( (other.getFullPath() != null && !other.getFullPath().equals(getFullPath())) ||
			(other.getFullPath() == null && getFullPath() != null )	) return false;
		
		return true;
			
	}	

}
