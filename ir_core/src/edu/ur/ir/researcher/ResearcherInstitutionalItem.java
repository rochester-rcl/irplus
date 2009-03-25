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

import org.apache.log4j.Logger;
import net.sf.json.JSONObject;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.persistent.BasePersistent;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;

/**
 * This creates a link between the institutional item and a researcher 
 * folder
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ResearcherInstitutionalItem extends BasePersistent implements NameAware, 
DescriptionAware, FileSystem{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -8865089434840131990L;

	/**  Logger */
	private static final Logger log = Logger.getLogger(ResearcherInstitutionalItem.class);
	
	/**  Institutional Item that is in the folder */
	private InstitutionalItem institutionalItem;
	
	/**  Researcher folder the publication belongs to. */
	private ResearcherFolder parentFolder;
	
	/** Researcher the publication belongs to */
	private Researcher researcher;
	
	/** represents the file system type for this researcher publication */
	private FileSystemType fileSystemType = FileSystemType.RESEARCHER_INSTITUTIONAL_ITEM;
	
	/** Researcher given description of the institutional item */
	private String description;

	
	/**
	 * Package protected constructor.
	 */
	ResearcherInstitutionalItem(){};
	
	/**
	 * Create a researcher publication with a null researcher folder.  This means this
	 * is a root researcher publication.
	 * 
	 * @param publicationVersion
	 */
	public ResearcherInstitutionalItem(Researcher researcher, InstitutionalItem institutionalItem)
	{
		setResearcher(researcher);
		setInstitutionalItem(institutionalItem);
	}
	
	/**
	 * Create a link between a folder and publication.
	 * 
	 * @param publication - publication to create a link with
	 * @param parentFolder - folder the publication is in.  
	 */
	ResearcherInstitutionalItem(Researcher researcher, ResearcherFolder parentFolder, InstitutionalItem institutionalItem)
	{
		if (institutionalItem == null)
		{
			throw new IllegalStateException("publication cannot be null");
		}
		
		
		setResearcher(researcher);
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
	 * Hash code for a researcher publication.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += institutionalItem == null ? 0 : institutionalItem.hashCode();
		value += parentFolder == null ? 0 : parentFolder.hashCode();
		value += researcher == null ? 0 : researcher.hashCode();
		return value;
	}
	
	/**
	 * Equals method for a researcher publication.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ResearcherInstitutionalItem)) return false;

		final ResearcherInstitutionalItem other = (ResearcherInstitutionalItem) o;

		if( (other.getResearcher() != null && !other.getResearcher().equals(getResearcher())) ||
				(other.getResearcher() == null && getResearcher() != null )) return false;

		if( (other.getFullPath() != null && !other.getFullPath().equals(getFullPath())) ||
			(other.getFullPath() == null && getFullPath() != null )	) return false;
		
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
	 * Set the researcher description of the institutional item.
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
		log.debug("call getJsonString Publication");
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			jsonObj.put("name",getName().replaceAll("'", "&#146;").replaceAll("\"", "&#148;"));
			jsonObj.put("id",id);
			jsonObj.put("institutionalItemId",institutionalItem.getId());
			jsonObj.put("type",fileSystemType.getType());
			String description = getDescription();
			if( description != null )
			{
			    jsonObj.put("description",description.replaceAll("'", "&#146;").replaceAll("\"", "&#148;"));
			}
			else
			{
				jsonObj.put("description", "");
			}

		} catch (Exception e) {
			 log.debug("jsonObj Exception::"+e.getMessage());
		}
		
		log.debug("jsonObj Publication::"+jsonObj);
		
		return jsonObj;
	}

	public InstitutionalItem getInstitutionalItem() {
		return institutionalItem;
	}

	public void setInstitutionalItem(InstitutionalItem institutionalItem) {
		this.institutionalItem = institutionalItem;
	}


	
}
