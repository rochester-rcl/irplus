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

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;;

/**
 * Class to deal with the mime types.
 * 
 * @author Nathan Sarr
 *
 */
public class InMemoryMimeTypeService implements BasicMimeTypeService{
	
	/**
	 * Eclipse generated id
	 */
	private static final long serialVersionUID = 8511350916245787659L;
	
	/**
	 * Separator for mime type
	 */
	private static final String MIME_TYPE_SEPERATOR = "/";

	/**
	 * Set of top media types.
	 */
	private Set<TopMediaType> topMediaTypes = new HashSet<TopMediaType>();
	
	/**
	 * Creates and adds a mime top media type.
	 * 
	 * @param name - name of the top media type
	 * @param desciption - description of the top media type.
	 * 
	 * @return the created top media type
	 */
	public TopMediaType createTopMediaType(String name, String description)
	{
		TopMediaType topMediaType = this.findTopMediaType(name);
		if( topMediaType == null )
		{
		    topMediaType = new TopMediaType(name, description);
		    topMediaTypes.add(topMediaType);
		}
		return topMediaType;
	}

	/**
	 * Get the set of mime top media types.  Returns an
	 * unmodifiable set.
	 * 
	 * @return
	 */
	public Set<TopMediaType> getTopMediaTypes() {
		return Collections.unmodifiableSet(topMediaTypes);
	}

	/**
	 * Set the top media types.
	 * 
	 * @param topMediaTypes
	 */
	void setTopMediaTypes(Set<TopMediaType> topMediaTypes) {
		this.topMediaTypes = topMediaTypes;
	}

	/**
	 * Return the string mime type value
	 * 
	 * topMediaType/subType
	 * 
	 * @see edu.ur.file.mime.BasicMimeTypeService#findMimeType(java.lang.String)
	 */
	public String findMimeType(String extension) {
		for(TopMediaType topMediaType: topMediaTypes)
		{
			for(SubType subType: topMediaType.getSubTypes())
			{
				for( SubTypeExtension subTypeExtension : subType.getExtensions())
				{
					if( extension.equalsIgnoreCase(subTypeExtension.getName()))
					{
						return topMediaType.getName() + MIME_TYPE_SEPERATOR + subType.getName();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Find the top media type using the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public TopMediaType findTopMediaType(String name) {
		for(TopMediaType topMediaType: topMediaTypes)
		{
			if(name.equals(topMediaType.getName()))
			{
				return topMediaType;
			}
		}
		return null;
	}

	/**
	 * Remove the specified top media type.
	 * 
	 * @param name - unique name of the top media type
	 * @return
	 */
	public boolean removeTopMediaType(String name) {
		TopMediaType tmt = null;
		
		for(TopMediaType topMediaType: topMediaTypes)
		{
			if(name.equals(topMediaType.getName()))
			{
				tmt = topMediaType;
			}
		}
		return topMediaTypes.remove(tmt);
	}

	/**
	 * Find the subTypeExtension.
	 * @see edu.ur.file.mime.BasicMimeTypeService#findExtension(java.lang.String)
	 */
	public SubTypeExtension findExtension(String extension) {
		for(TopMediaType topMediaType: topMediaTypes)
		{
			for(SubType subType: topMediaType.getSubTypes())
			{
				for( SubTypeExtension subTypeExtension : subType.getExtensions())
				{
					if( extension.equalsIgnoreCase(subTypeExtension.getName()))
					{
						return subTypeExtension;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Find the top media type.
	 * 
	 * @see edu.ur.file.mime.BasicMimeTypeService#getTopMediaType(java.lang.Long)
	 */
	public TopMediaType findTopMediaType(Long id) {
		
		for(TopMediaType topMediaType: topMediaTypes)
		{
			if(id.equals(topMediaType.getId()))
			{
				return topMediaType;
			}
		}
		return null;
	}

	/**
	 * Remove the top media type from the system.
	 * 
	 * @see edu.ur.file.mime.BasicMimeTypeService#removeTopMediaType(java.lang.Long)
	 */
	public boolean removeTopMediaType(Long id) {
		TopMediaType tmt = null;
		
		for(TopMediaType topMediaType: topMediaTypes)
		{
			if(id.equals(topMediaType.getId()))
			{
				tmt = topMediaType;
			}
		}
		return topMediaTypes.remove(tmt);
	}

	/**
	 * Find the mime type using the specified file.  This is a basic implementation
	 * which only tries to get the extension and return the file type.
	 * 
	 * @see edu.ur.file.mime.BasicMimeTypeService#findMimeType(java.io.File)
	 */
	public String findMimeType(File f) {
		String extension = FilenameUtils.getExtension(f.getName());
		return findMimeType(extension);
	}

}
