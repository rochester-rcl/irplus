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

import javax.el.ELException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;


import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.VersionedFile;
import edu.ur.tag.CommonSimpleTag;
import edu.ur.tag.TagUtil;
import edu.ur.tag.html.HtmlImage;

/**
 * Outputs a graphical icon for the specified file type.
 * 
 * @author Nathan Sarr
 *
 */
public class FileTypeImgTag extends CommonSimpleTag implements HtmlImage{
	
	private String src;
	private String alt;
	private String align;
	private String border;
	private String height;
	private String hSpace;
	private String ismap;
	private String longDesc;
	private String useMap;
	private String vSpace;
	private String width;
	
	/** The personal file to get the thumbnail from */
	private VersionedFile versionedFile;
	
	/** The ir file to get the thumbnail from */
	private IrFile irFile;
	

	public void doTag() throws JspException {
	    PageContext context = (PageContext) getJspContext();
	    
	    try {
	    	String extension = null;
	    	src = "page-resources/images/all-images/";
	    	alt = "";

	    	String output = "<span class=\"whiteFileImg\"></span>";
	    	
	    	if (versionedFile != null) {
	    		extension = versionedFile.getExtension();
	    	}
	    	
	    	if (irFile != null) {
	    		extension = irFile.getFileInfo().getExtension();
	    	}
	    	
	    	if( extension != null )
	    	{
	    		if(extension.equalsIgnoreCase("pdf"))
	    		{
	    			output = "<span class=\"pdfFileImg\"></span>";
	    		}
	    		if( extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg") 
	    			|| extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("tiff") 
	    			|| extension.equalsIgnoreCase("tif") || extension.equalsIgnoreCase("bmp"))
	    		{
	    			output = "<span class=\"imgFileImg\"></span>";
	    		}
	    		if(extension.equalsIgnoreCase("doc") || extension.equalsIgnoreCase("docx"))
		    	{
	    			output = "<span class=\"wordFileImg\"></span>";
		    	}
	    		if(extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx"))
		    	{
	    			output = "<span class=\"excelFileImg\"></span>";
		    	}
	    		if(extension.equalsIgnoreCase("txt"))
		    	{
	    			output = "<span class=\"textFileImg\"></span>";
		    	}
	    		if(extension.equalsIgnoreCase("zip"))
		    	{
	    			output = "<span class=\"compressedFileImg\"></span>";
		    	}
	    		if(extension.equalsIgnoreCase("ppt") || extension.equalsIgnoreCase("pptx"))
		    	{
	    			output = "<span class=\"powerPointFileImg\"></span>";
		    	}
	    		if(extension.equalsIgnoreCase("mp3") || 
	    		   extension.equalsIgnoreCase("wav") ||
	    		   extension.equalsIgnoreCase("aac"))
	    		{
	    			output = "<span class=\"musicFileImg\"></span>";
	    		}
	    		if(extension.equalsIgnoreCase("mp4") ||  extension.equalsIgnoreCase("mov"))
	 	    	{
	 	    		output = "<span class=\"movieFileImg\"></span>";
	 	    	}
	    	}
	    	
	        JspWriter o = context.getOut();
	        o.print(output);
	   } catch (Exception e) {
	       throw new JspException(e);
	   }

	}
	
	public VersionedFile getVersionedFile() {
		return versionedFile;
	}

	public void setVersionedFile(VersionedFile versionedFile) {
		this.versionedFile = versionedFile;
	}

	
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getBorder() {
		return border;
	}
	public void setBorder(String boarder) {
		this.border = boarder;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getHSpace() {
		return hSpace;
	}
	public void setHSpace(String space) {
		hSpace = space;
	}
	public String getIsmap() {
		return ismap;
	}
	public void setIsmap(String ismap) {
		this.ismap = ismap;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getUseMap() {
		return useMap;
	}
	public void setUseMap(String useMap) {
		this.useMap = useMap;
	}
	public String getVSpace() {
		return vSpace;
	}
	public void setVSpace(String space) {
		vSpace = space;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public StringBuffer getAttributes() {
		PageContext context = (PageContext) getJspContext();
		StringBuffer sb = new StringBuffer();
		
    	if(!TagUtil.isEmpty(src)) 
    	{ 
			    
			try {
				sb.append("src=\"" + TagUtil.fixRelativePath(src,context) + "\" ");
			} catch (ELException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
    	
    	if(!TagUtil.isEmpty(border)) { sb.append("border=\"" + border + "\" "); }  
    	if(!TagUtil.isEmpty(alt)) { sb.append("alt=\"" + alt + "\" "); }  
    	if(!TagUtil.isEmpty(height)) { sb.append("height=\"" + height + "\" "); }  
    	if(!TagUtil.isEmpty(hSpace)) { sb.append("hspace=\"" + hSpace + "\" "); }  
    	if(!TagUtil.isEmpty(ismap)) { sb.append("ismap=\"" + ismap + "\" "); }  
    	if(!TagUtil.isEmpty(useMap)) { sb.append("usemap=\"" + useMap + "\" "); }  
    	if(!TagUtil.isEmpty(vSpace)) { sb.append("vspace=\"" + vSpace + "\" "); }  

    	if(!TagUtil.isEmpty(longDesc)) { sb.append("longdesc=\"" + longDesc + "\" "); } 
    	if(!TagUtil.isEmpty(align)) { sb.append("align=\"" + align + "\" "); }
    	if(!TagUtil.isEmpty(width)) { sb.append("width=\"" + width + "\" "); } 
    	
    	sb.append(getAllSimpleTagAttributes());
		return sb;

	}

	public IrFile getIrFile() {
		return irFile;
	}

	public void setIrFile(IrFile irFile) {
		this.irFile = irFile;
	}



	

}
