/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.ir.importer.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.marc4j.MarcException;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;


import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.SearchResults;
import edu.ur.ir.importer.BadMarcFileException;
import edu.ur.ir.importer.MarcFileToVersionedItemImporter;
import edu.ur.ir.item.DuplicateContributorException;
import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeService;
import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.PlaceOfPublicationService;
import edu.ur.ir.item.PublishedDate;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherService;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.SeriesService;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.item.metadata.marc.ExtentTypeSubFieldMapper;
import edu.ur.ir.item.metadata.marc.ExtentTypeSubFieldMapperService;
import edu.ur.ir.item.metadata.marc.IdentifierTypeSubFieldMapper;
import edu.ur.ir.item.metadata.marc.IdentifierTypeSubFieldMapperService;
import edu.ur.ir.item.metadata.marc.MarcContentTypeFieldMapper;
import edu.ur.ir.item.metadata.marc.MarcContentTypeFieldMapperService;
import edu.ur.ir.item.metadata.marc.MarcContributorTypeRelatorCode;
import edu.ur.ir.item.metadata.marc.MarcContributorTypeRelatorCodeService;
import edu.ur.ir.person.BirthDate;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorService;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeService;
import edu.ur.ir.person.DeathDate;
import edu.ur.ir.person.NameAuthorityIndexService;
import edu.ur.ir.person.NameAuthoritySearchService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;

/**
 * Deals with importing marc files.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultMarcFileToVersionedItemImporter implements MarcFileToVersionedItemImporter{
	
	//  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(DefaultMarcFileToVersionedItemImporter.class);
	
	// service to help with relator codes
	private MarcContributorTypeRelatorCodeService marcContributorTypeRelatorCodeService;

	// service for dealing with identifier type sub fields
	private IdentifierTypeSubFieldMapperService identifierTypeSubFieldMapperService;
	
	// service for dealing with identifier type sub fields
	private ExtentTypeSubFieldMapperService extentTypeSubFieldMapperService;
	
	// Service for indexing names */
	private NameAuthorityIndexService nameAuthorityIndexService;
	
	// service for finding names
	private NameAuthoritySearchService nameAuthoritySearchService;

	// service to deal with repository information
	private RepositoryService repositoryService;
	
	// Service for dealing with contributor types */
	private ContributorTypeService contributorTypeService;
	
	// service to deal with contributor information
	private ContributorService contributorService;
	
	// service to deal with person information
	private PersonService personService;
	
	// service to deal with place of publication information
	private PlaceOfPublicationService placeOfPublicationService;
	
	// service to deal with publisher information
	private PublisherService publisherService;
	
	//  Service for dealing with mapping between content type and marc fields */
	private MarcContentTypeFieldMapperService marcContentTypeFieldMapperService;
	
	// deal with language information
	private LanguageTypeService languageTypeService;
	
	// service to deal with series information
	private SeriesService seriesService;

	// service to deal with errors
	ErrorEmailService errorEmailService;



	/**
	 * Create a versioned item.
	 * 
	 * @param record - MARC record
	 * @param owner - user who will own the generic item
	 * @return - created versioned item.
	 * 
	 * @throws NoIndexFoundException
	 */
	@SuppressWarnings("unchecked")
	public VersionedItem createVersionedItem(Record record, IrUser owner) throws NoIndexFoundException
	{
	
		
		GenericItem item = new GenericItem("unknown");
	    VersionedItem versionedItem = new VersionedItem(owner, item);
		String keywords = "";

		
		this.setContentType(record, item);
		this.setLanguage(record, item);
		
        // returns fields for tags 010 through 999
		List<DataField> fields = (List<DataField>)record.getDataFields();
		for(DataField field : fields)
		{
			String tag = field.getTag();
		    char ind1 = field.getIndicator1();
		    char ind2 = field.getIndicator2();

		    log.debug("Tag: " + tag + " Indicator 1: " + ind1 + " Indicator 2: " + ind2);
		    
		    List subfields = field.getSubfields();
	        Iterator i = subfields.iterator();

	        while (i.hasNext()) {
	            Subfield subfield = (Subfield) i.next();
		        char code = subfield.getCode();
		        String data = subfield.getData();
		        log.debug("Subfield code: " + code + " Data element: " + data);
	        }
		    
		    if( tag.equals("245"))
		    {
		    	handle245(field, item);
		    }
		    else if( tag.equals("246") && ind1 == '3')
		    {
		    	addSubTitle(field, item);
		    }
		    else if( tag.equals("240") )
		    {
		    	addSubTitle(field, item);
		    }
		    else if( tag.equals("740") )
		    {
		    	addSubTitle(field, item);
		    }
		    else if( tag.equals("520") && ind1 == '3')
		    {
		    	addAbstract(field, item);
		    }
		    else if( tag.equals("100") || tag.equals("700"))
		    {
		    	handle100(field, item);
		    	
		    	if( tag.equals("700"))
		    	{
		    		if( field.getSubfield('t') != null )
		    		{
		    			String data = field.getSubfield('t').getData();
		    			if( data != null && !data.trim().equals(""))
		    			{
		    				item.addSubTitle(this.endPunctuationStripper(data.trim()), null);
		    			}
		    		}
		    	}
		    }
		    else if( tag.equals("260"))
		    {
		        handle260(field, item);	
		    }
		    else if( tag.equals("653") || tag.equals("650") )
		    {
		    	//keywords
		    	String value = handleSubjects(field, item);
		    	if( value != null && !value.trim().equals("") )
		    	{
		    		if( keywords.equals("") )
		    		{
		    	        keywords = value;
		    		}
		    		else
		    		{
		    			keywords = keywords + ";" + value;
		    		}
		        }
		    }
		    else if( tag.equals("500") || tag.equals("502") || tag.equals("505") )
		    {
		    	addDescription(field, item);
		    }
		    else if( (tag.equals("490") && ind1 != '1') || tag.equals("830")  )
		    {
		    	addSeries(field, item);
		    }
		    else
		    {
		    	handleOtherTags(field, item);
		    }
		}
		
		if( keywords != null && !keywords.trim().equals(""))
		{
			item.setItemKeywords(keywords);
		}
		
		return versionedItem;
	}
	
	/**
	 * Deal with the series information.
	 * 
	 * @param field to get series from
	 * @param item to add series to
	 */
	private void addSeries(DataField field, GenericItem item)
	{
		log.debug("add series");
		if( field.getSubfield('a') != null ) 
		{
			String data = field.getSubfield('a').getData();
			data = this.endPunctuationStripper(data);
			if( data != null && !data.trim().equals(""))
			{
				Series series = seriesService.getSeries(data);
				log.debug("Found series " + series);
				// create new series if not found
				if( series == null )
				{
					log.debug("adding new series " + data);
					series = new Series(data);
					seriesService.saveSeries(series);
				}
				
				// get the report number
				String reportNumber = null;
			    if( field.getSubfield('v') != null )
			    {
			    	 data = field.getSubfield('v').getData();
			    	 if( data != null && !data.trim().equals(""))
			    	 {
			    		 reportNumber = data.trim();
			    	 }
			    }
			    log.debug("report number = " + reportNumber);
			    item.addReport(series, reportNumber);
			}
		}
	}
	
	/**
	 * Set the primary content type.
	 * 
	 * @param typeOfRecord - type of record to set the content type.
	 * @param item - item to set the content type for.
	 */
	private void setContentType(Record record, GenericItem item)
	{
		char typeOfRecord = record.getLeader().getTypeOfRecord();
		
		ControlField cf = (ControlField)record.getVariableField("008");
		boolean thesisMarkerSet = false;
		if( cf != null )
		{
		    String data = cf.getData();
		    if( data != null && data.length() > 25 )
		    {
		    	char marker = data.charAt(24);
		    	log.debug("thesis marker = " + marker);
		    	if( marker == 'm' )
		    	{
		    		thesisMarkerSet = true;
		    	}
		    }
		}
		
		
		List<MarcContentTypeFieldMapper> mappers = marcContentTypeFieldMapperService.getByRecordType(typeOfRecord);
	    
		Iterator<MarcContentTypeFieldMapper> iter = mappers.iterator();
		boolean done = false;
		while(iter.hasNext() && !done)
		{
			MarcContentTypeFieldMapper mapper = iter.next();
			if( mapper.getThesis()  && thesisMarkerSet )
			{
				item.setPrimaryContentType(mapper.getContentType());
				done = true;
			}
			else
			{
				item.setPrimaryContentType(mapper.getContentType());
			}
		}
	}
	
	/**
	 * Sets the language based on the 3 digits in the 008 header.
	 * 
	 * @param record to get language from 
	 * @param item - to add lanaguage to.
	 */
	private void setLanguage(Record record, GenericItem item)
	{
		ControlField cf = (ControlField)record.getVariableField("008");
		String code = "";
		if( cf != null )
		{
		    String data = cf.getData();
		    if( data != null && data.length() > 38 )
		    {
		    	code = code + data.charAt(35);
		    	code = code + data.charAt(36);
		    	code = code + data.charAt(37);
		    	code = code.trim();
		    	LanguageType lt = this.languageTypeService.getByIso639_2(code);
		    	log.debug("Language type = " + lt + " for code " + code);
		    	if( lt != null )
		    	{
		    	    item.setLanguageType(lt);
		    	}
		    }
		}
	}

	/**
	 * Import the marc file into the personal collection
	 * 
	 * @throws FileNotFoundException - if file does not exist
	 * @throws NoIndexFoundException  - if the index cannot be found
	 * @throws BadMarcFileException - if the marc file cannot be parsed
	 * 
	 * @see edu.ur.ir.importer.MarcFileToVersionedItemImporter#importMarc(java.io.File, edu.ur.ir.user.PersonalCollection)
	 */
	
	public List<VersionedItem> importMarc(File f, IrUser owner) throws FileNotFoundException, NoIndexFoundException, BadMarcFileException {
		
		List<VersionedItem> versionedItems = new LinkedList<VersionedItem>();
		FileInputStream fis = null;
		try
		{
		    fis = new FileInputStream(f);
		    MarcReader reader = new MarcStreamReader(fis);
		    while (reader.hasNext()) {
	            Record record = reader.next();
	            versionedItems.add(createVersionedItem(record, owner));
	        }
		}
		// deal with file format we cannot handle
		catch(MarcException e)
		{
			errorEmailService.sendError(e);
			throw new BadMarcFileException("The file could not be parsed " + e.getMessage());
		}
		finally
		{
		    if( fis != null )
		    {
		    	try {
					fis.close();
				} catch (IOException e) {
					fis = null;
				}
		    }
		}
		return versionedItems;
	}
	
	/**
	 * Add the sub title to the item.
	 * 
	 * @param field - to get sub title from
	 * @param item - to add sub title to
	 */
	private void addSubTitle(DataField field, GenericItem item)
	{
		log.debug("add subtitle ");
		String data = null;
		
		if( field.getSubfield('a') != null ) 
		{
			data = field.getSubfield('a').getData();
			log.debug("adding sub title " + data);
			if(  data != null && !data.trim().equals(""))
			{
				item.addSubTitle(this.endPunctuationStripper(data), null);
			}
		}
	}
	
	/**
	 * Add the abstract to the item.
	 * 
	 * @param field - to get data from
	 * @param item - to add abstract to
	 */
	private void addAbstract(DataField field, GenericItem item)
	{
		String data = null;
		
		if( field.getSubfield('a') != null ) 
		{
			data = field.getSubfield('a').getData();
			if(  data != null && !data.trim().equals(""))
			{
				item.setItemAbstract(data);
			}
		}
	}
	
	/**
	 * Add the description to the item.
	 * 
	 * @param field - to get data from
	 * @param item - to add data to
	 */
	private void addDescription(DataField field, GenericItem item)
	{
		String data = null;
		
		if( field.getSubfield('a') != null ) 
		{
			data = field.getSubfield('a').getData();
			if(  data != null && !data.trim().equals(""))
			{
				if( item.getDescription() == null )
				{
				    item.setDescription(data);
				}
				else
				{
					item.setDescription(item.getDescription() + " " + data);
				}
			}
		}
	}
	
	/**
	 * Get the key word
	 * 
	 * @param field - field to get data from
	 * @param item - to add data to
	 * 
	 * @return the found keyword or null not found
	 */
	private String handleSubjects(DataField field, GenericItem item)
	{
		String data = null;
		if( field.getSubfield('a') != null )
		{
			data = field.getSubfield('a').getData();
			if( data != null && !data.trim().equals(""))
			{
			    data = endPunctuationStripper(data.trim());
			}
		}
		return data;
	}
	
	/**
	 * Deal with tags set up by the user - mapped to identifiers and extents.
	 * 
	 * @param field - field to get data from
	 * @param item - item to add the data to
	 */
	@SuppressWarnings("unchecked")
	private void handleOtherTags(DataField field, GenericItem item)
	{
		log.debug("handeling other tags ");
		String tag = field.getTag();
	    char ind1 = field.getIndicator1();
	    char ind2 = field.getIndicator2();

	    log.debug("Tag: " + tag + " Indicator 1: " + ind1 + " Indicator 2: " + ind2);
	    
	    List subfields = field.getSubfields();
        Iterator i = subfields.iterator();

        // iterate through sub fields
        while (i.hasNext()) {
            Subfield subfield = (Subfield) i.next();
	        char code = subfield.getCode();
	        String data = subfield.getData();
	        log.debug("Subfield code: " + code + " Data element: " + data);
	        
	        
	        
	        if( data != null && !data.trim().equals(""))
	        {
	        	// handle 090 - 099 and 050 tags a little differently
		        // if they are using the 'a' tag then append the subfield
		        // b
		        if(tag.matches("09[0-9]") || tag.equals("050"))
		        {
		        	if( code == 'a')
		        	{
		        		if(field.getSubfield('b') != null )
		        		{
		        			String cutter = field.getSubfield('b').getData();
		        			if( cutter != null && !cutter.trim().equals(""))
		        			{
		        				data = data + cutter.trim();
		        			}
		        		}
		        	}
		        }
	        	//handle identifier type mappings
	            List<IdentifierTypeSubFieldMapper> identMappers = 
	            	identifierTypeSubFieldMapperService.getByDataField(tag, 
	            		ind1 + "", ind2 + "", code +"");
	            log.debug("Found " + identMappers.size() + " identifer type mappers");
	            for(IdentifierTypeSubFieldMapper mapper : identMappers )
	            {
	                        
	        	    IdentifierType identType = mapper.getIdentifierType();
	        	    item.addItemIdentifier(data, identType);
	            }
	            
	            // handle extent type mappings
	            List<ExtentTypeSubFieldMapper> extentMappers = 
	            	extentTypeSubFieldMapperService.getByDataField(tag, ind1 + "",  ind2 + "", code +"");
	            log.debug("Found " + extentMappers.size() + " extent type mappers ");
	            for(ExtentTypeSubFieldMapper mapper : extentMappers)
	            {
	                ExtentType extentType = mapper.getExtentType();
	                item.addItemExtent(extentType, endPunctuationStripper(data) );
	            }
	        }
        }
		
		 
	}
	
	/**
	 * Handle the 100 level record.
	 * 
	 * @param field to get primary contributor from
	 * @param item to add primary contributor to
	 * 
	 * @throws NoIndexFoundException 
	 */
	private void handle100(DataField field, GenericItem item) throws NoIndexFoundException
	{
		log.debug("Dealing with " + field.getTag() );
		if( field.getSubfield('a') != null )
		{
			ContributorType contributorType = contributorTypeService.getByUniqueSystemCode("AUTHOR");
		    
			
			PersonNameAuthority authority = splitName(field.getSubfield('a').getData().trim());
						
		    if( field.getSubfield('d') != null)
			{
		    	if( field.getSubfield('d').getData() != null )
		    	{
				    splitBirthDeathYears(field.getSubfield('d').getData(), authority);
		    	}
				log.debug("Person name authority = " + authority);
			}
		    
		   
		    log.debug("checking sub field 4 data ");
		    if( field.getSubfield('4') != null && field.getSubfield('4').getData() != null )
		    {
		    	log.debug("sub field 4 data = " + field.getSubfield('4').getData());
		    	List<MarcContributorTypeRelatorCode> marcContributorTypes = marcContributorTypeRelatorCodeService.getByRelatorCode(field.getSubfield('4').getData());
		        log.debug("Found " + marcContributorTypes.size() + " marc contributor types ");
		    	if( marcContributorTypes.size() > 0 )
		        {
		        	MarcContributorTypeRelatorCode contributorTypeByRelatorCode = marcContributorTypes.get(0);
		        	log.debug("found contributor type " + contributorTypeByRelatorCode);
		        	if ( contributorTypeByRelatorCode != null )
		        	{
		        		contributorType= contributorTypeByRelatorCode.getContributorType();
		        		log.debug("setting contributor type " + contributorType);
		        	}
		        }
		    }
		    
		    
		    Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		    PersonName pn = this.getExistingAuthor(repository, authority);
		    Contributor contributor = contributorService.get( pn, contributorType);
			log.debug("Contributor found = " + contributor);
			if( contributor == null )
			{
				log.debug("creating new contributor");
				contributor = new Contributor();
			    contributor.setContributorType(contributorType);
			    contributor.setPersonName(pn);
			}
			try {
				log.debug("existing contributor used");
				item.addContributor(contributor);
			} catch (DuplicateContributorException e) {
				log.debug("did not add contributor " + contributor + " because of error ", e);
			}
		    
		}
	}
	
	
	/**
	 * Deal with the 245. This is the title
	 * 
	 * @param field - get title from
	 * @param item - add title to.
	 */
	private void handle245(DataField field, GenericItem item)
	{
		log.debug("Dealing with 245");
		
		Subfield subfield = field.getSubfield('a');
		String title = "Unknown";
		String articles = "";
		
		if( subfield != null )
		{
		    char ind2 = field.getIndicator2(); 
		    int articlesEndPosition = 0;
		    
		    if( ind2 != ' ')
		    {
			    Integer value  = Integer.valueOf( ind2 + "");
			    if( value != null )
			    {
				    articlesEndPosition = value.intValue();
			    }
		    }
		    
		    if( subfield.getData() != null && !subfield.getData().trim().equals(""))
		    {
		        title = subfield.getData();
		        if(title.length() > articlesEndPosition)
		        {
		        	title = title.substring(articlesEndPosition).trim();
		        }
		    }
		    
		    // append remainder of title
		    Subfield subfieldRemainder = field.getSubfield('b');
		    if( subfieldRemainder != null )
		    {
		    	String titleRemainder = subfieldRemainder.getData();
		    	if( titleRemainder != null )
		    	{
		    		title = title + " " + titleRemainder.trim();
		    	}
		    }
		    
		    log.debug("Title before " + title);
		    title = endPunctuationStripper(title);
		    log.debug("title after " + title);
		    articles = subfield.getData().substring(0, articlesEndPosition).trim();
		    item.setName(title);
		    item.setLeadingNameArticles(articles);
		   
		}
		
		log.debug("Title = |" + title + "|");
		log.debug("Articles = " + articles);
		
	}
	
	/**
	 * Get the publisher information. 
	 * 
	 * @param field to get publisher from
	 * @param item - to add publisher to
	 */
	private void handle260(DataField field, GenericItem item)
	{
		log.debug("handle publisher");
		ExternalPublishedItem externalPublishedItem = new ExternalPublishedItem();
		Subfield subfield = field.getSubfield('a');
		boolean add = false;
		if( subfield != null && subfield.getData() != null )
		{
			String place = endPunctuationStripper(subfield.getData());
			log.debug("placeOfPublication = " + place);
			PlaceOfPublication placeOfPub = placeOfPublicationService.get(place);
			if( placeOfPub == null )
			{
				placeOfPub = new PlaceOfPublication(place);
				placeOfPublicationService.save(placeOfPub);
			}
			
			externalPublishedItem.setPlaceOfPublication(placeOfPub);
			add = true;
		}
		subfield = field.getSubfield('b');
		if( subfield != null && subfield.getData() != null)
		{
			String pub = endPunctuationStripper(subfield.getData());
			log.debug("publisher = " + pub );
			Publisher publisher = publisherService.getPublisher(pub);
			if( publisher == null )
			{
				publisher = new Publisher(pub);
				publisherService.savePublisher(publisher);
			}
			
			externalPublishedItem.setPublisher(publisher);
			
			add = true;
		}
		
		subfield = field.getSubfield('c');
		if( subfield != null && subfield.getData() != null)
		{
			String pubDate = this.endPunctuationStripper(subfield.getData());
			log.debug("Date of publication = " + pubDate);
			PublishedDate publicationDate = this.getPublishedDate(pubDate);
			if( publicationDate != null )
			{
				externalPublishedItem.updatePublishedDate(publicationDate.getMonth(), publicationDate.getDay(), publicationDate.getYear());
			}
			add = true;
		}
		
		if( add )
		{
			item.setExternalPublishedItem(externalPublishedItem);
		}
	}
	
	
	/**
	 * Strip off ending punctuation this includes
	 *  /
	 *  :
	 *  ,
	 *  ;
	 *  .
	 * 
	 * @param value 
	 * @return string with ending punctuation removed
	 */
	private String endPunctuationStripper(String value)
	{
		if( value == null )
		{
			return value;
		}
		if(value.lastIndexOf('/') == (value.length() - 1))
		{
			return value.substring(0, value.length()-1).trim();
		}
		else if(value.lastIndexOf(':') == (value.length() - 1))
		{
			return value.substring(0, value.length()-1).trim();
		}
		else if(value.lastIndexOf(',') == (value.length() - 1))
		{
			return value.substring(0, value.length()-1).trim();
		}
		else if(value.lastIndexOf(';') == (value.length() - 1))
		{
			return value.substring(0, value.length()-1).trim();
		}
		else if(value.lastIndexOf('.') == (value.length() - 1))
		{
			return value.substring(0, value.length()-1).trim();
		}
		else
		{
			return value;
		}
	}
	
	
	/**
	 * splits out the name as best it can to create a person name authority.
	 * 
	 * @param authorName
	 * @return the person name authority created.
	 */
	private PersonNameAuthority splitName(String authorName) {
		PersonName personName = new PersonName();
		PersonNameAuthority nameAuthority = new PersonNameAuthority(personName);

		// split on the name
		String[] fullNameParts = authorName.split(",");

		int size = fullNameParts.length;
		log.debug("size = 2");
		if( size == 2)
		{
			// assume last name in first part
			
			personName.setSurname(fullNameParts[0].trim());
			
			// check the last part for multiple parts
			String[] foreNameParts = fullNameParts[1].trim().split(" ");
			if( foreNameParts.length > 1)
			{
				personName.setForename(foreNameParts[0].trim());
				String middleName = "";
				for( int index = 1; index < foreNameParts.length; index++)
				{
					middleName = middleName + foreNameParts[index] + " ";
				}
				middleName = middleName.trim();
				personName.setMiddleName(middleName);
			}
			else
			{
				personName.setForename(fullNameParts[1].trim());
			}
		}
		else 
		{
			log.debug("Adding all to last name " + fullNameParts[0]);
			personName.setSurname(authorName);
		}

		log.debug("PersonName = " + personName);
		return nameAuthority;	
	}
	
	
	/**
	 * Split out the birth and death years if possible.
	 * 
	 * @param years
	 * @param authority
	 */
	private void splitBirthDeathYears(String years, PersonNameAuthority authority)
	{
		years = endPunctuationStripper(years);
		log.debug("handle birth death year splitting " + years);
		if( years != null )
		{
			
		    String[] dateParts = endPunctuationStripper(years).split("-");
		    if( dateParts.length ==  2)
		    {
		    	
		    	try
		    	{
		    		int birthYear = this.getYear(dateParts[0]);
		    		if( birthYear != -1 )
		    		{
		    			BirthDate birthDate = new BirthDate(birthYear);
		    			authority.setBirthDate(birthDate);
		    		}
		    		
		    		
		    	}
		    	catch(Exception e)
		    	{
		    		log.debug("Could not parse birth date " + dateParts[0]);
		    		// do nothing
		    	}
		    	
		    	try
		    	{
		    		int deathYear = this.getYear(dateParts[1]);
		    		if( deathYear != -1)
		    		{
		    		    DeathDate deathDate = new DeathDate(deathYear);
		    		    authority.setDeathDate(deathDate);
		    		}
		    	}
		    	catch(Exception e)
		    	{
		    		log.debug("Could not parse death date " + dateParts[1]);
		    		// do nothing
		    	}
		    	
		    }
		    else if ( dateParts.length == 1)
		    {
		    	try
		    	{
		    		int birthYear = this.getYear(dateParts[0]);
		    		if( birthYear != -1 )
		    		{
		    			BirthDate birthDate = new BirthDate(birthYear);
		    			authority.setBirthDate(birthDate);
		    		}
		    	}
		    	catch(Exception e)
		    	{
		    		log.debug("Could not parse birth date 2 " + dateParts[0]);
		    		// do nothing
		    	}
		    }
		}
		
		
	}
	
	
	public void setMarcContributorTypeRelatorCodeService(
			MarcContributorTypeRelatorCodeService marcContributorTypeRelatorCodeService) {
		this.marcContributorTypeRelatorCodeService = marcContributorTypeRelatorCodeService;
	}

	public void setIdentifierTypeSubFieldMapperService(
			IdentifierTypeSubFieldMapperService identifierTypeSubFieldMapperService) {
		this.identifierTypeSubFieldMapperService = identifierTypeSubFieldMapperService;
	}

	public void setExtentTypeSubFieldMapperService(
			ExtentTypeSubFieldMapperService extentTypeSubFieldMapperService) {
		this.extentTypeSubFieldMapperService = extentTypeSubFieldMapperService;
	}
	
	
	/**
	 * Get the final list of authors. This searches the existing list of authors.  If one
	 * is found it is used instead of the created name authority.
	 * 
	 * @param createdNameAuthorities
	 * @return
	 * @throws NoIndexFoundException 
	 */
	private PersonName getExistingAuthor(Repository repository, PersonNameAuthority createdAuthority) throws NoIndexFoundException
	{
		log.debug("check for existing author");
		String query  = buildAuthorityNameQuery(createdAuthority);
		log.debug("query = " + query);
		SearchResults<PersonNameAuthority> results = nameAuthoritySearchService.search(new File(repository.getNameIndexFolder()), query, 0, 10);
	    log.debug("search results size = " + results.getNumberObjects());
		
		PersonName createdName = createdAuthority.getAuthoritativeName();
		PersonName finalName =  createdName;
		boolean found = false;
		if( results.getNumberObjects() > 0  )
	    {
	        List<PersonNameAuthority> foundNames = results.getObjects();
	        Iterator<PersonNameAuthority> iterator = foundNames.iterator();
	        while(!found && iterator.hasNext())
	        {
	        	PersonNameAuthority foundAuthorityName = iterator.next();
	        	
	        	if( foundAuthorityName != null )
	        	{
	        	    BirthDate birthDate = foundAuthorityName.getBirthDate();
	        	    DeathDate deathDate = foundAuthorityName.getDeathDate();
	        	    boolean datesOk = true;
	        	
	        	    if( birthDate != null && createdAuthority.getBirthDate() != null)
	        	    {
	        		   if( birthDate.getYear() != createdAuthority.getBirthDate().getYear() )
	        		   {
	        			   datesOk = false;
	        		   }
	        	    }
	        	    
	        	    if( deathDate != null && createdAuthority.getDeathDate() != null)
	        	    {
	        		   if( deathDate.getYear() != createdAuthority.getDeathDate().getYear() )
	        		   {
	        			   datesOk = false;
	        		   }
	        	    }
	        	
	        	
	        	    log.debug("dates ok = " + datesOk );
	        	    
	        	    if( datesOk )
	        	    {
	        		    // set of names for the authoritative name
	        		    Set<PersonName> names = foundAuthorityName.getNames();
	        		    Iterator<PersonName> nameIter = names.iterator();
	        		
	        		    while(!found && nameIter.hasNext())
	        		    {
	        			    PersonName foundName = nameIter.next();
	        			    log.debug( "comparing found name " + foundName + " to name " +  createdName);
	        			    if( foundName.softEquals(createdName))
	        			    {
	        		            log.debug("Name found!");
	        		            found = true;
	        		            finalName = foundName;
	        			    }
	        			    else
	        			    {
	        			    	log.debug("Names NOT equal!");
	        			    }
	        		    }
	        	    }
	            }
	        }
		}
		if( !found )
	    {
	        personService.save(createdAuthority);
	        nameAuthorityIndexService.addToIndex(createdAuthority, new File(repository.getNameIndexFolder()));
	    }
		log.debug("adding final name " + finalName);
		return finalName;
	}
	
	/**
	 * Build a search query for the person name.
	 * 
	 * @param authority
	 * @return
	 */
	private String buildAuthorityNameQuery(PersonNameAuthority authority)
	{
	   StringBuffer queryString = new StringBuffer("");
	   PersonName personName = authority.getAuthoritativeName();
	   if( personName.getForename() != null )
		{
			queryString.append(personName.getForename());
			queryString.append(" ");
		}

		if( personName.getMiddleName()!= null )
		{
			queryString.append(personName.getMiddleName());
			queryString.append(" ");
		}

		if( personName.getFamilyName() != null )
		{
			queryString.append(personName.getFamilyName());
			queryString.append(" ");
		}

		if( personName.getSurname() != null )
		{
			queryString.append(personName.getSurname());
			queryString.append(" ");
		}

	   
	   return queryString.toString();
	}
	
	/**
	 * Get the published date for the marc record
	 * 
	 * @param dspaceItem
	 * @return
	 */
	private PublishedDate getPublishedDate(String date)
	{
		log.debug("Processing publish date");
		PublishedDate publishedDate = null;
		
		log.debug("Publish date = " + date);
		
		if(date != null && !date.trim().equals(""))
		{
			    date = this.endPunctuationStripper(date.trim());

			    log.debug("date length = " + date.length());
		    	// assume year only ex. 1999
			    // or 199- or 199?
		    	if( date.length() == 4 || date.length() == 5 || date.length() == 6)
		    	{
		    		 int year = this.getYear(date);
		    		 if( year != -1)
		    		 {
		    			 publishedDate = new PublishedDate();
		    		     publishedDate.setYear(year);
		    		 }
		    	}
		    	
		    	// assume year and month ex. 1985-03 or [188-?]
		    	else if( date.length() == 7 )
		    	{
		    		// assume [188-?]
		    		if(date.startsWith("["))
		    		{
		    			 int year = this.getYear(date);
			    		 if( year != -1)
			    		 {
			    			 publishedDate = new PublishedDate();
			    		     publishedDate.setYear(year);
			    		 }
		    			
		    		}
		    		else
		    		{
		    		    String[] dateParts = date.split("-");
		    		    if(dateParts.length == 2)
		    		    {
			    		    try
			    		    {
			    		    	publishedDate = new PublishedDate();
			    		        publishedDate.setYear(new Integer(dateParts[0]));
			    		        publishedDate.setMonth(new Integer(dateParts[1]));
			    		    }
			    		    catch(NumberFormatException nfe)
			    		    {
			    			    publishedDate = null;
			    			    log.error("could not create year for date " + date, nfe);
			    		    }
		    		    }
		    		    else
		    		    {
		    			
		    			     publishedDate = null;
		    			     log.error(" could not do 7 digit split on - for dspace date " + date);
		    		    }
		    	    }
		    	}
		    	
		    	// assume year month day ex. 1985-03-01
		    	else if( date.length() == 10 )
		    	{
		    		String[] dateParts = date.split("-");
		    		if(dateParts.length == 3)
		    		{
		    			publishedDate = new PublishedDate();
			    		 try
			    		 {
			    		     publishedDate.setYear(new Integer(dateParts[0]));
			    		     publishedDate.setMonth(new Integer(dateParts[1]));
			    		     publishedDate.setMonth(new Integer(dateParts[2]));
			    		 }
			    		 catch(NumberFormatException nfe)
			    		 {
			    			 publishedDate = null;
			    			 log.error("could not create year for date " + date, nfe);
			    		 }
		    		}
		    		else
		    		{
		    			 publishedDate = null;
		    			log.error(" could not do 10 digit split on - for dspace date " + date);
		    		}
		    	}
		    
		}
		return publishedDate;
	}
	
	/**
	 * Get the year based on the string input.
	 * 
	 * @param year
	 * @return the found year or -1 if could not be parsed.
	 */
	private int getYear(String date)
	{
		int value = -1;
		date = this.endPunctuationStripper(date);
		if( date == null )
		{
			return value;
		}
    	// assume year only ex. 1999
	    // or 199- or 199?
    	if( date.length() == 4 )
    	{
    		 date = date.replace('-', '0');
    		 date = date.replace('?', '0');
    		 try
    		 {
    			 value = Integer.valueOf(date).intValue();
    		 }
    		 catch(NumberFormatException nfe)
    		 {
    			 log.error("could not create year for date " + date, nfe);
    		 }
    	}
    	// assume year with circa ex. c1999
    	else if( date.length() == 5 )
    	{
    		 date = date.replace('c', ' ').trim();
    		 date = date.replace('-', '0');
    		 date = date.replace('?', '0');
    		 log.debug("date updated to " + date);
    		 try
    		 {
    			 value = Integer.valueOf(date).intValue();
    		 }
    		 catch(NumberFormatException nfe)
    		 {
    			 log.error("could not create year for date " + date, nfe);
    		 }
    	}
    	// assume [1978] or [197-] or [197?]
    	else if (date.length() == 6)
    	{
    		date = date.replace('[', ' ');
    		date = date.replace(']', ' ');
    		date = date.replace('-', '0');
    		date = date.replace('?', '0');
    		date = date.trim();
    		try
		    {
    			value = Integer.valueOf(date).intValue();
		    }
		    catch(NumberFormatException nfe)
		    {
			    log.error("could not create year for date " + date, nfe);
		    }
    	}
    	
    	// assume [188-?] or [c1996]
    	else if( date.length() == 7 )
    	{
    		// assume [c1996]
    		if( date.indexOf('c') != -1)
    		{
    			date = date.replace('c', ' ');
    			date = date.replace('-', '0');
    			date = date.replace('[', ' ');
    			date = date.replace(']', ' ');
    			date = date.replace('?', ' ');
    			date = date.trim();
    			try
      		    {
      		    	value = Integer.valueOf(date).intValue();
      		    }
      		    catch(NumberFormatException nfe)
      		    {
      			    log.error("could not create year for date " + date, nfe);
      		    }
    		}
    		// assume [188-?]
    		else if(date.startsWith("["))
    		{
    			date = date.replace('-', '0');
    			date = date.replace('[', ' ');
    			date = date.replace(']', ' ');
    			date = date.replace('?', ' ');
    			date = date.trim();
    			
    		    try
    		    {
    		    	value = Integer.valueOf(date).intValue();
    		    }
    		    catch(NumberFormatException nfe)
    		    {
    			    log.error("could not create year for date " + date, nfe);
    		    }
    		}
     	}
		return value;
	}
	

	/**
	 * Set the repository service
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	/**
	 * Set the contributor type service.
	 * 
	 * @param contributorTypeService
	 */
	public void setContributorTypeService(
			ContributorTypeService contributorTypeService) {
		this.contributorTypeService = contributorTypeService;
	}
	
	/**
	 * Set the contributor service.
	 * 
	 * @param contributorService
	 */
	public void setContributorService(ContributorService contributorService) {
		this.contributorService = contributorService;
	}
	
	/**
	 * Set the name authority index service.
	 * 
	 * @param nameAuthorityIndexService
	 */
	public void setNameAuthorityIndexService(
			NameAuthorityIndexService nameAuthorityIndexService) {
		this.nameAuthorityIndexService = nameAuthorityIndexService;
	}

	/**
	 * Set the name authority search service.
	 * 
	 * @param nameAuthoritySearchService
	 */
	public void setNameAuthoritySearchService(
			NameAuthoritySearchService nameAuthoritySearchService) {
		this.nameAuthoritySearchService = nameAuthoritySearchService;
	}

	/**
	 * Set the person service.
	 * 
	 * @param personService
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	/**
	 * Set the place of publication service.
	 * 
	 * @param placeOfPublicationService
	 */
	public void setPlaceOfPublicationService(
			PlaceOfPublicationService placeOfPublicationService) {
		this.placeOfPublicationService = placeOfPublicationService;
	}

	/**
	 * Set the publisher service.
	 * 
	 * @param publisherService
	 */
	public void setPublisherService(PublisherService publisherService) {
		this.publisherService = publisherService;
	}
	
	/**
	 * Set the marc content type field mapper service.
	 * 
	 * @param marcContentTypeFieldMapperService
	 */
	public void setMarcContentTypeFieldMapperService(
			MarcContentTypeFieldMapperService marcContentTypeFieldMapperService) {
		this.marcContentTypeFieldMapperService = marcContentTypeFieldMapperService;
	}
	
	/**
	 * Set the language type serivce.
	 * 
	 * @param languageTypeService
	 */
	public void setLanguageTypeService(LanguageTypeService languageTypeService) {
		this.languageTypeService = languageTypeService;
	}
	
	/**
	 * Set the series.
	 * 
	 * @param seriesService
	 */
	public void setSeriesService(SeriesService seriesService) {
		this.seriesService = seriesService;
	}
	
	/**
	 * Set the error email service.
	 * 
	 * @param errorEmailService
	 */
	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}


}
