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
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.cgLib.CgLibHelper;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeDAO;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrAclDAO;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypeDAO;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrClassTypePermissionDAO;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.security.IrUserAccessControlEntryDAO;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.IrUserGroupAccessControlEntryDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.IrUserGroupDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;
import edu.ur.ir.security.Sid;

/**
 * Test the persistence methods for Access Control lists
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrAclDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

    /** Class type data access */
    IrClassTypeDAO irClassTypeDAO = (IrClassTypeDAO) ctx
    .getBean("irClassTypeDAO");

    /** Language type data access */
    LanguageTypeDAO languageTypeDAO = (LanguageTypeDAO) ctx
	.getBean("languageTypeDAO");
    
    /** the Institutional repository acl relational data access */
    IrAclDAO irAclDAO = (IrAclDAO) ctx.getBean("irAclDAO");
    
	/** Platform transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

    /** Data access for class type permissions. */
    IrClassTypePermissionDAO classTypePermissionDAO = (IrClassTypePermissionDAO) 
    ctx.getBean("irClassTypePermissionDAO");

    /** User access control entry  */
    IrUserAccessControlEntryDAO uaceDAO = (IrUserAccessControlEntryDAO) ctx
    .getBean("irUserAccessControlEntryDAO");
    
    /** User access relational data access */
    IrUserGroupAccessControlEntryDAO ugaceDAO = (IrUserGroupAccessControlEntryDAO) ctx
    .getBean("irUserGroupAccessControlEntryDAO");  

 	/** User relational data access  */
    IrUserGroupDAO userGroupDAO= (IrUserGroupDAO) ctx.getBean("irUserGroupDAO");

    /** User relational data access */
    IrUserDAO userDAO= (IrUserDAO) ctx
	.getBean("irUserDAO");
    
	/**
	 * Test Access Control lists DAO
	 */
	public void baseIrAclDAOTest() throws Exception{
	
		LanguageType lt = new LanguageType();
		lt.setName("languageName");
 		lt.setDescription("languageDescription");
         
 		// create a language type to protect with permissions
        languageTypeDAO.makePersistent(lt);
	    IrClassType languageClassType = new IrClassType(LanguageType.class);
        irClassTypeDAO.makePersistent(languageClassType);
		
        // start a new transaction
        TransactionStatus ts = tm.getTransaction(td);
		LanguageType myLanguageType = languageTypeDAO.getById(lt.getId(), false);
		
        // create an access control list for the given object identity
        IrAcl irAcl = new IrAcl(myLanguageType, languageClassType);
		irAclDAO.makePersistent(irAcl);
		IrAcl other = irAclDAO.getById(irAcl.getId(), false);
		
		assert other.equals(irAcl) : "IrAcl's should be the same";
		
        //complete the transaction
		tm.commit(ts);
			
		// clean up the database
		irAclDAO.makeTransient(other);
 	    irClassTypeDAO.makeTransient(languageClassType);
 	    assert irAclDAO.getById(irAcl.getId(), false) == null : "IrAcls not find irAcl";
 	    languageTypeDAO.makeTransient(languageTypeDAO.getById(lt.getId(), false));
 	    
	}
	
	/**
	 * Test Access Control lists DAO for a user and getting the access
	 * control list for that user
	 */
	@Test
	public void irAclUserDAOTest() throws Exception{
	
		LanguageType lt = new LanguageType();
		lt.setName("languageName");
 		lt.setDescription("languageDescription");
         
 		// create a language type to protect with permissions
        languageTypeDAO.makePersistent(lt);
        
	    IrClassType languageClassType = new IrClassType(LanguageType.class);
        irClassTypeDAO.makePersistent(languageClassType);
        
        // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
    
        // create a new access control list
		IrAcl irAcl = new IrAcl(lt, languageClassType);
		irAclDAO.makePersistent(irAcl);
        //complete the transaction
		tm.commit(ts);
		
		UserEmail userEmail = new UserEmail("user@email");

		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("password", "userName");
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.addUserEmail(userEmail, true);
		
		// save the user
		userDAO.makePersistent(user);
	    
		// create the user access control entry
		IrUserAccessControlEntry  uace = irAcl.createUserAccessControlEntry(user);
 	
	    IrClassTypePermission classTypePermission = new IrClassTypePermission(languageClassType);
	    classTypePermission.setName("permission");
	    classTypePermission.setDescription("permissionDescription");

	    
	    // save the class type permission
	    classTypePermissionDAO.makePersistent(classTypePermission);
	    uace.addPermission(classTypePermission);
	    
        // persist the access control entry.

	    uaceDAO.makePersistent(uace);    
	    
		// start a new transaction
		ts = tm.getTransaction(td);
		
		IrAcl acl = irAclDAO.getAcl(lt.getId(), CgLibHelper.cleanClassName(lt.getClass().getName()), user);
		
		assert acl.equals(irAcl): "Acl should be equal to " + irAcl;
		
		IrUserAccessControlEntry other = acl.getUserAccessControlEntry(uace.getId());
		
		assert other.equals(uace) : "User access control entries should be equal";
		assert uace.getAcl().equals(irAcl) : "Acl's should be equal";
		assert irAcl.getUserAccessControlEntry(uace.getId()).equals(uace) : "Access control should bin in the irAcl";
		assert uace.getPermissions().size() == 1 : "Should have at least one permission";
		assert uace.getIrClassTypePermissions().contains(classTypePermission) : "Should equal the class type permission";
		assert uace.getIrUser().equals(user) : "Users should be equal";
		assert uaceDAO.getCount() == 1 : "Should have one uace";
		
		// commit the transaction
		tm.commit(ts);
		
		// clean up the database
		irAclDAO.makeTransient(irAclDAO.getById(irAcl.getId(), false));
		
		assert uaceDAO.getById(uace.getId(), false) == null : "Should not be able to find the access control entry";
		
 	    classTypePermissionDAO.makeTransient(classTypePermission);
 	    irClassTypeDAO.makeTransient(languageClassType);
 	    
        // Start the transaction 
		ts = tm.getTransaction(td);
		
 		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		
		languageTypeDAO.makeTransient(languageTypeDAO.getById(lt.getId(), false));
        //commit the transaction
		tm.commit(ts);
		
	}
	
	/**
	 *  make sure we can identify a user who has permissions to a given object
	 */
	@Test
	public void irAclUserDAOHasPermissionCountTest() throws Exception{
	
		LanguageType lt = new LanguageType();
		lt.setName("languageName");
 		lt.setDescription("languageDescription");
         
 		// create a language type to protect with permissions
        languageTypeDAO.makePersistent(lt);
        
	    IrClassType languageClassType = new IrClassType(LanguageType.class);
        irClassTypeDAO.makePersistent(languageClassType);
        
        // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
    
        // create a new access control list
		IrAcl irAcl = new IrAcl(lt, languageClassType);
		irAclDAO.makePersistent(irAcl);
        //complete the transaction
		tm.commit(ts);
		
		UserEmail userEmail = new UserEmail("user@email");
		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("password", "userName");
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.addUserEmail(userEmail, true);
		
		// save the user
		userDAO.makePersistent(user);
		
		UserEmail userEmail2 = new UserEmail("blah@email");
		IrUser user2 = userManager.createUser("password2", "userName2");
		user2.setLastName("familyName");
		user2.setFirstName("forename");
		user2.addUserEmail(userEmail2, true);
		
		userDAO.makePersistent(user2);
	    
		// create the user access control entry
		IrUserAccessControlEntry  uace = irAcl.createUserAccessControlEntry(user);
 	
	    IrClassTypePermission classTypePermission = new IrClassTypePermission(languageClassType);
	    classTypePermission.setName("permission");
	    classTypePermission.setDescription("permissionDescription");

	    
	    // save the class type permission
	    classTypePermissionDAO.makePersistent(classTypePermission);
	    uace.addPermission(classTypePermission);
	    
        // persist the access control entry.

	    uaceDAO.makePersistent(uace);    
	    
		// start a new transaction
		ts = tm.getTransaction(td);
		// user has been given the permission
		Long count = irAclDAO.hasPermission(lt.getId(), languageClassType.getName(), user, classTypePermission.getName());
		assert count.equals(1l) : "Count should equal 1 but equals " + count;

		// user 2 has not been given direct permission
		count = irAclDAO.hasPermission(lt.getId(), languageClassType.getName(), user2, classTypePermission.getName());
		assert count.equals(0l) : "Count should equal 0 but equals " + count;

		
		// commit the transaction
		tm.commit(ts);
		
		// test permissions given through a group
		ts = tm.getTransaction(td);
		IrUserGroup userGroup = new IrUserGroup("userGroup");
		userGroup.addUser(user);
		userGroup.addUser(user2);
		userGroupDAO.makePersistent(userGroup);
	    
		// create the user GROUP access control entry
		IrUserGroupAccessControlEntry  ugace = 
			irAcl.createGroupAccessControlEntry(userGroup);
		
		ugace.addPermission(classTypePermission);
		ugaceDAO.makePersistent(ugace);

		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// user has been given the permission
		count = irAclDAO.hasPermission(lt.getId(), languageClassType.getName(), user, classTypePermission.getName());
		assert count.equals(2l) : "Count should equal 2 but equals " + count;

		// user 2 has not been given direct permission
		count = irAclDAO.hasPermission(lt.getId(), languageClassType.getName(), user2, classTypePermission.getName());
		assert count.equals(1l) : "Count should equal 1 but equals " + count;
		tm.commit(ts);
		
		
		// clean up the database
		irAclDAO.makeTransient(irAclDAO.getById(irAcl.getId(), false));
		
		assert uaceDAO.getById(uace.getId(), false) == null : "Should not be able to find the access control entry";
		
 	    classTypePermissionDAO.makeTransient(classTypePermission);
 	    irClassTypeDAO.makeTransient(languageClassType);
 	    
        // Start the transaction 
		ts = tm.getTransaction(td);
		
		userGroupDAO.makeTransient(userGroupDAO.getById(userGroup.getId(), false));
 		userDAO.makeTransient(userDAO.getById(user.getId(), false));
 		userDAO.makeTransient(userDAO.getById(user2.getId(), false));
		
		languageTypeDAO.makeTransient(languageTypeDAO.getById(lt.getId(), false));
        //commit the transaction
		tm.commit(ts);
		
	}
	
	/**
	 *  make sure we can identify a user who has permissions to a given object
	 */
	@Test
	public void irAclGetSidsWithPermissionDAOTest() throws Exception{
	
		LanguageType lt = new LanguageType();
		lt.setName("languageName");
 		lt.setDescription("languageDescription");
         
 		// create a language type to protect with permissions
        languageTypeDAO.makePersistent(lt);
        
	    IrClassType languageClassType = new IrClassType(LanguageType.class);
        irClassTypeDAO.makePersistent(languageClassType);
        
        // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
    
        // create a new access control list
		IrAcl irAcl = new IrAcl(lt, languageClassType);
		irAclDAO.makePersistent(irAcl);
        //complete the transaction
		tm.commit(ts);
		
		UserEmail userEmail = new UserEmail("user@email");
		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("password", "userName");
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.addUserEmail(userEmail, true);
		
		// save the user
		userDAO.makePersistent(user);
		
		UserEmail userEmail2 = new UserEmail("blah@email");
		IrUser user2 = userManager.createUser("password2", "userName2");
		user2.setLastName("familyName");
		user2.setFirstName("forename");
		user2.addUserEmail(userEmail2, true);
		
		userDAO.makePersistent(user2);
	    
		// create the user access control entry
		IrUserAccessControlEntry  uace = irAcl.createUserAccessControlEntry(user);
 	
	    IrClassTypePermission classTypePermission = new IrClassTypePermission(languageClassType);
	    classTypePermission.setName("permission");
	    classTypePermission.setDescription("permissionDescription");

	    
	    // save the class type permission
	    classTypePermissionDAO.makePersistent(classTypePermission);
	    uace.addPermission(classTypePermission);
	    
        // persist the access control entry.

	    uaceDAO.makePersistent(uace);    
	    
		// start a new transaction
		ts = tm.getTransaction(td);
		Set<Sid> sids = irAclDAO.getSidsWithPermissionForObject(lt.getId(), languageClassType.getName(), classTypePermission.getName());
		assert sids.contains(user) : "Sids should contain " + user + " but doesn't";
		assert !sids.contains(user2) : "Sids should NOT contain " + user2 + " but does";
		
		List<Sid> specificSids = new LinkedList<Sid>();
		specificSids.add(user2);
		sids = irAclDAO.getSidsWithPermissionForObject(lt.getId(), languageClassType.getName(), 
				classTypePermission.getName(), specificSids);

		assert sids.size() == 0 : "Sids size should be 0 but is " + sids.size();
		
		specificSids.add(user);
		sids = irAclDAO.getSidsWithPermissionForObject(lt.getId(), languageClassType.getName(), 
				classTypePermission.getName(), specificSids);
		
		assert sids.contains(user) : "Should contain user " + user + " but does not";
		// commit the transaction
		tm.commit(ts);
		
		// test permissions given through a group
		ts = tm.getTransaction(td);
		IrUserGroup userGroup = new IrUserGroup("userGroup");
		userGroup.addUser(user);
		userGroup.addUser(user2);
		userGroupDAO.makePersistent(userGroup);
	    
		// create the user GROUP access control entry
		IrUserGroupAccessControlEntry  ugace = 
			irAcl.createGroupAccessControlEntry(userGroup);
		
		ugace.addPermission(classTypePermission);
		ugaceDAO.makePersistent(ugace);

		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		sids = irAclDAO.getSidsWithPermissionForObject(lt.getId(), languageClassType.getName(), classTypePermission.getName());
		assert sids.contains(user) : "Sids should contain " + user + " but doesn't";
		assert !sids.contains(user2) : "Sids should NOT contain " + user2 + " but does";
        assert sids.contains(userGroup) : "Sids should contain " + userGroup + "but doesn't";		
		
        
        
        
		specificSids = new LinkedList<Sid>();
		specificSids.add(user2);
		sids = irAclDAO.getSidsWithPermissionForObject(lt.getId(), languageClassType.getName(), 
				classTypePermission.getName(), specificSids);

		assert sids.size() == 0 : "Sids size should be 0 but is " + sids.size();
		
		specificSids.add(user);
		sids = irAclDAO.getSidsWithPermissionForObject(lt.getId(), languageClassType.getName(), 
				classTypePermission.getName(), specificSids);
		
		assert sids.size() == 1 : "Sids size should be 1 but is " + sids.size();
		assert sids.contains(user) : "Should contain user " + user + " but does not";
		
		specificSids.add(userGroup);
		sids = irAclDAO.getSidsWithPermissionForObject(lt.getId(), languageClassType.getName(), 
				classTypePermission.getName(), specificSids);
		
		assert sids.size() == 2 : "Sids size should be 2 but is " + sids.size();
		assert sids.contains(user) : "Should contain user " + user + " but does not";
		assert sids.contains(userGroup) : "Should contain user group " + userGroup + " but does not";
        
        
        
        
        tm.commit(ts);
		
		
		// clean up the database
		irAclDAO.makeTransient(irAclDAO.getById(irAcl.getId(), false));
		
		assert uaceDAO.getById(uace.getId(), false) == null : "Should not be able to find the access control entry";
		
 	    classTypePermissionDAO.makeTransient(classTypePermission);
 	    irClassTypeDAO.makeTransient(languageClassType);
 	    
        // Start the transaction 
		ts = tm.getTransaction(td);
		
		userGroupDAO.makeTransient(userGroupDAO.getById(userGroup.getId(), false));
 		userDAO.makeTransient(userDAO.getById(user.getId(), false));
 		userDAO.makeTransient(userDAO.getById(user2.getId(), false));
		
		languageTypeDAO.makeTransient(languageTypeDAO.getById(lt.getId(), false));
        //commit the transaction
		tm.commit(ts);
		
	}

	
}
