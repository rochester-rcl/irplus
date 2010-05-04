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
package edu.ur.ir.item.metadata.dc;

import edu.ur.ir.person.ContributorType;
import edu.ur.metadata.dc.DublinCoreTerm;
import edu.ur.persistent.BasePersistent;

/**
 * This maps an IR+ contributor type to a dublin core element
 * 
 * @author Nathan Sarr
 *
 */
public class ContributorTypeDublinCoreMapping extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -400284712885802621L;

	/** IR+ contributor type */
	private ContributorType contributorType;
	
	/** represents a dublin core term */
	private DublinCoreTerm dublinCoreTerm;

	
	/**
	 * Default constructor
	 */
	ContributorTypeDublinCoreMapping(){}
	
	
	/**
	 * Default Constructor
	 * 
	 * @param contributorType
	 * @param dublinCoreElement
	 */
	public ContributorTypeDublinCoreMapping(ContributorType contributorType, DublinCoreTerm dublinCoreTerm)
	{
		setContributorType(contributorType);
		setDublinCoreTerm(dublinCoreTerm);
	}
	
	public ContributorType getContributorType() {
		return contributorType;
	}

	public void setContributorType(ContributorType contributorType) {
		this.contributorType = contributorType;
	}

	public DublinCoreTerm getDublinCoreTerm() {
		return dublinCoreTerm;
	}

	public void setDublinCoreTerm(DublinCoreTerm dublinCoreTerm) {
		this.dublinCoreTerm = dublinCoreTerm;
	}
	
	/**
     * Override Equals.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
    	if (this == o) return true;
		if (!(o instanceof ContributorTypeDublinCoreMapping)) return false;

		final ContributorTypeDublinCoreMapping other = (ContributorTypeDublinCoreMapping) o;

		if( ( dublinCoreTerm != null && !dublinCoreTerm.equals(other.getDublinCoreTerm()) ) ||
			( dublinCoreTerm == null && other.getDublinCoreTerm() != null ) ) return false;
		
		if( ( contributorType != null && !contributorType.equals(other.getContributorType()) ) ||
			( contributorType == null && other.getContributorType() != null ) ) return false;
		return true;
    }
    
    /**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += dublinCoreTerm == null ? 0 : dublinCoreTerm.hashCode();
    	hash += contributorType == null ? 0 : contributorType.hashCode();
    	return hash;
    }
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append( "dublin core term = ");
		sb.append(dublinCoreTerm);
		sb.append("contributorType = ");
		sb.append(contributorType);
		sb.append("]");
		return sb.toString();
	}

}
