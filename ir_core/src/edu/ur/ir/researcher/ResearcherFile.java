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
import edu.ur.ir.file.IrFile;
import edu.ur.persistent.BasePersistent;

/**
 * Links a researcher folder, ir file and the owning researcher together
 * If the researcher folder is null this indicates the file is at the root
 * or held by the researcher 
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ResearcherFile extends BasePersistent implements FileSystem{
	
	/** Eclipse generated id. */
	private static final long serialVersionUID = -3975113539620871421L;

	/** Ir file to link to. */
	private IrFile irFile;

	/**  Parent Researcher folder for this file information */
	private ResearcherFolder parentFolder;
	
	/** The file system type  */
	private FileSystemType fileSystemType = FileSystemType.RESEARCHER_FILE;
	
	/** Researcher the file belongs to. */
	private Researcher researcher;
	
	/** Version number of IrFile */
	private int versionNumber;
	
	/** Logger */
	private static final Logger log = Logger.getLogger(ResearcherFile.class);
	
	
	/**
	 * Default constructor
	 */
	ResearcherFile(){}
	
	/**
	 * Create a researcher file.
	 * 
	 * @param researcher - researcher uploading the file.
	 * @param irFile - ir file.
	 */
	ResearcherFile(Researcher researcher, IrFile irFile)
	{
		setResearcher(researcher);
		setIrFile(irFile);
	}
	
	/**
	 * Create a researcher file with the specified ir file and folder.
	 * 
	 * @param researcher - researcher uploading the file
	 * @param irFile - ir file 
	 * @param parentFolder - researcher folder that owns the researcher file
	 */
	ResearcherFile(Researcher researcher, IrFile irFile, ResearcherFolder parentFolder)
	{
		setResearcher(researcher);
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
		hashCode += getName() == null ? 0 : getName().hashCode();
		hashCode += researcher == null ? 0 : researcher.hashCode();
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
		
		if( !(o instanceof ResearcherFile ) ) return false;
		final ResearcherFile other = (ResearcherFile)o;
		
		if( (other.getName() != null && !other.getName().equals(getName())) ||
			(other.getName() == null && getName() != null )	) return false;

		if( (other.getParentFolder() != null && !other.getParentFolder().equals(getParentFolder())) ||
			(other.getParentFolder() == null && getParentFolder() != null )	) return false;

		if( (other.getResearcher() != null && !other.getResearcher().equals(getResearcher())) ||
			(other.getResearcher() == null && getResearcher() != null )	) return false;


		return true;
			
	}	
	
	/**
	 * To string method for researcher file.
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
		sb.append(" researcher folder = " + parentFolder);
		sb.append(" path = ");
		sb.append(getPath());
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Get the path for this researcher file this does
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
	 * Set the description for this researcher file.
	 * 
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return irFile.getDescription();
	}

	/**
	 * Get the name for this researcher file.
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
	 * Get the parent researcher folder for this file.
	 * 
	 * @return
	 */
	public ResearcherFolder getParentFolder() {
		return parentFolder;
	}

	/**
	 * Set the parent researcher folder.
	 * 
	 * @param parentFolder
	 */
	void setParentFolder(ResearcherFolder parentFolder) {
		this.parentFolder = parentFolder;
	}

	/**
	 * Get researcher the file belongs to
	 * 
	 * @return
	 */
	public Researcher getResearcher() {
		return researcher;
	}

	/**
	 * Set researcher the file belongs to
	 * 
	 * @param researcher
	 */
	void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

	/**
	 * Creates JSON object
	 * 
	 * @return
	 */
	public JSONObject toJSONObject() {
		log.debug("call File getJsonString");
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			jsonObj.put("name",getName().replaceAll("'", "&#146;").replaceAll("\"", "&#148;"));
			
			String description = getDescription();
			if(description != null)
			{
			    jsonObj.put("description",description.replaceAll("'", "&#146;").replaceAll("\"", "&#148;"));
			}
			else
			{
				jsonObj.put("description", "");
			}
			jsonObj.put("id",id);
			jsonObj.put("type",fileSystemType.getType());
			jsonObj.put("extension",irFile.getFileInfo().getExtension());

		} catch (Exception e) {
			 log.debug("jsonObj Exception::"+e.getMessage());
		}
		
		log.debug("jsonObj File ::"+jsonObj);
		
		return jsonObj;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

}
