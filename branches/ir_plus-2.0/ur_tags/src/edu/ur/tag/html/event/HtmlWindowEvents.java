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


package edu.ur.tag.html.event;

/**
 * Only valid in body and frameset elements
 * 
 * @author Nathan Sarr
 *
 */
public interface HtmlWindowEvents {
	
	/**
	 * Script to be run when a document loads
	 * 
	 * @param onLoad
	 */
	public void setOnLoad(String onLoad);
	
	/**
	 * Script to be run when a document loads
	 * 
	 * @return
	 */
	public String getOnLoad();
	
	/**
	 * Script to be run when a document unloads
	 * 
	 * @param onUnLoad
	 */
	public void setOnUnLoad(String onUnLoad);
	
	/**
	 * Script to be run when a document unloads
	 * 
	 * @return
	 */
	public String getOnUnLoad();
	
	/**
	 * For all non empty attributes, returns
	 * the attributes in the form attribute="value"
	 * 
	 * @return
	 */
	public StringBuffer getAttributes();


}
