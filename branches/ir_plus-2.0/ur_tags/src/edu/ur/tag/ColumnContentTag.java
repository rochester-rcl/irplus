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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ColumnContentTag extends SimpleTagSupport{
	
	public void doTag() throws JspException {
		
			TableRowTag rowTag = (TableRowTag) findAncestorWithClass(this,
					TableRowTag.class);
			
			ColumnTag columnTag = (ColumnTag)findAncestorWithClass(this,
					ColumnTag.class);
			
			if (columnTag == null) {
				throw new JspTagException("the <ur:columnContent> tag must"
						+ " be nested within a <ur:column> tag");
			}
			
			if (rowTag.isProcessColumn()) 
	        {
				JspFragment body = getJspBody();
				try {
					body.invoke(null);
				} catch (IOException e) {
					e.printStackTrace();
					throw new JspException("Error in columnContent", e);
				}
	        }
	}

}
