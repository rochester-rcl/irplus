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

package edu.ur.dspace.load;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.dspace.model.Community;
import edu.ur.dspace.model.EpersonPermission;
import edu.ur.dspace.model.GroupPermission;
import edu.ur.dspace.model.Link;
import edu.ur.dspace.test.ContextHolder;
import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.dspace.test.PropertiesLoader;

/**
 * Help testing with community import.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class CommunityImporterTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	CommunityImporter communityImporter = (CommunityImporter)ctx.getBean("communityImporter");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	public void testCommunityImport() throws IOException, DuplicateNameException, IllegalFileSystemNameException
	{
		File f = new File("C:\\communities.xml");
	    List<Community> communities = communityImporter.getCommunities(f);
	    
	    for(Community c : communities )
	    {
	    	System.out.println("Found community " + c);
	    	for(Link link : c.links)
	    	{
	    		System.out.println("link = " + link);
	    	}
	    	
	    	for(GroupPermission groupPermission : c.groupPermissions)
	    	{
	    		System.out.println("group permission = " + groupPermission);
	    	}
	    	
	    	for(EpersonPermission epersonPermission : c.epersonPermissions)
	    	{
	    		System.out.println("eperson permission = " + epersonPermission);
	    	}
	    }
		
	}
}
