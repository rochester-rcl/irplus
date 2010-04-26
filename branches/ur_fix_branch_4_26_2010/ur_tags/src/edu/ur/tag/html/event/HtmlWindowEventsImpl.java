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

/**
 * Deal with window events.
 * 
 * @author Nathan Sarrr
 *
 */
public class HtmlWindowEventsImpl implements HtmlWindowEvents {
	
	private String onLoad;
	
	private String onUnLoad;

	public String getOnLoad() {
		return onLoad;
	}

	public void setOnLoad(String onLoad) {
		this.onLoad = onLoad;
	}

	public String getOnUnLoad() {
		return onUnLoad;
	}

	public void setOnUnLoad(String onUnLoad) {
		this.onUnLoad = onUnLoad;
	}

	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
	    if(!TagUtil.isEmpty(onLoad)){ sb.append("onload=\"" + onLoad + "\" ");}  
	    if(!TagUtil.isEmpty(onUnLoad)){sb.append("onUnLoad=\"" + onUnLoad + "\" ");}  
	    return sb;
	}

}
