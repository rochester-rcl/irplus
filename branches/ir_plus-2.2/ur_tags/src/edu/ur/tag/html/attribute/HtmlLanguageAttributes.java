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


package edu.ur.tag.html.attribute;

/**
 * Language Attributes
 * 
 * Not valid in base, br, frame, frameset, hr, iframe, param, and script elements.
 * 
 * @author Nathan Sarr
 *
 */
public interface HtmlLanguageAttributes {
	
	/**
	 * Get the direction (left or right).
	 * 
	 * @return
	 */
	public String getDir();
	
	/**
	 * Set the direction (left or right).
	 */
	public void setDir(String dir);
	
	/**
	 * Get the language code.
	 * 
	 * @return
	 */
	public String getLang();
	
	/**
	 * Set the language code.
	 *  
	 */
	public void setLang(String lang);
	
    /**
     * Xml lang attributes
     * 
     * @return
     */
    public String getXmlLang();
    
    /**
     * Xml lang attributes.
     * 
     * @param xmlLang
     */
    public void setXmlLang(String xmlLang);
	
	/**
	 * For all non empty attributes, returns
	 * the attributes in the form attribute="value"
	 * 
	 * @return
	 */
	public StringBuffer getAttributes();
	

}
