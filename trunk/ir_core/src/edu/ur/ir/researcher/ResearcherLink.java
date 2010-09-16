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

package edu.ur.ir.researcher;



import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.persistent.CommonPersistent;

/**
 * This is a link in the researcher folder.  This 
 * creates a link between a link and a researcher 
 * folder
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ResearcherLink extends CommonPersistent implements FileSystem{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 3144484183634385274L;

	/**  Link  */
	private String url;
	
	/**  researcher folder the link belongs to. */
	private ResearcherFolder parentFolder;
	
	/** Researcher the link belongs to */
	private Researcher researcher;
	
	/** represents the file system type for this researcher link */
	private FileSystemType fileSystemType = FileSystemType.RESEARCHER_LINK;

	
	/**
	 * Package protected constructor.
	 */
	ResearcherLink(){};
	
	/**
	 * Create a researcher link with a null researcher folder.  This means this
	 * is a root researcher link.
	 * 
	 * @param linkVersion
	 */
	ResearcherLink(Researcher researcher, String link)
	{
		setResearcher(researcher);
		setUrl(link);
	}
	
	/**
	 * Create a link between a folder and link.
	 * 
	 * @param link - link to create a link with
	 * @param parentFolder - folder the link is in.  
	 */
	ResearcherLink(Researcher researcher, ResearcherFolder parentFolder, String link)
	{
		if(link == null)
		{
			throw new IllegalStateException("link cannot be null");
		}
		
		setResearcher(researcher);
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
	 * Hash code for a researcher link.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += parentFolder == null ? 0 : parentFolder.hashCode();
		value += getName() == null ? 0 : getName().hashCode();
		value += researcher == null ? 0 : researcher.hashCode();
		return value;
	}
	
	/**
	 * Equals method for a researcher link.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ResearcherLink)) return false;

		final ResearcherLink other = (ResearcherLink) o;

		if( (other.getName() != null && !other.getName().equals(getName())) ||
			(other.getName() == null && getName() != null )	) return false;
		
		if( (other.getResearcher() != null && !other.getResearcher().equals(getResearcher())) ||
				(other.getResearcher() == null && getResearcher() != null )	) return false;

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

	public ResearcherFolder getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(ResearcherFolder parentFolder) {
		this.parentFolder = parentFolder;
	}

	public Researcher getResearcher() {
		return researcher;
	}

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}
	
}
