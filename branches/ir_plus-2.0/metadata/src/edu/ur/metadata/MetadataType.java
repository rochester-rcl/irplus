/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.metadata;

import edu.ur.persistent.CommonPersistent;

/**
 * Represents a metadata type.
 * 
 * @author Nathan Sarr
 *
 */
public class MetadataType extends CommonPersistent{

	/** Eclipse generated id */
	private static final long serialVersionUID = 921571423335433279L;
	
	/**  Default constructor */
	MetadataType(){}
	
	/**
	 * Create a dublin core element with the given name.
	 * 
	 * @param name
	 */
	public MetadataType(String name)
	{
		this.setName(name);
	}
	
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		return value;
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof MetadataType)) return false;

		final MetadataType other = (MetadataType) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[");
		sb.append(" id = " );
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" description = ");
		sb.append( description );
		sb.append("]");
		return sb.toString();
	}

}
