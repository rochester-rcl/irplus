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

import edu.ur.persistent.BasePersistent;

/**
 * Stores the checksum information for the file info.
 * 
 * @author Nathan Sarr
 *
 */
public class FileInfoChecksum extends BasePersistent{
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = 6519842197106981561L;

	/**  The checksum value  */
	private String checksum;
	
	/**  The checksum algorithm used  */
	private String algorithmType;
	
	/** File info object representing the file the checksum is for */
	private FileInfo fileInfo;
	
	/** Date the checksum was calculated */
	private Timestamp dateCalculated;
	
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

	public Timestamp getDateCalculated() {
		return dateCalculated;
	}

	void setDateCalculated(Timestamp dateCalculated) {
		this.dateCalculated = dateCalculated;
	}
	

}
