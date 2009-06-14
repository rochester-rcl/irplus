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

import javax.el.ELException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class SimpleTablePagerToolBarTag extends SimpleTagSupport {
	
	/**  adds java script to the onClick of the first page */
	private String firstPageClick;
	
	/**  adds java script to the onClick of the first page */
	private String nextPageClick;
	
	/**  adds java script to the onClick of the first page */
	private String lastPageClick;
	
	/**  adds java script to the onClick of the first page */
	private String previousPageClick;
	
	private String firstPageActiveImg = "page-resources/jmesa/firstPage.png";
	private String firstPageDisabledImg = "page-resources/jmesa/firstPageDisabled.png";
	
	private String lastPageActiveImg = "page-resources/jmesa/lastPage.png";
	private String lastPageDisabledImg = "page-resources/jmesa/lastPageDisabled.png";
	
	private String nextPageActiveImg = "page-resources/jmesa/nextPage.png";
	private String nextPageDisabledImg = "page-resources/jmesa/nextPageDisabled.png";
	
	private String previousPageActiveImg = "page-resources/jmesa/prevPage.png";
	private String previousPageDisabledImg = "page-resources/jmesa/prevPageDisabled.png";
	
	private TableRowTag rowTag;
	
	public void doTag() throws JspException {
	    PageContext context = (PageContext) getJspContext();
	    BasicFormTag formTag =
            (BasicFormTag)findAncestorWithClass(this,
              BasicFormTag.class);
	    
	    if(formTag == null)
	    {
	    	throw new JspTagException("the <ur:simpleTablePager> tag must"
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
	       
	    try {
	    	JspWriter out = context.getOut();
	    	out.print("<input type=\"hidden\" name=\"currentPage\" value=\"" + rowTag.getCurrentPage()+ "\"/>");
	    	printFirstPageImage(context, formTag.getName(), rowTag.getTotalNumberOfPages(), rowTag.getCurrentPage());
			printPreviousPageImage(context, formTag.getName(), rowTag.getTotalNumberOfPages(), rowTag.getCurrentPage());
			printNextPageImage(context, formTag.getName(), rowTag.getTotalNumberOfPages(), rowTag.getCurrentPage());
			printLastPageImage(context, formTag.getName(), rowTag.getTotalNumberOfPages(), rowTag.getCurrentPage());
	   } catch (Exception e) {
	       throw new JspException(e);
	   }
	       
	}

	public String getFirstPageActiveImg() {
		return firstPageActiveImg;
	}

	public void setFirstPageActiveImg(String firstPageActiveImg) {
		this.firstPageActiveImg = firstPageActiveImg;
	}

	public String getFirstPageDisabledImg() {
		return firstPageDisabledImg;
	}

	public void setFirstPageDisabledImg(String fistPageDisabledImg) {
		this.firstPageDisabledImg = fistPageDisabledImg;
	}

	public String getLastPageActiveImg() {
		return lastPageActiveImg;
	}

	public void setLastPageActiveImg(String lastPageActiveImg) {
		this.lastPageActiveImg = lastPageActiveImg;
	}

	public String getLastPageDisabledImg() {
		return lastPageDisabledImg;
	}

	public void setLastPageDisabledImg(String lastPageDisabledImg) {
		this.lastPageDisabledImg = lastPageDisabledImg;
	}

	public String getNextPageActiveImg() {
		return nextPageActiveImg;
	}

	public void setNextPageActiveImg(String nextPageActiveImg) {
		this.nextPageActiveImg = nextPageActiveImg;
	}

	public String getNextPageDisabledImg() {
		return nextPageDisabledImg;
	}

	public void setNextPageDisabledImg(String nextPageDisabledImg) {
		this.nextPageDisabledImg = nextPageDisabledImg;
	}

	public String getPreviousPageActiveImg() {
		return previousPageActiveImg;
	}

	public void setPreviousPageActiveImg(String previousPageActiveImg) {
		this.previousPageActiveImg = previousPageActiveImg;
	}

	public String getPreviousPageDisabledImg() {
		return previousPageDisabledImg;
	}

	public void setPreviousPageDisabledImg(String previousPageInactiveImg) {
		this.previousPageDisabledImg = previousPageInactiveImg;
	}

	private void printFirstPageImage(PageContext context, 
			String formName, 
			int totalNumberOfPages, 
			int currentPage) throws ELException, IOException
	{
		 JspWriter out = context.getOut();
		 
		 out.print("<td>");
	     if( totalNumberOfPages <= 1 || currentPage == 1)
	     {
	         out.print("<img src=\"");
	         out.print(TagUtil.fixRelativePath(firstPageDisabledImg, context));
	         out.print("\" style=\"border: 0pt none;\" alt=\"First Page\">");
	     }
	     else
	     {
	        out.print("<a href=\"");
	        if(firstPageClick != null && !firstPageClick.trim().equals(""))
	        {
	        	out.print(firstPageClick); 
	        }
	        		
	        out.print("javascript:document." + 
	        		formName + ".currentPage.value="
	        		+ 1 + ";javascript:" + 
	        		rowTag.getJavascriptObject() + ".submitForm('");
	        out.print(rowTag.getSubmitUrl());
	        out.print("')\">");
	        out.print("<img src=\"");
	        out.print(TagUtil.fixRelativePath(firstPageActiveImg, context));
	        out.print("\" style=\"border: 0pt none;\" title=\"First Page\" alt=\"First Page\"></a>");
	     }
	     out.print("</td>\n");
	}
	
	private void printPreviousPageImage(PageContext context, String formName, 
			int totalNumberOfPages,
			int currentPage) throws ELException, IOException
	{
		 JspWriter out = context.getOut();
		 out.print("<td>");
	        if( totalNumberOfPages <= 1 || currentPage == 1)
	        {
	        	out.print("<img src=\"");
	            out.print(TagUtil.fixRelativePath(previousPageDisabledImg, context));
	            out.print("\" style=\"border: 0pt none;\" alt=\"Previous Page\">");
	        }
	        else
	        {
		        out.print("<a href=\"");
		        if(previousPageClick != null && !previousPageClick.trim().equals(""))
		        {
		        	out.print(previousPageClick); 
		        }
		        
		        out.print("javascript:document." + 
		        		formName +".currentPage.value=" 
		        		+ (currentPage - 1) + ";javascript:" + 
		        		rowTag.getJavascriptObject() + ".submitForm('");
		        out.print(rowTag.getSubmitUrl());
		        out.print("')\">");
	        	out.print("<img src=\"");
	        	out.print(TagUtil.fixRelativePath(previousPageActiveImg, context));
	        	out.print("\" style=\"border: 0pt none;\" title=\"Previous Page\" alt=\"Previous Page\"></a>");
	        }
	        out.print("</td>\n");
	}
	
	private void printNextPageImage(PageContext context, String formName, 
			int totalNumberOfPages, 
			int currentPage) throws ELException, IOException
	{
		 JspWriter out = context.getOut();
		 out.print("<td>");
	        if( totalNumberOfPages <= 1 || currentPage == totalNumberOfPages)
	        {
	        	out.print("<img src=\"");
	            out.print(TagUtil.fixRelativePath(nextPageDisabledImg, context));
	            out.print("\" style=\"border: 0pt none;\" alt=\"Next Page\">");
	        }
	        else
	        {
		        out.print("<a href=\"");
		        
		        if(nextPageClick != null && !nextPageClick.trim().equals(""))
		        {
		        	out.print(nextPageClick); 
		        }
		        
		        out.print("javascript:document." + formName +".currentPage.value="
		        		+( currentPage + 1)
		        		+ ";javascript:" + rowTag.getJavascriptObject() + ".submitForm('");
		        out.print(rowTag.getSubmitUrl());
		        out.print("')\">");
		        out.print("<img src=\"");
	        	out.print(TagUtil.fixRelativePath(nextPageActiveImg, context));
	        	out.print("\" style=\"border: 0pt none;\" title=\"Next Page\" alt=\"Next Page\"></a>");
	        }
	        out.print("</td>\n");
	}
	
	private void printLastPageImage(PageContext context, String formName, 
			int totalNumberOfPages, 
			int currentPage) throws ELException, IOException
	{
		 JspWriter out = context.getOut();
		 out.print("<td>");
	        if( totalNumberOfPages <= 1 || currentPage == totalNumberOfPages)
	        {
	        	out.print("<img src=\"");
	            out.print(TagUtil.fixRelativePath(lastPageDisabledImg, context));
	            out.print("\" style=\"border: 0pt none;\" alt=\"Previous Page\">");
	        }
	        else
	        {
		        out.print("<a href=\"");
		        if(lastPageClick != null && !lastPageClick.trim().equals(""))
		        {
		        	out.print(lastPageClick); 
		        }
		        out.print("javascript:document." + formName +
		        		".currentPage.value=" + 
		        		totalNumberOfPages +
		        		";javascript:" + rowTag.getJavascriptObject() + ".submitForm('");
		        out.print(rowTag.getSubmitUrl());
		        out.print("')\">");
		        out.print("<img src=\"");
	        	out.print(TagUtil.fixRelativePath(lastPageActiveImg, context));
	        	out.print("\" style=\"border: 0pt none;\" title=\"Previous Page\" alt=\"Previous Page\"></a>");
	        }
	        out.print("</td>");
	}

	public String getFirstPageClick() {
		return firstPageClick;
	}

	public void setFirstPageClick(String firstPageClick) {
		this.firstPageClick = firstPageClick;
	}

	public String getNextPageClick() {
		return nextPageClick;
	}

	public void setNextPageClick(String nextPageClick) {
		this.nextPageClick = nextPageClick;
	}

	public String getLastPageClick() {
		return lastPageClick;
	}

	public void setLastPageClick(String lastPageClick) {
		this.lastPageClick = lastPageClick;
	}

	public String getPreviousPageClick() {
		return previousPageClick;
	}

	public void setPreviousPageClick(String previousPageClick) {
		this.previousPageClick = previousPageClick;
	}

}
