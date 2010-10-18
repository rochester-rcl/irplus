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
import java.util.StringTokenizer;

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
	private String ipAddress;
	
	/** Downloading Ip address part 1 */
	private int ipAddressPart1;
	
	/** Downloading Ip address part 2 */
	private int ipAddressPart2;
	
	/** Downloading Ip address part 3 */
	private int ipAddressPart3;
	
	/** Downloading Ip address part 4 */
	private int ipAddressPart4;
	
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
	 * @param ipAddress Downloading ipaddress
	 * @param irFileId Id of file that is downloaded
	 * @param date Date of download
	 */
	public IpIgnoreFileDownloadInfo(String ipAddress, Long fileId, Date downloadDate) {
		setIpAddress(ipAddress);
		this.irFileId = fileId;
		this.downloadDate = downloadDate;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		
		StringTokenizer token = new StringTokenizer(ipAddress, ".");
		
		ipAddressPart1 = Integer.parseInt(token.nextToken());
		ipAddressPart2 = Integer.parseInt(token.nextToken());
		ipAddressPart3 = Integer.parseInt(token.nextToken());
		ipAddressPart4 = Integer.parseInt(token.nextToken());
		
		this.ipAddress = ipAddress;
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

	public int getIpAddressPart1() {
		return ipAddressPart1;
	}

	public void setIpAddressPart1(int ipAddressPart1) {
		this.ipAddressPart1 = ipAddressPart1;
	}

	public int getIpAddressPart2() {
		return ipAddressPart2;
	}

	public void setIpAddressPart2(int ipAddressPart2) {
		this.ipAddressPart2 = ipAddressPart2;
	}

	public int getIpAddressPart3() {
		return ipAddressPart3;
	}

	public void setIpAddressPart3(int ipAddressPart3) {
		this.ipAddressPart3 = ipAddressPart3;
	}

	public int getIpAddressPart4() {
		return ipAddressPart4;
	}

	public void setIpAddressPart4(int ipAddressPart4) {
		this.ipAddressPart4 = ipAddressPart4;
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
		
		if( ( ipAddress != null && !ipAddress.equals(other.getIpAddress()) ) ||
			( ipAddress == null && other.getIpAddress() != null ) ) return false;
		
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
		value += ipAddress == null ? 0 : ipAddress.hashCode();
		value += downloadDate == null ? 0 : downloadDate.hashCode();
		return value;
	}	
	
	public String toString()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" ipAddress = ");
		sb.append(ipAddress);
		sb.append(" ipAddressPart1 = ");
		sb.append(this.ipAddressPart1);
		sb.append(" ipAddressPart2 = ");
		sb.append(this.ipAddressPart2);
		sb.append(" ipAddressPart3 = ");
		sb.append(this.ipAddressPart3);
		sb.append(" ipAddressPart4 = ");
		sb.append(this.ipAddressPart4);
		
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
