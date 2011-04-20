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

package edu.ur.ir.marc;

import edu.ur.metadata.marc.MarcDataField;
import edu.ur.metadata.marc.MarcSubField;
import edu.ur.persistent.BasePersistent;

public class MarcContentTypeSubField extends BasePersistent {

	// eclipse generated id
	private static final long serialVersionUID = 5546442738969048025L;
	
	// the parent marc datafield
	private MarcDataField marcDataField;
	
	// sub field to use
	private MarcSubField marcSubField;

	// value to use.
	private String value;
	
	
	// package protected constructor
	MarcContentTypeSubField(){}
	
	/**
	 * Default constructor.
	 * 
	 * @param marcDataField - parent marc data field
	 * @param marcSubField - sub field value
	 * @param value - value to set in the subfield.
	 */
	public MarcContentTypeSubField(MarcDataField marcDataField, MarcSubField marcSubField, String value)
	{
		this.setMarcDataField(marcDataField);
		this.setMarcSubField(marcSubField);
		this.setValue(value);
	}
	
	
	public void setMarcDataField(MarcDataField marcDataField) {
		this.marcDataField = marcDataField;
	}

	public void setMarcSubField(MarcSubField marcSubField) {
		this.marcSubField = marcSubField;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public MarcDataField getMarcDataField() {
		return marcDataField;
	}

	public MarcSubField getMarcSubField() {
		return marcSubField;
	}

	public String getValue() {
		return value;
	}

}
