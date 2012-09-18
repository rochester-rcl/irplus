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

package edu.ur.ir.statistics;

import java.sql.Timestamp;
import java.util.Date;

import edu.ur.persistent.BasePersistent;

/**
 * Represents a processing record for a file roll ups.
 * This allows a roll up record to be updated on a per file id 
 * value.
 * 
 * @author Nathan Sarr
 *
 */
public class FileDownloadRollUpProcessingRecord extends BasePersistent
{
	/** eclipse generated id */
	private static final long serialVersionUID = 3443629080906093049L;
	
	/** file id to update  */
	private Long irFileId;
	
	/** Date record was created */
	private Timestamp createdDate;
	
	/**
	 * Package protected constructor 
	 */
	FileDownloadRollUpProcessingRecord(){}
	
	/**
	 * Default constructor.
	 * 
	 * @param irFileId
	 */
	public FileDownloadRollUpProcessingRecord(Long irFileId)
	{
		setIrFileId(irFileId);
		setCreatedDate(new Timestamp(new Date().getTime()));
		
	}

	/**
	 * Get the ir file id for this record
	 * @return
	 */
	public Long getIrFileId() {
		return irFileId;
	}

	/**
	 * Set the ir file id for this record.
	 * 
	 * @param irFileId
	 */
	void setIrFileId(Long irFileId) {
		this.irFileId = irFileId;
	}
	
	public int hashCode()
	{
		int value = 0;
		value += irFileId == null ? 0 : irFileId.hashCode();
		value += createdDate == null ? 0 : createdDate.hashCode();
		return value;
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof FileDownloadRollUpProcessingRecord)) return false;

		final FileDownloadRollUpProcessingRecord other = (FileDownloadRollUpProcessingRecord) o;

		if( ( irFileId != null && !irFileId.equals(other.getIrFileId()) ) ||
			( irFileId == null && other.getIrFileId() != null ) ) return false;
		
		if( ( createdDate != null && !createdDate.equals(other.getCreatedDate()) ) ||
			( createdDate == null && other.getCreatedDate() != null ) ) return false;
		
		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" irFileId = ");
		sb.append(irFileId);
		sb.append(" created date = ");
		sb.append(createdDate);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Date the record was created.
	 * 
	 * @return
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * Date the record was created.
	 * 
	 * @param createdDate
	 */
	void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	
	

}
