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


package edu.ur.tag.html;

import edu.ur.tag.html.attribute.HtmlCoreAttributes;
import edu.ur.tag.html.attribute.HtmlLanguageAttributes;
import edu.ur.tag.html.event.HtmlKeyboardEvents;
import edu.ur.tag.html.event.HtmlMouseEvents;

public interface HtmlForm extends HtmlCoreAttributes,
HtmlLanguageAttributes, HtmlMouseEvents, HtmlKeyboardEvents{
	
	public String getOnSubmit();
	
	public void setOnSubmit(String onSubmit);
	
	public String getOnReset();
	
	public void setOnReset(String onReset);

	public String getMethod();
	
	public void setMethod(String method);
	
	public String getAction();
	
	public void setAction(String action);
	
	public String getAccept();
	
	public void setAccept(String accept);
	
	public String getAcceptCharset();
	
	public void setAcceptCharset(String acceptCharset);
	
	public String getEnctype();
	
	public void setEnctype(String enctype);

    public String getName();
    
    public void setName(String name);
    
    public String getTarget();
    
    public void setTarget(String target);

	public StringBuffer getAttributes();

}
