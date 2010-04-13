/**  
   Copyright 2008-2010 University of Rochester

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
package edu.ur.hibernate.metadata.helper.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Helper class to make sure only one application context is created for 
 * testing.
 * 
 * @author Nathan Sarr
 *
 */
public class ContextHolder {
	
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext(
	"applicationContext.xml");

	private ContextHolder() {}
	
	public static ApplicationContext getApplicationContext()
	{
		return ctx;
	}

}
