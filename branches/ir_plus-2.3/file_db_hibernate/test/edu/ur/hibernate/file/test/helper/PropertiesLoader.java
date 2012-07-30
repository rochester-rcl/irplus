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

package edu.ur.hibernate.file.test.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
	
	public static final String UNIX_PATH_SEPERATOR = "/";
	public static final String WINDOWS_PATH_SEPERATOR = "\\";

	
	public Properties getProperties()
	{
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = null;
		if( File.separator.equals(UNIX_PATH_SEPERATOR))
		{
		    is = cl.getResourceAsStream("testing_unix.properties");
		}
		else if( File.separator.equals(WINDOWS_PATH_SEPERATOR))
		{
			is = cl.getResourceAsStream("testing_windows.properties");
		}
		else
		{
			throw new RuntimeException("Could not determine system type");
		}
		Properties properties = new Properties();
	    
		try {
	        properties.load(is);
	    } catch (IOException e) {
	        throw new IllegalStateException("Could not read testing.properties file");
	    }
	    
	    return properties;
	}


}
