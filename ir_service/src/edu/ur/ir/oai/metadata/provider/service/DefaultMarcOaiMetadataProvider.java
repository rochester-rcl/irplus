package edu.ur.ir.oai.metadata.provider.service;

import java.util.Date;

import org.marc4j.marc.Record;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.ur.ir.export.MarcExportService;
import edu.ur.ir.institution.DeletedInstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.oai.OaiUtil;
import edu.ur.ir.oai.metadata.provider.ListSetsService;
import edu.ur.ir.oai.metadata.provider.OaiMetadataProvider;
import edu.ur.ir.person.BasicPersonNameFormatter;

public class DefaultMarcOaiMetadataProvider implements OaiMetadataProvider{

	/** eclipse generated id  */
	private static final long serialVersionUID = -8227691687049064056L;

	/** Prefix handled by this provider */
	public static String METADATA_PREFIX = "marc21";
	
	public static String METADATA_NAMESPACE = "http://urresearch.rochester.edu/OAI/2.0/marc21/";
	
	public static String SCHEMA = "http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd";

	MarcXmlElementAppender marcXmlAppender = new MarcXmlElementAppender();

	/** Person name formatter */
	private BasicPersonNameFormatter nameFormatter;
	
	/** namespace for the oai url */
	private String namespaceIdentifier;
	
	/** service to deal with listing set information */
	private ListSetsService listSetsService;
	
	// the marc export service
	private MarcExportService marcExportService;


	/**
	 * Get the xml output for the item
	 *  
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataProvider#getMetadata(edu.ur.ir.institution.InstitutionalItemVersion)
	 */
	public void addXml(Element record, InstitutionalItemVersion institutionalItemVersion) {

		 Document doc = record.getOwnerDocument();
		 // create the header
         createHeader(doc, record, institutionalItemVersion);
         
         boolean showAllFields = false;
         if( institutionalItemVersion.getItem().isPubliclyViewable() && !institutionalItemVersion.getItem().isEmbargoed() && !institutionalItemVersion.isWithdrawn() )
         {
        	 showAllFields = true;
         }
         Element metadataTag = doc.createElement("metadata");
         record.appendChild(metadataTag);
         Record marcRecord = marcExportService.export(institutionalItemVersion, showAllFields);
         marcXmlAppender.addToDocument(marcRecord, doc, metadataTag);
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
	private void createHeader(Document doc, Element record, InstitutionalItemVersion institutionalItemVersion)
	{
		 // create the header element of the record 
		 Element header = doc.createElement("header");
		 record.appendChild(header);
		 
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
		 String zuluDateTime = OaiUtil.getZuluTime(d);
		 
		 data = doc.createTextNode(zuluDateTime);
		 datestamp.appendChild(data);
		 header.appendChild(datestamp);
		 
		 InstitutionalCollection collection = institutionalItemVersion.getVersionedInstitutionalItem().getInstitutionalItem().getInstitutionalCollection();
		 Element setSpec = doc.createElement("setSpec");
 		 data = doc.createTextNode(listSetsService.getSetSpec(collection));
	     setSpec.appendChild(data);
	     header.appendChild(setSpec);

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
	
	
	public ListSetsService getListSetsService() {
		return listSetsService;
	}

	public void setListSetsService(ListSetsService listSetsService) {
		this.listSetsService = listSetsService;
	}

	/**
	 * Get the namespace for the provider
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataProvider#getNamespace()
	 */
	public String getMetadataNamespace() {
		return METADATA_NAMESPACE;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataProvider#getSchema()
	 */
	public String getSchema() {
		return SCHEMA;
	}
	
	public void addXml(Element record, DeletedInstitutionalItemVersion institutionalItemVersion)
	{
		 Document doc = record.getOwnerDocument();
		 // create the header
         createHeader(doc, record, institutionalItemVersion);
	}
	
	/**
	 * Create the header for the deleted item.
	 * 
	 * @param doc
	 * @param deletedInstitutionalItemVersion
	 */
	private void createHeader(Document doc, Element record, DeletedInstitutionalItemVersion institutionalItemVersion)
	{
		 // create the header element of the record 
		 Element header = doc.createElement("header");
		 record.appendChild(header);
		 
		 // identifier element
		 Element identifier = doc.createElement("identifier");
		 header.setAttribute("status", "deleted");
		 Text data = doc.createTextNode("oai:" + namespaceIdentifier + ":" + institutionalItemVersion.getInstitutionalItemVersionId().toString());
		 identifier.appendChild(data);
		 header.appendChild(identifier);
		 
		 // datestamp element
		 Element datestamp = doc.createElement("datestamp");
		 Date d = institutionalItemVersion.getDeletedInstitutionalItem().getDeletedDate();
		 String zuluDateTime = OaiUtil.getZuluTime(d);
		 
		 data = doc.createTextNode(zuluDateTime);
		 datestamp.appendChild(data);
		 header.appendChild(datestamp);
		 
		 Long collectionId = institutionalItemVersion.getDeletedInstitutionalItem().getInstitutionalCollectionId();
		 if( collectionId != null )
		 {
		     String setSpecStr = listSetsService.getSetSpec(collectionId);
		     if( setSpecStr != null )
		     {
		         Element setSpec = doc.createElement("setSpec");
		         data = doc.createTextNode(setSpecStr);
		         setSpec.appendChild(data);
		         header.appendChild(setSpec);
		     }
		 }

	}
	
	/**
	 * Set the marc export service.
	 * 
	 * @param marcExportService
	 */
	public void setMarcExportService(MarcExportService marcExportService) {
		this.marcExportService = marcExportService;
	}

	

}
