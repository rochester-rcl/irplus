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

package edu.ur.ir.oai.metadata.provider.service;

import java.util.Date;

import edu.ur.ir.oai.OaiUtil;
import edu.ur.ir.oai.OaiUtil.DateType;
import edu.ur.ir.oai.exception.BadArgumentException;
import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.metadata.provider.ResumptionToken;

/**
 * Class to help with resumption token data.  
 *
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultResumptionToken implements ResumptionToken
{

	
	/** eclipse generated id */
	private static final long serialVersionUID = 3833203729450793209L;

	/**  OAI set value */
	private String set = null;
	
	/** from date  */
	private Date from = null;
	
	/** the from date type */
	private DateType fromDateType = OaiUtil.DateType.NONE;
	
	/** the until date type */
	private DateType untilDateType = OaiUtil.DateType.NONE;
	
	/** Until date */
	private Date until = null;
	
	/** metadata prefix */
	private String metadataPrefix = null;
	
	/** start value */
	private Long lastId = null;

	/** batch size */
	private Integer batchSize = null;
	
	/** indicates that deleted records should now be sent */
	private Boolean deleted = Boolean.FALSE;
	
	/** the token delimiter */
	public static final String TOKEN_DELIMITER = ";";
	
	/** the set delimiter */
	public static final String SET_DELIMITER = ":";
	
	/** indicates that a non null token should be inserted back into the request */
	private boolean insertToken;
	
	/** size of the list */
	private Integer completeListSize = null;
	
	// indicates this is the inital request
	private boolean initialRequest = true;





	/**
	 * Default constructor
	 */
	public DefaultResumptionToken(){}
	
	/**
	 * Will take a string in the format 
	 * 
	 * set=227&from=1999-02-03&until=2002-04-01start=2987
	 * 
	 * @param values
	 * @throws BadResumptionTokenException 
	 */
	public DefaultResumptionToken(String resumptionToken) throws BadResumptionTokenException, BadArgumentException
	{
		parseResumptionToken(resumptionToken);
	}
	
	
	public Long getLastId() {
		return lastId;
	}

	public void setLastId(Long lastId) {
		this.lastId = lastId;
	}
	
	public void setLastId(String lastId) {
		this.lastId =  Long.valueOf(lastId);
	}
	
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}
	
	public void setFrom(String from) throws BadArgumentException {
		this.fromDateType = OaiUtil.getDateType(from);
		Date zuluFrom = OaiUtil.getDate(from);
		this.from = OaiUtil.getLocalTime(zuluFrom);
	}

	public Date getUntil() {
		return until;
	}

	public void setUntil(Date until) {
		this.until = until;
	}
	
	public void setUntil(String until) throws BadArgumentException {
		this.untilDateType = OaiUtil.getDateType(until);
		Date zuluUntil = OaiUtil.getDate(until);
		this.until = OaiUtil.getLocalTime(zuluUntil);
	}


	public String getMetadataPrefix() {
		return metadataPrefix;
	}

	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}

	public Integer getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}
	
	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}
	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public void setDeleted(String deleted)
	{
		this.deleted = Boolean.valueOf(deleted);
	}
	
	public boolean getInsertToken() {
		return insertToken;
	}

	public void setInsertToken(boolean insertToken) {
		this.insertToken = insertToken;
	}
	
	public Integer getCompleteListSize() {
		return completeListSize;
	}

	public void setCompleteListSize(Integer completeListSize) {
		this.completeListSize = completeListSize;
	}
	
	/**
	 * Get the last set id.  Returns null
	 * if the set is null.
	 * 
	 * @return the last set id or null if the set is empty
	 */
	public Long getLastSetId()
	{
		Long value = null;
		if( set != null )
		{
		    String[] parts = set.split(":");
		    if( parts.length > 0 )
		    {
		    	value = Long.valueOf(parts[parts.length - 1]);
		    }
		}
		return value;
	}
	
	public String toString()
	{
	
		StringBuffer sb = new StringBuffer("[set = ");
		sb.append(set);
		sb.append(" from = ");
		sb.append(from);
		sb.append(" until = ");
		sb.append(until);
		sb.append(" metadataPrefix = ");
		sb.append(metadataPrefix);
		sb.append(" lastId = ");
		sb.append(lastId);
		sb.append(" batchSize = ");
		sb.append(batchSize);
		sb.append(" deleted = ");
		sb.append(deleted);
		sb.append(" inserToken = ");
		sb.append(insertToken);
		sb.append(" completListSize = ");
		sb.append(completeListSize);
		sb.append("]");
		return sb.toString();
	}
	
	
	public String getAsTokenString()
	{
		StringBuffer sb = new StringBuffer("");
		if( set != null )
		{
			sb.append("set=");
			sb.append(set);
		}
		if( from != null )
		{
			if( sb.length() > 0)
			{
				sb.append(TOKEN_DELIMITER);
			}
			sb.append("from=");
			// convert back to zulu time
			sb.append(OaiUtil.getZuluTime(from));
		}
		if( until != null )
		{
			if( sb.length() > 0)
			{
				sb.append(TOKEN_DELIMITER);
			}
			sb.append("until=");
			// convert back to zulu time
			sb.append(OaiUtil.getZuluTime(until));
		}
		if( metadataPrefix != null )
		{
			if( sb.length() > 0)
			{
				sb.append(TOKEN_DELIMITER);
			}
			sb.append("metadataPrefix=");
			sb.append(metadataPrefix);
		}
		if( lastId != null )
		{
			if( sb.length() > 0)
			{
				sb.append(TOKEN_DELIMITER);
			}
			sb.append("lastId=");
			sb.append(lastId);
		}
		if( batchSize != null )
		{
			if( sb.length() > 0)
			{
				sb.append(TOKEN_DELIMITER);
			}
			sb.append("batchSize=");
			sb.append(batchSize);
		}
		if( deleted != null )
		{
			if( sb.length() > 0)
			{
				sb.append(TOKEN_DELIMITER);
			}
			sb.append("deleted=");
			sb.append(deleted);
		}
		return sb.toString();
	}
	
	/**
	 * Helper method to parse the resumption token.
	 * 
	 * @param resumptionToken
	 */
	public void parseResumptionToken(String resumptionToken) throws BadArgumentException, BadResumptionTokenException
	{
		this.set = null;
		this.setBatchSize(null);
		this.from = null;
		this.lastId = null;
		this.setMetadataPrefix(null);
		this.until = null;
		this.deleted = null;
		
		String[] values = resumptionToken.split(TOKEN_DELIMITER);
		for( String aValue : values)
		{
			String[] parts = aValue.split("=");
			if( parts.length == 2)
			{
			    String name = parts[0];
			    String value = parts[1];
			    assignParts(name, value);
			}
			else 
			{
				throw new BadResumptionTokenException("illegal token token string each value should have two parts token =  " + resumptionToken);
			}
		}
	
	}
	
	/**
	 * Determine if this is the initial request.
	 * 
	 * @return true if it is the initial request.
	 */
	public boolean isInitialRequest() {
		return initialRequest;
	}

	/**
	 * Set the initial request.
	 * 
	 * @param initialRequest
	 */
	public void setInitialRequest(boolean initialRequest) {
		this.initialRequest = initialRequest;
	}
	
	public void checkFromUntilDates() throws BadArgumentException
	{
		if( from != null & until != null)
		{
			if( fromDateType != untilDateType )
			{
				throw new BadArgumentException("From and Until date types do not have the same granularity");
			}
		}
	}
	
	/**
	 * Helper method to assign the values to the class.
	 * 
	 * @param name - name of the parameter
	 * @param value - value of the parameter
	 * @throws BadResumptionTokenException 
	 */
	private void assignParts(String name, String value) throws BadArgumentException, BadResumptionTokenException
	{
	
		if( name.equals("set"))
		{
			this.setSet(value);
		}
		else if( name.equals("from"))
		{
			// convert the UTC/Zulu time to local time
			this.setFrom(OaiUtil.getLocalTime(OaiUtil.getDate(value)));
		}
        else if (name.equals("until")) 
        {
        	// convert the UTC/Zulu time to local time
			this.setUntil(OaiUtil.getLocalTime(OaiUtil.getDate(value)));
		}
		else if(name.endsWith("metadataPrefix"))
		{
			setMetadataPrefix(value);
		}
		else if( name.equals("lastId"))
		{
			this.setLastId(Long.valueOf(value));
		}
		else if(name.equals("batchSize"))
		{
			this.setBatchSize(Integer.valueOf(value));
		}
		else if(name.equals("deleted"))
		{
			this.setDeleted(value);
		}
		else
		{
			throw new BadResumptionTokenException("illegal parameter " + name + " value = " + value);
		}
		
	}

}
