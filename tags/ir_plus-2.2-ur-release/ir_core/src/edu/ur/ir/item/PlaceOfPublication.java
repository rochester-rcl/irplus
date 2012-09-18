/**  
   Copyright 2008 - 2011 University of Rochester

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
 * Lists the place of publication.
 * 
 * @author Nathan Sarr
 *
 */
public class PlaceOfPublication extends CommonPersistent{

	// eclipse generated id
	private static final long serialVersionUID = -1024868974731310094L;
	
	// represents the two to three letter code for place of publication
	private String letterCode;
	
	
	/**
	 * Default constructor.
	 */
	public PlaceOfPublication(){}
	
	/**
	 * Constructor setting name.
	 * 
	 * @param name - name of the location
	 */
	public PlaceOfPublication(String name)
	{
		setName(name);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param name - name of the location
	 * @param description - description of the location
	 */
	public PlaceOfPublication(String name, String description)
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
		sb.append( " name = ");
		sb.append(name);
		sb.append(" letter code = ");
		sb.append(letterCode);
		sb.append(" description = ");
		sb.append(description);
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
		if (!(o instanceof PlaceOfPublication)) return false;

		final PlaceOfPublication other = (PlaceOfPublication) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}

	/**
	 * Get the 2 - 3 letter place of location code.
	 * 
	 * @return
	 */
	public String getLetterCode() {
		return letterCode;
	}

	/**
	 * Set the 2 - 3 letter place of location code.
	 * 
	 * @param letterCode
	 */
	public void setLetterCode(String letterCode) {
		this.letterCode = letterCode;
	}

}
