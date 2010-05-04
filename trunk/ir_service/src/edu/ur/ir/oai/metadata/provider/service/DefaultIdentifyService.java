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
import java.util.LinkedList;
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

import edu.ur.ir.institution.InstitutionalItemVersionDAO;
import edu.ur.ir.oai.OaiUtil;
import edu.ur.ir.oai.metadata.provider.IdentifyService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * Helps with the identify part of the oai request.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultIdentifyService implements IdentifyService{
	
	/** Service to deal with repository information. */
	private RepositoryService repositoryService;
	
	/** Data access for institutional item version information */
	private InstitutionalItemVersionDAO institutionalItemVersionDAO;

	/** base url for oai */
	private String baseOaiUrl;
	
	/** Oai protocol version */
	private String protocolVersion = "2.0"; 
	
    /** List of administration emails */
    private List<String> adminEmails = new LinkedList<String>();
    
    /** indicates that delete records are not supported */
    public static final String DELETE_RECORD_NO = "no";
    
    /** indicates that delete records are transient */
    public static final String DELETE_RECORD_TRANSIENT = "transient";
    
    /** indicates that delete records are tracked */
    public static final String DELETE_RECORD_PERSISTENT = "persistent";
    
    /** granularity for the identify */
    private String granularity = "YYYY-MM-DDThh:mm:ssZ";


	/**
	 * Return the identify response as an xml string.
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.IdentifyService#identify()
	 */
	public String identify() {
		
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

		 Document doc = impl.createDocument(null, "Identify", null);
		 
		 Element root = doc.getDocumentElement();
		 
		 addRepositoryName(doc, root);
		 addBaseOaiUrl(doc, root);
		 addAdminEmails(doc, root);
		 addEarliestDatestamp(doc, root);
		 addDeleteRecord(doc, root);
		 addGranularity(doc, root);
		 
		 
		 // do not include the xml declaration
		 serializer.getDomConfig().setParameter("xml-declaration", false);
		 
		 serializer.write(root, lsOut);
		 return stringWriter.getBuffer().toString();
	}
	
	/**
	 * Add the repository name
	 * 
	 * @param doc
	 * @param root
	 */
	private void addRepositoryName(Document doc, Element root)
	{
		 Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		 Element repositoryName = doc.createElement("repositoryName");
		 Text data = doc.createTextNode(repository.getName());
		 repositoryName.appendChild(data);
		 root.appendChild(repositoryName);
	}
	
	/**
	 * Add the base oai url.
	 * 
	 * @param doc
	 * @param root
	 */
	private void addBaseOaiUrl(Document doc, Element root)
	{
		 Element baseUrl = doc.createElement("baseURL");
		 Text data = doc.createTextNode(baseOaiUrl);
		 baseUrl.appendChild(data);
		 root.appendChild(baseUrl);
	}
	
	/**
	 * Add the admin emails 
	 * 
	 * @param doc
	 * @param root
	 */
	private void addAdminEmails(Document doc, Element root)
	{
		for(String email : adminEmails)
		{
		     Element adminEmail = doc.createElement("adminEmail");
		     Text data = doc.createTextNode(email);
		     adminEmail.appendChild(data);
		     root.appendChild(adminEmail);
		}
	}
	
	/**
	 * Add the earliest modification/delete/add time of the records.
	 * 
	 * @param doc
	 * @param root
	 */
	private void addEarliestDatestamp(Document doc, Element root)
	{
		 Element earliestDateStamp = doc.createElement("earliestDatestamp");
		 Date d = institutionalItemVersionDAO.getEarliestDateOfDeposit();
		 if( d == null )
		 {
			 d = new Date();
		 }
	     Text data = doc.createTextNode(OaiUtil.zuluTime(d));
	     earliestDateStamp.appendChild(data);
	     root.appendChild(earliestDateStamp);
	}
	
	/**
	 * Add delete record information.
	 * 
	 * @param doc
	 * @param root
	 */
	private void addDeleteRecord(Document doc, Element root)
	{
		 Element deleteRecord = doc.createElement("deleteRecord");
		 Text data = doc.createTextNode(DELETE_RECORD_PERSISTENT);
		 deleteRecord.appendChild(data);
		 root.appendChild(deleteRecord);	
	}
	
	/**
	 * Add granularity of the system
	 * 
	 * @param doc
	 * @param root
	 */
	private void addGranularity(Document doc, Element root)
	{
		 Element deleteRecord = doc.createElement("granularity");
		 Text data = doc.createTextNode(granularity);
		 deleteRecord.appendChild(data);
		 root.appendChild(deleteRecord);	
	}
	
	
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	public String getBaseOaiUrl() {
		return baseOaiUrl;
	}

	public void setBaseOaiUrl(String baseOaiUrl) {
		this.baseOaiUrl = baseOaiUrl;
	}
	
	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public List<String> getAdminEmails() {
		return adminEmails;
	}

	public void setAdminEmails(List<String> adminEmails) {
		this.adminEmails = adminEmails;
	}
	
	public InstitutionalItemVersionDAO getInstitutionalItemVersionDAO() {
		return institutionalItemVersionDAO;
	}

	public void setInstitutionalItemVersionDAO(
			InstitutionalItemVersionDAO institutionalItemVersionDAO) {
		this.institutionalItemVersionDAO = institutionalItemVersionDAO;
	}
	
	public String getGranularity() {
		return granularity;
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}
}
