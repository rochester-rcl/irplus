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

import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherDAO;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherFolderDAO;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherLinkDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;

/**
 * Class for testing researcher link persistence.
 * 
 * @author Sharmila Ranganathan  
 *
 */
public class ResearcherLinkDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/**  Transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	/** transaction definition  */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

    /** User relational data access   */
    IrUserDAO userDAO= (IrUserDAO) ctx
	.getBean("irUserDAO");
    
    /** personal item relational data access */
    ResearcherLinkDAO researcherLinkDAO = (ResearcherLinkDAO) 
    ctx.getBean("researcherLinkDAO");
    
    GenericItemDAO itemDAO = 
    	(GenericItemDAO) ctx.getBean("itemDAO");
    
    /** Researcher data access */
    ResearcherDAO researcherDAO= (ResearcherDAO) ctx.getBean("researcherDAO");
    
    /** Researcher data access */
    ResearcherFolderDAO researcherFolderDAO= (ResearcherFolderDAO) ctx.getBean("researcherFolderDAO");
    
    
	/**
	 * Test researcher link test
	 * 
	 */
	@Test
	public void rootResearcherLinkDAOTest() {

		TransactionStatus ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("user@email");

		// create a user who has their own folder
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("none");
		user.addUserEmail(userEmail, true);
		
		// create the user 
		userDAO.makePersistent(user);
		
		Researcher researcher = new Researcher(user);
		researcherDAO.makePersistent(researcher);
		
		tm.commit(ts);
		

		ts = tm.getTransaction(td);
		ResearcherLink researcherLink = researcher.createRootLink("www.google.com", "name", "description");
		researcherDAO.makePersistent(researcher);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		ResearcherLink other = researcherLinkDAO.getById(researcherLink.getId(), false);
		assert other.getUrl() != null : "Link should be found";
		assert other.getResearcher() != null : "Researcher should not be null";
		assert other.equals(researcherLink) : "The researcher link " + researcherLink + " should be found";
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		user.setResearcher(null);
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		researcherLinkDAO.makeTransient(other);
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
	    tm.commit(ts);
	
	}
	
	/**
	 * Test getting the set of root personal items
	 */
	@Test
	public void getRootResearcherLinksDAOTest()throws Exception{
		
		UserEmail userEmail = new UserEmail("user@email");
  		
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);

		userDAO.makePersistent(user);
		
		Researcher researcher = new Researcher(user);
		researcherDAO.makePersistent(researcher);
		
        TransactionStatus ts = tm.getTransaction(td);
        Researcher otherResearcher = researcherDAO.getById(researcher.getId(), false);
		otherResearcher.createRootLink("www.google.com", "myLink", "desc");

		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		otherResearcher = researcherDAO.getById(researcher.getId(), false);
		assert otherResearcher != null : "Other should not be null";
		
		Set<ResearcherLink> researcherLinks = otherResearcher.getRootLinks();
		
		assert researcherLinks != null : "Should be able to find the researcher link";
		assert researcherLinks.size() == 1 : "Root researcher links should be 1 but is " +
		    researcherLinks.size();
		
		user = userDAO.getById(user.getId(), false);
		user.setResearcher(null);
		userDAO.makePersistent(user);
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
		assert userDAO.getById(user.getId(), false) == null : "Should not be able to find other";
	}
	
	/**
	 * Test adding a link to a researcher folder
	 * @throws DuplicateNameException 
	 * 
	 */
	@Test
	public void folderResearcherLinkDAOTest() throws DuplicateNameException {

		TransactionStatus ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("user@email");

		// create a user who has their own folder
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("none");
		user.addUserEmail(userEmail, true);
		
		// create the user 
		userDAO.makePersistent(user);
		
		Researcher researcher = new Researcher(user);
		researcherDAO.makePersistent(researcher);
		
		tm.commit(ts);
		

		ts = tm.getTransaction(td);
		researcher = researcherDAO.getById(researcher.getId(), false);
		ResearcherFolder folder = researcher.createRootFolder("My Folder");
		ResearcherLink researcherLink = researcher.createRootLink("www.google.com", "name", "description");
		folder.addLink(researcherLink);
		researcherDAO.makePersistent(researcher);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		folder = researcherFolderDAO.getById(folder.getId(), false);
		researcherFolderDAO.makeTransient(folder);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		user.setResearcher(null);
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
	    tm.commit(ts);
	
	}

}
