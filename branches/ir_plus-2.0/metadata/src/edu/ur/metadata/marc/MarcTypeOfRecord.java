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

package edu.ur.metadata.marc;

import edu.ur.persistent.CommonPersistent;

/**
 * Represents the  Leader 06 type of record.
 * 
 * @author Nathan Sarr
 *
 */
public class MarcTypeOfRecord extends CommonPersistent{

	// Eclipse generated value
	private static final long serialVersionUID = -2110267536536562352L;
	
	// character record type
	private String recordType;

	/**
	 * Package protected constructor 
	 */
	MarcTypeOfRecord(){}
	
	/**
	 * Set the record type and name.
	 * 
	 * @param name
	 * @param recordType
	 */
	public MarcTypeOfRecord(String name, String recordType)
	{
		setRecordType(recordType);
		setName(name);
	}
	
	/**
	 * Get the record type.
	 * 
	 * @return
	 */
	public String getRecordType() {
		return recordType;
	}

	/**
	 * Set the record type
	 * 
	 * @param recordType
	 */
	void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	
	/**
	 * Overrides hash code.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		value += recordType == null ? 0 : recordType.hashCode();
		return value;
	}
	
	/**
	 * Overrides equals.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof MarcTypeOfRecord)) return false;

		final MarcTypeOfRecord other = (MarcTypeOfRecord) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		if( ( recordType != null && !recordType.equals(other.getRecordType()) ) ||
			( recordType == null && other.getRecordType() != null ) ) return false;
		
		return true;
	}
	
	/**
	 * To string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[");
		sb.append(" id = " );
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" recordType = ");
		sb.append(recordType);
		sb.append(" description = ");
		sb.append( description );
		sb.append("]");
		return sb.toString();
	}
}
