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
	private Timestamp dateFailed;
	
	/**  ir file id */
	private Long irFileId;
	
	/** reason for the failure */
	private String failureReason;
	
	/**
	 * Default constructor 
	 * 
	 * @param irFileId
	 * @param failureReason
	 */
	public IrFileTransformationFailureRecord(Long irFileId, String failureReason)
	{
		setDateFailed(new Timestamp(new Date().getTime()));
		setIrFileId(irFileId);
		setFailureReason(failureReason);
	}

	public Timestamp getDateFailed() {
		return dateFailed;
	}

	public void setDateFailed(Timestamp dateFailed) {
		this.dateFailed = dateFailed;
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
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[id = ");
		sb.append(id);
		sb.append(" date created = " );
		sb.append(dateFailed);
		sb.append(" failure reason = ");
		sb.append(failureReason);
		sb.append("]");
		return sb.toString();
		
	}

}
