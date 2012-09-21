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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.item.GenericItem;
import edu.ur.order.OrderType;

/**
 * Interface to get file download statistics
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface DownloadStatisticsService extends Serializable{

	/**
	 * Save file download info 
	 * 
	 * @param fileDownloadInfo
	 */
	public void save(FileDownloadInfo fileDownloadInfo);
	
	/**
	 * Save ignore ip file dlownload info 
	 * 
	 * @param fileDownloadInfo
	 */
	public void save(IpIgnoreFileDownloadInfo ipIgnoreFileDownloadInfo);
	
	/**
	 * Get the file download info 
	 * 
	 * @param id - id of the file download information
	 * @param lock - upgrade the lock mode
	 * 
	 * @return - the found file download info or null if not found
	 */
	public FileDownloadInfo getFileDownloadInfo(Long id,  boolean lock) ;
	
	/**
	 * Get the ignore file download info 
	 * 
	 * @param id - id of the file download information
	 * @param lock - upgrade the lock mode
	 * 
	 * @return - the found file download info or null if not found
	 */
	public IpIgnoreFileDownloadInfo getIpIgnoreFileDownloadInfo(Long id,  boolean lock) ;
	
	/**
	 * Adds the file download info
	 * 
	 * @param ipAddress IP address downloading a file
	 * @param fileId IrFile being downloaded
	 * 
	 * @return the download info object
	 */
	public FileDownloadInfo addFileDownloadInfo(String ipAddress, IrFile irFile);
	
	
	/**
	 * Add an ignore ip address file download.
	 * 
	 * @param ipAddress - ip address to add to the ignore set
	 * @param irFile - file that was downloaded
	 * 
	 * @return the created or updated record
	 */
	public IpIgnoreFileDownloadInfo addIgnoreFileDownloadInfo(String ipAddress, IrFile irFile);
	
	/**
	 * This process a file download.  This determines where the download
	 * should be attributed to.
	 * 
	 * @param ipAddress - ip address to process
	 * @param irFile - file to update the count for.
	 */
	public void processFileDownload(String ipAddress, IrFile irFile);
	
	
	/**
	 * Get the number of file downloads for an ir file.  This does not include
	 * any downloads which refer to igored counts.
	 * 
	 * @param irFileId
	 * @return count of acceptable downloads for an ir file
	 */
	public Long getNumberOfFileDownloadsForIrFile(Long irFileId);
	
	/**
	 * This retrieves all file download roll up processing record objects at the given start positions
	 * with the a maximum of maxResults
	 * 
	 * @param rowStart - row to start at
	 * @param maxResults - maximum number of results to return.
	 * 
	 * @return  the list of file download roll up processing records at the given start position and
	 * with a max of maxResults.
	 */
	public List<FileDownloadRollUpProcessingRecord> getDownloadRollUpProcessingRecords(int start, int maxResults);
	
	
	/**
	 * This retrieves a rollup of download counts for a specified ip address
	 * 
	 * @param rowStart - row to start at
	 * @param maxResults - maximum number of results to return.
	 * @param sort ascending or decending based on ip address
	 * 
	 * @return  the list of download counts per ip-address
	 */
	public List<IpDownloadCount> getIpIgnoreOrderByDownloadCounts(int start, int maxResults, OrderType sortType);
	
	/**
	 * Update all repository counts for ir files in the system.
	 * 
	 * @return the count of records to be updated
	 */
	public Long updateAllRepositoryFileRollUpCounts();
	
	/**
	 * Add a processing record for the specified file id.  If a processing record for the 
	 * specified ir file exits, that record is returned otherwise a new processing record is
	 * created and returned.
	 * 
	 * @param irFileId
	 */
	public FileDownloadRollUpProcessingRecord addDownloadRollUpProcessingRecord(Long irFileId);
	
	/**
	 * Delete the processing record.
	 * 
	 * @param processingRecord
	 */
	public void delete(FileDownloadRollUpProcessingRecord processingRecord);
		
	/**
	 * Delete file Download Info
	 * 
	 * @param fileDownloadInfo
	 */
	public void delete(FileDownloadInfo fileDownloadInfo);
	
	/**
	 * Delete file Download Info
	 * 
	 * @param fileDownloadInfo
	 */
	public void delete(IpIgnoreFileDownloadInfo ipIgnoreFileDownloadInfo);
	
	/**
	 * Get the number of file downloads in the items of the specified collection and its children
	 * 
	 * @param institutionalCollection collection to count for file downloads
	 * 
	 * @return Number of downloads
	 */
	public Long getNumberOfDownloadsForCollectionAndItsChildren(InstitutionalCollection institutionalCollection);

	
	/**
	 * Get the number of file downloads fin the items of the specified collection
	 * 
	 * @param institutionalCollection collection to count for file downloads
	 * 
	 * @return Number of downloads
	 */
	public Long getNumberOfDownloadsForCollection(InstitutionalCollection institutionalCollection);

	/**
	 * Get the number of file downloads for all collection 
	 * 
	 * @return Number of downloads
	 */
	public Long getNumberOfDownloadsForAllCollections();

	/**
	 * Get the number of file downloads in the item
	 * 
	 * @param item item to count for file downloads
	 * 
	 * @return Number of downloads
	 */
	public Long getNumberOfDownloadsForItem(GenericItem item) ;	
	
	/**
	 * Get the number of downloads for all institutional items in the repository.
	 * 
	 * @param sponsorId - id of the sponsor
	 * @return - total number of downloads for all institutional items in the repository.
	 */
	public Long getNumberOfDownloadsBySponsor(Long sponsorId);
	
	/**
	 * Gets a list of file info counts that should be ignored from the download info 
	 * 
	 * @param start - start position
	 * @param batchSize - batch size to process.
	 * @return list of records starting at start position and returning no more than batch size
	 */
	public List<FileDownloadInfo> getIgnoreCountsFromDownloadInfo(int start, int batchSize);
	
	/**
	 * Get ignored counts that are actually ok to place into the file download counts.
	 * 
	 * @param batchSize - number of records to process at once.
	 * @param start - start position in the list
	 * 
	 * @return list of records starting at start position and returning no more than batch size
	 */
	public List<IpIgnoreFileDownloadInfo> getIgnoreInfoNowAcceptable(int start, int batchSize);
	
	/**
	 * Get the file download info.
	 * 
	 * @param ipAddress ip address
	 * @param fileId the id of the file
	 * @param date - date for the downloads
	 * 
	 * @return the file download info or null if not found
	 */
	public FileDownloadInfo getFileDownloadInfo(String ipAddress, Long fileId, Date date);
	
	/**
	 * Get the ingnore file download info.
	 * 
	 * @param ipAddress - ip address
	 * @param fileId - the id of the file
	 * @param date - date of the download information
	 *  
	 * @return the ignore file download info.
	 */
	public IpIgnoreFileDownloadInfo getIpIgnoreFileDownloadInfo(String ipAddress, Long fileId, Date date);
	
	/**
	 * Get the list of ipaddress summed by download ordered by download count (asc/desc) 
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * 
	 * @return List of ip download counts for the specified information.
	 */
	public List<IpDownloadCount> getIpOrderByDownloadCount(int rowStart, 
    		int numberOfResultsToShow, OrderType sortType);
	
	/**
	 * Get the count of results for groping file download infos by ip address.
	 * 
	 * @return the number of results found
	 */
	public Long getGroupByIpAddressCount();
	
	/**
	 * Get the list of ignored ipaddress summed by download ordered by download count (asc/desc) 
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * 
	 * @return List of ignored ip download counts for the specified information.
	 */
	public List<IpDownloadCount> getIgnoreIpOrderByDownloadCount(int rowStart, 
    		int numberOfResultsToShow, OrderType sortType);
	
	/**
	 * Get the count of results for groping file ignored download infos by ip address.
	 * 
	 * @return the number of results found
	 */
	public Long getGroupByIgnoreIpAddressCount();
	
	/**
	 * Delete the counts that should not be stored because they should be ignored 
	 * and not stored from the file download info table.
	 * 
	 * @return number of records deleted
	 */
	public Long deleteNoStoreFileDownloadInfoCounts();
	
	/**
	 * Delete the counts that should not be stored because they should be ignored 
	 * and not stored from the ignore table.
	 * 
	 * @return number of records deleted
	 */
	public Long deleteNoStoreIgnoreDownloadInfoCounts();
}
