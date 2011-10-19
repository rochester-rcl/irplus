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

package edu.ur.hibernate.ir.security.db;

import java.util.LinkedList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeDAO;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeDAO;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrAclDAO;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypeDAO;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrClassTypePermissionDAO;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.security.IrUserAccessControlEntryDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;

/**
 * Test the persistance methods for User Access Control Entries
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrUserAccessControlEntryDAOTest {

	/* get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
    
	/* transaction manager for handling transactions */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/* Transaction definition   */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    
    /* Class type data access  */
    IrClassTypeDAO irClassTypeDAO = (IrClassTypeDAO) ctx
    .getBean("irClassTypeDAO");
    
	/* Language type data access */
    LanguageTypeDAO languageTypeDAO = (LanguageTypeDAO) ctx
	.getBean("languageTypeDAO");
    
	/* Language type data access */
    ContributorTypeDAO contributorTypeDAO = (ContributorTypeDAO) ctx
	.getBean("contributorTypeDAO");
    
 	/* User relational data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
    
    /* User access relational data access */
    IrUserAccessControlEntryDAO uaceDAO = (IrUserAccessControlEntryDAO) ctx
    .getBean("irUserAccessControlEntryDAO");
    
	/* Class type permission information  */
	IrClassTypePermissionDAO classTypePermissionDAO = (IrClassTypePermissionDAO) 
	ctx.getBean("irClassTypePermissionDAO");
	
    /* the Institutional repository acl relational data access */
    IrAclDAO irAclDAO = (IrAclDAO) ctx.getBean("irAclDAO");

	/**
	 * Test User Access Control Entry DAO
	 */
	@Test
	public void baseUserControlEntryDAOTest() throws Exception{
		
		TransactionStatus ts = tm.getTransaction(td);
		LanguageType lt = new LanguageType();
		lt.setName("languageName");
 		lt.setDescription("languageDescription");
        languageTypeDAO.makePersistent(lt);
        
	    IrClassType languageClassType = new IrClassType(LanguageType.class);
        irClassTypeDAO.makePersistent(languageClassType);
 		
		IrAcl irAcl = new IrAcl(lt, languageClassType);
		
		
		irAclDAO.makePersistent(irAcl);
        //complete the transaction
		tm.commit(ts);
	
		
		
		ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("user@email");
		
		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.addUserEmail(userEmail, true);
		
		// save the user
		userDAO.makePersistent(user);
	    
		// create the user access control entry
		IrUserAccessControlEntry  uace = irAcl.createUserAccessControlEntry(user);

 	    IrClassTypePermission classTypePermission = new IrClassTypePermission(languageClassType);
		classTypePermission.setName("permissionName");
		classTypePermission.setDescription("permissionDescription");

		IrClassTypePermission classTypePermission1 = new IrClassTypePermission(languageClassType);
		classTypePermission1.setName("permissionName1");
		classTypePermission1.setDescription("permissionDescription1");
         
	    
	    // save the class type permission
	    classTypePermissionDAO.makePersistent(classTypePermission);
	    classTypePermissionDAO.makePersistent(classTypePermission1);

	    uace.addPermission(classTypePermission1);
	    uace.addPermission(classTypePermission);
	    uaceDAO.makePersistent(uace); 
	    tm.commit(ts);
	    
		// start a new transaction
		ts = tm.getTransaction(td);
		
		IrUserAccessControlEntry other = uaceDAO.getById( uace.getId(), false);
		assert other.equals(uace) : "User access control entries should be equal";
		assert uace.getAcl().equals(irAcl) : "Acl's should be equal";
		assert irAcl.getUserAccessControlEntry(uace.getId()).equals(uace) : "Access control should bin in the irAcl";
		assert uace.getPermissions().size() == 2 : "Should have at least one permission";
		assert uace.getIrClassTypePermissions().contains(classTypePermission) : "Should equal the class type permission";
		assert uace.getIrUser().equals(user) : "Users should be equal";
		assert uaceDAO.getCount() == 1 : "Should have one uace";
		
		//make sure we can get the acl for the role
		List<IrAcl> acls = irAclDAO.getAllAclsForSid(user);
		assert acls.size() == 1 : "Should be able to find 1 acl for user " + user +
		" but found " + acls.size();
		
		// commit the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// clean up the database
		irAclDAO.makeTransient(irAclDAO.getById(irAcl.getId(), false));
		assert uaceDAO.getById(uace.getId(), false) == null : "Should not be able to find the access control entry";
		
 	    classTypePermissionDAO.makeTransient(classTypePermissionDAO.getById(classTypePermission.getId(), false));
 	    classTypePermissionDAO.makeTransient(classTypePermissionDAO.getById(classTypePermission1.getId(), false));
 	    
 	    irClassTypeDAO.makeTransient(irClassTypeDAO.getById(languageClassType.getId(), false));
 	    //assert irClassTypeDAO.getById(languageClassType.getId(), false) == null : "Should not be able to find class type " + languageClassType;
 	    
		// start a new transaction
		
 		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		languageTypeDAO.makeTransient(languageTypeDAO.getById(lt.getId(), false));
        //commit the transaction
		tm.commit(ts); 
	}
	
	/**
	 * Bulk entry creation
	 */
	@Test
	public void createEntriesForUsersTest() throws ClassNotFoundException
	{
		TransactionStatus ts = tm.getTransaction(td);
		LanguageType lt = new LanguageType();
		lt.setName("languageName");
 		lt.setDescription("languageDescription");
        languageTypeDAO.makePersistent(lt);
        
	    IrClassType languageClassType = new IrClassType(LanguageType.class);
        irClassTypeDAO.makePersistent(languageClassType);
 		
        LanguageType lt2 = new LanguageType();
		lt.setName("languageName2");
 		lt.setDescription("languageDescription2");
        languageTypeDAO.makePersistent(lt2);
		
		IrAcl irAcl = new IrAcl(lt, languageClassType);
		IrAcl irAcl2 = new IrAcl(lt2, languageClassType);
		
		irAclDAO.makePersistent(irAcl);
		irAclDAO.makePersistent(irAcl2);
       
	
		UserEmail userEmail = new UserEmail("user@email");
		
		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.addUserEmail(userEmail, true);
		
		// save the user
		userDAO.makePersistent(user);
		
		UserEmail userEmail2 = new UserEmail("user@email2");
		IrUser user2 = userManager.createUser("passowrd2", "userName2");
		user.setLastName("familyName2");
		user.setFirstName("forename2");
		user.addUserEmail(userEmail2, true);
		userDAO.makePersistent(user2);
		
		// create some permissions
 	    IrClassTypePermission classTypePermission = new IrClassTypePermission(languageClassType);
		classTypePermission.setName("permissionName");
		classTypePermission.setDescription("permissionDescription");

		IrClassTypePermission classTypePermission1 = new IrClassTypePermission(languageClassType);
		classTypePermission1.setName("permissionName1");
		classTypePermission1.setDescription("permissionDescription1");
         
	    
	    // save the class type permission
	    classTypePermissionDAO.makePersistent(classTypePermission);
	    classTypePermissionDAO.makePersistent(classTypePermission1);
		
		 //complete the transaction
		tm.commit(ts);
		
		
		
	    
		// start a new transaction
		ts = tm.getTransaction(td);
		List<IrUser> users = new LinkedList<IrUser>();
		users.add(user);
		users.add(user2);
		
		List<IrAcl> acls = new LinkedList<IrAcl>();
		acls.add(irAcl);
		acls.add(irAcl2);
		
		// create user entries for all given acls
		int count = uaceDAO.createUserControlEntriesForUsers(users, acls);
		assert count == 4 : "Should have 4 entries but have " + count;
		
		List<IrUserAccessControlEntry> entries = new LinkedList<IrUserAccessControlEntry>();
		
		entries.addAll(uaceDAO.getUserControlEntriesForUsers(irAcl, users));
		entries.addAll(uaceDAO.getUserControlEntriesForUsers(irAcl2, users));
		
		assert entries.size() == 4 : "Should find 4 entries but found " + entries.size();
		
		// create permissions for all given user control entries
	    List<IrClassTypePermission> permissions = new LinkedList<IrClassTypePermission>();
	    permissions.add(classTypePermission);
	    permissions.add(classTypePermission1);
	    
	    int permissionEntriesCount = uaceDAO.createPermissionsForUserControlEntries(entries, permissions);
	    assert permissionEntriesCount == 8 : "Should have 8 entries but have " + permissionEntriesCount;
	    
		
		// commit the transaction
		tm.commit(ts);
		
		
		// start a new transaction
		ts = tm.getTransaction(td);
		// clean up the database
		irAclDAO.makeTransient(irAclDAO.getById(irAcl.getId(), false));
		irAclDAO.makeTransient(irAclDAO.getById(irAcl2.getId(), false));
  		userDAO.makeTransient(userDAO.getById(user.getId(), false));
  		userDAO.makeTransient(userDAO.getById(user2.getId(), false));
		languageTypeDAO.makeTransient(languageTypeDAO.getById(lt.getId(), false));
		languageTypeDAO.makeTransient(languageTypeDAO.getById(lt2.getId(), false));
		
		classTypePermissionDAO.makeTransient(classTypePermissionDAO.getById(classTypePermission.getId(), false));
		classTypePermissionDAO.makeTransient(classTypePermissionDAO.getById(classTypePermission1.getId(), false));
       
 	    irClassTypeDAO.makeTransient(irClassTypeDAO.getById(languageClassType.getId(), false));

		//commit the transaction
		tm.commit(ts); 
	}
	
	/**
	 * Bulk entry creation
	 */
	@Test
	public void createEntriesForUsersMultipleClassTypesTest() throws ClassNotFoundException
	{
		TransactionStatus ts = tm.getTransaction(td);
		LanguageType lt = new LanguageType();
		lt.setName("languageName");
 		languageTypeDAO.makePersistent(lt);
 		
 		ContributorType ct = new ContributorType("type");
 		contributorTypeDAO.makePersistent(ct);
        
        
	    IrClassType languageClassType = new IrClassType(LanguageType.class);
        irClassTypeDAO.makePersistent(languageClassType);
        
        IrClassType contributorClassType = new IrClassType(ContributorType.class);
        irClassTypeDAO.makePersistent(contributorClassType);
       
		IrAcl irAcl = new IrAcl(lt, languageClassType);
		IrAcl irAcl2 = new IrAcl(ct, contributorClassType);
		
		irAclDAO.makePersistent(irAcl);
		irAclDAO.makePersistent(irAcl2);
       
	
		UserEmail userEmail = new UserEmail("user@email");
		
		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.addUserEmail(userEmail, true);
		
		// save the user
		userDAO.makePersistent(user);
		
		UserEmail userEmail2 = new UserEmail("user@email2");
		IrUser user2 = userManager.createUser("passowrd2", "userName2");
		user.setLastName("familyName2");
		user.setFirstName("forename2");
		user.addUserEmail(userEmail2, true);
		userDAO.makePersistent(user2);
		
		// create some permissions
 	    IrClassTypePermission classTypePermission = new IrClassTypePermission(languageClassType);
		classTypePermission.setName("permissionName");
		classTypePermission.setDescription("permissionDescription");

		IrClassTypePermission classTypePermission1 = new IrClassTypePermission(contributorClassType);
		classTypePermission1.setName("permissionName1");
		classTypePermission1.setDescription("permissionDescription1");
         
	    
	    // save the class type permission
	    classTypePermissionDAO.makePersistent(classTypePermission);
	    classTypePermissionDAO.makePersistent(classTypePermission1);
		
		 //complete the transaction
		tm.commit(ts);
		
		
		
	    
		// start a new transaction
		ts = tm.getTransaction(td);
		List<IrUser> users = new LinkedList<IrUser>();
		users.add(user);
		users.add(user2);
		
		List<IrAcl> acls = new LinkedList<IrAcl>();
		acls.add(irAcl);
		acls.add(irAcl2);
		
		// create user entries for all given acls
		int count = uaceDAO.createUserControlEntriesForUsers(users, acls);
		assert count == 4 : "Should have 4 entries but have " + count;
		
		List<IrUserAccessControlEntry> entries = new LinkedList<IrUserAccessControlEntry>();
		
		entries.addAll(uaceDAO.getUserControlEntriesForUsers(irAcl, users));
		entries.addAll(uaceDAO.getUserControlEntriesForUsers(irAcl2, users));
		
		assert entries.size() == 4 : "Should find 4 entries but found " + entries.size();
		
		// create permissions for all given user control entries
	    List<IrClassTypePermission> permissions = new LinkedList<IrClassTypePermission>();
	    permissions.add(classTypePermission);
	    permissions.add(classTypePermission1);
	    
	    int permissionEntriesCount = uaceDAO.createPermissionsForUserControlEntries(entries, permissions);
	    assert permissionEntriesCount == 4 : "Should have 4 entries but have " + permissionEntriesCount;
	    
		
		// commit the transaction
		tm.commit(ts);
		
		
		// start a new transaction
		ts = tm.getTransaction(td);
		// clean up the database
		irAclDAO.makeTransient(irAclDAO.getById(irAcl.getId(), false));
		irAclDAO.makeTransient(irAclDAO.getById(irAcl2.getId(), false));
  		userDAO.makeTransient(userDAO.getById(user.getId(), false));
  		userDAO.makeTransient(userDAO.getById(user2.getId(), false));
		languageTypeDAO.makeTransient(languageTypeDAO.getById(lt.getId(), false));
		contributorTypeDAO.makeTransient(contributorTypeDAO.getById(ct.getId(), false));
		
		classTypePermissionDAO.makeTransient(classTypePermissionDAO.getById(classTypePermission.getId(), false));
		classTypePermissionDAO.makeTransient(classTypePermissionDAO.getById(classTypePermission1.getId(), false));
       
 	    irClassTypeDAO.makeTransient(irClassTypeDAO.getById(languageClassType.getId(), false));
 	    irClassTypeDAO.makeTransient(irClassTypeDAO.getById(contributorClassType.getId(), false));

		//commit the transaction
		tm.commit(ts); 
	}
}
