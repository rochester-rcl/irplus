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

public class HtmlKeyboardEventsImpl implements HtmlKeyboardEvents{
	
	private String onKeyDown;
	private String onKeyPress;
	private String onKeyUp;
	
	
	public String getOnKeyDown() {
		return onKeyDown;
	}
	public void setOnKeyDown(String onKeyDown) {
		this.onKeyDown = onKeyDown;
	}
	public String getOnKeyPress() {
		return onKeyPress;
	}
	public void setOnKeyPress(String onKeyPress) {
		this.onKeyPress = onKeyPress;
	}
	public String getOnKeyUp() {
		return onKeyUp;
	}
	public void setOnKeyUp(String onKeyUp) {
		this.onKeyUp = onKeyUp;
	}
	
	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
	    if(!TagUtil.isEmpty(onKeyDown)){ sb.append("onkeydown=\"" + onKeyDown + "\" ");}  
	    if(!TagUtil.isEmpty(onKeyPress)){sb.append("onkeypress=\"" + onKeyPress + "\" ");}  
	    if(!TagUtil.isEmpty(onKeyUp)){sb.append("onkeyup=\"" + onKeyUp + "\" ");}  
	    return sb;
	}

}
