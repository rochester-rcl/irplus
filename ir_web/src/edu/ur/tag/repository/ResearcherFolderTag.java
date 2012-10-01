/**  
   Copyright 2008-2010 University of Rochester

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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.simple.type.AscendingNameComparator;
import edu.ur.tag.TagUtil;

/**
 * Tag to output researcher information
 * 
 * @author Nathan Sarr
 *
 */
public class ResearcherFolderTag extends SimpleTagSupport{
	
	/**  researcher to draw the tree for*/
	private Researcher researcher;
	
	public void doTag() throws JspException
	{
		JspWriter out = this.getJspContext().getOut();
		if( researcher == null)	
	    {
			// do nothing
	    }
		else
		{
			try 
			{
			    writeResearcherFolderTree(out);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeResearcherFolderTree(JspWriter out) throws IOException
	{
		List <ResearcherFolder> folders = new LinkedList<ResearcherFolder> (researcher.getRootFolders());
		Collections.sort( folders , new AscendingNameComparator());
		
		List <ResearcherFile> files = new LinkedList<ResearcherFile> (researcher.getRootFiles());
		Collections.sort( files , new AscendingNameComparator());
		
		List <ResearcherPublication> publications = new LinkedList<ResearcherPublication> (researcher.getRootPublications());
		Collections.sort( publications , new AscendingNameComparator());
		
		List <ResearcherInstitutionalItem> items = new LinkedList<ResearcherInstitutionalItem> (researcher.getRootInstitutionalItems());
		Collections.sort( items , new AscendingNameComparator());
				
		List <ResearcherLink> links = new LinkedList<ResearcherLink> (researcher.getRootLinks());
		Collections.sort( links , new AscendingNameComparator());
				
		out.write("<ul>");
		for(ResearcherFolder folder : folders)
		{
		    writeFolder(folder, out);
		}
		
		writeFiles(files, out);
		writePublications(publications, out);
		writeItems(items, out);
		writeLinks(links, out);
		out.write("</ul>");
		
	}
	
	private void writeFolder(ResearcherFolder parent, JspWriter out) throws IOException
	{
		List <ResearcherFolder> folders = new LinkedList<ResearcherFolder> (parent.getChildren());
		Collections.sort( folders , new AscendingNameComparator());
		
		List <ResearcherFile> files = new LinkedList<ResearcherFile> (parent.getFiles());
		Collections.sort( files , new AscendingNameComparator());
		
		List <ResearcherPublication> publications = new LinkedList<ResearcherPublication> (parent.getPublications());
		Collections.sort( publications , new AscendingNameComparator());
		
		List <ResearcherInstitutionalItem> items = new LinkedList<ResearcherInstitutionalItem> (parent.getInstitutionalItems());
		Collections.sort( items , new AscendingNameComparator());
		
		
		List <ResearcherLink> links = new LinkedList<ResearcherLink> (parent.getLinks());
		Collections.sort( links , new AscendingNameComparator());
		
		out.write("<li>" + parent.getName());
		if( parent.getDescription() != null && !parent.getDescription().trim().equals(""))
		{
			out.write(" - " + parent.getDescription());
		}
		out.write("<ul>");
		for( ResearcherFolder folder : folders)
		{
			
			writeFolder(folder, out);		
			
			
		}
		writeFiles(files, out);
		writePublications(publications, out);
		writeItems(items, out);
		writeLinks(links, out);
		out.write("</ul>");
		out.write("</li>");	
		
			
	}
	
	private void writeFiles(List<ResearcherFile> files, JspWriter out) throws IOException
	{
		
		String basePath = TagUtil.getPageContextPath((PageContext)getJspContext()) + "/";
		for( ResearcherFile f : files)
		{
			
			out.write("<li><span>");
			String extension = f.getIrFile().getFileInfo().getExtension();
			if( extension != null )
			{
				if( extension.equals("doc"))
				{
				    out.write("<span class=\"wordFileImg\">&nbsp;</span>");
				}
				else if( extension.equals("pdf"))
				{
					out.write("<span class=\"pdfFileImg\">&nbsp;</span>");
				}
				else if( extension.equals("ppt") || extension.equals("pptx"))
				{
					out.write("<span class=\"powerPointFileImg\">&nbsp;</span>");
				}
				else if( extension.equals("xls") || extension.equals("xlsx"))
				{
					out.write("<span class=\"excelFileImg\">&nbsp;</span>");
				}
				else if( extension.equals("txt"))
				{
					out.write("<span class=\"textFileImg\">&nbsp;</span>");
				}
				else if( extension.equals("zip"))
				{
					out.write("<span class=\"compressedFileImg\">&nbsp;</span>");
				}
				else if( extension.equals("jpg"))
				{
					out.write("<span class=\"imgFileImg\">&nbsp;</span>");
				}
				else if(extension.equalsIgnoreCase("mp3") || 
			    		   extension.equalsIgnoreCase("wav") ||
			    		   extension.equalsIgnoreCase("mp4") ||
			    		   extension.equalsIgnoreCase("aac"))
			    {
					out.write("<span class=\"musicFileImg\"></span>");
			    }
				else
				{
					out.write("<span class=\"whiteFileImg\">&nbsp;</span>");
				}
			
			}
			out.write("<a href=\"" + basePath + "researcherFileDownload.action?researcherFileId=" + f.getId() +"\">");
			out.write(f.getNameWithExtension());
			out.write("</a>");		
			if( f.getDescription() != null && !f.getDescription().trim().equals(""))
			{
				out.write(" - " + f.getDescription());
			}
			
			out.write("</span></li>");
		}
	}
	
	private void writePublications(List<ResearcherPublication> publicaitons, JspWriter out) throws IOException
	{
		String basePath = TagUtil.getPageContextPath((PageContext)getJspContext()) + "/";
		for( ResearcherPublication p : publicaitons)
		{
			out.write("<li><span>");
			out.write("<span class=\"scriptImg\">&nbsp;</span>");
			out.write("<a href=\"" + basePath + "researcherPublicationView.action?researcherPublicationId=" + p.getId() +"\">");
			out.write(p.getName());
			out.write("</a>");		
			
			if( p.getDescription() != null && !p.getDescription().trim().equals(""))
			{
				out.write(" - " + p.getDescription());
			}
			out.write("</span></li>");
		}
	}
	
	private void writeItems(List<ResearcherInstitutionalItem> items, JspWriter out) throws IOException
	{
		String basePath = TagUtil.getPageContextPath((PageContext)getJspContext()) + "/";
		for( ResearcherInstitutionalItem i : items)
		{
			out.write("<li><span>");
			out.write("<span class=\"packageBtnImg\">&nbsp;</span>");
			out.write("<a href=\"" + basePath + "institutionalPublicationPublicView.action?institutionalItemId=" + i.getInstitutionalItem().getId() +"\">");
			out.write(i.getName());
			out.write("</a>");	
			if( i.getDescription() != null && !i.getDescription().trim().equals(""))
			{
				out.write(" - " + i.getDescription());
			}
			out.write("</span></li>");
		}
	}
	
	private void writeLinks(List<ResearcherLink> links, JspWriter out) throws IOException
	{
		String basePath = TagUtil.getPageContextPath((PageContext)getJspContext()) + "/";
		for( ResearcherLink l : links)
		{
			out.write("<li><span>");
			out.write("<img  alt=\"link\" src=\"" + basePath + "page-resources/images/all-images/link.gif\"/>");
			out.write("<a href=\"" + l.getUrl() +  "\">");
			out.write(l.getName());
			out.write("</a>");	
			if( l.getDescription() != null && !l.getDescription().trim().equals(""))
			{
				out.write(" - " + l.getDescription());
			}
			out.write("</span></li>");
		}
	}
	

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

}
