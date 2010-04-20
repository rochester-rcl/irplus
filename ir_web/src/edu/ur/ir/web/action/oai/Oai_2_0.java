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

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.oai.OaiUtil;

/**
 * Handles an oai request.
 * 
 * @author Nathan Sarr
 *
 */
public class Oai_2_0 extends ActionSupport{
	

	

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
	

	public String execute()
	{
		if( !OaiUtil.isValidOaiVerb(verb))
		{
			return "badVerb";
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
		String responseDate = OaiUtil.zuluTime(d);
		return responseDate;
	}



}
