package edu.ur.file.db;

import java.sql.Timestamp;
import java.util.Date;

import edu.ur.persistent.BasePersistent;

/**
 * This class deals with the reset history of the file info checksum.
 * 
 * @author Nathan Sarr
 *
 */
public class FileInfoChecksumResetHistory extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -5473495136284347371L;

	/**  The checksum value  */
	private String originalChecksum;

	/** the new checksum value */
	private String newChecksum;
	
	/**  The checksum algorithm used  */
	private String algorithmType;
	
	/** Date the checksum was reset to a new value */
	private Timestamp dateReset;
	
	/** File info checksum that was reset */
	private FileInfoChecksum fileInfoChecksum;
	
	/** user who updated the checksum */
	private Long userId;
	
	/** notes for the reset */
	private String notes;

	/**
	 * Package protected constructor
	 */
	FileInfoChecksumResetHistory(){};
	
	/**
	 * The checksum reset history information
	 * 
	 * @param newChecksum - new checksum value
	 * @param fileInfoChecksum - parent file info checksum BEFORE change
	 * @param userId - id of the user who made the change.
	 * @param notes - set of notes
	 */
	FileInfoChecksumResetHistory (String newChecksum, 
			FileInfoChecksum fileInfoChecksum, Long userId, String notes)
	{
		this.dateReset = new Timestamp(new Date().getTime());
		this.originalChecksum = fileInfoChecksum.getChecksum();
		this.newChecksum =  newChecksum;
		this.algorithmType = fileInfoChecksum.getAlgorithmType();
		this.fileInfoChecksum = fileInfoChecksum;
		this.userId = userId;
		this.notes = notes;
	}

	/**
	 * Get the algorithm type used to calculate the checksum.
	 * 
	 * @return
	 */
	public String getAlgorithmType() {
		return algorithmType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer(" [id = " );
		sb.append(id);
		sb.append("originalChecksum = ");
		sb.append(originalChecksum);
		sb.append("newChecksum = ");
		sb.append(newChecksum);
		sb.append("user id = ");
		sb.append(userId);
		sb.append("notes = ");
		sb.append(notes);
		sb.append("dateReset = ");
		sb.append(dateReset);
		sb.append(" ] ");
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += dateReset == null ? 0 : dateReset.hashCode();
		value += newChecksum == null ? 0 : newChecksum.hashCode();
		value += originalChecksum == null ? 0 : originalChecksum.hashCode();
		value += algorithmType == null ? 0 : algorithmType.hashCode();
		return value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof FileInfoChecksumResetHistory)) return false;

		final FileInfoChecksumResetHistory other = (FileInfoChecksumResetHistory) o;

		if( ( newChecksum != null && !newChecksum.equals(other.getNewChecksum()) ) ||
			( newChecksum == null && other.getNewChecksum() != null ) ) return false;

		if( ( originalChecksum != null && !originalChecksum.equals(other.getOriginalChecksum()) ) ||
			( originalChecksum == null && other.getOriginalChecksum() != null ) ) return false;

		if( ( algorithmType != null && !algorithmType.equals(other.getAlgorithmType()) ) ||
			( algorithmType == null && other.getAlgorithmType() != null ) ) return false;

		if( ( dateReset != null && !dateReset.equals(other.getDateReset()) ) ||
			( dateReset == null && other.getDateReset() != null ) ) return false;
		
		return true;
	}

	/**
	 * Notes for the checksum reset history.
	 * 
	 * @return
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Set the notes for the checksum reset history.
	 * 
	 * @param notes
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Get the original checksum
	 * 
	 * @return
	 */
	public String getOriginalChecksum() {
		return originalChecksum;
	}

	/**
	 * Get the new checksum.
	 * 
	 * @return
	 */
	public String getNewChecksum() {
		return newChecksum;
	}

	/**
	 * Get the date the checksum was reset.
	 * 
	 * @return
	 */
	public Timestamp getDateReset() {
		return dateReset;
	}

	/**
	 * The file info checksum this refers to.
	 * 
	 * @return
	 */
	public FileInfoChecksum getFileInfoChecksum() {
		return fileInfoChecksum;
	}

	/**
	 * User who reset the checksum.
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}
}
