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


package edu.ur.file.db.service;

import java.io.File;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.ur.file.checksum.ChecksumCalculator;
import edu.ur.file.checksum.ChecksumService;
import edu.ur.file.db.service.test.helper.ContextHolder;
import edu.ur.file.db.service.test.helper.PropertiesLoader;


/**
 * Test the service methods for the default checksum server service.
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultChecksumServiceTest {
	
	/** get the spring context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** Default file server service data access */
	ChecksumService checksumService = (ChecksumService) ctx
			.getBean("checksumService");
	
	/**  Properties file with testing specific information. */
	Properties properties;
	
	/**
	 * Setup for testing the file System manager
	 * this loads the properties file for getting
	 * the correct path for different file systems
	 */
	@BeforeClass
	public void setUp() {
		properties = new PropertiesLoader().getProperties();
	}
	
	public void testGetChecksumCalculator()
	{
		String pdfIndexFile = properties.getProperty("file_db_service_location") + properties.getProperty("pdf-index-file");
		
		ChecksumCalculator cc = checksumService.getChecksumCalculator("md5");
		assert cc != null : "Calculator for md5 was not found";
	    File f = new File(pdfIndexFile);
	    assert f.exists() : "file " + f.getAbsolutePath() + " does not exist";
	    assert f.canRead() : "Cannot read file " + f.getAbsolutePath();
	    String checksum = cc.calculate(f);
	    assert checksum.equals("6839764b09073771141896dc52ee8b41") : 
	    	"checksum equals = " + checksum + " but should equal "
	    	+ "6839764b09073771141896dc52ee8b41";
	    
	    String rtfIndexFile = properties.getProperty("file_db_service_location") + properties.getProperty("rtf-index-file");
	    
	    f = new File(rtfIndexFile);
	    assert f.exists(): "file " + f.getAbsolutePath() + " does not exist";
	    assert f.canRead(): "Cannot read file " + f.getAbsolutePath();
	    checksum = cc.calculate(f);
	    assert checksum.equals("d7736ab4ca7389555802cfa131b6656d") : 
	    	"checksum equals = " + checksum + " but should equal "
	    	+ "d7736ab4ca7389555802cfa131b6656d";
	}


}
