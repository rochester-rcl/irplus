/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.institution;

/**
 * Represents an institutional item version with download counts.
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalItemVersionDownloadCount 
{
    /** download count for the insitutional item version */
    private Long downloadCount = new Long(0l);
    
	/** institutional item version  */
	private InstitutionalItemVersion institutionalItemVersion;
	

	/**
	 * Create a combination between an insitutional item version and the download count.
	 * 
	 * @param downloadCount - number of downloads for an institutional item version
	 * @param institutionalItemVersion - the institutional item with the downloads
	 */
	public InstitutionalItemVersionDownloadCount(InstitutionalItemVersion institutionalItemVersion, Long downloadCount)
	{
		if( downloadCount != null )
		{	
		    setDownloadCount(downloadCount);
		}
		else
		{
			setDownloadCount(new Long(0l));
		}
		setInstitutionalItemVersion(institutionalItemVersion);
	}
	
	/**
	 * Get the download count.
	 * 
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
		if( downloadCount != null )
		{
			this.downloadCount = new Long(downloadCount.longValue());	
		}
		else
		{
			this.downloadCount = new Long(0l);
		}
		
	}

	/**
	 * Get the institutional item version.
	 * 
	 * @return
	 */
	public InstitutionalItemVersion getInstitutionalItemVersion() {
		return institutionalItemVersion;
	}

	/**
	 * Set the insitutional item version.
	 * 
	 * @param institutionalItemVersion
	 */
	public void setInstitutionalItemVersion(
			InstitutionalItemVersion institutionalItemVersion) {
		this.institutionalItemVersion = institutionalItemVersion;
	}
	
	/**
	 * To String for outputting a institutional item version
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ institutional item version = ");
		sb.append(institutionalItemVersion);
		sb.append(" download count = ");
		sb.append(downloadCount);
		sb.append("]");
		return sb.toString();
		
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof InstitutionalItemVersionDownloadCount)) return false;

		final InstitutionalItemVersionDownloadCount other = (InstitutionalItemVersionDownloadCount) o;
		
		if( ( institutionalItemVersion != null && !institutionalItemVersion.equals(other.getInstitutionalItemVersion()) ) ||
		    ( institutionalItemVersion == null && other.getInstitutionalItemVersion() != null ) ) return false;
		
		if( ( downloadCount != null && !downloadCount.equals(other.getDownloadCount()) ) ||
			( downloadCount == null && other.getDownloadCount() != null ) ) return false;
		
		return true;
	}
	
	/**
	 * Hash code is based on download count and institutional item version
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += downloadCount > 0l? 0 : new Long(downloadCount).hashCode();
		value += institutionalItemVersion == null? 0 : institutionalItemVersion.hashCode();
		return value;
	}
	
}
