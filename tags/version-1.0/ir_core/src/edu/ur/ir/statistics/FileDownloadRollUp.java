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

import edu.ur.persistent.BasePersistent;

/**
 * This allows all the acceptable counts to be rolled up.
 * into a single value for an ir file.
 * 
 * @author Nathan Sarr
 *
 */
public class FileDownloadRollUp extends BasePersistent{

	/** elipse generated id */
	private static final long serialVersionUID = 4429561710566378894L;
	
	/** full count  of the downloads for an ir file*/
	private Long downloadCount;
	
	/** the ir file id*/
	private Long irFileId;
	
	/**
	 * Package protected constructor
	 */
	FileDownloadRollUp(){}
	
	/**
	 * Default constructor - construct a rollup count for the specified
	 * file id and count.
	 * 
	 * @param irFileId - ir file id
	 * @param count - rollup count of the downloads
	 */
	public FileDownloadRollUp(Long irFileId, Long count)
	{
		setIrFileId(irFileId);
		setDownloadCount(count);
	}
	
	
	public int hashCode()
	{
		int value = 0;
		value += irFileId == null ? 0 : irFileId.hashCode();
		return value;
	}
	
	public boolean equals(Object o)
	{
		final FileDownloadRollUp other = (FileDownloadRollUp) o;
		
		if( ( irFileId != null && !irFileId.equals(other.getIrFileId()) ) ||
			( irFileId == null && other.getIrFileId() != null ) ) return false;
		
		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" ir file id = ");
		sb.append(irFileId);
		sb.append(" count = ");
		sb.append(downloadCount);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Get the ir file id.
	 * 
	 * @return
	 */
	public Long getIrFileId() {
		return irFileId;
	}


	/**
	 * Set the ir file id.
	 * 
	 * @param irFileId
	 */
	void setIrFileId(Long irFileId) {
		this.irFileId = irFileId;
	}

	/**
	 * Total download count
	 * @return
	 */
	public Long getDownloadCount() {
		return downloadCount;
	}

	/**
	 * Set the download count.
	 * 
	 * @param downloadCount
	 */
	public void setDownloadCount(Long downloadCount) {
		this.downloadCount = downloadCount;
	}

}
