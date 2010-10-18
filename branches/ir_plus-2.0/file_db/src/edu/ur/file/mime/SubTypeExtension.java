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


import edu.ur.persistent.CommonPersistent;

/**
 * Represents the known file extensions for the
 * file sub types
 * 
 * @author Nathan Sarr
 *
 */
public class SubTypeExtension  extends CommonPersistent {
	
	/** Seperator for mime types. */
	public static final String MIME_TYPE_SEPERATOR = "/";

	/**  Unique serialization id  */
	private static final long serialVersionUID = -2025048005076605770L;
	
	/**  The mime sub type this belongs to  */
	private SubType subType;
	
	/** mime type for this extension */
	private String mimeType;

	/**
	 * Default constructor
	 */
	SubTypeExtension(){}
	
	/**
	 * Convience constructor
	 * 
	 * @param extension
	 * @param subType parent subType
	 */
	SubTypeExtension(SubType subType, String extension)
	{
		setName(extension);
		setSubType(subType);
		
		setMimeType(subType.getTopMediaType().getName() +  
		MIME_TYPE_SEPERATOR + 
		subType.getName());
	}
	
	/**
	 * Get the parent mime sub type
	 * 
	 * @return parent sub type
	 */
	public SubType getSubType() {
		return subType;
	}

	/**
	 * Set the parent mime sub type
	 * 
	 * @param subType
	 */
	void setSubType(SubType subType) {
		this.subType = subType;
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
		if (!(o instanceof SubTypeExtension)) return false;

		final SubTypeExtension subTypeExtension = (SubTypeExtension) o;

		if( ( name != null && !name.equals(subTypeExtension.getName()) ) ||
			( name == null && subTypeExtension.getName() != null ) ) return false;

		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[SubTypeExtension extension = ");
		sb.append(name);
		sb.append(" mime type = ");
		sb.append(mimeType);
		sb.append(" id = " );
		sb.append(id);
		sb.append( "description = ");
		sb.append(description);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Return the mime type for this extension.
	 * 
	 * @return
	 */
	public String getMimeType()
	{
		return mimeType;
	}

	/**
	 * Set the mime type for this subtype extension
	 * @param mimeType
	 */
	void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
