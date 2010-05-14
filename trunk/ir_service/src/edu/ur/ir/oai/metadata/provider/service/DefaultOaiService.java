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


import org.apache.log4j.Logger;

import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.exception.CannotDisseminateFormatException;
import edu.ur.ir.oai.exception.IdDoesNotExistException;
import edu.ur.ir.oai.exception.NoRecordsMatchException;
import edu.ur.ir.oai.exception.NoSetHierarchyException;
import edu.ur.ir.oai.metadata.provider.IdentifyService;
import edu.ur.ir.oai.metadata.provider.ListIdentifiersService;
import edu.ur.ir.oai.metadata.provider.ListSetsService;
import edu.ur.ir.oai.metadata.provider.OaiMetadataProvider;
import edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider;
import edu.ur.ir.oai.metadata.provider.OaiService;

/**
 * Service that provides information for OAI
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultOaiService implements OaiService{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -2559261575500618745L;

	/**  List of oai metadata service providers */
	private OaiMetadataServiceProvider oaiMetadataServiceProvider;

	/** Service to deal with institutional item information */
	private InstitutionalItemService institutionalItemService;
	
	/** oai namespace identifier */
	private String namespaceIdentifier;
	
	/** Service to handle identify information */
	private IdentifyService identifyService;
	
	/** Service to help with listing identifers */
	private ListIdentifiersService listIdentifiersService;

	/** Service to deal with institutional item information */
	private InstitutionalItemVersionService institutionalItemVersionService;
	
	/** service to deal with listing set information */
	private ListSetsService listSetsService;

	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(DefaultOaiService.class);

	/**
	 * Return the get record body as a string.
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiService#getRecord(java.lang.String, java.lang.String)
	 */
	public String getRecord(String identifier, String metadataPrefix)
			throws CannotDisseminateFormatException, IdDoesNotExistException 
	{
		String value = null;
		OaiMetadataProvider oaiMetadataProvider = oaiMetadataServiceProvider.getProvider(metadataPrefix) ;
		if( oaiMetadataProvider != null )
		{
			Long institutionalItemVersionId = getInstitutionalItemVersionId(identifier);
			InstitutionalItemVersion institutionalItemVersion = institutionalItemVersionService.getInstitutionalItemVersion(institutionalItemVersionId, false);
			
			if( institutionalItemVersion != null )
			{
			    value = oaiMetadataProvider.getXml(institutionalItemVersion);
			}
			else
			{
				throw new IdDoesNotExistException("identifier " + identifier + " does not exist");
			}
		}
		else
		{
			throw new CannotDisseminateFormatException("Format is not supported");
		}
		return value;
	}
	
	/**
	 * List the identifiers in the system.
	 * @throws NoSetHierarchyException 
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiService#listIdentifiers(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String listIdentifiers(String metadataPrefix, String set, String from, String until, String resumptionToken) throws BadResumptionTokenException,
			CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException{
        return listIdentifiersService.listIdentifiers(metadataPrefix, set, from, until, resumptionToken);
	}

	/**
	 * Parse the oai id.
	 * 
	 * @param oaiId
	 * @return the unique identifier for the publication.  this returns null if the
	 * id could not be established.
	 */
	private Long getInstitutionalItemVersionId(String oaiId)
	{
		String[] values = oaiId.split(":");
		Long value = null;
		if( values.length == 3)
		{
			try
			{
			    value = Long.valueOf(values[2]);
			}
			catch(Exception e)
			{
				log.error("the oai identifier " + oaiId + " could not be parsed");
			}
		}
		return value;
	}
	


	/**
	 * Get the identify response.
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiService#identify()
	 */
	public String identify() {
		return identifyService.identify();
	}


	/**
	 * Get the list of sets in the repository
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiService#listSets(java.lang.String)
	 */
	public String listSets(String resumptionToken)
			throws BadResumptionTokenException, NoSetHierarchyException {
		return listSetsService.listSets(resumptionToken);
	}
	
	public ListSetsService getListSetsService() {
		return listSetsService;
	}

	public void setListSetsService(ListSetsService listSetsService) {
		this.listSetsService = listSetsService;
	}
	
	public IdentifyService getIdentifyService() {
		return identifyService;
	}

	public void setIdentifyService(IdentifyService identifyService) {
		this.identifyService = identifyService;
	}

	public InstitutionalItemVersionService getInstitutionalItemVersionService() {
		return institutionalItemVersionService;
	}

	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}

	public ListIdentifiersService getListIdentifiersService() {
		return listIdentifiersService;
	}

	public void setListIdentifiersService(
			ListIdentifiersService listIdentifiersService) {
		this.listIdentifiersService = listIdentifiersService;
	}
	
	public OaiMetadataServiceProvider getOaiMetadataServiceProvider() {
		return oaiMetadataServiceProvider;
	}

	public void setOaiMetadataServiceProvider(
			OaiMetadataServiceProvider oaiMetadataServiceProvider) {
		this.oaiMetadataServiceProvider = oaiMetadataServiceProvider;
	}
	
	/**
	 * Get the namespace identifier.
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider#getNamespaceIdentifier()
	 */
	public String getNamespaceIdentifier() {
		return namespaceIdentifier;
	}

	/**
	 * Set the namespace identifier.
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider#setNamespaceIdentifier(java.lang.String)
	 */
	public void setNamespaceIdentifier(String namespaceIdentifier) {
		this.namespaceIdentifier = namespaceIdentifier;
	}
	
	/**
	 * Get the institutional item service.
	 * 
	 * @return
	 */
	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	/**
	 * Set the institutional item service.
	 * 
	 * @param institutionalItemService
	 */
	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

}
