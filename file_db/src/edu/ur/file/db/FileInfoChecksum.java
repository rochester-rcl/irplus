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
package edu.ur.file.db;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.ur.persistent.BasePersistent;

/**
 * Stores the checksum information for the file info.
 * 
 * @author Nathan Sarr
 *
 */
public class FileInfoChecksum extends BasePersistent{
	
	/*  Eclipse generated id */
	private static final long serialVersionUID = 6519842197106981561L;

	/*  The checksum value  */
	private String checksum;
	
	/*  The checksum algorithm used  */
	private String algorithmType;
	
	/* File info object representing the file the checksum is for */
	private FileInfo fileInfo;
	
	/* Date the checksum was calculated */
	private Timestamp dateCalculated;
	
	/* date the checksum was re-calculated */
	private Timestamp dateReCalculated;
	
	/* last time the check passed */
	private Timestamp dateLastCheckPassed;

	/* indicates if the last check passed */
	private boolean reCalculatedPassed = true;
	
	/* the calculated checksum check value on the given date */
	private String reCalculatedValue;
	
	/* indicates the checksum should be checked */
	private boolean reCalculateChecksum = true;

	/*  list of reset history */
	private Set<FileInfoChecksumResetHistory> resetHistory = new HashSet<FileInfoChecksumResetHistory>();

	/**
	 * Package protected constructor
	 */
	FileInfoChecksum(){};
	
	/**
	 * The checksum created.
	 * 
	 * @param checksum
	 * @param checksumAlgorithmType
	 */
	public FileInfoChecksum (String checksum, String algorithmType, FileInfo fileInfo)
	{
		this.checksum = checksum;
		this.algorithmType = algorithmType;
		this.fileInfo = fileInfo;
		this.dateCalculated = new Timestamp(new java.util.Date().getTime());
		this.dateReCalculated = new Timestamp(dateCalculated.getTime());
		this.reCalculatedValue = checksum;
	}

	/**
	 * Get the checksum calculated
	 * 
	 * @return checksum
	 */
	public String getChecksum() {
		return checksum;
	}

	/**
	 * Set the checksum calculated.
	 * 
	 * @param checksum
	 */
	void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	/**
	 * Get the algorithm type used to calculate the checksum.
	 * 
	 * @return
	 */
	public String getAlgorithmType() {
		return algorithmType;
	}

	/**
	 * Set the checksum algorithm calculated.
	 * 
	 * @param checksumAlgorithmType
	 */
	void setAlgorithmType(String algorithmType) {
		this.algorithmType = algorithmType;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{

		StringBuffer sb = new StringBuffer(" [id = " );
		sb.append(id);
		sb.append("Checksum = ");
		sb.append(checksum);
		sb.append( " algorithm type = ");
		sb.append( algorithmType);
		sb.append( " date calculated = " );
		sb.append( dateCalculated );
		sb.append(" date reCalculated = ");
		sb.append(dateReCalculated);
		sb.append(" reCalculated passed = ");
		sb.append(reCalculatedPassed);
		sb.append(" reCalculatedVale = ");
		sb.append(reCalculatedValue);
		sb.append(" reCalculateChecksum = ");
		sb.append(reCalculateChecksum);
		sb.append(" dateLastCheckPassed = ");
		sb.append(dateLastCheckPassed);
		sb.append(" ] ");
		
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		
		value += checksum == null ? 0 : checksum.hashCode();
		value += algorithmType == null ? 0 : algorithmType.hashCode();
		value += fileInfo == null ? 0 : fileInfo.hashCode();
		
		return value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof FileInfoChecksum)) return false;

		final FileInfoChecksum other = (FileInfoChecksum) o;

		if( ( checksum != null && !checksum.equals(other.getChecksum()) ) ||
			( checksum == null && other.getChecksum() != null ) ) return false;
		
		if( ( algorithmType != null && !algorithmType.equals(other.getAlgorithmType()) ) ||
			( algorithmType == null && other.getAlgorithmType() != null ) ) return false;

		if( ( fileInfo != null && !fileInfo.equals(other.getFileInfo()) ) ||
			( fileInfo == null && other.getFileInfo() != null ) ) return false;

		
		return true;
	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	/**
	 * Date the checksum was calculated.
	 * 
	 * @return
	 */
	public Timestamp getDateCalculated() {
		return dateCalculated;
	}

	/**
	 * Date the checksum was checked.
	 * 
	 * @param dateCalculated
	 */
	void setDateCalculated(Timestamp dateCalculated) {
		this.dateCalculated = dateCalculated;
	}
	
	/**
	 * Date the checksum was recalculated.
	 * 
	 * @return
	 */
	public Timestamp getDateReCalculated() {
		return dateReCalculated;
	}

	/**
	 * Set the date that the checksum was recalculated.
	 * 
	 * @param dateReCalculated
	 */
	public void setDateReCalculated(Timestamp dateReCalculated) {
		this.dateReCalculated = dateReCalculated;
	}

	/**
	 * Determine if the recalculation of the checksum passed.
	 * 
	 * @return true if the last checksum recalculation passed.
	 */
	public boolean getReCalculatedPassed() {
		return reCalculatedPassed;
	}

	/**
	 * Set to true if the re-calculation passed.
	 * 
	 * @param reCalculatedPassed
	 */
	public void setReCalculatedPassed(boolean reCalculatedPassed) {
		this.reCalculatedPassed = reCalculatedPassed;
	}

	/**
	 * Get the recalculated value.
	 * 
	 * @return
	 */
	public String getReCalculatedValue() {
		return reCalculatedValue;
	}

	/**
	 * Set the recalculated value.
	 * 
	 * @param reCalculatedValue
	 */
	public void setReCalculatedValue(String reCalculatedValue) {
		this.reCalculatedValue = reCalculatedValue;
	}

	/**
	 * Get the recalculated checksum.
	 * 
	 * @return
	 */
	public boolean getReCalculateChecksum() {
		return reCalculateChecksum;
	}

	/**
	 * Set the recalculated checksum.
	 * 
	 * @param reCalculateChecksum
	 */
	public void setReCalculateChecksum(boolean reCalculateChecksum) {
		this.reCalculateChecksum = reCalculateChecksum;
	}
	
	/**
	 * Last time the checksum was checked and it was the same.
	 * 
	 * @return
	 */
	public Timestamp getDateLastCheckPassed() {
		return dateLastCheckPassed;
	}

	/**
	 * Last time the checksum was checked and it was the same.
	 * 
	 * @return
	 */
	public void setDateLastCheckPassed(Timestamp dateLastCheckPassed) {
		this.dateLastCheckPassed = dateLastCheckPassed;
	}

	
	public FileInfoChecksumResetHistory reset(String newChecksum, 
			 Long userId, String notes){
		FileInfoChecksumResetHistory resetHistory = new FileInfoChecksumResetHistory (newChecksum, 
				this, userId, notes);
		this.checksum = newChecksum;
		this.resetHistory.add(resetHistory);
		return resetHistory;
	}
	
	/**
	 * List of unmodifiable reset histories.
	 * 
	 * @return list of reset histories
	 */
	public Set<FileInfoChecksumResetHistory> getResetHistory() {
		return Collections.unmodifiableSet(resetHistory);
	}

	void setResetHistory(Set<FileInfoChecksumResetHistory> resetHistory) {
		this.resetHistory = resetHistory;
	}

}
