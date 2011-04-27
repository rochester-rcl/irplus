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

import java.util.Set;

import edu.ur.ir.item.ContentType;
import edu.ur.metadata.marc.MarcDataField;
import edu.ur.metadata.marc.MarcSubField;
import edu.ur.persistent.BasePersistent;

public class MarcContentTypeDataField extends BasePersistent {
	
	// eclipse generated id */
	private static final long serialVersionUID = -4134768481728804926L;
	
	// parent content type 
	private ContentType contentType;

	// the mark data field to map this content type to.
	private MarcDataField marcDataField;
	
	// first indicator for the data field
	private char ind1;
	
	// second indicator for the data field
	private char ind2;
	
	// set of marc sub fields for this content type data field
	Set<MarcSubField> marcSubFields;
	
	
	MarcContentTypeDataField(){}
	
	/**
	 * Constructor for the mac content type data field.
	 * 
	 * @param contentType - content type for this data field
	 * @param marcDataField - marc data field
	 * @param ind1 - indicator 1 value
	 * @param ind2 - indicator 2 value
	 */
	public MarcContentTypeDataField(ContentType contentType,
			MarcDataField marcDataField, char ind1, char ind2)
	{
		this.setContentType(contentType);
		this.setMarcDataField(marcDataField);
		this.setInd1(ind1);
		this.setInd2(ind2);
	}
	
	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public MarcDataField getMarcDataField() {
		return marcDataField;
	}

	public void setMarcDataField(MarcDataField marcDataField) {
		this.marcDataField = marcDataField;
	}

	public char getInd1() {
		return ind1;
	}

	public void setInd1(char ind1) {
		this.ind1 = ind1;
	}

	public char getInd2() {
		return ind2;
	}

	public void setInd2(char ind2) {
		this.ind2 = ind2;
	}

	public Set<MarcSubField> getMarcSubFields() {
		return marcSubFields;
	}

	public void setMarcSubFields(Set<MarcSubField> marcSubFields) {
		this.marcSubFields = marcSubFields;
	}
	
	

}
