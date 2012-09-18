/**  
   Copyright 2008-2011 University of Rochester

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


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.MarcFactory;
import org.marc4j.marc.Record;

import edu.ur.ir.export.MarcExportService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.service.InstitutionalItemVersionUrlGenerator;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeService;
import edu.ur.ir.item.CopyrightStatement;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemContentType;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.item.ItemExtent;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemReport;
import edu.ur.ir.item.ItemTitle;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.PublishedDate;
import edu.ur.ir.item.metadata.marc.ExtentTypeSubFieldMapper;
import edu.ur.ir.item.metadata.marc.ExtentTypeSubFieldMapperService;
import edu.ur.ir.item.metadata.marc.IdentifierTypeSubFieldMapper;
import edu.ur.ir.item.metadata.marc.IdentifierTypeSubFieldMapperService;
import edu.ur.ir.item.metadata.marc.MarcContentTypeFieldMapper;
import edu.ur.ir.item.metadata.marc.MarcContentTypeFieldMapperService;
import edu.ur.ir.item.metadata.marc.MarcContributorTypeRelatorCode;
import edu.ur.ir.item.metadata.marc.MarcContributorTypeRelatorCodeService;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.PersonName;
import edu.ur.metadata.marc.MarcDataField;

/**
 * Export service for marc.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultMarcExportService implements MarcExportService, Comparator<ExtentTypeSubFieldMapper>{

	/** eclipse generated export */
	private static final long serialVersionUID = 1486364189520804900L;
	
	// create a factory instance
	private MarcFactory factory = MarcFactory.newInstance();
	
	// default date form
	private DateFormat dateFormat = new SimpleDateFormat("yyyy");
	
	//  Service for dealing with mapping between content type and marc fields */
	private MarcContentTypeFieldMapperService marcContentTypeFieldMapperService;
	
	// service to deal with content types
	private ContentTypeService contentTypeService;

	// service to help with relator codes
	private MarcContributorTypeRelatorCodeService marcContributorTypeRelatorCodeService;
	
	// service for dealing with identifier type sub fields
	private IdentifierTypeSubFieldMapperService identifierTypeSubFieldMapperService;
	
	/**  Used to get the url for a given item */
	private InstitutionalItemVersionUrlGenerator institutionalItemVersionUrlGenerator;
	
	// service for dealing with identifier type sub fields
	private ExtentTypeSubFieldMapperService extentTypeSubFieldMapperService;
	

	/**
	 * Export the institutional item version.
	 * 
	 * (non-Javadoc)
	 * @see edu.ur.ir.export.MarcExportService#export(edu.ur.ir.institution.InstitutionalItemVersion, boolean)
	 */
	@SuppressWarnings("unchecked")
	public Record export(InstitutionalItemVersion version, boolean showAllFields) {
        Record record = factory.newRecord();
        GenericItem item = version.getItem();
        ExternalPublishedItem externalPublishedItem = item.getExternalPublishedItem();
        PublishedDate publishedDate = null;
        PlaceOfPublication placeOfPublication = null;
        
        if( externalPublishedItem != null )
        {
             if( externalPublishedItem.getPublishedDate() != null )
             {
        	    publishedDate = externalPublishedItem.getPublishedDate();
             }
             
             if(externalPublishedItem.getPlaceOfPublication() != null )
             {
            	 placeOfPublication = externalPublishedItem.getPlaceOfPublication();
             }
             
             if( externalPublishedItem.getCitation() != null )
             {
            	 handleCitation(record, externalPublishedItem.getCitation());
             }
        }
        
        String year = "";
        
        if( publishedDate != null && publishedDate.getYear() > 0)
        {
        	year = publishedDate.getYear() + "";
        }
        else if( version.getDateOfDeposit() != null)
		{
			year = dateFormat.format(version.getDateOfDeposit());
		}
        
        ContentType contentType = null;
        if( item.getPrimaryItemContentType() != null  )
        {
            contentType = item.getPrimaryItemContentType().getContentType();
        }
        MarcContentTypeFieldMapper mapper = null;
        if( contentType != null )
        {
             mapper = marcContentTypeFieldMapperService.getByContentTypeId(contentType.getId());
        }
        
        // try to default to book
        if( mapper == null )
        {
       	    contentType = contentTypeService.getByUniqueSystemCode("BOOK");
       	    if( contentType != null )
       	    {
       		    mapper = marcContentTypeFieldMapperService.getByContentTypeId(contentType.getId());
       	    }
        }
        
        
        if( mapper != null )
        {
        	// add leader fields
            handleLeader(record, mapper);
            
            // add 006 fields
            ControlField control_006 = factory.newControlField("006", mapper.getControlField006());
    		record.getControlFields().add(control_006);
    		
    		// add 007 fields
    		ControlField control_007 = factory.newControlField("007", new String(mapper.getControlField007()));
    		record.getControlFields().add(control_007);
    		
    		handle008Field(record, mapper, year, item.getLanguageType(), placeOfPublication);
        }
		

		
		handleContributors(item, record);
		ItemContributor ic = this.getMainAuthor(item);
		handleTitle(item, ic, record);
		
		if( showAllFields )
		{
			if( externalPublishedItem != null )
			{
		        handlePublisher(record, externalPublishedItem, year);
			}
		    handleExtents(item, record);
		    if( item.getDescription() != null && !item.getDescription().trim().equals(""))
		    {
		        handleDescription(record, item.getDescription(), mapper);
		    }
		    if( item.isEmbargoed() )
		    {
			    this.handleEmbargo(record, item.getReleaseDate());
		    }
		    if( item.getItemAbstract() != null && !item.getItemAbstract().trim().equals(""))
		    {
			    handleAbstract(record, item.getItemAbstract());
		    }
		    handleFileType(record, item);
		    handleIdentifiers(record, item.getItemIdentifiers());
		    if( item.getItemKeywords() != null && !item.getItemKeywords().trim().equals(""))
		    {
		        handleKeywords(record, item.getItemKeywords());
		    }
		    handleThesisType(record, item);
		    String url = null;
		    if( version.getHandleInfo() != null )
		    {
				url = version.getHandleInfo().getNameAuthority().getAuthorityBaseUrl() + 
						version.getHandleInfo().getNameAuthority().getNamingAuthority() + "/" + 
						version.getHandleInfo().getLocalName();
		    }
		    else
		    {
		        url = institutionalItemVersionUrlGenerator.createUrl(version.getVersionedInstitutionalItem().getInstitutionalItem(), version.getVersionNumber());
		    }
		    if( url != null )
		    {
		        handleUrl(record,url);
		    }
		    
		    handleSeries(record, item.getItemReports());
		    handleCopyright(record, item.getCopyrightStatement());
		    handleOtherTitles(record, item);
		}
		return record;
	}
	
	// we will have to create a mapping for this to 
	// work properly
	private void handleExtents(GenericItem item, Record record)
	{
        Set<ItemExtent> extents = item.getItemExtents();
        
        // has b subfield
        boolean hasB = false;
        
        // has c subfield
        boolean hasC = false;
        
        List<ExtentTypeSubFieldMapper> mappers = new LinkedList<ExtentTypeSubFieldMapper>();
        
        // get all mappers
        for(ItemExtent extent : extents)
        {
        	mappers.addAll(extentTypeSubFieldMapperService.getByExtentTypeId(extent.getExtentType().getId()));
        }
        
        // sort the list according to marc sub field name
        // this is to make sure exported fields are in order - $a, $b, $c
        Collections.sort(mappers, this);
        
        // determine if we have a $b and or $c subfield
        // this will affect punctuation
 	    for(ExtentTypeSubFieldMapper mapper : mappers)
		{
		    if( mapper.getMarcSubField().getName().equalsIgnoreCase("b") )
			{
			    hasB = true;
			}
			else if( mapper.getMarcSubField().getName().equalsIgnoreCase("c") )
			{
				hasC = true;
			}
		}
		
 	    
		// match the extent type mappers up with the extents they belong to
		for(ExtentTypeSubFieldMapper mapper : mappers)
		{
			// take all the item extents 
			for(ItemExtent extent : extents)
			{
				if( mapper.getExtentType().getId() == extent.getExtentType().getId())
				{
			        //get the datafield 
				    MarcDataField marcDataField = mapper.getMarcDataFieldMapper().getMarcDataField();
					
					DataField df = (DataField)record.getVariableField(marcDataField.getCode());
					
					String value = extent.getValue();
					
					if(mapper.getMarcSubField().getName().equalsIgnoreCase("a") && hasB)
					{
						value = value + " : ";
					}
					
					if(mapper.getMarcSubField().getName().equalsIgnoreCase("a") && !hasB && hasC)
					{
						value = value + " ; ";
					}
					
					if(mapper.getMarcSubField().getName().equalsIgnoreCase("a") && !(hasB || hasC) )
					{
						value = value + ".";
					}
					
					
					if( df == null)
					{
						df = factory.newDataField(marcDataField.getCode(), 
								mapper.getMarcDataFieldMapper().getIndicator1AsChar(),
								mapper.getMarcDataFieldMapper().getIndicator2AsChar());
						
						
						
						df.addSubfield(factory.newSubfield(mapper.getMarcSubField().getName().charAt(0), removeInvalidXmlChars(value)));
						record.addVariableField(df);
					}
					else
					{
						df.addSubfield(factory.newSubfield(mapper.getMarcSubField().getName().charAt(0), removeInvalidXmlChars(value)));
					}
				}
			}
		}
		
	}
	
	/**
	 * Gets the main author of the item.
	 * 
	 * @param item
	 * @return
	 */
	private ItemContributor getMainAuthor(GenericItem item)
	{
		ItemContributor ic = null;
		if( item.getContributors().size() >= 1 )
		{
			ic = item.getContributor(0);
		}
		return ic;
	}
	
	// we should use publisher information as stored in publisher name.
	private void handlePublisher(Record record,ExternalPublishedItem externalPublishedItem, String year)
	{
         DataField df = factory.newDataField("260", ' ', ' ');
         if( externalPublishedItem.getPlaceOfPublication() != null )
         {
        	 String value = externalPublishedItem.getPlaceOfPublication().getName();
        	 if(externalPublishedItem.getPublisher() != null)
        	 {
        		 value = value + " :";
        	 }
		     df.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(value)));
         }
         if( externalPublishedItem.getPublisher() != null )
         {
        	 String name = externalPublishedItem.getPublisher().getName();
        	 if( year != null )
        	 {
        		 name = name + ", ";
        	 }
		     df.addSubfield(factory.newSubfield('b', removeInvalidXmlChars(name)));
         }
         if( year != null )
         {
		     df.addSubfield(factory.newSubfield('c',  year + "."));
         }
		 record.addVariableField(df);
	}
	
	/**
	 * Sets other titles to the 246 field.
	 * 
	 * @param record
	 * @param genericItem
	 */
	private void handleOtherTitles(Record record, GenericItem genericItem)
	{
		for(ItemTitle title : genericItem.getSubTitles())
		{
			DataField df = factory.newDataField("246", '3', ' ');
			df.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(title.getFullTitle())));
			record.addVariableField(df);
		}
	}
	
	/**
	 * Handles the main title.
	 * 
	 * @param item - item to get th the title from
	 * @param ic - the main contributor
	 * @param record - record to add the data to.
	 * 
	 */
	private void handleTitle(GenericItem item, ItemContributor ic, Record record)
	{
		char ind2 = '0';
		String leadingArticles = "";
		if( item.getLeadingNameArticles() != null && 
				item.getLeadingNameArticles().trim().length() > 0 &&
				item.getLeadingNameArticles().trim().length() <= 8)
		{
			ind2 = ( (item.getLeadingNameArticles().length() + 1) + "").charAt(0);
			leadingArticles = item.getLeadingNameArticles() + " ";
		}
		
		DataField df = factory.newDataField("245", '1', ind2);
		df.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(leadingArticles + item.getName())));
		
		if( ic != null )
		{
		    df.addSubfield(factory.newSubfield('h', "[electronic resource] /"));
		}
		else
		{
			df.addSubfield(factory.newSubfield('h', "[electronic resource]"));
		}
		
		if( ic != null )
		{
		    String authorName = "";
		    PersonName pn = ic.getContributor().getPersonName();
		    if( pn.getForename() != null )
		    {
			    authorName += pn.getForename();
		    }
		    if( pn.getMiddleName() != null )
		    {
			    authorName += " " + pn.getMiddleName();
		    }
		    if( pn.getSurname() != null )
		    {
			     authorName += " " + pn.getSurname();
		    }
		    df.addSubfield(factory.newSubfield('c', removeInvalidXmlChars(authorName) + ".") );
			record.addVariableField(df);
		}
	
		
	}

	/**
	 * Handle the contributors.
	 * 
	 * @param item - items to extract the contributors from
	 * @param record - record to add the data to
	 */
	private void handleContributors(GenericItem item, Record record)
	{
		List<ItemContributor> contributors = item.getContributors();
		
		for( int index = 0; index < contributors.size(); index++)
		{
			ItemContributor contributor = contributors.get(index);
			PersonName pn = contributor.getContributor().getPersonName();
			ContributorType ct = contributor.getContributor().getContributorType();
			
			MarcContributorTypeRelatorCode relatorCode = marcContributorTypeRelatorCodeService.getByContributorTypeId(ct.getId());
			
			String authorName = "";
			
			if( pn.getSurname() != null )
			{
				 authorName = pn.getSurname() + ", ";
			}
					
			if( pn.getForename() != null )
			{
				authorName += pn.getForename();
			}
			if( pn.getMiddleName() != null )
			{
				authorName += " " + pn.getMiddleName();
			}
			
			String birthYear = "";
			boolean hasBirthYear = (pn.getPersonNameAuthority().getBirthDate() != null) && (pn.getPersonNameAuthority().getBirthDate().getYear() > 0);
			if( hasBirthYear )
			{
				birthYear += pn.getPersonNameAuthority().getBirthDate().getYear();
			}
			
			String deathYear = "";
			boolean hasDeathYear = (pn.getPersonNameAuthority().getDeathDate() != null) && (pn.getPersonNameAuthority().getDeathDate().getYear() > 0);
			if( hasDeathYear )
			{
				deathYear += pn.getPersonNameAuthority().getDeathDate().getYear();
			}
			
			if( hasBirthYear || hasDeathYear )
			{
				authorName = authorName.trim() + ",";
			}
			else if( relatorCode != null )
			{
				authorName = authorName.trim() + ".";
			}
			
			String lifeDate = "";
			if( hasBirthYear || hasDeathYear )
			{
				
				lifeDate = birthYear + "-" + deathYear;
				if( hasDeathYear )
				{
					lifeDate = lifeDate + ".";
				}
			}
			
			
			// primary contributor
			if( index == 0 )
			{
				DataField df = factory.newDataField("100", '1', ' ');
				
				df.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(authorName)));
				
				if( hasBirthYear || hasDeathYear )
				{
				    df.addSubfield(factory.newSubfield('d',lifeDate));
				}
				
				
				if( relatorCode != null )
				{
					df.addSubfield(factory.newSubfield('4', relatorCode.getMarcRelatorCode().getRelatorCode()));
				}
				
				record.addVariableField(df);
			}
			// secondary contributor
			else
			{
				DataField df = factory.newDataField("700", '1', ' ');
				df.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(authorName)));
				if( hasBirthYear || hasDeathYear )
				{
					df.addSubfield(factory.newSubfield('d', lifeDate));
				}
				if( relatorCode != null )
				{
					df.addSubfield(factory.newSubfield('4', relatorCode.getMarcRelatorCode().getRelatorCode()));
					
					// add note
					if( relatorCode.equals("ths"))
					{
						DataField df2 = factory.newDataField("500", ' ', ' ');
						df2.addSubfield(factory.newSubfield('a', "Advisor:" + removeInvalidXmlChars(pn.getForename() + " " + pn.getMiddleName() + " " + pn.getSurname())));
					}
				}
				record.addVariableField(df);
			}
		}
		
	}
	
	/**
	 * Handle the description.
	 * 
	 * @param record - record to add the description to
	 * @param description - description
	 * @param mapper - mapper
	 */
	private void handleDescription(Record record, String description, MarcContentTypeFieldMapper mapper)
	{
		if( mapper != null && mapper.isThesis() )
		{
		    DataField df = factory.newDataField("502", ' ', ' ');
		    df.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(description)));
		    record.addVariableField(df);
		}
		else
		{
			DataField df = factory.newDataField("500", ' ', ' ');
		    df.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(description)));
		    record.addVariableField(df);
		}
	}
	
	/**
	 * Deal with the embargo.
	 * 
	 * @param record - record to deal with the embargo 
	 * @param embargoDate - date of the embargo
	 */
	private void handleEmbargo(Record record, Date embargoDate)
	{
		
        DateFormat fullDateFormater = new SimpleDateFormat("MM/dd/yyyy");
		
		String date = "";
		
		if(embargoDate != null )
		{
			date = fullDateFormater.format(embargoDate);
		}
		
		DataField df = factory.newDataField("506", ' ', ' ');
		String value = "Access restricted  until  " + date + ".";
		df.addSubfield(factory.newSubfield('a', value));
		record.addVariableField(df);
	}
	
	/**
	 * Handle the abstract information.
	 * 
	 * @param record
	 * @param itemAbstract
	 */
	private void handleAbstract(Record record, String itemAbstract)
	{
		DataField df = factory.newDataField("520", '3', ' ');
		df.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(itemAbstract)));
		record.addVariableField(df);
	}
	
	/**
	 * Deal with pdf information.
	 * 
	 * @param record
	 * @param item
	 */
	private void handleFileType(Record record, GenericItem item)
	{
		boolean hasPdf = false;
		for(ItemFile file : item.getItemFiles())
		{
			if( file.getIrFile().getFileInfo().getExtension().equals("pdf") )
			{
				hasPdf = true;
			}
		}
		
		if( hasPdf )
		{
			DataField df = factory.newDataField("538", ' ', ' ');
			df.addSubfield(factory.newSubfield('a', "System requirements: PDF viewer/reader."));
			record.addVariableField(df);
		}
		
	}
	
	// identifiers will have to have field and indicators matched
	private void handleIdentifiers(Record record, Set<ItemIdentifier> identifiers)
	{
		for(ItemIdentifier identifier : identifiers)
		{
			List<IdentifierTypeSubFieldMapper> mappers = identifierTypeSubFieldMapperService.getByIdentifierTypeId(identifier.getIdentifierType().getId());
			
			if( mappers.size() > 0 )
			{
				for(IdentifierTypeSubFieldMapper mapper : mappers)
				{
					//get the datafield 
					MarcDataField marcDataField = mapper.getMarcDataFieldMapper().getMarcDataField();
					
					DataField df = (DataField)record.getVariableField(marcDataField.getCode());
					
					String value = identifier.getValue();
					
					if( marcDataField.isRepeatable() || df == null)
					{
						df = factory.newDataField(marcDataField.getCode(), 
								mapper.getMarcDataFieldMapper().getIndicator1AsChar(),
								mapper.getMarcDataFieldMapper().getIndicator2AsChar());
						
						df.addSubfield(factory.newSubfield(mapper.getMarcSubField().getName().charAt(0), removeInvalidXmlChars(value)));
						record.addVariableField(df);
					}
					else
					{
						df.addSubfield(factory.newSubfield(mapper.getMarcSubField().getName().charAt(0), removeInvalidXmlChars(value)));
					}
				}
			}
		}
	}
	
	/**
	 * Add the keywords to the 653 field
	 * 
	 * @param record
	 * @param keyWords
	 */
	private void handleKeywords(Record record, String keyWords)
	{
		String[] words = keyWords.split(";");
		for( String word : words)
		{
			DataField df = factory.newDataField("653", ' ', ' ');
			df.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(word) + "."));
			record.addVariableField(df);
		}
	}
	
	/**
	 * Handle the copyright statement.
	 * 
	 * @param record
	 * @param copyrightStatement
	 */
	private void handleCopyright(Record record, CopyrightStatement copyrightStatement)
	{
		if( copyrightStatement != null)
		{
			DataField df = factory.newDataField("540", ' ', ' ');
			df.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(copyrightStatement.getText())));
			record.addVariableField(df);
		}
	}
	
	/**
	 * Handle the series information - placed in the 490 and 830 fields.
	 * 
	 * @param record
	 * @param reports
	 */
	private void handleSeries(Record record, Set<ItemReport> reports)
	{
		for(ItemReport report : reports)
		{
			DataField df490 = factory.newDataField("490", '1', ' ');
			DataField df830 = factory.newDataField("830", ' ', '0');
			String series = report.getSeries().getName();
			if( report.getReportNumber() != null && !report.getReportNumber().trim().equals(""))
			{
			    series = series + ";";   
			}
			
			df490.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(series)));
			df830.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(series)));
			if( report.getReportNumber() != null && !report.getReportNumber().trim().equals(""))
			{
				df490.addSubfield(factory.newSubfield('v', removeInvalidXmlChars(report.getReportNumber())));
				df830.addSubfield(factory.newSubfield('v', removeInvalidXmlChars(report.getReportNumber())));
			}
			record.addVariableField(df490);
			record.addVariableField(df830);
		}
	}
	
	/**
	 * Handle the citation information. placed in the 524 field
	 * 
	 * @param record
	 * @param citation
	 */
	private void handleCitation(Record record, String citation)
	{
		if( citation != null && !citation.trim().equals(""))
		{
			DataField df = factory.newDataField("524", ' ', ' ');
			df.addSubfield(factory.newSubfield('a', removeInvalidXmlChars(citation)));
			record.addVariableField(df);
		}
	}
	
	
	/**
	 * If this is a thesis record then add thesis metadata.
	 * 
	 * @param record
	 * @param item
	 */
	private void handleThesisType(Record record, GenericItem item)
	{
		for(ItemContentType itemContentType : item.getItemContentTypes() )
		{
			MarcContentTypeFieldMapper mapper = marcContentTypeFieldMapperService.getByContentTypeId(itemContentType.getContentType().getId());
			if( mapper != null && mapper.isThesis() )
			{
				DataField df = factory.newDataField("655", ' ', ' ');
				df.setIndicator2('7');
				
				df.addSubfield(factory.newSubfield('a', "Electronic dissertations."));
				df.addSubfield(factory.newSubfield('2', "lcgft"));
				record.addVariableField(df);
				// exit out once set
				return;
			}
		}
	}
	
	/**
	 * Handle the url for the record.
	 * 
	 * @param record
	 * @param url
	 */
	private void handleUrl(Record record, String url)
	{
		DataField df = factory.newDataField("856", '4', '0');
		df.addSubfield(factory.newSubfield('u', url));
		record.addVariableField(df);
	}
	
	/**
	 * Handle the leader of the record.
	 * 
	 * @param record
	 * @param mapper
	 */
	private void handleLeader(Record record, MarcContentTypeFieldMapper mapper)
	{
		// these are specific to thesis
		Leader leader = record.getLeader();
		
		leader.setRecordStatus(mapper.getRecordStatus());
		
		
		if( mapper.getMarcTypeOfRecord() != null )
		{
		    leader.setTypeOfRecord(mapper.getMarcTypeOfRecord().getRecordType());
		}
		else
		{
			leader.setTypeOfRecord(' ');
		}
		leader.setImplDefined1(new char[]{mapper.getBibliographicLevel(), mapper.getTypeOfControl()});
		leader.setImplDefined2(new char[]{mapper.getEncodingLevel(), mapper.getDescriptiveCatalogingForm(), ' '});
	    leader.setCharCodingScheme('a');
	}
	
	/**
	 * Create the 008 field.
	 * 
	 * @param record - record to 
	 * @param mapper - mapper for ctent types
	 * @param year - year of publication
	 * @param languageType - langauge type
	 * @param placeOfPublication - location where published.
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void handle008Field(Record record, MarcContentTypeFieldMapper mapper, String year, 
			LanguageType languageType,
			PlaceOfPublication placeOfPublication)
	{
		// add 008 fields
		char[] _008 = mapper.getControlField008().toCharArray();
		
		// overwrite the year information
		_008[6] = 's';
		
		
		if( year.length() <= 4 )
		{
		    for( int index = 0; index < year.length(); index++ )
		    {
		        _008[7 + index] = year.charAt(index);
		    }
		}
		
		
		if(languageType != null)
		{
			if( languageType.getIso639_2() != null && languageType.getIso639_2() != null )
			{
		        String language = languageType.getIso639_2();
		        if( language.length() > 3 )
		        {
		    	    language = language.substring(0, 3);
		        }
		        
		        for( int index = 0; index < language.length(); index++ )
		        {
		        	_008[35 + index] = language.charAt(index);
		        }
		      
			}
		}
		
		if( placeOfPublication != null )
		{
			if( placeOfPublication.getLetterCode() != null )
			{
				String letterCode = placeOfPublication.getLetterCode();
				if( letterCode.length() > 3 )
				{
					letterCode = letterCode.substring(0, 3);
				}
				
				for( int index = 0; index < letterCode.length(); index++ )
		        {
		        	_008[15 + index] = letterCode.charAt(index);
		        }
				
			}
		}
		
		// this is hard coded for new york right now.  will need to be fixed to work with
		// any state / country
		ControlField control_008 = factory.newControlField("008", new String(_008));
		record.getControlFields().add(control_008);
	}
	

	
	/**
	 * Set the content type filed mapping service.
	 * 
	 * @param marcContentTypeFieldMapperService
	 */
	public void setMarcContentTypeFieldMapperService(
			MarcContentTypeFieldMapperService marcContentTypeFieldMapperService) {
		this.marcContentTypeFieldMapperService = marcContentTypeFieldMapperService;
	}
	
	/**
	 * Set the content type service.
	 * 
	 * @param contentTypeService
	 */
	public void setContentTypeService(ContentTypeService contentTypeService) {
		this.contentTypeService = contentTypeService;
	}

	/**
	 * Set the marc contributor type relator code service.
	 * 
	 * @param marcContributorTypeRelatorCodeService
	 */
	public void setMarcContributorTypeRelatorCodeService(
			MarcContributorTypeRelatorCodeService marcContributorTypeRelatorCodeService) {
		this.marcContributorTypeRelatorCodeService = marcContributorTypeRelatorCodeService;
	}
	
	/**
	 * Set the identifier type sub field mapper service.
	 * 
	 * @param identifierTypeSubFieldMapperService
	 */
	public void setIdentifierTypeSubFieldMapperService(
			IdentifierTypeSubFieldMapperService identifierTypeSubFieldMapperService) {
		this.identifierTypeSubFieldMapperService = identifierTypeSubFieldMapperService;
	}


	/**
	 * Set the institutional item version url generator.
	 * 
	 * @param institutionalItemVersionUrlGenerator
	 */
	public void setInstitutionalItemVersionUrlGenerator(
			InstitutionalItemVersionUrlGenerator institutionalItemVersionUrlGenerator) {
		this.institutionalItemVersionUrlGenerator = institutionalItemVersionUrlGenerator;
	}

	/**
	 * Set the extent type field mapper service.
	 * 
	 * @param extentTypeSubFieldMapperService
	 */
	public void setExtentTypeSubFieldMapperService(
			ExtentTypeSubFieldMapperService extentTypeSubFieldMapperService) {
		this.extentTypeSubFieldMapperService = extentTypeSubFieldMapperService;
	}
	
	/**
	 * Removes invalid xml charachters from the specified string.
	 * 
	 * @param value - string to replace the characters with
	 * @return cleaned up string.
	 */
	private String removeInvalidXmlChars(String value)
	{
		String fixed = value.replaceAll("\u201C", "\"");
		fixed = fixed.replaceAll("\u201D", "\"");
		fixed = fixed.replaceAll("\u2019", "'");
	    return fixed;
	}

	
	/**
	 * Compare the extent type sub field mapper based on name.
	 * 
	 * @param o1
	 * @param o2
	 * 
	 * @return sub field mapper based on name.
	 */
	public int compare(ExtentTypeSubFieldMapper o1, ExtentTypeSubFieldMapper o2) {
		return o1.getMarcSubField().getName().compareTo(o2.getMarcSubField().getName());
	}

	

}
