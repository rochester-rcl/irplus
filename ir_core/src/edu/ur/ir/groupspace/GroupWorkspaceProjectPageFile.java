/**  
   Copyright 2008 - 2012 University of Rochester

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
import edu.ur.ir.file.IrFile;
import edu.ur.persistent.BasePersistent;

public class GroupWorkspaceProjectPageFile extends BasePersistent implements FileSystem{
	
	// Eclipse generated id. 
	private static final long serialVersionUID = -7227291733875203233L;

	// Ir file to link to. 
	private IrFile irFile;

	//  Parent GroupWorkspaceProjectPage folder for this file information 
	private GroupWorkspaceProjectPageFolder parentFolder;
	
	// The file system type  
	private FileSystemType fileSystemType = FileSystemType.GROUP_WORKSPACE_PROJECT_PAGE_FILE;
	
	// Group workspace project page the file belongs to. 
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;
	
	// Version number of IrFile 
	private int versionNumber;
	
	/**
	 * Default constructor
	 */
	GroupWorkspaceProjectPageFile(){}
	
	/**
	 * Create a groupWorkspaceProjectPage file.
	 * 
	 * @param groupWorkspaceProjectPage - groupWorkspaceProjectPage uploading the file.
	 * @param irFile - ir file.
	 */
	GroupWorkspaceProjectPageFile(GroupWorkspaceProjectPage groupWorkspaceProjectPage, IrFile irFile)
	{
		setGroupWorkspaceProjectPage(groupWorkspaceProjectPage);
		setIrFile(irFile);
	}
	
	/**
	 * Create a groupWorkspaceProjectPage file with the specified ir file and folder.
	 * 
	 * @param groupWorkspaceProjectPage - groupWorkspaceProjectPage uploading the file
	 * @param irFile - ir file 
	 * @param parentFolder - groupWorkspaceProjectPage folder that owns the groupWorkspaceProjectPage file
	 */
	GroupWorkspaceProjectPageFile(GroupWorkspaceProjectPage groupWorkspaceProjectPage, IrFile irFile, GroupWorkspaceProjectPageFolder parentFolder)
	{
		setGroupWorkspaceProjectPage(groupWorkspaceProjectPage);
		setIrFile(irFile);
		setParentFolder(parentFolder);
	}

	/**
	 * Hashcode method.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int hashCode = 0;
		hashCode += getNameWithExtension() == null ? 0 : getNameWithExtension().hashCode();
		hashCode += groupWorkspaceProjectPage == null ? 0 : groupWorkspaceProjectPage.hashCode();
		hashCode += parentFolder == null ? 0 : parentFolder.hashCode();
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
		
		if( !(o instanceof GroupWorkspaceProjectPageFile ) ) return false;
		final GroupWorkspaceProjectPageFile other = (GroupWorkspaceProjectPageFile)o;
		
		if( (other.getNameWithExtension() != null && !other.getNameWithExtension().equals(getNameWithExtension())) ||
			(other.getNameWithExtension() == null && getNameWithExtension() != null )	) return false;

		if( (other.getParentFolder() != null && !other.getParentFolder().equals(getParentFolder())) ||
			(other.getParentFolder() == null && getParentFolder() != null )	) return false;

		if( (other.getGroupWorkspaceProjectPage() != null && !other.getGroupWorkspaceProjectPage().equals(getGroupWorkspaceProjectPage())) ||
			(other.getGroupWorkspaceProjectPage() == null && getGroupWorkspaceProjectPage() != null )	) return false;


		return true;
			
	}	
	
	/**
	 * To string method for groupWorkspaceProjectPage file.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" version = ");
		sb.append(version);
		sb.append("Ir file = " + irFile);
		sb.append(" groupWorkspaceProjectPage folder = " + parentFolder);
		sb.append(" path = ");
		sb.append(getPath());
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Get the path for this groupWorkspaceProjectPage file this does
	 * not include the path name.
	 *
	 * @see edu.ur.ir.FileSystem#getPath()
	 */
	public String getPath() {
		String path = null;
		if(parentFolder == null)
		{
			path = PATH_SEPERATOR;
		}
		else
		{
			path = parentFolder.getFullPath();
		}
		
		return path;
	}
	
	/**
	 * Get the full path for the irFile.
	 * 
	 * @return
	 */
	public String getFullPath()
	{
		if( irFile == null )
		{
			throw new IllegalStateException(toString());
		}
		return getPath() + getNameWithExtension();
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
	 * Set the description for this groupWorkspaceProjectPage file.
	 * 
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return irFile.getDescription();
	}

	/**
	 * Get the name for this groupWorkspaceProjectPage file.
	 * 
	 * @see edu.ur.simple.type.NameAware#getName()
	 */
	public String getName() {
		return irFile.getName();
	}
	
	/**
	 * Returns the name with the extension.  If there
	 * is no extension only the name is returned.
	 * 
	 * @return
	 */
	public String getNameWithExtension()
	{
		String nameWithExtension = irFile.getName();
		String extension = irFile.getFileInfo().getExtension();
		
		if( extension != null &&
				!extension.trim().equals(""))
		{
			nameWithExtension = irFile.getName() + "." + extension; 
		}
		
		return nameWithExtension;
	}	

	/**
	 * Get the file 
	 * 
	 * @return
	 */
	public IrFile getIrFile() {
		return irFile;
	}

	/**
	 * Set the file
	 * 
	 * @param irFile  
	 */
	public void setIrFile(IrFile irFile) {
		this.irFile = irFile;
	}

	/**
	 * Get the parent groupWorkspaceProjectPage folder for this file.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPageFolder getParentFolder() {
		return parentFolder;
	}

	/**
	 * Set the parent groupWorkspaceProjectPage folder.
	 * 
	 * @param parentFolder
	 */
	void setParentFolder(GroupWorkspaceProjectPageFolder parentFolder) {
		this.parentFolder = parentFolder;
	}

	/**
	 * Get groupWorkspaceProjectPage the file belongs to
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPage getGroupWorkspaceProjectPage() {
		return groupWorkspaceProjectPage;
	}

	/**
	 * Set groupWorkspaceProjectPage the file belongs to
	 * 
	 * @param groupWorkspaceProjectPage
	 */
	void setGroupWorkspaceProjectPage(GroupWorkspaceProjectPage groupWorkspaceProjectPage) {
		this.groupWorkspaceProjectPage = groupWorkspaceProjectPage;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}


}
