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

import edu.ur.dao.CrudDAO;

/**
 * Data access for a file download roll up processing record.
 * 
 * @author Nathan Sarr
 *
 */
public interface FileDownloadRollUpProcessingRecordDAO extends CrudDAO<FileDownloadRollUpProcessingRecord> 
{
	/**
	 * Insert all download counts for the repository
	 * 
	 * @param processingType - processing type.
	 * 
	 * @return - number of records created for processing
	 */
	public Long updateAllRepositoryDownloadCounts();
	
	/**
	 * Get a processing record by id.
	 * 
	 * @param irFileId - id of the ir file
	 * @return the found processing record or null if not found
	 */
	public FileDownloadRollUpProcessingRecord getByIrFileId(Long irFileId);
	   
}
