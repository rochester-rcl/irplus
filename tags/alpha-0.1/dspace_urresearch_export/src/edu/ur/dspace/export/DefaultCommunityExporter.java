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
import edu.ur.dspace.model.Community;
import edu.ur.dspace.model.EpersonPermission;
import edu.ur.dspace.model.EpersonPermissionMapper;
import edu.ur.dspace.model.GroupPermission;
import edu.ur.dspace.model.GroupPermissionMapper;
import edu.ur.dspace.model.Link;
import edu.ur.dspace.util.FileZipperUtil;
import edu.ur.dspace.util.ZipFileNameHelper;

/**
 * Default implementation for communities.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultCommunityExporter implements CommunityExporter{
	
    /** gets the file location of the file in dspace */
    private BitstreamFileLoader bitstreamFileLoader;


	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;
	
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
	public List<Community> getCommunities() throws IOException
	{
		 List<Community> communities = jdbcTemplate.query( "select * from community", new CommunityMapper());
		 
		 for(Community c : communities)
		 {
		     if( c.logoId != null && c.logoId > 0)
		     {
			     System.out.println(c.logoId);
			     BitstreamFileInfo info = bitstreamFileLoader.getBitstreamFileInfo(c.logoId);
		         File bitstreamFile = new File(info.dspaceFileName);
		         info.newFileName = "community_" + c.id + "." + info.extension;
		         System.out.println(bitstreamFile.getCanonicalPath());
		         System.out.println("file name = " + info.newFileName);
		         c.logoFileInfo = info;
		     }
		     
             c.links = jdbcTemplate.query( "select * from community_link where community_id = " + c.id, new CollectionLinkMapper());
             String groupPermissionsSelect = "select * from resourcepolicy where resource_type_id = 4 and epersongroup_id is not null and resource_id = " + c.id;
             String epersonPermissionsSelect = "select * from resourcepolicy where resource_type_id = 4 and eperson_id is not null and resource_id = " + c.id;
		 
             c.groupPermissions = jdbcTemplate.query( groupPermissionsSelect, new GroupPermissionMapper());
             c.epersonPermissions = jdbcTemplate.query( epersonPermissionsSelect, new EpersonPermissionMapper());

		 }
		 
		 return communities;
	}
	
	/**
	 * Get the logos for the communities
	 * 
	 * @param communities
	 * @return
	 */
	public List<BitstreamFileInfo> getCommunityLogos(Collection<Community> communities)
	{
		LinkedList<BitstreamFileInfo> logos = new LinkedList<BitstreamFileInfo>();
		// create XML for the communities
		 for(Community c : communities)
		 {
			 if( c.logoFileInfo != null)
			 {
				 logos.add(c.logoFileInfo);
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
	public void generateCommunityXMLFile(File f, Collection<Community> communities) throws IOException
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

		 Document doc = impl.createDocument(null, "communities", null);
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
		 for(Community c : communities)
		 {
			 Element community = doc.createElement("community");
			 Element id = doc.createElement("id");
			 Text data = doc.createTextNode(c.id.toString());
			 id.appendChild(data);
			 community.appendChild(id);
			 
			 Element name = doc.createElement("name");
			 data = doc.createTextNode(c.name);
			 name.appendChild(data);
			 community.appendChild(name);
			 
			 Element introductoryText = doc.createElement("introductory_text");
			 data = doc.createTextNode(c.introductoryText);
			 introductoryText.appendChild(data);
			 community.appendChild(introductoryText);
			 
			 Element sideBarText = doc.createElement("side_bar_text");
			 data = doc.createTextNode(c.sideBarText);
			 sideBarText.appendChild(data);
			 community.appendChild(sideBarText);
			 
			 Element copyright = doc.createElement("copyright");
			 data = doc.createTextNode(c.copyright);
			 copyright.appendChild(data);
			 community.appendChild(copyright);
			 
			 Element logoFileName = null;
			 if( c.logoFileInfo != null)
			 {
			     logoFileName  = doc.createElement("logo_file_name");
				 data = doc.createTextNode(c.logoFileInfo.newFileName);
				 logoFileName.appendChild(data);
				 community.appendChild(logoFileName);
			 }
			 
			 root.appendChild(community);
			 
			 if( c.links != null && c.links.size() > 0 )
			 {
			     Element links = doc.createElement("links");
			 
			     // add the links
			     for(Link l : c.links)
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
			     community.appendChild(links);
			 }
			 
			 if( c.groupPermissions != null && c.groupPermissions.size() > 0 )
			 {
			     Element groupPermissions = doc.createElement("group_permissions");
			 
			     // add the links
			     for(GroupPermission p : c.groupPermissions)
			     {
				     Element groupPermission = doc.createElement("group_permission");
				     
				     Element permissionAction = doc.createElement("action_id");
				     data = doc.createTextNode(p.action + "");
				     permissionAction.appendChild(data);
				     groupPermission.appendChild(permissionAction);
				 
				     Element groupId = doc.createElement("group_id");
				     data = doc.createTextNode(p.groupId.toString());
				     groupId.appendChild(data);
				     groupPermission.appendChild(groupId);
				     
				     groupPermissions.appendChild(groupPermission);
			     }
			     community.appendChild(groupPermissions);
			 }
			 
			 if( c.epersonPermissions != null && c.epersonPermissions.size() > 0 )
			 {
			     Element epersonPermissions = doc.createElement("eperson_permissions");
			 
			     // add the links
			     for(EpersonPermission p : c.epersonPermissions)
			     {
				     Element epersonPermission = doc.createElement("eperson_permission");
				     
				     Element permissionAction = doc.createElement("action_id");
				     data = doc.createTextNode(p.action + "");
				     permissionAction.appendChild(data);
				     epersonPermission.appendChild(permissionAction);
				 
				     Element epersonId = doc.createElement("eperson_id");
				     data = doc.createTextNode(p.epersonId.toString());
				     epersonId.appendChild(data);
				     epersonPermission.appendChild(epersonId);
				     
				     epersonPermissions.appendChild(epersonPermission);
			     }
			     community.appendChild(epersonPermissions);
			 }
			 
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
	 * Default Implementation of the Community Exporter.
	 * @throws IOException 
	 * 
	 * @see edu.ur.dspace.export.CommunityExporter#exportCommunities(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public void  exportCommunities(String zipFileName, String xmlFilePath) throws IOException {
		 
        List<Community> communities = this.getCommunities();
        
        List<ZipFileNameHelper> helpers = new LinkedList<ZipFileNameHelper>();
        
        File f = new File(xmlFilePath);
     	String path = f.getCanonicalPath();
		path = path +  File.separatorChar + XML_FILE_NAME;
		
		ZipFileNameHelper helper = new ZipFileNameHelper();
    	helper.absoluteFilePath = path;
    	helper.zipFileName = XML_FILE_NAME;
    	
    	helpers.add(helper);
		 
        generateCommunityXMLFile(new File(path), communities);
        
        List<BitstreamFileInfo> bitstreams = this.getCommunityLogos(communities);
        
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
	 * Map the data to a community
	 * 
	 * @author Nathan Sarr
	 *
	 */
	private static final class CommunityMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Community community = new Community();
	    	community.id = new Long(rs.getInt("community_id"));
	        community.copyright = rs.getString("copyright_text");
	        community.shortDescription = rs.getString("short_description");
	        community.name = rs.getString("name");
	        community.introductoryText = rs.getString("introductory_text");
	        community.sideBarText = rs.getString("side_bar_text");
	        community.logoId = rs.getLong("logo_bitstream_id");
	        return community;
	    }
	}
	
	
	/**
	 * Map the data to a community link.
	 * 
	 * @author Nathan Sarr
	 *
	 */
	private static final class CollectionLinkMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Link link = new Link();
	    	link.name = rs.getString("name");
	    	link.value = rs.getString("link");
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
