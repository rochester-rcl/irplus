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
package edu.ur.file.mime;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import edu.ur.exception.DuplicateNameException;
import edu.ur.persistent.CommonPersistent;


/**
 * Represents a mime sub type for uploaded files
 * 
 * @author Nathan Sarr
 *
 */
public class SubType extends CommonPersistent{

	/**  serrial version id for Serialization */
	private static final long serialVersionUID = 126856037961151447L;
	
	/**  The top media type for this sub type */
	private TopMediaType topMediaType;
	
	/**  The set of extensions for this sub type */
	private Set<SubTypeExtension> extensions = new HashSet<SubTypeExtension>();
	
	/**  Description of mime sub type sub type  */
	private String shortDescription;
	
	/**
	 * Package protected Default Constructor 
	 */
	SubType(){}
	
	/**
	 * Create a sub type with the specified name and top media type.
	 * 
	 * @param topMediaType
	 * @param name
	 */
	SubType(TopMediaType topMediaType, String name)
	{
		setTopMediaType(topMediaType);
		setName(name);
	}
	
	/**
	 * Create the sub type
	 * 
	 * @param topMediaType - parent top media type
	 * @param name - name of the sub type
	 * @param description - description of the top media type.
	 */
	SubType(TopMediaType topMediaType, String name, String description)
	{
		this(topMediaType, name);
		setDescription(description);
	}
	
	/**
	 * Get the short description for this mime
	 * sub type
	 * 
	 * @return
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * Get the short description of this sub type 
	 * 
	 * @param shortDescription
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * Get the top media type family this sub type
	 * belongs to.
	 * 
	 * @return the top media type family
	 */
	public TopMediaType getTopMediaType() {
		return topMediaType;
	}

	/**
	 * Set the top media type family for this sub type
	 * 
	 * @param topMediaType
	 */
	void setTopMediaType(TopMediaType topMediaType) {
		this.topMediaType = topMediaType;
	}
	
	
	/**
	 * Create a sub type extension.
	 * 
	 * @param extension
	 * @return
	 * @throws DuplicateNameException if extension already exists
	 */
	public SubTypeExtension createExtension(String extension) throws DuplicateNameException
	{
		SubTypeExtension ext = new SubTypeExtension(this, extension);
		this.addExtension(ext);
		return ext;
	}
	
	/**
	 * Add an extension - this changes the data to reflect 
	 * an extension for this sub type.
	 * 
	 * @param extension
	 */
	public void addExtension(SubTypeExtension extension) throws DuplicateNameException
	{
		if( getExtension(extension.getName()) != null )
		{
			throw new DuplicateNameException("Subtype extension for subtype " + this
					+ " with name already exists", extension.getName() );
		}
		extension.setSubType(this);
		extension.setMimeType(getTopMediaType().getName() +  
		SubTypeExtension.MIME_TYPE_SEPERATOR + getName());
		extensions.add(extension);
	}
	
	/**
	 * Remove an extension from this subtype
	 * 
	 * @param extension to remove
	 * @return true if the extension is removed
	 */
	private boolean removeExtension(SubTypeExtension extension)
	{
		extension.setSubType(null); 
		return extensions.remove(extension);
	}
	
	/**
	 * Remove an extension from this subtype
	 * 
	 * @param extensionName to remove
	 * @return true if the extension is removed
	 */
	public boolean removeExtension(String extensionName)
	{
		return removeExtension(getExtension(extensionName));
	}
	
	/**
	 * Remove an extension from this subtype
	 * 
	 * @param id of extension to remove
	 * @return true if the extension is removed
	 */
	public boolean removeExtension(Long id)
	{
		return removeExtension(getExtension(id));
	}
	
	/**
	 * Returns the extension if the extension name exists otherwise 
	 * returns null
	 * 
	 * @param extensionName
	 * @return the found extension or null if it is not found
	 */
	public SubTypeExtension getExtension(String extensionName)
	{
		SubTypeExtension ext = null;
		
		for( SubTypeExtension e: extensions)
		{
			if( e.getName().equals(extensionName)){
				ext = e;
			}
		}
		
		return ext;
	}
	
	/**
	 * Returns the extension if the id exists otherwise 
	 * returns null - null is always returned if the id is
	 * 0
	 * 
	 * @param extensionName
	 * @return the found extension or null if it is not found
	 */
	public SubTypeExtension getExtension(Long id)
	{
		SubTypeExtension ext = null;
		
		if( id == 0 )
		{
			return ext;
		}
		
		for( SubTypeExtension e: extensions)
		{
			if( e.getId().equals(id)){
				ext = e;
			}
		}
		
		return ext;
	}
	
	/**
	 * Get an unmodifiable 
	 *  set of extensions available for this mime sub type
	 *  
	 * @return
	 */
	public Set<SubTypeExtension> getExtensions() {
		return Collections.unmodifiableSet(extensions);
	}

	/**
	 * Set the set of extensions for this sub type
	 * 
	 * @param extensions
	 */
	public void setExtensions(Set<SubTypeExtension> extensions) {
		this.extensions = extensions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Name = ");
	
		sb.append(name);
		sb.append(" Description = ");
		sb.append(description);
		sb.append(" id = ");
		sb.append(id);
		sb.append("short description = ");
		sb.append(shortDescription);
		sb.append(" version = ");
		sb.append(version);
		sb.append("]");
		
		return sb.toString();  
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return name == null ? 0 : name.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof SubType)) return false;

		final SubType subType = (SubType) o;

		if( ( name != null && !name.equals(subType.getName()) ) ||
			( name == null && subType.getName() != null ) ) return false;

		return true;
	}

}
