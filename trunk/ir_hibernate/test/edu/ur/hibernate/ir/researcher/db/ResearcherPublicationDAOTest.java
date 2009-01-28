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

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherDAO;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.ir.researcher.ResearcherPublicationDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;

/**
 * Class for testing researcher publication persistence.
 * 
 * @author Sharmila Ranganathan  
 *
 */
public class ResearcherPublicationDAOTest {
	
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
    ResearcherPublicationDAO researcherPublicationDAO = (ResearcherPublicationDAO) 
    ctx.getBean("researcherPublicationDAO");
    
    GenericItemDAO itemDAO = 
    	(GenericItemDAO) ctx.getBean("itemDAO");
    
    /** Researcher data access */
    ResearcherDAO researcherDAO= (ResearcherDAO) ctx.getBean("researcherDAO");
    
    
	/**
	 * Test personal item persistence
	 * 
	 */
	@Test
	public void rootResearcherPublicationDAOTest() {

		TransactionStatus ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");

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
		GenericItem genericItem = new GenericItem("aItem");
		ResearcherPublication researcherPublication = new ResearcherPublication(researcher, genericItem, 1);
		researcherPublicationDAO.makePersistent(researcherPublication);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		ResearcherPublication other = researcherPublicationDAO.getById(researcherPublication.getId(), false);
		GenericItem otherGenericItem = other.getPublication();
		assert otherGenericItem != null : "Generic Item should be found";
		assert other.getResearcher() != null : "Researcher should not be null";
		assert other.equals(researcherPublication) : "The researcher publication " + researcherPublication + " should be found";
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		user.setResearcher(null);
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		researcherPublicationDAO.makeTransient(other);
		itemDAO.makeTransient(otherGenericItem);
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
	    tm.commit(ts);
	
	}
	
	/**
	 * Test getting the set of root personal items
	 */
	@Test
	public void getRootResearcherPublicationsDAOTest()throws Exception{
		
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");
  		
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
		GenericItem genericItem = new GenericItem("aItem");
		otherResearcher.createRootPublication(genericItem, 1);

		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		otherResearcher = researcherDAO.getById(researcher.getId(), false);
		assert otherResearcher != null : "Other should not be null";
		
		Set<ResearcherPublication> researcherPublications = otherResearcher.getRootPublications();
		
		assert researcherPublications != null : "Should be able to find the researcher publication";
		assert researcherPublications.size() == 1 : "Root researcher publications should be 1 but is " +
		    researcherPublications.size();
		
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

}
