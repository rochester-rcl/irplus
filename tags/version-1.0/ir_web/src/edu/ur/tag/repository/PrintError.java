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


package edu.ur.tag.repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;


/**
 * Prints out a specified error.
 * 
 * @author Nathan Sarr
 *
 */
public class PrintError extends SimpleTagSupport{
	
	/** map which holds the errors */
	@SuppressWarnings("unchecked")
	private Map errors;
	
	/** key for the error */
	private String key;
	
	/**  Logger for managing users */
	private static final Logger log = Logger.getLogger(PrintError.class);
	
	@SuppressWarnings("unchecked")
	public void doTag() throws JspException, IOException {
		
		if( errors != null && errors.size() > 0)
		{
			log.debug("errors = " + errors.toString());
			log.debug("errors size = " + errors.size());
			if( key != null)
			{
				JspWriter out = this.getJspContext().getOut();
				List errorList = (List)errors.get(key);
				if( errorList != null && errorList.size() > 0)
				{
					log.debug( "error list = " + errorList.toString() );
					String error = (String)errorList.get(0);
					out.write(error);
				}
				else
				{
					log.debug( "error list is null or size is 0");
				}
			}
		}
	}

	/**
	 * Set of errors to look in.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getErrors() {
		return errors;
	}

	/**
	 * Set the set of errors.
	 * 
	 * @param errors
	 */
	@SuppressWarnings("unchecked")
	public void setErrors(Map errors) {
		this.errors = errors;
	}

	/**
	 * Get the key used to retrieve errors.
	 * 
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set the key to retrieve errors.
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

}
