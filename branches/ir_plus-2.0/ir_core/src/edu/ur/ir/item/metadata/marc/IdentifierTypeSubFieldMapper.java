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

import edu.ur.ir.item.IdentifierType;
import edu.ur.metadata.marc.MarcSubField;


/**
 * Mapps a sub filed to a subfield.
 * 
 * 
 * @author Nathan Sarr
 *
 */
public class IdentifierTypeSubFieldMapper extends MarcSubFieldMapper{
	
	//eclipse generated id.
	private static final long serialVersionUID = -2540077455609267252L;
	
	// identifier type to map this to
	private IdentifierType identifierType;

	/**
	 * Package protected constructor
	 */
	IdentifierTypeSubFieldMapper(){}
	
	/**
	 * Create an identifier type sub field mapper.
	 * 
	 * @param identifierType - identifier to map
	 * @param marcDataField - parent marc data field
	 * @param marcSubField - marc sub field to map the identifier type to
	 */
	public IdentifierTypeSubFieldMapper(IdentifierType identifierType, 
			MarcDataFieldMapper marcDataFieldMapper, MarcSubField marcSubField)
	{
		setIdentifierType(identifierType);
		setMarcDataFieldMapper(marcDataFieldMapper);
		setMarcSubField(marcSubField);
	}
	
	/**
	 * Get the identifier type.
	 * 
	 * @return
	 */
	public IdentifierType getIdentifierType() {
		return identifierType;
	}

	/**
	 * Set the identifier type.
	 * 
	 * @param identifierType
	 */
	public void setIdentifierType(IdentifierType identifierType) {
		this.identifierType = identifierType;
	}
	
	/**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += identifierType == null ? 0 : identifierType.hashCode();
    	hash += marcDataFieldMapper == null ? 0 : marcDataFieldMapper.hashCode();
    	hash += marcSubField == null ? 0 : marcSubField.hashCode();
    	return hash;
    }
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer("[Marc identifier type sub field mapper id = ");
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
		if (!(o instanceof IdentifierTypeSubFieldMapper)) return false;

		final IdentifierTypeSubFieldMapper other = (IdentifierTypeSubFieldMapper) o;

		if( ( identifierType != null && !identifierType.equals(other.getIdentifierType()) ) ||
			( identifierType == null && other.getIdentifierType() != null ) ) return false;
		
		if( ( marcDataFieldMapper != null && !marcDataFieldMapper.equals(other.getMarcDataFieldMapper()) ) ||
			( marcDataFieldMapper == null && other.getMarcDataFieldMapper() != null ) ) return false;
		
		if( ( marcSubField != null && !marcSubField.equals(other.getMarcSubField()) ) ||
			( marcSubField == null && other.getMarcSubField() != null ) ) return false;
		

		return true;
    }
	
}
