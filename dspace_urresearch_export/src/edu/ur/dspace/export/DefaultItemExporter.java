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
import edu.ur.dspace.model.DspaceItem;
import edu.ur.dspace.model.DspaceItemMetadata;
import edu.ur.dspace.model.EpersonPermission;
import edu.ur.dspace.model.EpersonPermissionMapper;
import edu.ur.dspace.model.GroupPermission;
import edu.ur.dspace.model.GroupPermissionMapper;
import edu.ur.dspace.util.FileZipperUtil;
import edu.ur.dspace.util.ZipFileNameHelper;


/**
 * Exporter for dspace items
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultItemExporter implements ItemExporter{
	
	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(DefaultItemExporter.class);
	
	/** gets the file location of the file in dspace */
    private BitstreamFileLoader bitstreamFileLoader;
	
	/**
	 * Data source for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Export the items into the specified zip file 
	 * 
	 * @see edu.ur.dspace.export.ItemExporter#exportItems(java.lang.String, java.lang.String, java.util.Collection)
	 */
	public void exportItems(String zipFileName, String xmlFileDirectory,
			Collection<DspaceItem> items) throws IOException {
        
        List<ZipFileNameHelper> helpers = new LinkedList<ZipFileNameHelper>();
        
        File f = new File(xmlFileDirectory);
     	String path = f.getCanonicalPath();
		path = path + File.separatorChar +  XML_FILE_NAME;
		
		ZipFileNameHelper helper = new ZipFileNameHelper();
    	helper.absoluteFilePath = path;
    	helper.zipFileName = XML_FILE_NAME;
    	
    	helpers.add(helper);
		 
        generateItemXMLFile(new File(path),items);
        
        List<BitstreamFileInfo> bitstreams = new LinkedList<BitstreamFileInfo>();
        for( DspaceItem i : items)
        {
        	bitstreams.addAll(i.files);
        }
        
        for(BitstreamFileInfo bitstream : bitstreams)
        {
        	helper = new ZipFileNameHelper();
        	log.debug("dspace file name = " + bitstream.dspaceFileName + " zip file name = " + bitstream.newFileName );
        	helper.absoluteFilePath = bitstream.dspaceFileName;
        	helper.zipFileName = bitstream.newFileName;
        	helpers.add(helper);
        }
        
        FileZipperUtil.createZipFile(zipFileName, helpers);
		
	}

	/**
	 * Generate the XML file
	 * 
	 * @see edu.ur.dspace.export.ItemExporter#generateItemXMLFile(java.io.File, java.util.Collection)
	 */
	public void generateItemXMLFile(File f, Collection<DspaceItem> items)
			throws IOException {
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

		 Document doc = impl.createDocument(null, "items", null);
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
		 for(DspaceItem i : items)
		 {
			 log.debug("Exporting dspace item " + i);
			 Element item = doc.createElement("item");
			 
			 Element itemId = doc.createElement("dspace_item_id");
			 Text data = doc.createTextNode(i.itemId + "");
			 itemId.appendChild(data);
			 item.appendChild(itemId);
			 
			 Element inArchive = doc.createElement("in_archive");
			 data = doc.createTextNode(i.inArchive + "");
			 inArchive.appendChild(data);
			 item.appendChild(inArchive);
			 
			 Element submitterId = doc.createElement("submitter_id");
			 data = doc.createTextNode(i.submitterId + "");
			 submitterId.appendChild(data);
			 item.appendChild(submitterId);
			 
			 Element withdrawn = doc.createElement("withdrawn");
			 data = doc.createTextNode(i.withdrawn + "");
			 withdrawn.appendChild(data);
			 item.appendChild(withdrawn);
			 
			 if( i.collectionIds.size() > 0 )
			 {
				 Element collections = doc.createElement("collections");
				 item.appendChild(collections);
				 appendCollectionElements(collections, i, doc);
				 
			 }
			 else
			 {
				 throw new IllegalStateException("item " + i + " does not have any collections");
			 }
			 
			 
			 
			 if( i.files.size() > 0)
			 {
				 Element files = doc.createElement("files");
				 item.appendChild(files);
				 appendFileElements(files, i, doc);
			 }
			 
			 if( i.metadata.size() > 0 )
			 {
				 Element metadata = doc.createElement("metadata");
				 item.appendChild(metadata);
				 appendMetadataElements(metadata, i, doc);
			 }
			 
			 
			 
			 if( i.groupPermissions != null && i.groupPermissions.size() > 0 )
			 {
			     Element groupPermissions = doc.createElement("group_permissions");
			 
			     // add the links
			     for(GroupPermission p : i.groupPermissions)
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
			     item.appendChild(groupPermissions);
			 }
			 
			 if( i.epersonPermissions != null && i.epersonPermissions.size() > 0 )
			 {
			     Element epersonPermissions = doc.createElement("eperson_permissions");
			 
			     // add the links
			     for(EpersonPermission p : i.epersonPermissions)
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
			     item.appendChild(epersonPermissions);
			 }
			 
			 root.appendChild(item);
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
	 * Removes invalid XML characters.
	 * 
	 * @param str
	 * @return the clean string or null if the string passed in is null
	 */
	private String cleanString(String str)
	{
		if( str != null)
		{
			char[] characters = str.toCharArray();
		    for(int index = 0; index < characters.length; index++)
		    {
		    	if(Character.getType(str.charAt(index)) == Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE )
		    	{
		    		characters[index] = ' ';
		    	}
		    }
		    return new String(characters);
		}
		
		return null;
		
		
		
	}
	
	/**
	 * Append the file elements to the xml files
	 * 
	 * @param parent
	 * @param i
	 */
	private void appendFileElements(Element parent, DspaceItem i, Document doc)
	{
		for(BitstreamFileInfo file : i.files)
		{
			 Element fileElement = doc.createElement("file");
			 parent.appendChild(fileElement);
			 
			 Element bitstreamId = doc.createElement("bitstream_id");
			 Text data = doc.createTextNode(file.bitstreamId + "");
			 bitstreamId.appendChild(data);
			 fileElement.appendChild(bitstreamId);
			 
			 Element originalFileName = doc.createElement("original_file_name");
			 data = doc.createTextNode(file.originalFileName);
			 originalFileName.appendChild(data);
			 fileElement.appendChild(originalFileName);
			 
			 Element newFileName = doc.createElement("new_file_name");
			 
			 if( file.extension != null )
			 {
				 file.newFileName = file.bitstreamId + "." + file.extension;
			 }
			 else
			 {
				 file.newFileName = file.bitstreamId +"";
				 
			 }
			 data = doc.createTextNode(file.newFileName);
			 newFileName.appendChild(data);
			 fileElement.appendChild(newFileName);
			 
			 if( file.groupPermissions != null && file.groupPermissions.size() > 0 )
			 {
			     Element groupPermissions = doc.createElement("group_permissions");
			 
			     // add the links
			     for(GroupPermission p : file.groupPermissions)
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
			     fileElement.appendChild(groupPermissions);
			 }
			 
			 if( file.epersonPermissions != null && file.epersonPermissions.size() > 0 )
			 {
			     Element epersonPermissions = doc.createElement("eperson_permissions");
			 
			     // add the links
			     for(EpersonPermission p : file.epersonPermissions)
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
			     fileElement.appendChild(epersonPermissions);
			 }
		}
	}
	
	/**
	 * Append the collection elements to the xml files
	 * 
	 * @param parent
	 * @param i
	 */
	private void appendCollectionElements(Element parent, DspaceItem i, Document doc)
	{
		for(Long id : i.collectionIds)
		{
			 Element collectionElement = doc.createElement("collection");
			 parent.appendChild(collectionElement);
			 
			 Element collectionId = doc.createElement("collection_id");
			 Text data = doc.createTextNode(id + "");
			 collectionId.appendChild(data);
			 collectionElement.appendChild(collectionId);
		}
	}
	
	
	/**
	 * Append the file elements to the xml files
	 * 
	 * @param parent
	 * @param i
	 */
	private void appendMetadataElements(Element parent, DspaceItem i, Document doc)
	{
		for(DspaceItemMetadata meta : i.metadata)
		{
			 Element metadataElement = doc.createElement("metadata_element");
			 parent.appendChild(metadataElement);
			 
			 Element label = doc.createElement("label");
			 Text data = doc.createTextNode(cleanString(meta.label));
			 label.appendChild(data);
			 metadataElement.appendChild(label);
			 
			 Element value = doc.createElement("value");
			 data = doc.createTextNode(cleanString(meta.value));
			 value.appendChild(data);
			 metadataElement.appendChild(value);
		}
	}
	

	/**
	 * Get all items in the repository.
	 * 
	 *  
	 * @see edu.ur.dspace.export.ItemExporter#getItems()
	 */
	@SuppressWarnings("unchecked")
	public List<DspaceItem> getItems() throws IOException {
		log.debug("get items called");
		List<DspaceItem> items = jdbcTemplate.query( "select * from item where in_archive = true", new ItemMapper());
		getCollections(items);
		getItemMetadata(items);
		getItemFiles(items);
		
		for(DspaceItem i : items)
		{
            String groupPermissionsSelect = "select * from resourcepolicy where resource_type_id = 2 and epersongroup_id is not null and resource_id = " + i.itemId;
            String epersonPermissionsSelect = "select * from resourcepolicy where resource_type_id = 2 and eperson_id is not null and resource_id = " + i.itemId;
		 
            i.groupPermissions = jdbcTemplate.query( groupPermissionsSelect, new GroupPermissionMapper());
            i.epersonPermissions = jdbcTemplate.query( epersonPermissionsSelect, new EpersonPermissionMapper());
		}
		return items;
	}
	
	/**
	 * Get the item metadata in name/value pairs.
	 * 
	 * @param items
	 */
	@SuppressWarnings("unchecked")
	private void getItemMetadata(List<DspaceItem> items)
	{
		for( DspaceItem i : items)
		 {
			 List<DspaceItemMetadata> metadata = jdbcTemplate.query( "select * from dcvalue, dctyperegistry where " +
			 		"dcvalue.item_id = " + i.itemId + "and dcvalue.dc_type_id = dctyperegistry.dc_type_id", new ItemMetadataMapper());
		     i.metadata = metadata;
		 }
	}
	
	/**
	 * Load the bitstreams for an item.
	 * 
	 * @param items
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void getItemFiles(List<DspaceItem> items) throws IOException
	{
		for( DspaceItem i : items)
		 {
			 List<Long> bitstreamIds = jdbcTemplate.query( "select bitstream.bitstream_id from item, " +
			 		"item2bundle, bundle, bundle2bitstream, " +
			 		"bitstream where item.item_id = " + i.itemId + " and " +
			 		"item.item_id = item2bundle.item_id " +
			 		"and item2bundle.bundle_id = bundle.bundle_id " +
			 		"and bundle.bundle_id = bundle2bitstream.bundle_id " +
			 		"and bundle2bitstream.bitstream_id = bitstream.bitstream_id", new BitstreamIdMapper());
		     
			
			 for(Long id : bitstreamIds)
		     {
		    	 i.files.add(bitstreamFileLoader.getBitstreamFileInfo(id));
		     }
		 }
	}
	
	/**
	 * Get the collections this item is in.  For some reason the item is somtimes listed twice in the database with the same
	 * collectin id
	 * 
	 * @param items
	 */
	@SuppressWarnings("unchecked")
	private void getCollections(List<DspaceItem> items)
	{
		for(DspaceItem i : items)
		{
		    i.collectionIds = jdbcTemplate.query("select distinct(collection_id) from collection2item where " +
		 		    "item_id = " + i.itemId, new CollectionIdMapper());
		}
	}

	/**
	 * Get items for the specified range.
	 * 
	 * @see edu.ur.dspace.export.ItemExporter#getItems(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<DspaceItem> getItems(int startId, int stopId) throws IOException {
		List<DspaceItem> items = jdbcTemplate.query( "select * from item where in_archive =" +
				" true and item.item_id between " 
				+ startId + " and " + stopId , new ItemMapper());
		getItemMetadata(items);
		getItemFiles(items);
		getCollections(items);
		
		for(DspaceItem i : items)
		{
            String groupPermissionsSelect = "select * from resourcepolicy where resource_type_id = 2 and epersongroup_id is not null and resource_id = " + i.itemId;
            String epersonPermissionsSelect = "select * from resourcepolicy where resource_type_id = 2 and eperson_id is not null and resource_id = " + i.itemId;
		 
            i.groupPermissions = jdbcTemplate.query( groupPermissionsSelect, new GroupPermissionMapper());
            i.epersonPermissions = jdbcTemplate.query( epersonPermissionsSelect, new EpersonPermissionMapper());
		}
		
		return items;
	}
	
	
	
	/**
	 * Map the data to an item
	 * 
	 * @author Nathan Sarr
	 *
	 */
	private static final class ItemMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	DspaceItem i = new DspaceItem();
	    	i.inArchive = rs.getBoolean("in_archive");
	    	i.itemId = rs.getLong("item_id");
	    	i.submitterId = rs.getLong("submitter_id");
	    	i.withdrawn = rs.getBoolean("withdrawn");
	        return i;
	    }
	}
	
	/**
	 * Map the collection ids
	 * 
	 * @author Nathan Sarr
	 *
	 */
	private static final class CollectionIdMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Long id =  rs.getLong("collection_id");
	        return id;
	    }
	}
	
	/**
	 * Map the data to an item
	 * 
	 * @author Nathan Sarr
	 *
	 */
	private static final class BitstreamIdMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Long l = rs.getLong("bitstream_id");
	        return l;
	    }
	}
	
	/**
	 * Map the data to a dspace item metadata
	 * 
	 * @author Nathan Sarr
	 *
	 */
	private static final class ItemMetadataMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	DspaceItemMetadata m= new DspaceItemMetadata();
	    	m.dcValueId = rs.getLong("dc_value_id");
	    	m.itemId = rs.getLong("item_id");
	    	m.label = rs.getString("label");
	    	m.value = rs.getString("text_value");
	        return m;
	    }
	}

	public BitstreamFileLoader getBitstreamFileLoader() {
		return bitstreamFileLoader;
	}

	public void setBitstreamFileLoader(BitstreamFileLoader bitstreamFileLoader) {
		this.bitstreamFileLoader = bitstreamFileLoader;
	}

	
	/**
	 * Get the maximum item id.
	 * 
	 * @see edu.ur.dspace.export.ItemExporter#getLastItemId()
	 */
	public int getLastItemId() {
		return jdbcTemplate.queryForInt("select max(item_id) from item");
	}

}
