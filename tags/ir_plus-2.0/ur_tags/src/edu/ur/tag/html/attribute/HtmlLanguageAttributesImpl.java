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

import edu.ur.tag.TagUtil;

/**
 * Implementation of the language attributes.
 * 
 * @author Nathan Sarr
 *
 */
public class HtmlLanguageAttributesImpl implements HtmlLanguageAttributes{
	
	private String dir;
	private String lang;
	private String xmlLang;


	/**
	 * Get the language attributes.
	 * 
	 * @see edu.ur.tag.html.attribute.HtmlLanguageAttributes#getLanguageAttributes()
	 */
	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
	    if(!TagUtil.isEmpty(dir)){ sb.append("dir=\"" + dir + "\" ");}  
	    if(!TagUtil.isEmpty(lang)){sb.append("lang=\"" + lang + "\" ");}  
    	if(!TagUtil.isEmpty(xmlLang)) { sb.append("xml:lang=\"" + xmlLang + "\" "); }   
		return sb;
	}

	/**
	 * Get the language direction
	 * 
	 * @see edu.ur.tag.html.attribute.HtmlLanguageAttributes#getDir()
	 */
	public String getDir() {
		return dir;
	}


	/**
	 * Set the language direction.
	 * 
	 * @see edu.ur.tag.html.attribute.HtmlLanguageAttributes#setDir(java.lang.String)
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}


	/**
	 * Get the language
	 * 
	 * @see edu.ur.tag.html.attribute.HtmlLanguageAttributes#getLang()
	 */
	public String getLang() {
		return lang;
	}


	/**
	 * Set the language.
	 * 
	 * @see edu.ur.tag.html.attribute.HtmlLanguageAttributes#setLang(java.lang.String)
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String getXmlLang() {
		return xmlLang;
	}

	public void setXmlLang(String xmlLang) {
		this.xmlLang = xmlLang;
	}

}
