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

package edu.ur.ir.user;

import java.io.File;
import java.util.Properties;

import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.repository.Repository;
import edu.ur.util.FileUtil;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;

/**
 * Test the user class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrUserTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testUserBasicSets() throws Exception
	{
	
		UserEmail userEmail = new UserEmail();
		userEmail.setEmail("test@hotmail.com");
		
		IrUser user = new IrUser();
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.setId(4L);
		ShaPasswordEncoder shaEncoder = new ShaPasswordEncoder();
		user.setPassword(shaEncoder.encodePassword("password", null));
		user.addUserEmail(userEmail, true);
		user.setUsername("username");
		user.setVersion(5);
		
		assert user.getFirstName().equals("forename") : "First name should equal ";
		assert user.getLastName().equals("familyName") : "Last name should equal ";
		assert user.getUsername().equals("username") : "User name should equal username";
		assert user.getPassword().equals(shaEncoder.encodePassword("password",null)) : "Password should equal password";
		assert user.getDefaultEmail().equals(userEmail);
	}
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()throws Exception
	{
		IrUser user1 = new IrUser();
		user1.setUsername("userName");
		
		IrUser user2 = new IrUser();
		user2.setUsername("userName2");
		
		IrUser user3 = new IrUser();
		user3.setUsername("userName");
		
		assert user1.equals(user3) : "Users should be equal";
		assert user1.hashCode() == user3.hashCode() : "Hash codes should be equal";
		
		assert !user1.equals(user2) : "Users should not be equal";
		assert user1.hashCode() != user2.hashCode() : "Hash codes should not be equal";
	}
	
	/**
	 * Test adding and removing a role from a user 
	 */
	public void testAddRole()
	{
		IrUser user1 = new IrUser();
		
		IrRole role = new IrRole();
		role.setName("role1");
		user1.addRole(role);
		assert user1.getRoles().contains(role) : " User should contain role " + role;
		
		IrRole role2 = new IrRole();
		role2.setName("role2");
		user1.addRole(role2);
		
		assert user1.getRoles().contains(role2) : "User should contain role " + role2;
		assert user1.removeRole(role2);
		assert !user1.getRoles().contains(role2) : "User should not contain " + role2;
		
		GrantedAuthority[] authorities = user1.getAuthorities();
		assert authorities.length == 1;
	}
	
	/**
	 * Test adding a file to a user.
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testAddFile() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException
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
		VersionedFile vif = new VersionedFile(user, fileInfo1, "displayName1");
		
		user.createRootFile(vif);
		PersonalFile rootFile = user.getRootFile(vif.getName());
		assert user.getRootFile(rootFile.getName()) != null : "Personal file should be found";
		assert user.removeRootFile(rootFile) : "Personal file " + rootFile + " should be removed";
		assert user.getRootFile(rootFile.getName()) == null : "Should no longer find personal file";
		
		repoHelper.cleanUpRepository();
	}
	
	

	
	/**
	 * Test adding a folder to the user as a root
	 * 
	 * @throws DuplicateNameException 
	 */
	public void testAddFolder() throws DuplicateNameException, IllegalFileSystemNameException
	{
		IrUser user = new IrUser();
		user.setUsername("aUser");
		PersonalFolder personalFolder = user.createRootFolder("rootFolder");
		
		assert personalFolder.getOwner().equals(user) : "Owner of folder should equal " +
		user + " but equals " + personalFolder.getOwner();
		
		assert personalFolder.getFullPath().equals("/rootFolder/") : 
			"Path Should equal /rootFolder/ but is: " + personalFolder.getFullPath();
		
		assert user.getRootFolder(personalFolder.getName()).equals(personalFolder) : 
			"Should find personal folder " + personalFolder;
		assert user.removeRootFolder(personalFolder) : "Should be able to remove " + personalFolder;
		assert user.getRootFolder(personalFolder.getName()) == null : "Root folder should be removed";
	}
	
	/**
	 * Test adding a personal collection 
	 */
	public void testAddPersonalCollection() throws DuplicateNameException
	{
		IrUser user = new IrUser();
		user.setUsername("aUser");
		PersonalCollection personalCollection = user.createRootPersonalCollection("myCollection");
		personalCollection.setId(35l);
		
		assert user.getRootPersonalCollection("myCollection").equals(personalCollection) :
			" user should have collection " + personalCollection;
		
		assert personalCollection.getFullPath().equals("/myCollection/"):
		    "personal collection = " + personalCollection.getFullPath();
		
		assert user.getRootPersonalCollection(35l).equals(personalCollection) : 
			"user should have collection " + personalCollection;
		
		assert user.removeRootPersonalCollection(personalCollection) : 
			"Personal Collection should be removed " + personalCollection;
		
		assert user.getRootPersonalCollection(35l) == null : "Should not be able to find personal collection";

	}
	
	/**
	 * Test adding a personal collection 
	 */
	public void testAddPersonalItem()
	{
		IrUser user = new IrUser();
		user.setUsername("aUser");
		
		GenericItem item = new GenericItem("myItem");
	    VersionedItem versionedItem = new VersionedItem(user, item);
	    
		PersonalItem personalItem = user.createRootPersonalItem(versionedItem);
		personalItem.setId(35l);
		
		assert user.getRootPersonalItem("myItem").equals(personalItem) :
			" user should have personalItem " + personalItem;
		
		
		assert personalItem.getFullPath().equals("/myItem"):
		    "personal item = " + personalItem.getFullPath();
		
		assert user.getRootPersonalItem(35l).equals(personalItem) : 
			"user should have item " + personalItem;
		
		assert user.removeRootPersonalItem(personalItem) : 
			"Personal item should be removed " + personalItem;
		
		assert user.getRootPersonalItem(35l) == null : 
			"Should not be able to find personal item";
	}
}
