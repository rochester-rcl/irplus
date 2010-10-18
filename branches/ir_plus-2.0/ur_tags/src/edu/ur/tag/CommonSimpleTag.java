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


package edu.ur.tag;

import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.ur.tag.html.attribute.HtmlCoreAttributes;
import edu.ur.tag.html.attribute.HtmlCoreAttributesImpl;
import edu.ur.tag.html.attribute.HtmlKeyboardAttributes;
import edu.ur.tag.html.attribute.HtmlKeyboardAttributesImpl;
import edu.ur.tag.html.attribute.HtmlLanguageAttributes;
import edu.ur.tag.html.attribute.HtmlLanguageAttributesImpl;
import edu.ur.tag.html.event.HtmlKeyboardEvents;
import edu.ur.tag.html.event.HtmlKeyboardEventsImpl;
import edu.ur.tag.html.event.HtmlMouseEvents;
import edu.ur.tag.html.event.HtmlMouseEventsImpl;

/**
 * Tag that implements most tag needs.
 * 
 * @author Nathan Sarr
 *
 */
public abstract class CommonSimpleTag extends SimpleTagSupport implements HtmlCoreAttributes, 
HtmlKeyboardAttributes,  HtmlKeyboardEvents, HtmlMouseEvents, HtmlLanguageAttributes
{
	protected HtmlCoreAttributes coreAttributes = new HtmlCoreAttributesImpl();
	protected HtmlKeyboardAttributes keyboardAttributes = new HtmlKeyboardAttributesImpl();
	protected HtmlKeyboardEvents keyboardEvents = new HtmlKeyboardEventsImpl();
	protected HtmlMouseEvents mouseEvents = new HtmlMouseEventsImpl();
	protected HtmlLanguageAttributes languageAttributes = new HtmlLanguageAttributesImpl();
	
	public StringBuffer getCoreAttributes() {
		return coreAttributes.getAttributes();
	}

	public String getCssClass() {
		return coreAttributes.getCssClass();
	}

	public String getId() {
		return coreAttributes.getId();
	}

	public String getStyle() {
		return coreAttributes.getStyle();
	}

	public String getTitle() {
		return coreAttributes.getTitle();
	}

	public String getXmlLang() {
		return languageAttributes.getXmlLang();
	}

	public void setCssClass(String cssClass) {
		coreAttributes.setCssClass(cssClass);
	}

	public void setId(String id) {
		coreAttributes.setId(id);
	}

	public void setStyle(String style) {
		coreAttributes.setStyle(style);
	}

	public void setTitle(String title) {
		coreAttributes.setTitle(title);
	}

	public void setXmlLang(String xmlLang) {
		languageAttributes.setXmlLang(xmlLang);
	}

	public String getAccessKey() {
		return keyboardAttributes.getAccessKey();
	}

	public StringBuffer getKeyboardAttributes() {
		return keyboardAttributes.getAttributes();
	}

	public String getTabIndex() {
		return keyboardAttributes.getTabIndex();
	}

	public void setAccessKey(String accessKey) {
		keyboardAttributes.setAccessKey(accessKey);
	}

	public void setTabIndex(String tabIndex) {
		keyboardAttributes.setTabIndex(tabIndex);
	}
	
	public StringBuffer getKeyboardEvents() {
		return keyboardEvents.getAttributes();
	}

	public String getOnKeyDown() {
		return keyboardEvents.getOnKeyDown();
	}

	public String getOnKeyPress() {
		return keyboardEvents.getOnKeyPress();
	}

	public String getOnKeyUp() {
		return keyboardEvents.getOnKeyUp();
	}

	public void setOnKeyDown(String onKeyDown) {
		keyboardEvents.setOnKeyDown(onKeyDown);
	}

	public void setOnKeyPress(String onKeyPress) {
		keyboardEvents.setOnKeyPress(onKeyPress);
	}

	public void setOnKeyUp(String onKeyUp) {
		keyboardEvents.setOnKeyUp(onKeyUp);
	}

	public StringBuffer getMouseEvents() {
		return mouseEvents.getAttributes();
	}

	public String getOnClick() {
		return mouseEvents.getOnClick();
	}

	public String getOnDblClick() {
		return mouseEvents.getOnDblClick();
	}

	public String getOnMouseDown() {
		return mouseEvents.getOnMouseDown();
	}

	public String getOnMouseMove() {
		return mouseEvents.getOnMouseMove();
	}

	public String getOnMouseOut() {
		return mouseEvents.getOnMouseOut();
	}

	public String getOnMouseOver() {
		return mouseEvents.getOnMouseOver();
	}

	public String getOnMouseUp() {
		return mouseEvents.getOnMouseUp();
	}

	public void setOnClick(String onClick) {
		mouseEvents.setOnClick(onClick);
	}

	public void setOnDblClick(String onDblClick) {
		mouseEvents.setOnDblClick(onDblClick);
	}

	public void setOnMouseDown(String onMouseDown) {
		mouseEvents.setOnMouseDown(onMouseDown);
	}

	public void setOnMouseMove(String onMouseMove) {
		mouseEvents.setOnMouseMove(onMouseMove);
	}

	public void setOnMouseOut(String onMouseOut) {
		mouseEvents.setOnMouseOut(onMouseOut);
	}

	public void setOnMouseOver(String onMouseOver) {
		mouseEvents.setOnMouseOver(onMouseOver);
	}

	public void setOnMouseUp(String onMouseUp) {
		mouseEvents.setOnMouseUp(onMouseUp);
	}

	public String getDir() {
		return languageAttributes.getDir();
	}

	public String getLang() {
		return languageAttributes.getLang();
	}

	public StringBuffer getLanguageAttributes() {
		return languageAttributes.getAttributes();
	}

	public void setDir(String dir) {
		languageAttributes.setDir(dir);
	}

	public void setLang(String lang) {
		languageAttributes.setLang(lang);
	}

	public void setCoreAttributes(HtmlCoreAttributes coreAttributes) {
		this.coreAttributes = coreAttributes;
	}

	public void setKeyboardAttributes(HtmlKeyboardAttributes keyboardAttributes) {
		this.keyboardAttributes = keyboardAttributes;
	}

	public void setKeyboardEvents(HtmlKeyboardEvents keyboardEvents) {
		this.keyboardEvents = keyboardEvents;
	}

	public void setLanguageAttributes(HtmlLanguageAttributes languageAttributes) {
		this.languageAttributes = languageAttributes;
	}

	public void setMouseEvents(HtmlMouseEvents mouseEvents) {
		this.mouseEvents = mouseEvents;
	}
	
	public StringBuffer getAllSimpleTagAttributes()
	{
		StringBuffer sb = new StringBuffer();
    	sb.append(getKeyboardEvents());
    	sb.append(getKeyboardAttributes());
    	sb.append(getCoreAttributes());
    	sb.append(getLanguageAttributes());
    	sb.append(getMouseEvents());
		return sb;
	}
}
