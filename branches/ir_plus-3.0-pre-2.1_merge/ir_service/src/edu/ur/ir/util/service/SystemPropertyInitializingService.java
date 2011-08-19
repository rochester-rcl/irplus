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


package edu.ur.ir.util.service;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

/**
 * Initialize the set of system properties.
 * 
 * @author Nathan Sarr
 *
 */
public class SystemPropertyInitializingService implements InitializingBean {

	        /** Properties to be set */
	        private Map<String, String> systemProperties;
	        
	    	/**  Get the logger for this class */
	    	private static final Logger log = Logger.getLogger(SystemPropertyInitializingService.class);

	        /** Sets the system properties */
	        public void afterPropertiesSet() 
	               throws Exception {
	                if (systemProperties == null || 
	                       systemProperties.isEmpty()) {
	                        // No properties to initialize
	                        return;
	                }

	                Iterator<String> i = systemProperties.keySet().iterator();
	                while (i.hasNext()) {
	                        String key = (String) i.next();
	                        String value = (String) systemProperties.get(key);
	                        if(log.isDebugEnabled())
	                        {	
	                            log.debug("Setting system property key = " + key + " value = " + value);
	                        }
	                        System.setProperty(key, value);
	                }
	        }

	        public void setSystemProperties(Map<String, String> systemProperties) {
	                this.systemProperties = systemProperties;
	        }
}
