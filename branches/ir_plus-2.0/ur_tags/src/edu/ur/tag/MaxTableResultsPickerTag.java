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

import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class MaxTableResultsPickerTag extends SimpleTagSupport{
	
	/**  Should be a string of results  */
	@SuppressWarnings("unchecked")
	private Collection choices;
	
	/**  Current value that should be set */
	private String maxResultsPerPage;
	
	/** Row tag which encapsulates the tag  */
	private TableRowTag rowTag;
	
	/** Javascript action to execute on select action  */
	private String onChange;

	
	public void doTag() throws JspException {
		
	    PageContext context = (PageContext) getJspContext();
	    BasicFormTag formTag =
            (BasicFormTag)findAncestorWithClass(this,
              BasicFormTag.class);
	    
	    if(formTag == null)
	    {
	    	throw new JspTagException("the <ur:maxResultsPicker> tag must"
	    			+ " be nested within a <ur:form> tag");
	    }
	    
	    rowTag =
            (TableRowTag)findAncestorWithClass(this,
              TableRowTag.class);
	    
	    if(rowTag == null)
	    {
	    	throw new JspTagException("the <ur:simpleTablePager> tag must"
	    			+ " be nested within a <ur:row> tag");
	    }

	    
	    try
	    {
	    	JspWriter out = context.getOut();
	    	out.print("<td>");
	    	out.print("<select name=\"maxResultsPerPage\"); onchange=\"");
	    	if(onChange != null && !onChange.trim().equals(""))
	        {
	        	out.print(onChange); 
	        }
	    	
	    	out.print("javascript:" + 
	    			rowTag.getJavascriptObject()+ ".submitForm('" +
	    			rowTag.getSubmitUrl()+ "')\">");
	    	
	    	if( choices != null )
	    	{
	    	    for(Object o: choices)
	    	    {
	    	        out.print("<option ");
	    		    if( maxResultsPerPage != null && maxResultsPerPage.equals(o))
	    		    {
	    			    out.print("selected=\"selected\" ");
	    		    }
	    		    out.print("value=\"");
	    		    out.print(o);
	    		    out.print("\">");
	    		    out.print(o);
	    		    out.print("</option>");
	    	    }
	    	    out.print("</select>");
	    	}
	    	out.print("</td>");
	    	
			
	    } catch (Exception e) {
	       throw new JspException(e);
	    }
	}

	@SuppressWarnings("unchecked")
	public Collection getChoices() {
		return choices;
	}

	@SuppressWarnings("unchecked")
	public void setChoices(Collection choices) {
		this.choices = choices;
	}

	public String getMaxResultsPerPage() {
		return maxResultsPerPage;
	}

	public void setMaxResultsPerPage(String maxResultsPerPage) {
		this.maxResultsPerPage = maxResultsPerPage;
	}

	public String getOnChange() {
		return onChange;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}
}
