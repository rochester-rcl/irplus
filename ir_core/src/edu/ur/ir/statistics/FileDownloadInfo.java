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

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.ur.persistent.BasePersistent;

/**
 * File download information
 * 
 * @author Sharmila Ranganathan
 *
 */
 public class FileDownloadInfo  extends BasePersistent {

	/** Eclipse generated id */
	private static final long serialVersionUID = -4370857994964827750L;

	/** Complete IP address */
	private String address;
	

	/** Id of file that is downloaded */
	private Long irFileId;
	
	/** Date of download */
	private Date downloadDate;

	/** Number of times downloaded */
	private int downloadCount = 1;
	
	/**
	 * Default constructor
	 */
	FileDownloadInfo() {}

	/**
	 * Constructor for file download info
	 * 
	 * @param ipAddress Downloading ipaddress
	 * @param irFileId Id of file that is downloaded
	 * @param date Date of download
	 */
	public FileDownloadInfo(String address, Long fileId, Date downloadDate) {
		setAddress(address);
		this.irFileId = fileId;
		this.downloadDate = downloadDate;
	}
	
	public String getAddress() {
		return address;
	}

	void setAddress(String address) {
		this.address = address;
	}

	public Long getIrFileId() {
		return irFileId;
	}

	void setIrFileId(Long fileId) {
		this.irFileId = fileId;
	}

	public Date getDownloadDate() {
		return downloadDate;
	}

	void setDownloadDate(Date downloadDate) {
		this.downloadDate = downloadDate;
	}

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int count) {
		this.downloadCount = count;
	}

	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof FileDownloadInfo)) return false;

		final FileDownloadInfo other = (FileDownloadInfo) o;

		if( ( irFileId != null && !irFileId.equals(other.getIrFileId()) ) ||
			( irFileId == null && other.getIrFileId() != null ) ) return false;
		
		if( ( address != null && !address.equals(other.getAddress()) ) ||
			( address == null && other.getAddress() != null ) ) return false;
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		String date1 = null;
		String date2 = null;
		
		if( downloadDate != null)
		{
		    date1 = simpleDateFormat.format(downloadDate);
		}
		
		if(other.getDownloadDate() != null)
		{
			date2 = simpleDateFormat.format(other.getDownloadDate());
		}
		
		if( ( date1 != null && !date1.equals(date2) ) ||
			( date1 == null && date2 != null ) ) return false;
		
		
		return true;
	}	
	
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += irFileId == null ? 0 : irFileId.hashCode();
		value += address == null ? 0 : address.hashCode();
		value += downloadDate == null ? 0 : downloadDate.hashCode();
		return value;
	}	
	
	public String toString()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" ipAddress = ");
		sb.append(address);
		
		
		if(this.downloadDate != null )
		{
			sb.append(" download date in MM/dd/yyyy format = ");
			sb.append(simpleDateFormat.format(downloadDate));
		}
		sb.append(" count = ");
		sb.append(downloadCount);
		sb.append(" irFileId = ");
		sb.append(irFileId);
		
		sb.append("]");
		
		return sb.toString();
		
	}
}
