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
	 * Update the roll up count for the specified irFileId.
	 * 
	 * @param irFileId - id of the irFile to update the count for.
	 */
	public FileDownloadRollUp updateRollUpCount(Long irFileId);
	
	/**
	 * Get all processing records.
	 * 
	 * @return all processing recod
	 */
	public List<FileDownloadRollUpProcessingRecord> getAllDownloadRollUpProcessingRecords();
	
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
	 * Get a file download roll up record by ir file id.
	 * 
	 * @param irFileId - ir file id the rollup record represents a roll up count for
	 * @return the file download roll up or null if not found.
	 */
	public FileDownloadRollUp getFileDownloadRollUpByIrFileId(Long irFileId);
	
	/**
	 * Get the roll up record by id.
	 * 
	 * @param id - id of the roll up record
	 * @param lock - upgrade the lock mode.
	 * 
	 * @return the file download roll up record if found otherwise null.
	 */
	public FileDownloadRollUp getFileDownloadRollUp(Long id, boolean lock);
	
	/**
	 * Delete the roll up record.
	 * 
	 * @param fileDownloadRollUp
	 */
	public void delete(FileDownloadRollUp fileDownloadRollUp);
	
	/**
	 * Get the number of downloads for the sepecified ir file Id
	 * 
	 * @param fileId Id of file to count the number of downloads
	 * 
	 * @return Number of times the file is downloaded
	 */
	public Long getNumberOfDownloadsForFile(IrFile irFile);
	
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
	 * Get the most downloaded  institutional item info by person name ids
	 * 
	 * @param personNameIds Id of person name
	 * 
	 * @return most downloaded  institutional item
	 */
	public InstitutionalItemDownloadCount getInstitutionalItemDownloadCountByPersonName(Set<PersonName> personNames);
	
	/**
	 * Moves counts that should be ignored from the download info counts
	 * to the ignore download info counts.
	 * 
	 * @param batchSize - batch size to process.
	 */
	public void removeIgnoreCountsFromDownloadInfo(int batchSize);
	
	/**
	 * Move counts that are ignored that are actually ok into the file download counts.
	 * 
	 * @param batchSize - number of records to process at once.
	 */
	public void removeOkCountsFromIgnoreDownloadInfo(int batchSize);
	
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
}
