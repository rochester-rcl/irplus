/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.tag.repository;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.ur.ir.web.util.WebBrowserFileViewerHelper;

/**
 * Determines the target for the file download.  If the file
 * can be shown in the browser a new target is opened otherwise
 * nothing is done
 * 
 * @author Nathan Sarr
 *
 */
public class FileDownloadTag extends SimpleTagSupport{

	private String extension;
	


	public void doTag() throws JspException
	{
		PageContext pageContext = (PageContext)this.getJspContext();
		ServletContext servletContext = pageContext.getServletContext();
		ApplicationContext context =  WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		
		
        String[] beans = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context, 
       		 WebBrowserFileViewerHelper.class, false, false);

        if (beans.length == 0) {
            throw new JspException("No web browser file viewer was found in the application context: " 
           		 + context.toString());
        }

        WebBrowserFileViewerHelper viewHelper = (WebBrowserFileViewerHelper) context.getBean(beans[0]);
        
        
        if( extension != null && viewHelper.canShowFileTypeInBrowser(extension) )
        {
		    try {
		    	JspWriter out = this.getJspContext().getOut();
			    String output = "target=\"_blank\"";
				out.write(output);
			} catch (IOException e) {
				throw new JspException(e);
			}
        }

	}
	
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
