package edu.ur.hibernate.ir.groupspace.db;

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
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPagePublication;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPagePublicationDAO;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;

public class GroupWorkspaceProjectPagePublicationDAOTest {
	
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

   
    /** personal item relational data access */
    GroupWorkspaceProjectPagePublicationDAO groupWorkspaceProjectPagePublicationDAO = (GroupWorkspaceProjectPagePublicationDAO) 
    ctx.getBean("groupWorkspaceProjectPagePublicationDAO");
    
    GenericItemDAO itemDAO = 
    	(GenericItemDAO) ctx.getBean("itemDAO");
    
    /** GroupWorkspaceProjectPage data access */
    GroupWorkspaceProjectPageDAO groupWorkspaceProjectPageDAO= (GroupWorkspaceProjectPageDAO) ctx.getBean("groupWorkspaceProjectPageDAO");
    
	// group workspace data access
	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");
    
	/**
	 * Test personal item persistence
	 * 
	 */
	@Test
	public void rootGroupWorkspaceProjectPagePublicationDAOTest() {

		TransactionStatus ts = tm.getTransaction(td);
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		
		tm.commit(ts);
		

		ts = tm.getTransaction(td);
		GenericItem genericItem = new GenericItem("aItem");
		GroupWorkspaceProjectPagePublication groupWorkspaceProjectPagePublication = new GroupWorkspaceProjectPagePublication(groupWorkspaceProjectPage, genericItem, 1);
		groupWorkspaceProjectPagePublicationDAO.makePersistent(groupWorkspaceProjectPagePublication);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		GroupWorkspaceProjectPagePublication other = groupWorkspaceProjectPagePublicationDAO.getById(groupWorkspaceProjectPagePublication.getId(), false);
		GenericItem otherGenericItem = other.getPublication();
		assert otherGenericItem != null : "Generic Item should be found";
		assert other.getGroupWorkspaceProjectPage() != null : "GroupWorkspaceProjectPage should not be null";
		assert other.equals(groupWorkspaceProjectPagePublication) : "The groupWorkspaceProjectPage publication " + groupWorkspaceProjectPagePublication + " should be found";
		tm.commit(ts);
		
		
		
		ts = tm.getTransaction(td);
		groupWorkspaceProjectPagePublicationDAO.makeTransient(other);
		itemDAO.makeTransient(itemDAO.getById(otherGenericItem.getId(), false));
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
	    tm.commit(ts);
	
	}
	
	/**
	 * Test getting the set of root personal items
	 */
	@Test
	public void getRootGroupWorkspaceProjectPagePublicationsDAOTest()throws Exception{
		
		TransactionStatus ts = tm.getTransaction(td);
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		
		tm.commit(ts);
		
        ts = tm.getTransaction(td);
        GroupWorkspaceProjectPage otherGroupWorkspaceProjectPage = groupWorkspaceProjectPageDAO.getById(groupWorkspaceProjectPage.getId(), false);
		GenericItem genericItem = new GenericItem("aItem");
		otherGroupWorkspaceProjectPage.createRootPublication(genericItem, 1);

		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		otherGroupWorkspaceProjectPage = groupWorkspaceProjectPageDAO.getById(groupWorkspaceProjectPage.getId(), false);
		assert otherGroupWorkspaceProjectPage != null : "Other should not be null";
		
		Set<GroupWorkspaceProjectPagePublication> projectPagePublications = otherGroupWorkspaceProjectPage.getRootPublications();
		
		assert projectPagePublications != null : "Should be able to find the groupWorkspaceProjectPage publication";
		assert projectPagePublications.size() == 1 : "Root groupWorkspaceProjectPage publications should be 1 but is " +
		    projectPagePublications.size();
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		tm.commit(ts);
		
	}

}
