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
import edu.ur.simple.type.UniqueSystemCodeAware;

/**
 * Describes the language for the data.
 * 
 * @author Nathan Sarr.
 *
 */
public class LanguageType extends CommonPersistent implements UniqueSystemCodeAware{

	/** Generated  */
	private static final long serialVersionUID = -299869379417282998L;
	
	/**  ISO 3 letter code for the language */
	private String iso639_2;
	
	/**  ISO 2 letter code for the language */
	private String iso639_1;
	
	/** Unique system code for this language */
	private String uniqueSystemCode;

	/**
	 * ISO 2 letter code for the language.
	 * 
	 * @return
	 */
	public String getIso639_1() {
		return iso639_1;
	}

	/**
	 * ISO 2 letter code for the language.
	 * 
	 * @param iso639_1
	 */
	public void setIso639_1(String iso639_1) {
		this.iso639_1 = iso639_1;
	}

	/**
	 * ISO 3 letter code for the language.
	 * @return
	 */
	public String getIso639_2() {
		return iso639_2;
	}

	/**
	 * ISO 3 letter code for the language.
	 * 
	 * @param iso639_2
	 */
	public void setIso639_2(String iso639_2) {
		this.iso639_2 = iso639_2;
	}
	
	/**
	 * Default constructor 
	 */
	public LanguageType(){}
	
	/**
	 * Create a language type with the specified name.
	 * 
	 * @param name
	 */
	public LanguageType(String name)
	{
		setName(name);
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
		sb.append(" description = ");
		sb.append(description);
		sb.append(" iso639_1 = ");
		sb.append(iso639_1);
		sb.append(" iso639_2 = ");
		sb.append(iso639_2);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof LanguageType)) return false;

		final LanguageType other = (LanguageType) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}

	
	/**
	 * Get the unique system code for this language.
	 * 
	 * @see edu.ur.simple.type.UniqueSystemCodeAware#getUniqueSystemCode()
	 */
	public String getUniqueSystemCode() {
		return uniqueSystemCode;
	}

	public void setUniqueSystemCode(String uniqueSystemCode) {
		this.uniqueSystemCode = uniqueSystemCode;
	}

}
