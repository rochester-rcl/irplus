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
import edu.ur.ir.statistics.IgnoreIpAddress;
import edu.ur.ir.statistics.IgnoreIpAddressDAO;

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
	
	/** Data access for ignore IP address */
	private IgnoreIpAddressDAO ignoreIpAddressDAO;
	
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
	 * Save ignore Ip Address
	 * 
	 * @param ignoreIpAddress
	 */
	public void saveIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress) {
		ignoreIpAddressDAO.makePersistent(ignoreIpAddress);
	}
	
	/**
	 * Delete ignore Ip Address
	 * 
	 * @param ignoreIpAddress
	 */
	public void deleteIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress) {
		ignoreIpAddressDAO.makeTransient(ignoreIpAddress);
	}	
	
	/**
	 * Delete file Download Info
	 * 
	 * @param fileDownloadInfo
	 */
	public void deleteFileDownloadInfo(FileDownloadInfo fileDownloadInfo) {
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

	/**
	 * Get ignore ip address dao
	 * 
	 * @return
	 */
	public IgnoreIpAddressDAO getIgnoreIpAddressDAO() {
		return ignoreIpAddressDAO;
	}

	/**
	 * Set ignore ip address dao
	 * 
	 * @param ignoreIpAddressDAO
	 */
	public void setIgnoreIpAddressDAO(IgnoreIpAddressDAO ignoreIpAddressDAO) {
		this.ignoreIpAddressDAO = ignoreIpAddressDAO;
	}

	public FileDownloadInfo getFileDownloadInfo(Long id, boolean lock) {
		return fileDownloadInfoDAO.getById(id, lock);
	}

	
	public IgnoreIpAddress getIgnoreIpAddress(Long id, boolean lock) {
		return ignoreIpAddressDAO.getById(id, lock);
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
	public void updateRollUpCount(Long irFileId) {		
		Long count = fileDownloadInfoDAO.getNumberOfFileDownloadsForIrFile(irFileId);
		
		FileDownloadRollUp rollUp = fileDownloadRollUpDAO.getByIrFileId(irFileId);
		
		if( rollUp == null )
		{
			rollUp = new FileDownloadRollUp(irFileId, count.intValue());
		}
		else
		{
			rollUp.setDownloadCount(count.intValue());
		}
		
		fileDownloadRollUpDAO.makePersistent(rollUp);
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

}
