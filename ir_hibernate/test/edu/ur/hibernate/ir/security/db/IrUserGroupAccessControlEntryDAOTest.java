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
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrAclDAO;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypeDAO;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrClassTypePermissionDAO;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.IrUserGroupAccessControlEntryDAO;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.IrUserGroupDAO;


/**
 * Test the persistance methods for User Group Access Control Entries
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrUserGroupAccessControlEntryDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
    
	/** transaction manager for handling transactions */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** Transaction definition   */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    
    /** Class type data access  */
    IrClassTypeDAO irClassTypeDAO = (IrClassTypeDAO) ctx
    .getBean("irClassTypeDAO");
    
	/** Language type data access */
    LanguageTypeDAO languageTypeDAO = (LanguageTypeDAO) ctx
	.getBean("languageTypeDAO");
    
 	/** User relational data access  */
    IrUserGroupDAO userGroupDAO= (IrUserGroupDAO) ctx.getBean("irUserGroupDAO");
    
    /** User access relational data access */
    IrUserGroupAccessControlEntryDAO uaceDAO = (IrUserGroupAccessControlEntryDAO) ctx
    .getBean("irUserGroupAccessControlEntryDAO");  
	
	/** Class type permission information  */
	IrClassTypePermissionDAO classTypePermissionDAO = (IrClassTypePermissionDAO) 
	ctx.getBean("irClassTypePermissionDAO");

	/**
	 * Test User Access Control Entry DAO
	 */
	@Test
	public void baseUserControlEntryDAOTest() throws Exception{
		
		LanguageType lt = new LanguageType();
		lt.setName("languageName");
 		lt.setDescription("languageDescription");
        languageTypeDAO.makePersistent(lt);
        
	    IrClassType languageClassType = new IrClassType(LanguageType.class);
        irClassTypeDAO.makePersistent(languageClassType);
 		
		TransactionStatus ts = tm.getTransaction(td);
		
		IrAcl irAcl = new IrAcl(lt, languageClassType);
		IrAclDAO irAclDAO = (IrAclDAO) ctx.getBean("irAclDAO");
		
		irAclDAO.makePersistent(irAcl);
        //complete the transaction
		tm.commit(ts);
	
		IrUserGroup userGroup = new IrUserGroup("userGroup");
		userGroupDAO.makePersistent(userGroup);
	    
		// create the user group access control entry
		IrUserGroupAccessControlEntry  uace = 
			irAcl.createGroupAccessControlEntry(userGroup);

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
	    
		// start a new transaction
		ts = tm.getTransaction(td);
		
		IrUserGroupAccessControlEntry other = uaceDAO.getById( uace.getId(), false);
		assert other.equals(uace) : "User access control entries should be equal";
		assert uace.getAcl().equals(irAcl) : "Acl's should be equal";
		assert irAcl.getGroupAccessControlEntry(uace.getId()).equals(uace) : "Access control should bin in the irAcl";
		assert uace.getPermissions().size() == 2 : "Should have at least one permission";
		assert uace.getIrClassTypePermissions().contains(classTypePermission) : "Should equal the class type permission";
		assert uace.getUserGroup().equals(userGroup) : "User groups should be equal";
		assert uaceDAO.getCount() == 1 : "Should have one uace but have " + uaceDAO.getCount();
		
		Long count = uaceDAO.getUserGroupPermissionCountForClassType(userGroup, classTypePermission, classTypePermission.getIrClassType());
		assert  count == 1l : "The count should equal 1 but equals " + count;

		count = uaceDAO.getUserGroupPermissionCountForClassTypeObject(userGroup, classTypePermission, 
				classTypePermission.getIrClassType(), lt.getId());

		assert  count == 1l : "The count should equal 1 but equals " + count;

		//make sure we can get the acl for the role
		List<IrAcl> acls = irAclDAO.getAllAclsForSid(userGroup);
		assert acls.size() == 1 : "Should be able to find 1 acl for user " + userGroup +
		" but found " + acls.size();
		
		// commit the transaction
		tm.commit(ts);
		
		// clean up the database
		irAclDAO.makeTransient(irAclDAO.getById(irAcl.getId(), false));
		assert uaceDAO.getById(uace.getId(), false) == null : "Should not be able to find the access control entry";
		
 	    classTypePermissionDAO.makeTransient(classTypePermission);
 	    classTypePermissionDAO.makeTransient(classTypePermission1);
 	    
 	    irClassTypeDAO.makeTransient(languageClassType);
 	    
		// start a new transaction
		ts = tm.getTransaction(td);
 		userGroupDAO.makeTransient(userGroupDAO.getById(userGroup.getId(), false));
		languageTypeDAO.makeTransient(languageTypeDAO.getById(lt.getId(), false));
        //commit the transaction
		tm.commit(ts); 
	}
}
