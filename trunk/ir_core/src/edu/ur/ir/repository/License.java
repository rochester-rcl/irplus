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

package edu.ur.ir.repository;

import edu.ur.ir.user.IrUser;
import edu.ur.persistent.CommonPersistent;

/**
 * A license saved in the IR.
 * 
 * @author Nathan Sarr.
 *
 */
public class License extends CommonPersistent{

	/**  Generated id. */
	private static final long serialVersionUID = 2380384082339838487L;
	
	/** User who created the license  */
	private IrUser creator;

	/** text for the license */
	private String text;

	/**
	 * Package protected constructor
	 */
	License(){}
	
	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param licenseVersion
	 */
	public License(String name, String text, IrUser creator)
	{
		setName(name);
		setText(text);
		setCreator(creator);
	}
	
	/**
	 * License text.
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * User who created the license.
	 * 
	 * @return
	 */
	public IrUser getCreator() {
		return creator;
	}

	/**
	 * User who created the license.
	 * 
	 * @param creator
	 */
	void setCreator(IrUser creator) {
		this.creator = creator;
	}


	/**
	 * Licnese text.
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += getName() == null ? 0 : getName().hashCode();
		value += getText() == null? 0 : getText().hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof License)) return false;

		final License other = (License) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		if( ( getText() != null && !getText().equals(other.getText()) ) ||
		    ( getText() == null && other.getText() != null ) ) return false;

		return true;
	}
	
	


	/** 
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("[ id = ");
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" text = ");
		sb.append(text);
		sb.append("]");
		return sb.toString();
	}

}
