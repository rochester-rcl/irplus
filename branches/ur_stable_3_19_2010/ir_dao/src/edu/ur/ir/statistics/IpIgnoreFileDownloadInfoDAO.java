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

import java.util.Date;
import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;

/**
 * 
 * Interface to save file download info 
 * 
 * @author Nathan Sarr
 *
 */
public interface IpIgnoreFileDownloadInfoDAO extends CountableDAO, 
CrudDAO<IpIgnoreFileDownloadInfo> {

	/**
	 * Get a file download info for specified IP address, file Id and download date
	 * 
	 * @param  ipAddress Ip address downloading the file
	 * @param fileId Id of IrFile being downloaded
	 * @param date - date of the download
	 * 
	 * @return Download information
	 */
	public IpIgnoreFileDownloadInfo getIpIgnoreFileDownloadInfo(String ipAddress, Long fileId, Date date);
	
	/**
	 * This retrieves all ip ignore file download info objects that should no longer be
	 * ignored
	 * 
	 * @param rowStart - row to start at
	 * @param maxResults - maximum number of results to return.
	 * 
	 * @return  the list of ignore file download info objects that should no longer be ignored. 
	 */
	public List<IpIgnoreFileDownloadInfo> getIgnoreInfoNowAcceptable(int rowStart, int maxResults);
	
	/**
	 * Get the count of ignore file download info's that should not be ignored.
	 * 
	 * @return count of ignored download info values that should no longer be ignored.
	 */
	public Long getIgnoreInfoNowAcceptableCount();

}
