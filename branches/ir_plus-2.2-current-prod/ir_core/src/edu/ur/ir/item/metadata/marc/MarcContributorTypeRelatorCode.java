/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.item.metadata.marc;

import edu.ur.ir.person.ContributorType;
import edu.ur.metadata.marc.MarcRelatorCode;
import edu.ur.persistent.BasePersistent;

/**
 * Relates a contributor type to a relator code.
 * 
 * @author Nathan Sarr
 *
 */
public class MarcContributorTypeRelatorCode extends BasePersistent{
	
	//eclipse generated id.
	private static final long serialVersionUID = 3752607504886002733L;

	// marc relator code
	private MarcRelatorCode marcRelatorCode;
	
	// content type.
	private ContributorType contributorType;
	
	/**
	 * Package protected constructor
	 */
	MarcContributorTypeRelatorCode(){}
	
	/**
	 * Default constructor.
	 * 
	 * @param marcRelatorCode
	 * @param contentType
	 */
	public MarcContributorTypeRelatorCode(MarcRelatorCode marcRelatorCode, ContributorType contributorType)
	{
		setMarcRelatorCode(marcRelatorCode);
		setContributorType(contributorType);
	}
	
	
	/**
	 * Get the marc relator code for this contributor type.
	 * 
	 * @return - relator code for the contributor type
	 */
	public MarcRelatorCode getMarcRelatorCode() {
		return marcRelatorCode;
	}

	/**
	 * Set the relator code for the contributor type.
	 * 
	 * @param marcRelatorCode
	 */
	public void setMarcRelatorCode(MarcRelatorCode marcRelatorCode) {
		this.marcRelatorCode = marcRelatorCode;
	}

	/**
	 * Get the contributor type for the mapping.
	 * 
	 * @return - contributor type
	 */
	public ContributorType getContributorType() {
		return contributorType;
	}

	/**
	 * Set the contributor type.
	 * 
	 * @param contributorType
	 */
	public void setContributorType(ContributorType contributorType) {
		this.contributorType = contributorType;
	}
	
	/**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += contributorType == null ? 0 : contributorType.hashCode();
    	return hash;
    }
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer("[Marc contributor type relator code id = ");
		sb.append(id);
		sb.append("]");
		return sb.toString();
    }
    
    /**
     * Person Name Equals.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
    	if (this == o) return true;
		if (!(o instanceof MarcContributorTypeRelatorCode)) return false;

		final MarcContributorTypeRelatorCode other = (MarcContributorTypeRelatorCode) o;

		if( ( contributorType != null && !contributorType.equals(other.getContributorType()) ) ||
			( contributorType == null && other.getContributorType() != null ) ) return false;
		

		return true;
    }
	
}
