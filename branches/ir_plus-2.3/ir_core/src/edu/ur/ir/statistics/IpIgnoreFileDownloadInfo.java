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
 * This is a record for an existing ignore address.  This allows
 * ignore addresses to be stored in a separate area.
 * 
 * @author Nathan Sarr
 *
 */
public class IpIgnoreFileDownloadInfo extends BasePersistent {

	/** eclipse generated id */
	private static final long serialVersionUID = 511549268746634484L;
	
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
	IpIgnoreFileDownloadInfo() {}

	/**
	 * Constructor for file download info
	 * 
	 * @param address Downloading ipaddress
	 * @param irFileId Id of file that is downloaded
	 * @param date Date of download
	 */
	public IpIgnoreFileDownloadInfo(String address, Long fileId, Date downloadDate) {
		setAddress(address);
		this.irFileId = fileId;
		this.downloadDate = downloadDate;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getIrFileId() {
		return irFileId;
	}

	public void setIrFileId(Long fileId) {
		this.irFileId = fileId;
	}

	public Date getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(Date downloadDate) {
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
		if (!(o instanceof IpIgnoreFileDownloadInfo)) return false;

		final IpIgnoreFileDownloadInfo other = (IpIgnoreFileDownloadInfo) o;

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
