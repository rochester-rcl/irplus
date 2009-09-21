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
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public interface FileDownloadInfoDAO extends CountableDAO, 
CrudDAO<FileDownloadInfo> {

	/**
	 * Get a file download info for specified IP address, file Id and download date
	 * 
	 * @param  ipAddress Ip address downloading the file
	 * @param fileId Id of IrFile being downloaded
	 * @param date - date of the download
	 * 
	 * @return Download information
	 */
	public FileDownloadInfo getFileDownloadInfo(String ipAddress, Long fileId, Date date);
	
	/**
	 * Gets the count of downloads for all collections within the repository.
	 * 
	 * @return
	 */
	public Long getNumberOfFileDownloadsForIrFile(Long irFileId);
		
	/**
	 * This retrieves all file download info objects that are currently in the ignore
	 * ip ranges.
	 * 
	 * @param rowStart - row to start at
	 * @param maxResults - maximum number of results to return.
	 * 
	 * @return  the list of file download info objects that are ignored.
	 */
	public List<FileDownloadInfo> getDownloadInfoIgnored(int start, int maxResults);
	
	/**
	 * Get the count of file download info's that would be ignored.
	 * 
	 * @return count of ignored download info values.
	 */
	public Long getDownloadInfoIgnoredCount();
	

}
