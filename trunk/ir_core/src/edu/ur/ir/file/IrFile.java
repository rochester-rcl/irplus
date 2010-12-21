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

package edu.ur.ir.file;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.persistent.BasePersistent;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.ir.user.IrUser;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;

/**
 * Represents a file stored in the ir.  It is a wrapper 
 * around a FileInfo object.  This allows the Ir file object
 * to store more information than just what the file info
 * stores.  Including transformed file information.
 * 
 * This class overrides the dsplay name of the FileInfo object.
 * 
 * The IR file 
 * 
 * @author Nathan Sarr
 *
 */
public class IrFile extends BasePersistent implements NameAware, DescriptionAware{

	/**  Eclipse generated id. */
	private static final long serialVersionUID = 4034692068858579239L;
	
	/** File information for this ir file */
	private FileInfo fileInfo;
	
	/** Logger */
	private static final Logger log = Logger.getLogger(IrFile.class);
	
	/** Set of files that have been transformed from this one. */
	private Set<TransformedFile> transformedFiles = new HashSet<TransformedFile>();
	
	/** owner of this file  */
	private IrUser owner;
	
	/** this indicates the file can be viewed by the public*/
	private boolean publicViewable = false;
	
	/** roll up download count for the ir file */
	private Long downloadCount = Long.valueOf(0l);
	

	/**
	 * Package protected default constructor 
	 */
	IrFile(){}

	/**
	 * Constructor that takes a file info.
	 * 
	 * @param fileInfo the file to wrap
	 * @param name name to give to this ir file
	 */
	public IrFile(FileInfo fileInfo, String name) throws IllegalFileSystemNameException 
	{
		log.debug("Setting the file info");
		if( name == null )
		{
			throw new IllegalStateException("name cannot be null");
		}
		fileInfo.setDisplayName(name);
		setFileInfo(fileInfo);
	}

	/**
	 * Get the file info for this ir file.
	 * 
	 * @return the file info
	 */
	public FileInfo getFileInfo() {
		return fileInfo;
	}

	/**
	 * Set the file info for this ir file.
	 * 
	 * @param fileInfo
	 */
	void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	/* (non-Javadoc)
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return fileInfo.getDescription();
	}



	/* (non-Javadoc)
	 * @see edu.ur.simple.type.NameAware#getName()
	 */
	public String getName() {
		return fileInfo.getDisplayName();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += getFileInfo() == null ? 0 : getFileInfo().hashCode();
		return value;
	}
	
	/**
	 * This assumes if the names are the same, the IrFiles are the
	 * same.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IrFile)) return false;

		final IrFile other = (IrFile) o;

		if( ( fileInfo != null && !fileInfo.equals(other.getFileInfo()) ) ||
			( fileInfo == null && other.getFileInfo() != null ) ) return false;
	
		return true;
	}
	
	/**
	 * Return an unmodifiable set of files
	 * 
	 * @return
	 */
	public Set<TransformedFile> getTransformedFiles() {
		return Collections.unmodifiableSet(transformedFiles);
	}

	/**
	 * Set the transformed files.
	 * 
	 * @param transformedFiles
	 */
	void setTransformedFiles(Set<TransformedFile> transformedFiles) {
		this.transformedFiles = transformedFiles;
	}
	
	/**
	 * This allows a specified transformed file to be returned by system code 
	 * if a transformed file with the specified system code exists.  If that
	 * transformed file does not exist, null is returned.
	 * 
	 * @param systemCode
	 * @return
	 */
	public TransformedFile getTransformedFileBySystemCode(String systemCode)
	{
		if( systemCode == null)
		{
			return null;
		}
		
		for(TransformedFile transformedFile: transformedFiles)
		{
			TransformedFileType transformedFileType = transformedFile.getTransformedFileType();
			if( transformedFileType != null && systemCode.equals(transformedFileType.getSystemCode()) )
			{
				return transformedFile;
			}
		}
		
		return null;
	}
	
	/**
	 * Add the transformed file to the set of transformed files.
	 * 
	 * @param derivedFile
	 */
	public TransformedFile addTransformedFile(FileInfo inTransformedFile, TransformedFileType transformedFileType)
	{
		TransformedFile transformedFile = new TransformedFile(this, inTransformedFile, transformedFileType);
		transformedFiles.add(transformedFile);
		return transformedFile;
	}
	
	/**
	 * Removes a transformed file with the specified id from the set.
	 * 
	 * @param id - of the transformed file
	 * @return the removed transformed file or null if no transformed file is found
	 */
	public boolean removeTransformedFile(TransformedFile transformedFile)
	{
		return transformedFiles.remove(transformedFile);
	}
	
	/**
	 * Print out base IrFile information.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Ir File id = ");
		sb.append(id);
		sb.append(" version = ");
		sb.append(version);
		sb.append(" name = ");
		sb.append(fileInfo.getName());
		sb.append(" public viewable = " + publicViewable);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Owner of this ir file
	 * @return
	 */
	public IrUser getOwner() {
		return owner;
	}

	/**
	 * Owner of this ir file.
	 * 
	 * @param owner
	 */
	void setOwner(IrUser owner) {
		this.owner = owner;
	}

	/**
	 * Indicates the file can be viewed by the public
	 * 
	 * @return true if the file can be viewed by the public
	 */
	public boolean isPublicViewable() {
		return publicViewable;
	}
	
	/**
	 * Indicates the file can be viewed by the public
	 * 
	 * @return true if the file can be viewed by the public
	 */
	public boolean getPublicViewable() {
		return isPublicViewable();
	}


	/**
	 * Indicates the file can be viewed by the public.
	 * 
	 * @param publicViewable
	 */
	public void setPublicViewable(boolean publicViewable) {
		this.publicViewable = publicViewable;
	}

	/**
	 * Returns the name with the extension.  If there
	 * is no extension only the name is returned.
	 * 
	 * @return
	 */
	public String getNameWithExtension()
	{
		String nameWithExtension = fileInfo.getDisplayName();
		String extension = getFileInfo().getExtension();
		
		if( extension != null &&
				!extension.trim().equals(""))
		{
			nameWithExtension = fileInfo.getDisplayName() + "." + extension; 
		}
		
		return nameWithExtension;
	}
	
	/**
	 * Get the download count for this ir file.
	 * 
	 * @return download count
	 */
	public Long getDownloadCount() {
		return downloadCount;
	}

	/**
	 * Set the download count.
	 * 
	 * @param downloadCount
	 */
	public void setDownloadCount(Long downloadCount) {
		if( downloadCount != null )
		{
		    this.downloadCount = downloadCount;
		}
		else
		{
			this.downloadCount = Long.valueOf(0l);
		}
	}

}
