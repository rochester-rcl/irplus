package edu.ur.ir.file;

import java.sql.Timestamp;
import java.util.Date;
import edu.ur.persistent.BasePersistent;

/**
 * Represents the failure to index a record.
 * 
 * @author Nathan Sarr
 *
 */
public class IrFileIndexingFailureRecord extends BasePersistent
{
	
	/** eclipse generated id*/
	private static final long serialVersionUID = -412718762580575635L;

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
	public IrFileIndexingFailureRecord(Long irFileId, String failureReason)
	{
		setDateFailed(new Timestamp(new Date().getTime()));
		setIrFileId(irFileId);
		setFailureReason(failureReason);
	}

	/**
	 * Date this record was created.
	 * 
	 * @return
	 */
	public Timestamp getDateFailed() {
		return dateFailed;
	}

	/**
	 * Date this record was created.
	 * 
	 * @param dateFailed
	 */
	public void setDateFailed(Timestamp dateCreated) {
		this.dateFailed = dateCreated;
	}

	/**
	 * Get the ir file id - file the indexing failed on
	 * 
	 * @return
	 */
	public Long getIrFileId() {
		return irFileId;
	}

	/**
	 * Set the file id.
	 * 
	 * @param irFileId
	 */
	void setIrFileId(Long irFileId) {
		this.irFileId = irFileId;
	}

	/**
	 * Reason for the indexing failure.
	 * 
	 * @return
	 */
	public String getFailureReason() {
		return failureReason;
	}

	/**
	 * Reason for the indexing failure.
	 * 
	 * @param failureReason
	 */
	void setFailureReason(String failureReason) {
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
