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
import edu.ur.ir.item.GenericItem;
import edu.ur.persistent.BasePersistent;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;

/**
 * Holder for a group workspace project page publication.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceProjectPagePublication extends BasePersistent implements NameAware, 
DescriptionAware, FileSystem{
	
	// eclipse generated id
	private static final long serialVersionUID = -3105773270486062300L;

	//  Publication that is in the folder 
	private GenericItem publication;
	
	//  groupWorkspaceProjectPage folder the publication belongs to. 
	private GroupWorkspaceProjectPageFolder parentFolder;
	
	// GroupWorkspaceProjectPage the publication belongs to 
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;
	
	// represents the file system type for this groupWorkspaceProjectPage publication 
	private FileSystemType fileSystemType = FileSystemType.GROUP_WORKSPACE_PROJECT_PAGE_PUBLICATION;
	
	// version number of the publication 
	private int versionNumber;
	
	/**
	 * Package protected constructor.
	 */
	GroupWorkspaceProjectPagePublication(){};
	
	/**
	 * Create a groupWorkspaceProjectPage publication with a null groupWorkspaceProjectPage folder.  This means this
	 * is a root groupWorkspaceProjectPage publication.
	 * 
	 * @param versionNumber
	 */
	public GroupWorkspaceProjectPagePublication(GroupWorkspaceProjectPage groupWorkspaceProjectPage, GenericItem publication, int versionNumber)
	{
		setResearcher(groupWorkspaceProjectPage);
		setPublication(publication);
		setVersionNumber(versionNumber);
	}
	
	/**
	 * Create a link between a folder and publication.
	 * 
	 * @param publication - publication to create a link with
	 * @param parentFolder - folder the publication is in.  
	 */
	GroupWorkspaceProjectPagePublication(GroupWorkspaceProjectPage groupWorkspaceProjectPage, GroupWorkspaceProjectPageFolder parentFolder, GenericItem publication, int versionNumber)
	{
		if(publication == null)
		{
			throw new IllegalStateException("publication cannot be null");
		}
		
		
		setResearcher(groupWorkspaceProjectPage);
		setPublication(publication);
		setParentFolder(parentFolder);
		setVersionNumber(versionNumber);
	}


	/**
	 * Returns the path for this versionNumber.
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
	 * version of the publication.
	 * 
	 * @return - the version of the publication
	 */
	public int getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Version of the publication.
	 * 
	 * @param pubicationVersion
	 */
	public void setVersionNumber(int pubicationVersion) {
		this.versionNumber = pubicationVersion;
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
		sb.append(" publication = ");
		sb.append(publication);
		sb.append(" publication version = ");
		sb.append(versionNumber);
		sb.append("]");
		return sb.toString();
		
	}
	

	
	/**
	 * Get the full path of this versionNumber.  If there is 
	 * no parent folder the path is just the name of
	 * the publication.
	 * 
	 * @return the full path.
	 */
	public String getFullPath()
	{
		return getPath() + publication.getName();
	}
	
	/**
	 * Hash code for a groupWorkspaceProjectPage publication.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += publication == null ? 0 : publication.hashCode();
		value += groupWorkspaceProjectPage == null ? 0 : groupWorkspaceProjectPage.hashCode();
		return value;
	}
	
	/**
	 * Equals method for a groupWorkspaceProjectPage publication.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceProjectPagePublication)) return false;

		final GroupWorkspaceProjectPagePublication other = (GroupWorkspaceProjectPagePublication) o;

		if( (other.getGroupWorkspaceProjectPage() != null && !other.getGroupWorkspaceProjectPage().equals(getGroupWorkspaceProjectPage())) ||
			(other.getGroupWorkspaceProjectPage() == null && getGroupWorkspaceProjectPage() != null )) return false;

		if( (other.getPublication() != null && !other.getPublication().equals(getPublication())) ||
			(other.getPublication() == null && getPublication() != null )	) return false;
		
		return true;
	}

	/**
	 * Returns the name of the publication.
	 * 
	 * @see edu.ur.simple.type.NameAware#getName()
	 */
	public String getName() {
		return publication.getName();
	}

	/**
	 * Returns the description of the publication.
	 * 
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return publication.getDescription();
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.FileSystem#getFileSystemType()
	 */
	public FileSystemType getFileSystemType() {
		return fileSystemType;
	}
	
	public GenericItem getPublication() {
		return publication;
	}

	public void setPublication(GenericItem publication) {
		this.publication = publication;
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

	public void setResearcher(GroupWorkspaceProjectPage groupWorkspaceProjectPage) {
		this.groupWorkspaceProjectPage = groupWorkspaceProjectPage;
	}

}
