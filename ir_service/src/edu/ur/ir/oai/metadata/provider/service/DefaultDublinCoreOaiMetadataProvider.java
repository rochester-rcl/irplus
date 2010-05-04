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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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

import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.CopyrightStatement;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.item.ItemExtent;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemTitle;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.PublishedDate;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMapping;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingService;
import edu.ur.ir.oai.OaiUtil;
import edu.ur.ir.oai.metadata.provider.OaiMetadataProvider;
import edu.ur.ir.person.BasicPersonNameFormatter;
import edu.ur.ir.SimpleDateFormatter;

/**
 * Dublin core implementation of the oai provider.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultDublinCoreOaiMetadataProvider implements OaiMetadataProvider{
	
	/** Prefix handled by this provider */
	public static String METADATA_PREFIX = "oai_dc";
	
	/** Service for dealing with contributor types */
	private ContributorTypeDublinCoreMappingService contributorTypeDublinCoreMappingService;  

	/** Person name formatter */
	private BasicPersonNameFormatter nameFormatter;
	
	/** namespace for the oai url */
	private String namespaceIdentifier;
	
	/** Service to deal with institutional collection information */
	private InstitutionalCollectionService institutionalCollectionService;



	/**
	 * Get the xml output for the item
	 *  
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataProvider#getMetadata(edu.ur.ir.institution.InstitutionalItemVersion)
	 */
	public String getXml(InstitutionalItemVersion institutionalItemVersion) {
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
		 
		 // create the header
         createHeader(doc, institutionalItemVersion);
         createMetadata(doc, institutionalItemVersion);		
			
		 Element root = doc.getDocumentElement();
		 serializer.getDomConfig().setParameter("xml-declaration", false);
		 
		 serializer.write(root, lsOut);
		 return stringWriter.getBuffer().toString();
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataProvider#getMetadataPrefixSupport()
	 */
	public String getMetadataPrefix() {
		return METADATA_PREFIX;
	}

	/**
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataProvider#supportsPrefix(java.lang.String)
	 */
	public boolean supports(String metadataPrefix) {
		return METADATA_PREFIX.equalsIgnoreCase(metadataPrefix);
	}
	
	/**
	 * Create the header for the item.
	 * 
	 * @param doc
	 * @param institutionalItemVersion
	 */
	private void createHeader(Document doc, InstitutionalItemVersion institutionalItemVersion)
	{
		 Element root = doc.getDocumentElement();
		 
		 // create the header element of the record 
		 Element header = doc.createElement("header");
		 root.appendChild(header);
		 
		 // identifier element
		 Element identifier = doc.createElement("identifier");
		 Text data = doc.createTextNode("oai:" + namespaceIdentifier + ":" + institutionalItemVersion.getId().toString());
		 identifier.appendChild(data);
		 header.appendChild(identifier);
		 
		 // datestamp element
		 Element datestamp = doc.createElement("datestamp");
		 Date d = institutionalItemVersion.getDateLastModified();
		 if( d == null )
		 {
			 d = institutionalItemVersion.getDateOfDeposit();
		 }
		 String zuluDateTime = OaiUtil.zuluTime(d);
		 
		 data = doc.createTextNode(zuluDateTime);
		 datestamp.appendChild(data);
		 header.appendChild(datestamp);
		 
		 InstitutionalCollection collection = institutionalItemVersion.getVersionedInstitutionalItem().getInstitutionalItem().getInstitutionalCollection();
		 
		 List<InstitutionalCollection> collections = institutionalCollectionService.getPath(collection);
		 
		 for(InstitutionalCollection c : collections)
		 {
		     Element setSpec = doc.createElement("setSpec");
		     data = doc.createTextNode(c.getId().toString());
		     setSpec.appendChild(data);
		     header.appendChild(setSpec);
		 }
	}
	
	/**
	 * Create the metadata section for oai.
	 * 
	 * @param doc  - xml document root
	 * @param institutionalItemVersion - institutional item version to write
	 */
	private void createMetadata(Document doc, InstitutionalItemVersion institutionalItemVersion)
	{
		 Element root = doc.getDocumentElement();
		 
		 // create the header element of the record 
		 Element metadata = doc.createElement("metadata");
		 root.appendChild(metadata);
		 
		 Element oaiDc = doc.createElement("oai_dc:dc");

		 oaiDc.setAttribute("xmlns:oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/");
		 oaiDc.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		 oaiDc.setAttribute("xmlns:dcterms", "http://purl.org/dc/terms");
		 oaiDc.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		 oaiDc.setAttribute("xsi:schemaLocation", "http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd");

		 metadata.appendChild(oaiDc);
		 
		 GenericItem item = institutionalItemVersion.getItem();
		 addTitle(doc, oaiDc, item);
		 addType(doc, oaiDc, item);
		 addContributors(doc, oaiDc, item);
		 addDescription(doc, oaiDc, item);
		 addAbstract(doc, oaiDc, item);
		 addIdentifiers(doc, oaiDc, item);
		 addLanguage(doc, oaiDc, item);	 
		 addSubjects(doc, oaiDc, item);
		 addPublisher(doc, oaiDc, item);
		 addRights(doc, oaiDc, item);
		 addAvailable(doc, oaiDc, item);
		 addCitation(doc, oaiDc, item);
		 addDateAccepted(doc, oaiDc, institutionalItemVersion);
		 addDateIssued(doc, oaiDc, item);
		 addDateModified(doc, oaiDc, institutionalItemVersion);
		 addExtents(doc, oaiDc, item);
		 addHandle(doc, oaiDc, institutionalItemVersion);
		 
		 
	}
	
	public ContributorTypeDublinCoreMappingService getContributorTypeDublinCoreMappingService() {
		return contributorTypeDublinCoreMappingService;
	}

	public void setContributorTypeDublinCoreMappingService(
			ContributorTypeDublinCoreMappingService contributorTypeDublinCoreMappingService) {
		this.contributorTypeDublinCoreMappingService = contributorTypeDublinCoreMappingService;
	}
	
	public BasicPersonNameFormatter getNameFormatter() {
		return nameFormatter;
	}

	public void setNameFormatter(BasicPersonNameFormatter nameFormatter) {
		this.nameFormatter = nameFormatter;
	}
	
	public String getNamespaceIdentifier() {
		return namespaceIdentifier;
	}

	public void setNamespaceIdentifier(String namespaceIdentifier) {
		this.namespaceIdentifier = namespaceIdentifier;
	}
	
	/**
	 * Add the title and alternative title information
	 * 
	 * @param doc
	 * @param oaiDc
	 */
	private void addTitle(Document doc, Element oaiDc, GenericItem item)
	{
		 Element title = doc.createElement("dc:title");
		 Text data = doc.createTextNode(item.getFullName());
		 title.appendChild(data);
		 oaiDc.appendChild(title);
		 	 
		 for(ItemTitle subTitle : item.getSubTitles())
		 {
			 Element alternative = doc.createElement("dcterms:alternative");
			 data = doc.createTextNode(subTitle.getFullTitle());
			 alternative.appendChild(data);
			 oaiDc.appendChild(alternative);
		 }	 
	}
	
	/**
	 * Add the type information
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addType(Document doc, Element oaiDc, GenericItem item)
	{
		 Element type = doc.createElement("dc:type");
		 Text data = doc.createTextNode(item.getPrimaryContentType().getName());
		 type.appendChild(data);
		 oaiDc.appendChild(type);
		 
		 for(ContentType secType : item.getSecondaryContentTypes())
		 {
			 Element secondaryType = doc.createElement("dc:type");
			 data = doc.createTextNode(secType.getName());
			 secondaryType.appendChild(data);
			 oaiDc.appendChild(secondaryType);
		 }		 
	}
	
	/**
	 * Add contributor information.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addContributors(Document doc, Element oaiDc, GenericItem item)
	{
		 for(ItemContributor itemContributor : item.getContributors())
		 {
			 if(itemContributor.getContributor() == null )
			 {
				 throw new IllegalStateException("contributor null");
			 }
			 if( itemContributor.getContributor().getContributorType() == null )
			 {
				 throw new IllegalStateException("contributor Type null");
			 }
			
			 
			 ContributorTypeDublinCoreMapping dcMapping = contributorTypeDublinCoreMappingService.get(itemContributor.getContributor().getContributorType().getId());
			 Element creator = null;
			 if( dcMapping != null )
			 {
				 if( dcMapping.getDublinCoreTerm().getIsSimpleDublinCoreElement())
				 {
			         creator = doc.createElement("dc:" + dcMapping.getDublinCoreTerm().getName());
				 }
				 else
				 {
					 creator = doc.createElement("dcterms:" + dcMapping.getDublinCoreTerm().getName());
				 }
		 
			 }
			 else
			 {
				 creator  = doc.createElement("dc:creator");
			 }
			 Text data = doc.createTextNode( nameFormatter.getNameFormatted(itemContributor.getContributor().getPersonName(), true) );
			 creator.appendChild(data);
			 oaiDc.appendChild(creator);
		 }
	}
	
	/**
	 * Add the description information 
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addDescription(Document doc, Element oaiDc, GenericItem item)
	{
		 if( item.getDescription() != null && !item.getDescription().equalsIgnoreCase(""))
		 {
			 Element description = doc.createElement("dc:description");
		     Text data = doc.createTextNode(item.getDescription());
		     description.appendChild(data);
		     oaiDc.appendChild(description);
		 }
	}

	/**
	 * Add the abstract information.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addAbstract(Document doc, Element oaiDc, GenericItem item)
	{
		 if( item.getItemAbstract() != null && !item.getItemAbstract().equalsIgnoreCase(""))
		 {
			 Element itemAbstract = doc.createElement("dcterms:abstract");
		     Text data = doc.createTextNode(item.getItemAbstract());
		     itemAbstract.appendChild(data);
		     oaiDc.appendChild(itemAbstract);
		 }
	}
	
	/**
	 * Add identifier information.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addIdentifiers(Document doc, Element oaiDc, GenericItem item)
	{
		for(ItemIdentifier itemIdentifier : item.getItemIdentifiers())
		{
			Element identifier = doc.createElement("dc:identifier");
			Text data = doc.createTextNode(itemIdentifier.getValue());
			identifier.appendChild(data);
			oaiDc.appendChild(identifier);
		}
	}
	
	/**
	 * Add the language type 
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addLanguage(Document doc, Element oaiDc, GenericItem item)
	{
	    LanguageType languageType = item.getLanguageType();
	    if( languageType != null )
	    {
	        if( languageType.getIso639_2() != null && !languageType.getIso639_2().equals(""))
	        {
	            Element language = doc.createElement("dc:language");
	            Text data = doc.createTextNode(languageType.getIso639_2());
	            language.appendChild(data);
	            oaiDc.appendChild(language);
	        }
	        else if( languageType.getIso639_1() != null && !languageType.getIso639_1().equals(""))
	        {
	            Element language = doc.createElement("dc:language");
	            Text data = doc.createTextNode(languageType.getIso639_1());
	            language.appendChild(data);
	            oaiDc.appendChild(language);
	        }
	        else if( languageType.getName()!= null && !languageType.getName().equals(""))
	        {
	            Element language = doc.createElement("dc:language");
	            Text data = doc.createTextNode(languageType.getName());
	            language.appendChild(data);
	            oaiDc.appendChild(language);
	        }
	    }
	}
	
	/**
	 * Add subjects
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addSubjects(Document doc, Element oaiDc, GenericItem item)
	{
	    String itemSubjects = item.getItemKeywords();
	    StringTokenizer tokenizer = new StringTokenizer(itemSubjects, ";");
	    while( tokenizer.hasMoreElements())
	    {
	    	String value = tokenizer.nextToken();
	    	if( value != null && !value.equals(""))
	    	{
	    	    Element subject = doc.createElement("dc:subject");
	    	    Text data = doc.createTextNode(value);
	    	    subject.appendChild(data);
	    	    oaiDc.appendChild(subject);
	    	}
	    }
		
	}
	
	/**
	 * Add publisher to the document.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addPublisher(Document doc, Element oaiDc, GenericItem item)
	{
	    ExternalPublishedItem externalPublishedItem = item.getExternalPublishedItem();
	    if( externalPublishedItem != null )
	    {
	    	Publisher pub = externalPublishedItem.getPublisher();
	    	if( pub != null )
	    	{
	    		 Element publisher = doc.createElement("dc:publisher");
		    	 Text data = doc.createTextNode(pub.getName());
		    	 publisher.appendChild(data);
		    	 oaiDc.appendChild(publisher);
	    	}
	    }
	}
	
	/**
	 * Add rights statement.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addRights(Document doc, Element oaiDc, GenericItem item)
	{
		CopyrightStatement copyrightStatement = item.getCopyrightStatement();
		if( copyrightStatement != null )
		{
			Element rightsElement = doc.createElement("dc:rights");
			Text data = doc.createTextNode(copyrightStatement.getText());
	    	rightsElement.appendChild(data);
	    	oaiDc.appendChild(rightsElement);
		}
	}
	
	/**
	 * Add the date this publication was made available.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addAvailable(Document doc, Element oaiDc, GenericItem item)
	{
		if( item.getReleaseDate() != null )
		{
		    Date d = item.getReleaseDate();
		    Element availableElement = doc.createElement("dcterms:available");
		    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
			Text data = doc.createTextNode(dateFormat.format(d));
	    	availableElement.appendChild(data);
	    	oaiDc.appendChild(availableElement);
		}
	}
	
	/**
	 * Add the citation
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addCitation(Document doc, Element oaiDc, GenericItem item)
	{
		ExternalPublishedItem externalPublishedItem = item.getExternalPublishedItem();
		if( externalPublishedItem != null )
		{
			if( externalPublishedItem.getCitation() != null )
			{
		        Element citationElement = doc.createElement("dcterms:bibliographicCitation");
			    Text data = doc.createTextNode(externalPublishedItem.getCitation());
			    citationElement.appendChild(data);
	    	    oaiDc.appendChild(citationElement);
			}
		}
	}
	
	/**
	 * Add the date accepted
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addDateAccepted(Document doc, Element oaiDc, InstitutionalItemVersion item)
	{
		if( item.getDateOfDeposit() != null )
		{
		    Date d = item.getDateOfDeposit();
		    Element acceptedElement = doc.createElement("dcterms:dateAccepted");
		    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
			Text data = doc.createTextNode(dateFormat.format(d));
	    	acceptedElement.appendChild(data);
	    	oaiDc.appendChild(acceptedElement);
		}
	}
	
	/**
	 * Add the date issued
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addDateIssued(Document doc, Element oaiDc, GenericItem item)
	{
		ExternalPublishedItem externalPublishedItem = item.getExternalPublishedItem();
		if( externalPublishedItem != null )
		{
			PublishedDate publishedDate = externalPublishedItem.getPublishedDate();
			if( publishedDate != null )
			{
		        Element citationElement = doc.createElement("dcterms:issued");
		        SimpleDateFormatter sdf = new SimpleDateFormatter();
		        Text data = doc.createTextNode(sdf.getDate(publishedDate));
		        citationElement.appendChild(data);
    	        oaiDc.appendChild(citationElement);
			}
		}
	}
	
	/**
	 * Add the date modified
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addDateModified(Document doc, Element oaiDc, InstitutionalItemVersion item)
	{
		if( item.getDateLastModified() != null )
		{
		    Date d = item.getDateLastModified();
		    Element modifiedElement = doc.createElement("dcterms:modified");
		    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
			Text data = doc.createTextNode(dateFormat.format(d));
	    	modifiedElement.appendChild(data);
	    	oaiDc.appendChild(modifiedElement);
		}
	}
	
	/**
	 * Add the extents
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addExtents(Document doc, Element oaiDc, GenericItem item)
	{
		for( ItemExtent ie : item.getItemExtents())
		{
			Element extentElement = doc.createElement("dcterms:extent");
		    Text data = doc.createTextNode(ie.getExtentType().getName() + ":" + ie.getValue());
		    extentElement.appendChild(data);
    	    oaiDc.appendChild(extentElement);
		}
	}
	
	/**
	 * Add handle url.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addHandle(Document doc, Element oaiDc, InstitutionalItemVersion item)
	{
		if( item.getHandleInfo() != null )
		{
			HandleInfo handle = item.getHandleInfo();
		    Element identifier = doc.createElement("dc:identifier");
		    Text data = doc.createTextNode(handle.getNameAuthority().getAuthorityBaseUrl() + handle.getNameAuthority().getNamingAuthority() + "/" + handle.getLocalName());
		    identifier.appendChild(data);
		    oaiDc.appendChild(identifier);
		}
	}

	
	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

}
