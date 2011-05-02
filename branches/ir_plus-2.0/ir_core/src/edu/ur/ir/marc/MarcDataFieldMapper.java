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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.IdentifierType;
import edu.ur.metadata.marc.MarcDataField;
import edu.ur.metadata.marc.MarcSubField;
import edu.ur.persistent.BasePersistent;

/**
 * Allows the mapping between data fields and IR+ data
 * 
 * @author Nathan Sarr
 *
 */
public class MarcDataFieldMapper extends BasePersistent{
	
	//Eclipse generated id
	private static final long serialVersionUID = 2158911043840732836L;

	// data to map to this marc data field
	private MarcDataField marcDataField;
	
	// first indicator
	private String indicator1 = " ";
	
	//second indicatior
	private String indicator2 = " ";
	
	// list of identifier type mappings
	private Set<IdentifierTypeSubFieldMapper> identifierTypeSubFieldMappings = new HashSet<IdentifierTypeSubFieldMapper>();

	// list of extent type mappings
	private Set<ExtentTypeSubFieldMapper> extentTypeSubFieldMappings = new HashSet<ExtentTypeSubFieldMapper>();


	/**
	 * Package protected constructor 
	 */
	MarcDataFieldMapper(){}
	
	/**
	 * Create the marc data field mapper with the specified marc data field.
	 * 
	 * @param marcDataField - marc data field to set to be mapped.
	 */
	public MarcDataFieldMapper(MarcDataField marcDataField)
	{
		setMarcDataField(marcDataField);
	}
	
	/**
	 * Get the identifier type sub field mapper by 
	 * @param identifierType
	 * @param MarcSubField marcSubField
	 * 
	 * @return the found identifier sub type field mapper otherwise null 
	 */
	public IdentifierTypeSubFieldMapper get(IdentifierType identifierType,MarcSubField marcSubField)
	{
		for(IdentifierTypeSubFieldMapper itsfm : identifierTypeSubFieldMappings)
		{
			if(itsfm.getIdentifierType().getId().equals(identifierType.getId()) && 
			   itsfm.getMarcSubField().equals(marcSubField))
			{
				return itsfm;
			}
		}
		return null;
	}
	
	/**
	 * Create a new identifier type subfield mapper.
	 * 
	 * @param identifierType
	 * @param marcSubField
	 * @return
	 */
	public IdentifierTypeSubFieldMapper add(IdentifierType identifierType, MarcSubField marcSubField)
	{
		IdentifierTypeSubFieldMapper subFieldMapper = get(identifierType, marcSubField);
		if( subFieldMapper == null )
		{
			subFieldMapper = new IdentifierTypeSubFieldMapper(identifierType, this, marcSubField);
			identifierTypeSubFieldMappings.add(subFieldMapper);
		}
		return subFieldMapper;
	}
	
	/**
	 * Remove the identifier type sub field mapper from the list of mappers.
	 * 
	 * @param mapper identifier type sub field mapper to remove
	 * @return true if the mapper is removed otherwise false
	 */
	public boolean remove(IdentifierTypeSubFieldMapper mapper)
	{
		return identifierTypeSubFieldMappings.remove(mapper);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Get the extent type sub field mapper by 
	 * @param extentType
	 * @param MarcSubField marcSubField
	 * 
	 * @return the found extent sub type field mapper otherwise null 
	 */
	public ExtentTypeSubFieldMapper get(ExtentType extentType,MarcSubField marcSubField)
	{
		for(ExtentTypeSubFieldMapper etsfm : extentTypeSubFieldMappings)
		{
			if(etsfm.getExtentType().getId().equals(extentType.getId()) && 
			   etsfm.getMarcSubField().equals(marcSubField))
			{
				return etsfm;
			}
		}
		return null;
	}
	
	/**
	 * Create a new identifier type subfield mapper.
	 * 
	 * @param identifierType
	 * @param marcSubField
	 * @return
	 */
	public ExtentTypeSubFieldMapper add(ExtentType extentType, MarcSubField marcSubField)
	{
		ExtentTypeSubFieldMapper subFieldMapper = get(extentType, marcSubField);
		if( subFieldMapper == null )
		{
			subFieldMapper = new ExtentTypeSubFieldMapper(extentType, this, marcSubField);
			extentTypeSubFieldMappings.add(subFieldMapper);
		}
		return subFieldMapper;
	}
	
	/**
	 * Remove the identifier type sub field mapper from the list of mappers.
	 * 
	 * @param mapper identifier type sub field mapper to remove
	 * @return true if the mapper is removed otherwise false
	 */
	public boolean remove(ExtentTypeSubFieldMapper mapper)
	{
		return extentTypeSubFieldMappings.remove(mapper);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Get the marc data field.
	 * 
	 * @return marc data field
	 */
	public MarcDataField getMarcDataField() {
		return marcDataField;
	}


	/**
	 * Set the marc data field.
	 * 
	 * @param marcDataField
	 */
	public void setMarcDataField(MarcDataField marcDataField) {
		this.marcDataField = marcDataField;
	}


	/**
	 * Get the indicator.
	 * 
	 * @return
	 */
	public String getIndicator1() {
		return indicator1;
	}


	/**
	 * Set the indicator.
	 * 
	 * @param indicator1
	 */
	public void setIndicator1(String indicator1) {
		if( indicator1 != null && !indicator1.trim().equals(""))
		{
		    this.indicator1 = indicator1;
		}
		else
		{
			this.indicator1 = " ";
		}
	}


	/**
	 * Get the second indicator.
	 * 
	 * @return
	 */
	public String getIndicator2() {
		return indicator2;
	}


	/**
	 * Set the second indicator.
	 * 
	 * @param indicator2
	 */
	public void setIndicator2(String indicator2) {
		if( indicator2 != null && !indicator2.trim().equals(""))
		{
		    this.indicator2 = indicator2;
		}
		else
		{
			this.indicator2 = " ";
		}
	}
	
	/**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
	public int hashCode()
    {
    	int hash = 0;
    	hash += marcDataField == null ? 0 : marcDataField.hashCode();
    	return hash;
    }
    
    /**
     * To string
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
    	StringBuffer sb = new StringBuffer("[Marc data field mapper id = ");
		sb.append(id);
		sb.append(" marc data field = ");
		sb.append(marcDataField);
		sb.append("]");
		return sb.toString();
    }
    
	/**
	 * Get the identifier type sub field mappings.
	 * 
	 * @return the subfield mappings
	 */
	public Set<IdentifierTypeSubFieldMapper> getIdentifierTypeSubFieldMappings() {
		return Collections.unmodifiableSet(identifierTypeSubFieldMappings);
	}

	
	/**
	 * Package protected setter.
	 * 
	 * @param identifierTypeSubFieldMappings
	 */
	void setIdentifierTypeSubFieldMappings(
			Set<IdentifierTypeSubFieldMapper> identifierTypeSubFieldMappings) {
		this.identifierTypeSubFieldMappings = identifierTypeSubFieldMappings;
	}
	
	/**
	 * Get the extent type sub field mappings.
	 * 
	 * @return the subfield mappings
	 */
	public Set<ExtentTypeSubFieldMapper> getExtentTypeSubFieldMappings() {
		return Collections.unmodifiableSet(extentTypeSubFieldMappings);
	}

	
	/**
	 * Package protected setter.
	 * 
	 * @param identifierTypeSubFieldMappings
	 */
	void setExtentTypeSubFieldMappings(
			Set<ExtentTypeSubFieldMapper> extentTypeSubFieldMappings) {
		this.extentTypeSubFieldMappings = extentTypeSubFieldMappings;
	}
    
    /**
     * Equals.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
    	if (this == o) return true;
		if (!(o instanceof MarcDataFieldMapper)) return false;

		final MarcDataFieldMapper other = (MarcDataFieldMapper) o;

		if( ( marcDataField != null && !marcDataField.equals(other.getMarcDataField()) ) ||
			( marcDataField == null && other.getMarcDataField() != null ) ) return false;
		
		if( ( indicator1 != null && !indicator1.equals(other.getIndicator1()) ) ||
			( indicator1 == null && other.getIndicator1() != null ) ) return false;
		
		if( ( indicator2 != null && !indicator2.equals(other.getIndicator2()) ) ||
			( indicator2 == null && other.getIndicator2() != null ) ) return false;
		return true;
    }
    
    public char getIndicator1AsChar()
    {
    	if( indicator1 == null || indicator1.length() == 0 || indicator1.trim().equals(""))
    	{
    		return ' ';
    	}
    	else
    	{
    		return indicator1.charAt(0);
    	}
    }
    
    public char getIndicator2AsChar()
    {
    	if( indicator2 == null || indicator2.length() == 0 || indicator2.trim().equals(""))
    	{
    		return ' ';
    	}
    	else
    	{
    		return indicator2.charAt(0);
    	}
    }

}
