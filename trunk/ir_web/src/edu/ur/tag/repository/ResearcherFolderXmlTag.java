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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * 
 * Write out the researcher folder tree in xml.
 * 
 * @author Nathan Sarr
 *
 */
public class ResearcherFolderXmlTag extends SimpleTagSupport{
	
	/**  researcher to draw the tree for*/
	private Researcher researcher;
	
	public void doTag() throws JspException
	{
		
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		 
		try 
		{
		   builder = factory.newDocumentBuilder();
		} 
		catch (ParserConfigurationException e) 
		{
		    throw new IllegalStateException(e);
		}
		 
		DOMImplementation impl = builder.getDOMImplementation();
		DOMImplementationLS domLs = (DOMImplementationLS)impl.getFeature("LS" , "3.0");
		LSSerializer serializer = domLs.createLSSerializer();
		LSOutput lsOut= domLs.createLSOutput();
		
		Document doc = impl.createDocument(null, "researcher_tree", null);
		Element root = doc.getDocumentElement();
		 
		JspWriter out = this.getJspContext().getOut();
		lsOut.setCharacterStream(out);
		
		if( researcher == null)	
	    {
			// do nothing
	    }
		else
		{
			try 
			{
			    createResearcherFolderTree(doc, root);
			    
			    // this is a fragment so do not output the xml declaration
			    DOMConfiguration domConfig = serializer.getDomConfig();
			    domConfig.setParameter("xml-declaration", false);
			    serializer.write(root, lsOut);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createResearcherFolderTree(Document doc, Element root) throws IOException
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
				

		for(ResearcherFolder folder : folders)
		{
			buildTree(doc, root, folder);
		}
		
		writeFiles(files, doc, root);
		writePublications(publications, doc, root);
		writeItems(items, doc, root);
		writeLinks(links, doc, root);
		
		
	}
	
	private void buildTree(Document doc, Element root, ResearcherFolder parent) throws IOException
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
		
		Element folder = doc.createElement("folder");
		root.appendChild(folder);
		
		// file name
		Element name = doc.createElement("name");
		Text data = doc.createTextNode(parent.getName());
		name.appendChild(data);
		folder.appendChild(name);
		
		// file description
		Element description = doc.createElement("description");
		data = doc.createTextNode(parent.getDescription());
		description.appendChild(data);
		folder.appendChild(description);

	
		
		for( ResearcherFolder child : folders)
		{
			buildTree(doc, folder, child);		
		}
		
		writeFiles(files, doc, folder);
		writePublications(publications, doc, folder);
		writeItems(items, doc, folder);
		writeLinks(links, doc, folder);
			
	}
	
	private void writeFiles(List<ResearcherFile> files, Document doc, Element root) 
	{
		String baseUrl = BaseUrlHelper.getBaseUrl((PageContext)this.getJspContext());
		for( ResearcherFile f : files)
		{
			Element file = doc.createElement("file");
			root.appendChild(file);
			
			// file name
			Element name = doc.createElement("name");
			Text data = doc.createTextNode(f.getName());
			name.appendChild(data);
			file.appendChild(name);
			
			// file description
			Element description = doc.createElement("description");
			data = doc.createTextNode(f.getDescription());
			description.appendChild(data);
			file.appendChild(description);

			// url to file
			Element url = doc.createElement("url");
			data = doc.createTextNode(baseUrl + "researcherFileDownload.action?researcherFileId=" + f.getId());
			url.appendChild(data);
			file.appendChild(url);
		}
	}
	
	private void writePublications(List<ResearcherPublication> publicaitons, Document doc, Element root) throws IOException
	{
		String baseUrl = BaseUrlHelper.getBaseUrl((PageContext)this.getJspContext());
		for( ResearcherPublication p : publicaitons)
		{
			Element personalPublication = doc.createElement("personal_publication");
			root.appendChild(personalPublication);
			
			// personal publication name
			Element name = doc.createElement("name");
			Text data = doc.createTextNode(p.getName());
			name.appendChild(data);
            personalPublication.appendChild(name);
            
			// file description
			Element description = doc.createElement("description");
			data = doc.createTextNode(p.getDescription());
			description.appendChild(data);
			personalPublication.appendChild(description);
            
            // url to personal publication
			Element url = doc.createElement("url");
			data = doc.createTextNode( baseUrl + "researcherPublicationView.action?researcherPublicationId=" + p.getId());
			url.appendChild(data);
			personalPublication.appendChild(url);
		}
	}
	
	private void writeItems(List<ResearcherInstitutionalItem> items, Document doc, Element root)
	{
		String baseUrl = BaseUrlHelper.getBaseUrl((PageContext)this.getJspContext());
		for( ResearcherInstitutionalItem i : items)
		{
			Element institutionalPublication = doc.createElement("institutional_publication");
			root.appendChild(institutionalPublication);
			
			// personal publication name
			Element name = doc.createElement("name");
			Text data = doc.createTextNode(i.getName());
			name.appendChild(data);
			institutionalPublication.appendChild(name);
            
			// file description
			Element description = doc.createElement("description");
			data = doc.createTextNode(i.getDescription());
			description.appendChild(data);
			institutionalPublication.appendChild(description);
            
            // url to personal publication
			Element url = doc.createElement("url");
			data = doc.createTextNode( baseUrl + 
					"institutionalPublicationPublicView.action?institutionalItemId=" + 
					i.getInstitutionalItem().getId());
			url.appendChild(data);
			institutionalPublication.appendChild(url);
		
		}
	}
	
	private void writeLinks(List<ResearcherLink> links, Document doc, Element root)throws IOException
	{
		for( ResearcherLink l : links)
		{
			Element link = doc.createElement("link");
			root.appendChild(link);
			
			// personal publication name
			Element name = doc.createElement("name");
			Text data = doc.createTextNode(l.getName());
			name.appendChild(data);
			link.appendChild(name);
            
			// file description
			Element description = doc.createElement("description");
			data = doc.createTextNode(l.getDescription());
			description.appendChild(data);
			link.appendChild(description);
            
            // url to personal publication
			Element url = doc.createElement("url");
			data = doc.createTextNode(l.getUrl());
			url.appendChild(data);
			link.appendChild(url);
	
		}
	}
	

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}


}
