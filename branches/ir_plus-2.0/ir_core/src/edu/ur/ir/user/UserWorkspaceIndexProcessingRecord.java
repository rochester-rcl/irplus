package edu.ur.ir.user;

import java.sql.Timestamp;
import java.util.Date;

import edu.ur.ir.FileSystem;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a record for processing a user workspace item.
 * 
 * @author Nathan Sarr
 *
 */
public class UserWorkspaceIndexProcessingRecord extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 9115157956212107384L;

	/** type of data to be processed */
	private String type;
	
	/** workspaceItemId of the workspace item */
	private Long workspaceItemId;
	
	/** id of the user */
	private Long userId;
	
	/** Type of processing to be completed on the record */
	private IndexProcessingType indexProcessingType;
	
	/** Date the record was last created */
	private Timestamp createdDate;
	
	/** indicates the record should be skipped */
	private boolean skipRecord = false;
	
	/** reason why the record should be skipped */
	private String skipReason;
	
	/**
	 * Package protected constructor
	 */
	UserWorkspaceIndexProcessingRecord(){}
	
	/**
	 * 
	 * @param fileSystemType
	 * @param workspaceItemId
	 * @param indexProcessingType
	 */
	public UserWorkspaceIndexProcessingRecord(FileSystem fileSystem,
			IndexProcessingType indexProcessingType, Long userId)
	{
		setType(fileSystem.getFileSystemType().getType());
		setWorkspaceItemId(fileSystem.getId());
		this.createdDate = new Timestamp(new Date().getTime());
        setIndexProcessingType(indexProcessingType);
        setUserId(userId);
	}

	/**
	 * Get the type should match the file system type.
	 * 
	 * @return file system type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the type.
	 * 
	 * @param dataType
	 */
	void setType(String dataType) {
		this.type = dataType;
	}

	/**
	 * Get the workspace item id.
	 * 
	 * @return workspace item id.
	 */
	public Long getWorkspaceItemId() {
		return workspaceItemId;
	}

	/**
	 * Set the workspace item id.
	 * 
	 * @param id
	 */
	void setWorkspaceItemId(Long id) {
		this.workspaceItemId = id;
	}

	/**
	 * Get the index processing type.
	 * 
	 * @return - index processing type
	 */
	public IndexProcessingType getIndexProcessingType() {
		return indexProcessingType;
	}

	/**
	 * Set the index processing type.
	 * 
	 * @param indexProcessingType
	 */
	void setIndexProcessingType(IndexProcessingType indexProcessingType) {
		this.indexProcessingType = indexProcessingType;
	}

	/**
	 * Get the date the record was created.
	 * 
	 * @return
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * Get the user id.
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Set the user id.
	 * 
	 * @param userId
	 */
	void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int value = 0;
		value += indexProcessingType == null ? 0 : indexProcessingType .hashCode();
		value += userId == null ? 0 : userId .hashCode();
		value += workspaceItemId == null ? 0 : workspaceItemId .hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserWorkspaceIndexProcessingRecord)) return false;

		final UserWorkspaceIndexProcessingRecord other = (UserWorkspaceIndexProcessingRecord) o;

		if( ( indexProcessingType != null && !indexProcessingType.equals(other.getIndexProcessingType()) ) ||
			( indexProcessingType == null && other.getIndexProcessingType() != null ) ) return false;
		
		if( ( userId  != null && !userId.equals(other.getUserId ()) ) ||
			( userId  == null && other.getUserId () != null ) ) return false;
		
		if( ( workspaceItemId  != null && !workspaceItemId.equals(other.getWorkspaceItemId ()) ) ||
			( workspaceItemId  == null && other.getWorkspaceItemId () != null ) ) return false;

		return true;
	}
	
	
	/**
	 * Return the string value of this 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[type = ");
		sb.append(type);
		sb.append(" workspace Item Id = ");
		sb.append(workspaceItemId);
		sb.append(" user Id = ");
		sb.append(userId);
		sb.append(" id = ");
		sb.append(id);
		sb.append(" version = ");
		sb.append(version);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Returns true if the record should be skipped.
	 * 
	 * @return
	 */
	public boolean getSkipRecord()
	{
		return skipRecord;
	}

	/**
	 * Set the skip record status.
	 * 
	 * @param skipRecord
	 */
	public void setSkipRecord(boolean skipRecord) {
		this.skipRecord = skipRecord;
	}

	/**
	 * Get the skip record reason.
	 * 
	 * @return
	 */
	public String getSkipReason() {
		return skipReason;
	}

	/**
	 * Set the skip record reason.
	 * 
	 * @param skipReason
	 */
	public void setSkipReason(String skipReason) {
		this.skipReason = skipReason;
	}
}
