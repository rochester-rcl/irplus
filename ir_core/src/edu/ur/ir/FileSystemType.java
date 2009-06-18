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

import edu.ur.order.Orderable;

/**
 * Allows the setting of different file types.
 * 
 * @author Nathan Sarr
 *
 */
public class FileSystemType implements Serializable, Orderable{
	
	/** eclipse genreated id */
	private static final long serialVersionUID = -7485498621126903978L;

	/** Represents a file type */
	public static final FileSystemType FILE = new FileSystemType("file", 1);
	public static final FileSystemType FOLDER = new FileSystemType("folder", 10);
	public static final FileSystemType PERSONAL_FILE = new FileSystemType("personalFile", 1);
	public static final FileSystemType SHARED_INBOX_FILE = new FileSystemType("sharedInboxFile", 1);
	public static final FileSystemType PERSONAL_FOLDER = new FileSystemType("personalFolder", 10);
	public static final FileSystemType ITEM = new FileSystemType("item", 1);
	public static final FileSystemType COLLECTION = new FileSystemType("collection", 10);
	public static final FileSystemType PERSONAL_ITEM = new FileSystemType("personalItem", 1);
	public static final FileSystemType PERSONAL_COLLECTION = new FileSystemType("personalCollection", 10);
	public static final FileSystemType INSTITUTIONAL_ITEM = new FileSystemType("institutionalItem", 1);
	public static final FileSystemType INSTITUTIONAL_COLLECTION = new FileSystemType("institutionalCollection", 10);
	public static final FileSystemType RESEARCHER_FILE = new FileSystemType("researcherFile", 1);
	public static final FileSystemType RESEARCHER_FOLDER = new FileSystemType("researcherFolder", 10);
	public static final FileSystemType RESEARCHER_PUBLICATION = new FileSystemType("researcherPublication", 1);
	public static final FileSystemType RESEARCHER_LINK = new FileSystemType("researcherLink", 1);
	public static final FileSystemType RESEARCHER_INSTITUTIONAL_ITEM = new FileSystemType("researcherInstitutionalItem", 1);

	
	/** The type of object */
	private String type;
	
	/**  ordering for type */
	private int order;
	
	/**
	 * Default constructor
	 * 
	 * @param type
	 */
	private FileSystemType(String type, int order)
	{
		this.type = type;
		this.order = order;
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
	
	/**
	 * Order basis for the type.
	 * 
	 * @return
	 */
	public int getOrder()
	{
		return order;
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
