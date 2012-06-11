package edu.ur.tag.repository;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFile;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageInstitutionalItem;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLink;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPagePublication;
import edu.ur.simple.type.AscendingNameComparator;
import edu.ur.tag.TagUtil;

/**
 * Tag to output group workspace project page folder information.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceProjectPageFileSystemTag extends SimpleTagSupport{
	
	/**  Group workspace project page to draw the tree for*/
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;
	
	public void doTag() throws JspException
	{
		JspWriter out = this.getJspContext().getOut();
		if( groupWorkspaceProjectPage == null)	
	    {
			// do nothing
	    }
		else
		{
			try 
			{
			    writeFolderTree(out);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeFolderTree(JspWriter out) throws IOException
	{
		List <GroupWorkspaceProjectPageFolder> folders = new LinkedList<GroupWorkspaceProjectPageFolder> (groupWorkspaceProjectPage.getRootFolders());
		Collections.sort( folders , new AscendingNameComparator());
		
		List <GroupWorkspaceProjectPageFile> files = new LinkedList<GroupWorkspaceProjectPageFile> (groupWorkspaceProjectPage.getRootFiles());
		Collections.sort( files , new AscendingNameComparator());
		
		List <GroupWorkspaceProjectPagePublication> publications = new LinkedList<GroupWorkspaceProjectPagePublication> (groupWorkspaceProjectPage.getRootPublications());
		Collections.sort( publications , new AscendingNameComparator());
		
		List <GroupWorkspaceProjectPageInstitutionalItem> items = new LinkedList<GroupWorkspaceProjectPageInstitutionalItem> (groupWorkspaceProjectPage.getRootInstitutionalItems());
		Collections.sort( items , new AscendingNameComparator());
				
		List <GroupWorkspaceProjectPageFileSystemLink> links = new LinkedList<GroupWorkspaceProjectPageFileSystemLink> (groupWorkspaceProjectPage.getRootLinks());
		Collections.sort( links , new AscendingNameComparator());
				
		out.write("<ul>");
		for(GroupWorkspaceProjectPageFolder folder : folders)
		{
		    writeFolder(folder, out);
		}
		
		writeFiles(files, out);
		writePublications(publications, out);
		writeItems(items, out);
		writeLinks(links, out);
		out.write("</ul>");
		
	}
	
	private void writeFolder(GroupWorkspaceProjectPageFolder parent, JspWriter out) throws IOException
	{
		List <GroupWorkspaceProjectPageFolder> folders = new LinkedList<GroupWorkspaceProjectPageFolder> (parent.getChildren());
		Collections.sort( folders , new AscendingNameComparator());
		
		List <GroupWorkspaceProjectPageFile> files = new LinkedList<GroupWorkspaceProjectPageFile> (parent.getFiles());
		Collections.sort( files , new AscendingNameComparator());
		
		List <GroupWorkspaceProjectPagePublication> publications = new LinkedList<GroupWorkspaceProjectPagePublication> (parent.getPublications());
		Collections.sort( publications , new AscendingNameComparator());
		
		List <GroupWorkspaceProjectPageInstitutionalItem> items = new LinkedList<GroupWorkspaceProjectPageInstitutionalItem> (parent.getInstitutionalItems());
		Collections.sort( items , new AscendingNameComparator());
		
		
		List <GroupWorkspaceProjectPageFileSystemLink> links = new LinkedList<GroupWorkspaceProjectPageFileSystemLink> (parent.getLinks());
		Collections.sort( links , new AscendingNameComparator());
		
		out.write("<li>" + parent.getName());
		if( parent.getDescription() != null && !parent.getDescription().trim().equals(""))
		{
			out.write(" - " + parent.getDescription());
		}
		out.write("<ul>");
		for( GroupWorkspaceProjectPageFolder folder : folders)
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
	
	private void writeFiles(List<GroupWorkspaceProjectPageFile> files, JspWriter out) throws IOException
	{
		
		String basePath = TagUtil.getPageContextPath((PageContext)getJspContext()) + "/";
		for( GroupWorkspaceProjectPageFile f : files)
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
			out.write("<a href=\"" + basePath + "groupWorkspaceProjectPageFileDownload.action?groupWorkspaceProjectPageFileId=" + f.getId() +"\">");
			out.write(f.getNameWithExtension());
			out.write("</a>");		
			if( f.getDescription() != null && !f.getDescription().trim().equals(""))
			{
				out.write(" - " + f.getDescription());
			}
			
			out.write("</span></li>");
		}
	}
	
	private void writePublications(List<GroupWorkspaceProjectPagePublication> publicaitons, JspWriter out) throws IOException
	{
		String basePath = TagUtil.getPageContextPath((PageContext)getJspContext()) + "/";
		for( GroupWorkspaceProjectPagePublication p : publicaitons)
		{
			out.write("<li><span>");
			out.write("<span class=\"scriptImg\">&nbsp;</span>");
			out.write("<a href=\"" + basePath + "groupWorkspaceProjectPagePublicationView.action?groupWorkspaceProjectPagePublicationId=" + p.getId() +"\">");
			out.write(p.getName());
			out.write("</a>");		
			
			if( p.getDescription() != null && !p.getDescription().trim().equals(""))
			{
				out.write(" - " + p.getDescription());
			}
			out.write("</span></li>");
		}
	}
	
	private void writeItems(List<GroupWorkspaceProjectPageInstitutionalItem> items, JspWriter out) throws IOException
	{
		String basePath = TagUtil.getPageContextPath((PageContext)getJspContext()) + "/";
		for( GroupWorkspaceProjectPageInstitutionalItem i : items)
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
	
	private void writeLinks(List<GroupWorkspaceProjectPageFileSystemLink> links, JspWriter out) throws IOException
	{
		String basePath = TagUtil.getPageContextPath((PageContext)getJspContext()) + "/";
		for( GroupWorkspaceProjectPageFileSystemLink l : links)
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
	

	public void setGroupWorkspaceProjectPage(GroupWorkspaceProjectPage groupWorkspaceProjectPage) {
		this.groupWorkspaceProjectPage = groupWorkspaceProjectPage;
	}


}
