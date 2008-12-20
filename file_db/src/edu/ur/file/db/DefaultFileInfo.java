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

import java.io.File;
import java.net.URI;

import org.apache.commons.io.FilenameUtils;

import edu.ur.file.checksum.ChecksumCalculator;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * Represents an object that is saved in the
 * system as a file
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultFileInfo implements FileInfo {

	/**  Eclipse generated id */
	private static final long serialVersionUID = -5517441144459200055L;

	/**  Database id */
	private Long id;

	/**  Parent folder  */
	private TreeFolderInfo folderInfo;
	
	/**  Name of the file */
	private String displayName;

	/**  Name of the file */
	private String name;
	
	/**  Description of the file */
	private String description;
	
	/**  Size of the file in bytes */
	private Long size;
	
	/**  Date the file was created;  */
	private Timestamp createdDate;
	
	/**  The last time the file was modified */
	private Timestamp modifiedDate;
	
	/**  Indicates if the folder is saved to the file system. */
	private boolean exists = false;
	
	/**  The file extension */
	private String extension;
	
	/**
	 * The database version of the data in the 
	 * database
	 */
	private int version;
	
	/** 
	 * Most recent Checksum information for the file info - there should only be one checksum 
	 * per algorithm type in this list
	 *
	 */
	private Set<FileInfoChecksum> fileInfoChecksums = new HashSet<FileInfoChecksum>();
	

	/**
	 * Default Constructor 
	 */
	DefaultFileInfo(){}
		
	/**
	 * Get the parent folder of this folder
	 * 
	 * @return
	 */
	public TreeFolderInfo getFolderInfo(){
		return folderInfo;
	}
	
	/**
	 * Set the parent folder of this folder
	 * 
	 * @param folder
	 */
	void setFolderInfo(TreeFolderInfo folderInfo)
	{
		this.folderInfo = folderInfo;
	}
	
	/**
	 * Get the file name used in the file system
	 * for this file.
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * The file name in the file system.  If the
	 * file name has an extension it is removed
	 * 
	 * @param fileName
	 */
	void setName(String name)
	{
		this.name = FilenameUtils.removeExtension(name);
	}
	
	/**
	 * Get the path for this file it does not 
	 * include the files name.
	 * 
	 * @return the path.
	 */
	public String getPath()
	{
		if( folderInfo != null )
		{
			return folderInfo.getFullPath();
		}
		else
		{
			return null;
		}
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see edu.ur.file.db.PathAware#getPrefix()
	 */
	public String getPrefix()
	{
		if( folderInfo != null )
		{
			return folderInfo.getPrefix();
		}
		
		return null;
	}
	
	/**
	 * Get the full path for the file
	 * 
	 * @return full path including the file name.
	 */
	public String getFullPath()
	{
		return getPath() + name;
	}

	/**
	 * Get the id of this data.
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the unique id of this data.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the version of data read from the database
	 * 
	 * @return the version of data
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Set the version of data read from the database.
	 * 
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}
	
	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Set to true if this file is in the
	 * file system.  This is different than
	 * deleted.  In File system indicates the
	 * file was added to the file system.
	 * 
	 * @return
	 */
	public boolean getExists() {
		return exists;
	}
	
	/**
	 * Determine if the file is in the
	 * file system.
	 * 
	 * @return true if the file is in 
	 * the file system.
	 */
	public boolean exists()
	{
		return exists;
	}

	/**
	 * Set the file as added to the file
	 * system.
	 * 
	 * @param inFileSystem
	 */
	void setExists(boolean exists) {
		this.exists = exists;
	}
	
	/**
	 * Description of the file
	 * 
	 * @return the files description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of the file.
	 * 
	 * @param description of the file
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[FileInfo id = ");
		sb.append(id);
		sb.append(" display name = ");
		sb.append(displayName);
		sb.append(" file name = ");
		sb.append(name);
		sb.append(" extension = ");
		sb.append(extension);
		sb.append(" path = ");
		sb.append(getFullPath());
		sb.append("]");
		return sb.toString();
	}

	/**
	 * The date the file was created in the file
	 * system.
	 * 
	 * @return createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * The date the file was created
	 * @param createDate
	 */
	void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Date the file was last modified.
	 * 
	 * @return last modified date
	 */
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Set the last modified date.
	 * 
	 * @param modifiedDate
	 */
	void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Set the size of the file.
	 * 
	 * @return
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * Set the size of the file.
	 * 
	 * @param size
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	/**
	 * Get the file extension
	 * 
	 * @return the file extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Set the file extension.
	 * 
	 * @param extension
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	/**
	 * Get the URI of the file
	 * 
	 * @return the uri
	 */
	public URI getUri() 
	{
	    return new File(getFullPath()).toURI();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		
		value += getName() == null ? 0 : getName().hashCode();
		value += getPath() == null ? 0 : getPath().hashCode();
        
		return value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DefaultFileInfo)) return false;

		final DefaultFileInfo other = (DefaultFileInfo) o;

		if( ( getName() != null && !getName().equals(other.getName()) ) ||
			( getName() == null && other.getName() != null ) ) return false;

		if( ( getPath() != null && !getPath().equals(other.getPath()) ) ||
			( getPath() == null && other.getPath() != null ) ) return false;
		
		return true;
	}

	
	/* (non-Javadoc)
	 * @see edu.ur.file.db.FileInfo#getNameWithExtension()
	 */
	public String getNameWithExtension() {
		String nameWithExtension = name;
		if( extension != null &&
				!extension.trim().equals(""))
		{
			nameWithExtension = name + "." + extension; 
		}
		
		return nameWithExtension;
	}
	

	/**
	 * Get the checksum information for this file.
	 * 
	 * @see edu.ur.file.db.FileInfo#getFileInfoChecksum()
	 */
	public Set<FileInfoChecksum> getFileInfoChecksums() {
		return Collections.unmodifiableSet(fileInfoChecksums);
	}

	/**
	 * Set the checksum information for this file.
	 * 
	 * @param fileInfoChecksum
	 */
	void setFileInfoChecksum(Set<FileInfoChecksum> fileInfoChecksums) {
		this.fileInfoChecksums = fileInfoChecksums;
	}

	/**
	 * Calcluate and add the specified checksum to this file info.
	 * 
	 * @see edu.ur.file.db.FileInfo#addFileInfoChecksum(edu.ur.file.checksum.ChecksumCalculator)
	 */
	public void addFileInfoChecksum(ChecksumCalculator checksumCalculator) {
		String checksum = checksumCalculator.calculate(new File(this.getFullPath()));
        FileInfoChecksum fileInfoChecksum = new FileInfoChecksum(checksum, checksumCalculator.getAlgorithmType(), this);
        
        FileInfoChecksum oldChecksum = getFileInfoChecksum(checksumCalculator.getAlgorithmType());
        if(oldChecksum != null)
        {
        	fileInfoChecksums.remove(oldChecksum);
        }
        
        fileInfoChecksums.add(fileInfoChecksum);
	}

	
	/**
	 * Get the checksum for the specified algorithm type or return null if none found.
	 * 
	 * @see edu.ur.file.db.FileInfo#getFileInfoChecksum(java.lang.String)
	 */
	public FileInfoChecksum getFileInfoChecksum(String algorithmType) {
		
		for(FileInfoChecksum sum : fileInfoChecksums)
		{
			if( sum.getAlgorithmType().equals(algorithmType))
			{
				return sum;
			}
		}
		
		return null;
		
	}


}
