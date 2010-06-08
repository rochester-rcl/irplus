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


import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import edu.ur.ir.institution.DeletedInstitutionalItemVersion;
import edu.ur.ir.institution.DeletedInstitutionalItemVersionService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.oai.exception.BadArgumentException;
import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.exception.CannotDisseminateFormatException;
import edu.ur.ir.oai.exception.IdDoesNotExistException;
import edu.ur.ir.oai.exception.NoMetadataFormatsException;
import edu.ur.ir.oai.exception.NoRecordsMatchException;
import edu.ur.ir.oai.exception.NoSetHierarchyException;
import edu.ur.ir.oai.metadata.provider.IdentifyService;
import edu.ur.ir.oai.metadata.provider.ListIdentifiersService;
import edu.ur.ir.oai.metadata.provider.ListMetadataFormatsService;
import edu.ur.ir.oai.metadata.provider.ListRecordsService;
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
	
	/** Service to deal with deleted institutional item information */
	private DeletedInstitutionalItemVersionService deletedInstitutionalItemVersionService;

	/** service to deal with listing set information */
	private ListSetsService listSetsService;
	
	/** list records service */
	private ListRecordsService listRecordsService;

	/** service to list metadata formats */
	private ListMetadataFormatsService listMetadataFormatsService;

	/**
	 * Return the get record body as a string.
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiService#getRecord(java.lang.String, java.lang.String)
	 */
	public String getRecord(String identifier, String metadataPrefix)
			throws CannotDisseminateFormatException, IdDoesNotExistException, BadArgumentException 
	{
		if( identifier == null || identifier.trim().equals(""))
		{
			throw new BadArgumentException("missing identifier");
		}
		if( metadataPrefix == null || metadataPrefix.equals(""))
		{
			throw new BadArgumentException("missing metadata prefix");
		}
		
		Long institutionalItemVersionId = DefaultOaiIdentifierHelper.getInstitutionalItemVersionId(identifier);
		InstitutionalItemVersion institutionalItemVersion = institutionalItemVersionService.getInstitutionalItemVersion(institutionalItemVersionId, false);
		DeletedInstitutionalItemVersion deletedVersion = null;
		if( institutionalItemVersion == null )
        {
			deletedVersion = deletedInstitutionalItemVersionService.getDeletedVersionByItemVersionId(institutionalItemVersionId);
			if( deletedVersion == null )
			{
				throw new IdDoesNotExistException("identifier " + identifier + " does not exist");
			}
        }
		
		OaiMetadataProvider oaiMetadataProvider = oaiMetadataServiceProvider.getProvider(metadataPrefix) ;
		if( oaiMetadataProvider != null )
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			 
			try {
			    builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
			    throw new IllegalStateException(e);
			}
			
			DOMImplementation impl = builder.getDOMImplementation();
			DOMImplementationLS domLs = (DOMImplementationLS)impl.getFeature("LS" , "3.0");
			LSSerializer serializer = domLs.createLSSerializer();
			LSOutput lsOut= domLs.createLSOutput();
			StringWriter stringWriter = new StringWriter();
			lsOut.setCharacterStream(stringWriter);

			Document doc = impl.createDocument(null, "record", null);
			Element record = doc.getDocumentElement();
			
			
			if( institutionalItemVersion != null )
			{
			    oaiMetadataProvider.addXml(record, institutionalItemVersion);
			}
			else
			{
				oaiMetadataProvider.addXml(record, deletedVersion);
			}
			
			serializer.getDomConfig().setParameter("xml-declaration", false);
			 
			serializer.write(record, lsOut);
			return stringWriter.getBuffer().toString();
		}
		else
		{
			throw new CannotDisseminateFormatException("Format is not supported");
		}
	}
	
	/**
	 * List the identifiers in the system.
	 * @throws NoSetHierarchyException 
	 * @throws BadArgumentException 
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiService#listIdentifiers(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String listIdentifiers(String metadataPrefix, String set, String from, String until, String resumptionToken) throws BadResumptionTokenException,
			CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException, BadArgumentException{
        return listIdentifiersService.listIdentifiers(metadataPrefix, set, from, until, resumptionToken);
	}


	/** List the records
	 * @see edu.ur.ir.oai.metadata.provider.OaiService#listRecords(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String listRecords(String metadataPrefix, String set, String from,
			String until, String resumptionToken) throws BadArgumentException,
			BadResumptionTokenException, CannotDisseminateFormatException,
			NoRecordsMatchException, NoSetHierarchyException,
			BadArgumentException {
		return listRecordsService.listRecords(metadataPrefix, set, from, until, resumptionToken);
	}


	/**
	 * Get the identify response.
	 * @throws BadArgumentException 
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiService#identify()
	 */
	public String identify() throws BadArgumentException {
		return identifyService.identify();
	}
	
	/**
	 * Interface for listing the metadata formats for a specific identifier
	 * 
	 * @param identifier - optional oai identifier for a specific item.
	 * 
	 * @return an xml string representing the formats made available by the system
	 *  
	 * @throws BadArgumentException - if a bad argument is passed in
	 * @throws IdDoesNotExistException - if the id does not exist
	 * @throws NoMetadataFormatsException - no metadata formats exist for the specified id
	 */
	public String listMetadataFormats(String identifier)
			throws BadArgumentException, IdDoesNotExistException,
			NoMetadataFormatsException {
		return listMetadataFormatsService.listMetadataFormats(identifier);
	}


	/**
	 * Get the list of sets in the repository
	 * @throws BadArgumentException 
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiService#listSets(java.lang.String)
	 */
	public String listSets(String resumptionToken)
			throws BadResumptionTokenException, NoSetHierarchyException, BadArgumentException {
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

	public ListMetadataFormatsService getListMetadataFormatsService() {
		return listMetadataFormatsService;
	}

	public void setListMetadataFormatsService(
			ListMetadataFormatsService listMetadataFormatsService) {
		this.listMetadataFormatsService = listMetadataFormatsService;
	}

	public ListRecordsService getListRecordsService() {
		return listRecordsService;
	}

	public void setListRecordsService(ListRecordsService listRecordsService) {
		this.listRecordsService = listRecordsService;
	}
	
	public DeletedInstitutionalItemVersionService getDeletedInstitutionalItemVersionService() {
		return deletedInstitutionalItemVersionService;
	}

	public void setDeletedInstitutionalItemVersionService(
			DeletedInstitutionalItemVersionService deletedInstitutionalItemVersionService) {
		this.deletedInstitutionalItemVersionService = deletedInstitutionalItemVersionService;
	}
}
