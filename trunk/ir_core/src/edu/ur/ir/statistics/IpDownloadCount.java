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


/**
 * Represents the download count for a given ip address.  This
 * class can be used for summing download counts accross a 
 * specific ip address.  This allows for checking for invalid
 * download counts that should be removed.
 * 
 * @author Nathan Sarr
 *
 */
public class IpDownloadCount {
	
	/** Complete IP address */
	private String ipAddress;
	
	/** download count for the ip address */
	private Long downloadCount;
	
	public IpDownloadCount(String ipAddress, Long downloadCount)
	{
		setIpAddress(ipAddress);
		if( downloadCount != null )
		{
		    setDownloadCount(downloadCount);
		}
		else
		{
			setDownloadCount(0l);
		}
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Long getDownloadCount() {
		return downloadCount;
	}

	void setDownloadCount(Long downloadCount) {
		this.downloadCount = downloadCount;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IpDownloadCount)) return false;

		final IpDownloadCount other = (IpDownloadCount) o;

		if( ( ipAddress != null && !ipAddress.equals(other.getIpAddress()) ) ||
			( ipAddress == null && other.getIpAddress() != null ) ) return false;
		
	
		return true;
	}	
	
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += ipAddress == null ? 0 : ipAddress.hashCode();
		return value;
	}	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(" ipAddress = ");
		sb.append(ipAddress);
		sb.append(" downloadCount = ");
		sb.append(downloadCount);
		sb.append("]");
		
		return sb.toString();
		
	}

}
