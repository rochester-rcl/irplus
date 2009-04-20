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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

import edu.ur.dspace.model.DspaceCollection;
import edu.ur.dspace.model.EpersonPermission;
import edu.ur.dspace.model.GroupPermission;
import edu.ur.dspace.model.PermissionConstants;
import edu.ur.dspace.util.FileZipperUtil;
import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.transformer.BasicThumbnailTransformer;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.user.UserService;

/**
 * Service for importing DSpace collections into the ur-research system.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultCollectionImporter implements CollectionImporter{
	
	/**  Service for dealing with institutional collections */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Set of security related services */
	private InstitutionalCollectionSecurityService institutionalCollectionSecurityService;
	
	/** Service for dealing with repositories */
	private RepositoryService repositoryService;
	
	/** Thumb-nailer  for logo files*/
	private BasicThumbnailTransformer defaultThumbnailTransformer;
	
	/** Temporary file creator to allow a temporary file to be created for processing */
	private TemporaryFileCreator temporaryFileCreator;

	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(DefaultCommunityImporter.class);
	
	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;
	
	/** Service for dealing with users */
	private UserService userService;
	
	/** Service to deal with user groups */
	private UserGroupService userGroupService;

	
	/**
	 * Data source for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	
	/**
	 * Import the collection information into the urresearch system.
	 * 
	 * @see edu.ur.dspace.load.CollectionImporter#ImportCollections(java.lang.String, edu.ur.ir.repository.Repository)
	 */
	public void ImportCollections(String zipFile, Repository repository) throws IOException,
			DuplicateNameException, IllegalFileSystemNameException 
	{
		 File zip = new File(zipFile);
		 if(!zip.exists())
		 {
			throw new IllegalStateException("File " +  zipFile + " does not exist");
		 }
		 
		 File communityXmlFile = FileZipperUtil.getZipEntry(zip, "collection.xml", "loaded-collection.xml");
		 
         List<DspaceCollection> collections = getCollections(communityXmlFile);
		 
		 loadCollections(collections, zip, repository);
		
	}
 
	
	/**
	 * Load the dspace collection information from the xml file.
	 * 
	 * @see edu.ur.dspace.load.CollectionImporter#getCollections(java.io.File)
	 */
	public List<DspaceCollection> getCollections(File communityXmlFile)
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
		 LSInput lsIn = domLs.createLSInput();
		 LSParser parser = domLs.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
		 
		 lsIn.setEncoding("UTF-8");

		 FileInputStream fileInputStream;
		 try {
			fileInputStream = new FileInputStream(communityXmlFile);
		 } catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		 }
		 InputStreamReader inputStreamReader;
		 try {
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
		 } catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		 }
		 lsIn.setCharacterStream(inputStreamReader);
		 
		 
		 Document doc = parser.parse(lsIn);
		 Element root = doc.getDocumentElement();
		 
		 NodeList nodeList = root.getChildNodes();
		 LinkedList<DspaceCollection> collections = new LinkedList<DspaceCollection>();
		 
		 for( int index = 0; index < nodeList.getLength(); index++)
		 {
	         Node child = nodeList.item(index);
	         collections.add(getCollection(child));
	     }
		 
		 return collections;
	}
	
	/**
	 * Create a collection from the xml data.
	 * 
	 * @param node - root collection node
	 * @return
	 */
	private DspaceCollection getCollection(Node node)
	{
		DspaceCollection c = new DspaceCollection();
		
        NodeList children = node.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
            if( child.getNodeName().equals("name"))
            {
            	c.name = child.getTextContent();
            }
            else if( child.getNodeName().equals("id"))
            {
            	c.id= new Long(child.getTextContent());
            }
            else if( child.getNodeName().equals("introductory_text"))
            {
            	c.introductoryText = child.getTextContent();
            }
            else if( child.getNodeName().equals("side_bar_text"))
            {
            	c.sideBarText = child.getTextContent();
            }
            else if( child.getNodeName().equals("copyright"))
            {
            	c.copyright = child.getTextContent();
            }
            else if( child.getNodeName().equals("logo_file_name"))
            {
            	c.logoFileName = child.getTextContent();
            }
            else if( child.getNodeName().equals("community_id"))
            {
            	c.communityId = new Long(child.getTextContent());
            }
            else if( child.getNodeName().equals("group_permissions"))
            {
            	getGroupPermissions(c, child);
            }
            else if( child.getNodeName().equals("eperson_permissions"))
            {
            	getEpersonPermissions(c, child);
            }
            else if( child.getNodeName().equals("subscribers"))
            {
            	getSubscribers(c, child);
            }
           
            
        }
		return c;
	}
	
	/**
	 * Get the group permissions for a collection out of xml node
	 * 
	 * @param c - dspace collection to load the permissions into
	 * @param groupPermissionsNode - xml data containing the group permissions
	 */
	private void getGroupPermissions(DspaceCollection c, Node groupPermissionsNode)
	{
		NodeList children = groupPermissionsNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node groupPermission = children.item(index);
        	c.groupPermissions.add(getGroupPermission(groupPermission));
        }
	}
	
	/**
	 * Create a group permission from a individual permission node
	 * 
	 * @param groupPermissionsNode - xml data containing the group permissions
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
	 * Create a list of user ids that have subscribed to the collection
	 * 
	 * @param subscribersNode - xml data containing the subscribers
	 */
	private void getSubscribers(DspaceCollection c, Node subscribersNode)
	{
		List<Long> userIds = new LinkedList<Long>();
		
		NodeList children = subscribersNode.getChildNodes();
		for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
        	if( child.getNodeName().equals("user_id"))
        	{
        		Long userId  = new Long(child.getTextContent());
        		userIds.add(userId);
        	}
        }
		c.subscriberUserIds = userIds;
		
	}
	
	/**
	 * Get the eperson permissions for a collection out of xml document
	 * 
	 * @param c - dspace collection to put the permissions in
	 * @param epersonPermissionsNode - xml data containing the eperson permissions
	 */
	private void getEpersonPermissions(DspaceCollection c, Node epersonPermissionsNode)
	{
		NodeList children = epersonPermissionsNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node epersonPermission = children.item(index);
        	c.epersonPermissions.add(getEpersonPermission(epersonPermission));
        }
	}
	
	/**
	 * Create an eperson permission from a individual permission node
	 * 
	 * @param epersonPermissionNode - xml node containing eperson permissions
	 * @return - created EpersonPermission
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
	 * Load the communities into the new urresearch system.
	 * 
	 * @param collections - set of collections to load
	 * @param zipFile - zip file containing logo's
	 * @param repo - repository to store files in
	 * @throws IOException 
	 * @throws IllegalFileSystemNameException
	 */
	private void loadCollections(List<DspaceCollection> collections, File zipFile, Repository repo) throws IOException, IllegalFileSystemNameException
	{
		for(DspaceCollection c : collections)
		{
			long urresearchParentCollectionId = this.getUrResearchParent(c.communityId);
			InstitutionalCollection parentCollection = institutionalCollectionService.getCollection(urresearchParentCollectionId, true);
			
			if(parentCollection == null)
			{
				System.out.println("could not import dspace collection " + c);
			}
			else
			{
				InstitutionalCollection urresearchCollection;
				try {
					urresearchCollection = parentCollection.createChild(c.name);
				
				    urresearchCollection.setDescription(c.introductoryText);
				    urresearchCollection.setCopyright(c.copyright);
				    institutionalCollectionService.saveCollection(urresearchCollection);
				    jdbcTemplate.execute("insert into dspace_convert.collection(dspace_collection_id, ur_research_collection_id) values (" + c.id + "," + urresearchCollection.getId() + ")");
				    if( c.logoFileName != null && !c.logoFileName.trim().equals(""))
				    {
				        File f = FileZipperUtil.getZipEntry(zipFile, c.logoFileName, "logoFile" );
				        System.out.println("logo file name = " + c.logoFileName);
				        IrFile logo = repositoryService.createIrFile(repo, f, c.logoFileName, "imported dspace logo");
				        System.out.println("file extension = " + logo.getFileInfo().getExtension());
				
				        urresearchCollection.setPrimaryPicture(logo);
				        try {
						    ThumbnailHelper.thumbnailFile(logo, repo, defaultThumbnailTransformer, temporaryFileCreator, repositoryService, "community logo thumbnail");
					    } catch (Exception e) {
						    log.debug("could not create thumbnail for collection " + c, e);
					    }
				    }
				    loadGroupPermissions(urresearchCollection, c);
					loadEpersonPermissions(urresearchCollection, c);
					loadSubscribers(urresearchCollection, c);
				} catch (DuplicateNameException e1) {
					log.debug("could not create collection " + c + " in parent " + 
							parentCollection + " because a collection with the name " + c.name + " already exists ", e1);
				}
			}
			
		}
	}
	
	/**
	 * Load the group permissions
	 * 
	 * @param collection - urresearch system collection to transfer the permissions to
	 * @param c - old dspace collection containing old permissions
	 */
	private void loadGroupPermissions(InstitutionalCollection collection, DspaceCollection c)
	{
		log.debug("loading group permissions for institutional collection " + collection);
		// colleciton is publicly viewable if the dspace collection is 
		collection.setPubliclyViewable(PermissionConstants.hasAnonymousRead(c.groupPermissions));
				
		
		for(GroupPermission groupPermission : c.groupPermissions)
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
				    
				    if( groupPermission.action == PermissionConstants.ADD)
				    {
				        institutionalCollectionSecurityService.givePermission(collection, userGroup, 
				        		InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION);
				    }
				    else if ( groupPermission.action == PermissionConstants.DEFAULT_BITSTREAM_READ)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.DEFAULT_ITEM_READ)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.DELETE)
				    {
				    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
				        		InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
				    }
				    else if ( groupPermission.action == PermissionConstants.READ)
				    {
				    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
				        		InstitutionalCollectionSecurityService.VIEW_PERMISSION);
				    }
				    else if ( groupPermission.action == PermissionConstants.REMOVE)
				    {
				    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
				        		InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
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
				    else if ( groupPermission.action == PermissionConstants.WRITE)
				    {
				    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
				        		InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
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
	 * Load the subscribers.
	 * 
	 * @param collection - urresearch system collection to transfer the subscribers to
	 * @param c - old dspace collection containing old permissions
	 */
	private void loadSubscribers(InstitutionalCollection collection, DspaceCollection c)
	{
		log.debug("loading eperson permissions for institutional collection " + collection);
		
		for(Long userId : c.subscriberUserIds)
		{
			// get the user id
			Long urresearchUserId = getUrResearchUser(userId);
			if( urresearchUserId != null )
			{
			    IrUser user = userService.getUser(urresearchUserId, false);
			    collection.addSuscriber(user);
			}
			else
			{
				log.error("user id " + userId + " not found ");
			}
		}
		   
	}
	
	/**
	 * Get the new urresearch group id based on the dspace group id.
	 * 
	 * @param dspacdGroupId - current dspace group id
	 * @return id of the urresearch group id
	 */
	private Long getUrGroupId(long dspaceGroupId)
	{
		return jdbcTemplate.queryForLong("select ur_research_group_id from dspace_convert.ir_group where dspace_group_id = " + dspaceGroupId);
	}
	
	/**
	 * Load the eperson permission.
	 * 
	 * @param collection - urresearch system collection to transfer the permissions to
	 * @param c - old dspace collection containing old permissions
	 */
	private void loadEpersonPermissions(InstitutionalCollection collection, DspaceCollection c)
	{
		log.debug("loading eperson permissions for institutional collection " + collection);
		for(EpersonPermission epersonPermission : c.epersonPermissions)
		{
			log.debug("loading epersonPermission  " + epersonPermission);
			Long urresearchUserId = getUrResearchUser(epersonPermission.epersonId);

			if (urresearchUserId == null) {
				log.error("User for eperson id " + epersonPermission.epersonId
						+ " could not be found");
			} 
			else 
			{

				IrUser user = userService.getUser(urresearchUserId, false);

				if (user == null) {
					throw new IllegalStateException(
							"Could not find user with id " + urresearchUserId);
				}

				String userGroupName = user.getUsername() + "_group";

				IrUserGroup userGroup = userGroupService.get(userGroupName);

				if (userGroup == null) {
					userGroup = new IrUserGroup(userGroupName);
					userGroup
							.setDescription("Created as user import permssion from dspace");
					userGroupService.save(userGroup);
				}

				if (epersonPermission.action == PermissionConstants.ADD) {
					institutionalCollectionSecurityService
							.givePermission(
									collection,
									userGroup,
									InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION);
				} else if (epersonPermission.action == PermissionConstants.DEFAULT_BITSTREAM_READ) {
					// no action
				} else if (epersonPermission.action == PermissionConstants.DEFAULT_ITEM_READ) {
					// no action
				} else if (epersonPermission.action == PermissionConstants.DELETE) {
					institutionalCollectionSecurityService
							.givePermission(
									collection,
									userGroup,
									InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
				} else if (epersonPermission.action == PermissionConstants.READ) {
					institutionalCollectionSecurityService
							.givePermission(
									collection,
									userGroup,
									InstitutionalCollectionSecurityService.VIEW_PERMISSION);
				} else if (epersonPermission.action == PermissionConstants.REMOVE) {
					institutionalCollectionSecurityService
							.givePermission(
									collection,
									userGroup,
									InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
				} else if (epersonPermission.action == PermissionConstants.WORKFLOW_ABORT) {
					// no action
				} else if (epersonPermission.action == PermissionConstants.WORKFLOW_STEP_1) {
					// no action
				} else if (epersonPermission.action == PermissionConstants.WORKFLOW_STEP_2) {
					// no action
				} else if (epersonPermission.action == PermissionConstants.WORKFLOW_STEP_3) {
					// no action
				} else if (epersonPermission.action == PermissionConstants.WRITE) {
					institutionalCollectionSecurityService
							.givePermission(
									collection,
									userGroup,
									InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
				}
			}
		}
	}

	/**
	 * Get the urresearch user id for the current dspace eperson id.  The takes the old dspace eperson id
	 * 
	 * @param id of the old dspace system eperson id
	 * @return  the urresearch user id in new system
	 */
	private Long getUrResearchUser(long epersonId)
	{
		try
		{
		    return jdbcTemplate.queryForLong("select ur_research_user_id from dspace_convert.ir_user where dspace_eperson_id = " + epersonId);
	 
		}
		catch(EmptyResultDataAccessException erdae)
		{
		    return null;	
		}
	}
	
	/**
	 * Get the urresearch parent id for the current dspace collection.  The takes the old dspace communiyt parent
	 * 
	 * @param dspaceCommunityParent
	 * @return the id of the current urresearch parent
	 */
	private Long getUrResearchParent(long dspaceCommunityParent)
	{
		return jdbcTemplate.queryForLong("select ur_research_collection_id from dspace_convert.community where dspace_community_id = " + dspaceCommunityParent);
	}



	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}



	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}



	public RepositoryService getRepositoryService() {
		return repositoryService;
	}



	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
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



	public InstitutionalCollectionSecurityService getInstitutionalCollectionSecurityService() {
		return institutionalCollectionSecurityService;
	}



	public void setInstitutionalCollectionSecurityService(
			InstitutionalCollectionSecurityService institutionalCollectionSecurityService) {
		this.institutionalCollectionSecurityService = institutionalCollectionSecurityService;
	}



	public UserService getUserService() {
		return userService;
	}



	public void setUserService(UserService userService) {
		this.userService = userService;
	}



	public UserGroupService getUserGroupService() {
		return userGroupService;
	}



	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}
	


}
