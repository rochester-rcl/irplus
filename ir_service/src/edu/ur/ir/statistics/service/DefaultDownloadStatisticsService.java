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
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalItemVersionDAO;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.repository.RepositoryDAO;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.statistics.FileDownloadInfo;
import edu.ur.ir.statistics.FileDownloadInfoDAO;
import edu.ur.ir.statistics.FileDownloadRollUpProcessingRecord;
import edu.ur.ir.statistics.FileDownloadRollUpProcessingRecordDAO;
import edu.ur.ir.statistics.IgnoreIpAddressDAO;
import edu.ur.ir.statistics.IpDownloadCount;
import edu.ur.ir.statistics.IpIgnoreFileDownloadInfo;
import edu.ur.ir.statistics.IpIgnoreFileDownloadInfoDAO;
import edu.ur.order.OrderType;

/**
 * Implementation of the download statistics service.
 * 
 * @author Nathan Sarr
 * @author Sharmila Ranganathan
 */
public class DefaultDownloadStatisticsService implements DownloadStatisticsService { 
	
	/** eclipse generated id */
	private static final long serialVersionUID = -6213945410469379189L;

	/** Data access for file download info */
	private FileDownloadInfoDAO fileDownloadInfoDAO;
	
    /** ir File data access */
    private IrFileDAO irFileDAO;
    
    /** institutional collection data access */
    private InstitutionalCollectionDAO institutionalCollectionDAO;
    
    /** generic item data access */
    private GenericItemDAO genericItemDAO;
    
    /** repository data access */
    private RepositoryDAO repositoryDAO;
    
    /** institutional item version data access */
    private InstitutionalItemVersionDAO institutionalItemVersionDAO;


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
	public void save(FileDownloadInfo fileDownloadInfo) {
		fileDownloadInfoDAO.makePersistent(fileDownloadInfo);
	}
	
	/**
	 * Save ignore ip file dlownload info 
	 * 
	 * @param fileDownloadInfo
	 */
	public void save(IpIgnoreFileDownloadInfo ipIgnoreFileDownloadInfo) {
		ipIgnoreFileDownloadInfoDAO.makePersistent(ipIgnoreFileDownloadInfo);
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
			save(fileDownloadInfo);
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
			save(ipIgnoreFileDownloadInfo);
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
		long ignoreKeepCount = ignoreIpAddressDAO.getIgnoreCountForIp(ipAddress, true);		
		long ignoreDoNotKeepCount = ignoreIpAddressDAO.getIgnoreCountForIp(ipAddress, false);	
		
		boolean isIgnoreAddress = (ignoreKeepCount > 0) || (ignoreDoNotKeepCount > 0) ;
		
		if( isIgnoreAddress )
		{
			if( ignoreDoNotKeepCount > 0 )
			{
			    // always check ignore and do not store first
			}
			else
			{
				addIgnoreFileDownloadInfo(ipAddress, irFile);
			}
		}
		else
		{
			addFileDownloadInfo(ipAddress, irFile);
			long downloadCount = irFile.getDownloadCount();
			downloadCount = downloadCount + 1l;
			irFile.setDownloadCount(downloadCount);
			irFileDAO.makePersistent(irFile);
		}
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
		return institutionalCollectionDAO.getFileDownloads(institutionalCollection);
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
		return genericItemDAO.getDownloadCount(item.getId());
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
		return institutionalCollectionDAO.getFileDownloadsWithChildren(institutionalCollection); 
	}

	/**
	 * Get the number of file downloads for all collection 
	 * 
	 * @return Number of downloads
	 */
	public Long getNumberOfDownloadsForAllCollections() {
		return repositoryDAO.getDownloadCount();
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
	public List<FileDownloadRollUpProcessingRecord> getDownloadRollUpProcessingRecords(int start, int maxResults) {
		return fileDownloadRollUpProcessingRecordDAO.getProcessingRecords(start, maxResults);
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
	 * Gets a list of file info downloads should be ignored from the download info 
	 * 
	 * @param batchSize - batch size to process.
	 * @return total number of records processed
	 */
	public List<FileDownloadInfo> getIgnoreCountsFromDownloadInfo(int start, int batchSize)
	{
		return fileDownloadInfoDAO.getDownloadInfoIgnored(start, batchSize);
	}
	

	/**
	 * Move counts that are ignored that are actually ok into the file download counts.
	 * 
	 * @param batchSize - number of records to process at once.
	 * @return total number of records processed
	 */
	public List<IpIgnoreFileDownloadInfo> getIgnoreInfoNowAcceptable(int start, int batchSize)
	{
		return ipIgnoreFileDownloadInfoDAO.getIgnoreInfoNowAcceptable(start, batchSize );
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

	/**
	 * Get the number of file downloads for an ir file.  This does not include
	 * any downloads which refer to igored counts.
	 * 
	 * @param irFileId
	 * @return count of acceptable downloads for an ir file
	 */
	public Long getNumberOfFileDownloadsForIrFile(Long irFileId) {
		return fileDownloadInfoDAO.getNumberOfFileDownloadsForIrFile(irFileId);
	}
	
	/**
	 * Get the total number of downloads by sponsor.
	 * 
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#getNumberOfDownloadsBySponsor(java.lang.Long)
	 */
	public Long getNumberOfDownloadsBySponsor(Long sponsorId) {
		return institutionalItemVersionDAO.getDownloadCountForSponsor(sponsorId);
	}
	
	public IrFileDAO getIrFileDAO() {
		return irFileDAO;
	}

	public void setIrFileDAO(IrFileDAO irFileDAO) {
		this.irFileDAO = irFileDAO;
	}

	public InstitutionalCollectionDAO getInstitutionalCollectionDAO() {
		return institutionalCollectionDAO;
	}

	public void setInstitutionalCollectionDAO(
			InstitutionalCollectionDAO institutionalCollectionDAO) {
		this.institutionalCollectionDAO = institutionalCollectionDAO;
	}

	public GenericItemDAO getGenericItemDAO() {
		return genericItemDAO;
	}

	public void setGenericItemDAO(GenericItemDAO genericItemDAO) {
		this.genericItemDAO = genericItemDAO;
	}
	
	public RepositoryDAO getRepositoryDAO() {
		return repositoryDAO;
	}

	public void setRepositoryDAO(RepositoryDAO repositoryDAO) {
		this.repositoryDAO = repositoryDAO;
	}

	public InstitutionalItemVersionDAO getInstitutionalItemVersionDAO() {
		return institutionalItemVersionDAO;
	}

	public void setInstitutionalItemVersionDAO(
			InstitutionalItemVersionDAO institutionalItemVersionDAO) {
		this.institutionalItemVersionDAO = institutionalItemVersionDAO;
	}

	public List<IpDownloadCount> getIpOrderByDownloadCount(int rowStart,
			int numberOfResultsToShow, OrderType sortType) {
		return fileDownloadInfoDAO.getIpOrderByDownloadCount(rowStart, numberOfResultsToShow, sortType);
	}


	public List<IpDownloadCount> getIpIgnoreOrderByDownloadCounts(int start,
			int maxResults, OrderType orderType) {
		return ipIgnoreFileDownloadInfoDAO.getIpIgnoreOrderByDownloadCounts(start, maxResults, orderType);
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#deleteNoStoreFileDownloadInfoCounts()
	 */
	public Long deleteNoStoreFileDownloadInfoCounts() {
		return fileDownloadInfoDAO.deleteIgnoreCounts();
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#deleteNoStoreIgnoreDownloadInfoCounts()
	 */
	public Long deleteNoStoreIgnoreDownloadInfoCounts() {
		return ipIgnoreFileDownloadInfoDAO.deleteIgnoreCounts();
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#getGroupByIpAddressCount()
	 */
	public Long getGroupByIpAddressCount() {
		return fileDownloadInfoDAO.getGroupByIpAddressCount();
	}

}
