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

import edu.ur.ir.item.IdentifierType;
import edu.ur.metadata.dc.DublinCoreEncodingScheme;
import edu.ur.metadata.dc.DublinCoreTerm;
import edu.ur.persistent.BasePersistent;

/**
 * This maps an identifier type to a dublin core mapping.
 * 
 * @author Nathan Sarr
 *
 */
public class IdentifierTypeDublinCoreMapping extends BasePersistent{

	/** eclipse generated id */
	private static final long serialVersionUID = -4006147264601932215L;
	
	/** Identifier type to map to */
	private IdentifierType identifierType;
	
	/** Dublin Core term to map to */
	private DublinCoreTerm dublinCoreTerm;
	
	/** dublin core encoding scheme to map to  */
	private DublinCoreEncodingScheme dublinCoreEncodingScheme;
	
	/**
	 * Default constructor
	 */
	IdentifierTypeDublinCoreMapping(){}

	/**
	 * Construct with an identifier type with the dublin core term
	 * 
	 * @param identifierType - identifier type to map
	 * @param dublinCoreTerm - dublin core term to map to.
	 */
	public IdentifierTypeDublinCoreMapping(IdentifierType identifierType, DublinCoreTerm dublinCoreTerm)
	{
		setIdentifierType(identifierType);
		setDublinCoreTerm(dublinCoreTerm);
	}
	
	/**
	 * Construct with an identifier type with the dublin core term and encoding schemen
	 * 
	 * @param identifierType - identifier type to map
	 * @param dublinCoreTerm - dublin core term to map to.
	 */
	public IdentifierTypeDublinCoreMapping(IdentifierType identifierType, 
			DublinCoreTerm dublinCoreTerm,
			DublinCoreEncodingScheme dublinCoreEncodingScheme)
	{
		this(identifierType, dublinCoreTerm);
		setDublinCoreEncodingScheme(dublinCoreEncodingScheme);
	}
	
	public IdentifierType getIdentifierType() {
		return identifierType;
	}

	public void setIdentifierType(IdentifierType identifierType) {
		this.identifierType = identifierType;
	}

	public DublinCoreTerm getDublinCoreTerm() {
		return dublinCoreTerm;
	}

	public void setDublinCoreTerm(DublinCoreTerm dublinCoreTerm) {
		this.dublinCoreTerm = dublinCoreTerm;
	}

	public DublinCoreEncodingScheme getDublinCoreEncodingScheme() {
		return dublinCoreEncodingScheme;
	}

	public void setDublinCoreEncodingScheme(
			DublinCoreEncodingScheme dublinCoreEncodingScheme) {
		this.dublinCoreEncodingScheme = dublinCoreEncodingScheme;
	}
	
	/**
     * Override Equals.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
    	if (this == o) return true;
		if (!(o instanceof IdentifierTypeDublinCoreMapping)) return false;

		final IdentifierTypeDublinCoreMapping other = (IdentifierTypeDublinCoreMapping) o;

		if( ( dublinCoreTerm != null && !dublinCoreTerm.equals(other.getDublinCoreTerm()) ) ||
			( dublinCoreTerm == null && other.getDublinCoreTerm() != null ) ) return false;
		
		if( ( identifierType != null && !identifierType.equals(other.getIdentifierType()) ) ||
			( identifierType == null && other.getIdentifierType() != null ) ) return false;
		
		if( ( dublinCoreEncodingScheme != null && !dublinCoreEncodingScheme.equals(other.getDublinCoreEncodingScheme()) ) ||
			( dublinCoreEncodingScheme == null && other.getDublinCoreEncodingScheme() != null ) ) return false;
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
    	hash += identifierType == null ? 0 : identifierType.hashCode();
    	hash += dublinCoreEncodingScheme == null ? 0 : dublinCoreEncodingScheme.hashCode();
    	return hash;
    }
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append( "dublin core term = ");
		sb.append(dublinCoreTerm);
		sb.append("identifierType = ");
		sb.append(identifierType);
		sb.append("dublinCoreEncodingScheme = ");
		sb.append(dublinCoreEncodingScheme);
		sb.append("]");
		return sb.toString();
	}



}
