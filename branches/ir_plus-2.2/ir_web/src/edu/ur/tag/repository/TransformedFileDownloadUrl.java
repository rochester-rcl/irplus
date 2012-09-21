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


package edu.ur.tag.repository;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.ur.tag.TagUtil;

import edu.ur.ir.file.IrFile;


/**
 * Get the transformed file.
 * 
 * @author Nathan Sarr
 *
 */
public class TransformedFileDownloadUrl extends SimpleTagSupport{
	
	/** The ir file to get the thumbnail from */
	private IrFile irFile;
	
	/** The thumbnail code to get the thumbnail */
	private String systemCode;
	
	/** Indicates that it should be a download url if possible*/
	private boolean download = false;
	
	/** var to set the path for*/
	private String var;
	
	public void doTag() throws JspException {
		 PageContext pageContext = (PageContext) this.getJspContext();

		if( irFile != null && systemCode != null)
		{   
			if(irFile.getTransformedFileBySystemCode(systemCode) != null)
			{
				try
				{
			        String path = TagUtil.getPageContextPath((PageContext)getJspContext());
			        path += "/transformedFileDownload.action?irFileId=" + irFile.getId();
			        path += "&systemCode=" + systemCode;
				
			        pageContext.setAttribute(var, path);
				}
				catch(Exception e)
				{
					throw new JspException("could not print path", e);
				}
			}
		}
		else
		{
			pageContext.setAttribute(var, null);
		}
	}
	
	   /**
     * Allows test cases to override where application context obtained from.
     *
     * @param pageContext so the <code>ServletContext</code> can be accessed as required by Spring's
     *        <code>WebApplicationContextUtils</code>
     *
     * @return the Spring application context (never <code>null</code>)
     */
    protected ApplicationContext getContext(PageContext pageContext) {
        ServletContext servletContext = pageContext.getServletContext();

        return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    }

	public IrFile getIrFile() {
		return irFile;
	}

	public void setIrFile(IrFile irFile) {
		this.irFile = irFile;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	

}
