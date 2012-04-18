package edu.ur.hibernate.ir.groupspace.db;

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
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLink;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLinkDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolderDAO;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.user.IrUserDAO;

public class GroupWorkspaceProjectPageFileSystemLinkDAOTest {
	
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
    GroupWorkspaceProjectPageFileSystemLinkDAO groupWorkspaceProjectPageFileSystemLinkDAO = (GroupWorkspaceProjectPageFileSystemLinkDAO) 
    ctx.getBean("groupWorkspaceProjectPageFileSystemLinkDAO");
    
    GenericItemDAO itemDAO = 
    	(GenericItemDAO) ctx.getBean("itemDAO");
    
    /** Group workspace project page data access */
    GroupWorkspaceProjectPageDAO groupWorkspaceProjectPageDAO= (GroupWorkspaceProjectPageDAO) ctx.getBean("groupWorkspaceProjectPageDAO");
    
    /** Group workspace project page folder data access */
    GroupWorkspaceProjectPageFolderDAO groupWorkspaceProjectPageFolderDAO = (GroupWorkspaceProjectPageFolderDAO) ctx.getBean("groupWorkspaceProjectPageFolderDAO");
    
	// group workspace data access
	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");
    
	/**
	 * Test groupWorkspaceProjectPage link test
	 * 
	 */
	@Test
	public void rootGroupWorkspaceProjectPageFileSystemLinkDAOTest() {

		TransactionStatus ts = tm.getTransaction(td);

		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		
		tm.commit(ts);
		

		ts = tm.getTransaction(td);
		GroupWorkspaceProjectPageFileSystemLink groupWorkspaceProjectPageLink = groupWorkspaceProjectPage.createRootLink("www.google.com", "name", "description");
		groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		GroupWorkspaceProjectPageFileSystemLink other = groupWorkspaceProjectPageFileSystemLinkDAO.getById(groupWorkspaceProjectPageLink.getId(), false);
		assert other.getUrl() != null : "Link should be found";
		assert other.getGroupWorkspaceProjectPage() != null : "GroupWorkspaceProjectPage should not be null";
		assert other.equals(groupWorkspaceProjectPageLink) : "The groupWorkspaceProjectPage link " + groupWorkspaceProjectPageLink + " should be found";
		tm.commit(ts);
		
		
		
		ts = tm.getTransaction(td);
		groupWorkspaceProjectPageFileSystemLinkDAO.makeTransient(other);
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
	    tm.commit(ts);
	
	}
	
	/**
	 * Test getting the set of root personal items
	 */
	@Test
	public void getRootGroupWorkspaceProjectPageFileSystemLinksDAOTest()throws Exception{
		
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);

		
        TransactionStatus ts = tm.getTransaction(td);
        GroupWorkspaceProjectPage otherGroupWorkspaceProjectPage = groupWorkspaceProjectPageDAO.getById(groupWorkspaceProjectPage.getId(), false);
		otherGroupWorkspaceProjectPage.createRootLink("www.google.com", "myLink", "desc");

		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		otherGroupWorkspaceProjectPage = groupWorkspaceProjectPageDAO.getById(groupWorkspaceProjectPage.getId(), false);
		assert otherGroupWorkspaceProjectPage != null : "Other should not be null";
		
		Set<GroupWorkspaceProjectPageFileSystemLink> groupWorkspaceProjectPageLinks = otherGroupWorkspaceProjectPage.getRootLinks();
		
		assert groupWorkspaceProjectPageLinks != null : "Should be able to find the groupWorkspaceProjectPage link";
		assert groupWorkspaceProjectPageLinks.size() == 1 : "Root groupWorkspaceProjectPage links should be 1 but is " +
		    groupWorkspaceProjectPageLinks.size();
		
		
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
		tm.commit(ts);
		
	}
	
	/**
	 * Test adding a link to a groupWorkspaceProjectPage folder
	 * @throws DuplicateNameException 
	 * 
	 */
	@Test
	public void folderGroupWorkspaceProjectPageFileSystemLinkDAOTest() throws DuplicateNameException {

		TransactionStatus ts = tm.getTransaction(td);
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		
		tm.commit(ts);
		

		ts = tm.getTransaction(td);
		groupWorkspaceProjectPage = groupWorkspaceProjectPageDAO.getById(groupWorkspaceProjectPage.getId(), false);
		GroupWorkspaceProjectPageFolder folder = groupWorkspaceProjectPage.createRootFolder("My Folder");
		GroupWorkspaceProjectPageFileSystemLink groupWorkspaceProjectPageLink = groupWorkspaceProjectPage.createRootLink("www.google.com", "name", "description");
		folder.addLink(groupWorkspaceProjectPageLink);
		groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		folder = groupWorkspaceProjectPageFolderDAO.getById(folder.getId(), false);
		groupWorkspaceProjectPageFolderDAO.makeTransient(folder);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
	    tm.commit(ts);
	
	}


}
