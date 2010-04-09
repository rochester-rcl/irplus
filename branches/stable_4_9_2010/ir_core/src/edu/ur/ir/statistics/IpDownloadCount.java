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

}
