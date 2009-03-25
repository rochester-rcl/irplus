/**  
   Copyright 2008 University of Rochester

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

package edu.ur.dspace.load;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import edu.ur.dspace.model.DspaceItem;
import edu.ur.dspace.model.DspaceMetadataLabel;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.SearchResults;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeService;
import edu.ur.ir.item.DuplicateContributorException;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.FirstAvailableDate;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeService;
import edu.ur.ir.item.ItemSponsor;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeService;
import edu.ur.ir.item.OriginalItemCreationDate;
import edu.ur.ir.item.PublishedDate;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherService;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.SeriesService;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorService;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorService;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeService;
import edu.ur.ir.person.NameAuthorityIndexService;
import edu.ur.ir.person.NameAuthoritySearchService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;


/**
 * Handles setting up a generic item for inserting items into the repository.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGenericItemPopulator implements GenericItemPopulator{

	/**  Service for dealing with institutional collections */
	private UserService userService;
	
	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;
	
	/** Helps to split out person names in the data */
	private AuthorNameSplitter authorNameSplitter;
	
	/** Search service for person names */
	private NameAuthoritySearchService nameAuthoritySearchService;
	
	/** Service for indexing names */
	private NameAuthorityIndexService nameAuthorityIndexService;
	
	/** Service for dealing with contributor types */
	private ContributorTypeService contributorTypeService;
	
	/** Service for dealing with contributors */
	private ContributorService contributorService;
	
	/** Service for dealing with identifier type's */
	private IdentifierTypeService identifierTypeService;
	
	/** Service for dealing with language types */
	private LanguageTypeService languageTypeService;
	
	/** Service for dealing with publishers */
	private PublisherService publisherService;
	
	/** Service for dealing with person information */
	private PersonService personService;
	
	/** Service for dealing with series */
	private SeriesService seriesService;
	
	/** Service for dealing with content types */
	private ContentTypeService contentTypeService;
	
	/** Splitter for series report numbers */
	private SeriesReportNumberSplitter seriesReportNumberSplitter = new SeriesReportNumberSplitter();
	
	/** Service for dealing with sponsors */
	private SponsorService sponsorService;
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(DefaultGenericItemPopulator.class);
	
	/**
	 * Data source for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * Create and retun a generic item.
	 * @throws NoIndexFoundException 
	 * 
	 * @see edu.ur.dspace.load.GenericItemPopulator#createGenericItem(edu.ur.dspace.model.DspaceItem)
	 */
	public GenericItem createGenericItem(Repository repository, DspaceItem dspaceItem) throws NoIndexFoundException {
		
		log.debug("\n \n ***creating generic item for dspace item " + dspaceItem + "***");
		

		
		// submitter from the system
		Long urresearchUserId = getUrResearchUser(dspaceItem.submitterId);
		
		String title  = dspaceItem.getSingleDataForLabel("title");
		log.debug("processing item title found " + title);
		if( title == null )
		{
			throw new IllegalStateException("Title for item " + dspaceItem + " is null " );
		}
		
		GenericItem genericItem = new GenericItem(title);
		
		IrUser submitter = userService.getUser(urresearchUserId, false);
		log.debug("Submitter found = " + submitter);
		if( submitter != null )
		{
		    genericItem.setOwner(submitter);
		}
		
		
		processSubjectKeywords(dspaceItem, genericItem);
		
		// both contributors and authors will be processed in the same way
		processAuthors(repository, dspaceItem, genericItem, DspaceMetadataLabel.AUTHORS);
		processAuthors(repository, dspaceItem, genericItem, DspaceMetadataLabel.CONTRIBUTORS);
		genericItem.setItemAbstract(dspaceItem.getSingleDataForLabel(DspaceMetadataLabel.ABSTRACT));
		genericItem.setFirstAvailableDate(getFirstAvailableDate(dspaceItem));
		genericItem.setOriginalItemCreationDate(getOriginalCreationDate(dspaceItem)); 
	    String description = dspaceItem.getSingleDataForLabel(DspaceMetadataLabel.DESCRIPTION);
	    genericItem.setDescription(description);
	    processIdentifiers(dspaceItem, genericItem, "GOVT_DOC", DspaceMetadataLabel.GOVERNMENT_DOC_NO);
	    processIdentifiers(dspaceItem, genericItem, "ISBN", DspaceMetadataLabel.ISBN);
	    processIdentifiers(dspaceItem, genericItem, "LCSH", DspaceMetadataLabel.LCSH);
	    processIdentifiers(dspaceItem, genericItem, "MESH", DspaceMetadataLabel.MESH);
	    processIdentifiers(dspaceItem, genericItem, "IS_PART_OF", DspaceMetadataLabel.RELATION_IS_PART_OF);
	    processIdentifiers(dspaceItem, genericItem, "UNKNOWN_IDENTIFIER", DspaceMetadataLabel.OTHER_IDENTIFIER);
	    processLanguages(dspaceItem, genericItem, DspaceMetadataLabel.LANGUAGE);
	    processLanguages(dspaceItem, genericItem, DspaceMetadataLabel.LANGUAGE_NON_ISO);
	    processOtherTitles(dspaceItem, genericItem);
		processExternalPublishingData(dspaceItem, genericItem);
		processSeriesReportNo(dspaceItem, genericItem);
		processUris(dspaceItem, genericItem);
		processItemType(dspaceItem, genericItem);
		processSponsors(dspaceItem, genericItem);
		

		return genericItem;
	}
	
	
	/**
	 * Process a series and report number combination
	 * @param dspaceItem
	 * @param genericItem
	 */
	private void processSeriesReportNo(DspaceItem dspaceItem, GenericItem genericItem)
	{
		log.debug("Processing series report numbers");
		String seriesReportNumber = dspaceItem.getSingleDataForLabel(DspaceMetadataLabel.SERIES_REPORT_NO);
	    List<String> seriesReportParts = seriesReportNumberSplitter.splitSeriesReport(seriesReportNumber, ";");
	    
	    log.debug("Series report parts size = " + seriesReportParts.size());
	    
	    Series series = null;
	    if(seriesReportParts.size() >= 1 )
	    {
	    	String seriesValue = seriesReportParts.get(0);
	    	log.debug("Series found = " + seriesValue);
	    	series = seriesService.getSeries(seriesValue);
	    	if( series == null )
	    	{
	    	    series = new Series(seriesValue);
	    	    seriesService.saveSeries(series);
	    	}
	    	
	    	String reportNumber = "";
	    	if(seriesReportParts.size() >= 2 )
	  	    {
	  	        reportNumber = seriesReportParts.get(1);
	  	    }
	    	log.debug("reportNumber = " + reportNumber);
	    	genericItem.addReport(series, reportNumber);
	    }
	}
	
	/**
	 * Process a series and report number combination
	 * @param dspaceItem
	 * @param genericItem
	 */
	private void processSponsors(DspaceItem dspaceItem, GenericItem genericItem)
	{
		log.debug("Processing sponsors");
		String sponsor = dspaceItem.getSingleDataForLabel(DspaceMetadataLabel.SPONSORS);
		
		if( sponsor != null )
		{
			sponsor = sponsor.trim();
			// get the first space assume that first word
			// is sponsor and the rest is the description of the
			// sponsor contribution
		    int firstSpaceIndex = sponsor.indexOf(' ');
			String description = null;
			String sponsorName = sponsor;
		    
			if(firstSpaceIndex != -1)
		    {
		    	description = sponsor.substring(firstSpaceIndex);
		    	sponsorName = sponsor.substring(0, firstSpaceIndex);
		    }
		    
		    Sponsor theSponsor = sponsorService.getSponsor(sponsorName);
		    if( theSponsor == null )
	        {
	            theSponsor = new Sponsor(sponsorName);
	            sponsorService.saveSponsor(theSponsor);
	        }
	        ItemSponsor itemSponsor = genericItem.addItemSponsor(theSponsor);
	        
	        if( description != null )
	        {
	            itemSponsor.setDescription(description);
	        }
		}
	}
	
	/**
	 * Process a series and report number combination
	 * 
	 * @param dspaceItem - the dspace item containing the date
	 * @param genericItem - the generic item to be built.
	 */
	private void processUris(DspaceItem dspaceItem, GenericItem genericItem)
	{
		log.debug("processing uri's");
		List<String> uris = dspaceItem.getMultipleDataForLabel(DspaceMetadataLabel.URI);
		
		for(String uri : uris)
		{
			log.debug("Found uri " + uri);
			//we've found a handle
			// it should be in the form: http://hdl.handle.net/[name-authority]/[local-name]
			if( uri.indexOf("http://hdl.handle.net/") > -1 )
			{
				// do nothing no processing of handles yet
			}
			else
			{
				IdentifierType identifierType = identifierTypeService.getByUniqueSystemCode("URI");
				if(identifierType == null)
				{
					throw new IllegalStateException("Identifier type for system code HANDLE could not be found");
				}
				log.debug("Uri found " + uri);
				genericItem.addItemIdentifier(uri, identifierType);
			}
		}
        
	}
	
	/**
	 * Process the externally published data.
	 * 
	 * @param dspaceItem
	 * @param genericItem
	 */
	private void processExternalPublishingData(DspaceItem dspaceItem, GenericItem genericItem)
	{
		log.debug("Processing external publishing data");
	    Publisher publisher = getPublisher(dspaceItem);
	   
	    String citation = dspaceItem.getSingleDataForLabel(DspaceMetadataLabel.CITATION);
	    log.debug("citation = " + citation);
	    PublishedDate publishedDate = getPublishedDate(dspaceItem);
	    log.debug("Published date = " + publishedDate);
	    if( publishedDate != null || publisher != null || citation != null && !citation.trim().equals(""))
	    {
	    	ExternalPublishedItem externalPublishedItem = genericItem.getExternalPublishedItem();
			if( externalPublishedItem == null )
			{
				externalPublishedItem = new ExternalPublishedItem();
			}
			
			if( publisher != null)
		    {
				externalPublishedItem.setPublishedDate(publishedDate);
		    }
			
			if( publishedDate != null)
			{
				externalPublishedItem.setPublishedDate(publishedDate);
			}
			
			if( citation != null && !citation.trim().equals(""))
			{
				externalPublishedItem.setCitation(citation);
			}
		
	    }
	}
	
	/**
	 * First avaliable date for dspace data - returns null if no available date exists or the parsing 
	 * of the date failed.
	 * 
	 * @param dspaceItem
	 * @return the first available date or null if the date does not exist
	 */
	private FirstAvailableDate getFirstAvailableDate(DspaceItem dspaceItem)
	{
		log.debug("Getting first available date");
		FirstAvailableDate firstAvailableDate = null;
		String dspaceDate = dspaceItem.getSingleDataForLabel(DspaceMetadataLabel.AVAILABLE_DATE);
		
		if(dspaceDate != null && !dspaceDate.trim().equals(""))
		{
			log.debug("First available date = " + dspaceDate);
		    try {
		    	firstAvailableDate = new FirstAvailableDate();
			    GregorianCalendar calendar = parseDate(dspaceDate);
			    if( calendar != null )
			    {
			        firstAvailableDate.setDay(calendar.get(Calendar.DAY_OF_MONTH));
			        firstAvailableDate.setMonth(calendar.get(Calendar.MONTH));
			        firstAvailableDate.setYear(calendar.get(Calendar.YEAR));
			    }
			    
		    } catch (ParseException e) {
			    log.debug("Could not parse available date " + dspaceDate);
		    }
		}
		return firstAvailableDate;
	}
	
	/**
	 * First avaliable date for dspace data - returns null if no available date exists or the parsing 
	 * of the date failed.
	 * 
	 * @param dspaceItem
	 * @return the first available date or null if the date does not exist
	 */
	private OriginalItemCreationDate getOriginalCreationDate(DspaceItem dspaceItem)
	{
		log.debug("Getting original creation date date");
		OriginalItemCreationDate originalItemCreationDate = null;
		String dspaceDate = dspaceItem.getSingleDataForLabel(DspaceMetadataLabel.CREATION_DATE);
		
		if(dspaceDate != null && !dspaceDate.trim().equals(""))
		{
			log.debug("creation date = " + dspaceDate);
		    try {
		    	originalItemCreationDate = new OriginalItemCreationDate();
			    GregorianCalendar calendar = parseDate(dspaceDate);
			    if( calendar != null )
			    {
			    	originalItemCreationDate.setDay(calendar.get(Calendar.DAY_OF_MONTH));
			    	originalItemCreationDate.setMonth(calendar.get(Calendar.MONTH));
			    	originalItemCreationDate.setYear(calendar.get(Calendar.YEAR));
			    }
			    
		    } catch (ParseException e) {
			    log.debug("Could not parse creation date " + dspaceDate);
		    }
		}
		return originalItemCreationDate;
	}

	
	/**
	 * Process the publisher data in the dspace item.
	 * 
	 * @param item
	 * @param genericItem
	 */
	private Publisher getPublisher(DspaceItem item)
	{
		Publisher publisher = null;
		log.debug("Getting publisher");
		String publisherName = item.getSingleDataForLabel(DspaceMetadataLabel.PUBLISHER);
		
		if( publisherName != null )
		{
		    publisher = publisherService.getPublisher(publisherName);
		
		    log.debug("Publisher found = " + publisher);
		
	 	    if( publisher == null)
		    {
			    log.debug("No publisher found creating publisher " + publisherName);
			    publisher = new Publisher(publisherName);
		    }
		}
		
		return publisher;
	}
	
	/**
	 * Process the languages
	 * 
	 * @param item - dspace item that has the languages
	 * @param genericItem - generic item that is to be created
	 * @param dspaceMetadatalabel - the metadata label
	 */
	private void processLanguages(DspaceItem item, GenericItem genericItem, String dspaceMetadatalabel)
	{
		log.debug("publishing languages");
		List<String> languages = item.getMultipleDataForLabel(dspaceMetadatalabel);
		
		for(String language : languages)
		{
			log.debug("Processing lanaguge " + language);
			if( language.equalsIgnoreCase("en_US") || language.equalsIgnoreCase("en") ||
					language.equals("english"))
			{
				LanguageType l = languageTypeService.getByUniqueSystemCode("ENG");
				genericItem.setLanguageType(l);
			}
			else if( language.equalsIgnoreCase("fr"))
			{
				LanguageType l = languageTypeService.getByUniqueSystemCode("FRE");
				genericItem.setLanguageType(l);
			}
			else if( language.equalsIgnoreCase("de"))
			{
				LanguageType l = languageTypeService.getByUniqueSystemCode("GER");
				genericItem.setLanguageType(l);
			}
			else if( language.equalsIgnoreCase("ja"))
			{
				LanguageType l = languageTypeService.getByUniqueSystemCode("JPN");
				genericItem.setLanguageType(l);
			}
			else if(language.equalsIgnoreCase("it"))
			{
				LanguageType l = languageTypeService.getByUniqueSystemCode("ITA");
				genericItem.setLanguageType(l);
			}
			else if( language.equalsIgnoreCase("es"))
			{
				LanguageType l = languageTypeService.getByUniqueSystemCode("SPA");
				genericItem.setLanguageType(l);
			}
			else
			{
				log.debug( "Could not process language " + language);
			}
		    
		}
	}
	
	/**
	 * Process the identifier 
	 * 
	 * @param item - dspace item that conatins the information
	 * @param genericItem - Generic item to add the data to
	 * @param uniqueTypeSystemCode - identifier type unique system code
	 * @param dspaceMetadatalabel - Metadata label given by dspace
	 */
	private void processIdentifiers(DspaceItem item, GenericItem genericItem, String uniqueTypeSystemCode, String dspaceMetadatalabel)
	{
		log.debug("Processing identifiers uniqueTypeSystemCode = " + uniqueTypeSystemCode + " dspaceMetadatalabel = " + dspaceMetadatalabel);
		IdentifierType identifierType = identifierTypeService.getByUniqueSystemCode(uniqueTypeSystemCode);
		if(identifierType == null)
		{
			throw new IllegalStateException("Identifier type for system code " + uniqueTypeSystemCode + " could not be found");
		}
		
        List<String> values = item.getMultipleDataForLabel(dspaceMetadatalabel);
		
		for(String value : values)
		{
			log.debug("Processing value " + value);
		    genericItem.addItemIdentifier(value, identifierType);
		}
	}
	

	
	/**
	 * Get the item types
	 * 
	 * @param item - item to add the type to
	 * @param genericItem - the generic item
	 * @param label - label for the item
	 */
	private void processItemType( DspaceItem item, GenericItem genericItem)
	{
		log.debug("Processing item type");
		List<String> itemTypes = item.getMultipleDataForLabel(DspaceMetadataLabel.TYPE);
		
		
		for(String type : itemTypes)
		{
			log.debug("found item type " + type);
			
			ContentType contentType = null;
			
			if( type.equals("Thesis") )
			{
				genericItem.setThesis(true);
			}
			else if( type.equalsIgnoreCase("Thesis, Technical Report"))
			{
				genericItem.setThesis(true);
			    contentType = contentTypeService.getByUniqueSystemCode("TECHNICAL_REPORT");
			}
			else if( type.equalsIgnoreCase("Article"))
			{
			    contentType = contentTypeService.getByUniqueSystemCode("ARTICLE");
				
			}
			else if( type.equalsIgnoreCase("Book"))
			{
				 contentType = contentTypeService.getByUniqueSystemCode("BOOK");
			}
			else if( type.equalsIgnoreCase("Book chapter"))
			{
				 contentType = contentTypeService.getByUniqueSystemCode("BOOK_CHAPTER");
			}
			else if( type.equalsIgnoreCase("Dataset"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("DATASET");
			}
			else if( type.equalsIgnoreCase("Form"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("FORM");
			}
			else if( type.equalsIgnoreCase("Image"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("IMAGE");
			}
			else if( type.equalsIgnoreCase("Learning object"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("LEARNING_OBJECT");
			}
			else if( type.equalsIgnoreCase("Musical Score"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("MUSICAL_SCORE");
			}
			else if( type.equalsIgnoreCase("Other"))
			{
				// do nothing
			}
			else if( type.equalsIgnoreCase("Preprint"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("PREPRINT");
			}
			else if( type.equalsIgnoreCase("Presentation"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("PRESENTATION");
			}
			else if( type.equalsIgnoreCase("Recording,musical"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("RECORDING_MUSICAL");
			}
			else if( type.equalsIgnoreCase("Recording,oral"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("RECORDING_ORAL");
			}
			else if( type.equalsIgnoreCase("Software"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("SOFTWARE");
			}
			else if( type.equalsIgnoreCase("Technical Report"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("TECHNICAL_REPORT");
			}
			else if( type.equalsIgnoreCase("Video"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("VIDEO");
			}
			else if( type.equalsIgnoreCase("Working Paper"))
			{
				contentType = contentTypeService.getByUniqueSystemCode("WORKING_PAPER");
			}
			
			if( contentType != null)
			{
				if(genericItem.getPrimaryContentType() == null)
				{
					log.debug("adding primary content type " + contentType);
					genericItem.setPrimaryContentType(contentType);
				}
				else
				{
					if( !genericItem.getPrimaryContentType().equals(contentType))
					{
						log.debug("adding secondary content type " + contentType);
					    genericItem.addSecondaryContentType(contentType);
					}
				}
			}
		}
		
	}
	
	/**
	 * Get the published date for the dspace item.
	 * 
	 * @param dspaceItem
	 * @return
	 */
	private PublishedDate getPublishedDate(DspaceItem dspaceItem)
	{
		log.debug("Processing publish date");
		PublishedDate publishedDate = null;
		String dspaceDate = dspaceItem.getSingleDataForLabel(DspaceMetadataLabel.DATE_OF_ISSUE);
		
		log.debug("Publish date = " + dspaceDate);
		
		if(dspaceDate != null && !dspaceDate.trim().equals(""))
		{
		    try {
		    	publishedDate = new PublishedDate();
			    GregorianCalendar calendar = parseDate(dspaceDate);
			    if( calendar != null )
			    {
			        publishedDate.setDay(calendar.get(Calendar.DAY_OF_MONTH));
			        publishedDate.setMonth(calendar.get(Calendar.MONTH));
			        publishedDate.setYear(calendar.get(Calendar.YEAR));
			    }
			    
		    } catch (ParseException e) {
			    log.debug("Could not parse available date " + dspaceDate);
		    }
		}
		return publishedDate;
	}

	
	/**
	 * Gets the other titles for an item
	 * 
	 * @param item
	 * @param genericItem
	 */
	private void processOtherTitles(DspaceItem item, GenericItem genericItem)
	{
		log.debug("Processing other titles");
		List<String> otherTitles = item.getMultipleDataForLabel(DspaceMetadataLabel.OTHER_TITLES);
	
		for(String title : otherTitles)
		{
			log.debug("adding title " + title);
		    genericItem.addSubTitle(title);
		}
	}
	
	/**
	 * Gets the keywords for an item.
	 * 
	 * @param item
	 * @param genericItem
	 */
	private void processSubjectKeywords(DspaceItem item, GenericItem genericItem)
	{
		log.debug("Processing subject keywords ");
		List<String> keywords = item.getMultipleDataForLabel(DspaceMetadataLabel.SUBJECT_KEYWORDS);
		String keywordList = "";
		Iterator<String> iter = keywords.iterator();
		
		while(iter.hasNext())
		{
			keywordList = keywordList + iter.next();
			log.debug("keyword list = " + keywordList);
			if( iter.hasNext())
			{
				keywordList = keywordList + ", ";
			}
		}
		
		if( !keywordList.equals(""))
		{
			genericItem.setItemKeywords(keywordList);
		}
		
	}
	
	/**
	 * Gets the authors for an item.
	 * 
	 * @param item
	 * @param genericItem
	 * @throws NoIndexFoundException 
	 */
	private void processAuthors(Repository repository, DspaceItem item, GenericItem genericItem, String label) throws NoIndexFoundException
	{
		log.debug("Processing authors label = " + label);
        ContributorType authorContributorType;
		authorContributorType = contributorTypeService.getByUniqueSystemCode("AUTHOR");
		
		if(authorContributorType == null)
		{
			throw new IllegalStateException("Author contributor type could not be found");
		}
		
		List<String> authorNames = item.getMultipleDataForLabel(label);
		List<PersonNameAuthority> personNameAuthorities = new LinkedList<PersonNameAuthority>();
		
		for(String authorName : authorNames)
		{
			log.debug("Processing author name" + authorName);
			PersonNameAuthority authority= authorNameSplitter.splitName(authorName);
			log.debug("Created authority " + authority);
			personNameAuthorities.add(authority);
		}
		
		// get the name list and find any existing authors
		List<PersonNameAuthority> finalNameList = getFinalAuthorList(repository, personNameAuthorities);
		
		for(PersonNameAuthority authority : finalNameList)
		{
			log.debug("Adding authority " + authority);
			if( authority.getId() == null || authority.getId() == 0 )
			{
				log.debug("save called");
				personService.save(authority);
				if( authority.getId() == null || authority.getId() < 1 )
				{
					throw new IllegalStateException("authority id not assigned");
				}
				nameAuthorityIndexService.addToIndex(authority, new File(repository.getNameIndexFolder().getFullPath()));
			}
		}
		
		for(PersonNameAuthority nameAuthority : finalNameList)
		{
			Contributor contributor = contributorService.get(nameAuthority.getAuthoritativeName(), authorContributorType);
			log.debug("Contributor found = " + contributor);
			if( contributor == null )
			{
				log.debug("creating new contributor");
				contributor = new Contributor();
			    contributor.setContributorType(authorContributorType);
			    contributor.setPersonName(nameAuthority.getAuthoritativeName());
			}
			try {
				log.debug("existing contributor used");
				genericItem.addContributor(contributor);
			} catch (DuplicateContributorException e) {
				log.debug("did not add contributor " + contributor + " because of error ", e);
			}
		}

	}
	
	/**
	 * Get the final list of authors. This searches the existing list of authors.  If one
	 * is found it is used instead of the created name authority.
	 * 
	 * @param createdNameAuthorities
	 * @return
	 */
	private List<PersonNameAuthority> getFinalAuthorList(Repository repository, List<PersonNameAuthority> createdNameAuthorities)
	{
		log.debug("Get final author list");
		List<PersonNameAuthority> finalNameAuthorities = new LinkedList<PersonNameAuthority>();
		for(PersonNameAuthority name : createdNameAuthorities)
		{
			String query  = buildAuthorityNameQuery(name);
			log.debug("query = " + query);
			SearchResults<PersonNameAuthority> results = nameAuthoritySearchService.search(new File(repository.getNameIndexFolder().getFullPath()), query, 0, 5);
	        log.debug("search results size = " + results.getNumberObjects());
			PersonNameAuthority finalName = name;
			if( results.getNumberObjects() > 0  )
	        {
	        	List<PersonNameAuthority> foundNames = results.getObjects();
	        	
	        	boolean found = false;
	        	Iterator<PersonNameAuthority> iterator = foundNames.iterator();
	        	while(!found && iterator.hasNext())
	        	{
	        		PersonNameAuthority foundName = iterator.next();
	        		log.debug("comparing found name " + foundName + " to name " + name);
	        		if( foundName!= null && foundName.softEquals(name))
	        		{
	        			log.debug("Name found!");
	        			found = true;
	        			finalName = foundName;
	        		}
	        	}
	        }
			log.debug("adding final name " + finalName);
			finalNameAuthorities.add(finalName);
		}
		
		return finalNameAuthorities;
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
	 *  Handles a date in the format yyyy-MM-ddTHH:MM:SSZ
	 *  
	 * @param dspaceDate
	 * @return
	 * @throws ParseException 
	 */
	private GregorianCalendar parseDate(String dspaceDate) throws ParseException
	{
		GregorianCalendar gregorianCalendar = null;
		
		log.debug("Tring to parse date " + dspaceDate);
		// split on the name
		String[] dateParts = dspaceDate.split("T");
		
		log.debug( " dateParts.length = " + dateParts.length); 
	
		if( dateParts.length >= 2 )
		{
		     String yearMonthDay = dateParts[0];
		    //remove the Z
		    String hourMinuteSecond = dateParts[1].substring(0, dateParts[1].length() -1);
		
		    String dateAndTime = yearMonthDay + " " +  hourMinuteSecond;
		    log.debug("parsing date " + dateAndTime);
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    gregorianCalendar = new GregorianCalendar();
		    gregorianCalendar.setTime(simpleDateFormat.parse(dateAndTime));
		}
		
		return gregorianCalendar;
	}
	
	
	

	/**
	 * Get the urresearch user id for the current dspace eperson id.  The takes the old dspace eperson id
	 * 
	 * @param dspaceCommunityParent
	 * @return
	 */
	private Long getUrResearchUser(long epersonId)
	{
		return jdbcTemplate.queryForLong("select ur_research_user_id from dspace_convert.ir_user where dspace_eperson_id = " + epersonId);
	}

	public AuthorNameSplitter getAuthorNameSplitter() {
		return authorNameSplitter;
	}

	public void setAuthorNameSplitter(AuthorNameSplitter authorNameSplitter) {
		this.authorNameSplitter = authorNameSplitter;
	}

	public NameAuthoritySearchService getNameAuthoritySearchService() {
		return nameAuthoritySearchService;
	}

	public void setNameAuthoritySearchService(
			NameAuthoritySearchService nameAuthoritySearchService) {
		this.nameAuthoritySearchService = nameAuthoritySearchService;
	}

	public ContributorTypeService getContributorTypeService() {
		return contributorTypeService;
	}

	public void setContributorTypeService(
			ContributorTypeService contributorTypeService) {
		this.contributorTypeService = contributorTypeService;
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public ContributorService getContributorService() {
		return contributorService;
	}

	public void setContributorService(ContributorService contributorService) {
		this.contributorService = contributorService;
	}

	public IdentifierTypeService getIdentifierTypeService() {
		return identifierTypeService;
	}

	public void setIdentifierTypeService(IdentifierTypeService identifierTypeService) {
		this.identifierTypeService = identifierTypeService;
	}

	public LanguageTypeService getLanguageTypeService() {
		return languageTypeService;
	}

	public void setLanguageTypeService(LanguageTypeService languageTypeService) {
		this.languageTypeService = languageTypeService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public PublisherService getPublisherService() {
		return publisherService;
	}

	public void setPublisherService(PublisherService publisherService) {
		this.publisherService = publisherService;
	}

	public SeriesService getSeriesService() {
		return seriesService;
	}

	public void setSeriesService(SeriesService seriesService) {
		this.seriesService = seriesService;
	}

	public NameAuthorityIndexService getNameAuthorityIndexService() {
		return nameAuthorityIndexService;
	}

	public void setNameAuthorityIndexService(
			NameAuthorityIndexService nameAuthorityIndexService) {
		this.nameAuthorityIndexService = nameAuthorityIndexService;
	}

	public ContentTypeService getContentTypeService() {
		return contentTypeService;
	}

	public void setContentTypeService(ContentTypeService contentTypeService) {
		this.contentTypeService = contentTypeService;
	}

	public SponsorService getSponsorService() {
		return sponsorService;
	}

	public void setSponsorService(SponsorService sponsorService) {
		this.sponsorService = sponsorService;
	}
	
}
