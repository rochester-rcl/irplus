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

package edu.ur.ir.marc;

import edu.ur.ir.item.ExtentType;
import edu.ur.metadata.marc.MarcSubField;

/**
 * Maps an extent type to a sub field.
 * 
 * @author Nathan Sarr
 *
 */
public class ExtentTypeSubFieldMapper extends MarcSubFieldMapper{
	
	//eclipse generated id.
	private static final long serialVersionUID = -7715705997475412147L;
	
	// extent type to map this to
	private ExtentType extentType;

	/**
	 * Package protected constructor
	 */
	ExtentTypeSubFieldMapper(){}
	
	/**
	 * Create an extent type sub field mapper.
	 * 
	 * @param extentType - extent to map
	 * @param marcDataField - parent marc data field
	 * @param marcSubField - marc sub field to map the extent type to
	 */
	public ExtentTypeSubFieldMapper(ExtentType extentType, 
			MarcDataFieldMapper marcDataFieldMapper, MarcSubField marcSubField)
	{
		setExtentType(extentType);
		setMarcDataFieldMapper(marcDataFieldMapper);
		setMarcSubField(marcSubField);
	}
	
	/**
	 * Get the extent type.
	 * 
	 * @return
	 */
	public ExtentType getExtentType() {
		return extentType;
	}

	/**
	 * Set the extent type.
	 * 
	 * @param extentType
	 */
	public void setExtentType(ExtentType extentType) {
		this.extentType = extentType;
	}
	
	/**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += extentType == null ? 0 : extentType.hashCode();
    	hash += marcDataFieldMapper == null ? 0 : marcDataFieldMapper.hashCode();
    	hash += marcSubField == null ? 0 : marcSubField.hashCode();
    	return hash;
    }
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer("[Marc extent type sub field mapper id = ");
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
		if (!(o instanceof ExtentTypeSubFieldMapper)) return false;

		final ExtentTypeSubFieldMapper other = (ExtentTypeSubFieldMapper) o;

		if( ( extentType != null && !extentType.equals(other.getExtentType()) ) ||
			( extentType == null && other.getExtentType() != null ) ) return false;
		
		if( ( marcDataFieldMapper != null && !marcDataFieldMapper.equals(other.getMarcDataFieldMapper()) ) ||
			( marcDataFieldMapper == null && other.getMarcDataFieldMapper() != null ) ) return false;
		
		if( ( marcSubField != null && !marcSubField.equals(other.getMarcSubField()) ) ||
			( marcSubField == null && other.getMarcSubField() != null ) ) return false;
		

		return true;
    }
	

}
