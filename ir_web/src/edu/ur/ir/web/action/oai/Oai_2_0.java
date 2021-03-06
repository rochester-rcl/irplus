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
package edu.ur.ir.web.action.oai;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.interceptor.ParameterAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.oai.OaiUtil;
import edu.ur.ir.oai.exception.BadArgumentException;
import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.exception.CannotDisseminateFormatException;
import edu.ur.ir.oai.exception.IdDoesNotExistException;
import edu.ur.ir.oai.exception.NoMetadataFormatsException;
import edu.ur.ir.oai.exception.NoRecordsMatchException;
import edu.ur.ir.oai.exception.NoSetHierarchyException;
import edu.ur.ir.oai.metadata.provider.OaiService;

/**
 * Handles an oai request.
 * 
 * @author Nathan Sarr
 *
 */
public class Oai_2_0 extends ActionSupport implements ParameterAware{
	

	/** eclipse generated id */
	private static final long serialVersionUID = -338408426728792558L;

	/** oai verb passed in */
	private String verb;

	/** identifier passed in */
	private String identifier;
	
	/** metadata prefix passed in */
	private String metadataPrefix;
	
	/** from date */
	private String from;
	
	/** until date */
	private String until;
	
	/** set to be retrieved */
	private String set;
	
	/** resumption token  */
	private String resumptionToken;
	
	/** Output of the oai information */
	private String oaiOutput;
	
	/** Service to help deal with underlying calls for oai information */
	private OaiService oaiService;
	
	/** parameters passed in */
	private Map<String, String[]> parameters;

	public String execute()
	{
		try {
			this.checkParameters();
		} catch (BadArgumentException e) {
			oaiOutput = e.getMessage();
			return "badArgument";
		}
		
		if( !OaiUtil.isValidOaiVerb(verb))
		{
			return "badVerb";
		}
		else if( verb.equalsIgnoreCase(OaiUtil.GET_RECORD_VERB))
		{
			
			try {
				oaiOutput = oaiService.getRecord(identifier, metadataPrefix);
				return "getRecord";
			} catch (CannotDisseminateFormatException e) {
				oaiOutput = e.getMessage();
				return "cannotDisseminateFormat";
			} catch (IdDoesNotExistException e) {
				oaiOutput = e.getMessage();
				return "idDoesNotExist";
			} catch (BadArgumentException e) {
				oaiOutput = e.getMessage();
				return "badArgument";
			}
		}
		else if( verb.equalsIgnoreCase(OaiUtil.IDENTIFY_VERB))
		{
			try {
				if(parameters.size() > 1)
				{
					oaiOutput = "More than one argument";
					return "badArgument";
				}
				else
				{
				    oaiOutput = oaiService.identify();
				}
			} catch (BadArgumentException e) {
				return "badArgument";
			}
			return "identify";
		}
		else if( verb.equalsIgnoreCase(OaiUtil.LIST_IDENTIFIERS_VERB))
		{
			try {
				oaiOutput = oaiService.listIdentifiers(metadataPrefix, set, from, until, resumptionToken);
			    return "listIdentifiers";
			} catch (BadResumptionTokenException e) {
				oaiOutput = e.getMessage();
				return("badResumptionToken");
			} catch (CannotDisseminateFormatException e) {
				oaiOutput = e.getMessage();
				return "cannotDisseminateFormat";
			} catch (NoRecordsMatchException e) {
				oaiOutput = e.getMessage();
				return "noRecordsMatch";
			} catch (NoSetHierarchyException e) {
				oaiOutput = e.getMessage();
				return "noSetHierarchy";
			} catch (BadArgumentException e) {
				oaiOutput = e.getMessage();
				return "badArgument";
			}
		}
		else if( verb.equalsIgnoreCase(OaiUtil.LIST_RECORDS_VERB))
		{
			try {
				oaiOutput = oaiService.listRecords(metadataPrefix, set, from, until, resumptionToken);
				return "listRecords";
			} catch (BadArgumentException e) {
				oaiOutput = e.getMessage();
				return "badArgument";
			} catch (BadResumptionTokenException e) {
				oaiOutput = e.getMessage();
				return("badResumptionToken");
			} catch (CannotDisseminateFormatException e) {
				oaiOutput = e.getMessage();
				return "cannotDisseminateFormat";
			} catch (NoRecordsMatchException e) {
				oaiOutput = e.getMessage();
				return "noRecordsMatch";
			} catch (NoSetHierarchyException e) {
				oaiOutput = e.getMessage();
				return "noSetHierarchy";
			}
		    
		}
		else if( verb.equalsIgnoreCase(OaiUtil.LIST_SETS_VERB))
		{
			try {
				oaiOutput = oaiService.listSets(resumptionToken);
				return "listSets";
			} catch (BadResumptionTokenException e) {
				oaiOutput = e.getMessage();
				return("badResumptionToken");
			} catch (NoSetHierarchyException e) {
				oaiOutput = e.getMessage();
				return "noSetHierarchy";
			} catch (BadArgumentException e) {
				oaiOutput = e.getMessage();
				return "badArgument";
			}
		}
		else if( verb.equalsIgnoreCase(OaiUtil.LIST_METADATA_FORMATS_VERB))
		{
			try {
				oaiOutput = oaiService.listMetadataFormats(identifier);
				return "listMetadataFormats";
			} catch (BadArgumentException e) {
				oaiOutput = e.getMessage();
				return "badVerb";
			} catch (IdDoesNotExistException e) {
				oaiOutput = e.getMessage();
				return "idDoesNotExist";
			} catch (NoMetadataFormatsException e) {
				oaiOutput = e.getMessage();
				return "cannotDisseminateFormat";
			}
		}
		
		return SUCCESS;
	
	}
	
	
	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getMetadataPrefix() {
		return metadataPrefix;
	}

	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getUntil() {
		return until;
	}

	public void setUntil(String until) {
		this.until = until;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public String getResumptionToken() {
		return resumptionToken;
	}

	public void setResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}
	
	
	public String getResponseDate() {
		Date d = new Date();
		String responseDate = OaiUtil.getZuluTime(d);
		return responseDate;
	}

	public String getOaiOutput() {
		return oaiOutput;
	}

	public void setOaiService(OaiService oaiService) {
		this.oaiService = oaiService;
	}


	/* (non-Javadoc)
	 * @see org.apache.struts2.interceptor.ParameterAware#setParameters(java.util.Map)
	 */
	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}
	
	private void checkParameters() throws BadArgumentException
	{
		Set<String> keys = parameters.keySet();
		
		for(String key : keys)
		{
			if( parameters.get(key) == null )
			{
				throw new BadArgumentException("parameter " + key + " is null" );
			}
			else if( parameters.get(key).length > 1 )
			{
				throw new BadArgumentException(" parameter " + key + " appers " 
						+ parameters.get(key).length 
						+ " times and should only appear once");
			}
		}
		
		// if there is a resumption token there should only be the verb remaining
		if( resumptionToken != null )
		{
			if(keys.size() > 2 )
			{
				throw new BadArgumentException("Only resumption token and verb should be passed in when using resumption token");
			}
		}
	   
	}


}
