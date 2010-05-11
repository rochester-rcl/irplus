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
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.oai.OaiUtil;
import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.exception.CannotDisseminateFormatException;
import edu.ur.ir.oai.exception.NoRecordsMatchException;
import edu.ur.ir.oai.metadata.provider.ListIdentifiersService;
import edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider;
import edu.ur.ir.oai.metadata.provider.ResumptionToken;

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
	private int batchSize = 500;
	
	/**  List of oai metadata service providers */
	private OaiMetadataServiceProvider oaiMetadataServiceProvider;
	
	/** Service for dealing with institutional item information */
	private InstitutionalItemVersionService institutionalItemVersionService;
	
	/** namespace for the oai url */
	private String namespaceIdentifier;
	
	/** Service to deal with institutional collection information */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** resumption token */
	private DefaultResumptionToken resumptionToken;

	/**
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.ListIdentifiersService#listIdentifiers(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String listIdentifiers(String metadataPrefix, String set,
			String from, String until, String strResumptionToken)
			throws BadResumptionTokenException,
			CannotDisseminateFormatException, NoRecordsMatchException{
		
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
		 
		 Document doc = impl.createDocument(null, "ListIdentifiers", null);

		
		if( !oaiMetadataServiceProvider.supports(metadataPrefix) )
		{
			throw new CannotDisseminateFormatException("Format: " + metadataPrefix + " is not supported");
		}

		// parse the token if it exists
		if(strResumptionToken != null && !strResumptionToken.equals(""))
		{
			resumptionToken = new DefaultResumptionToken(strResumptionToken);
		}
		else
		{
			resumptionToken = new DefaultResumptionToken();
			
			resumptionToken.setBatchSize(batchSize);
			if( from != null && !from.equals(""))
			{
				resumptionToken.setFrom(from);
			}
			if( set != null && !set.equals(""))
			{
				resumptionToken.setSet(set);
			}
			if( until != null && !until.equals(""))
			{
				resumptionToken.setUntil(until);
			}
			resumptionToken.setMetadataPrefix(metadataPrefix);
			resumptionToken.setLastId(0l);
		}

		boolean doDeleted = resumptionToken.getDeleted();
		if(!doDeleted)
		{
		    // do batch size plus one - if we retrieve all records then another request must be issued
		    // with resumption token
		    List<InstitutionalItemVersion> versions = institutionalItemVersionService.getItemsIdOrder(resumptionToken.getLastId(), batchSize + 1);
		    int size = versions.size();
		   
		    // we will need to send resumption token
		    if( size == (batchSize + 1))
		    {
			    // remove the last item as it should not be sent
			    // this only indicates that there is one more than the batch size
			    // allows
			    versions.remove(size - 1);
			    resumptionToken.setDeleted(Boolean.FALSE);
		    }
		    else if( size > 0)
		    {
		    	resumptionToken.setDeleted(Boolean.TRUE);
		    }
		    else
		    {
		    	resumptionToken.setDeleted(Boolean.TRUE);
		    	doDeleted = true;
		    }
		    Long lastId = addIdentifiers(versions, doc);
		    resumptionToken.setLastId(lastId);
		}
		
		if( doDeleted )
		{
			// handle getting all the deleted institutional item versions
		}
		 
		 addResumptionToken(doc);
		 
		 // do not output the headers
		 Element root = doc.getDocumentElement();
		 serializer.getDomConfig().setParameter("xml-declaration", false);
		 serializer.write(root, lsOut);
		 return stringWriter.getBuffer().toString();
	}
	
	/**
	 * A request to list identifiers with only the metadata prefix.
	 * 
	 * @param metadataPrefix
	 * @return
	 */
	private Long addIdentifiers(List<InstitutionalItemVersion> versions, Document doc )
	{
		 Long lastId = -1l;
		 Element root = doc.getDocumentElement();		 
		 for(InstitutionalItemVersion version : versions)
		 {
			 lastId = version.getId();
			 
			 // create the header element of the record 
			 Element header = doc.createElement("header");
			 root.appendChild(header);
			 
			 // identifier element
			 Element identifier = doc.createElement("identifier");
			 Text data = doc.createTextNode("oai:" + namespaceIdentifier + ":" + version.getId().toString());
			 identifier.appendChild(data);
			 header.appendChild(identifier);
			 
			 // datestamp element
			 Element datestamp = doc.createElement("datestamp");
			 Date d = version.getDateLastModified();
			 if( d == null )
			 {
				 d = version.getDateOfDeposit();
			 }
			 String zuluDateTime = OaiUtil.zuluTime(d);
			 
			 data = doc.createTextNode(zuluDateTime);
			 datestamp.appendChild(data);
			 header.appendChild(datestamp);
			 
			 InstitutionalCollection collection = version.getVersionedInstitutionalItem().getInstitutionalItem().getInstitutionalCollection();
			 
			 List<InstitutionalCollection> collections = institutionalCollectionService.getPath(collection);
			 
			 for(InstitutionalCollection c : collections)
			 {
			     Element setSpec = doc.createElement("setSpec");
			     data = doc.createTextNode(c.getId().toString());
			     setSpec.appendChild(data);
			     header.appendChild(setSpec);
			 }
		 }
		 return lastId;
	}
	
	private void addResumptionToken(Document doc )
	{
		 Element root = doc.getDocumentElement();	
		// create the header element of the record 
		 Element resumption = doc.createElement("resumptionToken");
		 Text data = doc.createTextNode(resumptionToken.getAsTokenString());
		 resumption.appendChild(data);
		 root.appendChild(resumption);
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
	
	public String getNamespaceIdentifier() {
		return namespaceIdentifier;
	}

	public void setNamespaceIdentifier(String namespaceIdentifier) {
		this.namespaceIdentifier = namespaceIdentifier;
	}

	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}
	
	public ResumptionToken getResumptionToken() {
		return resumptionToken;
	}

}
