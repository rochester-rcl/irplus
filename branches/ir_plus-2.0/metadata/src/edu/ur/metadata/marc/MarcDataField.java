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
 * Represents a generic marc data field.
 * 
 * @author Nathan Sarr
 *
 */
public class MarcDataField extends CommonPersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -3387071569468295421L;
	
	// indicates the field is repeatable
	private boolean repeatable;
	
	//indicates the code (100/200/....)
	private String code;



	/**  Default constructor */
	MarcDataField(){}
	
	/**
	 * Create a dublin core element with the given name.
	 * 
	 * @param name
	 */
	public MarcDataField(String name, boolean repeatable, String code)
	{
		this.setName(name);
		this.setRepeatable(repeatable);
		this.setCode(code);
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
		if (!(o instanceof MarcDataField)) return false;

		final MarcDataField other = (MarcDataField) o;

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
		sb.append(" code = ");
		sb.append(code);
		sb.append(" description = ");
		sb.append( description );
		sb.append("]");
		return sb.toString();
	}
	
	public boolean isRepeatable() {
		return repeatable;
	}

	public void setRepeatable(boolean repeatable) {
		this.repeatable = repeatable;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


}
