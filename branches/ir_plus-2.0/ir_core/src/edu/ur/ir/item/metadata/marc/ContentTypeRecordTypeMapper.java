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

import edu.ur.ir.item.ContentType;
import edu.ur.metadata.marc.MarcTypeOfRecord;
import edu.ur.persistent.BasePersistent;

/**
 * Maps a content type to a record type.
 * 
 * @author Nathan Sarr
 *
 */
public class ContentTypeRecordTypeMapper extends BasePersistent{

	// eclipse generated id
	private static final long serialVersionUID = 3871322089998068661L;
	
	// content type that is mapped to the marc type of record
	private ContentType contentType;
	
	// leader 06 type of record mapped to the content type
	private MarcTypeOfRecord marcTypeOfRecord;

	/**
	 * Package protected content type of record.
	 */
	ContentTypeRecordTypeMapper(){}
	
	/**
	 * Constructor.
	 * 
	 * @param contentType - content type
	 * @param marcTypeOfRecord - type of record.
	 */
	public ContentTypeRecordTypeMapper(ContentType contentType, MarcTypeOfRecord marcTypeOfRecord)
	{
		setContentType(contentType);
		setMarcTypeOfRecord(marcTypeOfRecord);
	}
	
	/**
	 * Get the content type.
	 * 
	 * @return
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * Set the content type.
	 * 
	 * @param contentType
	 */
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * Set the type of record.
	 *
	 * @return
	 */
	public MarcTypeOfRecord getMarcTypeOfRecord() {
		return marcTypeOfRecord;
	}

	/**
	 * Set the marc type of record.
	 * 
	 * @param marcTypeOfRecord
	 */
	public void setMarcTypeOfRecord(MarcTypeOfRecord marcTypeOfRecord) {
		this.marcTypeOfRecord = marcTypeOfRecord;
	}
	
	/**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += marcTypeOfRecord == null ? 0 : marcTypeOfRecord.hashCode();
    	return hash;
    }
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer("[Marc content type type of record id = ");
		sb.append(id);
		sb.append("]");
		return sb.toString();
    }
    
    /**
     * Person Name Equals.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
    	if (this == o) return true;
		if (!(o instanceof ContentTypeRecordTypeMapper)) return false;

		final ContentTypeRecordTypeMapper other = (ContentTypeRecordTypeMapper) o;

		if( ( marcTypeOfRecord != null && !marcTypeOfRecord.equals(other.getMarcTypeOfRecord()) ) ||
			( marcTypeOfRecord == null && other.getMarcTypeOfRecord() != null ) ) return false;
		
		return true;
    }
}
