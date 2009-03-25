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

package edu.ur.dspace.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import edu.ur.dspace.model.BitstreamFileInfo;
import edu.ur.dspace.model.DspaceResearcherFolder;
import edu.ur.dspace.model.Link;
import edu.ur.dspace.model.DspaceResearcher;
import edu.ur.dspace.model.ResearcherFolderLink;
import edu.ur.dspace.util.FileZipperUtil;
import edu.ur.dspace.util.ZipFileNameHelper;

/**
 * Exports researcher data.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultResearcherExporter implements ResearcherExporter {
	
	/** gets the file location of the file in dspace */
    private BitstreamFileLoader bitstreamFileLoader;


	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(DefaultResearcherExporter.class);
	
	/**
	 * Data source for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	/**
	 * Get all the community records.
	 * 
	 * @param xmlFileName
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public List<DspaceResearcher> getResearchers() throws IOException
	{
		 List<DspaceResearcher> researchers = jdbcTemplate.query( "select * from researcher", new ResearcherMapper());
		 
		 for(DspaceResearcher r : researchers)
		 {
		     if( r.logoId != null && r.logoId > 0)
		     {
			     System.out.println(r.logoId);
			     BitstreamFileInfo info = bitstreamFileLoader.getBitstreamFileInfo(r.logoId);
		         File bitstreamFile = new File(info.dspaceFileName);
		         info.newFileName = "researcher_" + r.researcherId + "." + info.extension;
		         System.out.println(bitstreamFile.getCanonicalPath());
		         System.out.println("file name = " + info.newFileName);
		         r.logoFileInfo = info;
		     }
		     
		     // get the researcher folders
		     List<DspaceResearcherFolder> folders = jdbcTemplate.query( "select * from researcher_folder where researcher_id = " + r.researcherId, new ResearcherFolderMapper());
		     r.folder = buildTree(null, folders);
		 }
		 
		 return researchers;
	}
	
	/**
	 * @param folders
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DspaceResearcherFolder buildTree(DspaceResearcherFolder parent, List<DspaceResearcherFolder> folders)
	{
		log.debug(" building tree for parent " + parent);
		DspaceResearcherFolder currentParent = null;
		if(parent == null)
		{
			log.debug("parent is null");
			// find the top level parent
			for(DspaceResearcherFolder f : folders)
			{
				log.debug("f.parnent id = " + f.parentId);
				log.debug( "testing f.parentId.equals(0l) " + (f.parentId.equals(0l)));
				
				if(f.parentId.equals(0l))
				{
					currentParent = f;
				    List<ResearcherFolderLink> links = jdbcTemplate.query( "select * from folder_link where folder_id = " + f.folderId, new ResearcherFolderLinkMapper());
				    f.links = links;
				}
			}
			log.debug(" Found root " + currentParent);
			buildTree(currentParent, folders);
		}
		else
		{
			
			currentParent = parent;
			log.debug("addding a child to current parent parent " + currentParent);
			for(DspaceResearcherFolder f : folders)
			{
				log.debug( " f.parentId = " + f.parentId + " currentParent.folderId = " + currentParent.folderId);
				log.debug(" testing f.parentId.equals(currentParent.folderId) " + f.parentId.equals(currentParent.folderId));
				if( f.parentId.equals(currentParent.folderId))
				{
					log.debug("adding child " + f);
					List<ResearcherFolderLink> links = jdbcTemplate.query( "select * from folder_link where folder_id = " + f.folderId, new ResearcherFolderLinkMapper());
				    f.links = links;
					currentParent.children.add(f);
					buildTree(f, folders);
				}
			}
		}
		
		return currentParent;
	}
	
	/**
	 * Get the logos for the researchers
	 * 
	 * @param communities
	 * @return
	 */
	public List<BitstreamFileInfo> getResearcherLogos(Collection<DspaceResearcher> researchers)
	{
		LinkedList<BitstreamFileInfo> logos = new LinkedList<BitstreamFileInfo>();
		// create XML for the communities
		 for(DspaceResearcher r : researchers)
		 {
			 if( r.logoFileInfo != null)
			 {
				 logos.add(r.logoFileInfo);
			 }
		 }
		return logos;
	}
	
	/**
	 * Create the xml files for communities
	 * @param f
	 * @param communities
	 * @throws IOException 
	 */
	public void generateResearcherXMLFile(File f, Collection<DspaceResearcher> researchers) throws IOException
	{
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder;
		 
		 try {
			builder = factory.newDocumentBuilder();
		 } catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		 }
		 
		 DOMImplementation impl = builder.getDOMImplementation();
		 DOMImplementationLS domLs = (DOMImplementationLS)impl.getFeature("LS" , "3.0");
		 LSSerializer serializer = domLs.createLSSerializer();
		 LSOutput lsOut= domLs.createLSOutput();

		 Document doc = impl.createDocument(null, "researchers", null);
		 Element root = doc.getDocumentElement();
		 
		 FileOutputStream fos;
		 OutputStreamWriter outputStreamWriter;
		 BufferedWriter writer;
		 
		 try {
			fos = new FileOutputStream(f);
			
			try {
				outputStreamWriter = new OutputStreamWriter(fos, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException(e);
			}
			writer = new BufferedWriter(outputStreamWriter);
			lsOut.setCharacterStream(writer);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
		 
		// create XML for the communities
		 for(DspaceResearcher r : researchers)
		 {
			 Element researcher = doc.createElement("researcher");
			 
			 Element userId = doc.createElement("dspace_user_id");
			 Text data = doc.createTextNode(r.dspaceUserId + "");
			 userId.appendChild(data);
			 researcher.appendChild(userId);
			 
			 Element researcherId = doc.createElement("researcher_id");
			 data = doc.createTextNode(r.researcherId + "");
			 researcherId.appendChild(data);
			 researcher.appendChild(researcherId);
			 
			 Element researcherInterests = doc.createElement("researcher_interests");
			 data = doc.createTextNode(r.researchInterests);
			 researcherInterests.appendChild(data);
			 researcher.appendChild(researcherInterests);
			 
			 Element campusLocation = doc.createElement("campus_location");
			 data = doc.createTextNode(r.campusLocation);
			 campusLocation.appendChild(data);
			 researcher.appendChild(campusLocation);
			 
			 Element teachingInterests = doc.createElement("teaching_interests");
			 data = doc.createTextNode(r.teachingInterests);
			 teachingInterests.appendChild(data);
			 researcher.appendChild(teachingInterests);
			
			 Element title = doc.createElement("title");
			 data = doc.createTextNode(r.title);
			 title.appendChild(data);
			 researcher.appendChild(title);
			 
			 Element department = doc.createElement("department");
			 data = doc.createTextNode(r.department);
			 department.appendChild(data);
			 researcher.appendChild(department);
			 
			 Element field = doc.createElement("field");
			 data = doc.createTextNode(r.field);
			 field.appendChild(data);
			 researcher.appendChild(field);
			 
			 Element fax = doc.createElement("fax");
			 data = doc.createTextNode(r.fax);
			 fax.appendChild(data);
			 researcher.appendChild(fax);
			 
			 Element pagePublic = doc.createElement("page_public");
			 data = doc.createTextNode(r.pagePublic + "");
			 pagePublic.appendChild(data);
			 researcher.appendChild(pagePublic);
			 
			 Element logoFileName = null;
			 if( r.logoFileInfo != null)
			 {
			     logoFileName  = doc.createElement("logo_file_name");
				 data = doc.createTextNode(r.logoFileInfo.newFileName);
				 logoFileName.appendChild(data);
				 researcher.appendChild(logoFileName);
			 }
			 
			 root.appendChild(researcher);
			 
			 if( r.links != null && r.links.size() > 0 )
			 {
			     Element links = doc.createElement("links");
			 
			     // add the links
			     for(Link l : r.links)
			     {
				     Element link = doc.createElement("link");
				     Element linkName = doc.createElement("name");
				     data = doc.createTextNode(l.name);
				     linkName.appendChild(data);
				 
				     Element linkUrl = doc.createElement("url");
				     data = doc.createTextNode(l.value);
				     linkUrl.appendChild(data);
				 
				     link.appendChild(linkName);
				     link.appendChild(linkUrl);
				     links.appendChild(link);
			     }
			     researcher.appendChild(links);
			 }
			 
			 Element folders = doc.createElement("folders");
			 researcher.appendChild(folders);
			 buildFolderTree(doc, folders, r.folder);
			 
			 
			 
		 }
		 serializer.write(root, lsOut);
		 
		 try
		 {
		      fos.close();
		      writer.close();
		      outputStreamWriter.close();
		 }
		 catch(Exception e)
		 {
			 throw new IllegalStateException(e);
		 }
	}
	
	/**
	 * Build the folder tree.
	 * 
	 * @param doc
	 * @param root
	 * @param currentParent
	 * @return
	 */
	private Element buildFolderTree(Document doc, Element root, DspaceResearcherFolder currentParent )
	{
		log.debug("current parent = " + currentParent);
		Element folder = doc.createElement("folder");
		root.appendChild(folder);
		
		
		Element title = doc.createElement("title");
		Text data = doc.createTextNode(currentParent.title);
		title.appendChild(data);
		folder.appendChild(title);
		
		Element description = doc.createElement("description");
		data = doc.createTextNode(currentParent.description);
		description.appendChild(data);
		folder.appendChild(description);
		
		Element folderId = doc.createElement("folder_id");
		data = doc.createTextNode(currentParent.folderId + "");
		folderId.appendChild(data);
		folder.appendChild(folderId);
		
		Element researcherId = doc.createElement("researcher_id");
		data = doc.createTextNode(currentParent.researcherId + "");
		researcherId.appendChild(data);
		folder.appendChild(researcherId);
		
		Element parentId = doc.createElement("parent_folder_id");
		data = doc.createTextNode(currentParent.parentId + "");
		parentId.appendChild(data);
		folder.appendChild(parentId);
		
		
		addFolderLinks(doc, folder, currentParent);
		
		log.debug("children size = " + currentParent.children.size());
		for(DspaceResearcherFolder f : currentParent.children)
		{
		    	buildFolderTree(doc, folder, f);
		}
		

		return folder;
		
	}
	
	
	/**
	 * Add the links to the folder element.
	 * 
	 * @param doc
	 * @param folder
	 * @param f
	 */
	private void addFolderLinks(Document doc, Element folder, DspaceResearcherFolder f)
	{
		
		if( f.links != null && f.links.size() > 0)
		{
			Element links = doc.createElement("links");
			for(ResearcherFolderLink l : f.links)
			{
				Element link = doc.createElement("link");
				
				Element title = doc.createElement("title");
				Text data = doc.createTextNode(l.title);
				title.appendChild(data);
				link.appendChild(title);
				
				Element description = doc.createElement("description");
				data = doc.createTextNode(l.description);
				description.appendChild(data);
				link.appendChild(description);
				
				Element linkType = doc.createElement("link_type");
				data = doc.createTextNode(l.linkType);
				linkType.appendChild(data);
				link.appendChild(linkType);
				
				Element url = doc.createElement("url");
				data = doc.createTextNode(l.url);
				url.appendChild(data);
				link.appendChild(url);
				
				Element folderId = doc.createElement("folder_id");
				data = doc.createTextNode(l.folderId + "");
				folderId.appendChild(data);
				link.appendChild(folderId);
				
				Element linkId = doc.createElement("link_id");
				data = doc.createTextNode(l.linkId + "");
				linkId.appendChild(data);
				link.appendChild(linkId);
				
				links.appendChild(link);
				
			}
			folder.appendChild(links);
		}
		
	}
	
	
	
	/**
	 * Default Implementation of the Community Exporter.
	 * @throws IOException 
	 * 
	 * @see edu.ur.dspace.export.CommunityExporter#exportCommunities(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public void  exportResearchers(String zipFileName, String xmlFilePath) throws IOException {
		 
        List<DspaceResearcher> researchers = this.getResearchers();
        
        List<ZipFileNameHelper> helpers = new LinkedList<ZipFileNameHelper>();
        
        File f = new File(xmlFilePath);
     	String path = f.getCanonicalPath();
		path = path + File.separatorChar +  XML_FILE_NAME;
		
		ZipFileNameHelper helper = new ZipFileNameHelper();
    	helper.absoluteFilePath = path;
    	helper.zipFileName = XML_FILE_NAME;
    	
    	helpers.add(helper);
		 
        generateResearcherXMLFile(new File(path),researchers);
        
        List<BitstreamFileInfo> bitstreams = this.getResearcherLogos(researchers);
        
        for(BitstreamFileInfo bitstream : bitstreams)
        {
        	helper = new ZipFileNameHelper();
        	helper.absoluteFilePath = bitstream.dspaceFileName;
        	helper.zipFileName = bitstream.newFileName;
        	helpers.add(helper);
        }
        
        FileZipperUtil.createZipFile(zipFileName, helpers);
        
		 
	}
	
	/**
	 * Map the data to a researcher
	 * 
	 * @author Nathan Sarr
	 *
	 */
	private static final class ResearcherMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	DspaceResearcher r = new DspaceResearcher();
	    	r.department = rs.getString("department");
	    	r.dspaceUserId = rs.getInt("eperson_id");
	    	r.fax = rs.getString("fax");
	    	r.field = rs.getString("field");
	    	r.pagePublic = rs.getBoolean("page_public");
	    	r.researcherId = rs.getInt("researcher_id");
	    	r.researchInterests = rs.getString("research_interests");
	    	r.teachingInterests = rs.getString("teaching_interests");
	    	r.title = rs.getString("title");
	    	r.logoId = rs.getLong("bitstream_id");
	    	r.campusLocation = rs.getString("campus_location");
	    	
	    	String link1name = rs.getString("web_site_1_name");
	    	if( link1name != null && !link1name.equals(""))
	    	{
	    		Link link1 = new Link();
	    	    link1.name = link1name;
	    	    link1.value = rs.getString("web_site_1_link");
	    	    r.links.add(link1);
	    	}
	    	
	    	String link2name = rs.getString("web_site_2_name");
	    	if( link1name != null && !link1name.equals(""))
	    	{
	    		Link link2 = new Link();
	    	    link2.name = link2name;
	    	    link2.value = rs.getString("web_site_2_link");
	    	    r.links.add(link2);
	    	}
	        return r;
	    }
	}
	
	/**
	 * Map the data to a researcher
	 * 
	 * @author Nathan Sarr
	 *
	 */
	private static final class ResearcherFolderMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	DspaceResearcherFolder f = new DspaceResearcherFolder();
	    	f.description = rs.getString("description");
	    	f.folderId = rs.getLong("folder_id");
	    	f.parentId = rs.getLong("parent_id");
	    	f.title = rs.getString("title");
	    	f.researcherId = rs.getLong("researcher_id");
	    	f.rightValue = rs.getLong("right_val");
	    	f.leftValue = rs.getLong("left_val");
	        return f;
	    }
	}
	
	/**
	 * Map the data to a researcher
	 * 
	 * @author Nathan Sarr
	 *
	 */
	private static final class ResearcherFolderLinkMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	ResearcherFolderLink link = new ResearcherFolderLink();
	    	link.description = rs.getString("description");
	    	link.folderId = rs.getLong("folder_id");
	    	link.linkId = rs.getLong("link_id");
	    	link.linkType = rs.getString("type_name");
	    	link.title = rs.getString("title");
	    	link.url = rs.getString("url");
	        return link;
	    }
	}
	
	


	public BitstreamFileLoader getBitstreamFileLoader() {
		return bitstreamFileLoader;
	}



	public void setBitstreamFileLoader(BitstreamFileLoader bitstreamFileLoader) {
		this.bitstreamFileLoader = bitstreamFileLoader;
	}

}
