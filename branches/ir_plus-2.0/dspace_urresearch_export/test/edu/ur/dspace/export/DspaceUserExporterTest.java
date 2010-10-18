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

package edu.ur.dspace.export;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import edu.ur.dspace.model.DspaceUser;
import edu.ur.dspace.test.ContextHolder;

@Test(groups = { "baseTests" }, enabled = true)
public class DspaceUserExporterTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Exporter for users */
	UserExporter userExporter = (UserExporter)ctx.getBean("userExporter");
	
	/**
	 * Try getting all the users from the database.
	 * 
	 * @throws IOException
	 */
	public void testGetUsers() throws IOException
	{
		List<DspaceUser> users = userExporter.getUsers();
		
		for(DspaceUser u : users)
		{
			System.out.println("User u = " + u);
		}
	}
	
	public void testGenerateUserXMLFile() throws IOException
	{
		String letmein = "b7a875fc1ea228b9061041b7cec4bd3c52ab3ce3";
		userExporter.generateUserXMLFile(new File("C:\\users.xml"), userExporter.getUsers(), letmein);
	}
	
}
