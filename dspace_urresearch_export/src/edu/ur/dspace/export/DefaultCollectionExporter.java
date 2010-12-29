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
import edu.ur.dspace.model.DspaceCollection;
import edu.ur.dspace.model.EpersonPermission;
import edu.ur.dspace.model.EpersonPermissionMapper;
import edu.ur.dspace.model.GroupPermission;
import edu.ur.dspace.model.GroupPermissionMapper;
import edu.ur.dspace.util.FileZipperUtil;
import edu.ur.dspace.util.ZipFileNameHelper;

/**
 * Default implementation of Collection Exporter.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultCollectionExporter implements CollectionExporter{
	
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
	 * Get the logos for the communities
	 * 
	 * @param communities
	 * @return
	 */
	public List<BitstreamFileInfo> getCollectionLogos(Collection<DspaceCollection> collections)
	{
		LinkedList<BitstreamFileInfo> logos = new LinkedList<BitstreamFileInfo>();
		// create XML for the communities
		 for(DspaceCollection c : collections)
		 {
			 if( c.logoFileInfo != null)
			 {
				 logos.add(c.logoFileInfo);
			 }
		 }
		return logos;
	}
	
	/**
	 * Export the collections to the specified zip file name.
	 * 
	 * @see edu.ur.dspace.export.CollectionExporter#exportCollections(java.lang.String, java.lang.String)
	 */
	public void exportCollections(String zipFileName, String xmlFileDirectory)
			throws IOException {
		
	       List<DspaceCollection> collections = this.getCollections();
	        
	        List<ZipFileNameHelper> helpers = new LinkedList<ZipFileNameHelper>();
	        
	        File f = new File(xmlFileDirectory);
	     	String path = f.getCanonicalPath();
			path = path +   File.separatorChar  + XML_FILE_NAME;
			
			ZipFileNameHelper helper = new ZipFileNameHelper();
	    	helper.absoluteFilePath = path;
	    	helper.zipFileName = XML_FILE_NAME;
	    	
	    	helpers.add(helper);
			 
	        generateCollectionXMLFile(new File(path), collections);
	        
	        List<BitstreamFileInfo> bitstreams = this.getCollectionLogos(collections);
	        
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
	 * Generate an xml file with the specified collections.
	 * 
	 * @see edu.ur.dspace.export.CollectionExporter#generateCollectionXMLFile(java.io.File, java.util.Collection)
	 */
	public void generateCollectionXMLFile(File f,
			Collection<DspaceCollection> collections) throws IOException {
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

		 Document doc = impl.createDocument(null, "collections", null);
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
		 for(DspaceCollection c : collections)
		 {
			 Element collection = doc.createElement("collection");
			 Element id = doc.createElement("id");
			 Text data = doc.createTextNode(c.id.toString());
			 id.appendChild(data);
			 collection.appendChild(id);
			 			 
			 Element name = doc.createElement("name");
			 data = doc.createTextNode(c.name);
			 name.appendChild(data);
			 collection.appendChild(name);
			 
			 Element introductoryText = doc.createElement("introductory_text");
			 data = doc.createTextNode(c.introductoryText);
			 introductoryText.appendChild(data);
			 collection.appendChild(introductoryText);
			 
			 Element sideBarText = doc.createElement("side_bar_text");
			 data = doc.createTextNode(c.sideBarText);
			 sideBarText.appendChild(data);
			 collection.appendChild(sideBarText);
			 
			 Element copyright = doc.createElement("copyright");
			 data = doc.createTextNode(c.copyright);
			 copyright.appendChild(data);
			 collection.appendChild(copyright);
			 
			 Element communityId = doc.createElement("community_id");
			 data = doc.createTextNode(c.communityId + "");
			 communityId.appendChild(data);
			 collection.appendChild(communityId);
			 
			 Element logoFileName = null;
			 if( c.logoFileInfo != null)
			 {
			     logoFileName  = doc.createElement("logo_file_name");
				 data = doc.createTextNode(c.logoFileInfo.newFileName);
				 logoFileName.appendChild(data);
				 collection.appendChild(logoFileName);
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
			     collection.appendChild(groupPermissions);
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
			     collection.appendChild(epersonPermissions);
			 }
			 
			 // handle subscribers
			 if( c.subscriberUserIds.size() > 0 )
			 {
			     Element subscribers = doc.createElement("subscribers");
			 
			     // add the links
			     for(Long userId: c.subscriberUserIds)
			     {
				     Element user = doc.createElement("user_id");
				     data = doc.createTextNode(userId + "");
				     user.appendChild(data);
				     subscribers.appendChild(user);
			     }
			     collection.appendChild(subscribers);
			 }
			 root.appendChild(collection);
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
	 * Get the collections from the database.
	 * 
	 * @see edu.ur.dspace.export.CollectionExporter#getCollections()
	 */
	@SuppressWarnings("unchecked")
	public List<DspaceCollection> getCollections() throws IOException {
		
		 List<DspaceCollection> collections = jdbcTemplate.query( "select * from community2collection, collection where community2collection.collection_id = collection.collection_id", new CollectionMapper());
		 
		 for(DspaceCollection c : collections)
		 {
		     if( c.logoId != null && c.logoId > 0)
		     {
			     System.out.println(c.logoId);
			     BitstreamFileInfo info = bitstreamFileLoader.getBitstreamFileInfo(c.logoId);
		         File bitstreamFile = new File(info.dspaceFileName);
		         info.newFileName = "collection_" + c.id + "." + info.extension;
		         System.out.println(bitstreamFile.getCanonicalPath());
		         System.out.println("file name = " + info.newFileName);
		         c.logoFileInfo = info;
		     }
		     
             String groupPermissionsSelect = "select * from resourcepolicy where resource_type_id = 3 and epersongroup_id is not null and resource_id = " + c.id;
             String epersonPermissionsSelect = "select * from resourcepolicy where resource_type_id = 3 and eperson_id is not null and resource_id = " + c.id;
             String subscribersSelect = "select eperson_id from subscription where  collection_id = " + c.id;
		 
             c.groupPermissions = jdbcTemplate.query( groupPermissionsSelect, new GroupPermissionMapper());
             c.epersonPermissions = jdbcTemplate.query( epersonPermissionsSelect, new EpersonPermissionMapper());
		     c.subscriberUserIds = jdbcTemplate.queryForList(subscribersSelect, Long.class);
		 }
		 
		 return collections;
	}
	
	/**
	 * Map the data to a collection
	 * 
	 * @author Nathan Sarr
	 *
	 */
	private static final class CollectionMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	DspaceCollection collection = new DspaceCollection();
	    	collection.communityId = Long.valueOf(rs.getInt("community_id"));
	    	collection.id = Long.valueOf(rs.getInt("collection_id"));
	        collection.copyright = rs.getString("copyright_text");
	        collection.shortDescription = rs.getString("short_description");
	        collection.name = rs.getString("name");
	        collection.introductoryText = rs.getString("introductory_text");
	        collection.sideBarText = rs.getString("side_bar_text");
	        collection.logoId = rs.getLong("logo_bitstream_id");
	        return collection;
	    }
	}
	

	
	public BitstreamFileLoader getBitstreamFileLoader() {
		return bitstreamFileLoader;
	}

	public void setBitstreamFileLoader(BitstreamFileLoader bitstreamFileLoader) {
		this.bitstreamFileLoader = bitstreamFileLoader;
	}

	

}
