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

package edu.ur.ir.file;

import edu.ur.persistent.CommonPersistent;

/**
 * Represents a type of file that has been derived from another
 * file.
 * 
 * @author Nathan Sarr
 *
 */
public class TransformedFileType extends CommonPersistent{
	
	
	/**  Eclipse generated id. */
	private static final long serialVersionUID = -2988139527242186355L;
	
	/** primary thumbnail type */
	public static final String PRIMARY_THUMBNAIL = "PRIMARY_THUMBNAIL";
	
	
	/** internal systemCode for this transformed file type */
	private String systemCode;
	
	/**
	 * Pacakge protected constructor. 
	 */
	TransformedFileType(){}
	
	/**
	 * A name representing the type of derrived file 
	 * that will be produced.
	 * 
	 * @param name
	 */
	public TransformedFileType(String name)
	{
		setName(name);
	}
	
	/**
	 * Create a derived file type with the given name and systemCode.
	 * 
	 * @param name
	 * @param systemCode
	 */
	public TransformedFileType(String name, String code)
	{
		setSystemCode(code);
		setName(name);
	}
	
	
	/**
	 * Return the systemCode for this derived file type.
	 * 
	 * @return
	 */
	public String getSystemCode() {
		return systemCode;
	}

	/**
	 * Set the systemCode for this derived file type.
	 * 
	 * @param systemCode
	 */
	public void setSystemCode(String code) {
		this.systemCode = code;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" version = ");
		sb.append(version);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" description = ");
		sb.append(description);
		sb.append(" systemCode = ");
		sb.append(systemCode);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof TransformedFileType)) return false;

		final TransformedFileType other = (TransformedFileType) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}


}
