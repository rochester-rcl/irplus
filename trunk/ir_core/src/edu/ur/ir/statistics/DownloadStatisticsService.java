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

import java.util.List;
import java.util.Set;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItemDownloadCount;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.person.PersonName;

/**
 * Interface to get file download statistics
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface DownloadStatisticsService {

	/**
	 * Save file download info 
	 * 
	 * @param fileDownloadInfo
	 */
	public void saveFileDownloadInfo(FileDownloadInfo fileDownloadInfo) ;
	
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
	 * Adds the file download info
	 * 
	 * @param ipAddress IP address downloading a file
	 * @param fileId IrFile being downloaded
	 * 
	 * @return the download info object
	 */
	public FileDownloadInfo addFileDownloadInfo(String ipAddress, IrFile irFile);
	
	/**
	 * Update the roll up count for the specified irFileId.
	 * 
	 * @param irFileId - id of the irFile to update the count for.
	 */
	public void updateRollUpCount(Long irFileId);
	
	/**
	 * Get all processing records.
	 * 
	 * @return all processing recod
	 */
	public List<FileDownloadRollUpProcessingRecord> getAllDownloadRollUpProcessingRecords();
	
	/**
	 * Delete the processing record.
	 * 
	 * @param processingRecord
	 */
	public void delete(FileDownloadRollUpProcessingRecord processingRecord);
	
	/**
	 * Get the number of downloads for the sepecified ir file Id
	 * 
	 * @param fileId Id of file to count the number of downloads
	 * 
	 * @return Number of times the file is downloaded
	 */
	public Long getNumberOfDownloadsForFile(IrFile irFile);
	
	/**
	 * Save ignore Ip Address
	 * 
	 * @param ignoreIpAddress
	 */
	public void saveIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress);
	
	/**
	 * Get the ignore ip address
	 * 
	 * @param id - id of the ingore ip address
	 * @param lock - upgrade the lock
	 * @return - the found ignore ip address or null if one is not found
	 */
	public IgnoreIpAddress getIgnoreIpAddress(Long id,  boolean lock) ;
	

	/**
	 * Delete file Download Info
	 * 
	 * @param fileDownloadInfo
	 */
	public void deleteFileDownloadInfo(FileDownloadInfo fileDownloadInfo);
	
	/**
	 * Delete ignore Ip Address
	 * 
	 * @param ignoreIpAddress
	 */
	public void deleteIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress);

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
	 * Get the most downloaded  institutional item info by person name ids
	 * 
	 * @param personNameIds Id of person name
	 * 
	 * @return most downloaded  institutional item
	 */
	public InstitutionalItemDownloadCount getInstitutionalItemDownloadCountByPersonName(Set<PersonName> personNames);
}
