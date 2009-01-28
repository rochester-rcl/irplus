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

package edu.ur.ir.researcher;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.DefaultFileInfo;
import edu.ur.file.db.DefaultFileServer;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.Department;
import edu.ur.ir.user.IrUser;
import edu.ur.util.FileUtil;

/**
 * Test the Researcher type Class
 * 
 * @author Sharmila Ranganathan
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class ResearcherTest {

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	

	
	/**
	 * Setup for testing
	 * 
	 * this deletes exiting test directories if they exist
	 */
	@BeforeMethod
	public void cleanDirectory() {
		try {
			File f = new File(properties.getProperty("a_repo_path"));
			if(f.exists())
			{
			    FileUtils.forceDelete(f);
			}
			File f2 = new File(properties.getProperty("FileInfoTest.base.equals"));
			if(f2.exists() )
			{
				FileUtils.forceDelete(f2);
			}
			    
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() throws Exception {
		
		// this will create folders so we need to place them in directories
		String folderPath = properties.getProperty("FileInfoTest.base.equals");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName2", "fileDB_2", folderPath, "Description");
		
		TreeFolderInfo folder1 = fd.createRootFolder("displayName1", "TreeFolderInfo1");
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_core_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "basicFile1","Hello  -  This file is for equals 1");
		DefaultFileInfo fileInfo = folder1.createFileInfo(f, "basicFile1");
		
		IrUser user = new IrUser();
		user.setLastName("familyName");
		user.setFirstName("forename");
		
		fileInfo.setVersion(22);
		fileInfo.setDisplayName("displayName");
		fileInfo.setId(9l);
		
		Department d = new Department("MyDept");
		
		Field field = new Field("field");
		
		IrFile irFile =  new IrFile(fileInfo, "testFile");

		Researcher researcher = new Researcher(user);
		researcher.addDepartment(d);
		researcher.addField(field);
		researcher.setTitle("Stent");
		researcher.setPrimaryPicture(irFile);
		researcher.setCampusLocation("campusLocation");
		researcher.setEmail("email@e.com");
		researcher.setFax("123");
		researcher.setPhoneNumber("123456");
		researcher.setResearchInterest("researchInterest");
		researcher.setTeachingInterest("teachingInterest");
		
		assert researcher.getDepartments().contains(d) : "Researcher department should equal science";
		assert researcher.getTitle().equals("Stent") : "Researcher title should equal Stent";		
		assert researcher.getFields().contains(field) : "Researcher field should equal field";
		assert researcher.getPrimaryPicture().equals(irFile) : "Researcher picture should be equal " + irFile.toString();
		assert researcher.getCampusLocation().equals("campusLocation") : "Location  field should equal campusLocation";
		assert researcher.getEmail().equals("email@e.com") : "Email field should equal email@e.com";
		assert researcher.getFax().equals("123") : "Fax field should equal 123";
		assert researcher.getPhoneNumber().equals("123456") : "Ph no. field should equal 123456";
		assert researcher.getResearchInterest().equals("researchInterest") : "research Interest field should be equal";
		assert researcher.getTeachingInterest().equals("teachingInterest") : "Teaching Interest field should be equal";
		assert researcher.getUser().equals(user) : "User should equal";

	}
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals() {
		IrUser user1 = new IrUser();
		user1.setUsername("user1");
		user1.setLastName("familyName1");
		user1.setFirstName("forename1");
		
		IrUser user2 = new IrUser();
		user2.setUsername("user2");
		user2.setLastName("familyName2");
		user2.setFirstName("forename3");
		
		Department d = new Department("MyDept");
		
		Department d1 = new Department("MyDept1");
		
		
		Field field1 = new Field();
		field1.setName("field1");
		
		Field field2 = new Field();
		field2.setName("field2");
		
		Researcher researcher1 = new Researcher(user1);
		researcher1.addDepartment(d);
		researcher1.addField(field1);
		researcher1.setTitle("Stent1");
		
		Researcher researcher2 = new Researcher(user2);
		researcher2.addDepartment(d1);
		researcher2.addField(field2);
		researcher2.setTitle("Stent2");

		Researcher researcher3 = new Researcher(user1);
		researcher3.addDepartment(d);
		researcher3.addField(field1);
		researcher3.setTitle("Stent1");

		assert researcher1.equals(researcher3) : "researcher1 should equal researcher3";
		assert !researcher1.equals(researcher2) : "researcher2 should not equal researcher1";
		assert researcher1.hashCode() == researcher3.hashCode() : "Hash codes should be equal";
		assert researcher1.hashCode() != researcher2.hashCode() : "Hash codes should not be equal";		

	}

	/**
	 * Test adding a folder
	 * @throws DuplicateNameException 
	 */
	public void testAddFolder() throws DuplicateNameException
	{
		IrUser user = new IrUser();
		user.setUsername("aUser");
		
		Researcher r = new Researcher(user);
		
		ResearcherFolder researcherFolder = r.createRootFolder("rootFolder");
		
		assert researcherFolder.getResearcher().equals(r) : "Researcher of folder should equal " +
		user + " but equals " + researcherFolder.getResearcher();
		
		assert researcherFolder.getFullPath().equals("/rootFolder/") : 
			"Path equals " + researcherFolder.getFullPath();
		
		assert r.getRootFolder(researcherFolder.getName()).equals(researcherFolder) : 
			"Should find personal folder " + researcherFolder;
		assert r.removeRootFolder(researcherFolder) : "Should be able to remove " + researcherFolder;
		assert r.getRootFolder(researcherFolder.getName()) == null : "Root folder should be removed";
	}
	
	
	/**
	 * Test adding a file to a researcher.
	 * @throws DuplicateNameException 
	 */
	public void testAddFile() throws DuplicateNameException,  IllegalFileSystemNameException
	{
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
				"Hello  - user versionedFile This is text in a file"); 
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();
		
		// create a new file info container
		FileInfo fileInfo1 = fd.addFile(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");
		
		// create a new versioned file
		IrUser user = new IrUser("username", "password");
		
		Researcher researcher  = new Researcher(user);
		IrFile irf = new IrFile(fileInfo1, "displayName1");
		
		researcher.createRootFile(irf, 1);
		ResearcherFile rootFile = researcher.getRootFile(irf.getName());
		assert researcher.getRootFile(rootFile.getName()) != null : "Personal file should be found";
		assert researcher.removeRootFile(rootFile) : "Personal file " + rootFile + " should be removed";
		assert researcher.getRootFile(rootFile.getName()) == null : "Should no longer find personal file";
		
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test adding a publication
	 */
	public void testAddRootPublication()
	{
		IrUser user = new IrUser();
		user.setUsername("aUser");
		
		Researcher researcher = new Researcher(user);
		
		GenericItem item = new GenericItem("myItem");
	    
		ResearcherPublication researcherPublication= researcher.createRootPublication(item, 1);
		researcherPublication.setId(35l);
		
		assert researcher.getRootPublication("myItem").equals(researcherPublication) :
			" researcher should have publication " + researcherPublication;
		
		assert researcherPublication.getFullPath().equals("/myItem"):
		    "researcher publication = " + researcherPublication.getFullPath();
		
		assert researcher.getRootPublication(35l).equals(researcherPublication) : 
			"researcher should have publication " + researcherPublication;
		
		assert researcher.removeRootPublication(researcherPublication) : 
			"researcher publication should be removed " + researcherPublication;
		
		assert researcher.getRootPublication(35l) == null : 
			"Should not be able to find publication";
	}

	/**
	 * Test adding a link
	 */
	public void testAddRootLink()
	{
		IrUser user = new IrUser();
		user.setUsername("aUser");
		
		Researcher researcher = new Researcher(user);
		
		ResearcherLink researcherLink= researcher.createRootLink("www.google.com", "myLink", "description");
		researcherLink.setId(35l);
		
		assert researcher.getRootLink("myLink").equals(researcherLink) :
			" researcher should have link" + researcherLink;
		
		assert researcherLink.getFullPath().equals("/myLink"):
		    "personal item = " + researcherLink.getFullPath();
		
		assert researcher.getRootLink(35l).equals(researcherLink) : 
			"researcher should have link " + researcherLink;
		
		assert researcher.removeRootLink(researcherLink) : 
			"Link should be removed " + researcherLink;
		 
		assert researcher.getRootLink(35l) == null : 
			"Should not be able to find researcher link";
	}
}
