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

package edu.ur.ir.item.metadata.marc;

import edu.ur.metadata.marc.MarcSubField;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a mapping between a sub field and a given IR+ type.
 * 
 * @author Nathan Sarr
 *
 */
public abstract class MarcSubFieldMapper extends BasePersistent{

	// eclipse generated id
	private static final long serialVersionUID = -2495191063807034813L;
	
	// parent marc data field mapper
	protected MarcDataFieldMapper marcDataFieldMapper;

	// subfield to map this too
	protected MarcSubField marcSubField;
	
	/**
	 * Get the parent marc data field mapper.
	 * 
	 * @return parent marc data field mapper
	 */
	public MarcDataFieldMapper getMarcDataFieldMapper() {
		return marcDataFieldMapper;
	}

	/**
	 * Get the marc subfield value.
	 * 
	 * @return marc sub field mapper
	 */
	public MarcSubField getMarcSubField() {
		return marcSubField;
	}
	
	/**
	 * Set the marc data field mapper.
	 * 
	 * @param marcDataFieldMapper
	 */
	void setMarcDataFieldMapper(MarcDataFieldMapper marcDataFieldMapper) {
		this.marcDataFieldMapper = marcDataFieldMapper;
	}

	/**
	 * Set the marc sub field 
	 * 
	 * @param marcSubField
	 */
	public void setMarcSubField(MarcSubField marcSubField) {
		this.marcSubField = marcSubField;
	}
	



}
