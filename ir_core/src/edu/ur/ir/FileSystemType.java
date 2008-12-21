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

package edu.ur.ir;

import java.io.Serializable;

/**
 * Allows the setting of different file types.
 * 
 * @author Nathan Sarr
 *
 */
public class FileSystemType implements Serializable{
	
	/** eclipse genreated id */
	private static final long serialVersionUID = -7485498621126903978L;

	/** Represents a file type */
	public static final FileSystemType FILE = new FileSystemType("file");
	public static final FileSystemType FOLDER = new FileSystemType("folder");
	public static final FileSystemType PERSONAL_FILE = new FileSystemType("personalFile");
	public static final FileSystemType SHARED_INBOX_FILE = new FileSystemType("sharedInboxFile");
	public static final FileSystemType PERSONAL_FOLDER = new FileSystemType("personalFolder");
	public static final FileSystemType ITEM = new FileSystemType("item");
	public static final FileSystemType COLLECTION = new FileSystemType("collection");
	public static final FileSystemType PERSONAL_ITEM = new FileSystemType("personalItem");
	public static final FileSystemType PERSONAL_COLLECTION = new FileSystemType("personalCollection");
	public static final FileSystemType INSTITUTIONAL_ITEM = new FileSystemType("institutionalItem");
	public static final FileSystemType INSTITUTIONAL_COLLECTION = new FileSystemType("institutionalCollection");
	public static final FileSystemType RESEARCHER_FILE = new FileSystemType("researcherFile");
	public static final FileSystemType RESEARCHER_FOLDER = new FileSystemType("researcherFolder");
	public static final FileSystemType RESEARCHER_PUBLICATION = new FileSystemType("researcherPublication");
	public static final FileSystemType RESEARCHER_LINK = new FileSystemType("researcherLink");
	public static final FileSystemType RESEARCHER_INSTITUTIONAL_ITEM = new FileSystemType("researcherInstitutionalItem");

	
	/** The type of object */
	private String type;
	
	/**
	 * Default constructor
	 * 
	 * @param type
	 */
	private FileSystemType(String type)
	{
		this.type = type;
	}
	
	/**
	 * Get the type.
	 * 
	 * @return
	 */
	public String getType()
	{
		return this.type;
	}
	
	public String toString()
	{
		return type;
	}
	
	public int hashCode()
	{
		int hashCode = 0;
		hashCode += type != null ? type.hashCode() : 0;
		return hashCode;
	}
	
	public boolean equals(Object o)
	{
    	if (this == o) return true;
		if (!(o instanceof FileSystemType)) return false;

		final FileSystemType other = (FileSystemType) o;

		if( ( type != null && !type.equals(other.getType()) ) ||
			( type == null && other.getType() != null ) ) return false;
		
		return true;
	}
	

}
