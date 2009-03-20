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

package edu.ur.ir.handle;

import edu.ur.persistent.BasePersistent;

/**
 * Class that represents localName information.  
 * 
 * @author Nathan Sarr
 *
 */
public class HandleInfo extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -4297910642983562791L;
	
	/** the main data type used for handles in this system */
	public static final String URL_DATA_TYPE = "URL";
	
	/** 24 hours - default time to live */
	public static final Integer DEFAULT_TIME_TO_LIVE = new Integer(86400);
	
	/** default integer index */
	public static final Integer DEFAULT_INDEX = new Integer(100);
	
	/**  relative time to live */
	public static final Integer RELATIVE_TIME_TO_LIVE_TYPE = new Integer(100);
	
	/** time stamp value  */
	public static final Integer DEFAULT_TIMESTAMP_VALUE = new Integer(100);
	
	/** default references */
	public static final String DEFAULT_REFERENCES = "";
	
	/** default admin read */
	public static final boolean DEFAULT_ADMIN_READ = true;
	
	/** default public read */
	public static final boolean DEFAULT_PUBLIC_READ = true;
	
	/** default admin write */
	public static final boolean DEFAULT_ADMIN_WRITE = true;
	
	/** default public write */
	public static final boolean DEFAULT_PUBLIC_WRITE = false;
	
	/** index - positive integer value - this should be unique across the local naming authority */
	private Integer index = DEFAULT_INDEX ;
	
	private String localName;
	

	/** the type of data for the localName data */
	private String dataType = URL_DATA_TYPE;
	
	/** data for the localName - will usually be a URL */
	private String data;
	
	/** time to live type */
	private Integer timeToLiveType = RELATIVE_TIME_TO_LIVE_TYPE;
	
	/** time to live */
	private Integer timeToLive = DEFAULT_TIME_TO_LIVE;
	
	/** time stamp value */
	private Integer timestamp = DEFAULT_TIMESTAMP_VALUE;
	
	/** references for this localName */
	private String references = DEFAULT_REFERENCES;
	
	/** admin read permissions */
	private boolean adminRead = DEFAULT_ADMIN_READ;
	
	/** admin write permissions */
	private boolean adminWrite = DEFAULT_ADMIN_WRITE;
	
	/** public read permissions */
	private boolean publicRead = DEFAULT_PUBLIC_READ;
	
	/** public write permissions */
	private boolean publicWrite = DEFAULT_PUBLIC_WRITE;
	
	/** name authority for this localName */
	private HandleNameAuthority nameAuthority;
	

	/**
	 * Package protected constructor
	 */
	HandleInfo(){}
	
	/**
	 * Public constructor for localName.
	 * 
	 * @param index - unique index for the naming authority
	 * @param data - data for the localName
	 * @param nameAuthority - name authority for this localName
	 */
	public HandleInfo(String handle, String data, HandleNameAuthority nameAuthority)
	{
	   setLocalName(handle);
	   setData(data);
	   setNameAuthority(nameAuthority);
	}
	
	
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Integer getTimeToLiveType() {
		return timeToLiveType;
	}
	public void setTimeToLiveType(Integer timeToLiveType) {
		this.timeToLiveType = timeToLiveType;
	}
	public Integer getTimeToLive() {
		return timeToLive;
	}
	public void setTimeToLive(Integer timeToLive) {
		this.timeToLive = timeToLive;
	}
	public Integer getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}
	public String getReferences() {
		return references;
	}
	public void setReferences(String references) {
		this.references = references;
	}
	public boolean isAdminRead() {
		return adminRead;
	}
	public void setAdminRead(boolean adminRead) {
		this.adminRead = adminRead;
	}
	public boolean isAdminWrite() {
		return adminWrite;
	}
	public void setAdminWrite(boolean adminWrite) {
		this.adminWrite = adminWrite;
	}
	public boolean isPublicRead() {
		return publicRead;
	}
	public void setPublicRead(boolean publicRead) {
		this.publicRead = publicRead;
	}
	public boolean isPublicWrite() {
		return publicWrite;
	}
	public void setPublicWrite(boolean publicWrite) {
		this.publicWrite = publicWrite;
	}
	
	public HandleNameAuthority getNameAuthority() {
		return nameAuthority;
	}

	public void setNameAuthority(HandleNameAuthority nameAuthority) {
		this.nameAuthority = nameAuthority;
	}

	public int hashCode()
	{
		int value = 0;
		value += nameAuthority == null ? 0 : nameAuthority.hashCode();
		value += localName == null ? 0 : localName.hashCode();
		value += index == null ? 0 : index.hashCode();
		return value;
	}
	
	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String handle) {
		this.localName = handle;
	}

	
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof HandleInfo)) return false;

		final HandleInfo other = (HandleInfo) o;
		if( (localName != null && !localName.equals(other.getLocalName()) ) ||
			(localName == null && other.getLocalName() != null ) ) return false;
		
		if( (index != null && !index.equals(other.getIndex()) ) ||
			(index == null && other.getIndex() != null ) ) return false;
		
		if( (nameAuthority != null && !nameAuthority.equals(other.getNameAuthority()) ) ||
			(nameAuthority == null && other.getNameAuthority() != null ) ) return false;
	
		return true;		
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id ");
		sb.append(id);
		sb.append(" localName = ");
		sb.append(localName);
		sb.append(" index = ");
		sb.append(index);
		sb.append(" dataType = ");
		sb.append(dataType);
		sb.append(" data = ");
		sb.append(data);
		sb.append(" timeToLiveType = ");
		sb.append(timeToLiveType);
		sb.append(" timeToLive = ");
		sb.append(timeToLive);
		sb.append(" timestamp= ");
		sb.append(timestamp);
		sb.append(" references = ");
		sb.append(references);
		sb.append(" adminRead = ");
		sb.append(adminRead);
		sb.append(" adminWrite ");
		sb.append(adminWrite);
		sb.append(" publicRead = ");
		sb.append(publicRead);
		sb.append(" publicWrite = ");
		sb.append(publicWrite);
		sb.append("]");
		return sb.toString();
	}

}
