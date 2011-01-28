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

import edu.ur.tag.TagUtil;

public class HtmlFormElementEventsImpl implements HtmlFormElementEvents{
	
	private String onChange;
	private String onSubmit;
	private String onReset;
	private String onSelect;
	private String onBlur;
	private String onFocus;

	public String getOnBlur() {
		return onBlur;
	}

	public void setOnBlur(String onBlur) {
		this.onBlur = onBlur;
	}

	public String getOnChange() {
		return onChange;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	public String getOnFocus() {
		return onFocus;
	}

	public void setOnFocus(String onFocus) {
		this.onFocus = onFocus;
	}

	public String getOnReset() {
		return onReset;
	}

	public void setOnReset(String onReset) {
		this.onReset = onReset;
	}

	public String getOnSelect() {
		return onSelect;
	}

	public void setOnSelect(String onSelect) {
		this.onSelect = onSelect;
	}

	public String getOnSubmit() {
		return onSubmit;
	}

	public void setOnSubmit(String onSubmit) {
		this.onSubmit = onSubmit;
	}

	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
	    if(!TagUtil.isEmpty(onChange)){ sb.append("onchange=\"" + onChange + "\" ");}  
	    if(!TagUtil.isEmpty(onSubmit)){sb.append("onsubmit=\"" + onSubmit + "\" ");}  
	    if(!TagUtil.isEmpty(onReset)){ sb.append("onreset=\"" + onReset + "\" ");}  
	    if(!TagUtil.isEmpty(onSelect)){sb.append("onselect=\"" + onSelect + "\" ");}  
	    if(!TagUtil.isEmpty(onBlur)){sb.append("onsubmit=\"" + onBlur + "\" ");}  
	    if(!TagUtil.isEmpty(onBlur)){sb.append("onFocus=\"" + onFocus + "\" ");}  
	    return sb;
	}

}
