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

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class for exporting groups.
 * 
 * @author Nathan Sarr
 *
 */
public class DspaceGroupExporter {
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		// path where the xml file will be created
		String xmlFilePath = args[0];
		
		/** get the application context */
		ApplicationContext ctx  = new ClassPathXmlApplicationContext(
		"applicationContext.xml");

		File f = new File(xmlFilePath);
	    String path = f.getCanonicalPath();
		path = path +  File.separatorChar + GroupExporter.XML_FILE_NAME;
		GroupExporter groupExporter = (GroupExporter)ctx.getBean("groupExporter");
		File xmlFile = new File(path);
	
		System.out.println("Writing to file " + xmlFile.getCanonicalPath());
		groupExporter.generateGroupXMLFile(xmlFile, groupExporter.getGroups());
	}

}
