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

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.metadata.provider.ListSetsService;

/**
 * Default implementation for handling list sets service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultListSetsService implements ListSetsService {

	/** eclipse generated id */
	private static final long serialVersionUID = 3510610389163807643L;
	
	/** Service to deal with institutional collection information */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/**
	 * Handles returning the correct set spec value
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.ListSetsService#getSetSpec(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public String getSetSpec(InstitutionalCollection collection) {
		 List<InstitutionalCollection> collections = institutionalCollectionService.getPath(collection);
		 
		 StringBuffer setSpecValue = new StringBuffer("");
		 for(InstitutionalCollection c : collections)
		 {
			 if(setSpecValue.length() > 0)
			 {
				setSpecValue.append(":"); 
			 }
		     setSpecValue.append(c.getId()); 
		    
		 }
		 return setSpecValue.toString();
	}
	
	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	/**
	 * List the sets in this repository.
	 * @throws BadResumptionTokenException 
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.ListSetsService#listSets()
	 */
	public String listSets(String resumptionToken) throws BadResumptionTokenException {
		
		 if( resumptionToken != null && !resumptionToken.trim().equals(""))
		 {
			 throw new BadResumptionTokenException("Resumption token is not used " + resumptionToken);
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
		 
		 Document doc = impl.createDocument(null, "ListSets", null);
		 
		 Element root = doc.getDocumentElement();
		 
		 List<InstitutionalCollection> collections = institutionalCollectionService.getAll();
         for(InstitutionalCollection c : collections)
         {
    		 Element set = doc.createElement("set");
    		 root.appendChild(set);
    		 
    		 Element setSpec = doc.createElement("setSpec");
    		 Text data = doc.createTextNode(this.getSetSpec(c));
    		 setSpec.appendChild(data);
    		 
    		 set.appendChild(setSpec);
    		 
    		 
    		 Element setName = doc.createElement("setName");
    		 data = doc.createTextNode(c.getName());
    		 setName.appendChild(data);
    		 
    		 set.appendChild(setName);
         }
         
		 
		 // do not include the xml declaration
		 serializer.getDomConfig().setParameter("xml-declaration", false);
		 
		 serializer.write(root, lsOut);
		 return stringWriter.getBuffer().toString();
	}

	/**
	 * Get the set spec or null if the set spec does not exist.
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.ListSetsService#getSetSpec(java.lang.Long)
	 */
	public String getSetSpec(Long collectionId) {
	    InstitutionalCollection collection = institutionalCollectionService.getCollection(collectionId, false);
        if( collection != null )
        {
        	return getSetSpec(collection);
        }
		return null;
	}
}
