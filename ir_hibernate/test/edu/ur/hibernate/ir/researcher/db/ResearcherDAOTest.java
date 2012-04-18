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

package edu.ur.hibernate.ir.researcher.db;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.researcher.ResearcherPersonalLink;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.util.FileUtil;

/**
 * Test the persistence methods for researcher
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ResearcherDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Researcher data access.  */
	ResearcherDAO researcherDAO = (ResearcherDAO) ctx
	.getBean("researcherDAO");
	
	/** Researcher data access.  */
	IrUserDAO userDAO = (IrUserDAO) ctx
	.getBean("irUserDAO");	
	
	/** Ir File relational data access.  */
	IrFileDAO irFileDAO = (IrFileDAO) ctx.getBean("irFileDAO");
	
	
	
	/**
	 * Test Institutional researcherlection persistence
	 */
	@Test
	public void baseResearcherDAOTest() {

	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
		
		// create a user and add the versioned file to the item
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		
		
		//commit the transaction 
		// create a researcher
		Researcher researcher = user.createResearcher();
		researcher.setTitle("title");
		researcher.setCampusLocation("campusLocation");
		researcher.setEmail("email@e.com");
		researcher.setFax("123");
		researcher.setPhoneNumber("123456");
		researcher.setResearchInterest("researchInterest");
		researcher.setTeachingInterest("teachingInterest");
		
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);

		Researcher other = researcherDAO.getById(researcher.getId(), false);
		assert other != null : "Should be able to find " +
		"the researcher by id";
		
		assert other.equals(researcher) : "Researchers should be equal";
		
		
		
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
		assert researcherDAO.getById(other.getId(), false ) == null : "Should not be able to find " +
			"other";

	}
	
	/**
	 * test adding pictures to the institutional researcherlection
	 * @throws LocationAlreadyExistsException 
	 */
	public void addResearcherPictures()  throws IllegalFileSystemNameException, LocationAlreadyExistsException
	{
		TransactionStatus ts = tm.getTransaction(td);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
        
        UserEmail userEmail = new UserEmail("email");
		
		// create a user and add the versioned file to the item
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);

		// create a researcher
		Researcher researcher = user.createResearcher();
		researcher.setTitle("title");
		
		userDAO.makePersistent(user);
        //commit the transaction
 		
        // helper to create the file
		// we are only testing the ability to add a file to
		// the researcher rather than an actual picture.
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", 
				"Hello  - irFile This is text in a file");

		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		fileInfo1.setDescription("testThis");
		IrFile irFile = new IrFile(fileInfo1, "newName");
		
		irFileDAO.makePersistent(irFile);
		
		// file to represent the primary picture
		File primaryFile = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file");

        FileInfo primaryInfo1 = repo.getFileDatabase().addFile(primaryFile, "newFile2");
        primaryInfo1.setDescription("testThis2");
        IrFile primaryIrFile = new IrFile(fileInfo1, "newName2");
        
        irFileDAO.makePersistent(primaryIrFile);
        
        tm.commit(ts);
		
		// add the file to the researcherlection
        ts = tm.getTransaction(td);
        researcher = researcherDAO.getById(researcher.getId(), false);
        researcher.addPicture(irFile);
        researcher.setPrimaryPicture(primaryIrFile);
		researcherDAO.makePersistent(researcher);
		tm.commit(ts);
		
		//create a new transaction
		ts = tm.getTransaction(td);
		//reload the repository
		researcher = researcherDAO.getById(researcher.getId(), false);
		assert researcher.getPicture(irFile.getId()) != null : "The picture should be found";
		assert researcher.getPrimaryPicture() != null : "The primary picture should exist";
		tm.commit(ts);	
		
		//create a new transaction
		ts = tm.getTransaction(td);
		researcher = researcherDAO.getById(researcher.getId(), false);
		Set<IrFile> pictures = researcher.getPictures();
		
		for(IrFile picture: pictures)
		{
			researcher.removePicture(picture);
			irFileDAO.makeTransient(picture);
		}
		
		irFileDAO.makeTransient(researcher.getPrimaryPicture());
		researcher.setPrimaryPicture(null);
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
		//start a new transaction
		ts = tm.getTransaction(td);
		repoHelper.cleanUpRepository();
		tm.commit(ts);
	}
	
	/**
	 * Test Institutional researcher personal link persistence
	 * @throws DuplicateNameException 
	 */
	@Test
	public void researcherPersonalLinkDAOTest() throws DuplicateNameException {

	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
		
		// create a user and add the versioned file to the item
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		
		
		//commit the transaction 
		// create a researcher
		Researcher researcher = user.createResearcher();
		researcher.setTitle("title");
		researcher.setCampusLocation("campusLocation");
		researcher.setEmail("email@e.com");
		researcher.setFax("123");
		researcher.setPhoneNumber("123456");
		researcher.setResearchInterest("researchInterest");
		researcher.setTeachingInterest("teachingInterest");
		
		userDAO.makePersistent(user);
		
		ResearcherPersonalLink link0 = researcher.addPersonalLink("link0", "http://theservierside.com");
		ResearcherPersonalLink link1 = researcher.addPersonalLink("link1", "http://www.hotmail.com");
		ResearcherPersonalLink link2 = researcher.addPersonalLink("link2", "http://www.hotmail.com");
		ResearcherPersonalLink link3 = researcher.addPersonalLink("link3", "http://www.hotmail.com");
		
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		researcher = researcherDAO.getById(researcher.getId(), false);
		List<ResearcherPersonalLink> links = researcher.getPersonalLinks();
		
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 0 : "link 0 order should be 0 but is " + link0.getOrder();
		
		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 1 : "link 1 order should be 1 but is " + link1.getOrder();
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 2 : "link 2 order should be 2 but is " + link2.getOrder();

		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 3 : "link 3 order should be 3 but is " + link3.getOrder();
				
		researcher.movePersonalLink(link3, 8);
		researcherDAO.makePersistent(researcher);
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		researcher = researcherDAO.getById(researcher.getId(), false);
		links = researcher.getPersonalLinks();
		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 3 : "link 3 order should be 3 but is " + link3.getOrder();
		
		assert researcher.movePersonalLink(link1, 3) : " link 1 should be moved ";
		
		links = researcher.getPersonalLinks();
		
		link0 = researcher.getPersonalLink("link0");
		link2 = researcher.getPersonalLink("link2");
		link3 = researcher.getPersonalLink("link3");
		link1 = researcher.getPersonalLink("link1");
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 0 : "link 0 order should be 0 but is " + link0.getOrder();
		
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 1 : "link 2 order should be 1 but is " + link2.getOrder();

		
		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 2 : "link 3 order should be 2 but is " + link3.getOrder();
		
		
		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 3 : "link 3 order should be 3 but is " + link1.getOrder();
		
		
		links = researcher.getPersonalLinks();
		
		for(ResearcherPersonalLink l : links)
		{
			System.out.println("link = " + l);
		}
		
		
		researcherDAO.makePersistent(researcher);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		researcher = researcherDAO.getById(researcher.getId(), false);
		links = researcher.getPersonalLinks();
		link0 = researcher.getPersonalLink("link0");
		link2 = researcher.getPersonalLink("link2");
		link3 = researcher.getPersonalLink("link3");
		link1 = researcher.getPersonalLink("link1");
		
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 0 : "link 0 order should be 0 but is " + link0.getOrder();
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 1 : "link 1 order should be 1 but is " + link2.getOrder();

		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 2 : "link 2 order should be 2 but is " + link3.getOrder();
		
		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 3 : "link 3 order should be 3 but is " + link1.getOrder();
		tm.commit(ts);
		
		// remove link 1
		ts = tm.getTransaction(td);
		researcher = researcherDAO.getById(researcher.getId(), false);
		link1 = researcher.getPersonalLink("link1");
		researcher.removPersonalLink(link1);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		researcher = researcherDAO.getById(researcher.getId(), false);
		links = researcher.getPersonalLinks();
		link0 = researcher.getPersonalLink("link0");
		link2 = researcher.getPersonalLink("link2");
		link3 = researcher.getPersonalLink("link3");
		
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 0 : "link 0 order should be 0 but is " + link0.getOrder();
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 1 : "link 1 order should be 1 but is " + link2.getOrder();

		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 2 : "link 2 order should be 2 but is " + link3.getOrder();
		
		assert researcher.getPersonalLink("link1") == null : "links should not contain link 1";
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);

		Researcher other = researcherDAO.getById(researcher.getId(), false);
		assert other != null : "Should be able to find " +
		"the researcherlection by id";

		user = other.getUser();
		userDAO.makeTransient(user);
		tm.commit(ts);
		
		//create a new transaction
		ts = tm.getTransaction(td);
		assert researcherDAO.getById(other.getId(), false ) == null : "Should not be able to find " +
		"other";
		tm.commit(ts);	
	}

}
