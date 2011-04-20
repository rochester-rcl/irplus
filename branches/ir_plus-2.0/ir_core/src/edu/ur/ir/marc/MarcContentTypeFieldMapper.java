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

import edu.ur.ir.item.ContentType;
import edu.ur.persistent.BasePersistent;

/**
 * Way to convert content type information to a marc record 
 * 
 * @author Nathan Sarr
 *
 */
public class MarcContentTypeFieldMapper extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 6179552141202045278L;
	
	public static final int DATA_006_LENGTH = 18;
	public static final int DATA_007_LENGTH = 14;
	public static final int DATA_008_LENGTH = 40;

	// content type this is a decoder field
	private ContentType contentType;

	// template control fields - 006: 18 characters
	private String controlField006;

	// template control fields - 007: 14 characters
	private String controlField007;
	
	// template control field - 008: 40 characters
	private String controlField008;
	
	// leader record status
	private char recordStatus = ' ';

	// leader record type
	private char typeOfRecord = ' ';
	
	// leader bib level
	private char bibliographicLevel = ' ';
	
	// leader type of control
	private char typeOfControl = ' ';
	
	// leader encoding level
	private char encodingLevel = ' ';
	
	// descriptive cataloging form
	private char descriptiveCatalogingForm = ' ';
	
	/**
	 * Content type field mapper
	 */
	MarcContentTypeFieldMapper()
	{
		char[] char006Data = new char[DATA_006_LENGTH];
		for (int index = 0; index < char006Data.length; index++ )
		{
			char006Data[index] = ' ';
		}
		controlField006 = new String(char006Data); 
		
		char[] char007Data = new char[DATA_007_LENGTH];
		char007Data[0] = 'z';
		for (int index = 1; index < char007Data.length; index++ )
		{
			char007Data[index] = '|';
		}
		controlField007 = new String(char007Data); 
		
		char[] char008Data = new char[DATA_008_LENGTH];
		for (int index = 0; index < char008Data.length; index++ )
		{
			char008Data[index] = ' ';
		}
		controlField008 = new String(char008Data); 
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param contentType
	 */
	public MarcContentTypeFieldMapper(ContentType contentType)
	{
		this();
		setContentType(contentType);
	}
	
	/**
	 * Marc 05 position.
	 * 
	 * @return
	 */
	public char getRecordStatus() {
		return recordStatus;
	}

	/**
	 * Marc 05 position
	 * 
	 * @param recordStatus
	 */
	public void setRecordStatus(char recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * Marc 06 position
	 * 
	 * @return
	 */
	public char getTypeOfRecord() {
		return typeOfRecord;
	}

	/**
	 * Marc 06 position.
	 * 
	 * @param typeOfRecord
	 */
	public void setTypeOfRecord(char typeOfRecord) {
		this.typeOfRecord = typeOfRecord;
	}

	/**
	 * Marc 07 position.
	 * 
	 * @return
	 */
	public char getBibliographicLevel() {
		return bibliographicLevel;
	}

	/**
	 * Marc 07 position.
	 * 
	 * @param bibliographicLevel
	 */
	public void setBibliographicLevel(char bibliographicLevel) {
		this.bibliographicLevel = bibliographicLevel;
	}

	/**
	 * Marc 08 position.
	 * 
	 * @return
	 */
	public char getTypeOfControl() {
		return typeOfControl;
	}

	/**
	 * Marc 08 position.
	 * 
	 * @param typeOfControl
	 */
	public void setTypeOfControl(char typeOfControl) {
		this.typeOfControl = typeOfControl;
	}

	/**
	 * Marc 17 position.
	 * 
	 * @return
	 */
	public char getEncodingLevel() {
		return encodingLevel;
	}

	/**
	 * Marc 17 position.
	 * 
	 * @param encodingLevel
	 */
	public void setEncodingLevel(char encodingLevel) {
		this.encodingLevel = encodingLevel;
	}

	/**
	 * Marc 18 position.
	 * 
	 * @return
	 */
	public char getDescriptiveCatalogingForm() {
		return descriptiveCatalogingForm;
	}

	/**
	 * Marc 18 position.
	 * 
	 * @param descriptiveCatalogingForm
	 */
	public void setDescriptiveCatalogingForm(char descriptiveCatalogingForm) {
		this.descriptiveCatalogingForm = descriptiveCatalogingForm;
	}
	
	/**
	 * Get the 006 control field.
	 * 
	 * @return
	 */
	public String getControlField006() {
		return controlField006;
	}

	/**
	 * Sets the character fields for the 006 - this must be 18 characters long 
	 * 
	 * @param controlFieldChar006
	 */
	public void setControlField006(char[] controlFieldChar006) {
		
		if(controlFieldChar006.length != DATA_006_LENGTH )
		{
			throw new IllegalStateException("Character data must be 18 characters long");
		}
		else
		{
			this.controlField006  = new String(controlFieldChar006);
		}
	}

	/**
	 * Get the 007 control field.
	 * 
	 * @return
	 */
	public String getControlField007() {
		return controlField007;
	}

	/**
	 * Sets the 007 control field - this must be 14 characters long. 
	 * 
	 * @param controlFieldChar007
	 */
	public void setControlField007(char[] controlFieldChar007) {
		if(controlFieldChar007.length != DATA_007_LENGTH )
		{
			throw new IllegalStateException("Character data must be 14 characters long");
		}
		else
		{
			this.controlField007  = new String(controlFieldChar007);
		}
	}

	/**
	 * Get the 008 control field as a string.
	 * 
	 * @return
	 */
	public String getControlField008() {
		return controlField008;
	}

	/**
	 * Sets the 008 control field this must be 40 characters long.
	 * 
	 * @param controlFieldChar008
	 */
	public void setControlField008(char[] controlFieldChar008) {
		if(controlFieldChar008.length != DATA_008_LENGTH )
		{
			throw new IllegalStateException("Character data must be 40 characters long");
		}
		else
		{
			this.controlField008  = new String(controlFieldChar008);
		}
	}
	
	/**
	 * Get the content type for these fields.
	 * 
	 * @return
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * Set the content type for these fields.
	 * 
	 * @param contentType
	 */
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
	
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += contentType == null ? 0 : contentType.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" controlField006 = " );
		sb.append(controlField006);
		sb.append(" controlField007 = " );
		sb.append(controlField007);
		sb.append(" controlField008 = " );
		sb.append(controlField008);
		sb.append(" recordStatus = " );
		sb.append(recordStatus);
		sb.append(" typeOfRecord = " );
		sb.append(typeOfRecord);
		sb.append(" bibliographicLevel = " );
		sb.append(bibliographicLevel);
		sb.append(" typeOfControl = " );
		sb.append(typeOfControl);
		sb.append(" encodingLevel = " );
		sb.append(encodingLevel);
		sb.append(" descriptiveCatalogingForm = " );
		sb.append(descriptiveCatalogingForm);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof MarcContentTypeFieldMapper)) return false;

		final MarcContentTypeFieldMapper other = (MarcContentTypeFieldMapper) o;

		if( ( contentType != null && !contentType.equals(other.getContentType()) ) ||
			( contentType == null && other.getContentType() != null ) ) return false;
		
		return true;
	}


}
