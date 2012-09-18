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


package edu.ur.ir.file;

import java.sql.Timestamp;
import java.util.Date;

import edu.ur.persistent.BasePersistent;

/**
 * Represents a failure when trying to transform a file - for example - trying
 * to transform a file into a thumbnail.
 * 
 * @author Nathan Sarr
 *
 */
public class IrFileTransformationFailureRecord extends BasePersistent{

	/** eclipse generated id */
	private static final long serialVersionUID = 4048273694853055759L;
	
	/** date the failure record was created  */
	private Timestamp dateCreated;
	
	/**  ir file id */
	private Long irFileId;
	
	/** reason for the failure */
	private String failureReason;
	
	/**
	 * Package protected constructor
	 */
	IrFileTransformationFailureRecord(){}
	
	/**
	 * Default constructor 
	 * 
	 * @param irFileId
	 * @param failureReason
	 */
	public IrFileTransformationFailureRecord(Long irFileId, String failureReason)
	{
		setDateCreated(new Timestamp(new Date().getTime()));
		setIrFileId(irFileId);
		setFailureReason(failureReason);
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Timestamp dateFailed) {
		this.dateCreated = dateFailed;
	}

	public Long getIrFileId() {
		return irFileId;
	}

	public void setIrFileId(Long irFileId) {
		this.irFileId = irFileId;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	
	public int hashCode()
	{
		int value = 0;
		value += dateCreated == null ? 0 : dateCreated.hashCode();
		value += id == null ? 0 : id.hashCode();
		value += irFileId == null ? 0 : irFileId.hashCode();
		return value;
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IrFileTransformationFailureRecord)) return false;

		final IrFileTransformationFailureRecord other = (IrFileTransformationFailureRecord) o;

		if( ( dateCreated != null && !dateCreated.equals(other.getDateCreated()) ) ||
			( dateCreated == null && other.getDateCreated() != null ) ) return false;
		
		if( ( id != null && !id.equals(other.getId()) ) ||
			( id == null && other.getId() != null ) ) return false;
		
		if( ( irFileId != null && !irFileId.equals(other.getIrFileId()) ) ||
			( irFileId == null && other.getIrFileId() != null ) ) return false;
	
		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[id = ");
		sb.append(id);
		sb.append(" IR file id = ");
		sb.append(irFileId);
		sb.append(" date created = " );
		sb.append(dateCreated);
		sb.append(" failure reason = ");
		sb.append(failureReason);
		sb.append("]");
		return sb.toString();
		
	}

}
