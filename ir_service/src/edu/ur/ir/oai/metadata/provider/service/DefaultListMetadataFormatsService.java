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


import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.oai.exception.BadArgumentException;
import edu.ur.ir.oai.exception.IdDoesNotExistException;
import edu.ur.ir.oai.exception.NoMetadataFormatsException;
import edu.ur.ir.oai.metadata.provider.ListMetadataFormatsService;
import edu.ur.ir.oai.metadata.provider.OaiMetadataProvider;
import edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider;

/**
 * Default implementation of the list metadata formats service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultListMetadataFormatsService implements ListMetadataFormatsService{

	
	/** eclipse generated id */
	private static final long serialVersionUID = -4395383131075248090L;

	/**  Provider for record metadata */
	private OaiMetadataServiceProvider oaiMetadataServiceProvider;

	/** Service to deal with institutional item information */
	private InstitutionalItemVersionService institutionalItemVersionService;
	

	/**
	 * List the metadata formats.
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.ListMetadataFormatsService#listMetadataFormats(java.lang.String)
	 */
	public String listMetadataFormats(String identifier)
			throws BadArgumentException, IdDoesNotExistException,
			NoMetadataFormatsException {
		
		 // we only need to determine if the identifier exists
		 // format output is independent of existence
		 if(identifier!= null && !identifier.trim().equals("") )
		 {
			 Long institutionalItemVersionId = DefaultOaiIdentifierHelper.getInstitutionalItemVersionId(identifier);
			 InstitutionalItemVersion institutionalItemVersion = institutionalItemVersionService.getInstitutionalItemVersion(institutionalItemVersionId, false);
		     if( institutionalItemVersion == null )
		     {
		    	 throw new IdDoesNotExistException("identifier " + identifier + " does not exist");
		     }
		 }
		
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
		 
		 Document doc = impl.createDocument(null, "ListMetadataFormats", null);
		 
		 Element root = doc.getDocumentElement();
		 
		 
		 List<OaiMetadataProvider> providers = oaiMetadataServiceProvider.getProviders();
		 for( OaiMetadataProvider p : providers)
		 {
			 Element metadataFormat = doc.createElement("metadataFormat");
    		 root.appendChild(metadataFormat);
    		 
    		 Element metadataPrefix = doc.createElement("metadataPrefix");
    		 Text data = doc.createTextNode(p.getMetadataPrefix());
    		 metadataPrefix.appendChild(data);
    		 
    		 metadataFormat.appendChild(metadataPrefix);
    		 
    		 
    		 Element schema = doc.createElement("schema");
    		 data = doc.createTextNode(p.getSchema());
    		 schema.appendChild(data);
    		 
    		 metadataFormat.appendChild(schema);
    		 
    		 Element metadataNamespace = doc.createElement("metadataNamespace");
    		 data = doc.createTextNode(p.getMetadataNamespace());
    		 metadataNamespace.appendChild(data);
    		 
    		 metadataFormat.appendChild(metadataNamespace);
		 }
		 
		 
		 
		 // do not include the xml declaration
		 serializer.getDomConfig().setParameter("xml-declaration", false);
		 
		 serializer.write(root, lsOut);
		 return stringWriter.getBuffer().toString();
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
