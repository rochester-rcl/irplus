/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.metadata.marc;

import edu.ur.persistent.CommonPersistent;

/**
 * Represents a relator code in Marc.
 * 
 * @author Nathan Sarr
 *
 */
public class MarcRelatorCode extends CommonPersistent{

	// eclipse generated value
	private static final long serialVersionUID = 6558716097678605759L;
	
	// actual relator code value
	private String relatorCode;
	
	/**  Default constructor */
	 MarcRelatorCode(){}
	
	/**
	 * Create a dublin core element with the given name.
	 * 
	 * @param name
	 */
	public  MarcRelatorCode(String name, String relatorCode)
	{
		this.setName(name);
		this.setRelatorCode(relatorCode);
	}
	
	/**
	 * Set the relator code.
	 * 
	 * @param relatorCode
	 */
	public void setRelatorCode(String relatorCode) {
		this.relatorCode = relatorCode;
	}
	
	/**
	 * Get the relator code.
	 * 
	 * @return
	 */
	public String getRelatorCode() {
		return relatorCode;
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
		if (!(o instanceof MarcRelatorCode)) return false;

		final MarcRelatorCode other = (MarcRelatorCode) o;

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
		sb.append(" relator code = ");
		sb.append(relatorCode);
		sb.append(" description = ");
		sb.append( description );
		sb.append("]");
		return sb.toString();
	}

}
