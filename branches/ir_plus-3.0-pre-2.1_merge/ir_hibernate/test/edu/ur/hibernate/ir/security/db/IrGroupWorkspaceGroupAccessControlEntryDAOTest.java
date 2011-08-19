/**  
   Copyright 2008 - 2011 University of Rochester

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
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceGroup;
import edu.ur.ir.groupspace.GroupWorkspaceGroupDAO;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeDAO;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrAclDAO;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypeDAO;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrClassTypePermissionDAO;
import edu.ur.ir.security.IrGroupWorkspaceGroupAccessControlEntry;
import edu.ur.ir.security.IrGroupWorkspaceGroupAccessControlEntryDAO;



/**
 * Test the persistance methods for Workspace Group Access Control Entries
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrGroupWorkspaceGroupAccessControlEntryDAOTest {

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
    
    /** User access relational data access */
    IrGroupWorkspaceGroupAccessControlEntryDAO wgaceDAO = (IrGroupWorkspaceGroupAccessControlEntryDAO) ctx
    .getBean("irGroupWorkspaceGroupAccessControlEntryDAO");  
	
	/** Class type permission information  */
	IrClassTypePermissionDAO classTypePermissionDAO = (IrClassTypePermissionDAO) 
	ctx.getBean("irClassTypePermissionDAO");
	
	// group workspace group data access
	GroupWorkspaceGroupDAO groupWorkspaceGroupDAO = (GroupWorkspaceGroupDAO) ctx
	.getBean("groupWorkspaceGroupDAO");

	// group workspace data access
	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");

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
		IrAclDAO irAclDAO = (IrAclDAO) ctx.getBean("irAclDAO");
		
		irAclDAO.makePersistent(irAcl);
        //complete the transaction
		tm.commit(ts);
	
		
		
		
		tm.getTransaction(td);
		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupSpace);
        GroupWorkspaceGroup group = groupSpace.createGroup("groupName");
        group.setDescription("groupDescription");
 		groupWorkspaceGroupDAO.makePersistent(group);
	    
		// create the user group access control entry
		IrGroupWorkspaceGroupAccessControlEntry  wgace = 
			irAcl.createGroupWorkspaceGroupAccessControlEntry(group);

 	    IrClassTypePermission classTypePermission = new IrClassTypePermission(languageClassType);
		classTypePermission.setName("permissionName");
		classTypePermission.setDescription("permissionDescription");

		IrClassTypePermission classTypePermission1 = new IrClassTypePermission(languageClassType);
		classTypePermission1.setName("permissionName1");
		classTypePermission1.setDescription("permissionDescription1");
         
	    
	    // save the class type permission
	    classTypePermissionDAO.makePersistent(classTypePermission);
	    classTypePermissionDAO.makePersistent(classTypePermission1);

	    wgace.addPermission(classTypePermission1);
	    wgace.addPermission(classTypePermission);
	    wgaceDAO.makePersistent(wgace);    
	    tm.commit(ts);
	    
		// start a new transaction
		ts = tm.getTransaction(td);
		
		IrGroupWorkspaceGroupAccessControlEntry other = wgaceDAO.getById( wgace.getId(), false);
		assert other.equals(wgace) : "User access control entries should be equal";
		assert wgace.getAcl().equals(irAcl) : "Acl's should be equal";
		assert irAcl.getGroupAccessControlEntry(wgace.getId()).equals(wgace) : "Access control should bin in the irAcl";
		assert wgace.getPermissions().size() == 2 : "Should have at least one permission";
		assert wgace.getIrClassTypePermissions().contains(classTypePermission) : "Should equal the class type permission";
		assert wgace.getWorkspaceGroup().equals(group) : "User groups should be equal";
		assert wgaceDAO.getCount() == 1 : "Should have one uace but have " + wgaceDAO.getCount();
		
		Long count = wgaceDAO.getPermissionCountForClassType(group, classTypePermission, classTypePermission.getIrClassType());
		assert  count == 1l : "The count should equal 1 but equals " + count;

		count = wgaceDAO.getPermissionCountForClassTypeObject(group, classTypePermission, 
				classTypePermission.getIrClassType(), lt.getId());

		assert  count == 1l : "The count should equal 1 but equals " + count;

		//make sure we can get the acl for the role
		List<IrAcl> acls = irAclDAO.getAllAclsForSid(group);
		assert acls.size() == 1 : "Should be able to find 1 acl for user " + group +
		" but found " + acls.size();
		
		// commit the transaction
		tm.commit(ts);
		
		// clean up the database
		// start a new transaction
		ts = tm.getTransaction(td);
		irAclDAO.makeTransient(irAclDAO.getById(irAcl.getId(), false));
		assert wgaceDAO.getById(wgace.getId(), false) == null : "Should not be able to find the access control entry";
		
 	    classTypePermissionDAO.makeTransient(classTypePermission);
 	    classTypePermissionDAO.makeTransient(classTypePermission1);
 	    
 	    irClassTypeDAO.makeTransient(languageClassType);
 	    
		
 	    groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupSpace.getId(), false));
		languageTypeDAO.makeTransient(languageTypeDAO.getById(lt.getId(), false));
        //commit the transaction
		tm.commit(ts); 
	}
	

}
