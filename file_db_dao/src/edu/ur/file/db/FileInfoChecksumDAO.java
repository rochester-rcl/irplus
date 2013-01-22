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

package edu.ur.file.db;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.file.db.FileInfoChecksum;
import edu.ur.order.OrderType;

import java.util.Date;
import java.util.List;

/**
 * @author Nathan Sarr
 *
 */
public interface FileInfoChecksumDAO extends CrudDAO<FileInfoChecksum>, CountableDAO {
	
	/**
	 * Get the oldest checksums that are before a given date that are set to true 
	 * for checking
	 * 
	 * @param start - start position
	 * @param maxResults - maximum number of results
	 * 
	 * @param beforeDate - date the check sums must be less than.
	 * 
	 * @return - list of checksums found
	 */
	public List<FileInfoChecksum> getOldestChecksumsForChecker(int start, int maxResults, Date beforeDate);
	
	/**
	 * Get the checksum file info's in date order 
	 * 
	 * @param start - start position.
	 * @param maxResults - maximum number of results to retrieve.
	 * @param onlyFails - get only the one's that have failed their checksum
	 * @param orderType - order date ascending or decending
	 * 
	 * @return - all checksum infos
	 */
	public List<FileInfoChecksum> getChecksumInfosDateOrder(int start, 
			int maxResults, 
			boolean onlyFails, 
			OrderType orderType);
	
	/**
	 * Get all file info checksums where it failed the check
	 * 
	 * @return - where it is currently failing the check
	 */
	public List<FileInfoChecksum> getAllFailingChecksums();
	
	/**
	 * Get a count of fails for checksum infos.
	 * 
	 * @return count of failed checksums.
	 */
	public Long getChecksumInfoFailsCount();
}
