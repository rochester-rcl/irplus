package edu.ur.ir.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.MarcFactory;
import org.marc4j.marc.Record;

import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.DuplicateContributorException;
import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.ItemContentType;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.item.ItemExtent;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemLink;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.person.BirthDate;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;

public class DefaultMarcRecordCreator implements MarcRecordCreator 
{
	// create a factory instance
	private MarcFactory factory = MarcFactory.newInstance();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy");

	/**
	 * Export a generic item to MARC format.
	 * 
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	public Record export(InstitutionalItemVersion version)
	{
		Record record = factory.newRecord();
		
		// these are specific to thesis
		Leader leader = record.getLeader();
		leader.setTypeOfRecord('a');
		leader.setImplDefined1(new char[]{'m', ' '});
		leader.setImplDefined2(new char[]{'K', 'a', ' '});
		
		char[] _006 = new char[18];
		for( int index = 0; index < _006.length; index++)
		{
			_006[index] = ' ';
		}
		
		// start adding control fields
		_006[0] = 'm';
		_006[9] = 'd';
		ControlField control_006 = factory.newControlField("006", new String(_006));
		record.getControlFields().add(control_006);
		
		char[] _007 = new char[14];
		for( int index = 0; index < _007.length; index++)
		{
			_007[index] = '|';
		}
		_007[0] = 'c';
		_007[1] = 'r';
		ControlField control_007 = factory.newControlField("007", new String(_007));
		record.getControlFields().add(control_007);
		
		
		
		char[] _008 = new char[40];
		for( int index = 0; index < _008.length; index++)
		{
			_008[index] = ' ';
		}
		
		_008[6] = 's';
		String year = "";
		if( version.getDateOfDeposit() != null)
		{
			year = dateFormat.format(version.getDateOfDeposit());
			if( year.length() <= 4 )
			{
			    for( int index = 0; index < year.length(); index++ )
			    {
			        _008[7 + index] = year.charAt(index);
			    }
			}
		}
		
		GenericItem item = version.getItem();
		_008[15] = 'n';
		_008[16] = 'y';
		_008[17] = 'u';
		_008[23] = 's';
		_008[24] = 'b';
		_008[25] = 'm';
		
		if( item.getLanguageType() != null)
		{
			if( item.getLanguageType().getIso639_2() != null && item.getLanguageType().getIso639_2() != null )
			{
		        String language = item.getLanguageType().getIso639_2();
		        if( language.length() > 3 )
		        {
		    	    language = language.substring(0, 2);
		        }
		        
		        for( int index = 0; index < language.length(); index++ )
		        {
		        	_008[35 + index] = language.charAt(index);
		        }
		      
			}
		}
		
		// this is hard coded for new york right now.  will need to be fixed to work with
		// any state / country
		ControlField control_008 = factory.newControlField("008", new String(_008));
		record.getControlFields().add(control_008);
		
		handleContributors(item, record);
		ItemContributor ic = this.getMainAuthor(item);
		handleTitle(item, ic, record);
		
		if( item.isPubliclyViewable() && !item.isEmbargoed() && !version.isWithdrawn())
		{
		    handlePublisher(record, year);
		    handleExtents(item, record);
		    handleDateSubmitted(record, version.getDateOfDeposit());
		    if( item.getDescription() != null && !item.getDescription().trim().equals(""))
		    {
		        handleDescription(record, item.getDescription());
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
		    handleContentTypes(record, item);
		    if( version.getHandleInfo() != null )
		    {
			    if( version.getHandleInfo().getData() != null && !version.getHandleInfo().getData().trim().equals(""))
			    {
				    handleHandleUrl(record, version.getHandleInfo().getData());
			    }
		    }
		}
		return record;
	}
	
	// we will have to create a mapping for this to 
	// work properly
	private void handleExtents(GenericItem item, Record record)
	{
		String numPages = "";
		String illustrations = "";
		
		Set<ItemExtent> extents = item.getItemExtents();
		
		for(ItemExtent extent : extents)
		{
			if(extent.getExtentType().getName().equalsIgnoreCase("Number of Pages"))
			{
				numPages = extent.getValue().trim();
			}
			if( extent.getExtentType().getName().equalsIgnoreCase("Illustrations"))
			{
				illustrations = extent.getValue().trim();
			}
		}
		
		DataField df = factory.newDataField("300", ' ', ' ');
		df.addSubfield(factory.newSubfield('a', "1 online resource " + numPages));
		
		
		if(illustrations != null && !illustrations.equals(""))
		{
			df.addSubfield(factory.newSubfield('b', illustrations));
		}
		record.addVariableField(df);
	}
	
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
	private void handlePublisher(Record record, String year)
	{
		DataField df = factory.newDataField("260", ' ', ' ');
		df.addSubfield(factory.newSubfield('a', "Rochester, N.Y.:"));
		df.addSubfield(factory.newSubfield('b', "University of Rochester"));
		df.addSubfield(factory.newSubfield('c',  year));
		record.addVariableField(df);
	}
	
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
		df.addSubfield(factory.newSubfield('a', leadingArticles + item.getName()));
		df.addSubfield(factory.newSubfield('h', "[electronic resource]:"));
		
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
		    df.addSubfield(factory.newSubfield('c', authorName + ".") );
			record.addVariableField(df);
		}
	
		
	}

	private void handleContributors(GenericItem item, Record record)
	{
		List<ItemContributor> contributors = item.getContributors();
		
		for( int index = 0; index < contributors.size(); index++)
		{
			ItemContributor contributor = contributors.get(index);
			PersonName pn = contributor.getContributor().getPersonName();
			ContributorType ct = contributor.getContributor().getContributorType();
			
			String relatorCode = this.getRelatorCode(ct);
			
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
			// primary contributor
			if( index == 0 )
			{
				DataField df = factory.newDataField("100", '1', ' ');
				df.addSubfield(factory.newSubfield('a', authorName));
				
				
				if( hasBirthYear || hasDeathYear )
				{
					df.addSubfield(factory.newSubfield('d', birthYear + "-" + deathYear));
				}
				
				if( relatorCode != null )
				{
					df.addSubfield(factory.newSubfield('4', relatorCode));
				}
				
				record.addVariableField(df);
			}
			// secondary contributor
			else
			{
				DataField df = factory.newDataField("700", '1', ' ');
				df.addSubfield(factory.newSubfield('a', authorName));
				if( hasBirthYear || hasDeathYear )
				{
					df.addSubfield(factory.newSubfield('d', birthYear + "-" + deathYear));
				}
				if( relatorCode != null )
				{
					df.addSubfield(factory.newSubfield('4', relatorCode));
					
					// this is specific to U of R
					if( relatorCode.equals("ths"))
					{
						DataField df2 = factory.newDataField("500", ' ', ' ');
						df2.addSubfield(factory.newSubfield('a', "Advisor:" + pn.getForename() + " " + pn.getMiddleName() + " " + pn.getSurname()));
					}
				}
				record.addVariableField(df);
			}
		}
		
	}
	
	// this should become a mapping
	private String getRelatorCode(ContributorType contributorType)
	{
		
		if( contributorType.getName().trim().equalsIgnoreCase("Arranger"))
		{
		    return "arr";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Artist"))
		{
		    return "art";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Author"))
		{
		    return "aut";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Choreographer"))
		{
		    return "chr";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Compiler"))
		{
		    return "com";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Composer"))
		{
		    return "cmp";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Donor"))
		{
		    return "dnr";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Editor"))
		{
		    return "edt";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Illustrator"))
		{
		    return "ill";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Librettist"))
		{
		    return "lbt";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Photographer"))
		{
		    return "pht";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Thesis Advisor"))
		{
		    return "ths";	
		}
		if( contributorType.getName().trim().equalsIgnoreCase("Translator"))
		{
		    return "trl";	
		}
		
		return null;
	}
	
	private void handleDateSubmitted(Record record, Date dateSubmitted)
	{
		DateFormat fullDateFormater = new SimpleDateFormat("MM/dd/yyyy");
		
		String date = "";
		
		if( dateSubmitted != null )
		{
			date = fullDateFormater.format(dateSubmitted);
		}
		
		DataField df = factory.newDataField("500", ' ', ' ');
		String value = "Title from PDF of title page (University of Rochester, viewed on " + date + ")";
		df.addSubfield(factory.newSubfield('a', value));
		record.addVariableField(df);
	}
	
	private void handleDescription(Record record, String description)
	{
		
		DataField df = factory.newDataField("502", ' ', ' ');
		df.addSubfield(factory.newSubfield('a', description));
		record.addVariableField(df);
	}
	
	private void handleEmbargo(Record record, Date embargoDate)
	{
		
        DateFormat fullDateFormater = new SimpleDateFormat("MM/dd/yyyy");
		
		String date = "";
		
		if(embargoDate != null )
		{
			date = fullDateFormater.format(embargoDate);
		}
		
		DataField df = factory.newDataField("506", ' ', ' ');
		String value = "Access restricted to current members of the University of Rochester until  " + date + ". Registration with NetID required.";
		df.addSubfield(factory.newSubfield('a', value));
		record.addVariableField(df);
	}
	
	private void handleAbstract(Record record, String itemAbstract)
	{
		DataField df = factory.newDataField("520", ' ', ' ');
		df.addSubfield(factory.newSubfield('3', itemAbstract));
		record.addVariableField(df);
	}
	
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
			if( identifier.getIdentifierType().getName().equals("LCSH"))
			{
				DataField df = factory.newDataField("650", ' ', ' ');
				df.setIndicator2('0');
				df.addSubfield(factory.newSubfield('a', identifier.getValue()));
				record.addVariableField(df);
			}
		}
	}
	
	private void handleKeywords(Record record, String keyWords)
	{
		String[] words = keyWords.split(";");
		for( String word : words)
		{
			DataField df = factory.newDataField("653", ' ', ' ');
			df.addSubfield(factory.newSubfield('a', word));
			record.addVariableField(df);
		}
	}
	
	
	private void handleContentTypes(Record record, GenericItem item)
	{
		for(ItemContentType itemContentType : item.getItemContentTypes() )
		{
			if( itemContentType.getContentType().equals("Thesis"))
			{
				DataField df = factory.newDataField("655", ' ', ' ');
				df.setIndicator2('7');
				
				df.addSubfield(factory.newSubfield('a', "Electronic dissertations."));
				df.addSubfield(factory.newSubfield('2', "lcgft"));
				record.addVariableField(df);
			}
		}
	}
	
	private void handleHandleUrl(Record record, String url)
	{
		DataField df = factory.newDataField("856", '4', '0');
		df.addSubfield(factory.newSubfield('u', url));
		record.addVariableField(df);
	}
	
	
	
	public static void main(String[] args) throws DuplicateContributorException, CollectionDoesNotAcceptItemsException, IOException
	{
		
		Repository repository  = new Repository();
		repository.setDescription("myDescription");
		repository.setName("myName");
		

		// create a new institution
		InstitutionalCollection institutionalCollection = 
			new InstitutionalCollection(repository, "myInstitution");
		
		
	
		GenericItem item = new GenericItem("genericItem");
		
		ContentType contentType = new ContentType("name");
		
		ContributorType contributorType = new ContributorType("Author");
		ContributorType contributorType2 = new ContributorType("Thesis Advisor");
		
		
		PersonName personName = new PersonName();
		personName.setForename("forename");
		personName.setSurname("surname");
		personName.setMiddleName("middle");
		
		PersonNameAuthority authority = new PersonNameAuthority(personName);
		authority.setBirthDate(new BirthDate(1998));
		Contributor contributor = new Contributor();
		contributor.setContributorType(contributorType);
		contributor.setPersonName(personName);
		
		PersonName personName2 = new PersonName();
		personName2.setForename("forename2");
		personName2.setSurname("surname2");
		personName2.setMiddleName("middle2");
		
		PersonNameAuthority authority2 = new PersonNameAuthority(personName2);
		authority2.setBirthDate(new BirthDate(1998));
		Contributor contributor2 = new Contributor();
		contributor2.setContributorType(contributorType2);
		contributor2.setPersonName(personName2);
		
		
		
		
		IdentifierType identifier = new IdentifierType();
		identifier.setName("identifiername");
		
		ItemLink itemLink = item.createLink("name", "url");
		itemLink.setId(5l);
		
		IrUser owner = new IrUser("name", "password");
		
		Sponsor sponsor = new Sponsor("sponserer");
		
		ExtentType numPages = new ExtentType("Number of Pages");
		item.addItemExtent(numPages, "12");
		
		ExtentType illustrations = new ExtentType("Illustrations");
		item.addItemExtent(illustrations, "12");

		
		item.setDescription("description");
		item.setPrimaryContentType(contentType);
		item.addContributor(contributor);
		item.addContributor(contributor2);
		ExternalPublishedItem externalPublishedItem = item.createExternalPublishedItem();
		Publisher publisher = new Publisher("name");
		externalPublishedItem.updatePublishedDate(12,10,2008);
		externalPublishedItem.setPublisher(publisher);
		item.updateFirstAvailableDate(1, 30, 2008);
		item.setId(10l);
		item.setItemAbstract("itemAbstract");
		item.setDescription("Description");
		
		
		item.addItemIdentifier("value", identifier);
		
		item.setItemKeywords("itemKeywords");
		item.addReport(new Series("series", "Series10"), "Report15");
		LanguageType languageType = new LanguageType("English");
		languageType.setIso639_2("eng");
		item.setLanguageType(languageType);
		item.addLink(itemLink);
		item.setName("Itemname");
		item.setLeadingNameArticles("a");
		item.setOwner(owner);
		item.setPublishedToSystem(true);
		item.updateOriginalItemCreationDate(10,25,2008);
		item.setReleaseDate(new Date());
		item.addItemSponsor(sponsor);
		item.addSubTitle("Title 2", "The");
		
		InstitutionalItem instItem = institutionalCollection.createInstitutionalItem(item);
		
		MarcRecordCreator marcExport = new DefaultMarcRecordCreator();
		
		
		
		Record record = marcExport.export(instItem.getVersionedInstitutionalItem().getCurrentVersion());
		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MarcWriter writer = new MarcXmlWriter(baos, "utf-8");	
		writer.write(record);
		writer.close(); 
		
		
		String value = baos.toString("utf-8");
		System.out.println(value);
		
		baos = new ByteArrayOutputStream();
		writer = new MarcStreamWriter(baos);
		writer.write(record);
		writer.close(); 
		
		
		value = baos.toString();
		System.out.println(value);
		
		
		
	}
}