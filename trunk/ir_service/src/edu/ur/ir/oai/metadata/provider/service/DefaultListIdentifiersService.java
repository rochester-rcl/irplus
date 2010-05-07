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

import java.util.List;

import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.exception.CannotDisseminateFormatException;
import edu.ur.ir.oai.exception.NoRecordsMatchException;
import edu.ur.ir.oai.exception.NoSetHierarchyException;
import edu.ur.ir.oai.metadata.provider.ListIdentifiersService;
import edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider;

/**
 * Default implementation of the list identifiers service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultListIdentifiersService implements ListIdentifiersService{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 9056980425349175595L;
	
	/**  Default batch size for harvesting */
	private int batchSize = 100;
	
	/**  List of oai metadata service providers */
	private OaiMetadataServiceProvider oaiMetadataServiceProvider;
	
	/** Service for dealing with institutional item information */
	private InstitutionalItemVersionService institutionalItemVersionService;
	
	/**
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.ListIdentifiersService#listIdentifiers(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String listIdentifiers(String metadataPrefix, String set,
			String from, String until, String resumptionToken)
			throws BadResumptionTokenException,
			CannotDisseminateFormatException, NoRecordsMatchException,
			NoSetHierarchyException {
		
		if( !oaiMetadataServiceProvider.supports(metadataPrefix) )
		{
			throw new CannotDisseminateFormatException("Format: " + metadataPrefix + " is not supported");
		}

		// parse the token if it exists
		DefaultResumptionToken defaultToken = null;
		if(resumptionToken != null && !resumptionToken.equals(""))
		{
			defaultToken = new DefaultResumptionToken(resumptionToken);
		}
		else
		{
			defaultToken = new DefaultResumptionToken();
			
			defaultToken.setBatchSize(batchSize);
			if( from != null && !from.equals(""))
			{
			    defaultToken.setFrom(from);
			}
			if( set != null && !set.equals(""))
			{
				defaultToken.setSet(set);
			}
			if( until != null && !until.equals(""))
			{
				defaultToken.setUntil(until);
			}
			defaultToken.setMetadataPrefix(metadataPrefix);
			defaultToken.setLastId(0l);
			
		}

		// do batch size plus one - if we retrieve all records then another request must be issued
		// with resumption token
		List<InstitutionalItemVersion> versions = institutionalItemVersionService.getItemsIdOrder(defaultToken.getLastId(), batchSize + 1);
		int size = versions.size();
		
		// we will need to send resumption token
		if( size == (batchSize + 1))
		{
			// remove the last item as it should not be sent
			// this only indicates that there is one more than the batch size
			// allows
			versions.remove(size - 1);
		}
		
		return listIdentifiers(versions);

		
		
	}
	
	/**
	 * A request to list identifiers with only the metadata prefix.
	 * 
	 * @param metadataPrefix
	 * @return
	 */
	private String listIdentifiers(List<InstitutionalItemVersion> versions)
	{
		String value = "";
		
		
		
		return value;
	}
	
	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	
	public OaiMetadataServiceProvider getOaiMetadataServiceProvider() {
		return oaiMetadataServiceProvider;
	}

	public void setOaiMetadataServiceProvider(
			OaiMetadataServiceProvider oaiMetadataServiceProvider) {
		this.oaiMetadataServiceProvider = oaiMetadataServiceProvider;
	}

	public InstitutionalItemVersionService getInstitutionalItemVersionService() {
		return institutionalItemVersionService;
	}

	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}
	
	

}
