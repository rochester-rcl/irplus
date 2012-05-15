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
import edu.ur.persistent.CommonPersistent;

public class GroupWorkspaceProjectPageFileSystemLink extends CommonPersistent implements FileSystem{
	
	/* Eclipse generated id */
	private static final long serialVersionUID = 3144484183634385274L;

	/*  Link  */
	private String url;
	
	/*  groupWorkspaceProjectPage folder the link belongs to. */
	private GroupWorkspaceProjectPageFolder parentFolder;
	
	/* GroupWorkspaceProjectPage the link belongs to */
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;
	
	/* represents the file system type for this groupWorkspaceProjectPage link */
	private FileSystemType fileSystemType = FileSystemType.GROUP_WORKSPACE_PROJECT_PAGE_FILE_SYSTEM_LINK;

	
	/**
	 * Package protected constructor.
	 */
	GroupWorkspaceProjectPageFileSystemLink(){};
	
	/**
	 * Create a groupWorkspaceProjectPage link with a null groupWorkspaceProjectPage folder.  This means this
	 * is a root groupWorkspaceProjectPage link.
	 * 
	 * @param linkVersion
	 */
	GroupWorkspaceProjectPageFileSystemLink(GroupWorkspaceProjectPage groupWorkspaceProjectPage, String link)
	{
		setGroupWorkspaceProjectPage(groupWorkspaceProjectPage);
		setUrl(link);
	}
	
	/**
	 * Create a link between a folder and link.
	 * 
	 * @param link - link to create a link with
	 * @param parentFolder - folder the link is in.  
	 */
	GroupWorkspaceProjectPageFileSystemLink(GroupWorkspaceProjectPage groupWorkspaceProjectPage, GroupWorkspaceProjectPageFolder parentFolder, String link)
	{
		if(link == null)
		{
			throw new IllegalStateException("link cannot be null");
		}
		
		setGroupWorkspaceProjectPage(groupWorkspaceProjectPage);
		setUrl(link);
		setParentFolder(parentFolder);
	}


	/**
	 * Returns the path for this linkVersion.
	 * 
	 * The path is the path of the parent folder 
	 * 
	 * @return
	 */
	public String getPath()
	{

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
	 * Overridden to string method.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append( " path = ");
		sb.append(getPath());
		sb.append( " parent Folder = ");
		sb.append(parentFolder);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" link = ");
		sb.append(url);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Get the full path of this linkVersion.  If there is 
	 * no parent folder the path is just the name of
	 * the link.
	 * 
	 * @return the full path.
	 */
	public String getFullPath()
	{
		return getPath() + getName();
	}
	
	/**
	 * Hash code for a groupWorkspaceProjectPage link.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += parentFolder == null ? 0 : parentFolder.hashCode();
		value += getName() == null ? 0 : getName().hashCode();
		value += groupWorkspaceProjectPage == null ? 0 : groupWorkspaceProjectPage.hashCode();
		return value;
	}
	
	/**
	 * Equals method for a groupWorkspaceProjectPage link.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceProjectPageFileSystemLink)) return false;

		final GroupWorkspaceProjectPageFileSystemLink other = (GroupWorkspaceProjectPageFileSystemLink) o;

		if( (other.getName() != null && !other.getName().equals(getName())) ||
			(other.getName() == null && getName() != null )	) return false;
		
		if( (other.getGroupWorkspaceProjectPage() != null && !other.getGroupWorkspaceProjectPage().equals(getGroupWorkspaceProjectPage())) ||
				(other.getGroupWorkspaceProjectPage() == null && getGroupWorkspaceProjectPage() != null )	) return false;

		if( (other.getFullPath() != null && !other.getFullPath().equals(getFullPath())) ||
				(other.getFullPath() == null && getFullPath() != null )	) return false;

		return true;
	}

	/**
	 * Returns the name of the link.
	 * 
	 * @see edu.ur.simple.type.NameAware#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of the link.
	 * 
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.FileSystem#getFileSystemType()
	 */
	public FileSystemType getFileSystemType() {
		return fileSystemType;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public GroupWorkspaceProjectPageFolder getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(GroupWorkspaceProjectPageFolder parentFolder) {
		this.parentFolder = parentFolder;
	}

	public GroupWorkspaceProjectPage getGroupWorkspaceProjectPage() {
		return groupWorkspaceProjectPage;
	}

	public void setGroupWorkspaceProjectPage(GroupWorkspaceProjectPage groupWorkspaceProjectPage) {
		this.groupWorkspaceProjectPage = groupWorkspaceProjectPage;
	}
	

}
