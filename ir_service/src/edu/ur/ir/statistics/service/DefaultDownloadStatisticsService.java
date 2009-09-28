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


package edu.ur.ir.statistics.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItemDownloadCount;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.statistics.FileDownloadInfo;
import edu.ur.ir.statistics.FileDownloadInfoDAO;
import edu.ur.ir.statistics.FileDownloadRollUp;
import edu.ur.ir.statistics.FileDownloadRollUpDAO;
import edu.ur.ir.statistics.FileDownloadRollUpProcessingRecord;
import edu.ur.ir.statistics.FileDownloadRollUpProcessingRecordDAO;
import edu.ur.ir.statistics.IgnoreIpAddressDAO;
import edu.ur.ir.statistics.IpIgnoreFileDownloadInfo;
import edu.ur.ir.statistics.IpIgnoreFileDownloadInfoDAO;

/**
 * Implementation of the download statistics service.
 * 
 * @author Nathan Sarr
 * @author Sharmila Ranganathan
 */
public class DefaultDownloadStatisticsService implements DownloadStatisticsService { 
	
	/** Data access for file download info */
	private FileDownloadInfoDAO fileDownloadInfoDAO;
	
	/** Roll up Data access for file download info */
	private FileDownloadRollUpDAO fileDownloadRollUpDAO;
	
	/** data access for ignoring ip address information  */
	private IgnoreIpAddressDAO ignoreIpAddressDAO;
	
	/** download information for an ip address that is ignored  */
	private IpIgnoreFileDownloadInfoDAO ipIgnoreFileDownloadInfoDAO;
	
	/** roll up processing record data access object */
	private FileDownloadRollUpProcessingRecordDAO fileDownloadRollUpProcessingRecordDAO;
	
	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(DefaultDownloadStatisticsService.class);

	/**
	 * Save file download info 
	 * 
	 * @param fileDownloadInfo
	 */
	public void saveFileDownloadInfo(FileDownloadInfo fileDownloadInfo) {
		fileDownloadInfoDAO.makePersistent(fileDownloadInfo);
	}
	
	/**
	 * Save file download info 
	 * 
	 * @param fileDownloadInfo
	 */
	public void saveIpIgnoreFileDownloadInfo(IpIgnoreFileDownloadInfo ipIgnroeFileDownloadInfo) {
		ipIgnoreFileDownloadInfoDAO.makePersistent(ipIgnroeFileDownloadInfo);
	}
	
	/**
	 * Adds the file download info
	 * 
	 * @param ipAddress IP address downloading a file
	 * @param fileId File being downloaded
	 */
	public FileDownloadInfo addFileDownloadInfo(String ipAddress, IrFile irFile) {
		FileDownloadInfo fileDownloadInfo = null;
		log.debug("Add file download Info for Ipaddress: " + ipAddress );

		// get todays date
		Date d = new Date();
		
		// chop off time information
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String dateStr = simpleDateFormat.format(d);
		
		Date noTimeDate = null;
		try {
			noTimeDate = simpleDateFormat.parse(dateStr);
			fileDownloadInfo = fileDownloadInfoDAO.getFileDownloadInfo(ipAddress, irFile.getId(), noTimeDate );
			if (fileDownloadInfo == null) {
				fileDownloadInfo = new FileDownloadInfo(ipAddress, irFile.getId(), noTimeDate);
			} else {
				fileDownloadInfo.setDownloadCount(fileDownloadInfo.getDownloadCount() + 1);
			}
			saveFileDownloadInfo(fileDownloadInfo);
		} catch (ParseException e) {
			log.error("A parse problem should never occur ", e);
		}
		
		return fileDownloadInfo;
		 
	}
	
	
	/**
	 * Add an ignore ip address file download.
	 * 
	 * @param ipAddress - ip address to add to the ignore set
	 * @param irFile - file that was downloaded
	 * 
	 * @return the created or updated record
	 */
	public IpIgnoreFileDownloadInfo addIgnoreFileDownloadInfo(String ipAddress, IrFile irFile) 
	{
		IpIgnoreFileDownloadInfo ipIgnoreFileDownloadInfo = null;
		log.debug("Add file download Info for Ipaddress: " + ipAddress );

		// get todays date
		Date d = new Date();
		
		// chop off time information
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String dateStr = simpleDateFormat.format(d);
		
		Date noTimeDate = null;
		try {
			noTimeDate = simpleDateFormat.parse(dateStr);
			ipIgnoreFileDownloadInfo = ipIgnoreFileDownloadInfoDAO.getIpIgnoreFileDownloadInfo(ipAddress, irFile.getId(), noTimeDate );
			if (ipIgnoreFileDownloadInfo == null) {
				ipIgnoreFileDownloadInfo = new IpIgnoreFileDownloadInfo(ipAddress, irFile.getId(), noTimeDate);
			} else {
				ipIgnoreFileDownloadInfo.setDownloadCount(ipIgnoreFileDownloadInfo.getDownloadCount() + 1);
			}
			saveIpIgnoreFileDownloadInfo(ipIgnoreFileDownloadInfo);
		} catch (ParseException e) {
			log.error("A parse problem should never occur ", e);
		}
		
		return ipIgnoreFileDownloadInfo;
	}
	
	/**
	 * This process a file download.  This determines where the download
	 * should be attributed to.
	 * 
	 * @param ipAddress - ip address to process
	 * @param irFile - file to update the count for.
	 */
	public void processFileDownload(String ipAddress, IrFile irFile)
	{
		long count = ignoreIpAddressDAO.getIgnoreCountForIp(ipAddress);		
		boolean isIgnoreAddress = (count > 0);
		
		if( isIgnoreAddress )
		{
			addIgnoreFileDownloadInfo(ipAddress, irFile);
		}
		else
		{
			addFileDownloadInfo(ipAddress, irFile);
			updateRollUpCount(irFile.getId());
		}
	}

	
	/**
	 * Get the number of downloads for the specified file Id
	 * 
	 * @param fileId Id of file to count the number of downloads
	 * 
	 * @return Number of times the file is downloaded
	 */
	@SuppressWarnings("unchecked")
	public Long getNumberOfDownloadsForFile(IrFile irFile) {
		
		log.debug("No. of downloads for file Ids: " + irFile.getId() );
		
		List fileIds = new LinkedList<Long>();
		fileIds.add(irFile.getId());
		
		return fileDownloadRollUpDAO.getNumberOfFileDownloadsForIrFile(irFile);
		
	}
	
	/**
	 * Get the number of file downloads fin the items of the specified collection
	 * 
	 * @param institutionalCollection collection to count for file downloads
	 * 
	 * @return Number of downloads
	 */
	public Long getNumberOfDownloadsForCollection(InstitutionalCollection institutionalCollection) {
		
		log.debug("No. of downloads for insittuional colleciton: " + institutionalCollection );
		return fileDownloadRollUpDAO.getNumberOfFileDownloadsForCollection(institutionalCollection);
	}

	/**
	 * Get the number of file downloads in the item
	 * 
	 * @param item item to count for file downloads
	 * 
	 * @return Number of downloads
	 */
	public Long getNumberOfDownloadsForItem(GenericItem item) {
		
		log.debug("No. of downloads for item: " + item);
		return fileDownloadRollUpDAO.getNumberOfFileDownloadsForItem(item);
	}

	
	/**
	 * Get the number of file downloads in the items of the specified collection and its children
	 * 
	 * @param institutionalCollection collection to count for file downloads
	 * 
	 * @return Number of downloads
	 */
	public Long getNumberOfDownloadsForCollectionAndItsChildren(InstitutionalCollection institutionalCollection) {
		
		log.debug("No. of downloads for insitutional collection Id: " + institutionalCollection );
		return fileDownloadRollUpDAO.getNumberOfFileDownloadsForCollectionIncludingChildren(institutionalCollection);
		
	}

	/**
	 * Get the number of file downloads for all collection 
	 * 
	 * @return Number of downloads
	 */
	public Long getNumberOfDownloadsForAllCollections() {
		return fileDownloadRollUpDAO.getNumberOfFileDownloadsForRepository();
	
	}
	
	/**
	 * Update all repository counts for ir files in the system.
	 * 
	 * @return the count of records to be updated
	 */
	public Long updateAllRepositoryFileRollUpCounts()
	{
		return fileDownloadRollUpProcessingRecordDAO.updateAllRepositoryDownloadCounts();
	}
	
	/**
	 * Delete file Download Info
	 * 
	 * @param fileDownloadInfo
	 */
	public void delete(FileDownloadInfo fileDownloadInfo) {
		fileDownloadInfoDAO.makeTransient(fileDownloadInfo);
	}	
	
	/**
	 * Set file download data access
	 * 
	 * @param fileDownloadInfoDAO
	 */
	public void setFileDownloadInfoDAO(FileDownloadInfoDAO fileDownloadInfoDAO) {
		this.fileDownloadInfoDAO = fileDownloadInfoDAO;
	}



	public FileDownloadInfo getFileDownloadInfo(Long id, boolean lock) {
		return fileDownloadInfoDAO.getById(id, lock);
	}

	
	/**
	 * Get most downloaded institutional item info by person name ids
	 * 
	 * @param personNameIds Id of person name
	 * 
	 * @return most downloaded  institutional item
	 */
	public InstitutionalItemDownloadCount getInstitutionalItemDownloadCountByPersonName(Set<PersonName> personNames) {
		
		List<Long> ids = new ArrayList<Long>();
		for (PersonName p: personNames) {
			ids.add(p.getId());
		}

		return fileDownloadRollUpDAO.getInstitutionalItemDownloadCountByPersonName(ids);
	}

	public FileDownloadRollUpDAO getFileDownloadRollUpDAO() {
		return fileDownloadRollUpDAO;
	}

	public void setFileDownloadRollUpDAO(FileDownloadRollUpDAO fileDownloadRollUpDAO) {
		this.fileDownloadRollUpDAO = fileDownloadRollUpDAO;
	}

	
	/**
	 * Update the count for the specified ir file id.
	 * 
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#updateRollUpCount(java.lang.Long)
	 */
	public FileDownloadRollUp updateRollUpCount(Long irFileId) {		
		Long count = fileDownloadInfoDAO.getNumberOfFileDownloadsForIrFile(irFileId);
		FileDownloadRollUp rollUp = fileDownloadRollUpDAO.getByIrFileId(irFileId);
		
		if( rollUp == null )
		{
			rollUp = new FileDownloadRollUp(irFileId, count);
		}
		else
		{
			rollUp.setDownloadCount(count);
		}
		
		fileDownloadRollUpDAO.makePersistent(rollUp);
		return rollUp;
	}

	public FileDownloadRollUpProcessingRecordDAO getFileDownloadRollUpProcessingRecordDAO() {
		return fileDownloadRollUpProcessingRecordDAO;
	}

	public void setFileDownloadRollUpProcessingRecordDAO(
			FileDownloadRollUpProcessingRecordDAO fileDownloadRollUpProcessingRecordDAO) {
		this.fileDownloadRollUpProcessingRecordDAO = fileDownloadRollUpProcessingRecordDAO;
	}

	
	/**
	 * Delete the file download roll up processing record.
	 * 
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#delete(edu.ur.ir.statistics.FileDownloadRollUpProcessingRecord)
	 */
	public void delete(FileDownloadRollUpProcessingRecord processingRecord) {
		fileDownloadRollUpProcessingRecordDAO.makeTransient(processingRecord);
	}

	
	/**
	 * Get all download roll up processing records.
	 * 
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#getAllRollUpProcessingRecords()
	 */
	@SuppressWarnings("unchecked")
	public List<FileDownloadRollUpProcessingRecord> getAllDownloadRollUpProcessingRecords() {
		return fileDownloadRollUpProcessingRecordDAO.getAll();
	}
	
	/**
	 * Add a processing record for the specified file id.  If a processing record for the 
	 * specified ir file exits, that record is returned otherwise a new processing record is
	 * created and returned.
	 * 
	 * @param irFileId
	 */
	public FileDownloadRollUpProcessingRecord addDownloadRollUpProcessingRecord(Long irFileId)
	{
		FileDownloadRollUpProcessingRecord	processingRecord = fileDownloadRollUpProcessingRecordDAO.getByIrFileId(irFileId);
	    if( processingRecord == null )
	    {
	    	processingRecord = new FileDownloadRollUpProcessingRecord(irFileId);
	    	fileDownloadRollUpProcessingRecordDAO.makePersistent(processingRecord);
	    }
	    
	    return processingRecord;
	}
	
	/**
	 * Moves counts that should be ignored from the download info counts
	 * to the ignore download info counts.
	 * 
	 * @param batchSize - batch size to process.
	 */
	public void removeIgnoreCountsFromDownloadInfo(int batchSize)
	{
		int count = fileDownloadInfoDAO.getDownloadInfoIgnoredCount().intValue();
		if( count > 0l )
		{
			int numProcessed = 1;
			while( numProcessed < count)
			{
				if(log.isDebugEnabled())
				{
					log.debug(" num processed = " + numProcessed + " batch size = " + batchSize + " num processed + batch size = " 
							+ (numProcessed + batchSize));
				}
			    List<FileDownloadInfo> infos = fileDownloadInfoDAO.getDownloadInfoIgnored(numProcessed, numProcessed + batchSize);
			    for( FileDownloadInfo info : infos)
			    {
			    	IpIgnoreFileDownloadInfo ignoreRecord = ipIgnoreFileDownloadInfoDAO.getIpIgnoreFileDownloadInfo(info.getIpAddress(), info.getIrFileId(), info.getDownloadDate());
			    	if( ignoreRecord == null )
			    	{
			    		ignoreRecord = new IpIgnoreFileDownloadInfo(info.getIpAddress(), info.getIrFileId(), info.getDownloadDate());
			    		ignoreRecord.setDownloadCount(info.getDownloadCount());
			    	}
			    	else
			    	{
			    		ignoreRecord.setDownloadCount(ignoreRecord.getDownloadCount() + info.getDownloadCount());
			    	}
			    	ipIgnoreFileDownloadInfoDAO.makePersistent(ignoreRecord);
			    	fileDownloadInfoDAO.makeTransient(info);
			    }
			    numProcessed = numProcessed + batchSize;
			}
		}
	}
	
	/**
	 * Move counts that are ignored that are actually ok into the file download counts.
	 * 
	 * @param batchSize - number of records to process at once.
	 */
	public void removeOkCountsFromIgnoreDownloadInfo(int batchSize)
	{
		int count = ipIgnoreFileDownloadInfoDAO.getIgnoreInfoNowAcceptableCount().intValue(); 
		if( count > 0l )
		{
			int numProcessed = 1;
			while( numProcessed < count)
			{
				if(log.isDebugEnabled())
				{
					log.debug(" num processed = " + numProcessed + " batch size = " + batchSize + " num processed + batch size = " 
							+ (numProcessed + batchSize));
				}
			    List<IpIgnoreFileDownloadInfo> okCounts = ipIgnoreFileDownloadInfoDAO.getIgnoreInfoNowAcceptable(numProcessed, numProcessed + batchSize);
			    for( IpIgnoreFileDownloadInfo okInfo : okCounts)
			    {
			    	FileDownloadInfo downloadRecord = fileDownloadInfoDAO.getFileDownloadInfo(okInfo.getIpAddress(), okInfo.getIrFileId(), okInfo.getDownloadDate());
			    	if( downloadRecord == null )
			    	{
			    		downloadRecord = new FileDownloadInfo(okInfo.getIpAddress(), okInfo.getIrFileId(), okInfo.getDownloadDate());
			    		downloadRecord.setDownloadCount(okInfo.getDownloadCount());
			    	}
			    	else
			    	{
			    		downloadRecord.setDownloadCount(okInfo.getDownloadCount() + downloadRecord.getDownloadCount());
			    	}
			    	fileDownloadInfoDAO.makePersistent(downloadRecord);
			    	ipIgnoreFileDownloadInfoDAO.makeTransient(okInfo);
			    }
			    numProcessed = numProcessed + batchSize;
			}
		}
	}

	
	/**
	 * Get the file download roll up by ir file id.
	 * 
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#getFileDownloadRollUpByIrFileId(java.lang.Long)
	 */
	public FileDownloadRollUp getFileDownloadRollUpByIrFileId(Long irFileId)
	{
		return fileDownloadRollUpDAO.getByIrFileId(irFileId);
	}

	/**
	 * Get the file download roll up 
	 * 
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#getFileDownloadRollUp(java.lang.Long, boolean)
	 */
	public FileDownloadRollUp getFileDownloadRollUp(Long id, boolean lock)
	{
		return fileDownloadRollUpDAO.getById(id, lock);
	}
	
	/**
	 * Delete the roll up record.
	 * 
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#delete(edu.ur.ir.statistics.FileDownloadRollUp)
	 */
	public void delete(FileDownloadRollUp fileDownloadRollUp) {
		fileDownloadRollUpDAO.makeTransient(fileDownloadRollUp);
	}

	public IgnoreIpAddressDAO getIgnoreIpAddressDAO() {
		return ignoreIpAddressDAO;
	}

	public void setIgnoreIpAddressDAO(IgnoreIpAddressDAO ignoreIpAddressDAO) {
		this.ignoreIpAddressDAO = ignoreIpAddressDAO;
	}

	public IpIgnoreFileDownloadInfoDAO getIpIgnoreFileDownloadInfoDAO() {
		return ipIgnoreFileDownloadInfoDAO;
	}

	public void setIpIgnoreFileDownloadInfoDAO(
			IpIgnoreFileDownloadInfoDAO ipIgnoreFileDownloadInfoDAO) {
		this.ipIgnoreFileDownloadInfoDAO = ipIgnoreFileDownloadInfoDAO;
	}

	/**
	 * Get the file download info.
	 * 
	 * @param ipAddress ip address
	 * @param fileId the id of the file
	 * @param date - date for the downloads - should not contain time only the date
	 * 
	 * @return the file download info or null if not found
	 */
	public FileDownloadInfo getFileDownloadInfo(String ipAddress, Long fileId,
			Date date) {
		return fileDownloadInfoDAO.getFileDownloadInfo(ipAddress, fileId, date);
	}

	/**
	 * Get the ingnore file download info.
	 * 
	 * @param ipAddress - ip address
	 * @param fileId - the id of the file
	 * @param date - date of the download information - should only contain the date not time
	 *  
	 * @return the ignore file download info.
	 */
	public IpIgnoreFileDownloadInfo getIpIgnoreFileDownloadInfo(
			String ipAddress, Long fileId, Date date) {
		return ipIgnoreFileDownloadInfoDAO.getIpIgnoreFileDownloadInfo(ipAddress, fileId, date);
	}

	
	/**
	 * Delete the ip ignore file download info.
	 * 
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#delete(edu.ur.ir.statistics.IpIgnoreFileDownloadInfo)
	 */
	public void delete(IpIgnoreFileDownloadInfo ipIgnoreFileDownloadInfo) {
		ipIgnoreFileDownloadInfoDAO.makeTransient(ipIgnoreFileDownloadInfo);
	}

	/**
	 * Get the ignore file download info 
	 * 
	 * @param id - id of the file download information
	 * @param lock - upgrade the lock mode
	 * 
	 * @return - the found file download info or null if not found
	 */
	public IpIgnoreFileDownloadInfo getIpIgnoreFileDownloadInfo(Long id,
			boolean lock) {
		return ipIgnoreFileDownloadInfoDAO.getById(id, lock);
	}
	
	

}
