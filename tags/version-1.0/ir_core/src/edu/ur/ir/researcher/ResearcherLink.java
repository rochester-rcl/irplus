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

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import net.sf.json.JSONObject;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.net.UrlAware;
import edu.ur.persistent.CommonPersistent;

/**
 * This is a link in the researcher folder.  This 
 * creates a link between a link and a researcher 
 * folder
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ResearcherLink extends CommonPersistent implements UrlAware, FileSystem{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 3144484183634385274L;

	/**  Logger */
	private static final Logger log = Logger.getLogger(ResearcherLink.class);
	
	/**  Link  */
	private String link;
	
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
		setLink(link);
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
		setLink(link);
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
		sb.append(link);
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
	
	public String getLink() {
		return link;
	}
	
	/**
	 * The url for this link.
	 * 
	 * @see edu.ur.net.UrlAware#getUrl()
	 */
	public URL getUrl() throws MalformedURLException {
		return new URL(link);
	}

	public void setLink(String link) {
		this.link = link;
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
	
	/**
	 * Creates JSON object
	 * 
	 * @return
	 */
	public JSONObject toJSONObject() {
		log.debug("call getJsonString Link");
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			jsonObj.put("name",getName().replaceAll("'", "&#146;").replaceAll("\"", "&#148;"));
			jsonObj.put("id",id);
			jsonObj.put("type",fileSystemType.getType());
			jsonObj.put("url",getLink());
			
			if(description != null)
			{
			    jsonObj.put("description",getDescription().replaceAll("'", "&#146;").replaceAll("\"", "&#148;"));
			}
			else
			{
				jsonObj.put("description", "");
			}
				
		} catch (Exception e) {
			 log.debug("jsonObj Exception::"+e.getMessage());
		}
		
		log.debug("jsonObj Link::"+jsonObj);
		
		return jsonObj;
	}


	
}
