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

package edu.ur.metadata.dc;

import edu.ur.persistent.CommonPersistent;

/**
 * Represents a simple dublin core element.
 * 
 * @author Nathan Sarr
 *
 */
public class DublinCoreElement extends CommonPersistent {

	/** eclipse generated version uid  */
	private static final long serialVersionUID = 7292156977710663790L;
	
	/**  Default constructor */
	DublinCoreElement(){}
	
	/**
	 * Create a dublin core element with the given name.
	 * 
	 * @param name
	 */
	public DublinCoreElement(String name)
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
		if (!(o instanceof DublinCoreElement)) return false;

		final DublinCoreElement other = (DublinCoreElement) o;

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
