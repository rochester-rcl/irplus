package edu.ur.ir.user;

import java.sql.Timestamp;

import edu.ur.ir.index.IndexProcessingType;

/**
 * Represents a record for processing a user workspace item.
 * 
 * @author Nathan Sarr
 *
 */
public class UserWorkspaceIndexProcessingRecord {
	
	/** type of data to be processed */
	private String dataType;
	
	/** id of the item */
	private Long id;
	
	/** Type of processing to be completed on the record */
	private IndexProcessingType indexProcessingType;
	
	/** Date the record was last updated */
	private Timestamp createdDate;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IndexProcessingType getIndexProcessingType() {
		return indexProcessingType;
	}

	public void setIndexProcessingType(IndexProcessingType indexProcessingType) {
		this.indexProcessingType = indexProcessingType;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

}
