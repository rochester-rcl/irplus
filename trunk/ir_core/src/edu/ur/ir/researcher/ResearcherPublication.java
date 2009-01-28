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
import edu.ur.ir.item.GenericItem;
import edu.ur.persistent.BasePersistent;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;

/**
 * This is a publication in the researcher folder.  This 
 * creates a link between a publication and a researcher 
 * folder
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ResearcherPublication extends BasePersistent implements NameAware, 
DescriptionAware, FileSystem{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -6543465629776275496L;

	/**  Logger */
	private static final Logger log = Logger.getLogger(ResearcherPublication.class);
	
	/**  Publication that is in the folder */
	private GenericItem publication;
	
	/**  researcher folder the publication belongs to. */
	private ResearcherFolder parentFolder;
	
	/** Researcher the publication belongs to */
	private Researcher researcher;
	
	/** represents the file system type for this researcher publication */
	private FileSystemType fileSystemType = FileSystemType.RESEARCHER_PUBLICATION;
	
	/** version number of the publication */
	private int versionNumber;

	

	/**
	 * Package protected constructor.
	 */
	ResearcherPublication(){};
	
	/**
	 * Create a researcher publication with a null researcher folder.  This means this
	 * is a root researcher publication.
	 * 
	 * @param versionNumber
	 */
	public ResearcherPublication(Researcher researcher, GenericItem publication, int versionNumber)
	{
		setResearcher(researcher);
		setPublication(publication);
		setVersionNumber(versionNumber);
	}
	
	/**
	 * Create a link between a folder and publication.
	 * 
	 * @param publication - publication to create a link with
	 * @param parentFolder - folder the publication is in.  
	 */
	ResearcherPublication(Researcher researcher, ResearcherFolder parentFolder, GenericItem publication, int versionNumber)
	{
		if(publication == null)
		{
			throw new IllegalStateException("publication cannot be null");
		}
		
		
		setResearcher(researcher);
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
	 * Hash code for a researcher publication.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += publication == null ? 0 : publication.hashCode();
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
		if (!(o instanceof ResearcherPublication)) return false;

		final ResearcherPublication other = (ResearcherPublication) o;

		if( (other.getResearcher() != null && !other.getResearcher().equals(getResearcher())) ||
				(other.getResearcher() == null && getResearcher() != null )) return false;

		if( (other.getFullPath() != null && !other.getFullPath().equals(getFullPath())) ||
			(other.getFullPath() == null && getFullPath() != null )	) return false;
		
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
			
			String description = getDescription();
			if( description != null )
			{
			    jsonObj.put("description",description.replaceAll("'", "&#146;").replaceAll("\"", "&#148;"));
			}
			else
			{
				jsonObj.put("description", "");
			}
			jsonObj.put("id",id);
			jsonObj.put("publicationId",publication.getId());
			jsonObj.put("type",fileSystemType.getType());

		} catch (Exception e) {
			 log.debug("jsonObj Exception::"+e.getMessage());
		}
		
		log.debug("jsonObj Publication::"+jsonObj);
		
		return jsonObj;
	}


	
}
