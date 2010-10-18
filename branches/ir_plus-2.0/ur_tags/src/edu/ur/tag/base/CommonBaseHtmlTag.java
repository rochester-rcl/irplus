
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

package edu.ur.tag.base;

import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.ur.tag.TagUtil;
import edu.ur.tag.html.attribute.HtmlCoreAttributes;
import edu.ur.tag.html.attribute.HtmlLanguageAttributes;
import edu.ur.tag.html.event.HtmlKeyboardEvents;
import edu.ur.tag.html.event.HtmlMouseEvents;

/**
 * Aggregation of common element needs
 * 
 * @author Nathan Sarr
 *
 */
public class CommonBaseHtmlTag extends SimpleTagSupport implements HtmlCoreAttributes,  HtmlKeyboardEvents, HtmlMouseEvents, HtmlLanguageAttributes
{
	/** id of the element */
	protected String id;
	
	/** cascading style sheet class  */
	protected String cssClass; 

	/** title for the element*/
	protected String title;
	
	/** style of the element  */
	protected String style;
	
	/** xml language attribute  */
	protected String xmlLang;
	
	/** direction of the text */
	protected String dir;
	
	/** html language type  */
	protected String lang;
	
	/** on key down event */
	protected String onKeyDown;
	
	/** on key press event */
	protected String onKeyPress;
	
	/** on key up event  */
	protected String onKeyUp;
	
	/** on click event  */
	protected String onClick;
	
	/** on double click event */
	protected String onDblClick;
	
	/** on mouse down event */
	protected String onMouseDown;
	
	/** on mouse move event */
	protected String onMouseMove;
	
	/** on mouse over event */
	protected String onMouseOver;
	
	/** on mouse out event */
	protected String onMouseOut;
	
	/** on mouse up event  */
	protected String onMouseUp;
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#getCssClass()
	 */
	public String getCssClass() {
		return cssClass;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#setCssClass(java.lang.String)
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#getId()
	 */
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#getStyle()
	 */
	public String getStyle() {
		return style;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#setStyle(java.lang.String)
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#getTitle()
	 */
	public String getTitle() {
		return title;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
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
	
	/**
	 * Set the xml language
	 * 
	 * @see edu.ur.tag.html.attribute.HtmlLanguageAttributes#getXmlLang()
	 */
	public String getXmlLang() {
		return xmlLang;
	}

	/**
	 * Set the xml language
	 * 
	 * @see edu.ur.tag.html.attribute.HtmlLanguageAttributes#setXmlLang(java.lang.String)
	 */
	public void setXmlLang(String xmlLang) {
		this.xmlLang = xmlLang;
	}
	
	/**
	 * Get the on key down event
	 * 
	 * @see edu.ur.tag.html.event.HtmlKeyboardEvents#getOnKeyDown()
	 */
	public String getOnKeyDown() {
		return onKeyDown;
	}
	
	/**
	 * Set the on key down event
	 * 
	 * @see edu.ur.tag.html.event.HtmlKeyboardEvents#setOnKeyDown(java.lang.String)
	 */
	public void setOnKeyDown(String onKeyDown) {
		this.onKeyDown = onKeyDown;
	}
	
	/**
	 * Get the on key press event.
	 * 
	 * @see edu.ur.tag.html.event.HtmlKeyboardEvents#getOnKeyPress()
	 */
	public String getOnKeyPress() {
		return onKeyPress;
	}
	
	/**
	 * Set the on key press event
	 * 
	 * @see edu.ur.tag.html.event.HtmlKeyboardEvents#setOnKeyPress(java.lang.String)
	 */
	public void setOnKeyPress(String onKeyPress) {
		this.onKeyPress = onKeyPress;
	}
	
	/**
	 * Get the on key up event
	 * 
	 * @see edu.ur.tag.html.event.HtmlKeyboardEvents#getOnKeyUp()
	 */
	public String getOnKeyUp() {
		return onKeyUp;
	}
	
	/**
	 * Set the on key up event
	 * 
	 * @see edu.ur.tag.html.event.HtmlKeyboardEvents#setOnKeyUp(java.lang.String)
	 */
	public void setOnKeyUp(String onKeyUp) {
		this.onKeyUp = onKeyUp;
	}
	
	public String getOnClick() {
		return onClick;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public String getOnDblClick() {
		return onDblClick;
	}

	public void setOnDblClick(String onDblClick) {
		this.onDblClick = onDblClick;
	}

	public String getOnMouseDown() {
		return onMouseDown;
	}

	public void setOnMouseDown(String onMouseDown) {
		this.onMouseDown = onMouseDown;
	}

	public String getOnMouseMove() {
		return onMouseMove;
	}

	public void setOnMouseMove(String onMouseMove) {
		this.onMouseMove = onMouseMove;
	}

	public String getOnMouseOut() {
		return onMouseOut;
	}

	public void setOnMouseOut(String onMouseOut) {
		this.onMouseOut = onMouseOut;
	}

	public String getOnMouseOver() {
		return onMouseOver;
	}

	public void setOnMouseOver(String onMouseOver) {
		this.onMouseOver = onMouseOver;
	}

	public String getOnMouseUp() {
		return onMouseUp;
	}

	public void setOnMouseUp(String onMouseUp) {
		this.onMouseUp = onMouseUp;
	}

	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
	    if(!TagUtil.isEmpty(onClick)){ sb.append("onclick=\"" + onClick + "\" ");}  
	    if(!TagUtil.isEmpty(onDblClick)){sb.append("ondblclick=\"" + onDblClick + "\" ");}  
	    if(!TagUtil.isEmpty(onMouseDown)){sb.append("onmousedown=\"" + onMouseDown + "\" ");}  
	    if(!TagUtil.isEmpty(onMouseMove)){sb.append("onmousemove=\"" + onMouseMove + "\" ");}  
	    if(!TagUtil.isEmpty(onMouseOver)){sb.append("onmouseover=\"" + onMouseOver + "\" ");}  
	    if(!TagUtil.isEmpty(onMouseOut)){sb.append("onmouseout=\"" + onMouseOut + "\" ");}  
	    if(!TagUtil.isEmpty(onMouseUp)){sb.append("onmouseup=\"" + onMouseUp + "\" ");}  
	    if(!TagUtil.isEmpty(onKeyDown)){ sb.append("onkeydown=\"" + onKeyDown + "\" ");}  
	    if(!TagUtil.isEmpty(onKeyPress)){sb.append("onkeypress=\"" + onKeyPress + "\" ");}  
	    if(!TagUtil.isEmpty(onKeyUp)){sb.append("onkeyup=\"" + onKeyUp + "\" ");}  
    	if(!TagUtil.isEmpty(id)){ sb.append("id=\"" + id + "\" ");}  
    	if(!TagUtil.isEmpty(cssClass)){sb.append("class=\"" + cssClass + "\" ");}  
    	if(!TagUtil.isEmpty(title)){sb.append("title=\"" + title + "\" "); }  
    	if(!TagUtil.isEmpty(style)){ sb.append("style=\"" + style + "\" "); }  
    	if(!TagUtil.isEmpty(xmlLang)) { sb.append("xml:lang=\"" + xmlLang + "\" "); }   
	    if(!TagUtil.isEmpty(dir)){ sb.append("dir=\"" + dir + "\" ");}  
	    if(!TagUtil.isEmpty(lang)){sb.append("lang=\"" + lang + "\" ");}  
    	if(!TagUtil.isEmpty(xmlLang)) { sb.append("xml:lang=\"" + xmlLang + "\" "); }   
		return sb;
	}
	

}
