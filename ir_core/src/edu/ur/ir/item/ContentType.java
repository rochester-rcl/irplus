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

package edu.ur.ir.item;

import edu.ur.persistent.CommonPersistent;

/**
 * Describes the media content type of the item (Book, Video, Music).
 * 
 * 
 * @author Nathan Sarr.
 *
 */
public class ContentType extends CommonPersistent{
	
	/**  Eclipse Generated id. */
	private static final long serialVersionUID = -5675769824332730328L;
	
	/** System code for this contributor type */
	private String uniqueSystemCode;

	/**
	 * Default constructor 
	 */
	ContentType(){}
	
	/**
	 * Create a content type with a name.
	 * 
	 * @param name
	 */
	public ContentType(String name)
	{
		setName(name);
	}
	
	/**
	 * Create a content type with the given name and description.
	 * 
	 * @param name - name of the content type
	 * @param description - description of the content type
	 */
	public ContentType(String name, String description)
	{
		this(name);
		setDescription(description);
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" name = " );
		sb.append(name);
		sb.append(" system code = ");
		sb.append(uniqueSystemCode);
		sb.append(" description = ");
		sb.append(description);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ContentType)) return false;

		final ContentType other = (ContentType) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}

	public String getUniqueSystemCode() {
		return uniqueSystemCode;
	}

	public void setUniqueSystemCode(String uniqueSystemCode) {
		this.uniqueSystemCode = uniqueSystemCode;
	}
}
