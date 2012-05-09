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
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.persistent.BasePersistent;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;

public class GroupWorkspaceProjectPageInstitutionalItem extends BasePersistent implements NameAware, 
DescriptionAware, FileSystem{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -8865089434840131990L;

	/**  Institutional Item that is in the folder */
	private InstitutionalItem institutionalItem;
	
	/**  GroupWorkspaceProjectPage folder the publication belongs to. */
	private GroupWorkspaceProjectPageFolder parentFolder;
	
	/** GroupWorkspaceProjectPage the publication belongs to */
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;
	
	/** represents the file system type for this groupWorkspaceProjectPage publication */
	private FileSystemType fileSystemType = FileSystemType.RESEARCHER_INSTITUTIONAL_ITEM;
	
	/** GroupWorkspaceProjectPage given description of the institutional item */
	private String description;

	
	/**
	 * Package protected constructor.
	 */
	GroupWorkspaceProjectPageInstitutionalItem(){};
	
	/**
	 * Create a groupWorkspaceProjectPage publication with a null groupWorkspaceProjectPage folder.  This means this
	 * is a root groupWorkspaceProjectPage publication.
	 * 
	 * @param publicationVersion
	 */
	public GroupWorkspaceProjectPageInstitutionalItem(GroupWorkspaceProjectPage groupWorkspaceProjectPage, InstitutionalItem institutionalItem)
	{
		setGroupWorkspaceProjectPage(groupWorkspaceProjectPage);
		setInstitutionalItem(institutionalItem);
	}
	
	/**
	 * Create a link between a folder and publication.
	 * 
	 * @param publication - publication to create a link with
	 * @param parentFolder - folder the publication is in.  
	 */
	GroupWorkspaceProjectPageInstitutionalItem(GroupWorkspaceProjectPage groupWorkspaceProjectPage, GroupWorkspaceProjectPageFolder parentFolder, InstitutionalItem institutionalItem)
	{
		if (institutionalItem == null)
		{
			throw new IllegalStateException("publication cannot be null");
		}
		
		
		setGroupWorkspaceProjectPage(groupWorkspaceProjectPage);
		setInstitutionalItem(institutionalItem);
		setParentFolder(parentFolder);
	}


	/**
	 * Returns the path for this publicationVersion.
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
		sb.append(" institutional item = ");
		sb.append(institutionalItem);
		sb.append( " groupWorkspaceProjectPage = ");
		sb.append(groupWorkspaceProjectPage);
		sb.append("]");
		return sb.toString();
		
	}
	

	
	/**
	 * Get the full path of this publicationVersion.  If there is 
	 * no parent folder the path is just the name of
	 * the publication.
	 * 
	 * @return the full path.
	 */
	public String getFullPath()
	{
		return getPath() + institutionalItem.getName();
	}
	
	/**
	 * Hash code for a groupWorkspaceProjectPage publication.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += institutionalItem == null ? 0 : institutionalItem.hashCode();
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
		if (!(o instanceof GroupWorkspaceProjectPageInstitutionalItem)) return false;

		final GroupWorkspaceProjectPageInstitutionalItem other = (GroupWorkspaceProjectPageInstitutionalItem) o;

		if( (other.getGroupWorkspaceProjectPage() != null && !other.getGroupWorkspaceProjectPage().equals(getGroupWorkspaceProjectPage())) ||
			(other.getGroupWorkspaceProjectPage() == null && getGroupWorkspaceProjectPage() != null )) return false;

		
		if( (other.getInstitutionalItem() != null && !other.getInstitutionalItem().equals(getInstitutionalItem())) ||
			(other.getInstitutionalItem() == null && getInstitutionalItem() != null )	) return false;
		
		return true;
	}

	/**
	 * Returns the name of the publication.
	 * 
	 * @see edu.ur.simple.type.NameAware#getName()
	 */
	public String getName() {
		return institutionalItem.getName();
	}

	/**
	 * Returns the description of the publication.
	 * 
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the groupWorkspaceProjectPage description of the institutional item.
	 * 
	 * @param description
	 */
	public void setDescription(String description)
	{
	   this.description = description;	
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.FileSystem#getFileSystemType()
	 */
	public FileSystemType getFileSystemType() {
		return fileSystemType;
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
	
	public InstitutionalItem getInstitutionalItem() {
		return institutionalItem;
	}

	public void setInstitutionalItem(InstitutionalItem institutionalItem) {
		this.institutionalItem = institutionalItem;
	}


}
