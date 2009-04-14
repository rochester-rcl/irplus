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

package edu.ur.ir.item;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.util.FileUtil;

/**
 * Test the Item Class
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class ItemTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**  Logger */
	protected static final Logger log = Logger.getLogger(ItemTest.class);
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		GenericItem item  = new GenericItem("myName");
		item.setDescription("myDescription");

		ContentType contentType = new ContentType();
		contentType.setName("Book");
		
		item.setPrimaryContentType(contentType);
		
		IdentifierType identifierType = new IdentifierType();
		identifierType.setName("isbn");
	
		ItemIdentifier itemIdentifier = item.addItemIdentifier("4444-55-6644", identifierType);

		FirstAvailableDate dateFirstAvailable = item.addFirstAvailableDate(12, 1, 1990);
		 
		assert item.getDescription().equals("myDescription") : "Descriptions should be equal";
		assert item.getName().equals("myName") : "Names should be equal";
		assert item.getFirstAvailableDate().equals(dateFirstAvailable) : "Accessioned Dates should be equal ";
		

		
		assert item.getPrimaryContentType().equals(contentType) : "content type should equal " + contentType + 
		"but equals " + item.getPrimaryContentType();
		
		assert item.getItemIdentifiers().contains(itemIdentifier) : "Item should contain item identifier";
		assert item.removeItemIdentifier(itemIdentifier) : "Identifier should be removed";
		assert !item.getItemIdentifiers().contains(itemIdentifier) : "Item should Not contain item identifier";

	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
		GenericItem item1  = new GenericItem("name1");
		GenericItem item2  = new GenericItem("name2");
		GenericItem item3  = new GenericItem("name1");
		
		assert item1.equals(item3): "Items should be the same";
		assert !item2.equals(item1): "The items should not be the same";
		assert item1.hashCode() == item3.hashCode() : "Hash codes should be the same";
		assert item2.hashCode() != item3.hashCode() : "Hash codes should not be the same";
	}
	
	/**
	 * Test getting a file from an item
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testGetFile() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {

		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_core_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		// create the first file to store in the temporary folder
		File f = testUtil.creatFile(directory, "testFile",
				"Hello  - ItemFile This is text in a file"); 
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();

		FileInfo fileInfo1 = fd.addFile(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");

		// create a collection
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");

		GenericItem item = new GenericItem("item1");
		
		IrFile irFile = new IrFile(fileInfo1, "myNewFile");
		
		item.addFile(irFile);
		ItemFile info = item.getItemFile("myNewFile");
		assert info.getIrFile().equals(irFile) : "file should be equal";
		
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test getting a primary image file to an item
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testAddingPrimaryImageFile() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {

		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_core_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		// create the first file to store in the temporary folder
		File f = testUtil.creatFile(directory, "testFile",
				"Hello  - ItemFile This is text in a file"); 
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();

		FileInfo fileInfo1 = fd.addFile(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");

		// create a collection
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");

		GenericItem item = new GenericItem("item1");
		
		IrFile irFile = new IrFile(fileInfo1, "myNewFile");
		
		item.addFile(irFile);
		ItemFile info = item.getItemFile("myNewFile");
		assert item.addPrimaryImageFile(info) == true : "should be able to add primary image file " + info;
		assert info.getIrFile().equals(irFile) : "file should be equal";
		
		item.removeItemFile(info);
		assert item.getPrimaryImageFile() == null : "Primary image file should be set to null";
		assert item.addPrimaryImageFile(info) == false : "should not be able to set a file that is not part of the set of files";
		
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test adding and removing contributors to an item.
	 * @throws DuplicateContributorException 
	 */
	public void contributorTest() throws DuplicateContributorException
	{
		GenericItem item1  = new GenericItem("generic item");
		ContributorType ct = new ContributorType("ctName");
		ct.setDescription("ctDescription");
		ct.setId(55l);
		ct.setVersion(33);
		
		PersonName name1 = new PersonName();
		name1.setFamilyName("familyName");
		name1.setForename("forename");
		name1.setInitials("n.d.s");
		name1.setMiddleName("MiddleName");
		name1.setNumeration("III");
		name1.setSurname("surname");
		
		Contributor contrib = new Contributor();
		contrib.setContributorType(ct);
		contrib.setPersonName(name1);
		
		ItemContributor itemContributor = item1.addContributor(contrib);
		
		assert item1.getContributors().contains(itemContributor) : "Item one should contain the contributor";
		assert item1.getContributors().size() == 1 : "Should have only one contributor";
		
		
		PersonName name2 = new PersonName();
		name2.setFamilyName("familyName2");
		name2.setForename("forename2");
		name2.setInitials("n.d.s2");
		name2.setMiddleName("MiddleName2");
		name2.setNumeration("III2");
		name2.setSurname("surname2");
		
		Contributor contrib2 = new Contributor();
		contrib2.setContributorType(ct);
		contrib2.setPersonName(name2);
		

		ItemContributor itemContributor2 = item1.addContributor(contrib2);
		itemContributor2.setId(44l);

		assert item1.getContributors().contains(itemContributor2) : "Item one should contain the contributor";
		assert item1.getContributors().size() == 2 : "Should have two contributors";
		
		assert item1.removeContributor(itemContributor) : "Should be able to remove contributor";
		
		assert item1.getContributors().size() == 1 : "Should only have one contributor again";
		assert item1.getContributors().contains(itemContributor2) : "Should contain contributor 2 still";
		
		assert item1.getContributor(44l).equals(itemContributor2) : "Should be able to find contrib2";
	}
	

	
	/**
	 * Item Clone test
	 * @throws DuplicateContributorException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void itemCloneTest() throws IllegalFileSystemNameException, DuplicateContributorException, LocationAlreadyExistsException {

		
		GenericItem item = new GenericItem("genericItem");
		
		ContentType contentType = new ContentType("name");
		
		ContributorType contributorType = new ContributorType("author");
		PersonName personName = new PersonName();
		personName.setForename("forename");
		Contributor contributor = new Contributor();
		contributor.setContributorType(contributorType);
		contributor.setPersonName(personName);
		
		ExternalPublishedItem externalPublishedItem = new ExternalPublishedItem();
		Publisher publisher = new Publisher();
		publisher.setName("name");
		externalPublishedItem.setPublishedDate(new PublishedDate(12,10,2008));
		externalPublishedItem.setPublisher(publisher);
		
		IdentifierType identifier = new IdentifierType();
		identifier.setName("identifiername");
		
		ItemLink itemLink = new ItemLink(item, "name", "url");
		itemLink.setId(5l);
		
		IrUser owner = new IrUser("name", "password");
		
		Sponsor sponsor = new Sponsor();
		sponsor.setName("sponserer");
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_core_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		// create the first file to store in the temporary folder
		File f = testUtil.creatFile(directory, "testFile",
				"Hello  -  This is text in a test file"); 
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();
		
		FileInfo fileInfo1 = fd.addFile(f, "testFile");
		fileInfo1.setDisplayName("testDisplayName1");
		
		IrFile irFile = new IrFile(fileInfo1, "my test file");


		
		item.setDescription("description");
		item.setPrimaryContentType(contentType);
		item.addContributor(contributor);
		item.setExternalPublishedItem(externalPublishedItem);
		item.setFirstAvailableDate(new FirstAvailableDate(1, 30, 2008));
		item.setId(10l);
		item.setThesis(false);
		item.setItemAbstract("itemAbstract");
		item.addFile(irFile);
		
		item.addItemIdentifier("value", identifier);
		
		item.setItemKeywords("itemKeywords");
		item.addReport(new Series("series", "Series10"), "Report15");
		item.setLanguageType(new LanguageType("English"));
		item.addLink(itemLink);
		item.setName("Itemname");
		item.setOwner(owner);
		item.setPublishedToSystem(true);
		item.setOriginalItemCreationDate(new OriginalItemCreationDate(10,25,2008));
		item.setReleaseDate(new Date());
		item.addItemSponsor(sponsor);
		item.addSubTitle("Title 2");
		
		GenericItem clonedItem = item.clone();
		
		assert clonedItem.getName().equals(item.getName()) : "Item name should be equal";
		assert clonedItem.getDescription().equals(item.getDescription()) : "Item Description should be equal";
		assert clonedItem.getPrimaryContentType().equals(item.getPrimaryContentType()) : "Item Content type should be equal";
		
		for(ItemContributor c : item.getContributors())
		{
			assert clonedItem.getContributor(c.getContributor()) != null : " coloned item does not contain contributor " + c.getContributor();
		}
		
		assert clonedItem.getExternalPublishedItem().equals(item.getExternalPublishedItem()) : "ExternalPublishedItem should be equal";
		assert clonedItem.getFirstAvailableDate().equals(item.getFirstAvailableDate()) : "FirstAvailableDate should be equal cloned = " + clonedItem.getFirstAvailableDate() + " original = " + item.getFirstAvailableDate();
		assert !item.getId().equals(clonedItem.getId()) : "Id should not be equal";
		assert clonedItem.getIsThesis() == (item.getIsThesis()) : "Thesis should be equal";
		assert clonedItem.getItemAbstract().equals(item.getItemAbstract()) : "Item Abstract should be equal";
		assert clonedItem.getItemFile("my test file").getIrFile().equals(item.getItemFile("my test file").getIrFile()) : "Item files should be equal";
		assert clonedItem.getItemIdentifiers().equals(item.getItemIdentifiers()) : "Item Identifiers should be equal";
		assert clonedItem.getItemKeywords().equals(item.getItemKeywords()) : "Item Keywords should be equal";
		assert clonedItem.getItemReportByReportNumber("Report15") != null : "Item Report should exist";
		assert clonedItem.getLanguageType().equals(item.getLanguageType()) : "Language Type should be equal";
		assert clonedItem.getItemLink(itemLink.getName()) != null : "Link should exist";
		assert clonedItem.getName().equals(item.getName()) : "Name should be equal";
		assert clonedItem.getOwner().equals(item.getOwner()) : "Owner should be equal";
		assert clonedItem.isPublishedToSystem() != item.isPublishedToSystem() : "Is Published To System should be equal";
		assert clonedItem.getReleaseDate().equals(item.getReleaseDate()) : "Release Date should be equal";
		assert clonedItem.getItemSponsors().size() == 1 : "Sponsor should be 1";
		assert clonedItem.getItemSponsors().iterator().next().getSponsor().equals(sponsor) : "Sponsor should be equal";
		assert clonedItem.getSubTitles().size() == 1: "SubTitles should be equal";
		assert clonedItem.getOriginalItemCreationDate().equals(item.getOriginalItemCreationDate()) : "Original creation Date should be equal coloned = " + clonedItem.getOriginalItemCreationDate() + " original = " + item.getOriginalItemCreationDate();
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test getting a file from an item
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testItemObjectMove() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {
		
		log.debug("HERE IS SOME OUTPUT");

		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_core_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		// create the first file to store in the temporary folder
		File f = testUtil.creatFile(directory, "testFile",
				"Hello  - ItemFile This is text in a file"); 
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();

		FileInfo fileInfo1 = fd.addFile(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");

		// create a collection
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");

		GenericItem item = new GenericItem("item1");
		
		IrFile irFile = new IrFile(fileInfo1, "myNewFile");
		
		item.addFile(irFile);
		ItemFile info = item.getItemFile("myNewFile");
		assert info.getIrFile().equals(irFile) : "file should be equal";
		assert info.getOrder() == 0 : "item file order should be 0 but is " + info.getOrder();
		
		ItemLink itemLink = new ItemLink();
		itemLink.setName("ItemLinkName");
		itemLink.setUrlValue("http://www.hotmail.com");
		itemLink.setDescription("ItemLinkDescription");
		
		item.addLink(itemLink);
		
		assert itemLink.getOrder() == 1 : " link order should be 1 but is " + itemLink.getOrder();
		
		ItemLink itemLink2 = new ItemLink();
		itemLink2.setName("ItemLinkName2");
		itemLink2.setUrlValue("http://www.cnn.com");
		itemLink2.setDescription("ItemLinkDescription2");
		
		item.addLink(itemLink2);
		
		assert itemLink2.getOrder() == 2 : " link 2 order should be 2 but is " + itemLink2.getOrder();
		
		item.moveItemObject(info, 1);
		
		assert itemLink.getOrder() == 0 : " link order should be 0 but is " + itemLink.getOrder();
		assert info.getOrder() == 1 :  "item file order should be 1 but is " + info.getOrder();
		assert itemLink2.getOrder() == 2 : " link 2 order should be 2 but is " + itemLink2.getOrder();
		
		repoHelper.cleanUpRepository();
	}
}
