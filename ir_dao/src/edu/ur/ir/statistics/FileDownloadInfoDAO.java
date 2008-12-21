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
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItemDownloadCount;
import edu.ur.ir.item.GenericItem;


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
	public Long getNumberOfFileDownloadsForRepository();
	
	/**
	 * Gets the count of downloads for all collections within the repository.
	 * 
	 * @return
	 */
	public Long getNumberOfFileDownloadsForIrFile(IrFile irFile);
	
	/**
	 * Get the number of file downloads for a specific collection.  This does not
	 * include counts for files within children of the collection.
	 * 
	 * @param collectionId - id of the collection to get the count for
	 * @return - a sum of all the counts for file downloads
	 */
	public Long getNumberOfFileDownloadsForCollection(InstitutionalCollection institutionalCollection);
	
	/**
	 * Get the number of file downloads for a specific collection.  This 
	 * include counts for files within children of the collection.
	 * 
	 * @param collectionId - id of the collection to get the count for
	 * @return - a sum of all the counts for file downloads
	 */
	public Long getNumberOfFileDownloadsForCollectionIncludingChildren(InstitutionalCollection institutionalCollection);

	/**
	 * Get number of times the files in the specified item is downloaded.
	 * 
	 * @param item Item to get download count
	 * @return Number of times downloaded
	 */
	public Long getNumberOfFileDownloadsForItem(GenericItem item);

	/**
	 * Get most  downloaded  institutitonal item for specified person name ids.
	 * 
	 * @param personNameIds Ids of person name
	 * @return most  downloaded  institutional Item
	 */	
	public InstitutionalItemDownloadCount getInstitutionalItemDownloadCountByPersonName(List<Long> personNameIds) ;
}
