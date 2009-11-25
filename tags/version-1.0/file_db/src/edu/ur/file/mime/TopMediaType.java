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

import edu.ur.persistent.CommonPersistent;
import edu.ur.exception.DuplicateNameException;
/**
 * This represents a Mime top Media type
 * 
 * @author Nathan Sarr
 *
 */
public class TopMediaType extends CommonPersistent
{
	/** Unique serial id */
	private static final long serialVersionUID = -8615770105531118951L;
	
	/**  Set of Mime sub types for this top level type */
    private Set<SubType> subTypes = new HashSet<SubType>();
    
	
	/**
	 * Default Constructor 
	 */
	TopMediaType(){}
	
	
	/**
	 * Create a top media type with the specified name.
	 * 
	 * @param name
	 */
	public TopMediaType(String name)
	{
		setName(name);
	}
	
	/**
	 * Create a new top media type with the given name and description.
	 * 
	 * @param name
	 * @param description
	 */
	public TopMediaType(String name, String description)
	{
		this(name);
		setDescription(description);
	}
	
	/**
	 * Create a subType and return it.
	 * 
	 * @param name - name of the sub type
	 * @return
	 * 
	 * @throws DuplicateNameException if sub type with name already exists
	 */
	public SubType createSubType(String name) throws DuplicateNameException
	{
		if( this.getSubType(name) != null )
		{
			throw new DuplicateNameException("Top media type " + this + " already contains sub type", name );
		}
		SubType subType = new SubType(this, name);
		subTypes.add(subType);
		return subType;
	}
	
	/**
	 * Remove a subType from this subtype
	 * 
	 * @param subType to remove
	 * @return true if the subType is removed
	 */
	private boolean removeSubType(SubType subType)
	{
		return subTypes.remove(subType);
	}
	
	/**
	 * Remove a subtype
	 * 
	 * @param sub type name to remove
	 * @return true if the subtype is removed
	 */
	public boolean removeSubType(String subTypeName)
	{
		return removeSubType(getSubType(subTypeName));
	}
	
	/**
	 * Remove an extension from this subtype
	 * 
	 * @param id of extension to remove
	 * @return true if the extension is removed
	 */
	public boolean removeSubType(Long id)
	{
		return removeSubType(getSubType(id));
	}
	
	/**
	 * Returns the sub type if the sub type name exists otherwise 
	 * returns null
	 * 
	 * @param subTypeName
	 * @return the found sub type or null if it is not found
	 */
	public SubType getSubType(String subTypeName)
	{
		SubType subType = null;
		
		for( SubType type: subTypes)
		{
			if( type.getName().equals(subTypeName)){
				subType = type;
			}
		}
		
		return subType;
	}
	
	/**
	 * Returns the sub type if the id exists otherwise 
	 * returns null - null is always returned if the id is
	 * 0
	 * 
	 * @param Sub type id
	 * @return the found subType or null if it is not found
	 */
	public SubType getSubType(Long id)
	{
		SubType subType = null;
		
		if( id == 0 )
		{
			return subType;
		}
		
		for( SubType type: subTypes)
		{
			if( type.getId().equals(id)){
				subType = type;
			}
		}
		
		return subType;
	}

	/**
	 * The set of mime sub types for this top 
	 * level mime type
	 * 
	 * @return
	 */
	public Set<SubType> getSubTypes() {
		return Collections.unmodifiableSet(subTypes);
	}

	/**
	 * Set the set of mime sub types for this top level
	 * type
	 * 
	 * @param subTypes
	 */
	public void setSubTypes(Set<SubType> subTypes) {
		this.subTypes = subTypes;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		
		StringBuffer sb = new StringBuffer("[TopMediaType name = ");
		sb.append(name);
		sb.append(" id = " );
		sb.append(id);
		sb.append( "description = ");
		sb.append(description);
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
		if (!(o instanceof TopMediaType)) return false;

		final TopMediaType topMediaType = (TopMediaType) o;

		if( ( name != null && !name.equals(topMediaType.getName()) ) ||
			( name == null && topMediaType.getName() != null ) ) return false;

		return true;
	}
}
