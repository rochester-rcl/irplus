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

public class HtmlMouseEventsImpl implements HtmlMouseEvents{
	
	private String onClick;
	private String onDblClick;
	private String onMouseDown;
	private String onMouseMove;
	private String onMouseOver;
	private String onMouseOut;
	private String onMouseUp;

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
	    return sb;
	}

}
