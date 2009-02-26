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

package edu.ur.dspace.load;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

import edu.ur.dspace.model.BitstreamFileInfo;
import edu.ur.dspace.model.DspaceItem;
import edu.ur.dspace.model.DspaceItemMetadata;
import edu.ur.dspace.model.EpersonPermission;
import edu.ur.dspace.model.GroupPermission;
import edu.ur.dspace.model.PermissionConstants;

import edu.ur.dspace.util.FileZipperUtil;
import edu.ur.ir.FileSystem;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.transformer.BasicThumbnailTransformer;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemFileSecurityService;
import edu.ur.ir.item.ItemSecurityService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.user.UserService;


/**
 * Item importer for dspace items.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultItemImporter implements ItemImporter{
	
	/** Service for dealing with repositories */
	private RepositoryService repositoryService;
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(DefaultItemImporter.class);
	
	/**  Deals with getting the generic item information */
	private GenericItemPopulator genericItemPopulator;
	
	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;
	
	/** Service for dealing with institutional collections */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Thumb-nailer  for logo files*/
	private BasicThumbnailTransformer defaultThumbnailTransformer;
	
	/** Temporary file creator to allow a temporary file to be created for processing */
	private TemporaryFileCreator temporaryFileCreator;
	
	/** Service for dealing with institutional items */
	private InstitutionalItemService institutionalItemService;
	
	/** Service for indexing items */
	private InstitutionalItemIndexService institutionalItemIndexService;
	
	/** Service to deal with user groups */
	private UserGroupService userGroupService;
	
	/** Securty service for items */
	private ItemSecurityService itemSecurityService;
	
	/** Security service for item files */
	private ItemFileSecurityService itemFileSecurityService;
	
	/** Service for dealing with users */
	private UserService userService;
	
	/**
	 * Data source for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Get the dspace items
	 * 
	 * @see edu.ur.dspace.load.ItemImporter#getItems(java.io.File)
	 */
	public List<DspaceItem> getItems(File itemXmlFile) throws IOException {
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder;
		 LinkedList<DspaceItem> items = new LinkedList<DspaceItem>();
		 
		 try {
			builder = factory.newDocumentBuilder();
		 } catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		 }
		 
		 

		 DOMImplementation impl = builder.getDOMImplementation();
		 DOMImplementationLS domLs = (DOMImplementationLS)impl.getFeature("LS" , "3.0");
		 LSInput lsIn = domLs.createLSInput();
		 LSParser parser = domLs.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
		 lsIn.setEncoding("UTF-8");
		 FileInputStream fileInputStream = null;
		 InputStreamReader inputStreamReader = null;
		 
	     try
		 {
		     try {
			    fileInputStream = new FileInputStream(itemXmlFile);
		     } catch (FileNotFoundException e) {
			    throw new IllegalStateException(e);
		     }
		     
		     try {
			    inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
		     } catch (UnsupportedEncodingException e) {
			    throw new IllegalStateException(e);
		     }
		     lsIn.setCharacterStream(inputStreamReader);
		
		 
		 
		     Document doc = parser.parse(lsIn);
		     Element root = doc.getDocumentElement();
		 
		     NodeList nodeList = root.getChildNodes();
		 
		 
		     for( int index = 0; index < nodeList.getLength(); index++)
		     {
	            Node child = nodeList.item(index);
	             items.add(getItem(child));
	         }
		 }
		 finally
		 {
			 if( inputStreamReader != null )
			 {
				 inputStreamReader.close();
				 inputStreamReader = null;
			 }
			 if( fileInputStream != null)
			 {
				 fileInputStream.close();
				 fileInputStream = null;
			 }
		 }
		 return items;
		 
		 
	}
	
	/**
	 * Create an item from the xml data.
	 * 
	 * @param node - root items node
	 * @return the created dspace item
	 */
	private DspaceItem getItem(Node node)
	{
		DspaceItem i = new DspaceItem();
		
        NodeList children = node.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	
        	Node child = children.item(index);
            if( child.getNodeName().equals("dspace_item_id"))
            {
            	i.itemId = new Long(child.getTextContent());
            }
            else if( child.getNodeName().equals("in_archive"))
            {
            	i.inArchive = new Boolean(child.getTextContent());
            }
            else if( child.getNodeName().equals("submitter_id"))
            {
            	i.submitterId = new Long(child.getTextContent());
            }
            else if( child.getNodeName().equals("withdrawn"))
            {
            	i.withdrawn = new Boolean(child.getTextContent());
            }
            else if( child.getNodeName().equals("collections"))
            {
            	NodeList parentCollections = child.getChildNodes();
        		for( int collectionIndex = 0; collectionIndex < parentCollections.getLength(); collectionIndex++)
                {
        			i.collectionIds.add(getCollection(parentCollections.item(collectionIndex)));
                }
            }
            else if( child.getNodeName().equals("files"))
            {
            	NodeList childrenFiles = child.getChildNodes();
        		for( int fileIndex = 0; fileIndex < childrenFiles.getLength(); fileIndex++)
                {
        			i.files.add(getFile(childrenFiles.item(fileIndex)));
                }
        		
            }
            else if( child.getNodeName().equals("metadata"))
            {
            	NodeList childrenMetadata = child.getChildNodes();
        		for( int metadataIndex = 0; metadataIndex < childrenMetadata.getLength(); metadataIndex++)
                {
        			i.metadata.add(getMetadata(childrenMetadata.item(metadataIndex)));
                }
            }
            else if( child.getNodeName().equals("group_permissions"))
            {
            	getGroupPermissions(i, child);
            }
            else if( child.getNodeName().equals("eperson_permissions"))
            {
            	getEpersonPermissions(i, child);
            }
            
            
        }
		return i;
	}
	
	/**
	 * File information in the xml file for the item.
	 * 
	 * @param node - xml node containing the file info
	 * @return the created bitstream file info 
	 */
	private  BitstreamFileInfo getFile(Node node)
	{
		BitstreamFileInfo file = new BitstreamFileInfo();
	    NodeList children = node.getChildNodes();
	    for( int index = 0; index < children.getLength(); index++)
	    {
	        Node child = children.item(index);
	        if( child.getNodeName().equals("bitstream_id"))
	        {
	           file.bitstreamId = new Long(child.getTextContent());
	        }
	        else if (child.getNodeName().equals("original_file_name"))
	        {
	           file.originalFileName = child.getTextContent();
	        }
	        else if (child.getNodeName().equals("new_file_name"))
	        {
	           file.newFileName = child.getTextContent();
	        }
	        else if( child.getNodeName().equals("group_permissions"))
            {
            	getGroupPermissions(file, child);
            }
            else if( child.getNodeName().equals("eperson_permissions"))
            {
            	getEpersonPermissions(file, child);
            }
	    }
		return file;
	}
	
	/**
	 * Get the group permissions for a file 
	 * 
	 * @param info - file info object to populate
	 * @param group permission node
	 */
	private void getGroupPermissions(BitstreamFileInfo info, Node groupPermissionsNode)
	{
		NodeList children = groupPermissionsNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node groupPermission = children.item(index);
        	info.groupPermissions.add(getGroupPermission(groupPermission));
        }
	}
	
	/**
	 * Get the group permissions for an item
	 * 
	 * @param i - the dspace item
	 * @param group permission node
	 */
	private void getGroupPermissions(DspaceItem i, Node groupPermissionsNode)
	{
		NodeList children = groupPermissionsNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node groupPermission = children.item(index);
        	i.groupPermissions.add(getGroupPermission(groupPermission));
        }
	}
	
	/**
	 * Create a group permission from a individual permission node
	 * @param groupPermissionNode - node containing the group permissions
	 * @return - group permission object created
	 */
	private GroupPermission getGroupPermission(Node groupPermissionNode)
	{
		GroupPermission groupPermission = new GroupPermission();
		
		NodeList children = groupPermissionNode.getChildNodes();
		for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
        	if( child.getNodeName().equals("action_id"))
        	{
        		groupPermission.action = new Integer(child.getTextContent()).intValue();
        	}
        	else if(child.getNodeName().equals("group_id") )
        	{
        		groupPermission.groupId = new Long(child.getTextContent());
        	}
        }
		return groupPermission;
	}
	
	/**
	 * Get the group permissions for a file info object
	 * 
	 * @param info - file info object
	 * @param epersonPermissionsNode - xml node containging the eperson permissions data
	 */
	private void getEpersonPermissions(BitstreamFileInfo info, Node epersonPermissionsNode)
	{
		NodeList children = epersonPermissionsNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node epersonPermission = children.item(index);
        	info.epersonPermissions.add(getEpersonPermission(epersonPermission));
        }
	}
	
	/**
	 * Get the group permissions for an item
	 * 
	 * @param i - dspace item
	 * @param epersonPermissionsNode - xml node containing the eperson permissions
 	 */
	private void getEpersonPermissions(DspaceItem i, Node epersonPermissionsNode)
	{
		NodeList children = epersonPermissionsNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node epersonPermission = children.item(index);
        	i.epersonPermissions.add(getEpersonPermission(epersonPermission));
        }
	}
	
	/**
	 * Create a group permission from a individual permission node
	 * 
	 * @param epersonPermissionNode - xml node containing permissions
	 * @return - the created eperson permission
	 */
	private EpersonPermission getEpersonPermission(Node epersonPermissionNode)
	{
		EpersonPermission epersonPermission = new EpersonPermission();
		
		NodeList children = epersonPermissionNode.getChildNodes();
		for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
        	if( child.getNodeName().equals("action_id"))
        	{
        		epersonPermission.action = new Integer(child.getTextContent()).intValue();
        	}
        	else if(child.getNodeName().equals("group_id") )
        	{
        		epersonPermission.epersonId = new Long(child.getTextContent());
        	}
        }
		return epersonPermission;
	}
	
	
	/**
	 * Get the collection id out of the node
	 * 
	 * @param node - xml node containg the collection id
	 * @return the collection id
	 */
	private  Long getCollection(Node node)
	{
        Long id = null;
	    NodeList children = node.getChildNodes();
	    for( int index = 0; index < children.getLength(); index++)
	    {
	        Node child = children.item(index);
	        if( child.getNodeName().equals("collection_id"))
	        {
	           id = new Long(child.getTextContent());
	        }
	    }
		return id;
	}
	
	/**
	 * Load the item meatadata from the xml file.
	 * 
	 * @param node - parent metadata_element node in label/value pairs
	 * @return the dspace item metadata 
	 */
	private  DspaceItemMetadata getMetadata(Node node)
	{
		DspaceItemMetadata metadata = new DspaceItemMetadata();
	    NodeList children = node.getChildNodes();
	    for( int index = 0; index < children.getLength(); index++)
	    {
	        Node child = children.item(index);
	        if( child.getNodeName().equals("label"))
	        {
	            metadata.label = child.getTextContent();
	        }
	        else if (child.getNodeName().equals("value"))
	        {
	        	metadata.value = child.getTextContent();
	        }
	        
	    }
		return metadata;
	}

	/**
	 * Import the items into the repository.
	 * @throws NoIndexFoundException 
	 * 
	 * @see edu.ur.dspace.load.ItemImporter#importItems(java.lang.String, edu.ur.ir.repository.Repository)
	 */
	public void importItems(String zipFile, Repository repository, boolean publicOnly)
			throws IOException, NoIndexFoundException {
		log.debug("Processing zip file " + zipFile);
		 File zip = new File(zipFile);
		 if(!zip.exists())
		 {
			throw new IllegalStateException("File " +  zipFile + " does not exist");
		 }
		 
		 File itemXmlFile = FileZipperUtil.getZipEntry(zip, "item.xml", "loaded-item.xml");
		 
         List<DspaceItem> items = getItems(itemXmlFile);
		 
		 loadItems(items, zip, repository, publicOnly);
		
	}
	
	/**
	 * Load the items into the new urresearch system.
	 * 
	 * @param items - items to load into the system
	 * @param zipFile - zip file containing the information
	 * @param repo - repository to use for information storage
	 * 
	 * @throws IOException
	 * @throws NoIndexFoundException
	 */
	private void loadItems(List<DspaceItem> items, File zipFile, Repository repo, boolean publicOnly) throws IOException, NoIndexFoundException
	{
		for(DspaceItem i : items)
		{
			log.debug("Processing dspace item " + i);
			if( publicOnly && !PermissionConstants.hasAnonymousRead(i.groupPermissions) )
			{
				log.debug("Loading only public items item and " + i + " is not public ");
			}
			else
			{
				log.debug("*********************** Loading ****************************");
				log.debug("loading item " + i);
				loadItem(i, zipFile, repo, publicOnly);
				log.debug("*********************** Done Loading Loading ****************************\n\n");
			}
		}
	}
	
	
	/**
	 * Load a single file 
	 * @param i
	 * @param zipFile
	 * @param repo
	 * @throws NoIndexFoundException
	 * @throws IOException
	 */
	private void loadItem(DspaceItem i,  File zipFile, Repository repo, boolean publicOnly) throws NoIndexFoundException, IOException
	{
		GenericItem genericItem = genericItemPopulator.createGenericItem(repo, i);
		List<InstitutionalCollection> urResearchCollections = getCollections(i);
		
		log.debug("Found " + urResearchCollections.size() + " collections for item");
		
		if( urResearchCollections.size() == 0 )
		{
			log.debug("No collections found for dspace item " + i);
		}
		else
		{
			LinkedList<ItemFileBitstreamInfo> itemFileBitstreamInfos = new LinkedList<ItemFileBitstreamInfo>(); 
			for(BitstreamFileInfo bfi : i.files)
			{
				
				if( publicOnly && !PermissionConstants.hasAnonymousRead(bfi.groupPermissions) )
				{
					log.debug("Only loading public files and " + bfi + " is not pblic ");
				}
				else
				{
				    File f = FileZipperUtil.getZipEntry(zipFile, bfi.newFileName, "contentFile" );
				    IrFile irFile = null;
				    try {
				    	for(int index = 0; index < FileSystem.INVALID_CHARACTERS.length; index++) {
							if (bfi.originalFileName.contains(Character.toString(FileSystem.INVALID_CHARACTERS[index]))) {
								bfi.originalFileName = bfi.originalFileName.replace(FileSystem.INVALID_CHARACTERS[index],'_');
							}
						}
					    irFile = repositoryService.createIrFile(repo, f, bfi.originalFileName, "imported dspace item file");
					    ItemFile itemFile = genericItem.addFile(irFile);
					    itemFileBitstreamInfos.add(new ItemFileBitstreamInfo(itemFile, bfi));
					    if( defaultThumbnailTransformer.canTransform(irFile.getFileInfo().getExtension()))
					    {
						    try 
						    {
							    ThumbnailHelper.thumbnailFile(irFile, repo, defaultThumbnailTransformer, 
									temporaryFileCreator, repositoryService, "thumbnail of item");
							    genericItem.addPrimaryImageFile(itemFile);
						    } 
						    catch (Exception e) 
						    {
						        log.debug("Could not thubmnail file " + irFile, e);
						    }
						
					    }
				    } catch (IllegalFileSystemNameException e) {
					    log.debug("Could not add file " + irFile, e);
				    }
				    
				}
			}
		    for(InstitutionalCollection ic : urResearchCollections)
		    {
			    InstitutionalItem institutionalItem = ic.createInstitutionalItem(genericItem);
			    institutionalItemService.saveInstitutionalItem(institutionalItem);
			    institutionalItemIndexService.addItem(institutionalItem, new File(repo.getInstitutionalItemIndexFolder().getFullPath()));
			    jdbcTemplate.execute("insert into dspace_convert.item(dspace_item_id, ur_research_institutional_item_id) values (" + i.itemId + "," + institutionalItem.getId() + ")");

		    }
		    loadGroupPermissions(genericItem, i);
		    loadEpersonPermissions(genericItem, i);
		    for(ItemFileBitstreamInfo ifbi : itemFileBitstreamInfos)
		    {
		    	loadGroupPermissions(ifbi.itemFile, ifbi.bitstreamFileInfo);
		    	loadEpersonPermissions(ifbi.itemFile, ifbi.bitstreamFileInfo);
		    	jdbcTemplate.execute("insert into dspace_convert.item_file(dspace_bitstream_id, ur_research_institutional_item_file_id) values (" + ifbi.itemFile.getId() + "," + ifbi.bitstreamFileInfo.bitstreamId + ")");
		    }
		}
	}

	
	/**
	 * Get the new urresearch collection id.
	 * 
	 * @param dspaceCollectionId - current dspace collection id
	 * @return - the urresearchsystem collection id
	 */
	private Long getUrCollectionId(long dspaceCollectionId)
	{
		return jdbcTemplate.queryForLong("select ur_research_collection_id from dspace_convert.collection where dspace_collection_id = " + dspaceCollectionId);
	}
	
	/**
	 * Returns the list of urresearch collections for the dspace item.  This allows
	 * for items to be within multiple collections
	 * 
	 * @param dspaceItem
	 * @return - found list of collections or an empty list
	 */
	private List<InstitutionalCollection> getCollections(DspaceItem dspaceItem)
	{
		log.debug("Getting collections");
		// deal with collections
		List<InstitutionalCollection> collections = new LinkedList<InstitutionalCollection>();
		for( Long id : dspaceItem.collectionIds)
		{
			long urResearchCollectionId = this.getUrCollectionId(id);
			InstitutionalCollection collection = institutionalCollectionService.getCollection(urResearchCollectionId, false);
		    
			if( collection != null )
			{
			    collections.add(collection);
			}
			else
			{
				log.debug("Could not find collection with dspace_id = " + id );
			}
		}
		
		return collections;
	}
	
	/**
	 * Load the group permissions
	 * 
	 * @param genericItem - urresearch system item to transfer the permissions to
	 * @param dspaceItem - related dspace item containing the permissions
	 */
	private void loadGroupPermissions(GenericItem genericItem, DspaceItem dspaceItem)
	{
		log.debug("loading group permissions for generic item " + genericItem);
		
		//urresearch system institutional item is publicly viewable if the dspace item is
		genericItem.setPubliclyViewable(PermissionConstants.hasAnonymousRead(dspaceItem.groupPermissions));
				
		
		for(GroupPermission groupPermission : dspaceItem.groupPermissions)
		{
			log.debug("loading groupPermission  " + groupPermission);
			if( !groupPermission.groupId.equals(PermissionConstants.ANONYMOUS_GROUP_ID))
			{
				// get the group we want to add permissions to
			    Long urreseachGroupId = getUrGroupId(groupPermission.groupId);
			    if( urreseachGroupId != null)
			    {
				    IrUserGroup userGroup = userGroupService.get(urreseachGroupId, false);
				    if( userGroup == null)
				    {
				    	throw new IllegalStateException("Could not find user group for id  " + urreseachGroupId);
				    }
				    
				    if( groupPermission.action == PermissionConstants.ADD || groupPermission.action == PermissionConstants.DELETE || 
				    		groupPermission.action == PermissionConstants.REMOVE || groupPermission.action == PermissionConstants.WRITE)
				    {
				    	itemSecurityService.assignItemPermission(ItemSecurityService.ITEM_FILE_EDIT_PERMISSION, userGroup, genericItem);
				    	itemSecurityService.assignItemPermission(ItemSecurityService.ITEM_METADATA_EDIT_PERMISSION, userGroup, genericItem);
				    	itemSecurityService.assignItemPermission(ItemSecurityService.ITEM_METADATA_READ_PERMISSION, userGroup, genericItem);
				    }
				    else if ( groupPermission.action == PermissionConstants.DEFAULT_BITSTREAM_READ)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.DEFAULT_ITEM_READ || groupPermission.action == PermissionConstants.READ)
				    {
				    	itemSecurityService.assignItemPermission(ItemSecurityService.ITEM_METADATA_READ_PERMISSION, userGroup, genericItem);
				    }
				   
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_ABORT)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_STEP_1)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_STEP_2)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_STEP_3)
				    {
				    	// no action
				    }  
			    }
			    else
			    {
				    throw new IllegalStateException("Could not find user group for permission " + groupPermission);
			    }
			}
		}
	}
	

	
	/**
	 * Load the eperson permission.
	 * 
     * @param genericItem - urresearch system item to transfer the permissions to
	 * @param dspaceItem - related dspace item containing the permissions
	 */
	private void loadEpersonPermissions(GenericItem genericItem, DspaceItem dspaceItem)
	{
		log.debug("loading eperson permissions for generic item " + genericItem);
		for(EpersonPermission epersonPermission : dspaceItem.epersonPermissions)
		{
			log.debug("loading epersonPermission  " + epersonPermission);
			Long urresearchUserId = getUrResearchUser(epersonPermission.epersonId);
			
			
			if( urresearchUserId == null )
			{
				throw new IllegalStateException("User for eperson id " +
						epersonPermission.epersonId + " could not be found" );
			}
			
			IrUser user = userService.getUser(urresearchUserId, false);
			
			if( user == null)
			{
				throw new IllegalStateException("Could not find user with id " + urresearchUserId);
			}
			
			String userGroupName = user.getUsername() + "_group";
			
			IrUserGroup userGroup = userGroupService.get(userGroupName);
			
			if( userGroup == null)
			{
			    userGroup = new IrUserGroup(userGroupName);	
			    userGroup.setDescription("Created as user import permssion from dspace");
			    userGroupService.save(userGroup);
			}
			
			if( epersonPermission.action == PermissionConstants.ADD || epersonPermission.action == PermissionConstants.DELETE || 
					epersonPermission.action == PermissionConstants.REMOVE || epersonPermission.action == PermissionConstants.WRITE)
		    {
		    	itemSecurityService.assignItemPermission(ItemSecurityService.ITEM_FILE_EDIT_PERMISSION, userGroup, genericItem);
		    	itemSecurityService.assignItemPermission(ItemSecurityService.ITEM_METADATA_EDIT_PERMISSION, userGroup, genericItem);
		    	itemSecurityService.assignItemPermission(ItemSecurityService.ITEM_METADATA_READ_PERMISSION, userGroup, genericItem);
		    }
		    else if ( epersonPermission.action == PermissionConstants.DEFAULT_BITSTREAM_READ)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.DEFAULT_ITEM_READ || epersonPermission.action == PermissionConstants.READ)
		    {
		    	itemSecurityService.assignItemPermission(ItemSecurityService.ITEM_METADATA_READ_PERMISSION, userGroup, genericItem);
		    }
		   
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_ABORT)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_STEP_1)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_STEP_2)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_STEP_3)
		    {
		    	// no action
		    }  
		}
	}
	
	/**
	 * Load the group permissions for an individual file
	 * 
	 * @param itemFile - urresearch system item file 
	 * @param fileInfo - old dspace file info permissions
	 */
	private void loadGroupPermissions(ItemFile itemFile, BitstreamFileInfo fileInfo)
	{
		log.debug("loading group permissions for item file " + itemFile);
		
		//urresearch system file is publicly viewable if the dspace file is
		itemFile.setPublic(PermissionConstants.hasAnonymousRead(fileInfo.groupPermissions));
				
		
		for(GroupPermission groupPermission : fileInfo.groupPermissions)
		{
			log.debug("loading groupPermission  " + groupPermission);
			if( !groupPermission.groupId.equals(PermissionConstants.ANONYMOUS_GROUP_ID))
			{
				// get the group we want to add permissions to
			    Long urreseachGroupId= getUrGroupId(groupPermission.groupId);
			    if( urreseachGroupId != null)
			    {
				    IrUserGroup userGroup = userGroupService.get(urreseachGroupId, false);
				    if( userGroup == null)
				    {
				    	throw new IllegalStateException("Could not find user group for id  " + urreseachGroupId);
				    }
				    
				    if( groupPermission.action == PermissionConstants.ADD || groupPermission.action == PermissionConstants.DELETE || 
				    		groupPermission.action == PermissionConstants.REMOVE || groupPermission.action == PermissionConstants.WRITE)
				    {
				    	itemSecurityService.assignItemPermission(ItemSecurityService.ITEM_FILE_EDIT_PERMISSION, userGroup, itemFile.getItem());
				    	itemFileSecurityService.assignItemFilePermission(ItemFileSecurityService.ITEM_FILE_READ_PERMISSION, userGroup, itemFile);
				    }
				    else if ( groupPermission.action == PermissionConstants.DEFAULT_BITSTREAM_READ)
				    {
				    	itemFileSecurityService.assignItemFilePermission(ItemFileSecurityService.ITEM_FILE_READ_PERMISSION, userGroup, itemFile);
				    }
				    else if ( groupPermission.action == PermissionConstants.DEFAULT_ITEM_READ  )
				    {
				    }
				    else if( groupPermission.action == PermissionConstants.READ)
				    {
				    	itemFileSecurityService.assignItemFilePermission(ItemFileSecurityService.ITEM_FILE_READ_PERMISSION, userGroup, itemFile);
				    }		   
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_ABORT)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_STEP_1)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_STEP_2)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_STEP_3)
				    {
				    	// no action
				    }  
			    }
			    else
			    {
				    throw new IllegalStateException("Could not find user group for permission " + groupPermission);
			    }
			}
		}
	}
	
	/**
	 * Get the new urresearch group id based on the dspace group id.
	 * 
	 * @param dspaceGroupId - current dspace group id
	 * @return id of the urresearch group id
	 */
	private Long getUrGroupId(long dspaceGroupId)
	{
		return jdbcTemplate.queryForLong("select ur_research_group_id from dspace_convert.ir_group where dspace_group_id = " + dspaceGroupId);
	}
	
	/**
	 * Load the eperson permission.
	 * 
	 * @param itemFile - urresearch system item file 
	 * @param fileInfo - old dspace file info permissions
	 */
	private void loadEpersonPermissions(ItemFile itemFile, BitstreamFileInfo fileInfo)
	{
		log.debug("loading eperson permissions for item file" + itemFile);
		for(EpersonPermission epersonPermission : fileInfo.epersonPermissions)
		{
			log.debug("loading epersonPermission  " + epersonPermission);
			Long urresearchUserId = getUrResearchUser(epersonPermission.epersonId);
			
			
			if( urresearchUserId == null )
			{
				throw new IllegalStateException("User for eperson id " +
						epersonPermission.epersonId + " could not be found" );
			}
			
			IrUser user = userService.getUser(urresearchUserId, false);
			
			if( user == null)
			{
				throw new IllegalStateException("Could not find user with id " + urresearchUserId);
			}
			
			String userGroupName = user.getUsername() + "_group";
			
			IrUserGroup userGroup = userGroupService.get(userGroupName);
			
			if( userGroup == null)
			{
			    userGroup = new IrUserGroup(userGroupName);	
			    userGroup.setDescription("Created as user import permssion from dspace");
			    userGroupService.save(userGroup);
			}
			
		    if( epersonPermission.action == PermissionConstants.ADD || epersonPermission.action == PermissionConstants.DELETE || 
		    		epersonPermission.action == PermissionConstants.REMOVE || epersonPermission.action == PermissionConstants.WRITE)
		    {
		    	itemSecurityService.assignItemPermission(ItemSecurityService.ITEM_FILE_EDIT_PERMISSION, userGroup, itemFile.getItem());
		    	itemFileSecurityService.assignItemFilePermission(ItemFileSecurityService.ITEM_FILE_READ_PERMISSION, userGroup, itemFile);
		    }
		    else if ( epersonPermission.action == PermissionConstants.DEFAULT_BITSTREAM_READ)
		    {
		    	itemFileSecurityService.assignItemFilePermission(ItemFileSecurityService.ITEM_FILE_READ_PERMISSION, userGroup, itemFile);
		    }
		    else if ( epersonPermission.action == PermissionConstants.DEFAULT_ITEM_READ  )
		    {
		    }
		    else if( epersonPermission.action == PermissionConstants.READ)
		    {
		    	itemFileSecurityService.assignItemFilePermission(ItemFileSecurityService.ITEM_FILE_READ_PERMISSION, userGroup, itemFile);
		    }		   
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_ABORT)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_STEP_1)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_STEP_2)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_STEP_3)
		    {
		    	// no action
		    }  
		}
	}

	/**
	 * Get the urresearch user id for the current dspace eperson id.  The takes the old dspace eperson id
	 * 
	 * @param dspaceCommunityParent
	 * @return
	 */
	private Long getUrResearchUser(long epersonId)
	{
		return jdbcTemplate.queryForLong("select ur_research_user_id from dspace_convert.ir_user where dspace_eperson_id = " + epersonId);
	}
	
	

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public GenericItemPopulator getGenericItemPopulator() {
		return genericItemPopulator;
	}

	public void setGenericItemPopulator(GenericItemPopulator genericItemPopulator) {
		this.genericItemPopulator = genericItemPopulator;
	}

	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	public BasicThumbnailTransformer getDefaultThumbnailTransformer() {
		return defaultThumbnailTransformer;
	}

	public void setDefaultThumbnailTransformer(
			BasicThumbnailTransformer defaultThumbnailTransformer) {
		this.defaultThumbnailTransformer = defaultThumbnailTransformer;
	}

	public TemporaryFileCreator getTemporaryFileCreator() {
		return temporaryFileCreator;
	}

	public void setTemporaryFileCreator(TemporaryFileCreator temporaryFileCreator) {
		this.temporaryFileCreator = temporaryFileCreator;
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public InstitutionalItemIndexService getInstitutionalItemIndexService() {
		return institutionalItemIndexService;
	}

	public void setInstitutionalItemIndexService(
			InstitutionalItemIndexService institutionalItemIndexService) {
		this.institutionalItemIndexService = institutionalItemIndexService;
	}

	public UserGroupService getUserGroupService() {
		return userGroupService;
	}

	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

	public ItemSecurityService getItemSecurityService() {
		return itemSecurityService;
	}

	public void setItemSecurityService(ItemSecurityService itemSecurityService) {
		this.itemSecurityService = itemSecurityService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ItemFileSecurityService getItemFileSecurityService() {
		return itemFileSecurityService;
	}

	public void setItemFileSecurityService(
			ItemFileSecurityService itemFileSecurityService) {
		this.itemFileSecurityService = itemFileSecurityService;
	}
	
	/**
	 * Conatiner class to match up item files with their bitstream file info classes.
	 * 
	 * @author nathans
	 *
	 */
	class ItemFileBitstreamInfo
	{
		private ItemFile itemFile;
		private BitstreamFileInfo bitstreamFileInfo;
		
		ItemFileBitstreamInfo(ItemFile itemFile, BitstreamFileInfo bitstreamFileInfo)
		{
			this.itemFile = itemFile;
			this.bitstreamFileInfo = bitstreamFileInfo;
		}

		public ItemFile getItemFile() {
			return itemFile;
		}

		public BitstreamFileInfo getBitstreamFileInfo() {
			return bitstreamFileInfo;
		}

	}



}
